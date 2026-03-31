package tool.migration.creator;



import tool.migration.logging.AppLogger;
import tool.migration.service.WebLogicService;

/**
 * Encapsula el manejo de sesión de edición en WebLogic:
 *  startEdit / activate / cancelEdit
 */
public class EditSessionManager {

    private final WebLogicService svc;

    public EditSessionManager(WebLogicService svc) {
        this.svc = svc;
    }

    public void startEdit() {
        AppLogger.info("startEdit()");
        svc.post("/startEdit", null);
    }

    public void activate() {
        AppLogger.info("activate()");
        svc.post("/edit/changeManager/activate", null);
    }

    public void cancelEdit() {
        AppLogger.warn("cancelEdit()");
        svc.post("/edit/changeManager/cancelEdit", null);
    }
}
