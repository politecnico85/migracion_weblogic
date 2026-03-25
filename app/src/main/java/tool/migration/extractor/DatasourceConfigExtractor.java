package tool.migration.extractor;

import java.util.List;
import java.util.Map;

import tool.migration.model.domainConfig.JDBCResource.JDBCConnectionPoolParamsConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcePropertiesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDriverParamsConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class DatasourceConfigExtractor {
    private final WebLogicService service;

    public DatasourceConfigExtractor (WebLogicService service){
        this.service = service;
    }
    
    public List<JDBCDatasourcesConfig> getFromJDBCDatasourcesConfig(){
        String json = service.getFromJDBCDatasourcesConfig("null", 
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
        return JsonUtil.mapArray(json, "items", JDBCDatasourcesConfig.class);
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

    public JDBCConnectionPoolParamsConfig getCJDBCConnectionPoolParameters(String datasourceName) {
         String json = service.getFromJDBCConnectionPoolParamsConfig(datasourceName,
            Map.of(
                "links","none",
                "fields",
                "inactiveConnectionTimeoutSeconds"+
                "testConnectionsOnReserve"+
                "fatalErrorCodes"+
                "initialCapacity"+
                "statementTimeout"+
                "countOfRefreshFailuresTillDisable"+
                "minCapacity"+
                "maxCapacity"+
                "secondsToTrustAnIdlePoolConnection"+
                "shrinkFrequencySeconds"+
                "connectionReserveTimeoutSeconds"+
                "testTableName"+
                "identity"

            )
         );
         //return JsonUtil.getString(json, json)
         return JsonUtil.mapObject(json, JDBCConnectionPoolParamsConfig.class);
         //return JsonUtil.mapArray(json, "items", JDBCConnectionPoolParamsConfig.class);
    }


    public JDBCDatasourcePropertiesConfig getFromJDBCPropertiesConfig(String datasourceName){
        String json = service.getFromJDBCPropertiesConfig(datasourceName, 
                        Map.of(
                            "links","none",
                            "fields",
                            "encryptedValue,"+
                            "name,"+
                            "sysPropValue,"+
                            "value,"+
                            "identity"
                            )
        );
        return JsonUtil.mapObject(json,  JDBCDatasourcePropertiesConfig.class);
    }
}
