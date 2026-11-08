package nozzle;

/**
 * Receives GPU textures from a named sender.
 */
public class Receiver implements AutoCloseable {
    private long ptr;

    private Receiver(long ptr) {
        this.ptr = ptr;
    }

    public static Receiver create(ReceiverDesc desc) {
        long ptr = NozzleNative.receiverCreate(
            desc.name(), desc.applicationName(), desc.receiveMode().value()
        );
        return new Receiver(ptr);
    }

    @Override
    public void close() {
        if (ptr != 0) {
            NozzleNative.receiverDestroy(ptr);
            ptr = 0;
        }
    }

    public Frame acquireFrame(long timeoutMs) {
        long framePtr = NozzleNative.receiverAcquireFrame(ptr, timeoutMs);
        return new Frame(framePtr);
    }

    public ConnectedSenderInfo connectedInfo() {
        return connectionSnapshot().info();
    }

    /** Copies state and metadata together, retaining identity after an observed disconnect. */
    public ConnectionSnapshot connectionSnapshot() {
        return NozzleNative.receiverGetConnectionSnapshot(ptr);
    }

    public boolean isConnected() {
        try {
            return connectionSnapshot().connected();
        } catch (NozzleException e) {
            return false;
        }
    }

    long ptr() { return ptr; }
}
