# eBlumbit — Backend

API REST para el proyecto de eBlumbit (productos, inventario, clientes, proveedores, compras, ventas, sucursales, almacenes, usuarios y permisos), construida con **Spring Boot 4**, **Java 25** y **PostgreSQL**.

Incluye autenticación con **JWT** (access token + refresh token), RBAC, Paginación, documentación OpenAPI, generación de reportes PDF y datos de prueba que se cargan automáticamente al iniciar.

---

## Tabla de contenidos

1. [Stack tecnológico](#stack-tecnológico)
2. [Requisitos](#requisitos)
3. [Instalación de Java 25](#instalación-de-java-25)
4. [Instalación de PostgreSQL](#instalación-de-postgresql)
5. [Configuración de la base de datos](#configuración-de-la-base-de-datos)
6. [Configuración de la aplicación](#configuración-de-la-aplicación)
7. [Ejecutar en desarrollo (sin Docker)](#ejecutar-en-desarrollo-sin-docker)
8. [Ejecutar en producción (sin Docker)](#ejecutar-en-producción-sin-docker)
9. [Ejecutar con Docker](#ejecutar-con-docker)
10. [Ejecutar con Docker Compose](#ejecutar-con-docker-compose)
11. [Verificar que la aplicación funciona](#verificar-que-la-aplicación-funciona)
12. [Usuarios de prueba](#usuarios-de-prueba)
13. [Endpoints principales](#endpoints-principales)
14. [Variables de entorno](#variables-de-entorno)
15. [Solución de problemas](#solución-de-problemas)
16. [Estructura del proyecto](#estructura-del-proyecto)

---

## Stack tecnológico

| Componente | Versión |
|---|---|
| Java | 25 |
| Spring Boot | 4.1.1 |
| Maven | Wrapper incluido (`mvnw`) |
| PostgreSQL | 16 o superior (recomendado) |
| Spring Security + JWT | Incluido |
| springdoc OpenAPI (Swagger UI) | 3.0.3 |
| Docker | Opcional (imagen basada en Amazon Corretto 25) |

---

## Requisitos

Antes de ejecutar el proyecto necesitas:

- **Java 25 (JDK)** instalado y configurado.
- **PostgreSQL** en ejecución (local o remoto).
- **Maven no es obligatorio**: el proyecto incluye el Maven Wrapper (`mvnw` / `mvnw.cmd`), que descarga Maven automáticamente.
- **Docker y Docker Compose** solo si vas a usar los apartados de Docker.

Verifica tu instalación:

```bash
java -version
# Debe mostrar: openjdk version "25..." o java version "25..."
```

> El proyecto está compilado para **Java 25** (`<java.version>25</java.version>` en `pom.xml`). Con una versión anterior la compilación falla.

---

## Instalación de Java 25

Elige una de las opciones según tu sistema operativo.

### Opción A — Eclipse Temurin (Adoptium) — recomendada

Descarga gratuita y multiplataforma:

- Descargas: <https://adoptium.net/temurin/releases/?version=25>
- Guía de instalación: <https://adoptium.net/installation/>

### Opción B — Oracle JDK 25

- Descargas oficiales: <https://www.oracle.com/java/technologies/downloads/#java25>

### Opción C — SDKMAN (Linux / macOS)

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 25-tem
sdk use java 25-tem
```

### Opción D — Gestores de paquetes

- **Ubuntu / Debian**: usa el paquete de Adoptium indicado en <https://adoptium.net/installation/linux/>
- **macOS (Homebrew)**: `brew install --cask temurin@25`
- **Windows**: descarga el instalador `.msi` desde Adoptium u Oracle y verifica con `java -version`.

### Configurar `JAVA_HOME`

**Linux / macOS**

```bash
export JAVA_HOME=/ruta/a/jdk-25
export PATH="$JAVA_HOME/bin:$PATH"
```

Añade esas líneas a `~/.bashrc` o `~/.zshrc` para que sean permanentes.

**Windows (PowerShell)**

```powershell
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-25..."
```

Cierra y vuelve a abrir la terminal, luego comprueba:

```bash
echo $JAVA_HOME
java -version
```

---

## Instalación de PostgreSQL

### Opción A — Instalación local (recomendada para desarrollo)

Descargas oficiales por sistema operativo: <https://www.postgresql.org/download/>

- **Windows**: <https://www.postgresql.org/download/windows/> (instalador de EnterpriseDB)
- **macOS**: <https://www.postgresql.org/download/macosx/> o `brew install postgresql@16`
- **Ubuntu / Debian**: <https://www.postgresql.org/download/linux/ubuntu/>
- **Otras distribuciones Linux**: <https://www.postgresql.org/download/linux/>

Durante la instalación se solicita una contraseña para el usuario `postgres`. Anótala, porque la aplicación la necesita.

### Opción B — PostgreSQL con Docker (sin instalarlo)

```bash
docker run -d \
  --name postgres-eblumbit \
  -e POSTGRES_DB=eblumbit_spring \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=123456 \
  -p 5432:5432 \
  -v postgres-eblumbit-data:/var/lib/postgresql/data \
  postgres:16-alpine
```

### Verificar que PostgreSQL está activo

```bash
psql --version
psql -h localhost -U postgres -c "SELECT version();"
```

Si el comando `psql` no existe en el `PATH`, usa la ruta de instalación (por ejemplo, en Windows `C:\Program Files\PostgreSQL\16\bin\psql.exe`).

---

## Configuración de la base de datos

Crea la base de datos que usa la aplicación por defecto:

```bash
# Conéctate como superusuario
psql -h localhost -U postgres

# Dentro de psql
CREATE DATABASE eblumbit_spring;
\q
```

O en un solo comando:

```bash
createdb -h localhost -U postgres eblumbit_spring
```

La configuración por defecto de la aplicación es:

| Parámetro | Valor por defecto |
|---|---|
| Host | `localhost` |
| Puerto | `5432` |
| Base de datos | `eblumbit_spring` |
| Usuario | `postgres` |
| Contraseña | `123456` |
| Driver | `org.postgresql.Driver` |

Si tu contraseña o usuario son distintos, cambia `src/main/resources/application.yaml` o usa las variables de entorno indicadas en [Variables de entorno](#variables-de-entorno).

> **Importante:** el esquema de tablas se crea automáticamente con Hibernate (`spring.jpa.hibernate.ddl-auto: create-drop`). No hay scripts de migración manuales. Al detener la aplicación en modo `create-drop`, **las tablas y los datos se eliminan** y se vuelven a crear (junto con los datos de prueba) en el siguiente arranque.

---

## Configuración de la aplicación

El archivo de configuración es:

```
src/main/resources/application.yaml
```

Contenido relevante:

```yaml
server:
  port: 8090
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/eblumbit_spring
    username: postgres
    password: 123456
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
application:
  jwt:
    secret-key: <clave-jwt>
    expiration: 900000        # 15 minutos
    refresh-expiration: 432000000  # 5 días
storage:
  location: ${STORAGE_LOCATION:./files}
```

Puntos a tener en cuenta:

- El puerto HTTP por defecto es **8090**.
- Los archivos subidos se guardan en la carpeta indicada por `storage.location` (por defecto `./files`, relativa al directorio desde donde se ejecuta la aplicación). Puedes cambiarla con `STORAGE_LOCATION`.
- La clave JWT es solo para desarrollo. **Cámbiala en producción** mediante `APPLICATION_JWT_SECRET_KEY`.

---

## Ejecutar en desarrollo (sin Docker)

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd eblumbit
```

### 2. Tener PostgreSQL iniciado y la base de datos creada

Sigue los pasos de [Configuración de la base de datos](#configuración-de-la-base-de-datos).

### 3. Ejecutar la aplicación

**Linux / macOS**

```bash
./mvnw spring-boot:run
```

**Windows (CMD / PowerShell)**

```cmd
mvnw.cmd spring-boot:run
```

La aplicación arranca en **<http://localhost:8090>**.

`spring-boot-devtools` está incluido, por lo que al recompilar los cambios (por ejemplo, `./mvnw compile`) la aplicación se reinicia automáticamente.

### 4. Comandos útiles de Maven

```bash
./mvnw clean              # limpiar artefactos
./mvnw compile            # compilar
./mvnw test               # ejecutar tests
./mvnw package            # empaquetar el JAR
./mvnw spring-boot:run    # ejecutar en desarrollo
```

> En el primer arranque, `DataSeeder` crea permisos, roles, usuarios, sucursales, almacenes, clientes, proveedores y 1000 productos de prueba. Puede tardar unos segundos.

---

## Ejecutar en producción (sin Docker)

### 1. Compilar y empaquetar

```bash
./mvnw clean package -DskipTests
```

Esto genera el archivo:

```
target/eblumbit-0.0.2-SNAPSHOT.jar
```

### 2. Configurar las variables de entorno de producción

Como mínimo, define la conexión a la base de datos, la clave JWT y la ruta de archivos:

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/eblumbit_spring"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="una-contraseña-segura"
export SPRING_JPA_HIBERNATE_DDL_AUTO="update"
export APPLICATION_JWT_SECRET_KEY="una-clave-larga-y-aleatoria-de-al-menos-32-caracteres"
export STORAGE_LOCATION="/var/lib/eblumbit/files"
export SERVER_PORT=8090
```

> **Recomendación para producción:** no uses `create-drop` (elimina los datos al apagar). Usa `update` o, si gestionas el esquema con migraciones, `validate`. Actualmente el proyecto no incluye Flyway/Liquibase, por lo que `update` es la opción práctica.
>
> Usa siempre una clave JWT propia y robusta (mínimo 32 caracteres). La clave incluida en el repositorio es solo para desarrollo.

### 3. Ejecutar el JAR

```bash
java -jar target/eblumbit-0.0.2-SNAPSHOT.jar
```

Con parámetros de Spring directamente (alternativa a las variables de entorno):

```bash
java -jar target/eblumbit-0.0.2-SNAPSHOT.jar \
  --server.port=8090 \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/eblumbit_spring \
  --spring.datasource.username=postgres \
  --spring.datasource.password=123456 \
  --spring.jpa.hibernate.ddl-auto=update
```

### 4. Ejecutarlo en segundo plano (Linux)

```bash
nohup java -jar target/eblumbit-0.0.2-SNAPSHOT.jar > app.log 2>&1 &
```

O como servicio del sistema con `systemd`, o detrás de un proxy inverso (Nginx, Caddy, Traefik) para HTTPS.

---

## Ejecutar con Docker

El proyecto incluye un `Dockerfile` multi-stage:

1. Compila con `maven:3.9.16-amazoncorretto-25-alpine`.
2. Ejecuta el JAR con `amazoncorretto:25-alpine`, exponiendo el puerto **8090**.

### 1. Construir la imagen

```bash
docker build -t eblumbit:latest .
```

### 2. Crear una red de Docker

```bash
docker network create eblumbit-net
```

### 3. Levantar PostgreSQL

```bash
docker run -d \
  --name eblumbit-db \
  --network eblumbit-net \
  -e POSTGRES_DB=eblumbit_spring \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=123456 \
  -p 5432:5432 \
  -v eblumbit-postgres-data:/var/lib/postgresql/data \
  postgres:16-alpine
```

### 4. Levantar la aplicación

```bash
docker run -d \
  --name eblumbit-app \
  --network eblumbit-net \
  -p 8090:8090 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://eblumbit-db:5432/eblumbit_spring \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
  -e APPLICATION_JWT_SECRET_KEY=una-clave-larga-y-aleatoria-de-al-menos-32-caracteres \
  -e STORAGE_LOCATION=/app/files \
  -v eblumbit-files:/app/files \
  eblumbit:latest
```

> La URL de la base de datos usa el nombre del contenedor (`eblumbit-db`) porque ambos están en la misma red de Docker. No uses `localhost` dentro del contenedor de la aplicación.

### 5. Comandos útiles

```bash
docker logs -f eblumbit-app     # ver logs
docker stop eblumbit-app        # detener la app
docker rm eblumbit-app          # eliminar el contenedor
docker stop eblumbit-db         # detener la base de datos
```

---

## Ejecutar con Docker Compose

El repositorio incluye un `docker-compose.yml` que levanta **PostgreSQL + la aplicación** con la red, los volúmenes y el orden de arranque ya configurados.

### 1. Levantar todo

```bash
docker compose up --build -d
```

La primera vez puede tardar varios minutos porque compila la aplicación dentro de la imagen.

### 2. Ver logs y estado

```bash
docker compose ps
docker compose logs -f app
docker compose logs -f db
```

### 3. Detener los servicios

```bash
docker compose down
```

### 4. Detener y borrar también los datos (base de datos y archivos)

```bash
docker compose down -v
```

> `-v` elimina los volúmenes `postgres-data` y `app-files`. Úsalo solo si quieres reiniciar la base de datos desde cero.

### 5. Cambiar puertos o credenciales

Puedes crear un archivo `.env` en la raíz del proyecto (Docker Compose lo lee automáticamente):

```env
APP_PORT=8090
POSTGRES_PORT=5432
POSTGRES_DB=eblumbit_spring
POSTGRES_USER=postgres
POSTGRES_PASSWORD=123456
DDL_AUTO=update
JWT_SECRET=una-clave-larga-y-aleatoria-de-al-menos-32-caracteres
```

Luego vuelve a levantar:

```bash
docker compose up --build -d
```

### 6. Reconstruir después de cambios en el código

```bash
docker compose up --build -d
```

---

## Verificar que la aplicación funciona

### Health check

```bash
curl http://localhost:8090/health
# Respuesta: Service is running!
```

### Swagger UI / OpenAPI

- Swagger UI: <http://localhost:8090/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8090/v3/api-docs>

### Iniciar sesión y probar un endpoint protegido

```bash
curl -X POST http://localhost:8090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

La respuesta incluye `accessToken` y `refreshToken`. Usa el access token en las demás peticiones:

```bash
curl http://localhost:8090/productos \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### Renovar el token

```bash
curl -X POST http://localhost:8090/api/auth/refresh-token \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<REFRESH_TOKEN>"}'
```

---

## Usuarios de prueba

Los datos se generan automáticamente en el primer arranque (`DataSeeder`) y solo si el usuario `admin` no existe.

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `123456` | ADMIN |
| `usuario1` … `usuario5` | `123456` | VENDEDOR |
| `usuario6` … `usuario9` | `123456` | RRHH |

> Cambia o elimina estos usuarios antes de desplegar en producción.

---

## Endpoints principales

Todas las rutas requieren el header `Authorization: Bearer <token>`, excepto `auth`, `health` y `files`.

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/refresh-token` | Renovar access token |
| GET | `/health` | Health check |
| GET/POST/PUT/DELETE | `/productos` | Productos |
| GET/POST/PUT/DELETE | `/categorias` | Categorías |
| GET/POST/PUT/DELETE | `/clientes` | Clientes |
| GET/POST/PUT/DELETE | `/proveedores` | Proveedores |
| GET/POST/PUT/DELETE | `/almacenes` | Almacenes |
| GET/POST/PUT/DELETE | `/sucursales` | Sucursales |
| GET/POST/PUT/DELETE | `/compras` | Compras |
| GET/POST/PUT/DELETE | `/api/ventas` o `/ventas` | Ventas |
| GET/POST/PUT/DELETE | `/usuarios` | Usuarios |
| GET/POST/PUT/DELETE | `/permisos` | Permisos |
| GET/POST/PUT/DELETE | `/roles` | Roles |
| GET | `/files?filePath=...` | Descargar archivo |

La documentación interactiva completa está disponible en Swagger UI.

---

## Variables de entorno

Spring Boot aplica automáticamente estas variables sobre `application.yaml`:

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `SERVER_PORT` | Puerto HTTP | `8090` |
| `SPRING_DATASOURCE_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://localhost:5432/eblumbit_spring` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos | `123456` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estrategia de esquema (`create-drop`, `update`, `validate`, `none`) | `create-drop` |
| `APPLICATION_JWT_SECRET_KEY` | Clave secreta para firmar los JWT | Clave de desarrollo del `application.yaml` |
| `STORAGE_LOCATION` | Carpeta de archivos subidos | `./files` |
| `SPRING_PROFILES_ACTIVE` | Perfil(es) de Spring activos | — |

---

## Solución de problemas

### `UnsupportedClassVersionError` o error de compilación por versión de Java

Estás usando una versión distinta de Java 25. Comprueba:

```bash
java -version
./mvnw -version
```

`JAVA_HOME` debe apuntar al JDK 25.

### `Connection refused` al conectar con PostgreSQL

- Confirma que PostgreSQL está en ejecución: `pg_isready -h localhost -p 5432`.
- Verifica que la base de datos `eblumbit_spring` existe.
- Revisa usuario, contraseña y puerto en `application.yaml` o en las variables de entorno.
- En Docker, la URL debe usar el nombre del servicio (`db`), no `localhost`.

### `FATAL: password authentication failed for user "postgres"`

La contraseña configurada no coincide con la de PostgreSQL. Actualízala en `application.yaml` o define `SPRING_DATASOURCE_PASSWORD`.

### El puerto 8090 o 5432 ya está en uso

Cambia el puerto de la aplicación:

```bash
SERVER_PORT=8091 ./mvnw spring-boot:run
```

En Docker Compose, edita `APP_PORT` o `POSTGRES_PORT` en el archivo `.env`.

### Los datos desaparecen al reiniciar

Es el comportamiento de `ddl-auto: create-drop`. Para conservarlos usa:

```bash
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### Error al guardar o leer archivos

Verifica que la carpeta indicada en `STORAGE_LOCATION` existe y tiene permisos de escritura. En Docker se monta el volumen `app-files` en `/app/files`.

### El primer arranque tarda mucho

`DataSeeder` genera 1000 productos y datos relacionados. Es normal. Solo ocurre cuando el usuario `admin` no existe todavía.

### Docker Compose no espera a la base de datos

El `docker-compose.yml` ya incluye un `healthcheck` y `depends_on: condition: service_healthy`. Si usas una versión antigua de Docker Compose, actualízala.

---

## Estructura del proyecto

```
eblumbit/
├── src/main/java/com/blumbit/eblumbit/
│   ├── auth/            # Login, refresh token, JWT
│   ├── config/          # Seguridad, CORS, beans
│   ├── controllers/     # Endpoints REST
│   ├── dto/             # Objetos de entrada/salida
│   ├── entities/        # Entidades JPA
│   ├── exception/       # Manejo global de errores
│   ├── repository/      # Repositorios Spring Data JPA
│   ├── services/        # Lógica de negocio
│   └── utils/           # DataSeeder y utilidades
├── src/main/resources/
│   ├── application.yaml
│   ├── static/          # Recursos estáticos
│   └── templates/       # Plantillas para reportes PDF
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

---
