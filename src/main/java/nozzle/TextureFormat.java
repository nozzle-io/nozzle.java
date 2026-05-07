package nozzle;

public enum TextureFormat {
    UNKNOWN(0),
    R8_UNORM(1),
    RG8_UNORM(2),
    RGB8_UNORM(3),
    RGBA8_UNORM(4),
    BGRA8_UNORM(5),
    RGBA8_SRGB(6),
    BGRA8_SRGB(7),
    R16_UNORM(8),
    RG16_UNORM(9),
    RGB16_UNORM(10),
    RGBA16_UNORM(11),
    R16_FLOAT(12),
    RG16_FLOAT(13),
    RGB16_FLOAT(14),
    RGBA16_FLOAT(15),
    R32_FLOAT(16),
    RG32_FLOAT(17),
    RGB32_FLOAT(18),
    RGBA32_FLOAT(19),
    R32_UINT(20),
    RGBA32_UINT(21),
    RGB32_UINT(22),
    DEPTH32_FLOAT(23);

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
            case RGB8_UNORM:
            case RGBA8_UNORM:
            case BGRA8_UNORM:
            case RGBA8_SRGB:
            case BGRA8_SRGB:
            case RG16_UNORM:
            case RG16_FLOAT:
            case R32_FLOAT:
            case R32_UINT:
            case DEPTH32_FLOAT: return 4;
            case RGB16_UNORM:
            case RGBA16_UNORM:
            case RGBA16_FLOAT:
            case RG32_FLOAT: return 8;
            case RGB32_FLOAT:
            case RGBA32_FLOAT:
            case RGBA32_UINT: return 16;
            case RGB32_UINT: return 12;
            default: return 0;
        }
    }
}
