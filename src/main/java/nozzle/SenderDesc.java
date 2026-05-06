package nozzle;

public class SenderDesc {
    private final String name;
    private final String applicationName;
    private final int ringBufferSize;
    private final boolean allowFormatFallback;

    public SenderDesc(String name, String applicationName, int ringBufferSize, boolean allowFormatFallback) {
        this.name = name;
        this.applicationName = applicationName;
        this.ringBufferSize = ringBufferSize;
        this.allowFormatFallback = allowFormatFallback;
    }

    public String name() { return name; }
    public String applicationName() { return applicationName; }
    public int ringBufferSize() { return ringBufferSize; }
    public boolean allowFormatFallback() { return allowFormatFallback; }
}
