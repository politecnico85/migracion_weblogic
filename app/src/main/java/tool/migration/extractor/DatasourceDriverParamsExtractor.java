package tool.migration.extractor;

import java.util.Map;


import tool.migration.model.domainConfig.JDBCResource.JDBCDriverParamsConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class DatasourceDriverParamsExtractor {
    private final WebLogicService service;


    public DatasourceDriverParamsExtractor (WebLogicService service) {
        this.service = service;
    }


    public JDBCDriverParamsConfig getJDBCDriversParameters(String datasourceName){
        String json = service.getFromJDBCDriversParamsConfig(datasourceName, 
            Map.of(
                "links", "none",
                "fields",
                "password,"+
                "driverName,"+
                "url,"+
                "identity"
            )
        );
        return JsonUtil.mapObject(json, JDBCDriverParamsConfig.class);
    }
}
