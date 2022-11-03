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
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.extended.kubectl.Kubectl;
import io.kubernetes.client.extended.kubectl.exception.KubectlException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.LogsApi;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SimpleWorkflowLogs {
  public static void main(String[] args) {

    WorkflowClient workflowClient = new WorkflowClient();

    WorkflowServiceApi apiInstance = new WorkflowServiceApi(workflowClient.client);

    String name = "whalesay-2ql8c";

    try {
      StreamResultOfIoArgoprojWorkflowV1alpha1LogEntry result = apiInstance.workflowServiceWorkflowLogs(
              "saas",
              name,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null
      );
      System.out.println(result);

      if (result == null) {
        K8sClient k8sClient = new K8sClient();

        // LogsApi k8sLogsApi = new LogsApi(k8sClient.client);
        // k8sLogsApi.logFileHandler("workflows.argoproj.io/workflow=whalesay-2ql8c");
        // CoreV1Api api = new CoreV1Api(k8sClient.client);

        // V1PodList list =
        //         api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        // for (V1Pod item : list.getItems()) {
        //   System.out.println(item.getMetadata().getName());
        // }

        List<V1Pod> pods = Kubectl.get(V1Pod.class)
                .namespace("saas")
                .execute();

        for (V1Pod item : pods) {
           System.out.println(item.getMetadata().getName());
        }

        InputStream logStream = Kubectl.log().namespace("saas").name("workflows.argoproj.io/workflow=whalesay-2ql8c").execute();
        System.out.println(logStream.toString());

      }

    } catch (ApiException e) {
      System.err.println("Exception when calling WorkflowServiceApi#workflowServiceCreateWorkflow");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    } catch (IOException | KubectlException e) {
      throw new RuntimeException(e);
    }
  }
}
