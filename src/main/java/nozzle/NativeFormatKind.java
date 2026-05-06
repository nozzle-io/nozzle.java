package nozzle;

public enum NativeFormatKind {
    UNKNOWN(0),
    MTL_PIXEL_FORMAT(1),
    DXGI_FORMAT(2),
    DRM_FOURCC(3),
    GL_INTERNAL_FORMAT(4);

    private final int value;

    NativeFormatKind(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static NativeFormatKind fromValue(int value) {
        for (NativeFormatKind k : values()) {
            if (k.value == value) {
                return k;
            }
        }
        return UNKNOWN;
    }
}
