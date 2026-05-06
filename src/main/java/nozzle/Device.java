package nozzle;

/**
 * Represents a GPU device handle.
 */
public class Device implements AutoCloseable {
    private long ptr;

    private Device(long ptr) {
        this.ptr = ptr;
    }

    public static Device getDefault() {
        long ptr = NozzleNative.deviceGetDefault();
        return new Device(ptr);
    }

    @Override
    public void close() {
        if (ptr != 0) {
            NozzleNative.deviceDestroy(ptr);
            ptr = 0;
        }
    }

    long ptr() { return ptr; }
}
