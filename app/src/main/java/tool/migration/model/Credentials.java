package tool.migration.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public final class Credentials {
    private final String username;
    private final String password;

    private Credentials(String username, String password) {
        this.username = Objects.requireNonNull(username, "username requerido");
        this.password = Objects.requireNonNull(password, "password requerido");
    }

    public static Credentials basic(String username, String password) {
        return new Credentials(username, password);
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    /**
     * Header Authorization para HTTP BASIC
     */
    public String basicAuthHeader() {
        String token = username + ":" + password;
        String encoded = Base64.getEncoder()
                .encodeToString(token.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }

    @Override
    public String toString() {
        return "Credentials{username='" + username + "'}";

    }

}