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
