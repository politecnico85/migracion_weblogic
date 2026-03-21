package tool.migration.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
     private static final ObjectMapper MAPPER = new ObjectMapper();

     public static String getString(String json, String field) {
        try {
            JsonNode node = MAPPER.readTree(json).get(field);
            return node != null && !node.isNull() ? node.asText() : null;
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo campo: " + field, e);
        }
    }
    public static int getInt(String json, String field) {
        try {
            JsonNode node = MAPPER.readTree(json).get(field);
            return node != null ? node.asInt() : 0;
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo campo: " + field, e);
        }
    }

    public static List<String> readStringArray(String json, String path) {
        try {
            JsonNode node = MAPPER.readTree(json);
            return MAPPER.convertValue(
                    node.at("/items"),
                    new TypeReference<List<String>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo array: " + path, e);
        }
    }

    public static String toPrettyJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando JSON", e);
        }
    }

     /**
      * Retorna nombres de items cuyo array "targets" contiene el valor indicado.
     */
     public static List<String> readConditionalArray(
          String json,
          String arrayPath,
          String conditionField,
          String containsValue) {

          try {
               JsonNode root = MAPPER.readTree(json).get(arrayPath);
               List<String> result = new ArrayList<>();

               for (JsonNode item : root) {
                    JsonNode targets = item.get(conditionField);
                    if (targets != null) {
                         for (JsonNode t : targets) {
                              if (t.asText().contains(containsValue)) {
                              result.add(item.get("name").asText());
                              break;
                              }
                         }
                    }
               }
               return result;

          } catch (Exception e) {
               throw new RuntimeException("Error filtrando array condicionado", e);
          }
     }

}
