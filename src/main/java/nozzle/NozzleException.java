package nozzle;

public class NozzleException extends RuntimeException {
    private final ErrorCode code;

    public NozzleException(ErrorCode code) {
        super(code.message());
        this.code = code;
    }

    public NozzleException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public NozzleException(int value) {
        this(ErrorCode.fromValue(value));
    }

    public ErrorCode errorCode() {
        return code;
    }
}
