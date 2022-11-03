import com.google.gson.Gson;
import io.argoproj.workflow.ApiException;
import io.argoproj.workflow.apis.CronWorkflowServiceApi;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1CreateCronWorkflowRequest;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1CronWorkflow;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1CronWorkflowList;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1CronWorkflowSpec;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1Template;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1UpdateCronWorkflowRequest;
import io.argoproj.workflow.models.IoArgoprojWorkflowV1alpha1WorkflowSpec;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

import java.util.List;

public class UpdateCronWorkflow {

    public static void main(String[] args) {

        WorkflowClient workflowClient = new WorkflowClient();
        CronWorkflowServiceApi apiInstance = new CronWorkflowServiceApi(workflowClient.client);

        IoArgoprojWorkflowV1alpha1UpdateCronWorkflowRequest cronWorkflowUpdateRequest =
                new IoArgoprojWorkflowV1alpha1UpdateCronWorkflowRequest();

        String cronWorkflowName = "my-cron-workflow";

        try {

            IoArgoprojWorkflowV1alpha1CronWorkflow cronWorkflow =
                    apiInstance.cronWorkflowServiceGetCronWorkflow(workflowClient.namespace, cronWorkflowName, null);

            cronWorkflow.setSpec(
                new IoArgoprojWorkflowV1alpha1CronWorkflowSpec()
                    .schedule("10 * * * *")  // new schedule
                    .workflowSpec(
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
                                                .args(List.of("This is an updated Argo CRON Workflow!"))))))  // new args
            );

            cronWorkflowUpdateRequest.setCronWorkflow(cronWorkflow);
            System.out.println(new Gson().toJson(cronWorkflowUpdateRequest));

            IoArgoprojWorkflowV1alpha1CronWorkflow result = apiInstance.cronWorkflowServiceUpdateCronWorkflow(
                    workflowClient.namespace,
                    cronWorkflowName,
                    cronWorkflowUpdateRequest
            );
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
