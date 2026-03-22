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

    /** Configuración del dominio */
    public String getFromDomainConfig(String path) {
        //return client.get("/domainConfig" + path, null);
        return client.get("/domainRuntime/serverLifeCycleRuntimes"+ path, Map.of("links", "none", "fields", "state,name"));
        //Map<String, String> query = Map.of("links", "none", "fields", "state,name");
    }
}