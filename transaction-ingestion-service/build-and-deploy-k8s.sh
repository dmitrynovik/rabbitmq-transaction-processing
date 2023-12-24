#set -eo pipefail

project="transaction-ingestion-service"

registry="localhost:5001"
image=$project
chart_name=$project
chart_version="0.1.0" # Increment each time!
namespace="transaction-processing"

cd ../
./gradlew -Dskip.tests build

kubectl create namespace $namespace --dry-run=client -o yaml | kubectl apply -f-

cd k8s/
kubectl -n $namespace apply -f configmap.yaml
kubectl -n $namespace apply -f secret.yaml
cd ../$project

docker build -t $image .
docker tag $image "$registry/$image"
docker image push "$registry/$image"

cd k8s/helm/chart/$chart_name

helm package .
kubectl -n $namespace delete deployment $image
kubectl -n $namespace delete svc $image
set +e
kubectl apply -f ../../../../k8s/permissions.yaml
helm -n $namespace delete $chart_name
helm -n $namespace install $chart_name "./$chart_name-$chart_version.tgz"

kubectl wait --namespace $namespace --for=condition=ready pod --selector=app.kubernetes.io/name=$image --timeout=40s
kubectl -n $namespace get pods
