## What this is all about
This code creates **two Kubernetes-ready microservices** written in [Spring Boot](https://spring.io/projects/spring-boot/) `3.2.1`:
* The `transaction-ingestion-service`: using the RabbitMQ to publish transactions
* The  `notification-service`: scalable Kubernetes stateful set of multiple replicas consuming the transactions and matching its data with the customer data stored in VMware GemFire. The idea is to spin a replica which is consuming from a dedicated [RabbitMQ queue](https://www.rabbitmq.com/queues.html).

## Architecture
![Architecture](Docs/architecture.png)

## Prerequisites

### Create a Kubernetes cluster
Use a distribution of your choice. e.g. [kind](https://kind.sigs.k8s.io/) if for development purposes.
On the cluster, create and configure:
* [Local registry](https://kind.sigs.k8s.io/docs/user/local-registry/), or use an existing private registry.
* [Load balancer](https://kind.sigs.k8s.io/docs/user/loadbalancer/)

### Install Helm
Helm is a package manager for Kuberntes used to package our microservices.
Follow the [guide](https://helm.sh/docs/intro/install/)

### Install RabbitMQ
Follow the [guide](https://docs.vmware.com/en/VMware-RabbitMQ-for-Kubernetes/1/rmq/installation.html)

### Install GemFire
* Install the [operator](https://docs.vmware.com/en/VMware-GemFire-for-Kubernetes/2.3/gf-k8s/install.html)
* Create a [GemFire cluster](https://docs.vmware.com/en/VMware-GemFire-for-Kubernetes/2.3/gf-k8s/create-and-delete.html)

### Install JDK
Used to build the artefacts from the source code.

### Enable the RabbitMQ Consistent Hash Plugin
The [Consistent Hash Exchange](https://github.com/rabbitmq/rabbitmq-consistent-hash-exchange) is used to distribute workload to multiple queues backed by mutiple CPU cores.

To enable the plugin, assuming the k8s rabbitmq namespace is `rabbitmq-system`, and the number of RabbitMQ pods is `3`:
```
kubectl -n rabbitmq-system exec -it tanzu-rabbitmq-server-0 -- rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
kubectl -n rabbitmq-system exec -it tanzu-rabbitmq-server-1 -- rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
kubectl -n rabbitmq-system exec -it tanzu-rabbitmq-server-2 -- rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
```

## Running Services locally
From the CLI or the IDE, do:
#### Transaction Ingestion Service
```
./gradlew :transaction-ingestion-service:bootRun
```
#### Notification Service
```
./gradlew :notification-service:bootRun
```

## Building the Helm charts
Each microservice has a corresponding `build-and-deploy-k8s.sh` script file.
You may need to adjust the following variables:
* `namespace`: the k8s namespace used to deploy the artefacts
* `registry`: your local or private [Docker registry](https://docs.docker.com/registry/) URL.

## Appendix
### Connecting to GemFire on K8s
```
# connect to a locator pod:
kubectl -n YOUR-GEMFIRE-NAMESPACE exec -it YOUR-GEMFIRE-CLUSTER-locator-0 -- gfsh

# type in gfsh:
connect --locator=YOUR-GEMFIRE-CLUSTER-locator-0.YOUR-GEMFIRE-CLUSTER-locator.YOUR-GEMFIRE-NAMESPACE.svc.cluster.local[10334] --security-properties-file=/security/gfsecurity.properties

```
For example, if the k8s namespace is `tanzu-gemfire`, and the gemfire cluster is named `gemfire-cluster`, it is:
```
# connect to a locator pod:
kubectl -n tanzu-gemfire exec -it gemfire-cluster-locator-0 -- gfsh

# type in gfsh:
connect --locator=gemfire-cluster-locator-0.gemfire-cluster-locator.tanzu-gemfire.svc.cluster.local[10334] --security-properties-file=/security/gfsecurity.properties
```

### Running microservices in Docker
Example:
```
# build
docker build -t transaction-ingestion-service .

# run
docker run --rm -p8081:8080 transaction-ingestion-service
```
