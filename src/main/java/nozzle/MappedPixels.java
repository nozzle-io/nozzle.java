package nozzle;

import java.nio.ByteBuffer;

public class MappedPixels implements AutoCloseable {
    private final ByteBuffer data;
    private final int rowStrideBytes;
    private final int width;
    private final int height;
    private final TextureFormat format;
    private final TextureOrigin origin;
    private final long framePtr;
    private final boolean writable;

    MappedPixels(ByteBuffer data, int rowStrideBytes, int width, int height,
                 TextureFormat format, TextureOrigin origin,
                 long framePtr, boolean writable) {
        this.data = data;
        this.rowStrideBytes = rowStrideBytes;
        this.width = width;
        this.height = height;
        this.format = format;
        this.origin = origin;
        this.framePtr = framePtr;
        this.writable = writable;
    }

    public ByteBuffer data() { return data; }
    public int rowStrideBytes() { return rowStrideBytes; }
    public int width() { return width; }
    public int height() { return height; }
    public TextureFormat format() { return format; }
    public TextureOrigin origin() { return origin; }

    public ByteBuffer row(int y) {
        if (y < 0 || y >= height) {
            throw new IllegalArgumentException("row " + y + " out of bounds (height " + height + ")");
        }
        int start = y * rowStrideBytes;
        ByteBuffer slice = data.duplicate();
        slice.position(start);
        slice.limit(start + rowStrideBytes);
        return slice.slice();
    }

    @Override
    public void close() {
        if (framePtr == 0) {
            return;
        }
        if (writable) {
            NozzleNative.unlockWritablePixels(framePtr);
        } else {
            NozzleNative.unlockPixels(framePtr);
        }
    }
}
