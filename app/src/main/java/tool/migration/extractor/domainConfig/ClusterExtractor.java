


package tool.migration.extractor.domainConfig;



import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import tool.migration.logging.AppLogger;
import tool.migration.model.domainConfig.ClusterConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class ClusterExtractor {

    private final WebLogicService service;

    public ClusterExtractor(WebLogicService service) {
        this.service = service;
    }

    /**
     * Extrae los clusters del dominio ORIGEN.
     * Solo atributos mínimos y seguros.
     */
    public List<ClusterConfig> extractAll() {

        AppLogger.info("Extrayendo Clusters del dominio ORIGEN");

        String json = service.getFromDomainConfig("/clusters", null);
        JsonNode root = JsonUtil.toNode(json);
        JsonNode items = root.path("items");

        List<ClusterConfig> clusters = new ArrayList<>();

        for (JsonNode item : items) {

            String name = item.path("name").asText(null);
            if (name == null) {
                continue;
            }

            ClusterConfig cfg = new ClusterConfig();
            cfg.setName(name);

            // ✅ Este es el único atributo funcional que vale la pena migrar
            cfg.setMessagingMode(
                item.path("clusterMessagingMode").asText("unicast")
            );

            clusters.add(cfg);

            AppLogger.debug(
                "Cluster detectado: " + name +
                " (messagingMode=" + cfg.getMessagingMode() + ")"
            );
        }

        AppLogger.info("Clusters extraídos: " + clusters.size());
        return clusters;
    }
}

/*

package tool.migration.extractor.domainConfig;

import java.util.List;
import java.util.Map;


import tool.migration.model.domainConfig.ClusterConfig;
import tool.migration.service.WebLogicService;
import tool.migration.util.JsonUtil;

public class ClusterExtractor {
    private final WebLogicService service;

    public ClusterExtractor(WebLogicService service){
        this.service = service;
    }

    public List<ClusterConfig> getAllClustersConfig(){
         String json = service.getFromDomainConfig(
                "/clusters",
                Map.of(
                    "links","none",
                    "fields","identity"
                )
        );

        return JsonUtil.mapArray(json, "items", ClusterConfig.class);
    }

}
 */