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
On default values this release deploys the latest image tagged as `v1.0.0` from the repository's GitHub Container Registry.
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


## Keycloak

To use auth, you need to have a Keycloak server ready to generate tokens and users, after which it can be used through the property.


```yaml
# -- Defines the client secret and client ID
applicationSecret:
  # -- Value that specifies whether the application secret should be used
  enabled: true
  # -- String value that represents the client secret
  clientSecret: "" #
  # -- String value that represents the client ID
  clientId: ""  #
```

As well as activating through the application.yaml or configmap the security-enabled variable, which by default is active except for local tests.

```yaml
configmap:
  # Specifies whether a configmap should be created
  create: true
  data:
    # -- Security configurations for the application
    security_enabled: 'true'
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

On default, the Helm deployment also contains a PostgreSQL and PGAdmin deployment.
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

## Values.Yaml Chart explanation 

### country-risk-backend

![Version: 2.0.6](https://img.shields.io/badge/Version-2.0.6-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 1.0.0](https://img.shields.io/badge/AppVersion-1.0.0-informational?style=flat-square)

A Helm chart for deploying the Country Risk service

## Requirements

| Repository | Name | Version |
|------------|------|---------|
| https://charts.bitnami.com/bitnami | postgres(postgresql) | 11.*.* |
| https://helm.runix.net | pgadmin4 | 1.x.x |

## Values
<div style="font-size: 70%">

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity.podAntiAffinity.preferredDuringSchedulingIgnoredDuringExecution[0].podAffinityTerm.labelSelector.matchExpressions[0] | object | `{"key":"app.kubernetes.io/name","operator":"DoesNotExist"}` | Match Pod rules |
| affinity.podAntiAffinity.preferredDuringSchedulingIgnoredDuringExecution[0].podAffinityTerm.topologyKey | string | `"kubernetes.io/hostname"` | Key that is used to determine the topology of the cluster |
| affinity.podAntiAffinity.preferredDuringSchedulingIgnoredDuringExecution[0].weight | int | `100` |  |
| appName | string | `"vas-country-risk-backend"` | Name of the backend service |
| applicationSecret | object | `{"clientId":"","clientSecret":"","enabled":true}` | Defines the client secret and client ID |
| applicationSecret.clientId | string | `""` | String value that represents the client ID |
| applicationSecret.clientSecret | string | `""` | String value that represents the client secret |
| applicationSecret.enabled | bool | `true` | Value that specifies whether the application secret should be used |
| autoscaling | object | `{"enabled":false}` | Specifies whether autoscaling should be enabled for the pod |
| certificate.host | string | `"localhost"` | Hostname for the certificate |
| configmap.create | bool | `true` |  |
| configmap.data.security_enabled | string | `"false"` | Security configurations for the application |
| configmap.data.spring_profiles_active | string | `"dev"` | Which profile should be activated for the application |
| elastic.enabled | bool | `false` | Should elastic be enabled or not |
| elastic.security.tls | object | `{"restEncryption":false}` | Information about the transport layer security (TLS) |
| elastic.security.tls.restEncryption | bool | `false` | Encryption for the REST requests made to the Elastic cluster |
| image.name | string | `"catenax-ng/tx-vas-country-risk-backend"` | Name of the docker image |
| image.pullPolicy | string | `"Always"` |  |
| image.registry | string | `"ghcr.io"` |  |
| image.tag | string | `""` | Overrides the image tag whose default is the chart appVersion. |
| imagePullSecrets | list | `[]` | List of secrets to be used |
| ingress.className | string | `"nginx"` | Class name |
| ingress.enabled | bool | `false` | Ingress enabled or not |
| livenessProbe | object | `{"failureThreshold":3,"initialDelaySeconds":60,"path":"/management/health/liveness","periodSeconds":10,"port":8080,"successThreshold":1,"timeoutSeconds":1}` | Determines if a pod is still alive or not |
| livenessProbe.initialDelaySeconds | int | `60` | Number of seconds to wait before performing the first liveness probe |
| livenessProbe.path | string | `"/management/health/liveness"` | HTTP endpoint |
| livenessProbe.periodSeconds | int | `10` | Number of seconds to wait between consecutive probes |
| livenessProbe.port | int | `8080` | Port used |
| livenessProbe.successThreshold | int | `1` | Number of consecutive successful probes before a pod is considered healthy |
| livenessProbe.timeoutSeconds | int | `1` | Number of seconds after which a liveness probe times out |
| nodeSelector | object | `{}` | Node placement constraints |
| pgadmin4.enabled | bool | `false` | Should pgadmin4 be enabled or not |
| pgadmin4.env.email | string | `"vas@catena-x.net"` | Email used on the Env environment |
| pgadmin4.ingress.annotations."cert-manager.io/cluster-issuer" | string | `"letsencrypt-prod"` | Cluster issuer used for the ingress |
| pgadmin4.ingress.annotations."kubernetes.io/ingress.class" | string | `"nginx"` | Class for the pgadmin4 deployment |
| pgadmin4.ingress.enabled | bool | `true` | Ingress enabled or not |
| pgadmin4.secret.path | string | `"value-added-service/data/country-risk/dev/pgadmin4"` | Path where the information related to the secret |
| podAnnotations | object | `{}` | Annotations to be added to the running pod |
| podSecurityContext | object | `{"fsGroup":2000}` | Configuration for security-related options of the running pod |
| podSecurityContext.fsGroup | int | `2000` | Set the file system group ID for all containers in the pod |
| postgres.appName | string | `"vas-country-risk-postgres"` | Database application name |
| postgres.auth | object | `{"database":"vas","password":"vas","postgresPassword":"vas","username":"vas"}` | Configuration values for the Database |
| postgres.enabled | bool | `true` | Should postgres DB be enabled or not |
| postgres.environment | string | `"dev"` | Type of environment the database is running |
| postgres.ingress.className | string | `"nginx"` |  |
| postgres.ingress.enabled | bool | `true` | Ingress enabled or not |
| postgres.service.port | int | `5432` | Port to be used on this service |
| postgres.service.type | string | `"ClusterIP"` | Type of service to be used |
| readinessProbe | object | `{"failureThreshold":3,"initialDelaySeconds":60,"path":"/management/health/readiness","periodSeconds":10,"port":8080,"successThreshold":1,"timeoutSeconds":1}` | Determine when a pod is ready to start accepting requests |
| replicaCount | int | `1` |  |
| resources.limits | object | `{"cpu":"800m","memory":"2Gi"}` | Maximum amount of resources that the deployment should be able to consume |
| resources.requests | object | `{"cpu":"300m","memory":"1Gi"}` | Minimum amount of resources that the deployment should be guaranteed to receive |
| securityContext.allowPrivilegeEscalation | bool | `false` | Specifies if processes running inside the container can gain more privileges than its initial user |
| securityContext.capabilities | object | `{"drop":["ALL"]}` | Capabilities that the process inside the container should have |
| securityContext.runAsGroup | int | `3000` | Specifies the group ID that the process inside the container should run |
| securityContext.runAsNonRoot | bool | `true` | Specifies whether the process inside the container should run as a non-root user |
| securityContext.runAsUser | int | `10001` | Specifies the user ID that the process inside the container should run |
| service | object | `{"port":8080,"type":"ClusterIP"}` | Service that should be created for the pod |
| service.port | int | `8080` | Service port |
| service.type | string | `"ClusterIP"` | Type of service to be used |
| springProfiles[0] | string | `"dev"` |  |
| tolerations | list | `[]` | Pod toleration constraints |

</div>
----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.11.0](https://github.com/norwoodj/helm-docs/releases/v1.11.0)



