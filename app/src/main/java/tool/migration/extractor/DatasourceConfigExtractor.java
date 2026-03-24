package tool.migration.extractor;

import java.util.List;
import java.util.Map;

import tool.migration.model.DatasourceConfig;

import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class DatasourceConfigExtractor {
    private final WebLogicService service;

    public DatasourceConfigExtractor (WebLogicService service){
        this.service = service;
    }
    
    public List<DatasourceConfig> getAllDatasourceConfig(){
        String json = service.getFromDatasourceConfig("null", 
                        Map.of(
                            "links","none",
                            "fields",
                            "identity,"+
                            "name,"+
                            "type,"+
                            "descriptorFileName,"+
                            "targets"
                            )
        );
        return JsonUtil.mapArray(json, "items", DatasourceConfig.class);
    }
}
