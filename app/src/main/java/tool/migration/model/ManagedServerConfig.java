package tool.migration.model;



public class ManagedServerConfig {

    private String name;

    // edit
    private Integer listenPort;
    private String cluster;
    private String machine;

    private String keyStores;
    private String customTrustKeyStoreFileName;
    private String customTrustKeyStoreType;

    // domainConfig (si aplica)
    private Integer listenPortDomain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getKeyStores() {
        return keyStores;
    }

    public void setKeyStores(String keyStores) {
        this.keyStores = keyStores;
    }

    public String getCustomTrustKeyStoreFileName() {
        return customTrustKeyStoreFileName;
    }

    public void setCustomTrustKeyStoreFileName(String customTrustKeyStoreFileName) {
        this.customTrustKeyStoreFileName = customTrustKeyStoreFileName;
    }

    public String getCustomTrustKeyStoreType() {
        return customTrustKeyStoreType;
    }

    public void setCustomTrustKeyStoreType(String customTrustKeyStoreType) {
        this.customTrustKeyStoreType = customTrustKeyStoreType;
    }

    public Integer getListenPortDomain() {
        return listenPortDomain;
    }

    public void setListenPortDomain(Integer listenPortDomain) {
        this.listenPortDomain = listenPortDomain;
    }

    
}
