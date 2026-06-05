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

    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;

    ReservaRepository repository;

    final int id = 1;
    final int idUsuario = 2;
    final int idActividad = 3;
    final LocalDate fecha = LocalDate.of(2025, 6, 10);
    final String estado = "ACTIVA";
    final String nombreActividad = "Yoga matutino";

    @BeforeEach
    void setup() {
        repository = new ReservaRepository();
    }

    private void configurarResultSetFindAll() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getInt("id_usuario")).thenReturn(idUsuario);
        when(resultSet.getInt("id_actividad")).thenReturn(idActividad);
        when(resultSet.getString("fecha")).thenReturn(fecha.toString());
        when(resultSet.getString("estado")).thenReturn(estado);
        when(resultSet.getString("nombre")).thenReturn(nombreActividad);
    }

    private void configurarResultSetFindByUsuario() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getInt("id_usuario")).thenReturn(idUsuario);
        when(resultSet.getInt("id_actividad")).thenReturn(idActividad);
        when(resultSet.getString("fecha")).thenReturn(fecha.toString());
        when(resultSet.getString("estado")).thenReturn(estado);
        when(resultSet.getString("nombre_actividad")).thenReturn(nombreActividad);
    }

    @DisplayName("findAll: devuelve lista con una reserva y mapea todos los campos")
    @Order(1)
    @Test
    void findAllDevuelveListaTest() throws SQLException {
        configurarResultSetFindAll();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            List<Reserva> resultado = repository.findAll();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(1, resultado.size()),
                    () -> Assertions.assertEquals(id, resultado.get(0).getId()),
                    () -> Assertions.assertEquals(idUsuario, resultado.get(0).getIdUsuario()),
                    () -> Assertions.assertEquals(idActividad, resultado.get(0).getIdActividad()),
                    () -> Assertions.assertEquals(estado, resultado.get(0).getEstado()),
                    () -> Assertions.assertEquals(fecha, resultado.get(0).getFecha()),
                    () -> Assertions.assertEquals(nombreActividad, resultado.get(0).getNombreActividad()));
        }
    }

    @DisplayName("findAll: lista vacía cuando no hay filas")
    @Order(2)
    @Test
    void findAllListaVaciaTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.findAll().isEmpty(),
                    "Sin filas debe devolver lista vacía");
        }
    }

    @DisplayName("findAll: lanza RuntimeException si falla la BD")
    @Order(3)
    @Test
    void findAllExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.findAll());
        }
    }

    @DisplayName("findById: devuelve reserva cuando existe y mapea todos los campos")
    @Order(4)
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
                    () -> Assertions.assertEquals(id, resultado.getId()),
                    () -> Assertions.assertEquals(idUsuario, resultado.getIdUsuario()),
                    () -> Assertions.assertEquals(idActividad, resultado.getIdActividad()),
                    () -> Assertions.assertEquals(fecha, resultado.getFecha()),
                    () -> Assertions.assertEquals(estado, resultado.getEstado()));
        }
    }

    @DisplayName("findById: verifica que se setea el id como parámetro")
    @Order(5)
    @Test
    void findByIdVerificaParametroTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            repository.findById(id);

            verify(preparedStatement).setInt(1, id);
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

            Assertions.assertNull(repository.findById(99),
                    "Si no existe debe devolver null");
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

            Assertions.assertTrue(repository.save(
                    new Reserva(0, idUsuario, idActividad, fecha, estado)));
        }
    }

    @DisplayName("save: verifica que se setean todos los parámetros en orden correcto")
    @Order(9)
    @Test
    void saveVerificaParametrosTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            repository.save(new Reserva(0, idUsuario, idActividad, fecha, estado));

            verify(preparedStatement).setInt(1, idUsuario);
            verify(preparedStatement).setInt(2, idActividad);
            verify(preparedStatement).setString(3, fecha.toString());
            verify(preparedStatement).setString(4, estado);
        }
    }

    @DisplayName("save: devuelve false cuando no inserta ninguna fila")
    @Order(10)
    @Test
    void saveFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertFalse(repository.save(
                    new Reserva(0, idUsuario, idActividad, fecha, estado)));
        }
    }

    @DisplayName("save: lanza RuntimeException si falla la BD")
    @Order(11)
    @Test
    void saveExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.save(
                    new Reserva(0, idUsuario, idActividad, fecha, estado)));
        }
    }

    @DisplayName("update: devuelve true cuando actualiza correctamente")
    @Order(12)
    @Test
    void updateTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.update(
                    new Reserva(id, idUsuario, idActividad, fecha, estado)));
        }
    }

    @DisplayName("update: verifica que el id de la reserva se setea en la posición 5")
    @Order(13)
    @Test
    void updateVerificaIdReservaTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            repository.update(new Reserva(id, idUsuario, idActividad, fecha, estado));

            verify(preparedStatement).setInt(5, id);
        }
    }

    @DisplayName("update: devuelve false cuando no encuentra la reserva")
    @Order(14)
    @Test
    void updateFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertFalse(repository.update(
                    new Reserva(99, idUsuario, idActividad, fecha, estado)));
        }
    }

    @DisplayName("update: lanza RuntimeException si falla la BD")
    @Order(15)
    @Test
    void updateExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.update(
                    new Reserva(id, idUsuario, idActividad, fecha, estado)));
        }
    }

    @DisplayName("delete: devuelve true cuando elimina correctamente")
    @Order(16)
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
    @Order(17)
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
    @Order(18)
    @Test
    void deleteExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.delete(id));
        }
    }

    @DisplayName("existsReserva: devuelve true cuando existe la combinación actividad+usuario")
    @Order(19)
    @Test
    void existsReservaTrueTest() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.existsReserva(idActividad, idUsuario));
        }
    }

    @DisplayName("existsReserva: verifica el orden de parámetros (actividad=1, usuario=2)")
    @Order(20)
    @Test
    void existsReservaVerificaParametrosTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            repository.existsReserva(idActividad, idUsuario);

            verify(preparedStatement).setInt(1, idActividad);
            verify(preparedStatement).setInt(2, idUsuario);
        }
    }

    @DisplayName("existsReserva: devuelve false cuando no existe la combinación")
    @Order(21)
    @Test
    void existsReservaFalseTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertFalse(repository.existsReserva(idActividad, idUsuario));
        }
    }

    @DisplayName("existsReserva: lanza RuntimeException si falla la BD")
    @Order(22)
    @Test
    void existsReservaExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class,
                    () -> repository.existsReserva(idActividad, idUsuario));
        }
    }

    @DisplayName("findByIdUsuario: devuelve lista con reservas y nombre de actividad")
    @Order(23)
    @Test
    void findByIdUsuarioDevuelveListaTest() throws SQLException {
        configurarResultSetFindByUsuario();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            List<Reserva> resultado = repository.findByIdUsuario(idUsuario);

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(1, resultado.size()),
                    () -> Assertions.assertEquals(idUsuario, resultado.get(0).getIdUsuario()),
                    () -> Assertions.assertEquals(nombreActividad, resultado.get(0).getNombreActividad()));
        }
    }

    @DisplayName("findByIdUsuario: verifica que se setea el idUsuario como parámetro")
    @Order(24)
    @Test
    void findByIdUsuarioVerificaParametroTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            repository.findByIdUsuario(idUsuario);

            verify(preparedStatement).setInt(1, idUsuario);
        }
    }

    @DisplayName("findByIdUsuario: devuelve lista vacía si el usuario no tiene reservas")
    @Order(25)
    @Test
    void findByIdUsuarioListaVaciaTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.findByIdUsuario(idUsuario).isEmpty());
        }
    }

    @DisplayName("findByIdUsuario: lanza RuntimeException si falla la BD")
    @Order(26)
    @Test
    void findByIdUsuarioExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class,
                    () -> repository.findByIdUsuario(idUsuario));
        }
    }

    @DisplayName("cambiarEstado: devuelve true cuando actualiza correctamente")
    @Order(27)
    @Test
    void cambiarEstadoTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.cambiarEstado(id, "CANCELADA"));
        }
    }

    @DisplayName("cambiarEstado: verifica orden de parámetros (estado=1, id=2)")
    @Order(28)
    @Test
    void cambiarEstadoVerificaParametrosTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            repository.cambiarEstado(id, "CANCELADA");

            verify(preparedStatement).setString(1, "CANCELADA");
            verify(preparedStatement).setInt(2, id);
        }
    }

    @DisplayName("cambiarEstado: devuelve false cuando no encuentra la reserva")
    @Order(29)
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
    @Order(30)
    @Test
    void cambiarEstadoExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class,
                    () -> repository.cambiarEstado(id, "CANCELADA"));
        }
    }

    @DisplayName("findByIdUsuario: mapea correctamente todos los campos de cada reserva")
    @Order(31) // ajusta el Order según los que ya tengas
    @Test
    void findByIdUsuarioMapeaCamposCompletosTest() throws SQLException {
        configurarResultSetFindByUsuario();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Reserva resultado = repository.findByIdUsuario(idUsuario).get(0);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(id, resultado.getId()),
                    () -> Assertions.assertEquals(idUsuario, resultado.getIdUsuario()),
                    () -> Assertions.assertEquals(idActividad, resultado.getIdActividad()),
                    () -> Assertions.assertEquals(fecha, resultado.getFecha()),
                    () -> Assertions.assertEquals(estado, resultado.getEstado()),
                    () -> Assertions.assertEquals(nombreActividad, resultado.getNombreActividad()));
        }
    }

    @DisplayName("findById: verifica que el mensaje de excepción es correcto")
    @Order(32)
    @Test
    void findByIdMensajeExcepcionTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                    () -> repository.findById(id));
            Assertions.assertTrue(ex.getMessage().contains("Error al buscar reserva"));
        }
    }

    
}