package tool.migration.model;

public enum WebLogicVersion {

    
    WLS_12_2_1_3("12.2.1.3.0"),
    WLS_12_2_1_4("12.2.1.4.0"),
    WLS_14_1_1("14.1.1.0.0"),
    WLS_14_1_2_0("14.1.2.0.0");

    
    private final String path;

    WebLogicVersion(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }
}

