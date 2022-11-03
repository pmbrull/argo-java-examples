import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileReader;
import java.io.IOException;
import io.kubernetes.client.extended.kubectl.Kubectl;

public class K8sClient {

    public final Kubectl client;

    public K8sClient() throws IOException {

        // String token = System.getenv("ARGO_TOKEN");

        // ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Configure API key authorization: BearerToken
        // ApiKeyAuth BearerToken = (ApiKeyAuth) defaultClient.getAuthentication("BearerToken");
        // Store the token as an env var or change it here
        // BearerToken.setApiKey(token);
        // BearerToken.setApiKeyPrefix("Bearer");

        //this.client = defaultClient;

        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";

        // loading the out-of-cluster config, a kubeconfig from file-system
        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        this.client = new Kubectl();

    }

}
