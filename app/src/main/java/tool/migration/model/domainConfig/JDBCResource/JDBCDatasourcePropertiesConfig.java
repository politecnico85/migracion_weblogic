package tool.migration.model.domainConfig.JDBCResource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JDBCDatasourcePropertiesConfig {
    private String encryptedValue;
    private String name;
    private String sysPropValue;
    private String value;

    private String identity;



    @JsonProperty("identity")
    private void unpackCluster(Object identityData) {
        if (identityData instanceof List) {
            List<?> list = (List<?>) identityData;
            // Según tu JSON: ["clusters", "MicroCreditoSocial_es_cluster"]
            //"identity": [ "JDBCSystemResources","jdbc_FbDataSource","JDBCResource","JDBCConnectionPoolParams"]
            // "identity": [ "JDBCSystemResources","jdbc\/ConnWsScorePredictivoCredito","JDBCResource","JDBCDriverParams","properties","properties","user" ]
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

    public String getEncryptedValue() {
        return encryptedValue;
    }

    public void setEncryptedValue(String encryptedValue) {
        this.encryptedValue = encryptedValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSysPropValue() {
        return sysPropValue;
    }

    public void setSysPropValue(String sysPropValue) {
        this.sysPropValue = sysPropValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIdentity() {
        return identity;
    }

    

    
}
