package tool.migration.model;

public record WebLogicConnectionInfo(
        String host,
        int port,
        boolean ssl,
        WebLogicVersion version,
        java.nio.file.Path trustStorePath,
        char[] trustStorePassword

) {}