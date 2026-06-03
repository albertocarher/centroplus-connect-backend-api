package dam.mod.repositories;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import dam.mod.models.Reserva;
import dam.mod.repositories.impl.ReservaRepository;
import dam.mod.repositories.sqlite.ConnectionManager;

@ExtendWith(MockitoExtension.class)
public class ReservaRepositoryTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;

    ReservaRepository repository;

    final int       id              = 1;
    final int       idUsuario       = 2;
    final int       idActividad     = 3;
    final LocalDate fecha           = LocalDate.of(2025, 6, 10);
    final String    estado          = "ACTIVA";
    final String    nombreActividad = "Yoga matutino";

    @BeforeEach
    void setup() {
        repository = new ReservaRepository();
    }

    private void configurarResultSetConUnaFila() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getInt("id_usuario")).thenReturn(idUsuario);
        when(resultSet.getInt("id_actividad")).thenReturn(idActividad);
        when(resultSet.getString("fecha")).thenReturn(fecha.toString());
        when(resultSet.getString("estado")).thenReturn(estado);
        when(resultSet.getString("nombre")).thenReturn(nombreActividad);
    }

    @DisplayName("findAll: devuelve lista con una reserva")
    @Order(1)
    @Test
    void findAllDevuelveListaTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            List<Reserva> resultado = repository.findAll();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(1, resultado.size()),
                    () -> Assertions.assertEquals(estado,          resultado.get(0).getEstado()),
                    () -> Assertions.assertEquals(fecha,           resultado.get(0).getFecha()),
                    () -> Assertions.assertEquals(nombreActividad, resultado.get(0).getNombreActividad())
            );
        }
    }

    @DisplayName("findAll: asigna nombreActividad desde el JOIN")
    @Order(2)
    @Test
    void findAllAsignaNombreActividadTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            List<Reserva> resultado = repository.findAll();

            Assertions.assertEquals(nombreActividad, resultado.get(0).getNombreActividad(),
                    "El nombre de actividad debe venir del JOIN con la tabla actividades");
        }
    }

    @DisplayName("findAll: lista vacía cuando no hay filas")
    @Order(3)
    @Test
    void findAllListaVaciaTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            List<Reserva> resultado = repository.findAll();

            Assertions.assertTrue(resultado.isEmpty(), "Sin filas debe devolver lista vacía");
        }
    }

    @DisplayName("findAll: lanza RuntimeException si falla la BD")
    @Order(4)
    @Test
    void findAllExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.findAll());
        }
    }

    // ── findById ──────────────────────────────────────────────────────────

    @DisplayName("findById: devuelve reserva cuando existe")
    @Order(5)
    @Test
    void findByIdEncontradoTest() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getInt("id_usuario")).thenReturn(idUsuario);
        when(resultSet.getInt("id_actividad")).thenReturn(idActividad);
        when(resultSet.getString("fecha")).thenReturn(fecha.toString());
        when(resultSet.getString("estado")).thenReturn(estado);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva resultado = repository.findById(id);

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(id,          resultado.getId()),
                    () -> Assertions.assertEquals(idUsuario,   resultado.getIdUsuario()),
                    () -> Assertions.assertEquals(idActividad, resultado.getIdActividad()),
                    () -> Assertions.assertEquals(fecha,       resultado.getFecha()),
                    () -> Assertions.assertEquals(estado,      resultado.getEstado())
            );
        }
    }

    @DisplayName("findById: devuelve null cuando no existe")
    @Order(6)
    @Test
    void findByIdNoEncontradoTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertNull(repository.findById(99), "Si no existe debe devolver null");
        }
    }

    @DisplayName("findById: lanza RuntimeException si falla la BD")
    @Order(7)
    @Test
    void findByIdExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.findById(id));
        }
    }

    @DisplayName("save: devuelve true cuando inserta correctamente")
    @Order(8)
    @Test
    void saveTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva reserva = new Reserva(0, idUsuario, idActividad, fecha, estado);
            Assertions.assertTrue(repository.save(reserva));
        }
    }

    @DisplayName("save: devuelve false cuando no inserta ninguna fila")
    @Order(9)
    @Test
    void saveFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva reserva = new Reserva(0, idUsuario, idActividad, fecha, estado);
            Assertions.assertFalse(repository.save(reserva));
        }
    }

    @DisplayName("save: lanza RuntimeException si falla la BD")
    @Order(10)
    @Test
    void saveExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva reserva = new Reserva(0, idUsuario, idActividad, fecha, estado);
            Assertions.assertThrows(RuntimeException.class, () -> repository.save(reserva));
        }
    }

    @DisplayName("update: devuelve true cuando actualiza correctamente")
    @Order(11)
    @Test
    void updateTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva reserva = new Reserva(id, idUsuario, idActividad, fecha, estado);
            Assertions.assertTrue(repository.update(reserva));
        }
    }

    @DisplayName("update: devuelve false cuando no encuentra la reserva")
    @Order(12)
    @Test
    void updateFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva reserva = new Reserva(99, idUsuario, idActividad, fecha, estado);
            Assertions.assertFalse(repository.update(reserva));
        }
    }

    @DisplayName("update: lanza RuntimeException si falla la BD")
    @Order(13)
    @Test
    void updateExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva reserva = new Reserva(id, idUsuario, idActividad, fecha, estado);
            Assertions.assertThrows(RuntimeException.class, () -> repository.update(reserva));
        }
    }

    @DisplayName("delete: devuelve true cuando elimina correctamente")
    @Order(14)
    @Test
    void deleteTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.delete(id));
        }
    }

    @DisplayName("delete: devuelve false cuando no encuentra la reserva")
    @Order(15)
    @Test
    void deleteFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertFalse(repository.delete(99));
        }
    }

    @DisplayName("delete: lanza RuntimeException si falla la BD")
    @Order(16)
    @Test
    void deleteExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.delete(id));
        }
    }

    @DisplayName("existsReserva: devuelve true cuando existe la combinación")
    @Order(17)
    @Test
    void existsReservaTrueTest() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.existsReserva(idActividad, idUsuario),
                    "Debe devolver true si ya existe la reserva para esa actividad y usuario");
        }
    }

    @DisplayName("existsReserva: devuelve false cuando no existe la combinación")
    @Order(18)
    @Test
    void existsReservaFalseTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertFalse(repository.existsReserva(idActividad, idUsuario),
                    "Debe devolver false si no existe la reserva");
        }
    }

    @DisplayName("existsReserva: lanza RuntimeException si falla la BD")
    @Order(19)
    @Test
    void existsReservaExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class,
                    () -> repository.existsReserva(idActividad, idUsuario));
        }
    }

    // ── findByIdUsuario ─────────────────────────────────────────────────────

