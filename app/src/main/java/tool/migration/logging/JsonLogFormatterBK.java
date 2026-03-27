package tool.migration.logging;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import tool.migration.util.JsonLogStyle;

public class JsonLogFormatterBK extends Formatter {

    private static final DateTimeFormatter ISO =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());


    /* 
    @Override
    public String format(LogRecord r) {
        return String.format(
            "{\"timestamp\":\"%s\",\"level\":\"%s\",\"message\":\"%s\"}%n",
            Instant.ofEpochMilli(r.getMillis()),
            r.getLevel(),
            r.getMessage().replace("\"","\\\"")
        );
    }
    */

    
    private final JsonLogStyle style;

    public JsonLogFormatterBK(JsonLogStyle style) {
        this.style = style;
    }

    
    @Override
    public synchronized String format(LogRecord r) {

        String cls = r.getSourceClassName();
        String mtd = r.getSourceMethodName();

        if (cls != null && cls.contains(".")) {
            cls = cls.substring(cls.lastIndexOf('.') + 1);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"timestamp\":\"").append(ISO.format(Instant.ofEpochMilli(r.getMillis()))).append("\",");
        sb.append("\"level\":\"").append(r.getLevel().getName()).append("\",");
        sb.append("\"thread\":\"").append(Thread.currentThread().getName()).append("\",");
        sb.append("\"logger\":\"").append(r.getLoggerName()).append("\",");
        sb.append("\"class\":\"").append(cls != null ? cls : "Unknown").append("\",");
        sb.append("\"method\":\"").append(mtd != null ? mtd : "unknown").append("\",");
        sb.append("\"message\":\"").append(escape(r.getMessage())).append("\"");

        if (r.getThrown() != null) {
            sb.append(",\"exception\":\"")
              .append(escape(r.getThrown().toString()))
              .append("\"");
        }

        sb.append("}\n");

        
        return sb.toString();
    }
        

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}


