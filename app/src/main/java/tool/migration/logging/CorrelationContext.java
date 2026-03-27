package tool.migration.logging;



import java.util.UUID;

/**
 * Mantiene el correlation-id por hilo (ThreadLocal).
 */
public final class CorrelationContext {

    private static final ThreadLocal<String> CORRELATION_ID =
            ThreadLocal.withInitial(() ->
                    UUID.randomUUID().toString().substring(0, 8)
            );

    private CorrelationContext() {}

    public static String get() {
        return CORRELATION_ID.get();
    }

    public static void set(String id) {
        CORRELATION_ID.set(id);
    }

    public static void clear() {
        CORRELATION_ID.remove();
    }

    /** Genera uno nuevo y lo asigna */
    public static void reset() {
        set(UUID.randomUUID().toString().substring(0, 8));
    }
}