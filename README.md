![Architecture](Docs/architecture.png)

## Docker
```
# build
docker build -t transaction-ingestion-service .

# run (in Docker)
docker run --rm -p8080:8080 transaction-ingestion-service
```
## Prerequisites

### Install RabbitMQ

### Install GemFire

### Enable RabbitMQ Consistent Hash Plugin
```
kubectl -n rabbitmq-system exec -it tanzu-rabbitmq-server-0 -- rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
kubectl -n rabbitmq-system exec -it tanzu-rabbitmq-server-1 -- rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
kubectl -n rabbitmq-system exec -it tanzu-rabbitmq-server-2 -- rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
```

### Running locally
From the CLI or the IDE, do:
#### Transaction Ingestion Service
```
./gradlew :transaction-ingestion-service:bootRun
```
#### Notification Service
```
./gradlew :notification-service:bootRun
```

## Connecting to GemFire on K8s
```
kubectl -n tanzu-gemfire exec -it gemfire-cluster-locator-0 -- gfsh

# type in gfsh:
connect --locator=gemfire-cluster-locator-0.gemfire-cluster-locator.tanzu-gemfire.svc.cluster.local[10334] --security-properties-file=/security/gfsecurity.properties
```
