package tool.migration.extractor.domainConfig;

import java.util.List;
import java.util.Map;


import tool.migration.model.domainConfig.ClusterConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class ClusterExtractor {
    private final WebLogicService service;

    public ClusterExtractor(WebLogicService service){
        this.service = service;
    }

    public List<ClusterConfig> getAllClustersConfig(){
         String json = service.getFromDomainConfig(
                "/clusters",
                Map.of(
                    "links","none",
                    "fields","identity"
                )
        );

        return JsonUtil.mapArray(json, "items", ClusterConfig.class);
    }

}
