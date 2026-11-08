package nozzle;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class Utf8NamesTest {
    private static final String SUPPLEMENTARY = "\uD83D\uDE80";
    private static final String APPLICATION = "\u65E5\u672C-app-" + SUPPLEMENTARY;

    private static void assertInvalidArgument(String message, org.junit.jupiter.api.function.Executable operation) {
        NozzleException error = assertThrowsExactly(NozzleException.class, operation);
        assertEquals(ErrorCode.INVALID_ARGUMENT, error.errorCode());
        assertEquals(message, error.getMessage());
    }

    @Test
    void malformedNamesAndApplicationsAreRejectedThroughJni() {
        for (String bad : new String[] {"a\0b", "a\uD800", "a\uDC00", "\uD800x"}) {
            String message = bad.indexOf('\0') >= 0 ? "embedded NUL in name or application name"
                : "unpaired surrogate in name or application name";
            assertInvalidArgument(message, () -> {
                try (Sender ignored = Sender.create(new SenderDesc(bad, APPLICATION, 3, false))) {}
            });
            assertInvalidArgument(message, () -> {
                try (Sender ignored = Sender.create(new SenderDesc("valid", bad, 3, false))) {}
            });
            assertInvalidArgument(message, () -> {
                try (Receiver ignored = Receiver.create(new ReceiverDesc(bad, APPLICATION, ReceiveMode.LATEST_ONLY))) {}
            });
            assertInvalidArgument(message, () -> {
                try (Receiver ignored = Receiver.create(new ReceiverDesc("valid", bad, ReceiveMode.LATEST_ONLY))) {}
            });
        }
        NozzleException tooLong = assertThrows(NozzleException.class, () -> {
            try (Sender ignored = Sender.create(new SenderDesc("a".repeat(64), APPLICATION, 3, false))) {}
        });
        assertEquals(ErrorCode.INVALID_ARGUMENT, tooLong.errorCode());
    }

    @Test
    void malformedNativeBytesUseTheRuntimeErrorContract() throws ReflectiveOperationException {
        // Codec-level negative control; this does not simulate corrupted core IPC.
        java.lang.reflect.Method decode = NozzleNative.class.getDeclaredMethod("decodeUtf8", byte[].class);
        decode.setAccessible(true);
        for (byte[] bad : new byte[][] {{(byte)0xff}, {(byte)0xed, (byte)0xa0, (byte)0x80}}) {
            assertInvalidArgument("invalid UTF-8 returned by native code", () -> {
                try {
                    decode.invoke(null, (Object)bad);
                } catch (java.lang.reflect.InvocationTargetException error) {
                    throw error.getCause();
                }
            });
        }
    }

    @Test
    void nativeNonBmpNamesRoundTripAt63Bytes() {
        assumeFalse(System.getProperty("os.name").startsWith("Linux"),
            "SKIP: Linux default backend unavailable; malformed JNI controls run separately");
        String prefix = "j252-" + ProcessHandle.current().pid() + "-";
        String name = prefix + "a".repeat(56 - prefix.length()) + "\u65E5" + SUPPLEMENTARY;
        assertEquals(63, name.getBytes(StandardCharsets.UTF_8).length);
        try (Sender sender = Sender.create(new SenderDesc(name, APPLICATION, 3, false))) {
            SenderInfo own = sender.info();
            assertEquals(name, own.name());
            assertEquals(APPLICATION, own.applicationName());
            SenderInfo found = Arrays.stream(Nozzle.enumerateSenders())
                .filter(info -> own.id().equals(info.id())).findFirst().orElseThrow();
            assertEquals(name, found.name());
            assertEquals(APPLICATION, found.applicationName());
            try (Receiver receiver = Receiver.create(new ReceiverDesc(name, APPLICATION, ReceiveMode.LATEST_ONLY))) {
                ConnectedSenderInfo connected = receiver.connectedInfo();
                assertEquals(name, connected.name());
                assertEquals(APPLICATION, connected.applicationName());
                assertEquals(own.id(), connected.id());
            }
            NozzleException tooLong = assertThrows(NozzleException.class, () -> {
                try (Sender ignored = Sender.create(new SenderDesc(name + "a", APPLICATION, 3, false))) {}
            });
            assertEquals(ErrorCode.INVALID_ARGUMENT, tooLong.errorCode());
        }
    }

    private static BackendType requiredMetadataBackend() {
        String os = System.getProperty("os.name");
        assumeFalse(os.startsWith("Linux"),
            "SKIP: Linux default backend unavailable; native metadata lifecycle requires macOS/Windows");
        assertTrue(os.startsWith("Mac") || os.startsWith("Windows"),
            "Unsupported metadata test platform: " + os);
        return os.startsWith("Windows") ? BackendType.D3D11 : BackendType.METAL;
    }

    private static String metadataName(String purpose) {
        String name = "j193-" + purpose + "-" + ProcessHandle.current().pid() + "-"
            + Long.toUnsignedString(System.nanoTime()) + "-\u65E5" + SUPPLEMENTARY;
        assertTrue(name.getBytes(StandardCharsets.UTF_8).length <= 63);
        return name;
    }

    private static void assertSameMetadata(ConnectedSenderInfo expected, ConnectedSenderInfo actual) {
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.applicationName(), actual.applicationName());
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.backend(), actual.backend());
        assertEquals(expected.width(), actual.width());
        assertEquals(expected.height(), actual.height());
        assertEquals(expected.format(), actual.format());
        assertEquals(expected.estimatedFps(), actual.estimatedFps());
        assertEquals(expected.frameCounter(), actual.frameCounter());
        assertEquals(expected.lastUpdateTimeNs(), actual.lastUpdateTimeNs());
    }

    @Test
    void nativeSenderCloseRetainsIdentityButDisconnectsReceiver() {
        BackendType backend = requiredMetadataBackend();
        String name = metadataName("close");
        try (Sender sender = Sender.create(new SenderDesc(name, APPLICATION, 3, false));
             Receiver receiver = Receiver.create(new ReceiverDesc(name, APPLICATION, ReceiveMode.LATEST_ONLY))) {
            SenderInfo own = sender.info();
            ConnectedSenderInfo before = receiver.connectedInfo();
            ConnectionSnapshot initialSnapshot = receiver.connectionSnapshot();
            assertEquals(name, own.name());
            assertEquals(APPLICATION, own.applicationName());
            assertFalse(own.id().isEmpty());
            assertEquals(backend, own.backend());
            assertEquals(name, before.name());
            assertEquals(APPLICATION, before.applicationName());
            assertEquals(own.id(), before.id());
            assertEquals(backend, before.backend());
            assertTrue(receiver.isConnected());
            assertTrue(initialSnapshot.connected());
            assertSameMetadata(before, initialSnapshot.info());

            // No outstanding frames or mappings retain the native sender.
            sender.close();
            NozzleException closed = assertThrowsExactly(NozzleException.class, () -> {
                try (Frame unexpected = receiver.acquireFrame(0)) {}
            });
            assertEquals(ErrorCode.SENDER_CLOSED, closed.errorCode());
            ConnectedSenderInfo after = receiver.connectedInfo();
            assertEquals(before.name(), after.name());
            assertEquals(before.applicationName(), after.applicationName());
            assertEquals(before.id(), after.id());
            assertEquals(before.backend(), after.backend());
            boolean connected = receiver.isConnected();
            System.out.println("METADATA_NATIVE acquire=" + closed.errorCode()
                + " retainedId=" + after.id() + " backend=" + backend + " connected=" + connected);
            assertFalse(connected, "After " + closed.errorCode()
                + " with retained identity " + after.id() + ", isConnected must be false");
            ConnectionSnapshot disconnected = receiver.connectionSnapshot();
            assertFalse(disconnected.connected());
            assertSameMetadata(after, disconnected.info());
            assertTrue(initialSnapshot.connected());
            assertSameMetadata(before, initialSnapshot.info());
        }
    }

    @Test
    void nativeUtf8MetadataRemainsOwnedAfterLaterGettersAndBothCloses() {
        BackendType backend = requiredMetadataBackend();
        String name = metadataName("owned");
        SenderInfo own;
        ConnectedSenderInfo retained;
        ConnectionSnapshot snapshot;
        try (Sender sender = Sender.create(new SenderDesc(name, APPLICATION, 3, false));
             Receiver receiver = Receiver.create(new ReceiverDesc(name, APPLICATION, ReceiveMode.LATEST_ONLY))) {
            own = sender.info();
            retained = receiver.connectedInfo();
            snapshot = receiver.connectionSnapshot();
            assertEquals(own.id(), sender.info().id());
            assertEquals(retained.id(), receiver.connectedInfo().id());
        }

        // Only owned Java values are read after both native handles close.
        assertEquals(name, own.name());
        assertEquals(APPLICATION, own.applicationName());
        assertFalse(own.id().isEmpty());
        assertEquals(backend, own.backend());
        assertEquals(name, retained.name());
        assertEquals(APPLICATION, retained.applicationName());
        assertEquals(own.id(), retained.id());
        assertEquals(backend, retained.backend());
        assertTrue(snapshot.connected());
        assertSameMetadata(retained, snapshot.info());
        System.out.println("METADATA_NATIVE ownedUTF8AfterClose id=" + retained.id()
            + " backend=" + backend);
    }
}
