package tool.migration.util;



import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonPayloadBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonPayloadBuilder() {}

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error construyendo JSON payload", e);
        }
    }
}
