package tool.migration.extractor;


import tool.migration.model.ManagedServerRuntime;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

import java.util.List;
import java.util.Map;

public class ServerRuntimeExtractor {

    private final WebLogicService service;

    public ServerRuntimeExtractor(WebLogicService service) {
        this.service = service;
    }

    public List<ManagedServerRuntime> getServerStates() {

        String json = service.getFromDomainRuntime(
                "/serverLifeCycleRuntimes",
                Map.of(
                    "links","none",
                    "fields","name,state"
                )
        );

        return JsonUtil.mapArray(json, "items", ManagedServerRuntime.class);
    }
}
