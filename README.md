# CentroPlus Connect

CentroPlus Connect es una aplicación de gestión desarrollada en Java que permite administrar usuarios, actividades, reservas e incidencias dentro de un sistema centralizado.

El proyecto incluye autenticación de usuarios, sistema de reservas, gestión de incidencias, control de sesiones y una base de datos SQLite incluida.

---

# Funcionalidades principales

- Gestión de usuarios con roles
- Sistema de actividades
- Reservas de actividades
- Gestión de incidencias
- Login con sistema de tokens (remember me)
- Sesión persistente
- Base de datos SQLite embebida
- Arquitectura en capas (MVC + Service + Repository)

---

# Tecnologías utilizadas

| Módulo | Tecnologías |
|---|---|
| JavaFX | Java 21, JavaFX, SQLite, JDBC, Maven |
| API REST | Java 21, Spring Boot 3.5.14, Spring Data JPA, Hibernate, SQLite, Swagger |

---

# Estructura del proyecto

```
centroplus/
├── README.md   
├── backend/                       
│   ├── README.md
│   ├── pom.xml
│   ├── centroplus.db
│   └── src/    
│               
├── mobile/             
│   ├── deps.txt
│   └── src/
│       └── main/
│           ├── java/dam/mod/
│           │   ├── controllers/
│           │   ├── models/
│           │   ├── repositories/
│           │   ├── services/
│           │   └── utils/
│           └── resources/
│               ├── database/      
│               ├── i18n/
│               ├── icons/
│               ├── styles/
│               └── views/
├── database/
├── docs/
└── web/
```
---

# Documentación

La documentación completa del proyecto está disponible en los siguientes idiomas:

## 🇪🇸 Español
- Documentación → [Ver documentación en español](./docs/es/README.md)

## 🇬🇧 English
- Documentation → [View English documentation](./docs/en/README.md)

---

## Ejecución del proyecto

### App JavaFX

1. Clonar el repositorio
2. Abrir el proyecto en un IDE compatible con Java

```bash
cd mobile
mvn javafx:run
```

### API REST

```bash
cd backend
mvn spring-boot:run
```

Disponible en `http://localhost:8080`
Swagger UI en `http://localhost:8080/swagger-ui/index.html`

---

# Autores

Proyecto desarrollado por:

- Alberto Carballo Hernández
- Nauzet Torres Tejera  


Desarrollo del ciclo formativo de Desarrollo de Aplicaciones Multiplataforma (DAM)

---

# Nota

Este proyecto forma parte de un sistema de gestión completo con enfoque académico, orientado a la arquitectura en capas y buenas prácticas de desarrollo en Java.