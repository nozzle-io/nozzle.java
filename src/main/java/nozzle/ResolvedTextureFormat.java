package nozzle;

public class ResolvedTextureFormat {
    private final TextureFormat storageFormat;
    private final TextureFormat semanticFormat;
    private final FormatSource formatSource;
    private final BackendType nativeBackend;
    private final NativeFormatKind nativeKind;
    private final int nativeValue;
    private final int channelOrder;
    private final int componentType;
    private final int componentBits;
    private final int channelCount;
    private final int bytesPerPixel;

    public ResolvedTextureFormat(TextureFormat storageFormat, TextureFormat semanticFormat,
                                 FormatSource formatSource, BackendType nativeBackend,
                                 NativeFormatKind nativeKind, int nativeValue,
                                 int channelOrder, int componentType,
                                 int componentBits, int channelCount, int bytesPerPixel) {
        this.storageFormat = storageFormat;
        this.semanticFormat = semanticFormat;
        this.formatSource = formatSource;
        this.nativeBackend = nativeBackend;
        this.nativeKind = nativeKind;
        this.nativeValue = nativeValue;
        this.channelOrder = channelOrder;
        this.componentType = componentType;
        this.componentBits = componentBits;
        this.channelCount = channelCount;
        this.bytesPerPixel = bytesPerPixel;
    }

    public TextureFormat storageFormat() { return storageFormat; }
    public TextureFormat semanticFormat() { return semanticFormat; }
    public FormatSource formatSource() { return formatSource; }
    public BackendType nativeBackend() { return nativeBackend; }
    public NativeFormatKind nativeKind() { return nativeKind; }
    public int nativeValue() { return nativeValue; }
    public int channelOrder() { return channelOrder; }
    public int componentType() { return componentType; }
    public int componentBits() { return componentBits; }
    public int channelCount() { return channelCount; }
    public int bytesPerPixel() { return bytesPerPixel; }
}
