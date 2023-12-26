
## Kubernetes ready

### Expose the management endpoints

#management.endpoints.web.exposure.include=*
management.endpoint.health.probes.enabled=true
management.endpoint.health.show-details=always
management.health.livenessState.enabled=true
management.endpoint.health.group.liveness.include=readinessState
management.health.readinessState.enabled=true
management.endpoint.health.group.readiness.include=readinessState

### Change the Spring Boot application type
```
new SpringApplicationBuilder(NotificationServiceApplication.class)
      .web(WebApplicationType.SERVLET)
      .run(args);
```

## Creating a Helm Chart
### Health Endpoints
```
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: http
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: http
```
### .helmignore
```
deploy.sh
*.tgz
```
### Chart.yaml
```
appVersion: "latest"
```
### Values.yaml
```
image:
  repository: localhost:5001/weather-web-server
  name: weather-web-server
  pullPolicy: Always
  # Overrides the image tag whose default is the chart appVersion.
  tag: "latest"
```

```
service:
  enabled: true
  type: LoadBalancer
  port: 80
  targetPort: 8080
  protocol: TCP
```

```
ingress:
  enabled: false
  annotations:
      kubernetes.io/ingress.class: nginx
      kubernetes.io/tls-acme: "true"
  hosts:
    - host: weather-web-server.local
      paths:
        - path: /
          pathType: Prefix
          backend:
            serviceName: weather-web-server-svc
            servicePort: 80
```
