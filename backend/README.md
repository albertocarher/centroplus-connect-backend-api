# CentroPlus Connect — Backend API

API REST desarrollada con Spring Boot para la aplicación de gestión **CentroPlus**, siguiendo arquitectura hexagonal (Ports & Adapters).

---

## Tecnologías

- Java 21
- Spring Boot 3.5.14
- Spring Data JPA + Hibernate 6
- SQLite (base de datos existente de la app JavaFX)
- Springdoc OpenAPI (Swagger UI)

---

## Estructura del proyecto

```
src/main/java/com/dam/mod/
│
├── ApiApplication.java
│
├── domain/
│   ├── models/                     # Modelos de dominio (POJOs puros)
│   │   ├── Usuario.java
│   │   ├── Actividad.java
│   │   ├── Reserva.java
│   │   └── Incidencia.java
│   │
│   └── ports/                      # Interfaces de salida (contratos de persistencia)
│       ├── IUsuarioRepository.java
│       ├── IActividadRepository.java
│       ├── IReservaRepository.java
│       └── IIncidenciaRepository.java
│
├── business/                       # Capa de negocio
│   ├── UsuarioServicePort.java     # Interfaces de entrada
│   ├── ActividadServicePort.java
│   ├── ReservaServicePort.java
│   ├── IncidenciaServicePort.java
│   ├── UsuarioService.java         # Implementaciones con lógica de negocio
│   ├── ActividadService.java
│   ├── ReservaService.java
│   └── IncidenciaService.java
│
└── adapters/
    ├── in/
    │   ├── controller/             # Controladores REST
    │   │   ├── UsuarioController.java
    │   │   ├── ActividadController.java
    │   │   ├── ReservaController.java
    │   │   └── IncidenciaController.java
    │   └── api/                    # DTOs de entrada y salida
    │       ├── UsuarioRequest.java
    │       ├── UsuarioResponse.java
    │       ├── LoginRequest.java
    │       ├── ActividadRequest.java
    │       ├── ActividadResponse.java
    │       ├── ReservaRequest.java
    │       ├── ReservaResponse.java
    │       ├── IncidenciaRequest.java
    │       └── IncidenciaResponse.java
    │
    ├── mapper/                     # Conversión dominio <-> Entity JPA
    │   ├── UsuarioMapper.java
    │   ├── ActividadMapper.java
    │   ├── ReservaMapper.java
    │   └── IncidenciaMapper.java
    │
    └── out/
        └── persistence/            # Capa de persistencia JPA
            ├── ApiJpaUsuario.java          # Entities JPA
            ├── ApiJpaActividad.java
            ├── ApiJpaReserva.java
            ├── ApiJpaIncidencia.java
            ├── UsuarioRepositoryJpa.java   # Spring Data repositories
            ├── ActividadRepositoryJpa.java
            ├── ReservaRepositoryJpa.java
            ├── IncidenciaRepositoryJpa.java
            ├── UsuarioPersistenceAdapter.java   # Implementan ports de dominio
            ├── ActividadPersistenceAdapter.java
            ├── ReservaPersistenceAdapter.java
            └── IncidenciaPersistenceAdapter.java
```

---

## Configuración

### `application.properties`

```properties
spring.datasource.url=jdbc:sqlite:centroplus.db?date_string_format=yyyy-MM-dd
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
server.error.include-message=always
```

### Base de datos

Coloca el archivo `centroplus.db` en la raíz del proyecto, al mismo nivel que el `pom.xml`.

```
backend/
├── src/
├── pom.xml
└── centroplus.db   ← aquí
```

### Dependencias clave (`pom.xml`)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.45.1.0</version>
</dependency>
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-community-dialects</artifactId>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

---

## Arrancar la aplicación

```bash
mvn spring-boot:run
```

La API arranca en `http://localhost:8080`.

---

## Documentación interactiva (Swagger UI)

Una vez arrancada la aplicación, accede a:

```
http://localhost:8080/swagger-ui/index.html
```

Desde ahí puedes probar todos los endpoints directamente en el navegador.

---

## Endpoints

### Usuarios `/usuarios`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/usuarios` | Listar todos los usuarios |
| GET | `/usuarios/{id}` | Buscar usuario por ID |
| GET | `/usuarios/dni/{dni}` | Buscar usuario por DNI |
| POST | `/usuarios` | Crear nuevo usuario |
| POST | `/usuarios/login` | Login con DNI y password |
| PUT | `/usuarios/{id}` | Actualizar usuario |
| DELETE | `/usuarios/{id}` | Eliminar usuario |

### Actividades `/actividades`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/actividades` | Listar todas las actividades |
| GET | `/actividades/{id}` | Buscar actividad por ID |
| POST | `/actividades` | Crear nueva actividad |
| PUT | `/actividades/{id}` | Actualizar actividad |
| DELETE | `/actividades/{id}` | Eliminar actividad |

### Reservas `/reservas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/reservas` | Listar todas las reservas |
| GET | `/reservas/{id}` | Buscar reserva por ID |
| GET | `/reservas/usuario/{idUsuario}` | Reservas de un usuario |
| POST | `/reservas` | Crear nueva reserva |
| PUT | `/reservas/{id}` | Actualizar reserva |
| PATCH | `/reservas/{id}/estado` | Cambiar estado de una reserva |
| DELETE | `/reservas/{id}` | Eliminar reserva |

### Incidencias `/incidencias`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/incidencias` | Listar todas las incidencias |
| GET | `/incidencias/{id}` | Buscar incidencia por ID |
| POST | `/incidencias` | Crear nueva incidencia |
| PUT | `/incidencias/{id}` | Actualizar incidencia |
| DELETE | `/incidencias/{id}` | Eliminar incidencia |

---

## Arquitectura hexagonal

El proyecto sigue el patrón **Ports & Adapters**:

```
[Swagger/Cliente]
       ↓
[Controller]  →  adapters/in
       ↓
[ServicePort]  →  business (interfaz)
       ↓
[Service]  →  business (lógica de negocio)
       ↓
[IRepository]  →  domain/ports (interfaz)
       ↓
[PersistenceAdapter]  →  adapters/out (implementación)
       ↓
[RepositoryJpa]  →  Spring Data JPA
       ↓
[ApiJpaEntity]  →  Hibernate / SQLite
```

Los modelos de dominio (`domain/models`) son POJOs puros sin dependencias de JPA ni de frameworks. La lógica de negocio en `business` no sabe nada de cómo se persisten los datos — solo conoce las interfaces de los ports.

---

## Notas sobre SQLite

SQLite almacena las fechas como texto (`TEXT`) en formato `yyyy-MM-dd`. Para que Hibernate las lea correctamente se usa:

- `@JdbcTypeCode(Types.VARCHAR)` en los campos `fecha` de las Entities
- El parámetro `?date_string_format=yyyy-MM-dd` en la URL de conexión
- `ddl-auto=none` para evitar que Hibernate valide o modifique el esquema existente

---

## Seguridad

Las contraseñas se almacenan hasheadas usando `PasswordUtils` (BCrypt). El endpoint `/usuarios/login` compara la contraseña recibida con el hash almacenado — nunca se guarda ni devuelve la contraseña en texto plano. El campo `password` está excluido de `UsuarioResponse`.
