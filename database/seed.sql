PRAGMA foreign_keys = ON;

INSERT INTO usuarios (nombre, dni, email, telefono, tipo_usuario, password)
VALUES
    ('Ana Pérez', '11111111A', 'ana@email.com', '600111111', 'ALUMNO', ''),
    ('Luis Ramos', '22222222B', 'luis@email.com', '600222222', 'SOCIO', ''),
    ('Marta Díaz', '33333333C', 'marta@email.com', '600333333', 'AMBOS', '');

INSERT INTO actividades (nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas)
VALUES
    ('Yoga', 'DEPORTIVA', 60, 25.50, 15, 8),
    ('Programación Java', 'ACADEMICA', 90, 40.00, 20, 12),
    ('Spinning', 'DEPORTIVA', 45, 18.00, 12, 12),
    ('Inglés técnico', 'ACADEMICA', 60, 30.00, 18, 6),
    ('Sistemas Linux', 'ACADEMICA', 120, 45.00, 16, 10);

INSERT INTO reservas (id_usuario, id_actividad, fecha, estado)
VALUES
    (1, 1, '2025-01-10', 'ACTIVA'),
    (2, 2, '2025-01-11', 'ACTIVA');

INSERT INTO incidencias (id_usuario, asunto, descripcion, fecha, estado)
VALUES
    (1, 'Problema con reserva', 'No puedo reservar una plaza', '2025-01-12', 'ABIERTA'),
    (2, 'Cambio de horario', 'El horario de la actividad no coincide', '2025-01-13', 'EN_PROCESO');