package tool.migration.service;


import java.util.Map;

import tool.migration.client.WebLogicRestClient;





public class WebLogicService {

    private final WebLogicRestClient client;

    public WebLogicService(WebLogicRestClient client) {
        this.client = client;
    }

    /** Configuración editable */
    public String getFromEdit(String path) {
        return client.get("/edit" + path, null);
    }

    public String getFromDomainRuntime(String path, Map<String,String> q) {
        return client.get("/domainRuntime" + path, q);
    }
    /** Configuración del dominio */
    public String getFromDomainConfig(String path,  Map<String,String> q) {
        return client.get("/domainConfig"+ path, q);
    }


    public String getFromDomainConfig(String path) {
        //return client.get("/domainConfig" + path, null);
        return client.get("/domainRuntime/serverLifeCycleRuntimes"+ path, Map.of("links", "none", "fields", "state,name"));
        //Map<String, String> query = Map.of("links", "none", "fields", "state,name");
    }


    public String getClusterConfig(String path, Map<String,String> map){
        return client.get("/domainConfig/clusters", 
            Map.of(
                "links", "none",
                "fields",
                "identity"
            ) );  
    }

    /*
    public String getMAchineConfig(String path, Map<String,String> map) {
        return client.get("/domainConfig/machines", 
            Map.of(
                "links", "none",
                "fields",
                "identity"
            ) );  
    }
    */

    public String getFromServerConfig(String string, Map<String,String> map){
        return client.get("/serverConfig/servers", 
                Map.of(
                    "links", "none", 
                    "fields", 
                    "name," +
                    "type," +
                    "listenAddress," +
                    "listenPort," +
                    "cluster," +
                    "keyStores," +
                    "customTrustKeyStoreType," +
                    "customTrustKeyStorePassPhrase," +
                    "customTrustKeyStoreFileName," +
                    "autoRestart," +
                    "restartMax," +
                    "notes," +
                    "machine"
                ));
        //serverConfig/servers?links=none&fields=name,type,listenAddress,listenPort,cluster,keyStores,customTrustKeyStoreType,customTrustKeyStorePassPhrase,customTrustKeyStoreFileName,autoRestart,restartMax,machine,notes
    }

    public String getFromJDBCDatasourcesConfig(String string, Map<String, String> map) {
        return client.get("/domainConfig/JDBCSystemResources",
                Map.of(
                    "links", "none", 
                    "fields", 
                    "identity,"+
                    "name,"+
                    "type,"+
                    "descriptorFileName,"+
                    "targets"
                ));
    }


    public String getFromJDBCConnectionPoolParamsConfig(String datasourceName, Map<String, String> map ){
        String template = "/domainConfig/JDBCSystemResources/#DATASOURCE#/JDBCResource/JDBCConnectionPoolParams";
        String path = template.replace("#DATASOURCE#", datasourceName);
        return client.get(path, 
            Map.of(
                "links", "none",
                "fields",
                "inactiveConnectionTimeoutSeconds,"+
                "testConnectionsOnReserve,"+
                "fatalErrorCodes,"+
                "initialCapacity,"+
                "statementTimeout,"+
                "countOfRefreshFailuresTillDisable,"+
                "minCapacity,"+
                "maxCapacity,"+
                "secondsToTrustAnIdlePoolConnection,"+
                "shrinkFrequencySeconds,"+
                "connectionReserveTimeoutSeconds,"+
                "testTableName,"+
                "identity"
            )    
        );
    }

    public String getFromJDBCDriversParamsConfig(String datasourceName, Map<String, String> map ){
        String template = "/domainConfig/JDBCSystemResources/#DATASOURCE#/JDBCResource/JDBCDriverParams";
        String path = template.replace("#DATASOURCE#", datasourceName);
        return client.get(path, 
            Map.of(
                "links", "none",
                "fields",
                "password,"+
                "driverName,"+
                "url,"+
                "identity"
            )    
        );
    }

    public String getFromJDBCPropertiesConfig(String datasourceName, Map<String, String> map ){
        String template = "/domainConfig/JDBCSystemResources/#DATASOURCE#/JDBCResource/JDBCDriverParams/properties/properties/user";
        String path = template.replace("#DATASOURCE#", datasourceName);
        return client.get(path, 
            Map.of(
                "links", "none",
                "fields",
                "encryptedValue,"+
                "name,"+
                "sysPropValue,"+
                "value,"+
                "identity"
            )    
        );
    }


    // ==========================================================
    // ========================== EDIT ==========================
    // ==========================================================

    public String exists(String path, Map<String, String> query) {
        return client.get("/edit" + normalize(path), query);
    }

    /**
     * Lee información desde el árbol edit (poco usado, pero disponible).
     */
    public String getFromEdit(String path, Map<String, String> query) {
        return client.get("/edit" + normalize(path), query);
    }

    /**
     * Crea recursos en el árbol edit.
     */
    public String post(String path, String body) {
        return client.post("/edit" + normalize(path), null, body);
    }

    /**
     * Actualiza recursos en el árbol edit.
     */
    public String put(String path, String body) {
        return client.put("/edit" + normalize(path), null, body);
    }

    /**
     * Elimina recursos en el árbol edit.
     */
    public String delete(String path, Map<String, String> query) {
        return client.delete("/edit" + normalize(path), query);
    }

    // ==========================================================
    // ====================== HELPERS ===========================
    // ==========================================================

    private static String normalize(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        return path.startsWith("/") ? path : "/" + path;
    }
}