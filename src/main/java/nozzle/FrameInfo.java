package nozzle;

public class FrameInfo {
    private final long frameIndex;
    private final long timestampNs;
    private final int width;
    private final int height;
    private final TextureFormat format;
    private final int droppedFrameCount;

    public FrameInfo(long frameIndex, long timestampNs, int width, int height,
                     TextureFormat format, int droppedFrameCount) {
        this.frameIndex = frameIndex;
        this.timestampNs = timestampNs;
        this.width = width;
        this.height = height;
        this.format = format;
        this.droppedFrameCount = droppedFrameCount;
    }

    public long frameIndex() { return frameIndex; }
    public long timestampNs() { return timestampNs; }
    public int width() { return width; }
    public int height() { return height; }
    public TextureFormat format() { return format; }
    public int droppedFrameCount() { return droppedFrameCount; }
}
