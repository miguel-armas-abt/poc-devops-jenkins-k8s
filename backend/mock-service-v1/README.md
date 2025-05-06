
[← Regresar](../README.md) <br>

---

## ▶️ Despliegue local

1. Generar el compilado
```sh
mvn clean install
```

2. Configurar las [variables de entorno](./variables.env) en el IDE.

2. Ejecutar aplicación


---

## ▶️ Despliegue con Docker

⚙️ Crear imagen
```shell
docker build -t miguelarmasabt/mock-service:v1.0.1 -f ./Dockerfile .
```

⚙️ Crear red
```shell
docker network create --driver bridge common-network
```

⚙️ Ejecutar contenedor
```shell
docker run --rm -p 8082:8082 --env-file ./variables.env --name mock-service-v1 --network common-network miguelarmasabt/mock-service:v1.0.1
```

---

## ▶️ Despliegue con Kubernetes

⚙️ Encender Minikube
```shell
docker context use default
minikube start
```

⚙️ Crear imagen
```shell
eval $(minikube docker-env --shell bash)
docker build -t miguelarmasabt/mock-service:v1.0.1 -f ./Dockerfile .
```

⚙️ Crear namespace y aplicar manifiestos
```shell
kubectl create namespace oauth
kubectl apply -f ./k8s.yaml -n oauth
```

⚙️ Eliminar orquestación
```shell
kubectl delete -f ./k8s.yaml -n oauth
```

⚙️ Port-forward
```shell
kubectl port-forward <pod-id> 8082:8082 -n oauth
```