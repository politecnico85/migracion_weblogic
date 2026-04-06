package tool.migration.creator;



import tool.migration.logging.AppLogger;
import tool.migration.model.domainConfig.MachineConfig;
import tool.migration.service.WebLogicService;

public class MachineCreator {

    private final WebLogicService service;

    public MachineCreator(WebLogicService service) {
        this.service = service;
    }

    public void create(MachineConfig config) {

        AppLogger.info("Creando Machine: " + config.getName());

        EditSessionManager edit = new EditSessionManager(service);

        try {
            edit.startEdit();

            if (!exists(config.getName())) {
                service.post(
                    "/Machines",
                    """
                    {
                      "name": "%s"
                    }
                    """.formatted(config.getName())
                );
                AppLogger.info("Machine creada: " + config.getName());
            } else {
                AppLogger.info("Machine ya existe: " + config.getName());
            }

            // Configuración Node Manager
            service.put(
                "/Machines/" + config.getName() + "/NodeManager",
                """
                {
                  "listenAddress": "%s",
                  "listenPort": %d
                }
                """.formatted(
                    config.getListenAddress(),
                    config.getListenPort()
                )
            );

            edit.activate();

        } catch (Exception e) {
            AppLogger.error("Error creando Machine: " + config.getName(), e);
            edit.cancelEdit();
            throw e;
        }
    }

    private boolean exists(String machineName) {
        try {
            service.getFromEdit("/Machines/" + machineName, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}