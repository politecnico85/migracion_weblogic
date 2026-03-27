package tool.migration.util;

import tool.migration.model.WebLogicConnectionInfo;

public final class WebLogicUrlBuilder {

    private WebLogicUrlBuilder() {}

    public static String buildBaseUrl(WebLogicConnectionInfo info) {
        String protocol = info.ssl() ? "https" : "http";
        return String.format(
            "%s://%s:%d/management/weblogic/%s",
            protocol,
            info.host(),
            info.port(),
            info.version().path()
        );
    }
}