@DisplayName("findByIdUsuario: devuelve lista con reservas")
@Order(20)
@Test
void findByIdUsuarioDevuelveListaTest() throws SQLException {

    when(resultSet.next()).thenReturn(true, false);

    when(resultSet.getInt("id")).thenReturn(id);
    when(resultSet.getInt("id_usuario")).thenReturn(idUsuario);
    when(resultSet.getInt("id_actividad")).thenReturn(idActividad);
    when(resultSet.getString("fecha")).thenReturn(fecha.toString());
    when(resultSet.getString("estado")).thenReturn(estado);
    when(resultSet.getString("nombre_actividad")).thenReturn(nombreActividad);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {

        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        List<Reserva> resultado = repository.findByIdUsuario(idUsuario);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(resultado),
                () -> Assertions.assertEquals(1, resultado.size()),
                () -> Assertions.assertEquals(idUsuario, resultado.get(0).getIdUsuario()),
                () -> Assertions.assertEquals(nombreActividad, resultado.get(0).getNombreActividad())
        );
    }
}

@DisplayName("findByIdUsuario: devuelve lista vacía")
@Order(21)
@Test
void findByIdUsuarioListaVaciaTest() throws SQLException {

    when(resultSet.next()).thenReturn(false);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {

        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        List<Reserva> resultado = repository.findByIdUsuario(idUsuario);

        Assertions.assertTrue(resultado.isEmpty(),
                "Debe devolver lista vacía si el usuario no tiene reservas");
    }
}

@DisplayName("findByIdUsuario: lanza RuntimeException si falla la BD")
@Order(22)
@Test
void findByIdUsuarioExcepcionSQLTest() throws SQLException {

    when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Error BD"));

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {

        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        Assertions.assertThrows(RuntimeException.class,
                () -> repository.findByIdUsuario(idUsuario));
    }
}

// ── cambiarEstado ───────────────────────────────────────────────────────

@DisplayName("cambiarEstado: devuelve true cuando actualiza correctamente")
@Order(23)
@Test
void cambiarEstadoTrueTest() throws SQLException {

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(1);

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {

        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        Assertions.assertTrue(repository.cambiarEstado(id, "CANCELADA"));
    }
}

@DisplayName("cambiarEstado: devuelve false cuando no encuentra la reserva")
@Order(24)
@Test
void cambiarEstadoFalseTest() throws SQLException {

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(0);

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {

        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        Assertions.assertFalse(repository.cambiarEstado(99, "CANCELADA"));
    }
}

@DisplayName("cambiarEstado: lanza RuntimeException si falla la BD")
@Order(25)
@Test
void cambiarEstadoExcepcionSQLTest() throws SQLException {

    when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Error BD"));

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {

        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        Assertions.assertThrows(RuntimeException.class,
                () -> repository.cambiarEstado(id, "CANCELADA"));
    }
}
}