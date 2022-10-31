# Argo Java Client examples

Playground for Argo java.

- [Create Simple Workflow](#create-simple-workflow)
- [Argo Client Dependency](#argo-client-dependency)

To get this running you need a local Argo cluster. The easiest way is to follow the [Getting Started](https://argoproj.github.io/argo-workflows/quick-start/) guide.

It will also require to have an access token ready for the namespace of your choosing. You can follow
this [doc](https://argoproj.github.io/argo-workflows/access-token/).

## Create Simple Workflow

First step to set up the `whalesay` example. After running it you can check the results with:

```bash
❯ argo list -n <your-namespace>
NAME                STATUS      AGE   DURATION   PRIORITY
whalesay-8b2nq      Succeeded   21s   10s        0
whalesay-8f9pk      Succeeded   1m    10s        0
```

# Argo Client Dependency

We are using the following [client](https://github.com/argoproj/argo-workflows/tree/master/sdks/java).

The jar is hosted in GitHub Packages for [Argo Project](https://github.com/orgs/argoproj/packages). Setting up the
connection from Maven to GitHub requires to set up authentication as explained in the [docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

In order to simplify the process with this playground, we are pushing the jar together with the code. Note that the
testing has been done using Argo `v3.4.2`. Update the jar or properly set up your `settings.xml` and repositories
to handle the right version.
