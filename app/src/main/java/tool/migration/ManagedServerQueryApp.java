package tool.migration;



import tool.migration.client.WebLogicRestClient;
import tool.migration.extractor.DatasourceConfigExtractor;

import tool.migration.extractor.ServerConfigExtractor;
import tool.migration.extractor.ServerRuntimeExtractor;
import tool.migration.logging.AppLogger;
import tool.migration.logging.LogFormat;
import tool.migration.model.ManagedServerConfig;
import tool.migration.model.ManagedServerRuntime;
import tool.migration.model.domainConfig.JDBCResource.JDBCConnectionPoolParamsConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcePropertiesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDriverParamsConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

import java.time.Duration;
import java.util.List;

public class ManagedServerQueryApp {

    public static void main(String[] args) {
        String Template= "http://%SERVER%:%PORT%/management/weblogic/12.2.1.4.0";
        String Server = "servidor";
        String Port = "7001";
        String BaseURL = Template.replace("%SERVER%", Server)
                     .replace("%PORT%", Port);
        WebLogicRestClient client =
                new WebLogicRestClient(
                        BaseURL,
                        "weblogic",
                        "clase",
                        Duration.ofSeconds(30)
                );
        AppLogger.setFormat(LogFormat.JSON); // logging estructurado

        WebLogicService service = new WebLogicService(client);
        
        ServerRuntimeExtractor extractor = new ServerRuntimeExtractor(service);
        List<ManagedServerRuntime> servers = extractor.getServerStates();
        System.out.println((JsonUtil.toPrettyJson(servers)));
        /*
        for (ManagedServerRuntime managedServerRuntime : servers) {
                AppLogger.info(managedServerRuntime.getName());
        }
        */

        ServerConfigExtractor configExtractor = new ServerConfigExtractor(service);
        List<ManagedServerConfig> serverConfig = configExtractor.getAllServerConfigs();
        System.out.println((JsonUtil.toPrettyJson(serverConfig)));
        /*
        for (ManagedServerConfig config : serverConfig){
                AppLogger.info(config.getName() + " : " + config.getListenPort());
        }
        */

        DatasourceConfigExtractor datasourceExtractor = new DatasourceConfigExtractor(service);
        List<JDBCDatasourcesConfig> datasources = datasourceExtractor.getFromJDBCDatasourcesConfig();
        System.out.println(JsonUtil.toPrettyJson(datasources));
        /*
        for (JDBCDatasourcesConfig config : datasources) {
                AppLogger.info(config.getName());

        } */

        
        //DatasourceConnectionPoolParamsExtractor datasourceConnectionParameters = new DatasourceConnectionPoolParamsExtractor(service);
        JDBCConnectionPoolParamsConfig datasourceParameters = datasourceExtractor.getCJDBCConnectionPoolParameters("EvolDataSource");
        System.out.println((JsonUtil.toPrettyJson(datasourceParameters)));

        //DatasourceDriverParamsExtractor driversExtractor = new DatasourceDriverParamsExtractor(service);
    
        JDBCDriverParamsConfig driverConfig = datasourceExtractor.getJDBCDriversParameters("jdbc%2FConnWsScorePredictivoCredito");
        System.out.println((JsonUtil.toPrettyJson(driverConfig)));


        //DatasourceConfigExtractor extractorProps = new DatasourceConfigExtractor(service);
        JDBCDatasourcePropertiesConfig properties = datasourceExtractor.getFromJDBCPropertiesConfig("jdbc%2FConnWsScorePredictivoCredito");

        System.out.println((JsonUtil.toPrettyJson(properties)));
    }
}