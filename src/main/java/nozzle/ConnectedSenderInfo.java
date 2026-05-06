package nozzle;

public class ConnectedSenderInfo {
    private final String name;
    private final String applicationName;
    private final String id;
    private final BackendType backend;
    private final int width;
    private final int height;
    private final TextureFormat format;
    private final double estimatedFps;
    private final long frameCounter;
    private final long lastUpdateTimeNs;

    public ConnectedSenderInfo(String name, String applicationName, String id,
                               BackendType backend, int width, int height,
                               TextureFormat format, double estimatedFps,
                               long frameCounter, long lastUpdateTimeNs) {
        this.name = name;
        this.applicationName = applicationName;
        this.id = id;
        this.backend = backend;
        this.width = width;
        this.height = height;
        this.format = format;
        this.estimatedFps = estimatedFps;
        this.frameCounter = frameCounter;
        this.lastUpdateTimeNs = lastUpdateTimeNs;
    }

    public String name() { return name; }
    public String applicationName() { return applicationName; }
    public String id() { return id; }
    public BackendType backend() { return backend; }
    public int width() { return width; }
    public int height() { return height; }
    public TextureFormat format() { return format; }
    public double estimatedFps() { return estimatedFps; }
    public long frameCounter() { return frameCounter; }
    public long lastUpdateTimeNs() { return lastUpdateTimeNs; }
}
