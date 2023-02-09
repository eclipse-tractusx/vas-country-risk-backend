# Country Risk Helm Chart

This Helm Chart deploys the Country Risk Backend service to a Kubernetes environment.

## Prerequisites

* [Kubernetes Cluster](https://kubernetes.io/)
* [Helm](https://helm.sh/docs/)

In an existing Kubernetes cluster the application can be deployed with the following command:

```bash
helm install release_name ./charts/country-risk-backend-charts --namespace your_namespace
```

This will install a new release of the Country Risk in the given namespace.
On default values this release deploys the latest image tagged as `latest` from the repository's GitHub Container Registry.
The application is run on default profile (you can run it on a dev profile or local).
Additionally, the Helm deployment contains a PostgreSQL database which the Country Risk connects to.

Per default ingress is disabled, as well as no authentication for endpoints.

By giving your own values file you can configure the Helm deployment of the Country Risk freely:

```bash
helm install release_name ./charts/country-risk-backend-charts --namespace your_namespace -f ./path/to/your/values.yaml
```

In the following sections you can have a look at the most important configuration options.

## Image Tag

Per default, the Helm deployment references a certain Country Risk release version where the newest Helm release points to the newest Country Risk version.
This is a stable tag pointing to a fixed release version of the Country Risk.
For your deployment you might want to follow the latest application releases instead.

In your values file you can overwrite the default tag:

```yaml
image:
  tag: "latest"
```

## Profiles

You can also activate Spring profiles in which the Country Risk should be run.


```yaml
configmap:
  # Specifies the spring profile
  data:
    spring_profiles_active: dev
```

## Ingress

You can specify your own ingress configuration for the Helm deployment to make the Country Risk available over Ingress.
Note that you need to have the appropriate Ingress controller installed in your cluster first.
For example, consider a Kubernetes cluster with an [Ingress-Nginx](https://kubernetes.github.io/ingress-nginx/) installed.
An Ingress configuration for the Country Risk deployment could look like this:

```yaml
ingress:
  enabled: true
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/backend-protocol: "HTTP"
  hosts:
    - host: vas-country-risk-backend.your-domain.net
      paths:
        - path: /
          pathType: Prefix
```

## Country Risk Configuration

The Helm deployment comes with the ability to configure the Country Risk application directly over the values file.
This way you are able to overwrite any configuration property of the `application.yaml` files.



## Helm Dependencies

On default, the Helm deployment also contains a PostgresSQL and PGAdmin deployment.
You can configure these deployments in your value file as well.
For this, consider the documentation of the correspondent dependency [PostgreSQL](https://artifacthub.io/packages/helm/bitnami/postgresql/11.9.13)
or [PGAdmin](https://www.pgadmin.org/docs/pgadmin4/latest/container_deployment.html).
To use the ones that are on the charts file you can:


```bash
helm dependency build path-to-chart
```


In case you want to use an already deployed database or PGAdmin instance you can also disable the respective dependency:

```yaml

postgres:
  enabled: false

pgadmin4:
  enabled: false
```

And you can also change the link where you connect by the configMap, you just need to change the property on the configMap according to the respective value on the application.yaml

```yaml
vas:
  datasource:
    host: localhost
    user: default
    pass: defaultpassword
```


