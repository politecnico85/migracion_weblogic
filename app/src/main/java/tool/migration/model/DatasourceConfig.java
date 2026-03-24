package tool.migration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatasourceConfig {
    private String name;
    private String type;
    private String targets;

     @JsonProperty("targets")
    private void unpackTargets(Object targetsData) {
        if (targetsData instanceof List) {
            List<?> list = (List<?>) targetsData;
            // Según tu JSON: [{ "identity": [ "servers", "AppServerEslognotificainversion" ] }]
           
            // El nombre real es el segundo elemento (índice 1)
            List<?> listIdentity = (List<?>) list;
            if (listIdentity.size() >= 2) {
                this.targets = listIdentity.get(1).toString();
            } else if (list.size() == 1) {
                this.targets = listIdentity.get(0).toString();
            }
        } else {
            // Por si viene null u otro formato
            this.targets = targetsData != null ? targetsData.toString() : null;
        }
    }

     private String identity;

    @JsonProperty("identity")
    private void unpackCluster(Object identityData) {
        if (identityData instanceof List) {
            List<?> list = (List<?>) identityData;
            // Según tu JSON: ["clusters", "MicroCreditoSocial_es_cluster"]
            // El nombre real es el segundo elemento (índice 1)
            if (list.size() >= 2) {
                this.identity = list.get(1).toString();
            } else if (list.size() == 1) {
                this.identity = list.get(0).toString();
            }
        } else {
            // Por si viene null u otro formato
            this.identity = identityData != null ? identityData.toString() : null;
        }
    }


    private String descriptorFileName;


    private String password;
    private String driverName;
    private String url;
    private Integer minCapacity;
    private String testTableName;
    private Integer maxCapacity;
    private Integer testFrequencySeconds;


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
    public String getTargets() {
        return targets;
    }
    /*
    public void setTarget(String target) {
        this.target = target;
    }
    */
    public String getIdentity() {
        return identity;
    }
    /* 
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    */
    public String getDescriptorFileName() {
        return descriptorFileName;
    }
    public void setDescriptorFileName(String descriptorFileName) {
        this.descriptorFileName = descriptorFileName;
    }




    
    

}
