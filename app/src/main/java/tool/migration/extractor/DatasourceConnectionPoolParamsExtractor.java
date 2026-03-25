package tool.migration.extractor;

import java.util.List;
import java.util.Map;

import tool.migration.model.domainConfig.JDBCResource.JDBCConnectionPoolParamsConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class DatasourceConnectionPoolParamsExtractor {
    private final WebLogicService service;

    public DatasourceConnectionPoolParamsExtractor(WebLogicService service) {
        this.service = service;
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
}
