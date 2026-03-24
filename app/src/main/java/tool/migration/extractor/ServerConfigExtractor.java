package tool.migration.extractor;


import tool.migration.model.ManagedServerConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

import java.util.List;
import java.util.Map;

public class ServerConfigExtractor {

    private final WebLogicService service;

    public ServerConfigExtractor(WebLogicService service) {
        this.service = service;
    }

    public List<ManagedServerConfig> getAllServerConfigs() {

        String json = service.getFromServerConfig(
                "/servers",
                Map.of(
                    "links","none",
                    "fields",
                    "name,type,listenAddress,listenPort,cluster," +
                    "keyStores,customTrustKeyStoreType," +
                    "customTrustKeyStorePassPhrase," +
                    "customTrustKeyStoreFileName," +
                    "autoRestart,restartMax,notes,machine"
                )
        );

        return JsonUtil.mapArray(json, "items", ManagedServerConfig.class);
    }

    
}