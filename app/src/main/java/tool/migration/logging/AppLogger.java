package tool.migration.logging;

import java.util.logging.*;

public final class AppLogger {

    private static final Logger LOG = Logger.getLogger("MigrationLogger");
    private static final ConsoleHandler HANDLER = new ConsoleHandler();

    static {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.FINE);

        HANDLER.setLevel(Level.FINE);
        HANDLER.setFormatter(new SimpleFormatter()); // default
        LOG.addHandler(HANDLER);

        // ✅ Formato por demanda desde env / JVM
        applyFormatFromConfig();
    }

    private AppLogger() {}

    // ===================== PUBLIC API =====================

    public static void info(String msg) {
        LOG.info(msg);
    }

    public static void debug(String msg) {
        LOG.fine(msg);
    }

    public static void warn(String msg) {
        LOG.warning(msg);
    }

    public static void error(String msg, Throwable t) {
        LOG.log(Level.SEVERE, msg, t);
    }

    // ===================== FORMAT CONTROL =====================

    /** Cambia el formato en runtime */
    public static void setFormat(LogFormat format) {
        switch (format) {
            case JSON -> HANDLER.setFormatter(new JsonLogFormatter());
            case TEXT -> HANDLER.setFormatter(new SimpleFormatter());
        }
    }

    private static void applyFormatFromConfig() {
        String value = System.getProperty("app.log.format");
        if (value == null) {
            value = System.getenv("APP_LOG_FORMAT");
        }

        if ("JSON".equalsIgnoreCase(value)) {
            setFormat(LogFormat.JSON);
        } else {
            setFormat(LogFormat.TEXT);
        }
    }
}

