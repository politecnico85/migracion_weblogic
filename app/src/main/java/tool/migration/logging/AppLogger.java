package tool.migration.logging;

import java.util.logging.*;

import tool.migration.util.JsonLogStyle;

public final class AppLogger {

    //private static final Logger LOG = Logger.getLogger("MigrationLogger");

    private static final Logger OPS =
        Logger.getLogger("tool.migration.log.OPERATION");

    private static final Logger TECH =
        Logger.getLogger("tool.migration.log.TECHNICAL");

    static {
        configure(OPS, Level.INFO);
        configure(TECH, Level.FINE); // técnico
    }


    private static void configure(Logger log, Level level) {
        log.setUseParentHandlers(false);
        log.setLevel(level);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        handler.setFormatter(new JsonLogFormatterBK(JsonLogStyle.COMPACT));

        log.addHandler(handler);
    }


    //private static final ConsoleHandler HANDLER = new ConsoleHandler();

    /* 
    static {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.FINE);

        HANDLER.setLevel(Level.FINE);
        HANDLER.setFormatter(new SimpleFormatter()); // default
        LOG.addHandler(HANDLER);

        // ✅ Formato por demanda desde env / JVM
        applyFormatFromConfig();
    }
    */

    private AppLogger() {}

    // ===================== PUBLIC API =====================

    public static void info(String msg) {
        OPS.info(msg);
    }

    public static void debug(String msg) {
        OPS.fine(msg);
    }

    public static void warn(String msg) {
        OPS.warning(msg);
    }

    public static void error(String msg, Throwable t) {
        OPS.log(Level.SEVERE, msg, t);
    }

    // ================= TÉCNICOS =================
    public static void tech(String msg) {
        TECH.fine(msg);
    }

    public static void tech(String msg, Throwable t) {
        TECH.log(Level.FINE, msg, t);
    }


    // ===================== FORMAT CONTROL =====================


    

    /** Cambia el formato en runtime */

    /* 
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

    */
}

