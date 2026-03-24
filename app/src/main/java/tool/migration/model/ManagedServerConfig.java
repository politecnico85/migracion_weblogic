package tool.migration.model;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagedServerConfig {

    private String name;
    private String type;

    private String listenAddress;
    private Integer listenPort;

    private String cluster;

    

// 2. Jackson usará este método para procesar el nodo "cluster" del JSON
    @JsonProperty("cluster")
    private void unpackCluster(Object clusterData) {
        if (clusterData instanceof List) {
            List<?> list = (List<?>) clusterData;
            // Según tu JSON: ["clusters", "MicroCreditoSocial_es_cluster"]
            // El nombre real es el segundo elemento (índice 1)
            if (list.size() >= 2) {
                this.cluster = list.get(1).toString();
            } else if (list.size() == 1) {
                this.cluster = list.get(0).toString();
            }
        } else {
            // Por si viene null u otro formato
            this.cluster = clusterData != null ? clusterData.toString() : null;
        }
    }

    private String machine;

    @JsonProperty("machine")
    private void unpackCMachine(Object MachineData) {
        if (MachineData instanceof List) {
            List<?> list = (List<?>) MachineData;
            // Según tu JSON: ["clusters", "MicroCreditoSocial_es_cluster"]
            // El nombre real es el segundo elemento (índice 1)
            if (list.size() >= 2) {
                this.machine = list.get(1).toString();
            } else if (list.size() == 1) {
                this.machine = list.get(0).toString();
            }
        } else {
            // Por si viene null u otro formato
            this.cluster = MachineData != null ? MachineData.toString() : null;
        }
    }

    private String keyStores;
    private String customTrustKeyStoreType;
    private String customTrustKeyStorePassPhrase;
    private String customTrustKeyStoreFileName;

    private Boolean autoRestart;
    private Integer restartMax;

    private String notes;

    // getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public String getCluster() {
        return cluster;
    }

    /* 
    public void setCluster(String cluster) {
        this.cluster = cluster;
    }*/

    public String getMachine() {
        return machine;
    }

    //public void setMachine(String machine) {
    //    this.machine = machine;
    //}

    public String getKeyStores() {
        return keyStores;
    }

    public void setKeyStores(String keyStores) {
        this.keyStores = keyStores;
    }

    public String getCustomTrustKeyStoreType() {
        return customTrustKeyStoreType;
    }

    public void setCustomTrustKeyStoreType(String customTrustKeyStoreType) {
        this.customTrustKeyStoreType = customTrustKeyStoreType;
    }

    public String getCustomTrustKeyStorePassPhrase() {
        return customTrustKeyStorePassPhrase;
    }

    public void setCustomTrustKeyStorePassPhrase(String customTrustKeyStorePassPhrase) {
        this.customTrustKeyStorePassPhrase = customTrustKeyStorePassPhrase;
    }

    public String getCustomTrustKeyStoreFileName() {
        return customTrustKeyStoreFileName;
    }

    public void setCustomTrustKeyStoreFileName(String customTrustKeyStoreFileName) {
        this.customTrustKeyStoreFileName = customTrustKeyStoreFileName;
    }

    public Boolean getAutoRestart() {
        return autoRestart;
    }

    public void setAutoRestart(Boolean autoRestart) {
        this.autoRestart = autoRestart;
    }

    public Integer getRestartMax() {
        return restartMax;
    }

    public void setRestartMax(int restartMax) {
        this.restartMax = restartMax;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ManagedServerConfig [name=" + name + ", type=" + type + ", listenAddress=" + listenAddress
                + ", listenPort=" + listenPort + ", cluster=" + cluster + ", machine=" + machine + ", keyStores="
                + keyStores + ", customTrustKeyStoreType=" + customTrustKeyStoreType
                + ", customTrustKeyStorePassPhrase=" + customTrustKeyStorePassPhrase + ", customTrustKeyStoreFileName="
                + customTrustKeyStoreFileName + ", autoRestart=" + autoRestart + ", restartMax=" + restartMax
                + ", notes=" + notes + "]";
    }


    
 
}