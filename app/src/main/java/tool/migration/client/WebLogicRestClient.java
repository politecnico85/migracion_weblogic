package tool.migration.client;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Map;
import java.util.StringJoiner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import tool.migration.logging.AppLogger;
import tool.migration.model.Credentials;
import tool.migration.model.WebLogicConnectionInfo;

public class WebLogicRestClient {

    private final HttpClient httpClient;
    private final String baseUrl;
    private final Credentials credentials;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    // 🔴 CAMBIO: ahora recibe WebLogicConnectionInfo
    public WebLogicRestClient(
            String baseUrl,
            Credentials credentials,
            WebLogicConnectionInfo connInfo
    ) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.credentials = credentials;

        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(DEFAULT_TIMEOUT);

        // 🔴 SSL REAL (equivalente a curl --cacert)
        if (connInfo.ssl() && connInfo.trustStorePath() != null) {
            AppLogger.info(
                "Configurando SSL usando truststore: " + connInfo.trustStorePath()
            );
            builder.sslContext(
                buildSSLContextFromJKS(
                    connInfo.trustStorePath(),
                    connInfo.trustStorePassword()
                )
            );
        }

        this.httpClient = builder.build();
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
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
                case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
                case "DELETE" -> builder.DELETE();
                default -> builder.GET();
            }

            HttpResponse<String> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            }

            AppLogger.tech("HTTP ERROR " + response.statusCode() + " → " + response.body());
            throw new RuntimeException("HTTP " + response.statusCode() + " calling " + url);

        } catch (Exception ex) {
            throw new RuntimeException("Error executing HTTP " + method, ex);
        }
    }

    // ==========================================================
    // ====================== SSL HELPERS =======================
    // ==========================================================

    private static SSLContext buildSSLContextFromJKS(Path jksPath, char[] password) {
        try (InputStream in = Files.newInputStream(jksPath)) {
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(in, password);

            TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            tmf.init(trustStore);

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tmf.getTrustManagers(), null);
            return ctx;

        } catch (Exception e) {
            throw new RuntimeException("Error configurando SSL con JKS", e);
        }
    }

    private String buildUrl(String path, Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder(baseUrl);

        if (!path.startsWith("/")) sb.append("/");
        sb.append(path);

        if (queryParams != null && !queryParams.isEmpty()) {
            sb.append("?");
            StringJoiner joiner = new StringJoiner("&");
            queryParams.forEach((k, v) ->
                joiner.add(encode(k) + "=" + encode(v))
            );
            sb.append(joiner);
        }
        return sb.toString();
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String normalizeBaseUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}