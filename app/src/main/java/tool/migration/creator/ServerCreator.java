package tool.migration.creator;



import java.util.LinkedHashMap;
import java.util.Map;

import tool.migration.logging.AppLogger;
import tool.migration.model.ManagedServerConfig;
import tool.migration.service.WebLogicService;

/**
 * Crea (e idempotentemente evita duplicar) Managed Servers en el dominio destino
 * usando el árbol de configuración de WebLogic (edit tree).
 *
 * Flujo:
 *   startEdit -> (create if not exists) -> set properties -> activate
 *
 * Notas:
 *  - Para un Server no se hace "targeting" como en DataSources; se setea cluster/machine.
 *  - Los campos nulos/no definidos NO se envían (payload minimal).
 */
public class ServerCreator {

    private final WebLogicService svc;

    public ServerCreator(WebLogicService svc) {
        this.svc = svc;
    }

    public void crear(String payload) {
        AppLogger.info("Creando/ajustando Managed Server: " + payload);
        try {
            String path = "/servers/";
            svc.post(path, payload);
        } catch (RuntimeException ex) {
            AppLogger.error("Fallo creando/ajustando server: " + payload, ex);
            // Importante: cancelar edición si algo falla en medio
            //try { new EditSessionManager(svc).cancelEdit(); } catch (Exception ignore) {}
            throw ex;
        }

    }

    /**
     * Crea o actualiza (parcialmente) un Managed Server en el dominio destino.
     * Es idempotente: si el Server ya existe, no intenta recrearlo.
     *
     * @param cfg Configuración del server a crear.
     */
    public void create(ManagedServerConfig cfg) {
        validate(cfg);

        EditSessionManager edit = new EditSessionManager(svc);

        AppLogger.info("Creando/ajustando Managed Server: " + cfg.getName());

        try {
            edit.startEdit();

            if (!exists(cfg.getName())) {
                // 1) Crear el MBean del Server
                svc.post("/servers", """
                {
                  "name": "%s"
                }
                """.formatted(cfg.getName()));
                AppLogger.info("Server creado: " + cfg.getName());
            } /*else {
                AppLogger.info("Server ya existe, se continúa con actualización: " + cfg.getName());
            }*/

            // 2) Aplicar propiedades (solo las no nulas)
            String path = "/servers/" + cfg.getName();

            // Construir payload con campos no nulos
            Map<String, Object> payload = new LinkedHashMap<>();
            putIfNotNull(payload, "listenAddress", cfg.getListenAddress());
            putIfNotNull(payload, "listenPort", cfg.getListenPort());
            putIfNotNull(payload, "cluster", cfg.getCluster());     // WLS resuelve referencia por nombre
            putIfNotNull(payload, "machine", cfg.getMachine());     // idem

            // keystores / trust
            //putIfNotNull(payload, "keyStores", cfg.getKeyStores());
            //putIfNotNull(payload, "customTrustKeyStoreType", cfg.getCustomTrustKeyStoreType());
            //putIfNotNull(payload, "customTrustKeyStorePassPhrase", cfg.getCustomTrustKeyStorePassPhrase());
            //putIfNotNull(payload, "customTrustKeyStoreFileName", cfg.getCustomTrustKeyStoreFileName());

            // HA / Restart
            //putIfNotNull(payload, "autoRestart", cfg.getAutoRestart());
            //putIfNotNull(payload, "restartMax", cfg.getRestartMax());

            // Observaciones
            putIfNotNull(payload, "notes", cfg.getNotes());

            if (!payload.isEmpty()) {
                String json = toJson(payload);
                svc.post(path, json);
                AppLogger.info("Propiedades aplicadas a: " + cfg.getName());
            } else {
                AppLogger.info("No hay propiedades para actualizar en: " + cfg.getName());
            }

            edit.activate();
            AppLogger.info("Cambios ACTIVADOS para server: " + cfg.getName());

        } catch (RuntimeException ex) {
            AppLogger.error("Fallo creando/ajustando server: " + cfg.getName(), ex);
            // Importante: cancelar edición si algo falla en medio
            try { new EditSessionManager(svc).cancelEdit(); } catch (Exception ignore) {}
            throw ex;
        }
    }

    /**
     * Elimina (rollback/cleanup) el Managed Server.
     * Útil en escenarios de Command/rollback.
     */
    public void delete(String serverName) {
        EditSessionManager edit = new EditSessionManager(svc);
        try {
            edit.startEdit();
            if (exists(serverName)) {
                svc.delete("/edit/Servers/" + serverName, null);
                AppLogger.warn("Server eliminado: " + serverName);
            } else {
                AppLogger.warn("Server no existe, no se elimina: " + serverName);
            }
            edit.activate();
        } catch (RuntimeException ex) {
            AppLogger.error("Fallo eliminando server: " + serverName, ex);
            try { edit.cancelEdit(); } catch (Exception ignore) {}
            throw ex;
        }
    }

    /**
     * Verifica existencia de un Server por nombre.
     */
    /**
    public boolean exists(String serverName) {
        try {
            svc.get("/edit/Servers/" + serverName, null);
            return true;
        } catch (RuntimeException ex) {
            // Si GET 404 (o similar) devolvió excepción del client -> se considera no existente
            return false;
        }
    }
         */

    public boolean exists(String serverName){
        try{
            svc.exists(serverName, null);
            return true;
        }catch(RuntimeException ex){
            return false;
        }
    }

    // =================== Helpers ===================

    private void validate(ManagedServerConfig cfg) {
        if (cfg == null) throw new IllegalArgumentException("cfg es requerido");
        if (cfg.getName() == null || cfg.getName().isBlank())
            throw new IllegalArgumentException("cfg.name es requerido");
        // No forzamos listenPort ni cluster/machine: se permite creación mínima + actualización parcial.
    }

    private static void putIfNotNull(Map<String, Object> map, String k, Object v) {
        if (v != null) map.put(k, v);
    }

    /**
     * Serializa un mapa simple a JSON sin dependencias adicionales
     * (si ya usas Jackson en JsonUtil, puedes reemplazar por JsonUtil.toPrettyJson(map) o similar).
     */
    private static String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (var e : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(escape(e.getKey())).append("\":");
            Object val = e.getValue();
            if (val instanceof Number || val instanceof Boolean) {
                sb.append(val);
            } else {
                sb.append("\"").append(escape(String.valueOf(val))).append("\"");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}