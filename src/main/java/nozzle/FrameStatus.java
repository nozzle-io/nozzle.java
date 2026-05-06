package nozzle;

public enum FrameStatus {
    NEW(0),
    NO_NEW(1),
    DROPPED(2),
    SENDER_CLOSED(3),
    ERROR(4);

    private final int value;

    FrameStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static FrameStatus fromValue(int value) {
        for (FrameStatus s : values()) {
            if (s.value == value) {
                return s;
            }
        }
        return ERROR;
    }
}
