package nozzle;

public enum BackendType {
    UNKNOWN(0),
    D3D11(1),
    METAL(2),
    OPENGL(3),
    DMA_BUF(4);

    private final int value;

    BackendType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static BackendType fromValue(int value) {
        for (BackendType b : values()) {
            if (b.value == value) {
                return b;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        switch (this) {
            case D3D11: return "d3d11";
            case METAL: return "metal";
            case OPENGL: return "opengl";
            case DMA_BUF: return "dma_buf";
            default: return "unknown";
        }
    }
}
