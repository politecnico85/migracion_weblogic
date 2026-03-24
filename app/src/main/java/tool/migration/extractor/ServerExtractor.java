package tool.migration.extractor;



import tool.migration.logging.AppLogger;
import tool.migration.model.ManagedServerConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

import java.util.List;

public class ServerExtractor {

    private final WebLogicService service;

    public ServerExtractor(WebLogicService service) {
        this.service = service;
    }

    /** Lista de nombres desde domainConfig */
    public List<String> listServerNames() {

        AppLogger.debug("Obteniendo lista de servidores (edit tree)");
        String json = service.getFromDomainConfig("");
       
        return JsonUtil.readStringArray(json, "items[].name");
    }

    /** Configuración completa de un Managed Server */
    public ManagedServerConfig getServerConfig(String serverName) {

        ManagedServerConfig cfg = new ManagedServerConfig();
        cfg.setName(serverName);

        loadFromEdit(serverName, cfg);
        loadFromDomainConfig(serverName, cfg);

        return cfg;
    }

    public List<ManagedServerConfig> getAllServerConfigs() {
        return listServerNames()
                .stream()
                .map(this::getServerConfig)
                .toList();
    }

    // ---------------- PRIVATE ----------------

    private void loadFromEdit(String serverName, ManagedServerConfig cfg) {

        String json = service.getFromEdit("/Servers/" + serverName);

        cfg.setListenPort(JsonUtil.getInt(json, "listenPort"));
        //cfg.setCluster(JsonUtil.getString(json, "cluster"));
        //cfg.setMachine(JsonUtil.getString(json, "machine"));
        cfg.setKeyStores(JsonUtil.getString(json, "keyStores"));
        cfg.setCustomTrustKeyStoreFileName(
                JsonUtil.getString(json, "customTrustKeyStoreFileName")
        );
        cfg.setCustomTrustKeyStoreType(
                JsonUtil.getString(json, "customTrustKeyStoreType")
        );
    }

    private void loadFromDomainConfig(String serverName, ManagedServerConfig cfg) {

        String json = service.getFromDomainConfig("/Servers/" + serverName);
        cfg.setListenPort(
                JsonUtil.getInt(json, "listenPort")
        );
    }
}