# Argo Java Client examples

Playground for Argo java.

- [Create Simple Workflow](#create-simple-workflow)
- [Argo Client Dependency](#argo-client-dependency)

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

TODO: currently only works on running workflows (pod must be up)

# Argo Client Dependency

We are using the following [client](https://github.com/argoproj/argo-workflows/tree/master/sdks/java).

The jar is hosted in GitHub Packages for [Argo Project](https://github.com/orgs/argoproj/packages). Setting up the
connection from Maven to GitHub requires to set up authentication as explained in the [docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

In order to simplify the process with this playground, we are pushing the jar together with the code. Note that the
testing has been done using Argo `v3.4.2`. Update the jar or properly set up your `settings.xml` and repositories
to handle the right version.

# Resources

- [Kubectl](https://github.com/kubernetes-client/java/blob/master/docs/kubectl-equivalence-in-java.md#kubectl-logs)
- [Argo Workflows Java](https://github.com/argoproj/argo-workflows/tree/master/sdks/java/client/docs)