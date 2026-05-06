package nozzle;

public enum ReceiveMode {
    LATEST_ONLY(0),
    SEQUENTIAL_BEST_EFFORT(1);

    private final int value;

    ReceiveMode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ReceiveMode fromValue(int value) {
        for (ReceiveMode m : values()) {
            if (m.value == value) {
                return m;
            }
        }
        return LATEST_ONLY;
    }
}
