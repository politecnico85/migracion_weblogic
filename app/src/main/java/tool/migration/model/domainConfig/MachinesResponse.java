package tool.migration.model.domainConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MachinesResponse {
    // Jackson busca la clave "items" en el JSON y la mete aquí




    private List<String> urls = new ArrayList<>();

    @JsonProperty("items")
    private void unpackUrls(List<Map<String, Object>> items) {
        if (items == null) return;

        for (Map<String, Object> item : items) {
            // Extraemos la lista de links de cada item
            List<Map<String, String>> links = (List<Map<String, String>>) item.get("links");
            
            if (links != null) {
                for (Map<String, String> link : links) {
                    // Solo guardamos el texto del link si es el 'self'
                    if ("self".equals(link.get("rel"))) {
                        this.urls.add(link.get("href"));
                    }
                }
            }
        }
    }

    public List<String> getUrls() {
        return urls;
    }

    /* 
    private List<MachineItem> items;

    public List<MachineItem> getItems() { return items; }
    public void setItems(List<MachineItem> items) { this.items = items; }
    */

}
