package tool.migration;



import tool.migration.client.WebLogicRestClient;
import tool.migration.extractor.ServerConfigExtractor;
import tool.migration.extractor.ServerRuntimeExtractor;
import tool.migration.logging.AppLogger;
import tool.migration.model.ManagedServerConfig;
import tool.migration.model.ManagedServerRuntime;
import tool.migration.service.WebLogicService;

import java.time.Duration;
import java.util.List;

public class ManagedServerQueryApp {

    public static void main(String[] args) {

        WebLogicRestClient client =
                new WebLogicRestClient(
                        "http://lnxgye00dw42:7001/management/weblogic/12.2.1.4.0",
                        "weblogic",
                        "wldesa2015",
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

        //ServerExtractor extractor = new ServerExtractor(service);

        //extractor.getAllServerConfigs()
        //        .forEach(System.out::println);
    }
}