package dam.mod.repositories;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import dam.mod.models.Actividad;
import dam.mod.repositories.impl.ActividadRepository;

@ExtendWith(MockitoExtension.class)
public class ActividadRepositoryTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;

    ActividadRepository repository;

    final int    id           = 1;
    final String nombre       = "Yoga";
    final String tipo         = "Deporte";
    final int    duracion     = 60;
    final double precio       = 15.0;
    final int    plazasMax    = 20;
    final int    plazasOcup   = 5;

    @BeforeEach
    void setup() {
        repository = new ActividadRepository();
    }

    private void configurarResultSetConUnaFila() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getString("nombre")).thenReturn(nombre);
        when(resultSet.getString("tipo_actividad")).thenReturn(tipo);
        when(resultSet.getInt("duracion")).thenReturn(duracion);
        when(resultSet.getDouble("precio")).thenReturn(precio);
        when(resultSet.getInt("plazas_maximas")).thenReturn(plazasMax);
        when(resultSet.getInt("plazas_ocupadas")).thenReturn(plazasOcup);
    }

    @DisplayName("findAll: devuelve lista con una actividad")
    @Order(1)
    @Test
    void findAllDevuelveListaTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            List<Actividad> resultado = repository.findAll();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(1, resultado.size()),
                    () -> Assertions.assertEquals(nombre, resultado.get(0).getNombre()),
                    () -> Assertions.assertEquals(tipo,   resultado.get(0).getTipoActividad())
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

            List<Actividad> resultado = repository.findAll();

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

    @DisplayName("findById: devuelve actividad cuando existe")
    @Order(4)
    @Test
    void findByIdEncontradoTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Actividad resultado = repository.findById(id);

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(id,     resultado.getId()),
                    () -> Assertions.assertEquals(nombre, resultado.getNombre()),
                    () -> Assertions.assertEquals(precio, resultado.getPrecio(), 0.001)
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

            Actividad resultado = repository.findById(99);

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

            Actividad actividad = new Actividad(0, nombre, tipo, duracion, precio, plazasMax, plazasOcup);
            Assertions.assertTrue(repository.save(actividad));
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

            Actividad actividad = new Actividad(0, nombre, tipo, duracion, precio, plazasMax, plazasOcup);
            Assertions.assertFalse(repository.save(actividad));
        }
    }

    @DisplayName("save: lanza RuntimeException si falla la BD")
    @Order(9)
    @Test
    void saveExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Actividad actividad = new Actividad(0, nombre, tipo, duracion, precio, plazasMax, plazasOcup);
            Assertions.assertThrows(RuntimeException.class, () -> repository.save(actividad));
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

            Actividad actividad = new Actividad(id, nombre, tipo, duracion, precio, plazasMax, plazasOcup);
            Assertions.assertTrue(repository.update(actividad));
        }
    }

    @DisplayName("update: devuelve false cuando no encuentra la actividad")
    @Order(11)
    @Test
    void updateFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Actividad actividad = new Actividad(99, nombre, tipo, duracion, precio, plazasMax, plazasOcup);
            Assertions.assertFalse(repository.update(actividad));
        }
    }

    @DisplayName("update: lanza RuntimeException si falla la BD")
    @Order(12)
    @Test
    void updateExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);

            Actividad actividad = new Actividad(id, nombre, tipo, duracion, precio, plazasMax, plazasOcup);
            Assertions.assertThrows(RuntimeException.class, () -> repository.update(actividad));
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

    @DisplayName("delete: devuelve false cuando no encuentra la actividad")
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