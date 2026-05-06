package nozzle;

/**
 * Represents a wrapped native GPU texture.
 */
public class Texture implements AutoCloseable {
    long ptr;

    private Texture(long ptr) {
        this.ptr = ptr;
    }

    public static Texture wrap(TextureWrapDesc desc) {
        long ptr = NozzleNative.textureWrap(
            desc.nativeTexture(), desc.width(), desc.height(),
            desc.format().value(), desc.backend().value()
        );
        return new Texture(ptr);
    }

    @Override
    public void close() {
        if (ptr != 0) {
            NozzleNative.textureDestroy(ptr);
            ptr = 0;
        }
    }

    long ptr() { return ptr; }
}
