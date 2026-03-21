package tool.migration.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

public class WebLogicRestClient {

    private final HttpClient client;
    private final String baseUrl;
    private final String authHeader;

    public WebLogicRestClient(
            String baseUrl,
            String username,
            String password,
            Duration timeout
    ) {
        this.baseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;

        this.client = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();

        String token = Base64.getEncoder()
                .encodeToString(
                        (username + ":" + password)
                                .getBytes(StandardCharsets.UTF_8)
                );
        this.authHeader = "Basic " + token;
    }

    // ---------------- GET ----------------

    public String get(String path, Map<String, String> query) {
        HttpRequest request = baseRequest(buildUrl(path, query))
                .GET()
                .build();
        return send(request);
    }

    // ---------------- POST ----------------

    public String post(String path, Map<String, String> query, String body) {
        HttpRequest request = baseRequest(buildUrl(path, query))
                .POST(HttpRequest.BodyPublishers.ofString(
                        body != null ? body : "{}"
                ))
                .build();
        return send(request);
    }

    // ---------------- PUT ----------------

    public String put(String path, Map<String, String> query, String body) {
        HttpRequest request = baseRequest(buildUrl(path, query))
                .PUT(HttpRequest.BodyPublishers.ofString(
                        body != null ? body : "{}"
                ))
                .build();
        return send(request);
    }

    // ---------------- DELETE ----------------

    public String delete(String path, Map<String, String> query) {
        HttpRequest request = baseRequest(buildUrl(path, query))
                .DELETE()
                .build();
        return send(request);
    }

    // ---------------- Helpers ----------------

    private HttpRequest.Builder baseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("X-Requested-By", "MigrationTool");
    }

    private String send(HttpRequest request) {
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            }

            throw new RuntimeException(
                    "HTTP " + response.statusCode() + ": " + response.body()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error invocando WebLogic REST", e);
        }
    }

    private String buildUrl(String path, Map<String, String> query) {
        StringBuilder sb = new StringBuilder(baseUrl).append(path);
        if (query != null && !query.isEmpty()) {
            sb.append("?");
            query.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
            sb.setLength(sb.length() - 1); // remove trailing &
        }
        return sb.toString();
    }
    
}
