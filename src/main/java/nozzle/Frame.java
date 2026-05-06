package nozzle;

import java.nio.ByteBuffer;

/**
 * Represents a received or writable GPU texture frame.
 */
public class Frame implements AutoCloseable {
    long ptr;

    Frame(long ptr) {
        this.ptr = ptr;
    }

    @Override
    public void close() {
        if (ptr != 0) {
            NozzleNative.frameRelease(ptr);
            ptr = 0;
        }
    }

    public FrameInfo info() {
        return NozzleNative.frameGetInfo(ptr);
    }

    public ResolvedTextureFormat resolvedFormat() {
        return NozzleNative.frameGetResolvedFormat(ptr);
    }

    public MappedPixels lockPixels(TextureOrigin origin) {
        return NozzleNative.frameLockPixels(ptr, origin.value());
    }

    public MappedPixels lockWritablePixels(TextureOrigin origin) {
        return NozzleNative.frameLockWritablePixels(ptr, origin.value());
    }

    public void copyToGLTexture(int glTextureName, int glTarget,
                                 int width, int height, TextureFormat format) {
        NozzleNative.frameCopyToGLTexture(ptr, glTextureName, glTarget,
            width, height, format.value());
    }

    public void copyToNativeTexture(long nativeTexture, int width, int height, TextureFormat format) {
        NozzleNative.frameCopyToNativeTexture(ptr, nativeTexture, width, height, format.value());
    }
}
