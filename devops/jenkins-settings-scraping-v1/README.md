# 📌 Instrucciones

[← Regresar](./../../README.md)

---

## 📄 Prerrequisitos

- Construir imágenes en Minikube
- [Instalar Ngrok](https://github.com/miguel-armas-abt/roadmap-ngrok/blob/main/path/00-setup/README.md)

---

## 📎 Variables
- `USER_NAME`: poc-user
- `USER_PASSWORD`: qwerty
- `K8S_CLUSTER_TOKEN`: k8s-cluster-token
- `GITHUB_PROJECT`: `https://github.com/miguel-armas-abt/poc-jenkins-k8s`
- `REPOSITORY_URL`: `https://miguel-armas-abt:<github-access-token>@github.com/miguel-armas-abt/poc-jenkins-k8s`
- `JENKINSFILE_PATH`: backend/mock-service-v1/Jenkinsfile

---

```shell
cd ./../scripts/resources
```

# 1. Iniciar Jenkins

> 🔨 **Construir imagen e iniciar docker-compose**
> 
> ```shell
> docker build -t miguelarmasabt/devops-jenkins:v1 . --no-cache
> docker-compose -f docker-compose.yml up -d
> ```

> ⏸️️ **Detener orquestación**
> 
> Para eliminar la orquestación utilice `down -v` en lugar de `stop`.
> ```shell
> docker-compose -f docker-compose.yml stop
> ```

- Abra el navegador en `http://localhost:8181`

> ✅ **Login**
> 
> Autenticarse en Jenkins con el token ubicado en los logs del contenedor.
> ```shell script 
> docker logs devops-jenkins
> ```

- Instale los plugins sugeridos: `Install suggested plugins`
- Cree una cuenta de administrador: (username=`$USER_NAME`, password=`$USER_PASSWORD`)
- Mantenga la URL por defecto: `http://localhost:8181/`

---

# 2. Integrar con Kubernetes
- Seleccione `Panel de control > Administrar Jenkins > Plugins > Available plugins` e instale `Kubernetes`.

> ⚠️ **Conectar Jenkins a la red de Minikube**
> 
> <u>Desconéctelo antes de apagar Minikube</u>, sino generará conflictos al encender el clúster la siguiente vez. Para tal propósito utilice `disconnect`.
> ```shell script 
> docker network connect minikube devops-jenkins
> ```

> 🔑 **Recuperar token de autenticación k8s**
> 
> Conceda privilegios a Jenkins sobre Kubernetes y reserve el token de autenticación k8s 🟢.
> ```shell script 
> kubectl apply -f ./jenkins-auth.yml
> kubectl describe secret/jenkins-token-rk2mg
> ```

- **🔓 Creación de secreto - Token de autenticación k8s**
  - Seleccione la opción `Panel de control > Administrar Jenkins > Credentials` y presione `(global)`
  - Presione el botón `+ Add Credentials`, configure los siguientes campos y acepte:

> - **Kind**: `Secret Text`
> - **Secret**: `<Token de autenticación k8s 🟢>`
> - **ID**: `$K8S_CLUSTER_TOKEN`
> 
> ⚠️ Si durante la ejecución del pipeline obtiene un error de autenticación, elimine y cree nuevamente la credencial.

> ⚙️ **Recuperar configuración del clúster**
> ```shell script 
> kubectl config view
> ```
> - **Certificado k8s**: Reserve el valor de la propiedad `clusters.cluster.certificate-authority`. Por ejemplo, `C:\Users\User\.minikube\ca.crt` 🟣. 
> - **URL pública k8s**: Ubique el valor de la propiedad `clusters.cluster.server`, por ejemplo, `https://127.0.0.1:52619` y expóngalo hacia internet con ayuda de ngrok. Reserve la URL pública, por ejemplo, `https://f247-179-6-212-27.ngrok-free.app` 🔵.
> 
> ```shell script 
> ngrok http https://127.0.0.1:52619
> ```

- **🔧 Configurar conexión a Kubernetes**
  - Seleccione la opción `Panel de control > Administrar Jenkins > Clouds > New cloud`
  - Digite `poc-kubernetes` en el campo `Cloud name`, seleccione la opción `Kubernetes` y de clic en el botón `Create`
  - Presione el botón `Kubernetes Cloud details`, configure los siguientes campos y guarde.

> - **Kubernetes URL**: `<URL pública k8s 🔵>`
> - **Kubernetes server certificate key**: `<Certificado k8s 🟣>`
> - **Disable https certificate check**: Habilitado
> - **Credentials**: `$K8S_CLUSTER_TOKEN`

# 3. Crear Jenkinsfile
> - Crear Jenkinsfile y subir el commit al repositorio remoto.

# 4. Crear pipeline
- 📂 Cree una estructura de carpetas conveniente para la organización de sus pipelines.
- Ingrese a su carpeta, seleccione `+ Nueva Tarea > Pipeline` y configure los siguientes campos:
> - **General > GitHub project**: `$GITHUB_PROJECT`
> - **Pipeline > Definition**: `Pipeline script from SCM`
> - **SCM**: `Git`
> - **Repository URL**: `$REPOSITORY_URL`
> - **Branch Specifier**: `*/main`
> - **Script Path**: `$JENKINSFILE_PATH`
- ▶️ Ejecute el pipeline.

# 5. Copiar pipelines
- Ubíquese en el folder en el que desea copiar su pipeline y seleccione `+ New Item`.
- Digite el nombre de su nuevo pipeline en el campo `Enter an item name`.
- Ubique la sección `Copy from`, digite el nombre del pipeline que copiará, selecciónelo y presiones `OK`.
- Ajuste las configuraciones revisadas durante la creación de un pipeline.