package tool.migration.service;

import java.util.List;
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
    public String getFromDomainConfig(String path) {
        //return client.get("/domainConfig" + path, null);
        return client.get("/domainRuntime/serverLifeCycleRuntimes"+ path, Map.of("links", "none", "fields", "state,name"));
        //Map<String, String> query = Map.of("links", "none", "fields", "state,name");
    }

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
                    "notes"
                ));
        //serverConfig/servers?links=none&fields=name,type,listenAddress,listenPort,cluster,keyStores,customTrustKeyStoreType,customTrustKeyStorePassPhrase,customTrustKeyStoreFileName,autoRestart,restartMax,machine,notes
    }
}