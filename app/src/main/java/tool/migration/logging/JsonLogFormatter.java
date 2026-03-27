package tool.migration.logging;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import tool.migration.util.JsonLogStyle;

public class JsonLogFormatter extends Formatter {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final JsonLogStyle style;

    public JsonLogFormatter(JsonLogStyle style) {
        this.style = style;
    }

    @Override
    public String format(LogRecord r) {
        try {

            String cls = r.getSourceClassName();
            String mtd = r.getSourceMethodName();

            if (cls != null && cls.contains(".")) {
                cls = cls.substring(cls.lastIndexOf('.') + 1);
            }
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("timestamp", Instant.ofEpochMilli(r.getMillis()));
            map.put("level", r.getLevel().getName());
            map.put("logger", r.getLoggerName());
            
            // ✅ CORRELATION ID
            map.put("correlationId", CorrelationContext.get());

            map.put("thread", Thread.currentThread().getName());
            map.put("class", cls != null ? cls : "Unknown");
            map.put("method", mtd != null ? mtd : "unknown");
            
            map.put("message", r.getMessage());

            if (r.getThrown() != null) {
                map.put("exception", r.getThrown().toString());
            }

            if (style == JsonLogStyle.PRETTY) {
                return MAPPER.writerWithDefaultPrettyPrinter()
                             .writeValueAsString(map) + "\n";
            } else {
                return MAPPER.writeValueAsString(map) + "\n"; // compact
            }

        } catch (Exception e) {
            return r.getMessage() + "\n";
        }
    }
}