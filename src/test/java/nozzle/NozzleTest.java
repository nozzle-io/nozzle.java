package nozzle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class NozzleTest {

    // ========== ErrorCode ==========

    @Test
    void errorCodeOkIsZero() {
        assertEquals(0, ErrorCode.OK.value());
    }

    @Test
    void errorCodeAllUnique() {
        ErrorCode[] codes = ErrorCode.values();
        for (int i = 0; i < codes.length; i++) {
            for (int j = i + 1; j < codes.length; j++) {
                assertNotEquals(codes[i].value(), codes[j].value(),
                    codes[i] + " and " + codes[j] + " have same value");
            }
        }
    }

    @Test
    void errorCodeFromValue() {
        assertEquals(ErrorCode.OK, ErrorCode.fromValue(0));
        assertEquals(ErrorCode.UNKNOWN, ErrorCode.fromValue(1));
        assertEquals(ErrorCode.INVALID_ARGUMENT, ErrorCode.fromValue(2));
        assertEquals(ErrorCode.TIMEOUT, ErrorCode.fromValue(10));
        assertEquals(ErrorCode.BACKEND_ERROR, ErrorCode.fromValue(11));
        assertEquals(ErrorCode.UNKNOWN, ErrorCode.fromValue(999));
    }

    @Test
    void errorCodeMessageNonEmpty() {
        for (ErrorCode c : ErrorCode.values()) {
            assertNotNull(c.message());
            assertFalse(c.message().isEmpty(), c + " has empty message");
        }
    }

    @Test
    void errorCodeCount() {
        assertEquals(12, ErrorCode.values().length);
    }

    // ========== BackendType ==========

    @Test
    void backendTypeUnknownIsZero() {
        assertEquals(0, BackendType.UNKNOWN.value());
    }

    @Test
    void backendTypeAllUnique() {
        BackendType[] types = BackendType.values();
        for (int i = 0; i < types.length; i++) {
            for (int j = i + 1; j < types.length; j++) {
                assertNotEquals(types[i].value(), types[j].value());
            }
        }
    }

    @Test
    void backendTypeFromValueRoundTrip() {
        for (BackendType b : BackendType.values()) {
            assertEquals(b, BackendType.fromValue(b.value()));
        }
    }

    @Test
    void backendTypeToString() {
        assertEquals("d3d11", BackendType.D3D11.toString());
        assertEquals("metal", BackendType.METAL.toString());
        assertEquals("opengl", BackendType.OPENGL.toString());
        assertEquals("dma_buf", BackendType.DMA_BUF.toString());
        assertEquals("unknown", BackendType.UNKNOWN.toString());
    }

    @Test
    void backendTypeCount() {
        assertEquals(5, BackendType.values().length);
    }

    // ========== TextureFormat ==========

    @Test
    void textureFormatUnknownIsZero() {
        assertEquals(0, TextureFormat.UNKNOWN.value());
    }

    @Test
    void textureFormatAllUnique() {
        TextureFormat[] formats = TextureFormat.values();
        for (int i = 0; i < formats.length; i++) {
            for (int j = i + 1; j < formats.length; j++) {
                assertNotEquals(formats[i].value(), formats[j].value());
            }
        }
    }

    @Test
    void textureFormatFromValueRoundTrip() {
        for (TextureFormat f : TextureFormat.values()) {
            assertEquals(f, TextureFormat.fromValue(f.value()));
        }
    }

    @Test
    void textureFormatBytesPerPixel() {
        assertEquals(1, TextureFormat.R8_UNORM.bytesPerPixel());
        assertEquals(2, TextureFormat.RG8_UNORM.bytesPerPixel());
        assertEquals(2, TextureFormat.R16_UNORM.bytesPerPixel());
        assertEquals(2, TextureFormat.R16_FLOAT.bytesPerPixel());
        assertEquals(4, TextureFormat.RGBA8_UNORM.bytesPerPixel());
        assertEquals(4, TextureFormat.BGRA8_UNORM.bytesPerPixel());
        assertEquals(4, TextureFormat.RGBA8_SRGB.bytesPerPixel());
        assertEquals(4, TextureFormat.BGRA8_SRGB.bytesPerPixel());
        assertEquals(4, TextureFormat.RG16_UNORM.bytesPerPixel());
        assertEquals(4, TextureFormat.RG16_FLOAT.bytesPerPixel());
        assertEquals(4, TextureFormat.R32_FLOAT.bytesPerPixel());
        assertEquals(4, TextureFormat.R32_UINT.bytesPerPixel());
        assertEquals(4, TextureFormat.DEPTH32_FLOAT.bytesPerPixel());
        assertEquals(8, TextureFormat.RGBA16_UNORM.bytesPerPixel());
        assertEquals(8, TextureFormat.RGBA16_FLOAT.bytesPerPixel());
        assertEquals(8, TextureFormat.RG32_FLOAT.bytesPerPixel());
        assertEquals(16, TextureFormat.RGBA32_FLOAT.bytesPerPixel());
        assertEquals(16, TextureFormat.RGBA32_UINT.bytesPerPixel());
        assertEquals(0, TextureFormat.UNKNOWN.bytesPerPixel());
    }

    @Test
    void textureFormatCount() {
        assertEquals(19, TextureFormat.values().length);
    }

    // ========== Remaining Enums ==========

    @Test
    void receiveModeValues() {
        assertEquals(2, ReceiveMode.values().length);
        assertEquals(0, ReceiveMode.LATEST_ONLY.value());
        assertEquals(1, ReceiveMode.SEQUENTIAL_BEST_EFFORT.value());
        assertEquals(ReceiveMode.LATEST_ONLY, ReceiveMode.fromValue(0));
        assertEquals(ReceiveMode.SEQUENTIAL_BEST_EFFORT, ReceiveMode.fromValue(1));
    }

    @Test
    void frameStatusValues() {
        assertEquals(5, FrameStatus.values().length);
        assertEquals(0, FrameStatus.NEW.value());
        assertEquals(1, FrameStatus.NO_NEW.value());
        assertEquals(2, FrameStatus.DROPPED.value());
        assertEquals(3, FrameStatus.SENDER_CLOSED.value());
        assertEquals(4, FrameStatus.ERROR.value());
    }

    @Test
    void textureOriginValues() {
        assertEquals(2, TextureOrigin.values().length);
        assertEquals(0, TextureOrigin.TOP_LEFT.value());
        assertEquals(1, TextureOrigin.BOTTOM_LEFT.value());
    }

    @Test
    void formatSourceValues() {
        assertEquals(4, FormatSource.values().length);
        assertEquals(0, FormatSource.UNKNOWN.value());
        assertEquals(1, FormatSource.REQUESTED.value());
        assertEquals(2, FormatSource.CALLER_HINT.value());
        assertEquals(3, FormatSource.NATIVE_OBSERVED.value());
    }

    @Test
    void nativeFormatKindValues() {
        assertEquals(5, NativeFormatKind.values().length);
        assertEquals(0, NativeFormatKind.UNKNOWN.value());
        assertEquals(1, NativeFormatKind.MTL_PIXEL_FORMAT.value());
        assertEquals(2, NativeFormatKind.DXGI_FORMAT.value());
        assertEquals(3, NativeFormatKind.DRM_FOURCC.value());
        assertEquals(4, NativeFormatKind.GL_INTERNAL_FORMAT.value());
    }

    // ========== NozzleException ==========

    @Test
    void nozzleExceptionHasErrorCode() {
        NozzleException ex = new NozzleException(ErrorCode.TIMEOUT);
        assertEquals(ErrorCode.TIMEOUT, ex.errorCode());
    }

    @Test
    void nozzleExceptionMessageFromErrorCode() {
        NozzleException ex = new NozzleException(ErrorCode.SENDER_NOT_FOUND);
        assertEquals("sender not found", ex.getMessage());
    }

    @Test
    void nozzleExceptionCustomMessage() {
        NozzleException ex = new NozzleException(ErrorCode.BACKEND_ERROR, "custom msg");
        assertEquals("custom msg", ex.getMessage());
        assertEquals(ErrorCode.BACKEND_ERROR, ex.errorCode());
    }

    @Test
    void nozzleExceptionFromIntValue() {
        NozzleException ex = new NozzleException(10);
        assertEquals(ErrorCode.TIMEOUT, ex.errorCode());
    }

    @Test
    void nozzleExceptionIsRuntimeException() {
        assertInstanceOf(RuntimeException.class, new NozzleException(ErrorCode.OK));
    }

    // ========== MappedPixels ==========

    @Test
    void mappedPixelsRowBounds() {
        java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocateDirect(64 * 4);
        MappedPixels mp = new MappedPixels(buf, 4, 64, 64,
            TextureFormat.RGBA8_UNORM, TextureOrigin.TOP_LEFT, 0, false);

        assertDoesNotThrow(() -> mp.row(0));
        assertDoesNotThrow(() -> mp.row(63));

        assertThrows(IllegalArgumentException.class, () -> mp.row(-1));
        assertThrows(IllegalArgumentException.class, () -> mp.row(64));
    }

    // ========== GPU-dependent tests ==========

    @Test
    void gpuAvailableCheck() {
        assumeTrue(Nozzle.isGpuAvailable(), "GPU not available in this environment");
        assertTrue(Nozzle.isGpuAvailable());
    }

    @Test
    void senderCreateAndClose() {
        assumeTrue(Nozzle.isGpuAvailable(), "GPU not available");

        try (Sender s = Sender.create(new SenderDesc("java-test-sender", "nozzle-java", 2, false))) {
            assertNotNull(s);
            SenderInfo info = s.info();
            assertEquals("java-test-sender", info.name());
            assertEquals("nozzle-java", info.applicationName());
            assertNotNull(info.id());
            assertNotEquals(BackendType.UNKNOWN, info.backend());
        }
    }

    @Test
    void receiverCreateAndClose() {
        assumeTrue(Nozzle.isGpuAvailable(), "GPU not available");

        try (Receiver r = Receiver.create(new ReceiverDesc("nonexistent", "nozzle-java", ReceiveMode.LATEST_ONLY))) {
            assertNotNull(r);
            assertFalse(r.isConnected());
        }
    }

    @Test
    void senderAcquireWritableFrameAndCommit() {
        assumeTrue(Nozzle.isGpuAvailable(), "GPU not available");

        try (Sender s = Sender.create(new SenderDesc("java-test-write", "nozzle-java", 2, false))) {
            try (WritableFrame f = s.acquireWritableFrame(64, 64, TextureFormat.RGBA8_UNORM)) {
                assertNotNull(f);
                FrameInfo info = f.info();
                assertEquals(64, info.width());
                assertEquals(64, info.height());
                assertEquals(TextureFormat.RGBA8_UNORM, info.format());
            }
        }
    }

    @Test
    void enumerateSendersReturnsArray() {
        assumeTrue(Nozzle.isGpuAvailable(), "GPU not available");

        SenderInfo[] senders = Nozzle.enumerateSenders();
        assertNotNull(senders);
    }

    @Test
    void deviceGetDefault() {
        assumeTrue(Nozzle.isGpuAvailable(), "GPU not available");

        try (Device d = Device.getDefault()) {
            assertNotNull(d);
        }
    }
}
