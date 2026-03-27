package tool.migration.client;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.StringJoiner;

import tool.migration.logging.AppLogger;
import tool.migration.model.Credentials;

public class WebLogicRestClient {

        private final HttpClient httpClient;
        private final String baseUrl;
        //private final String authHeader;
        private final Credentials credentials;

         private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    
        public WebLogicRestClient(String baseUrl, Credentials credentials) {
                this.baseUrl = normalizeBaseUrl(baseUrl);
                this.credentials = credentials;

                this.httpClient = HttpClient.newBuilder()
                        .connectTimeout(DEFAULT_TIMEOUT)
                        .build();
        }


        // ==========================================================
        // ======================= PUBLIC API =======================
        // ==========================================================

        public String get(String path, Map<String, String> queryParams) {
                return send("GET", path, queryParams, null);
        }

        public String post(String path, Map<String, String> queryParams, String body) {
                return send("POST", path, queryParams, body);
        }

        public String put(String path, Map<String, String> queryParams, String body) {
                return send("PUT", path, queryParams, body);
        }

        public String delete(String path, Map<String, String> queryParams) {
                return send("DELETE", path, queryParams, null);
        }

        // ==========================================================
        // ===================== INTERNAL LOGIC =====================
        // ==========================================================

        private String send(
            String method,
            String path,
            Map<String, String> queryParams,
            String body
        ) {
                String url = buildUrl(path, queryParams);

                AppLogger.tech(method + " " + url);

                try {
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(DEFAULT_TIMEOUT)
                        .header("Authorization", credentials.basicAuthHeader())
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .header("X-Requested-By", "migration-tool");

                switch (method) {
                        case "POST" ->
                                builder.POST(HttpRequest.BodyPublishers.ofString(
                                        body != null ? body : ""
                                ));
                        case "PUT" ->
                                builder.PUT(HttpRequest.BodyPublishers.ofString(
                                        body != null ? body : ""
                                ));
                        case "DELETE" ->
                                builder.DELETE();
                        default ->
                                builder.GET();
                }

                HttpRequest request = builder.build();
                HttpResponse<String> response =
                        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return response.body();
                }

                AppLogger.tech(
                        "HTTP ERROR " + response.statusCode() + " → " + response.body()
                );

                throw new RuntimeException(
                        "HTTP " + response.statusCode() + " calling " + url
                );

                } catch (Exception ex) {
                throw new RuntimeException("Error executing HTTP " + method, ex);
                }
        }


        private String buildUrl(String path, Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder(baseUrl);

                if (!path.startsWith("/")) {
                sb.append("/");
                }
                sb.append(path);

                if (queryParams != null && !queryParams.isEmpty()) {
                sb.append("?");
                StringJoiner joiner = new StringJoiner("&");

                queryParams.forEach((k, v) -> {
                        joiner.add(
                                encode(k) + "=" + encode(v)
                        );
                });

                sb.append(joiner);
                }
                return sb.toString();
        }


        private static String encode(String value) {
                return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }

        private static String normalizeBaseUrl(String url) {
                return url.endsWith("/")
                        ? url.substring(0, url.length() - 1)
                        : url;
        }

    /* 
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
        URI requestUri = request.uri();
        String urlString = requestUri.toString();
   
        AppLogger.info( urlString);
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
                        .header("Authorization", credentials.basicAuthHeader())
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json");
        }

    
    private HttpRequest.Builder baseRequest(String url) {
        AppLogger.debug(url);
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
            AppLogger.debug(String.valueOf(response.statusCode()));
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
    */
}
