package tool.migration.model;

public class ManagedServerView {

    private ManagedServerConfig config;
    private ManagedServerRuntime runtime;

    public ManagedServerView(
            ManagedServerConfig config,
            ManagedServerRuntime runtime
    ) {
        this.config = config;
        this.runtime = runtime;
    }

    public ManagedServerConfig getConfig() {
        return config;
    }

    public void setConfig(ManagedServerConfig config) {
        this.config = config;
    }

    public ManagedServerRuntime getRuntime() {
        return runtime;
    }

    public void setRuntime(ManagedServerRuntime runtime) {
        this.runtime = runtime;
    }

    // getters

    
}