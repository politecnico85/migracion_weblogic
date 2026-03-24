package tool.migration;



import tool.migration.client.WebLogicRestClient;
import tool.migration.extractor.DatasourceConfigExtractor;
import tool.migration.extractor.ServerConfigExtractor;
import tool.migration.extractor.ServerRuntimeExtractor;
import tool.migration.logging.AppLogger;
import tool.migration.model.DatasourceConfig;
import tool.migration.model.ManagedServerConfig;
import tool.migration.model.ManagedServerRuntime;
import tool.migration.service.WebLogicService;

import java.time.Duration;
import java.util.List;

public class ManagedServerQueryApp {

    public static void main(String[] args) {
        String Template= "http://%SERVER%:%PORT%/management/weblogic/12.2.1.4.0";
        String Server = "localhost";
        String Port = "7001";
        String BaseURL = Template.replace("%SERVER%", Server)
                     .replace("%PORT%", Port);
        WebLogicRestClient client =
                new WebLogicRestClient(
                        BaseURL,
                        "weblogic",
                        "clave",
                        Duration.ofSeconds(30)
                );

        WebLogicService service = new WebLogicService(client);
        
        ServerRuntimeExtractor extractor = new ServerRuntimeExtractor(service);
        List<ManagedServerRuntime> servers = extractor.getServerStates();
        for (ManagedServerRuntime managedServerRuntime : servers) {
                AppLogger.info(managedServerRuntime.getName());
        }

        ServerConfigExtractor configExtractor = new ServerConfigExtractor(service);
        List<ManagedServerConfig> serverConfig = configExtractor.getAllServerConfigs();
        for (ManagedServerConfig config : serverConfig){
                AppLogger.info(config.getName() + " : " + config.getListenPort());
        }

        DatasourceConfigExtractor datasourceExtractor = new DatasourceConfigExtractor(service);
        List<DatasourceConfig> datasources = datasourceExtractor.getAllDatasourceConfig();
        for (DatasourceConfig config : datasources) {
                AppLogger.info(config.getName());
        }
    }
}