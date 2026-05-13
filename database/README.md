

# Base de datos — CentroPlus Connect

<img src="./IMG/Diagrama%20CentroPlus.png" width="600">

## 1. Descripción

La base de datos del proyecto **CentroPlus Connect** está desarrollada en **SQLite3** y tiene como objetivo almacenar toda la información necesaria para la gestión del sistema.

Se trata de una base de datos relacional sencilla, diseñada para ser utilizada por una aplicación Java (JDBC), una API y una interfaz de usuario.



## 2. Tablas de la base de datos

La base de datos está compuesta por las siguientes tablas:

- usuarios
- actividades
- reservas
- incidencias



## 3. Estructura de las tablas

### usuarios

Almacena la información de los usuarios registrados en el sistema.

Campos:

- id (PK, AUTOINCREMENT)
- nombre
- dni (único)
- email
- telefono
- tipo_usuario



### actividades

Almacena las actividades disponibles en el sistema.

Campos:

- id (PK, AUTOINCREMENT)
- nombre
- tipo_actividad
- duracion
- precio
- plazas_maximas
- plazas_ocupadas


### reservas

Almacena las reservas realizadas por los usuarios.

Campos:

- id (PK, AUTOINCREMENT)
- id_usuario (FK)
- id_actividad (FK)
- fecha
- estado

Relaciones:

- usuarios → reservas (1:N)
- actividades → reservas (1:N)



### incidencias

Almacena incidencias reportadas por los usuarios.

Campos:

- id (PK, AUTOINCREMENT)
- id_usuario (FK)
- asunto
- descripcion
- fecha
- estado

Relación:

- usuarios → incidencias (1:N)



## 4. Relaciones entre tablas

- usuarios 1 ── N reservas
- actividades 1 ── N reservas
- usuarios 1 ── N incidencias



## 5. Tipo de datos y validaciones

Aunque SQLite permite guardar cualquier valor en TEXT, la aplicación debe validar:

### tipo_usuario

ALUMNO | SOCIO | AMBOS



### tipo_actividad

ACADEMICA | DEPORTIVA



### estado de reservas

ACTIVA | CANCELADA



### estado de incidencias

ABIERTA | EN_PROCESO | CERRADA



## 6. Claves primarias y foráneas

- Todas las tablas usan `id` como clave primaria con AUTOINCREMENT.
- Relaciones mediante claves foráneas:
  - reservas.id_usuario → usuarios.id
  - reservas.id_actividad → actividades.id
  - incidencias.id_usuario → usuarios.id



## 7. Tecnología utilizada

- SQLite3
- JDBC (Java)



## 8. Ubicación de la base de datos

database/centroplus.db

Acceso desde Java:

```java
String url = "jdbc:sqlite:database/centroplus.db";
```

## Regresar al inicio

[INICIO](../README.md)