package tool.migration.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import tool.migration.logging.AppLogger;

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

    
    public static String prettyJson(String rawJson) {
        try {
            Object json = MAPPER.readValue(rawJson, Object.class);
            return MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(json);
        } catch (Exception e) {
            throw new RuntimeException("Error prettifying JSON", e);
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


     /**i
     * Mapea un array JSON (ej: items) a una lista de objetos Java.
     *
     * @param json       JSON completo como String
     * @param arrayField nombre del campo array (ej: "items")
     * @param clazz      clase destino
     * @param <T>        tipo genérico
     * @return lista de objetos mapeados
     */
    public static <T> List<T> mapArray(
            String json,
            String arrayField,
            Class<T> clazz
    ) {
        try {
            JsonNode root = MAPPER.readTree(json);
            JsonNode arrayNode = root.get(arrayField);

            AppLogger.debug("JsonUtil.mapArray field=" + arrayField +
                    ", targetClass=" + clazz.getSimpleName());

            if (arrayNode == null || !arrayNode.isArray()) {
                throw new IllegalArgumentException(
                        "El campo '" + arrayField + "' no existe o no es un array"
                );
            }

            List<T> result = new ArrayList<>();

            for (JsonNode element : arrayNode) {
                result.add(MAPPER.treeToValue(element, clazz));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error mapeando array '" + arrayField +
                    "' a clase " + clazz.getSimpleName(), e
            );
        }
    }

    public static <T> T mapObject(String json, Class<T> clazz) {
        try {
            AppLogger.debug("JsonUtil.mapObject -> " + clazz.getSimpleName());
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(
                "Error mapeando JSON a clase " + clazz.getSimpleName(), e
            );
        }
    }

    
    public static JsonNode toNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Error parseando JSON", e);
        }
    }


}
