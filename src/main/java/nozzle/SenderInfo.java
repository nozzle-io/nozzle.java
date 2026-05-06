package nozzle;

public class SenderInfo {
    private final String name;
    private final String applicationName;
    private final String id;
    private final BackendType backend;

    public SenderInfo(String name, String applicationName, String id, BackendType backend) {
        this.name = name;
        this.applicationName = applicationName;
        this.id = id;
        this.backend = backend;
    }

    public String name() { return name; }
    public String applicationName() { return applicationName; }
    public String id() { return id; }
    public BackendType backend() { return backend; }
}
