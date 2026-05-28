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

import dam.mod.models.Incidencia;
import dam.mod.repositories.impl.IncidenciaRepository;

@ExtendWith(MockitoExtension.class)
public class IncidenciaRepositoryTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;

    IncidenciaRepository repository;

    final int       id          = 1;
    final int       idUsuario   = 2;
    final String    asunto      = "Problema acceso";
    final String    descripcion = "No puedo entrar";
    final LocalDate fecha       = LocalDate.of(2025, 1, 15);
    final String    estado      = "activo";

    @BeforeEach
    void setup() {
        repository = new IncidenciaRepository();
    }

    private void configurarResultSetConUnaFila() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getInt("id_usuario")).thenReturn(idUsuario);
        when(resultSet.getString("asunto")).thenReturn(asunto);
        when(resultSet.getString("descripcion")).thenReturn(descripcion);
        when(resultSet.getString("fecha")).thenReturn(fecha.toString());
        when(resultSet.getString("estado")).thenReturn(estado);
    }


    @DisplayName("findAll: devuelve lista con una incidencia")
    @Order(1)
    @Test
    void findAllDevuelveListaTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            List<Incidencia> resultado = repository.findAll();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(1, resultado.size()),
                    () -> Assertions.assertEquals(asunto,  resultado.get(0).getAsunto()),
                    () -> Assertions.assertEquals(estado,  resultado.get(0).getEstado()),
                    () -> Assertions.assertEquals(fecha,   resultado.get(0).getFecha())
            );
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

            List<Incidencia> resultado = repository.findAll();

            Assertions.assertTrue(resultado.isEmpty(), "Sin filas debe devolver lista vacía");
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

    @DisplayName("findById: devuelve incidencia cuando existe")
    @Order(4)
    @Test
    void findByIdEncontradoTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia resultado = repository.findById(id);

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(id,          resultado.getId()),
                    () -> Assertions.assertEquals(idUsuario,   resultado.getIdUsuario()),
                    () -> Assertions.assertEquals(asunto,      resultado.getAsunto()),
                    () -> Assertions.assertEquals(descripcion, resultado.getDescripcion()),
                    () -> Assertions.assertEquals(fecha,       resultado.getFecha()),
                    () -> Assertions.assertEquals(estado,      resultado.getEstado())
            );
        }
    }

    @DisplayName("findById: devuelve null cuando no existe")
    @Order(5)
    @Test
    void findByIdNoEncontradoTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia resultado = repository.findById(99);

            Assertions.assertNull(resultado, "Si no existe debe devolver null");
        }
    }

    @DisplayName("findById: lanza RuntimeException si falla la BD")
    @Order(6)
    @Test
    void findByIdExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.findById(id));
        }
    }

    @DisplayName("save: devuelve true cuando inserta correctamente")
    @Order(7)
    @Test
    void saveTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia incidencia = new Incidencia(0, idUsuario, asunto, descripcion, fecha, estado);
            Assertions.assertTrue(repository.save(incidencia));
        }
    }

    @DisplayName("save: devuelve false cuando no inserta ninguna fila")
    @Order(8)
    @Test
    void saveFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia incidencia = new Incidencia(0, idUsuario, asunto, descripcion, fecha, estado);
            Assertions.assertFalse(repository.save(incidencia));
        }
    }

    @DisplayName("save: lanza RuntimeException si falla la BD")
    @Order(9)
    @Test
    void saveExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia incidencia = new Incidencia(0, idUsuario, asunto, descripcion, fecha, estado);
            Assertions.assertThrows(RuntimeException.class, () -> repository.save(incidencia));
        }
    }

    @DisplayName("update: devuelve true cuando actualiza correctamente")
    @Order(10)
    @Test
    void updateTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia incidencia = new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);
            Assertions.assertTrue(repository.update(incidencia));
        }
    }

    @DisplayName("update: devuelve false cuando no encuentra la incidencia")
    @Order(11)
    @Test
    void updateFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia incidencia = new Incidencia(99, idUsuario, asunto, descripcion, fecha, estado);
            Assertions.assertFalse(repository.update(incidencia));
        }
    }

    @DisplayName("update: lanza RuntimeException si falla la BD")
    @Order(12)
    @Test
    void updateExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Incidencia incidencia = new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);
            Assertions.assertThrows(RuntimeException.class, () -> repository.update(incidencia));
        }
    }

    @DisplayName("delete: devuelve true cuando elimina correctamente")
    @Order(13)
    @Test
    void deleteTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertTrue(repository.delete(id));
        }
    }

    @DisplayName("delete: devuelve false cuando no encuentra la incidencia")
    @Order(14)
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
    @Order(15)
    @Test
    void deleteExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Assertions.assertThrows(RuntimeException.class, () -> repository.delete(id));
        }
    }
}