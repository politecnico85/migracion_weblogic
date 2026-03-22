package tool.migration;



import tool.migration.client.WebLogicRestClient;
import tool.migration.extractor.ServerExtractor;
import tool.migration.service.WebLogicService;

import java.time.Duration;

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
        ServerExtractor extractor = new ServerExtractor(service);

        extractor.getAllServerConfigs()
                .forEach(System.out::println);
    }
}