package tool.migration.creator;

import tool.migration.service.WebLogicService;
import tool.migration.logging.AppLogger;
import tool.migration.model.domainConfig.ClusterConfig;


/**
 * Crea Clusters en el dominio DESTINO usando WebLogic REST.
 *
 * Reglas:
 *  - POST (NO PUT)
 *  - edit/changeManager
 *  - payload mínimo
 *  - idempotente
 */
public class ClusterCreator {

    private final WebLogicService service;

    public ClusterCreator(WebLogicService service) {
        this.service = service;
    }

    public void create(ClusterConfig config) {

        AppLogger.info("Creando Cluster: " + config.getName());

        EditSessionManager edit = new EditSessionManager(service);

        try {
            edit.startEdit();

            if (!exists(config.getName())) {

                String payload = """
                {
                  "name": "%s",
                  "clusterMessagingMode": "%s"
                }
                """.formatted(
                    config.getName(),
                    config.getMessagingMode() != null
                        ? config.getMessagingMode()
                        : "unicast"
                );

                service.post("/clusters", payload);

                AppLogger.info("Cluster creado: " + config.getName());

            } else {
                AppLogger.info("Cluster ya existe, se omite creación: " + config.getName());
            }

            edit.activate();

        } catch (Exception e) {
            AppLogger.error("Error creando Cluster: " + config.getName(), e);
            edit.cancelEdit();
            throw e;
        }
    }

    /**
     * Verifica si el cluster existe en el dominio DESTINO.
     *
     * 404 = no existe (esperado)
     * 200 = existe
     */
    private boolean exists(String clusterName) {
        try {
            service.getFromEdit("/clusters/" + clusterName, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
