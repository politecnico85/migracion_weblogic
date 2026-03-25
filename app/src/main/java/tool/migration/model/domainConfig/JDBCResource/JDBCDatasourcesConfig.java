package tool.migration.model.domainConfig.JDBCResource;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JDBCDatasourcesConfig {
    private String name;
    private String type;
    private String targetServer;

    /* 
     @JsonProperty("targets")
    private void unpackTargets(Object targetsData) {
        if (targetsData instanceof List) {
            List<?> list = (List<?>) targetsData;
            // Según el JSON: [{ "identity": [ "servers", "AppServerEslognotificainversion" ] }]
           
            // El nombre real es el segundo elemento (índice 1)
            List<?> listIdentity = (List<?>) list;
            if (listIdentity.size() > 1) {
                this.targets = listIdentity.get(1).toString();
            } else if (list.size() == 1) {
                this.targets = listIdentity.get(0).toString();
            }
        } else {
            // Por si viene null u otro formato
            this.targets = targetsData != null ? targetsData.toString() : null;
        }
    }
     */

    @JsonProperty("targets")
    private void unpackTargetServer(List<Map<String, Object>> targets) {
        if (targets != null && !targets.isEmpty()) {
            // 1. Obtenemos el primer objeto del array targets
            Map<String, Object> firstTarget = targets.get(0);
            
            // 2. Extraemos la lista 'identity'
            List<String> identity = (List<String>) firstTarget.get("identity");
            
            // 3. El nombre del servidor está en la posición 1 (índice 1)
            if (identity != null && identity.size() >= 2) {
                this.targetServer = identity.get(1);
            }
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
    public String getTargetServer() {
        return targetServer;
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
