PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    dni TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL,
    telefono TEXT,
    tipo_usuario TEXT NOT NULL,
    password TEXT NOT NULL
);
INSERT INTO usuarios VALUES(1,'Ana Pérez','11111111A','ana@email.com','600111111','ALUMNO', '123456789');
INSERT INTO usuarios VALUES(2,'Luis Ramos','22222222B','luis@email.com','600222222','SOCIO', '123456789');
INSERT INTO usuarios VALUES(3,'Marta Díaz','33333333C','marta@email.com','600333333','AMBOS', '123456789');
CREATE TABLE actividades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    tipo_actividad TEXT NOT NULL,
    duracion INTEGER NOT NULL,
    precio REAL NOT NULL,
    plazas_maximas INTEGER NOT NULL,
    plazas_ocupadas INTEGER NOT NULL
);
INSERT INTO actividades VALUES(1,'Yoga','DEPORTIVA',60,25.499999999999999999,15,8);
INSERT INTO actividades VALUES(2,'Programación Java','ACADEMICA',90,40.0,20,12);
INSERT INTO actividades VALUES(3,'Spinning','DEPORTIVA',45,18.0,12,12);
INSERT INTO actividades VALUES(4,'Inglés técnico','ACADEMICA',60,30.0,18,6);
INSERT INTO actividades VALUES(5,'Sistemas Linux','ACADEMICA',120,45.0,16,10);
INSERT INTO actividades VALUES(6,'Yoga','DEPORTIVA',60,25.499999999999999999,15,8);
INSERT INTO actividades VALUES(7,'Programación Java','ACADEMICA',90,40.0,20,12);
INSERT INTO actividades VALUES(8,'Spinning','DEPORTIVA',45,18.0,12,12);
INSERT INTO actividades VALUES(9,'Inglés técnico','ACADEMICA',60,30.0,18,6);
INSERT INTO actividades VALUES(10,'Sistemas Linux','ACADEMICA',120,45.0,16,10);
CREATE TABLE reservas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    id_actividad INTEGER NOT NULL,
    fecha TEXT NOT NULL,
    estado TEXT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_actividad) REFERENCES actividades(id)
);
INSERT INTO reservas VALUES(1,1,1,'2025-01-10','ACTIVA');
INSERT INTO reservas VALUES(2,2,2,'2025-01-11','ACTIVA');
INSERT INTO reservas VALUES(3,1,1,'2025-01-10','ACTIVA');
INSERT INTO reservas VALUES(4,2,2,'2025-01-11','ACTIVA');
CREATE TABLE incidencias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    asunto TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha TEXT NOT NULL,
    estado TEXT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
INSERT INTO incidencias VALUES(1,1,'Problema con reserva','No puedo reservar una plaza','2025-01-12','ABIERTA');
INSERT INTO incidencias VALUES(2,2,'Cambio de horario','El horario de la actividad no coincide','2025-01-13','EN_PROCESO');
INSERT INTO incidencias VALUES(3,1,'Problema con reserva','No puedo reservar una plaza','2025-01-12','ABIERTA');
INSERT INTO incidencias VALUES(4,2,'Cambio de horario','El horario de la actividad no coincide','2025-01-13','EN_PROCESO');
DELETE FROM sqlite_sequence;
INSERT INTO sqlite_sequence VALUES('usuarios',3);
INSERT INTO sqlite_sequence VALUES('actividades',10);
INSERT INTO sqlite_sequence VALUES('reservas',4);
INSERT INTO sqlite_sequence VALUES('incidencias',4);
COMMIT;
