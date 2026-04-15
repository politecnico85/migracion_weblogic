package tool.migration.creator;



import tool.migration.logging.AppLogger;
import tool.migration.model.ApplicationConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonPayloadBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationCreator {

    private final WebLogicService service;

    public ApplicationCreator(WebLogicService service) {
        this.service = service;
    }

    public void deploy(ApplicationConfig app) {

        if (!app.hasTargets()) {
            AppLogger.warn("Aplicación sin targets, se omite: " + app.getName());
            return;
        }

        EditSessionManager edit = new EditSessionManager(service);

        AppLogger.info("Desplegando aplicación: " + app.getName());

        try {
            edit.startEdit();

            if (!exists(app.getName())) {
                createDeployment(app);
            } else {
                AppLogger.info("Aplicación ya existe, se omite creación: " + app.getName());
            }

            edit.activate();
            AppLogger.info("Aplicación desplegada: " + app.getName());

        } catch (Exception e) {
            AppLogger.error("Error desplegando aplicación: " + app.getName(), e);
            edit.cancelEdit();
            throw e;
        }
    }

    private void createDeployment(ApplicationConfig app) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", app.getName());
        payload.put("sourcePath", app.getSourcePath());
        payload.put("stagingMode",
                app.getStagingMode() != null ? app.getStagingMode() : "stage"
        );
        
        payload.put("targets", buildTargets(app.getTargets()));
        //payload.put("targets", app.getTargets());

        if (app.getDeploymentOrder() != null) {
            payload.put("deploymentOrder", app.getDeploymentOrder());
        }
        AppLogger.info("PayLoad" + JsonPayloadBuilder.toJson(payload));
        service.post(
            "/appDeployments",
            JsonPayloadBuilder.toJson(payload)
        );
    }

    private List<Map<String, Object>> buildTargets(List<String> targetNames) {

        List<Map<String, Object>> targets = new ArrayList<>();

        for (String t : targetNames) {

            Map<String, Object> target = new HashMap<>();

            // Por ahora asumimos clusters (lo más común)
            target.put("identity", List.of("clusters", t));

            targets.add(target);
        }

        return targets;
    }

    private boolean exists(String appName) {
        try {
            service.getFromEdit("/appDeployments/" + appName, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
