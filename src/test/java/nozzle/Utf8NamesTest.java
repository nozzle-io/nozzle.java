package nozzle;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class Utf8NamesTest {
    private static final String SUPPLEMENTARY = "\uD83D\uDE80";
    private static final String APPLICATION = "\u65E5\u672C-app-" + SUPPLEMENTARY;

    @Test
    void malformedNamesAndApplicationsAreRejectedThroughJni() {
        for (String bad : new String[] {"a\0b", "a\uD800", "a\uDC00", "\uD800x"}) {
            Class<? extends Throwable> error = bad.indexOf('\0') >= 0
                ? IllegalArgumentException.class : java.nio.charset.CharacterCodingException.class;
            assertThrows(error, () -> {
                try (Sender ignored = Sender.create(new SenderDesc(bad, APPLICATION, 3, false))) {}
            });
            assertThrows(error, () -> {
                try (Sender ignored = Sender.create(new SenderDesc("valid", bad, 3, false))) {}
            });
            assertThrows(error, () -> {
                try (Receiver ignored = Receiver.create(new ReceiverDesc(bad, APPLICATION, ReceiveMode.LATEST_ONLY))) {}
            });
            assertThrows(error, () -> {
                try (Receiver ignored = Receiver.create(new ReceiverDesc("valid", bad, ReceiveMode.LATEST_ONLY))) {}
            });
        }
        NozzleException tooLong = assertThrows(NozzleException.class, () -> {
            try (Sender ignored = Sender.create(new SenderDesc("a".repeat(64), APPLICATION, 3, false))) {}
        });
        assertEquals(ErrorCode.INVALID_ARGUMENT, tooLong.errorCode());
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
}
