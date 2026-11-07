package nozzle;

import java.nio.ByteBuffer;

/**
 * Low-level JNI bridge. All native methods are implemented in nozzle_jni.c.
 * Prefer using the high-level wrapper classes (Sender, Receiver, etc.)
 * instead of calling these directly.
 */
final class NozzleNative {

    static {
        System.loadLibrary("nozzle_jni");
    }

    private NozzleNative() {}

    // JNI's UTF chars use modified UTF-8. Core names use strict standard UTF-8.
    private static byte[] encodeUtf8(String text) throws java.nio.charset.CharacterCodingException {
        if (text == null) text = "";
        if (text.indexOf('\0') >= 0) throw new IllegalArgumentException("embedded NUL in name");
        java.nio.ByteBuffer bytes = java.nio.charset.StandardCharsets.UTF_8.newEncoder()
            .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT)
            .onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT)
            .encode(java.nio.CharBuffer.wrap(text));
        byte[] result = new byte[bytes.remaining()];
        bytes.get(result);
        return result;
    }

    private static String decodeUtf8(byte[] bytes) throws java.nio.charset.CharacterCodingException {
        return java.nio.charset.StandardCharsets.UTF_8.newDecoder()
            .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT)
            .onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT)
            .decode(java.nio.ByteBuffer.wrap(bytes)).toString();
    }

    private static SenderInfo senderInfo(byte[] name, byte[] app, byte[] id, int backend)
            throws java.nio.charset.CharacterCodingException {
        return new SenderInfo(decodeUtf8(name), decodeUtf8(app), decodeUtf8(id), BackendType.fromValue(backend));
    }

    private static ConnectedSenderInfo connectedInfo(byte[] name, byte[] app, byte[] id,
            int backend, int width, int height, int format, double fps, long frame, long time)
            throws java.nio.charset.CharacterCodingException {
        return new ConnectedSenderInfo(decodeUtf8(name), decodeUtf8(app), decodeUtf8(id),
            BackendType.fromValue(backend), width, height, TextureFormat.fromValue(format), fps, frame, time);
    }

    // --- sender ---

    static native long senderCreate(String name, String applicationName,
                                     int ringBufferSize, boolean allowFormatFallback);

    static native void senderDestroy(long senderPtr);

    static native void senderPublishTexture(long senderPtr, long texturePtr);

    static native long senderAcquireWritableFrame(long senderPtr, int width,
                                                   int height, int format);

    static native void senderCommitFrame(long senderPtr, long framePtr);

    static native long senderPublishGLTexture(long senderPtr, int glTextureName,
                                               int glTarget, int width, int height, int format);

    static native long senderPublishNativeTexture(long senderPtr, long nativeTexture,
                                                   int width, int height, int format);

    static native SenderInfo senderGetInfo(long senderPtr);

    // --- receiver ---

    static native long receiverCreate(String name, String applicationName, int receiveMode);

    static native void receiverDestroy(long receiverPtr);

    static native long receiverAcquireFrame(long receiverPtr, long timeoutMs);

    static native ConnectedSenderInfo receiverGetConnectedInfo(long receiverPtr);

    // --- frame ---

    static native void frameRelease(long framePtr);

    static native FrameInfo frameGetInfo(long framePtr);

    static native ResolvedTextureFormat frameGetResolvedFormat(long framePtr);

    static native MappedPixels frameLockPixels(long framePtr, int origin);

    static native MappedPixels frameLockWritablePixels(long framePtr, int origin);

    static native void unlockPixels(long framePtr);

    static native void unlockWritablePixels(long framePtr);

    static native void frameCopyToGLTexture(long framePtr, int glTextureName,
                                             int glTarget, int width, int height, int format);

    static native void frameCopyToNativeTexture(long framePtr, long nativeTexture,
                                                 int width, int height, int format);

    // --- device ---

    static native long deviceGetDefault();

    static native void deviceDestroy(long devicePtr);

    // --- texture ---

    static native long textureWrap(long nativeTexture, int width, int height,
                                    int format, int backend);

    static native void textureDestroy(long texturePtr);

    // --- discovery ---

    static native SenderInfo[] enumerateSenders();

    // --- pixel utilities ---

    static native void swizzleChannels(ByteBuffer src, ByteBuffer dst, int width, int height,
                                        int srcRowBytes, int dstRowBytes, int format,
                                        byte[] permuteMap);

    static native void widenUint16ToUint32(ByteBuffer src, ByteBuffer dst, int width, int height,
                                            int srcRowBytes, int dstRowBytes, int channels);

    static native void convertUint32ToFloat32(ByteBuffer src, ByteBuffer dst, int width, int height,
                                               int srcRowBytes, int dstRowBytes, int channels);
}
