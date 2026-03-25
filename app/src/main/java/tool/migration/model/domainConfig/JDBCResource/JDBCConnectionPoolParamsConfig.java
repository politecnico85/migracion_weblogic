package tool.migration.model.domainConfig.JDBCResource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JDBCConnectionPoolParamsConfig {
    private Integer inactiveConnectionTimeoutSeconds;
    private boolean testConnectionsOnReserve;
    private String fatalErrorCodes;
    private Integer initialCapacity;
    private Integer statementTimeout;
    private Integer countOfRefreshFailuresTillDisable;
    private Integer minCapacity;
    private Integer maxCapacity;
    private Integer secondsToTrustAnIdlePoolConnection;
    private Integer shrinkFrequencySeconds;
    private Integer connectionReserveTimeoutSeconds;
    private String testTableName;
    private String identity;

    @JsonProperty("identity")
    private void unpackCluster(Object identityData) {
        if (identityData instanceof List) {
            List<?> list = (List<?>) identityData;
            // Según tu JSON: ["clusters", "MicroCreditoSocial_es_cluster"]
            //"identity": [ "JDBCSystemResources","jdbc_FbDataSource","JDBCResource","JDBCConnectionPoolParams"]
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

    public Integer getInactiveConnectionTimeoutSeconds() {
        return inactiveConnectionTimeoutSeconds;
    }

    public void setInactiveConnectionTimeoutSeconds(Integer inactiveConnectionTimeoutSeconds) {
        this.inactiveConnectionTimeoutSeconds = inactiveConnectionTimeoutSeconds;
    }

    public boolean isTestConnectionsOnReserve() {
        return testConnectionsOnReserve;
    }

    public void setTestConnectionsOnReserve(boolean testConnectionsOnReserve) {
        this.testConnectionsOnReserve = testConnectionsOnReserve;
    }

    public String getFatalErrorCodes() {
        return fatalErrorCodes;
    }

    public void setFatalErrorCodes(String fatalErrorCodes) {
        this.fatalErrorCodes = fatalErrorCodes;
    }

    public Integer getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(Integer initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public Integer getStatementTimeout() {
        return statementTimeout;
    }

    public void setStatementTimeout(Integer statementTimeout) {
        this.statementTimeout = statementTimeout;
    }

    public Integer getCountOfRefreshFailuresTillDisable() {
        return countOfRefreshFailuresTillDisable;
    }

    public void setCountOfRefreshFailuresTillDisable(Integer countOfRefreshFailuresTillDisable) {
        this.countOfRefreshFailuresTillDisable = countOfRefreshFailuresTillDisable;
    }

    public Integer getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(Integer minCapacity) {
        this.minCapacity = minCapacity;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getSecondsToTrustAnIdlePoolConnection() {
        return secondsToTrustAnIdlePoolConnection;
    }

    public void setSecondsToTrustAnIdlePoolConnection(Integer secondsToTrustAnIdlePoolConnection) {
        this.secondsToTrustAnIdlePoolConnection = secondsToTrustAnIdlePoolConnection;
    }

    public Integer getShrinkFrequencySeconds() {
        return shrinkFrequencySeconds;
    }

    public void setShrinkFrequencySeconds(Integer shrinkFrequencySeconds) {
        this.shrinkFrequencySeconds = shrinkFrequencySeconds;
    }

    public Integer getConnectionReserveTimeoutSeconds() {
        return connectionReserveTimeoutSeconds;
    }

    public void setConnectionReserveTimeoutSeconds(Integer connectionReserveTimeoutSeconds) {
        this.connectionReserveTimeoutSeconds = connectionReserveTimeoutSeconds;
    }

    public String getTestTableName() {
        return testTableName;
    }

    public void setTestTableName(String testTableName) {
        this.testTableName = testTableName;
    }

    public String getIdentity() {
        return identity;
    }

    @Override
    public String toString() {
        return "JDBCConnectionPoolParamsConfig [inactiveConnectionTimeoutSeconds=" + inactiveConnectionTimeoutSeconds
                + ", testConnectionsOnReserve=" + testConnectionsOnReserve + ", fatalErrorCodes=" + fatalErrorCodes
                + ", initialCapacity=" + initialCapacity + ", statementTimeout=" + statementTimeout
                + ", countOfRefreshFailuresTillDisable=" + countOfRefreshFailuresTillDisable + ", minCapacity="
                + minCapacity + ", maxCapacity=" + maxCapacity + ", secondsToTrustAnIdlePoolConnection="
                + secondsToTrustAnIdlePoolConnection + ", shrinkFrequencySeconds=" + shrinkFrequencySeconds
                + ", connectionReserveTimeoutSeconds=" + connectionReserveTimeoutSeconds + ", testTableName="
                + testTableName + ", identity=" + identity + "]";
    }

    


    

}
