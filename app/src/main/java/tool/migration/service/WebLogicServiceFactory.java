package tool.migration.service;



import tool.migration.client.WebLogicRestClient;
import tool.migration.model.Credentials;
import tool.migration.model.WebLogicConnectionInfo;
import tool.migration.util.WebLogicUrlBuilder;

public final class WebLogicServiceFactory {

    private WebLogicServiceFactory() {}

    public static WebLogicService create(
            WebLogicConnectionInfo connection,
            Credentials credentials
    ) {
        String baseUrl = WebLogicUrlBuilder.buildBaseUrl(connection);

        WebLogicRestClient client =
                new WebLogicRestClient(baseUrl, credentials, connection);

        return new WebLogicService(client);
    }
}
