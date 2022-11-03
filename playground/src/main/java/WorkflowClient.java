import io.argoproj.workflow.ApiClient;
import io.argoproj.workflow.ApiException;
import io.argoproj.workflow.Configuration;
import io.argoproj.workflow.apis.WorkflowServiceApi;
import io.argoproj.workflow.auth.ApiKeyAuth;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1Template;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1Workflow;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1WorkflowCreateRequest;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1WorkflowSpec;
import io.argoproj.workflow.models.StreamResultOfIoArgoprojWorkflowV1alpha1LogEntry;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;


public class WorkflowClient {

    public final ApiClient client;
    public final String host;
    public final String namespace;


    public WorkflowClient() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        String host = System.getenv("ARGO_HOST");  // e.g., https://localhost:2746
        String token = System.getenv("ARGO_TOKEN");  // e.g., eyJhbGciOiJSUzI1N...5sbw
        String namespace = System.getenv("ARGO_NAMESPACE");  // e.g., argo

        defaultClient.setBasePath(host);
        defaultClient.setVerifyingSsl(false); // we don't have any local SSL. Tune it accordingly

        // Configure API key authorization: BearerToken
        ApiKeyAuth BearerToken = (ApiKeyAuth) defaultClient.getAuthentication("BearerToken");

        // Store the token as an env var or change it here
        BearerToken.setApiKey(token);
        BearerToken.setApiKeyPrefix("Bearer");

        this.client = defaultClient;
        this.host = host;
        this.namespace = namespace;
    }

}
