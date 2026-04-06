package tool.migration.extractor.domainConfig;

import java.util.List;
import java.util.Map;

import tool.migration.model.domainConfig.MachineConfig;
import tool.migration.model.domainConfig.MachinesResponse;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class MachineExtractor {
    private final WebLogicService service;

    public MachineExtractor(WebLogicService service){
        this.service = service;
    }

    public MachineConfig getMachineConfig(String path){
         String json = service.getFromDomainConfig(
                path,
                Map.of(
                    "links","none",
                    "fields","name,listenPort,listenAddress"
                    
                )
        );

        return JsonUtil.mapObject(json, MachineConfig.class);
    }

    public MachinesResponse getAllMachinesConfig2(){
         String json = service.getFromDomainConfig(
                "/machines",
                Map.of(
                    "fields","none"
                )
        );
        return JsonUtil.mapObject(json, MachinesResponse.class);
        //return JsonUtil.mapArray(json, "items", MachinesConfig.class);
    }

    

}
