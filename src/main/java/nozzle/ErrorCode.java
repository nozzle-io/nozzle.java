package nozzle;

public enum ErrorCode {
    OK(0),
    UNKNOWN(1),
    INVALID_ARGUMENT(2),
    UNSUPPORTED_BACKEND(3),
    UNSUPPORTED_FORMAT(4),
    DEVICE_MISMATCH(5),
    RESOURCE_CREATION_FAILED(6),
    SHARED_HANDLE_FAILED(7),
    SENDER_NOT_FOUND(8),
    SENDER_CLOSED(9),
    TIMEOUT(10),
    BACKEND_ERROR(11);

    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ErrorCode fromValue(int value) {
        for (ErrorCode c : values()) {
            if (c.value == value) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public String message() {
        switch (this) {
            case OK: return "ok";
            case UNKNOWN: return "unknown error";
            case INVALID_ARGUMENT: return "invalid argument";
            case UNSUPPORTED_BACKEND: return "unsupported backend";
            case UNSUPPORTED_FORMAT: return "unsupported format";
            case DEVICE_MISMATCH: return "device mismatch";
            case RESOURCE_CREATION_FAILED: return "resource creation failed";
            case SHARED_HANDLE_FAILED: return "shared handle failed";
            case SENDER_NOT_FOUND: return "sender not found";
            case SENDER_CLOSED: return "sender closed";
            case TIMEOUT: return "timeout";
            case BACKEND_ERROR: return "backend error";
            default: return "nozzle error (" + value + ")";
        }
    }
}
