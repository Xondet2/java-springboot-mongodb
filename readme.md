# API Vehiculo (Spring Boot + MongoDB)

Pequeña API CRUD para gestionar vehículos, construida con Spring Boot y MongoDB Atlas. Incluye endpoints para crear, consultar por placa y buscar por texto.

## Requisitos
- Java 17 (JDK)
- Maven (opcional; el proyecto trae `mvnw`/`mvnw.cmd`)
- Cuenta y cluster en MongoDB Atlas
- Variable de entorno `MONGODB_URI` apuntando a tu cluster

## Dependencias principales
- spring-boot-starter-web
- spring-boot-starter-validation
- spring-boot-starter-data-mongodb
- Spring Boot 3.x (probado con 3.5.7)

## Variables de entorno
- `MONGODB_URI`: cadena de conexión de Atlas (incluye usuario, contraseña y parámetros de conexión).
- `PORT`: usado por plataformas como Render. En local no es necesario, por defecto `8080`.

## Ejecución en local
1. Configura `MONGODB_URI`:
   - Windows (PowerShell, sesión actual):
     ```powershell
     $env:MONGODB_URI = "mongodb+srv://<user>:<pass>@<cluster>/?retryWrites=true&w=majority"
     ```
   - Linux/macOS (bash):
     ```bash
     export MONGODB_URI="mongodb+srv://<user>:<pass>@<cluster>/?retryWrites=true&w=majority"
     ```
2. Construye o ejecuta:
   - Windows:
     ```powershell
     .\mvnw.cmd -DskipTests package
     java -jar target/Vehiculo-0.0.1-SNAPSHOT.jar
     ```
   - Linux/macOS:
     ```bash
     ./mvnw -DskipTests package
     java -jar target/Vehiculo-0.0.1-SNAPSHOT.jar
     ```
3. La API queda en `http://localhost:8080`.

## Endpoints
- `POST /api/vehiculos`
  - Crea un vehículo. Responde `201 Created` y el recurso.
  - Responde `409 Conflict` si la placa ya existe.
- `GET /api/vehiculos/{placa}`
  - Obtiene un vehículo por su placa. `404` si no existe.
- `GET /api/vehiculos/buscar?q=<texto>`
  - Búsqueda por expresión regular en campos relevantes.

### Ejemplos (curl)
```bash
# Crear
curl -X POST http://localhost:8080/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{
    "placa": "ABC123",
    "marca": "Toyota",
    "modelo": "Corolla",
    "color": "Rojo"
  }'

# Obtener por placa
curl http://localhost:8080/api/vehiculos/ABC123

# Buscar
curl "http://localhost:8080/api/vehiculos/buscar?q=Toy"
```

## Nota importante sobre entorno local, App ID y API Key
- Esta API no expone credenciales públicas. Para operar de forma segura se espera un backend ejecutándose en un entorno local/controlado.
- Si deseas integrar servicios que requieren **App ID** y **API Key** (p. ej., App Services/Data API en Atlas), la creación y uso de esas credenciales puede estar **limitada por políticas de organización** o por el **plan free**.
- En el flujo actual, los datos se originan desde tu entorno local y se **persisten en la base de datos de Atlas**. Pueden ser consultados desde la misma API, pero el uso sigue **requiriendo el entorno local** para manejar credenciales y configuración de manera segura.

## Despliegue (Docker/Render)
- Este repositorio incluye un `Dockerfile` multi-stage:
  - Construye el jar con Maven y JDK 17.
  - Ejecuta con JRE 17.
- En Render:
  - Crea un Web Service con **runtime Docker** apuntando a este repo.
  - Define la variable `MONGODB_URI` en el Dashboard.
  - Render gestiona `PORT`; la app usa `server.port: ${PORT:8080}`.

## Desarrollo
- Ejecuta `spring-boot:run` si prefieres hot reload:
  - Windows: `./mvnw.cmd spring-boot:run`
  - Linux/macOS: `./mvnw spring-boot:run`

## Problemas comunes
- `JAVA_HOME` no definido: usa runtime Java o Docker en Render.
- `./mvnw: Permission denied` en Linux: marca ejecutable (`chmod +x mvnw`) o ejecuta con `bash ./mvnw`.
- Conexión Atlas rechazada: revisa IP allowlist y credenciales de usuario.
- Alaracion: en render un error comun es que no se referencia bien la base de datos o tiene una respuesta incorrecta, se llama a la base de datos y se actualiza desde consola a mongodb atlas y se puede llamar informacion desde la base a local, temas de configuracion api key y app id especificadas mas arriba, url de despliegue en render https://java-springboot-mongodb.onrender.com