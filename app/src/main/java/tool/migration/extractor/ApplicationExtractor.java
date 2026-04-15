package tool.migration.extractor;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import tool.migration.logging.AppLogger;
import tool.migration.model.ApplicationConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class ApplicationExtractor {

    private final WebLogicService service;

    public ApplicationExtractor(WebLogicService service) {
        this.service = service;
    }

    /**
     * Extrae TODAS las aplicaciones desplegadas en el dominio ORIGEN
     */
    public List<ApplicationConfig> extractAll() {

        AppLogger.info("Extrayendo aplicaciones desde el dominio ORIGEN");

        String json = service.getFromDomainConfig(
            "/appDeployments",
            null
        );

        List<ApplicationConfig> result = new ArrayList<>();

        JsonNode root = JsonUtil.toNode(json);
        JsonNode items = root.path("items");

        for (JsonNode item : items) {
            ApplicationConfig cfg = extractOne(item);
            if (cfg != null && cfg.isValid()) {
                result.add(cfg);
            }
        }

        AppLogger.info("Aplicaciones extraídas: " + result.size());
        return result;
    }

    /**
     * Extrae una aplicación individual
     */
    private ApplicationConfig extractOne(JsonNode item) {

        ApplicationConfig cfg = new ApplicationConfig();

        cfg.setName(item.path("name").asText(null));
        cfg.setSourcePath(item.path("sourcePath").asText(null));
        cfg.setStagingMode(item.path("stagingMode").asText("stage"));
        cfg.setDeploymentOrder(item.path("deploymentOrder").isMissingNode()
                ? null
                : item.path("deploymentOrder").asInt());

        // ===== tipo EAR/WAR =====
        detectType(cfg);

        // ===== targets =====
        cfg.setTargets(extractTargets(item));
     

        AppLogger.debug("Aplicación detectada: " + cfg.getName());
        return cfg;
    }

    /**
     * Determina si es EAR o WAR
     */
    private void detectType(ApplicationConfig cfg) {
        if (cfg.getSourcePath() == null) {
            cfg.setType("UNKNOWN");
            return;
        }

        if (cfg.getSourcePath().toLowerCase().endsWith(".ear")) {
            cfg.setType("EAR");
        } else if (cfg.getSourcePath().toLowerCase().endsWith(".war")) {
            cfg.setType("WAR");
        } else {
            cfg.setType("UNKNOWN");
        }
    }

    /**
     * Extrae los targets (clusters o servers)
     */
    private List<String> extractTargets(JsonNode item) {

        List<String> targets = new ArrayList<>();

        JsonNode targetsNode = item.path("targets");
        if (targetsNode.isArray()) {
            for (JsonNode t : targetsNode) {
                JsonNode identity = t.path("identity");
                if (identity.isArray() && identity.size() == 2) {
                    targets.add(identity.get(1).asText());
                }
            }
        }
        return targets;
    }

}
