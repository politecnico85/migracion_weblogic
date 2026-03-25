package tool.migration.model.domainConfig.JDBCResource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JDBCDriverParamsConfig {
    private String password;
    private String driverName;
    private String url;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdentity() {
        return identity;
    }

    



    
}
