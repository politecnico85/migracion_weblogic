package tool.migration;



import tool.migration.client.WebLogicRestClient;
import tool.migration.extractor.DatasourceConfigExtractor;

import tool.migration.extractor.ServerConfigExtractor;
import tool.migration.extractor.ServerRuntimeExtractor;
import tool.migration.logging.AppLogger;
import tool.migration.logging.CorrelationContext;
import tool.migration.model.Credentials;
import tool.migration.model.ManagedServerConfig;
import tool.migration.model.ManagedServerRuntime;
import tool.migration.model.WebLogicConnectionInfo;
import tool.migration.model.WebLogicVersion;
import tool.migration.model.domainConfig.JDBCResource.JDBCConnectionPoolParamsConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcePropertiesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDriverParamsConfig;
import tool.migration.service.WebLogicService;
import tool.migration.service.WebLogicServiceFactory;
import tool.migration.util.JsonUtil;

import java.time.Duration;
import java.util.List;

public class ManagedServerQueryApp {

    public static void main(String[] args) {
        //String Template= "http://%SERVER%:%PORT%/management/weblogic/12.2.1.4.0";
        //String Server = "server55";
        //String Port = "7001";
        //String BaseURL = Template.replace("%SERVER%", Server)
        //             .replace("%PORT%", Port);
        
        /* 
        WebLogicRestClient client =
                new WebLogicRestClient(
                        BaseURL,
                        "weblogic",
                        "wldesa2015",
                        Duration.ofSeconds(30)
                );
        */
        //AppLogger.setFormat(LogFormat.JSON); // logging estructurado

        //WebLogicConnectionInfo info = new WebLogicConnectionInfo();
       
        
        
        Credentials sourceCreds =
        Credentials.basic("weblogic", "clave");

        Credentials targetCreds =
        Credentials.basic("weblogic", "clave");

        WebLogicConnectionInfo sourceConnection = new WebLogicConnectionInfo(
                "server01", // host
                7001,           // port
                false,          // ssl
                WebLogicVersion.WLS_12_2_1_4       // version
                );
        WebLogicConnectionInfo targetConnection = new WebLogicConnectionInfo(
                "server01", // host
                9310,           // port
                false,          // ssl
                WebLogicVersion.WLS_14_1_1_0       // version
                );

      WebLogicService serviceSource  = WebLogicServiceFactory.create(
                                        sourceConnection,
                                        sourceCreds
                                        );

        WebLogicService serviceTarget  = WebLogicServiceFactory.create(
                                        targetConnection,
                                        targetCreds
                                        );

        
        // ✅ Un correlation-id por ejecución
        CorrelationContext.reset();


        try {

        


        //WebLogicService service = new WebLogicService(client);
        AppLogger.info("Extrayendo Informacion de Managed Server del servidor:" + Server);
        ServerRuntimeExtractor extractor = new ServerRuntimeExtractor(serviceSource);
        List<ManagedServerRuntime> servers = extractor.getServerStates();
        System.out.println((JsonUtil.toPrettyJson(servers)));
        /*
        for (ManagedServerRuntime managedServerRuntime : servers) {
                AppLogger.info(managedServerRuntime.getName());
        }
        */

        ServerConfigExtractor configExtractor = new ServerConfigExtractor(serviceSource);
        List<ManagedServerConfig> serverConfig = configExtractor.getAllServerConfigs();
        System.out.println((JsonUtil.toPrettyJson(serverConfig)));
        /*
        for (ManagedServerConfig config : serverConfig){
                AppLogger.info(config.getName() + " : " + config.getListenPort());
        }
        */

        DatasourceConfigExtractor datasourceExtractor = new DatasourceConfigExtractor(serviceSource);
        List<JDBCDatasourcesConfig> datasources = datasourceExtractor.getFromJDBCDatasourcesConfig();
        System.out.println(JsonUtil.toPrettyJson(datasources));
        

        
     
        JDBCConnectionPoolParamsConfig datasourceParameters = datasourceExtractor.getCJDBCConnectionPoolParameters("EvolDataSource");
        System.out.println((JsonUtil.toPrettyJson(datasourceParameters)));

     
    
        JDBCDriverParamsConfig driverConfig = datasourceExtractor.getJDBCDriversParameters("jdbc%2FConnWsScorePredictivoCredito");
        System.out.println((JsonUtil.toPrettyJson(driverConfig)));



        JDBCDatasourcePropertiesConfig properties = datasourceExtractor.getFromJDBCPropertiesConfig("jdbc%2FConnWsScorePredictivoCredito");

        System.out.println((JsonUtil.toPrettyJson(properties)));

        }
        finally{
                
                // ✅ Limpieza (buena práctica)
                CorrelationContext.clear();

        }
    }
}