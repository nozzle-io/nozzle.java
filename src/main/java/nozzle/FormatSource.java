package nozzle;

public enum FormatSource {
    UNKNOWN(0),
    REQUESTED(1),
    CALLER_HINT(2),
    NATIVE_OBSERVED(3);

    private final int value;

    FormatSource(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static FormatSource fromValue(int value) {
        for (FormatSource s : values()) {
            if (s.value == value) {
                return s;
            }
        }
        return UNKNOWN;
    }
}
