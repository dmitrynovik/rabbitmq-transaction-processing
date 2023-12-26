- move configmaps and secrets to the Helm charts
- change the `notification-service` to be a Stateful set
- create a queue per `notification-service` replica (e.g., retrieve its pod name)[https://stackoverflow.com/questions/51593129/how-to-get-current-pod-in-kubernetes-java-application]
- fix GemFire caching
- fix Cloud Cache config to be from K8s


