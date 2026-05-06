package nozzle;

/**
 * Sends GPU textures to a named receiver.
 */
public class Sender implements AutoCloseable {
    private long ptr;

    private Sender(long ptr) {
        this.ptr = ptr;
    }

    public static Sender create(SenderDesc desc) {
        long ptr = NozzleNative.senderCreate(
            desc.name(), desc.applicationName(),
            desc.ringBufferSize(), desc.allowFormatFallback()
        );
        return new Sender(ptr);
    }

    @Override
    public void close() {
        if (ptr != 0) {
            NozzleNative.senderDestroy(ptr);
            ptr = 0;
        }
    }

    public WritableFrame acquireWritableFrame(int width, int height, TextureFormat format) {
        long framePtr = NozzleNative.senderAcquireWritableFrame(ptr, width, height, format.value());
        return new WritableFrame(framePtr);
    }

    public void commitFrame(WritableFrame frame) {
        NozzleNative.senderCommitFrame(ptr, frame.ptr);
    }

    public void publishTexture(Texture texture) {
        NozzleNative.senderPublishTexture(ptr, texture.ptr);
    }

    public void publishGLTexture(int glTextureName, int glTarget,
                                  int width, int height, TextureFormat format) {
        NozzleNative.senderPublishGLTexture(ptr, glTextureName, glTarget,
            width, height, format.value());
    }

    public void publishNativeTexture(long nativeTexture, int width, int height, TextureFormat format) {
        NozzleNative.senderPublishNativeTexture(ptr, nativeTexture, width, height, format.value());
    }

    public SenderInfo info() {
        return NozzleNative.senderGetInfo(ptr);
    }

    long ptr() { return ptr; }
}
