package nozzle;

public enum TextureOrigin {
    TOP_LEFT(0),
    BOTTOM_LEFT(1);

    private final int value;

    TextureOrigin(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static TextureOrigin fromValue(int value) {
        for (TextureOrigin o : values()) {
            if (o.value == value) {
                return o;
            }
        }
        return TOP_LEFT;
    }
}
