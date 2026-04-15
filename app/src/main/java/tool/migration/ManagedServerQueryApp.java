package tool.migration;



import tool.migration.creator.ApplicationCreator;
import tool.migration.creator.ClusterCreator;
import tool.migration.creator.ServerCreator;
import tool.migration.extractor.ApplicationExtractor;
import tool.migration.extractor.DatasourceConfigExtractor;

import tool.migration.extractor.ServerConfigExtractor;
import tool.migration.extractor.ServerExtractor;
import tool.migration.extractor.domainConfig.ClusterExtractor;
import tool.migration.extractor.domainConfig.MachineExtractor;
import tool.migration.extractor.domainRuntime.ServerRuntimeExtractor;
import tool.migration.logging.AppLogger;
import tool.migration.logging.CorrelationContext;
import tool.migration.model.ApplicationConfig;
import tool.migration.model.Credentials;
import tool.migration.model.ManagedServerConfig;
import tool.migration.model.ManagedServerRuntime;
import tool.migration.model.WebLogicConnectionInfo;
import tool.migration.model.WebLogicVersion;
import tool.migration.model.domainConfig.ClusterConfig;
import tool.migration.model.domainConfig.MachineConfig;
import tool.migration.model.domainConfig.MachineItem;
import tool.migration.model.domainConfig.MachinesResponse;
import tool.migration.model.domainConfig.JDBCResource.JDBCConnectionPoolParamsConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcePropertiesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDatasourcesConfig;
import tool.migration.model.domainConfig.JDBCResource.JDBCDriverParamsConfig;
import tool.migration.service.WebLogicService;
import tool.migration.service.WebLogicServiceFactory;
import tool.migration.util.JsonUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ManagedServerQueryApp {

    public static void main(String[] args) {
        //String Template= "http://%SERVER%:%PORT%/management/weblogic/12.2.1.4.0";

        //  setx WLS_SERVER_USER "weblogic"
        //  setx WLS_SERVER_USER ""
        //  setx WLS_SERVER_PASS_ORIGEN ""
        //  setx WLS_SERVER_PASS_TARGET ""
       
        String Server = System.getenv("WLS_SERVER_ENV");
        String WLS_User = System.getenv("WLS_SERVER_USER");
        String WLS_Pass = System.getenv("WLS_SERVER_PASS_ORIGEN");
        String WLS_Pass_Target = System.getenv("WLS_SERVER_PASS_TARGET");

        //  setx WLS_SERVER_USER "weblogic"

        //String Port = "7001";
        //String BaseURL = Template.replace("%SERVER%", Server)
        //             .replace("%PORT%", Port);
        
         //Server = "xxxxx";
         //WLS_User = "xxxxx";
         //WLS_Pass = "xxxx";
         //WLS_Pass_Target = "xxxxx";
        
        //AppLogger.setFormat(LogFormat.JSON); // logging estructurado

        //WebLogicConnectionInfo info = new WebLogicConnectionInfo();
       
        
        
        Credentials sourceCreds =
        Credentials.basic(WLS_User, WLS_Pass);

        Credentials targetCreds =
        Credentials.basic(WLS_User, WLS_Pass_Target);

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

               
                AppLogger.info("Extrayendo Informacion de CLUSTERS del servidor:" + Server);
                /*
                ClusterExtractor clusterExtractor = new ClusterExtractor(serviceSource);
                List<ClusterConfig> clusters = clusterExtractor.getAllClustersConfig();
                System.out.println((JsonUtil.toPrettyJson(clusters)));
                */


                AppLogger.info("Extrayendo Informacion de MACHINES del servidor:" + Server);
                getAllMachines(serviceSource);


                //AppLogger.info("Extrayendo Informacion de Managed Server del servidor:" + Server);
                //List<ManagedServerRuntime> servers = getServersLifeCicleRuntimes(serviceSource);
                //System.out.println((JsonUtil.toPrettyJson(servers)));

           

                
                List<ManagedServerConfig> serversSource = getAllServersConfig(serviceSource);
                System.out.println((JsonUtil.toPrettyJson(serversSource)));

                AppLogger.info("Creacion de Managed Servers in Target.");
                for (ManagedServerConfig item : serversSource) {
                        AppLogger.info("Creacion de Managed Server: "+ item.getName());
                        if (!"AdminServer".equals(item.getName())) {
                                createManagedServer(serviceTarget, item);
                        }
                }

                



                ServerConfigExtractor extractorTarget = new ServerConfigExtractor(serviceTarget);
                AppLogger.info("Consulta con SSL");
                List<ManagedServerConfig> targetConfig = extractorTarget.getAllServerConfigs();
                //System.out.println((JsonUtil.toPrettyJson(targetConfig)));

              
                //keytool -importcert -alias adminserver-lnxgye00dw55 -file adminserver-lnxgye00dw55.cer -keystore truststore.jks -storepass changeit -noprompt

                //createManagedServer(serviceTarget, Server);


                /*  Funciono
                AppLogger.info("Creacion de Managed Server: "+ Server );
                ManagedServerConfig config_server = new ManagedServerConfig();
                config_server.setName("AppServer1");
                config_server.setListenAddress(Server);
                config_server.setListenPort((7101));
                config_server.setNotes("Creado desde servicio REST");
                creatorTarget.create(config_server );
                */


                /*
                AppLogger.info("Creacion de Clusters: "+ Server );
                ClusterCreator clusterCreator = new ClusterCreator(serviceTarget);
                for (ClusterConfig clusterItem : clusters) {
                        AppLogger.info("Cluster: "+ clusterItem.getName() );
                        clusterCreator.create(clusterItem); 
                }
                */
                
                
                getAllDeployments(serviceSource);
                // Debug
                List<ApplicationConfig> apps = new ArrayList<>();
                apps.forEach(app ->
                        AppLogger.info(
                                app.getName() + " -> " + app.getSourcePath() + " targets=" + app.getTargets()
                        )
                );
                AppLogger.info(".... DESPLIEGUE DE APLICACIONES ....");

                

                /* 
                AppLogger.info("Creacion de Managed Servers en server: "+ Server );
                for (ManagedServerConfig config : serverConfig){
                        AppLogger.info(config.getName());
                        if (!"AdminServer".equals(config.getName())) {
                                AppLogger.info("Creacion de Managed Server" + config.getName() + " : " + config.getListenPort());
                                AppLogger.info(config.toString());
                                creatorTarget.create(config );
                        }
                        
                }
                */
        

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

    static List<ManagedServerRuntime> getServersLifeCicleRuntimes(WebLogicService serviceSource){
        ServerRuntimeExtractor extractor = new ServerRuntimeExtractor(serviceSource);
        List<ManagedServerRuntime> servers = extractor.getServerStates();
        return servers;          
    }

    static List<ManagedServerConfig> getAllServersConfig (WebLogicService service) {

        ServerConfigExtractor configExtractor = new ServerConfigExtractor(service);
        List<ManagedServerConfig> serversConfig = configExtractor.getAllServerConfigs();
        return serversConfig;
    }

     static void createManagedServer(WebLogicService service, ManagedServerConfig server) {
        ServerCreator creatorTarget = new ServerCreator(service);
        creatorTarget.create(server );
     }

     static   void  createManagedServer(WebLogicService serviceTarget, String Server ){
        ServerCreator creatorTarget = new ServerCreator(serviceTarget);
        AppLogger.info("Creacion de Managed Server: "+ Server );
        ManagedServerConfig config_server = new ManagedServerConfig();
        config_server.setName("AppServer1");
        config_server.setListenAddress(Server);
        config_server.setListenPort((7101));
        
        config_server.setNotes("Creado desde servicio REST");
        creatorTarget.create(config_server );
     }

     static void getAllMachines (WebLogicService serviceSource) {
        MachineExtractor machineExtractor = new MachineExtractor(serviceSource);
        MachinesResponse machines = machineExtractor.getAllMachinesConfig2();
        //List<MachineItem> listaDeItems = machines.getItems();
        System.out.println((JsonUtil.toPrettyJson(machines)));


        List<MachineConfig> machinesConfig = new ArrayList<MachineConfig>();
                for (String url : machines.getUrls()) {
                        int index = url.indexOf("/machines/");

                        if (index != -1) {
                                // 1. La parte del dominio (antes de /machines/)
                                String domainConfig = url.substring(0, index);
                                
                                // 2. La parte del recurso (desde /machines/ en adelante)
                                String machinePath = url.substring(index);

                                System.out.println("Base Domain: " + domainConfig);
                                System.out.println("Resource Path: " + machinePath);

                                MachineConfig machine = machineExtractor.getMachineConfig(machinePath + "/nodeManager");
                                machinesConfig.add(machine);

                                System.out.println((JsonUtil.toPrettyJson(machinesConfig)));
                        }
                        
                } 
        
     }

     

     static List<ClusterConfig>  getAllClusters(WebLogicService serviceSource) {

        ClusterExtractor clusterExtractor = new ClusterExtractor(serviceSource);
        List<ClusterConfig> clusters = clusterExtractor.extractAll();
        return clusters;
                //System.out.println((JsonUtil.toPrettyJson(clusters)));
     }

     static List<ApplicationConfig> getAllDeployments (WebLogicService serviceSource) {
        ApplicationExtractor appExtractor =  new ApplicationExtractor(serviceSource);

        List<ApplicationConfig> apps = appExtractor.extractAll();
        return apps;
     }

     static void DesplegarAppsTarget(WebLogicService serviceTarget, List<ApplicationConfig> apps) {
        ApplicationCreator appCreator = new ApplicationCreator(serviceTarget);
                for(ApplicationConfig appItem : apps) {
                        try{
                                String path = appItem.getSourcePath();
                                String despuesUpload = path.split("/upload/")[1];
                                appItem.setSourcePath("/exports/rest_api_domain/despliegues/" + despuesUpload);
                                AppLogger.info(appItem.getSourcePath());
                                appCreator.deploy(appItem);
                        }
                        catch(Exception e){
                             AppLogger.error("Error desplegando Aplication: " + appItem.getName(), e);   
                        }
                }
     }
                
}