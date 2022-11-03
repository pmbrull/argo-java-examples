import io.argoproj.workflow.ApiException;
import io.argoproj.workflow.apis.CronWorkflowServiceApi;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1CronWorkflow;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1CronWorkflowList;

public class ListCronWorkflows {

    public static void main(String[] args) {

        WorkflowClient workflowClient = new WorkflowClient();
        CronWorkflowServiceApi apiInstance = new CronWorkflowServiceApi(workflowClient.client);

        try {
            IoArgoprojWorkflowV1alpha1CronWorkflowList result = apiInstance.cronWorkflowServiceListCronWorkflows(
                    workflowClient.namespace,
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

            for (IoArgoprojWorkflowV1alpha1CronWorkflow cronWorkflow : result.getItems()) {
                System.out.println(cronWorkflow.getMetadata().getName());
            }
        } catch (ApiException e) {
            System.err.println("Exception when calling WorkflowServiceApi#workflowServiceCreateWorkflow");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }

    }
}
