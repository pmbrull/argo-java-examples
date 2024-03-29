import io.argoproj.workflow.ApiException;
import io.argoproj.workflow.apis.WorkflowServiceApi;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1Template;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1Workflow;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1WorkflowCreateRequest;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1WorkflowSpec;
import io.argoproj.workflow.models.StreamResultOfIoArgoprojWorkflowV1alpha1LogEntry;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

import java.util.List;

public class SimpleWorkflowLogs {
  public static void main(String[] args) {

    WorkflowClient workflowClient = new WorkflowClient();
    WorkflowServiceApi apiInstance = new WorkflowServiceApi(workflowClient.client);

    try {

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

      // Create the workflow
      IoArgoprojWorkflowV1alpha1Workflow created = apiInstance.workflowServiceCreateWorkflow(workflowClient.namespace, workflowCreateRequest);

      // Wait for the whalesay to finish
      Thread.sleep(30 * 1000);

      /*
      This will throw:
        Exception in thread "main" com.google.gson.JsonSyntaxException: com.google.gson.stream.MalformedJsonException:
        Use JsonReader.setLenient(true) to accept malformed JSON at line 2 column 2 path $
       */
      StreamResultOfIoArgoprojWorkflowV1alpha1LogEntry result = apiInstance.workflowServiceWorkflowLogs(
          workflowClient.namespace,
          created.getMetadata().getName(),
          created.getMetadata().getName(),
              "main",
              null,
              null,
              null,
              null,
              null,
              false,
              null,
              null,
              null,
              null,
              null
      );
      System.out.println(result);

    } catch (ApiException e) {
      System.err.println("Exception when calling WorkflowServiceApi#workflowServiceCreateWorkflow");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
