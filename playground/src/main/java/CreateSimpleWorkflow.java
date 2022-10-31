import io.argoproj.workflow.ApiClient;
import io.argoproj.workflow.ApiException;
import io.argoproj.workflow.Configuration;
import io.argoproj.workflow.apis.WorkflowServiceApi;
import io.argoproj.workflow.auth.ApiKeyAuth;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1Template;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1Workflow;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1WorkflowCreateRequest;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1WorkflowSpec;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

import java.util.List;

public class CreateSimpleWorkflow {
  public static void main(String[] args) {
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

    WorkflowServiceApi apiInstance = new WorkflowServiceApi(defaultClient);

    IoArgoprojWorkflowV1alpha1WorkflowCreateRequest workflowCreateRequest =
        new IoArgoprojWorkflowV1alpha1WorkflowCreateRequest();

    IoArgoprojWorkflowV1alpha1Workflow workflow = new IoArgoprojWorkflowV1alpha1Workflow();
    workflow.setKind("Workflow");
    workflow.setMetadata(new V1ObjectMeta().generateName("whalesay-"));
    workflow.setSpec(
        new IoArgoprojWorkflowV1alpha1WorkflowSpec()
            .entrypoint("whalesay")
            .templates(
                List.of(
                    new IoArgoprojWorkflowV1alpha1Template()
                        .name("whalesay")
                        .container(
                            new V1Container()
                                .image("docker/whalesay")
                                .command(List.of("cowsay"))
                                .args(List.of("This is an Argo Workflow!"))))));

    workflowCreateRequest.setWorkflow(workflow);

    try {
      IoArgoprojWorkflowV1alpha1Workflow result =
          apiInstance.workflowServiceCreateWorkflow(namespace, workflowCreateRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling WorkflowServiceApi#workflowServiceCreateWorkflow");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
