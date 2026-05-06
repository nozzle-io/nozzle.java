package nozzle;

public enum TextureFormat {
    UNKNOWN(0),
    R8_UNORM(1),
    RG8_UNORM(2),
    RGBA8_UNORM(3),
    BGRA8_UNORM(4),
    RGBA8_SRGB(5),
    BGRA8_SRGB(6),
    R16_UNORM(7),
    RG16_UNORM(8),
    RGBA16_UNORM(9),
    R16_FLOAT(10),
    RG16_FLOAT(11),
    RGBA16_FLOAT(12),
    R32_FLOAT(13),
    RG32_FLOAT(14),
    RGBA32_FLOAT(15),
    R32_UINT(16),
    RGBA32_UINT(17),
    DEPTH32_FLOAT(18);

    private final int value;

    TextureFormat(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static TextureFormat fromValue(int value) {
        for (TextureFormat f : values()) {
            if (f.value == value) {
                return f;
            }
        }
        return UNKNOWN;
    }

    public int bytesPerPixel() {
        switch (this) {
            case R8_UNORM: return 1;
            case RG8_UNORM:
            case R16_UNORM:
            case R16_FLOAT: return 2;
            case RGBA8_UNORM:
            case BGRA8_UNORM:
            case RGBA8_SRGB:
            case BGRA8_SRGB:
            case RG16_UNORM:
            case RG16_FLOAT:
            case R32_FLOAT:
            case R32_UINT:
            case DEPTH32_FLOAT: return 4;
            case RGBA16_UNORM:
            case RGBA16_FLOAT:
            case RG32_FLOAT: return 8;
            case RGBA32_FLOAT:
            case RGBA32_UINT: return 16;
            default: return 0;
        }
    }
}
