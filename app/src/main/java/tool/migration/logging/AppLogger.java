package tool.migration.logging;

import java.util.logging.*;

public final class AppLogger {

    private static final Logger LOG = Logger.getLogger("WebLogicBatch");

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);

        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord r) {

                String className = r.getSourceClassName();
                String method = r.getSourceMethodName();

                // Dejar solo el nombre simple de la clase
                if (className != null && className.contains(".")) {
                    className = className.substring(className.lastIndexOf('.') + 1);
                }

                return String.format(
                        "%1$tF %1$tT [%2$s] %3$s.%4$s - %5$s%n",
                        r.getMillis(),
                        r.getLevel().getName(),
                        className != null ? className : "UnknownClass",
                        method != null ? method : "unknownMethod",
                        r.getMessage()
                );
            }
        });

        LOG.setUseParentHandlers(false);
        LOG.addHandler(handler);
        LOG.setLevel(Level.INFO);
    }

    private AppLogger() {}

    public static void info(String msg)  { LOG.info(msg); }
    public static void debug(String msg) { LOG.fine(msg); }
    public static void warn(String msg)  { LOG.warning(msg); }
    public static void error(String msg, Throwable t) {
        LOG.log(Level.SEVERE, msg, t);
    }

    public static void setLevel(Level level) {
        LOG.setLevel(level);
        for (Handler h : LOG.getHandlers()) {
            h.setLevel(level);
        }
    }
}
