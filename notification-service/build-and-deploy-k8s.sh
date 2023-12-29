project="notification-service"

registry="localhost:5001"
image=$project
chart_name=$project
chart_version="0.1.0" # Increment each time!
namespace="transaction-processing"

set -eo pipefail

cd ../
./gradlew -Dskip.tests build

cd "$project"

docker build -t $image .
docker tag $image "$registry/$image"
docker image push "$registry/$image"

cd k8s/helm/chart/$chart_name

helm package .

set +e

kubectl create namespace $namespace --dry-run=client -o yaml | kubectl apply -f-
kubectl -n $namespace delete deployment $image
kubectl -n $namespace delete statefulset $image
kubectl -n $namespace delete svc $image

helm -n $namespace delete $chart_name
set +e
helm -n $namespace install $chart_name "./$chart_name-$chart_version.tgz"

kubectl wait --namespace $namespace --for=condition=ready pod --selector=app.kubernetes.io/name=$image --timeout=40s
kubectl -n $namespace get pods
