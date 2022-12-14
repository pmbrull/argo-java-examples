# Argo Java Client examples

Playground for Argo java. Make sure to have the proper permissions. [RBAC](#rbac) can help there.

- [Create Simple Workflow](#create-simple-workflow)
- [Simple Workflow Logs](#simple-workflow-logs-)
- [Create Cron Workflow](#create-cron-workflow)
- [List Cron Workflows](#list-cron-workflows)
- [Update Cron Workflow](#update-cron-workflows)
- [Argo Client Dependency](#argo-client-dependency)
- [RBAC](#rbac)

To get this running you need a local Argo cluster. The easiest way is to follow the [Getting Started](https://argoproj.github.io/argo-workflows/quick-start/) guide.

It will also require to have an access token ready for the namespace of your choosing. You can follow
this [doc](https://argoproj.github.io/argo-workflows/access-token/).

Make sure that your port is properly forwarded if testing locally!

## Create Simple Workflow

The first step is to set up the `whalesay` example. After running it you can check the results with:

```bash
❯ argo list -n <your-namespace>
NAME                STATUS      AGE   DURATION   PRIORITY
whalesay-8b2nq      Succeeded   21s   10s        0
whalesay-8f9pk      Succeeded   1m    10s        0
```

## Simple Workflow Logs ❌

Now that we ran a workflow, we might need to check its logs.

We can do so with the CLI as:

```bash
❯ argo logs whalesay-2ql8c -n <your-namespace>
whalesay-2ql8c:  ___________________________
whalesay-2ql8c: < This is an Argo Workflow! >
whalesay-2ql8c:  ---------------------------
whalesay-2ql8c:     \
whalesay-2ql8c:      \
whalesay-2ql8c:       \
whalesay-2ql8c:                     ##        .
whalesay-2ql8c:               ## ## ##       ==
whalesay-2ql8c:            ## ## ## ##      ===
whalesay-2ql8c:        /""""""""""""""""___/ ===
whalesay-2ql8c:   ~~~ {~~ ~~~~ ~~~ ~~~~ ~~ ~ /  ===- ~~~
whalesay-2ql8c:        \______ o          __/
whalesay-2ql8c:         \    \        __/
whalesay-2ql8c:           \____\______/
whalesay-2ql8c: time="2022-11-02T10:03:51.856Z" level=info msg="sub-process exited" argo=true error="<nil>"
```

We can also pick up the logs as

```bash
kubectl logs -l workflows.argoproj.io/workflow=whalesay-2ql8c -n <your-namespace>
```

Note the difference in behavior between logs and archived logs from [here](https://github.com/argoproj/argo-workflows/issues/8057#issuecomment-1058607350).

Let's replicate the same using the Java Client.

**TODO**: currently only works on running workflows (pod must be up)

## Create Cron Workflow

It's pretty cool how we can just create a definition of a Workflow, wrap it up inside the Cron Spec and
convert it to a Cron Workflow.

After running, we can check the created workflow by:

```bash
❯ argo cron list -n <your-namespace>
NAME                  AGE   LAST RUN   NEXT RUN   SCHEDULE    TIMEZONE   SUSPENDED
cron-whalesay-b4nqt   55s   N/A        23m        5 * * * *              false
```

## List Cron Workflows

We can also list using the SDK. Running the `ListCronWorkflows` class will give us the same result as above.

Running the list endpoint after a Cron Workflow has run, bring out the following error. Will need
to look into this one

```
Exception in thread "main" com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1291 path $.items[0].status.lastScheduledTime
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:270)
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$1.read(ReflectiveTypeAdapterFactory.java:161)
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:266)
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$1.read(ReflectiveTypeAdapterFactory.java:161)
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:266)
        at com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper.read(TypeAdapterRuntimeTypeWrapper.java:41)
        at com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter.read(CollectionTypeAdapterFactory.java:82)
        at com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter.read(CollectionTypeAdapterFactory.java:61)
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$1.read(ReflectiveTypeAdapterFactory.java:161)
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:266)
        at com.google.gson.Gson.fromJson(Gson.java:1058)
        at com.google.gson.Gson.fromJson(Gson.java:1016)
        at com.google.gson.Gson.fromJson(Gson.java:959)
        at io.argoproj.workflow.JSON.deserialize(JSON.java:146)
        at io.argoproj.workflow.ApiClient.deserialize(ApiClient.java:795)
        at io.argoproj.workflow.ApiClient.handleResponse(ApiClient.java:1001)
        at io.argoproj.workflow.ApiClient.execute(ApiClient.java:925)
        at io.argoproj.workflow.apis.CronWorkflowServiceApi.cronWorkflowServiceListCronWorkflowsWithHttpInfo(CronWorkflowServiceApi.java:777)
        at io.argoproj.workflow.apis.CronWorkflowServiceApi.cronWorkflowServiceListCronWorkflows(CronWorkflowServiceApi.java:748)
        at ListCronWorkflows.main(ListCronWorkflows.java:16)
Caused by: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1291 path $.items[0].status.lastScheduledTime
        at com.google.gson.stream.JsonReader.beginObject(JsonReader.java:395)
        at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$Adapter.read(ReflectiveTypeAdapterFactory.java:259
```

We need to update the JSON java class to add an adapter for the `Instant` type. 

## Update Cron Workflows

Let's work on picking up our new Cron Workflow and update the specs. Here we need to:

1. Get the Cron Workflow we want to update
2. Keep the metadata information, as we need name, resource version,...
3. Update the specs
4. Send the PUT call.

In our case we will be updating the workflow created before `my-cron-workflow`.

# Argo Client Dependency

We are using the following [client](https://github.com/argoproj/argo-workflows/tree/master/sdks/java).

The jar is hosted in GitHub Packages for [Argo Project](https://github.com/orgs/argoproj/packages). Setting up the
connection from Maven to GitHub requires to set up authentication as explained in the [docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

In order to simplify the process with this playground, we are pushing the jar together with the code. Note that the
testing has been done using Argo `v3.4.2`. Update the jar or properly set up your `settings.xml` and repositories
to handle the right version.

# RBAC

To get this going we need:
- A **Role** providing the permissions
- A **Service Account** from which we will create the Token
- A **Role Binding** to link the service account to the role

For example:

```bash
# Create role for workflows
kubectl create role test --verb=get,delete,watch,list,create,update --resource=workflows.argoproj.io -n <your-namespace>

# Create a service account
kubectl create sa test -n <your-namespace>

# Link the role to the SA
kubectl create rolebinding test --role=test --serviceaccount=<your-namespace>:test -n <your-namespace>

# Prepare the token
kubectl apply -n <your-namespace> -f - <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: test3.service-account-token
  annotations:
    kubernetes.io/service-account.name: test
type: kubernetes.io/service-account-token
EOF
ARGO_TOKEN="Bearer $(kubectl get secret -n saas test.service-account-token -o=jsonpath='{.data.token}' | base64 --decode)"
```

This will give us permissions for the workflows, but to run the examples we also need to access Pods and Cron Workflows.

## Pods RBAC

```bash
kubectl apply -n <your-namespace> -f - <<EOF
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-and-pod-logs-reader
rules:
- apiGroups: [""]
  resources: ["pods", "pods/log"]
  verbs: ["get", "list", "watch"]
EOF

kubectl create rolebinding pod-and-pod-logs-reader --role=pod-and-pod-logs-reader --serviceaccount=<your-namespace>:test -n <your-namespace>
```

## Cron Workflows RBAC

```bash
kubectl apply -n <your-namespace> -f - <<EOF
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: cron-workflow
rules:
- apiGroups: ["argoproj.io"]
  resources: ["cronworkflows"]
  verbs: ["get", "delete", "list", "update", "create", "watch", "patch"]
EOF

kubectl create rolebinding cron-workflow --role=cron-workflow --serviceaccount=<your-namespace>:test -n <your-namespace>
```

# Resources

- [Kubectl](https://github.com/kubernetes-client/java/blob/master/docs/kubectl-equivalence-in-java.md#kubectl-logs)
  - Just experimenting to see if it can help us get the log information.
- [Argo Workflows Java](https://github.com/argoproj/argo-workflows/tree/master/sdks/java/client/docs)
