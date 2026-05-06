package nozzle;

/**
 * Represents a writable frame acquired from a Sender.
 */
public class WritableFrame implements AutoCloseable {
    final long ptr;

    WritableFrame(long ptr) {
        this.ptr = ptr;
    }

    @Override
    public void close() {
        if (ptr != 0) {
            NozzleNative.frameRelease(ptr);
        }
    }

    public FrameInfo info() {
        return NozzleNative.frameGetInfo(ptr);
    }

    public MappedPixels lockWritablePixels(TextureOrigin origin) {
        return NozzleNative.frameLockWritablePixels(ptr, origin.value());
    }
}
