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
        return NozzleNative.receiverGetConnectedInfo(ptr);
    }

    public boolean isConnected() {
        try {
            connectedInfo();
            return true;
        } catch (NozzleException e) {
            return false;
        }
    }

    long ptr() { return ptr; }
}
