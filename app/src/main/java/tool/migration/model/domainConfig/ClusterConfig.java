package tool.migration.model.domainConfig;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;



public class ClusterConfig {

    
    private String name;
    private String messagingMode; // unicast | multicast

    

    public String getName() {
        return name;
    }
    public String getMessagingMode() {
        return messagingMode;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMessagingMode(String messagingMode) {
        this.messagingMode = messagingMode;
    }

    

}

/* 
public class ClusterConfig {
    private String name;

    

    @JsonProperty("identity")
    private void unpackCluster(Object identityData) {
        if (identityData instanceof List) {
            List<?> list = (List<?>) identityData;
            // Según tu JSON: ["clusters", "MicroCreditoSocial_es_cluster"]
            // El nombre real es el segundo elemento (índice 1)
            if (list.size() >= 2) {
                this.name = list.get(1).toString();
            } else if (list.size() == 1) {
                this.name = list.get(0).toString();
            }
        } else {
            // Por si viene null u otro formato
            this.name = identityData != null ? identityData.toString() : null;
        }
    }



    public String getName() {
        return name;
    }

    
}

*/