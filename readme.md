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

## Probar en navegador
- Base URL (Render): `https://java-springboot-mongodb.onrender.com`
- Endpoints directos:
  - Listar todo: `https://java-springboot-mongodb.onrender.com/api/vehiculos/buscar`
  - Búsqueda por texto: `https://java-springboot-mongodb.onrender.com/api/vehiculos/buscar?q=Mazda`
  - Detalle por placa: `https://java-springboot-mongodb.onrender.com/api/vehiculos/TEST-A001`

### Crear desde consola del navegador
1. Abre la base URL y presiona `F12` → `Console`.
2. Pega y ejecuta el siguiente script para insertar un conjunto de vehículos de prueba:

Este script ya se ejecuto desde el link de manera que los datos se guardan en la base de datos MondoDb atlas

```js
const BASE_URL = 'https://java-springboot-mongodb.onrender.com';

const dataset = [
  { placa: 'TEST-A001', marca: 'Mazda', modelo: '3', color: 'Azul', anio: 2019 },
  { placa: 'TEST-A002', marca: 'Mazda', modelo: 'CX-5', color: 'Rojo', anio: 2021 },
  { placa: 'TEST-A003', marca: 'Mazda', modelo: '2', color: 'Gris', anio: 2018 },
  { placa: 'TEST-B001', marca: 'Toyota', modelo: 'Corolla', color: 'Rojo', anio: 2020 },
  { placa: 'TEST-B002', marca: 'Toyota', modelo: 'Yaris', color: 'Negro', anio: 2017 },
  { placa: 'TEST-B003', marca: 'Toyota', modelo: 'RAV4', color: 'Blanco', anio: 2022 },
  { placa: 'TEST-C001', marca: 'Hyundai', modelo: 'Accent', color: 'Blanco', anio: 2016 },
  { placa: 'TEST-C002', marca: 'Hyundai', modelo: 'Tucson', color: 'Azul', anio: 2023 },
  { placa: 'TEST-C003', marca: 'Hyundai', modelo: 'Elantra', color: 'Gris', anio: 2019 }
];

const postVehiculo = (v) =>
  fetch(`${BASE_URL}/api/vehiculos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(v)
  }).then(async r => ({ status: r.status, body: await r.json().catch(() => null), input: v }))
    .catch(e => ({ status: 'error', error: String(e), input: v }));

const wait = (ms) => new Promise(res => setTimeout(res, ms));

async function batchInsert(items, concurrency = 5, delayMs = 150) {
  const results = [];
  for (let i = 0; i < items.length; i += concurrency) {
    const chunk = items.slice(i, i + concurrency);
    const res = await Promise.all(chunk.map(postVehiculo));
    results.push(...res);
    await wait(delayMs);
  }
  const ok = results.filter(r => r.status === 201).length;
  const dup = results.filter(r => r.status === 409).length;
  const err = results.filter(r => r.status !== 201 && r.status !== 409).length;
  console.table(results.map(r => ({ status: r.status, placa: r.input.placa, marca: r.input.marca, modelo: r.input.modelo })));
  console.log(`Insertados: ${ok} | Duplicados: ${dup} | Otros: ${err}`);
  return results;
}

batchInsert(dataset, 6, 200);
```

### Validaciones rápidas
- Búsqueda: `https://java-springboot-mongodb.onrender.com/api/vehiculos/buscar?q=Mazda`
- Detalle por placa: `https://java-springboot-mongodb.onrender.com/api/vehiculos/TEST-A001`