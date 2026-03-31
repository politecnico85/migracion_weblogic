package tool.migration.model;

public class ManageServerCreate {
    private String name;
    private String listenAddress;
    private Integer listenPort;
    private String cluster;

    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getListenAddress() {
        return listenAddress;
    }
    public void setListenAddress(String listenAddress) {
        this.listenAddress = listenAddress;
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

    
}
