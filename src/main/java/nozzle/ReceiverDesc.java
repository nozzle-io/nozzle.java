package nozzle;

public class ReceiverDesc {
    private final String name;
    private final String applicationName;
    private final ReceiveMode receiveMode;

    public ReceiverDesc(String name, String applicationName, ReceiveMode receiveMode) {
        this.name = name;
        this.applicationName = applicationName;
        this.receiveMode = receiveMode;
    }

    public String name() { return name; }
    public String applicationName() { return applicationName; }
    public ReceiveMode receiveMode() { return receiveMode; }
}
