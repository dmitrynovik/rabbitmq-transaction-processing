project="transaction-ingestion-service"

registry="localhost:5001"
image=$project
chart_name=$project
chart_version="0.1.0" # Increment each time!
namespace="transaction-processing"

#../gradlew -Dskip.tests build 

docker build -t $image .
docker tag $image "$registry/$image"
docker image push "$registry/$image"

cd k8s/helm/chart/$chart_name

helm package .
helm -n $namespace delete $chart_name
helm -n $namespace install $chart_name "./$chart_name-$chart_version.tgz"

kubectl create namespace $namespace --dry-run=client -o yaml | kubectl apply -f-

kubectl wait --namespace $namespace --for=condition=ready pod --selector=app.kubernetes.io/name=$image --timeout=15s
kubectl -n $namespace get pods


