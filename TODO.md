- move configmaps and secrets to the Helm charts
- change the `notification-service` to be a Stateful set
- create a queue per `notification-service` replica (e.g., retrieve its pod name)[https://stackoverflow.com/questions/51593129/how-to-get-current-pod-in-kubernetes-java-application]
- fix GemFire connection: 
```
Pool unexpected closed socket on server connection=Pooled Connection to gemfire-cluster-server-0.gemfire-cluster-server.tanzu-gemfire.svc.cluster.local:40404,gemfire-cluster-server-0(gemfire-cluster-server-0:1)<v2>:51692: Connection[DESTROYED]). Server unreachable: could not connect after 1 attempts
Pool unexpected closed socket on server connection=Pooled Connection to gemfire-cluster-server-1.gemfire-cluster-server.tanzu-gemfire.svc.cluster.local:40404,gemfire-cluster-server-1(gemfire-cluster-server-1:1)<v1>:42149: Connection[DESTROYED]). Server unreachable: could not connect after 1 attempts
Could not find any server to host primary client queue. Number of excluded servers is 0 and exception is Unable to connect to any locators in the list [gemfire-cluster-locator-0.gemfire-cluster-locator.tanzu-gemfire.svc.cluster.local/<unresolved>:10334]
```
- fix Logs not appearing