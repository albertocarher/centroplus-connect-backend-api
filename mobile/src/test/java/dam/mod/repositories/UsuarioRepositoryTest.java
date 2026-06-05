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
 
import dam.mod.models.Usuario;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.repositories.sqlite.ConnectionManager;
import dam.mod.utils.PasswordUtils;
 
@ExtendWith(MockitoExtension.class)
public class UsuarioRepositoryTest {
 
    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;
 
    UsuarioRepository repository;
 
    final int    id          = 1;
    final String nombre      = "Ana García";
    final String dni         = "12345678A";
    final String email       = "ana@example.com";
    final String telefono    = "600123456";
    final String tipoUsuario = "AMBOS";
    final String password    = "secreto123";
    final String hashBD      = "$2a$12$hashFicticioParaTests";
 
    @BeforeEach
    void setup() {
        repository = new UsuarioRepository();
    }
 
    private void configurarResultSetConUnaFila() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getString("nombre")).thenReturn(nombre);
        when(resultSet.getString("dni")).thenReturn(dni);
        when(resultSet.getString("email")).thenReturn(email);
        when(resultSet.getString("telefono")).thenReturn(telefono);
        when(resultSet.getString("tipo_usuario")).thenReturn(tipoUsuario);
        when(resultSet.getString("password")).thenReturn(hashBD);
    }
 
    @DisplayName("findAll: devuelve lista con un usuario y mapea todos los campos")
    @Order(1)
    @Test
    void findAllDevuelveListaTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            List<Usuario> resultado = repository.findAll();
 
            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(1,          resultado.size()),
                    () -> Assertions.assertEquals(nombre,     resultado.get(0).getNombre()),
                    () -> Assertions.assertEquals(dni,        resultado.get(0).getDni()),
                    () -> Assertions.assertEquals(email,      resultado.get(0).getEmail()),
                    () -> Assertions.assertEquals(telefono,   resultado.get(0).getTelefono()),
                    () -> Assertions.assertEquals(tipoUsuario,resultado.get(0).getTipoUsuario())
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
 
    @DisplayName("findById: devuelve usuario cuando existe y mapea todos los campos")
    @Order(4)
    @Test
    void findByIdEncontradoTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Usuario resultado = repository.findById(id);
 
            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(id,          resultado.getId()),
                    () -> Assertions.assertEquals(nombre,      resultado.getNombre()),
                    () -> Assertions.assertEquals(dni,         resultado.getDni()),
                    () -> Assertions.assertEquals(email,       resultado.getEmail()),
                    () -> Assertions.assertEquals(telefono,    resultado.getTelefono()),
                    () -> Assertions.assertEquals(tipoUsuario, resultado.getTipoUsuario())
            );
        }
    }
 
    @DisplayName("findById: verifica que se setea el parámetro id en la query")
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
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(anyString())).thenReturn(hashBD);
 
            Assertions.assertTrue(repository.save(
                    new Usuario(0, nombre, dni, email, telefono, tipoUsuario, password)));
        }
    }
 
    @DisplayName("save: almacena la contraseña hasheada, nunca en texto plano")
    @Order(9)
    @Test
    void saveAlmacenaPasswordHasheadaTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(password)).thenReturn(hashBD);
 
            repository.save(new Usuario(0, nombre, dni, email, telefono, tipoUsuario, password));
 
            mockPwd.verify(() -> PasswordUtils.hashPassword(password));
            verify(preparedStatement).setString(6, hashBD);
        }
    }
 
    @DisplayName("save: devuelve false cuando no inserta ninguna fila")
    @Order(10)
    @Test
    void saveFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(anyString())).thenReturn(hashBD);
 
            Assertions.assertFalse(repository.save(
                    new Usuario(0, nombre, dni, email, telefono, tipoUsuario, password)));
        }
    }
 
    @DisplayName("save: lanza RuntimeException si falla la BD")
    @Order(11)
    @Test
    void saveExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(anyString())).thenReturn(hashBD);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.save(
                    new Usuario(0, nombre, dni, email, telefono, tipoUsuario, password)));
        }
    }
 
    @DisplayName("update: devuelve true cuando actualiza correctamente")
    @Order(12)
    @Test
    void updateTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(anyString())).thenReturn(hashBD);
 
            Assertions.assertTrue(repository.update(
                    new Usuario(id, nombre, dni, email, telefono, tipoUsuario, password)));
        }
    }
 
    @DisplayName("update: verifica que la contraseña se rehashea y se setean todos los parámetros")
    @Order(13)
    @Test
    void updateVerificaParametrosYHashTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(password)).thenReturn(hashBD);
 
            repository.update(new Usuario(id, nombre, dni, email, telefono, tipoUsuario, password));

            mockPwd.verify(() -> PasswordUtils.hashPassword(password));
            verify(preparedStatement).setString(6, hashBD);
            verify(preparedStatement).setInt(7, id);
        }
    }
 
    @DisplayName("update: devuelve false cuando no encuentra el usuario")
    @Order(14)
    @Test
    void updateFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(anyString())).thenReturn(hashBD);
 
            Assertions.assertFalse(repository.update(
                    new Usuario(99, nombre, dni, email, telefono, tipoUsuario, password)));
        }
    }
 
    @DisplayName("update: lanza RuntimeException si falla la BD")
    @Order(15)
    @Test
    void updateExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.hashPassword(anyString())).thenReturn(hashBD);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.update(
                    new Usuario(id, nombre, dni, email, telefono, tipoUsuario, password)));
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
 
    @DisplayName("delete: verifica que se setea el id correcto en la query")
    @Order(17)
    @Test
    void deleteVerificaParametroTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.delete(id);
 
            verify(preparedStatement).setInt(1, id);
        }
    }
 
    @DisplayName("delete: devuelve false cuando no encuentra el usuario")
    @Order(18)
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
    @Order(19)
    @Test
    void deleteExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.delete(id));
        }
    }
 
    @DisplayName("login: devuelve usuario cuando credenciales correctas")
    @Order(20)
    @Test
    void loginCredencialesCorrectasTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.checkPassword(password, hashBD)).thenReturn(true);
 
            Usuario resultado = repository.login(dni, password);
 
            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(id,     resultado.getId()),
                    () -> Assertions.assertEquals(nombre, resultado.getNombre()),
                    () -> Assertions.assertEquals(dni,    resultado.getDni())
            );
        }
    }
 
    @DisplayName("login: verifica que checkPassword se llama con password y hash de BD")
    @Order(21)
    @Test
    void loginVerificaCheckPasswordTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.checkPassword(password, hashBD)).thenReturn(true);
 
            repository.login(dni, password);
 
            mockPwd.verify(() -> PasswordUtils.checkPassword(password, hashBD));
        }
    }
 
    @DisplayName("login: devuelve null cuando la contraseña es incorrecta")
    @Order(22)
    @Test
    void loginPasswordIncorrectaTest() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("password")).thenReturn(hashBD);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
            mockPwd.when(() -> PasswordUtils.checkPassword(anyString(), anyString())).thenReturn(false);
 
            Assertions.assertNull(repository.login(dni, "passwordErronea"),
                    "Con contraseña incorrecta debe devolver null");
        }
    }
 
    @DisplayName("login: devuelve null cuando el DNI no existe")
    @Order(23)
    @Test
    void loginDniNoExisteTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertNull(repository.login("99999999Z", password),
                    "Con DNI inexistente debe devolver null");
        }
    }
 
    @DisplayName("login: lanza RuntimeException si falla la BD")
    @Order(24)
    @Test
    void loginExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mockConn = mockStatic(ConnectionManager.class);
             MockedStatic<PasswordUtils>     mockPwd  = mockStatic(PasswordUtils.class)) {
 
            mockConn.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class,
                    () -> repository.login(dni, password));
        }
    }
 
    @DisplayName("findByDni: devuelve usuario cuando existe y mapea todos los campos")
    @Order(25)
    @Test
    void findByDniEncontradoTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Usuario resultado = repository.findByDni(dni);
 
            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(id,          resultado.getId()),
                    () -> Assertions.assertEquals(nombre,      resultado.getNombre()),
                    () -> Assertions.assertEquals(dni,         resultado.getDni()),
                    () -> Assertions.assertEquals(email,       resultado.getEmail()),
                    () -> Assertions.assertEquals(telefono,    resultado.getTelefono()),
                    () -> Assertions.assertEquals(tipoUsuario, resultado.getTipoUsuario())
            );
        }
    }
 
    @DisplayName("findByDni: verifica que se setea el DNI como parámetro")
    @Order(26)
    @Test
    void findByDniVerificaParametroTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.findByDni(dni);
 
            verify(preparedStatement).setString(1, dni);
        }
    }
 
    @DisplayName("findByDni: devuelve null cuando no existe")
    @Order(27)
    @Test
    void findByDniNoEncontradoTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertNull(repository.findByDni("99999999Z"),
                    "DNI inexistente debe devolver null");
        }
    }
 
    @DisplayName("findByDni: lanza RuntimeException si falla la BD")
    @Order(28)
    @Test
    void findByDniExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class,
                    () -> repository.findByDni(dni));
        }
    }

    @DisplayName("findById: el mensaje de la RuntimeException contiene el texto esperado")
@Order(29)
@Test
void findByIdMensajeExcepcionTest() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> repository.findById(id));
        Assertions.assertTrue(ex.getMessage().contains("Error al buscar usuario"));
    }
}

@DisplayName("findByDni: el mensaje de la RuntimeException contiene el texto esperado")
@Order(30)
@Test
void findByDniMensajeExcepcionTest() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));

    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
        mock.when(ConnectionManager::getConnection).thenReturn(connection);

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> repository.findByDni(dni));
        Assertions.assertTrue(ex.getMessage().contains("Error al buscar usuario por DNI"));
    }
}
}