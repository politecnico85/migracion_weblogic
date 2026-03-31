package tool.migration;




import tool.migration.creator.ServerCreator;
import tool.migration.extractor.DatasourceConfigExtractor;

import tool.migration.extractor.ServerConfigExtractor;
import tool.migration.extractor.ServerExtractor;
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

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class ManagedServerQueryApp {

    public static void main(String[] args) {
        //String Template= "http://%SERVER%:%PORT%/management/weblogic/12.2.1.4.0";
        String Server = "server";
        String WLS_User = "weblogic";
        String WLS_Pass = "clave";
        //String Port = "7001";
        //String BaseURL = Template.replace("%SERVER%", Server)
        //             .replace("%PORT%", Port);
        
        
        //AppLogger.setFormat(LogFormat.JSON); // logging estructurado

        //WebLogicConnectionInfo info = new WebLogicConnectionInfo();
       
        
        
        Credentials sourceCreds =
        Credentials.basic(WLS_User, WLS_Pass);

        Credentials targetCreds =
        Credentials.basic(WLS_User, WLS_Pass);

        WebLogicConnectionInfo sourceConnection = new WebLogicConnectionInfo(
                Server, // host
                7001,           // port
                false,          // ssl
                WebLogicVersion.WLS_12_2_1_4,       // version
                null,
                null

                );
        WebLogicConnectionInfo targetConnection = new WebLogicConnectionInfo(
                Server, // host
                9230,           // port
                true,          // ssl
                WebLogicVersion.WLS_14_1_2_0,       // version
                Path.of("D:\\Documents\\keystores\\truststore.jks"),
                "changeit".toCharArray()
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

        ServerCreator creatorTarget = new ServerCreator(serviceTarget);
        

        ServerConfigExtractor extractorTarget = new ServerConfigExtractor(serviceTarget);
        AppLogger.info("Consulta con SSL");
        List<ManagedServerConfig> targetConfig = extractorTarget.getAllServerConfigs();
        System.out.println((JsonUtil.toPrettyJson(targetConfig)));

              
//keytool -importcert -alias adminserver-lnxgye00dw55 -file adminserver-lnxgye00dw55.cer -keystore truststore.jks -storepass changeit -noprompt


        

        AppLogger.info("Creacion de Managed Servers en server: "+ Server );
        for (ManagedServerConfig config : serverConfig){
                AppLogger.info(config.getName());
                if (!"AdminServer".equals(config.getName())) {
                        AppLogger.info("Creacion de Managed Server" + config.getName() + " : " + config.getListenPort());
                        AppLogger.info(config.toString());
                        creatorTarget.crear(config.toJsonCustom() );
                }
                


        }
        
        


        /*
        DatasourceConfigExtractor datasourceExtractor = new DatasourceConfigExtractor(serviceSource);
        List<JDBCDatasourcesConfig> datasources = datasourceExtractor.getFromJDBCDatasourcesConfig();
        System.out.println(JsonUtil.toPrettyJson(datasources));
        

        
     
        JDBCConnectionPoolParamsConfig datasourceParameters = datasourceExtractor.getCJDBCConnectionPoolParameters("EvolDataSource");
        System.out.println((JsonUtil.toPrettyJson(datasourceParameters)));

     
    
        JDBCDriverParamsConfig driverConfig = datasourceExtractor.getJDBCDriversParameters("jdbc%2FConnWsScorePredictivoCredito");
        System.out.println((JsonUtil.toPrettyJson(driverConfig)));



        JDBCDatasourcePropertiesConfig properties = datasourceExtractor.getFromJDBCPropertiesConfig("jdbc%2FConnWsScorePredictivoCredito");

        System.out.println((JsonUtil.toPrettyJson(properties)));
        */
        }
        finally{
                
                // ✅ Limpieza (buena práctica)
                CorrelationContext.clear();

        }
    }
}