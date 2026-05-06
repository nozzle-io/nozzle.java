package nozzle;

public class TextureWrapDesc {
    private final long nativeTexture;
    private final int width;
    private final int height;
    private final TextureFormat format;
    private final BackendType backend;

    public TextureWrapDesc(long nativeTexture, int width, int height,
                           TextureFormat format, BackendType backend) {
        this.nativeTexture = nativeTexture;
        this.width = width;
        this.height = height;
        this.format = format;
        this.backend = backend;
    }

    public long nativeTexture() { return nativeTexture; }
    public int width() { return width; }
    public int height() { return height; }
    public TextureFormat format() { return format; }
    public BackendType backend() { return backend; }
}
