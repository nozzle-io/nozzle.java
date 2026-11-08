package nozzle;

/** Owned metadata and last-observed connection state, not a fresh liveness check. */
public final class ConnectionSnapshot {
    private final boolean connected;
    private final ConnectedSenderInfo info;

    public ConnectionSnapshot(boolean connected, ConnectedSenderInfo info) {
        this.connected = connected;
        this.info = info;
    }

    public boolean connected() { return connected; }
    public ConnectedSenderInfo info() { return info; }
}
