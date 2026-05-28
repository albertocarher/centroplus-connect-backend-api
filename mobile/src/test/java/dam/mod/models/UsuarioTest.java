package dam.mod.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class UsuarioTest {

    Usuario usuario;
    int id = 1;
    String nombre = "Ana García";
    String dni = "12345678A";
    String email = "ana@example.com";
    String telefono = "600123456";
    String tipoUsuario = "admin";
    String password = "secreto123";

    @BeforeEach
    void setup() {
        usuario = new Usuario(id, nombre, dni, email, telefono, tipoUsuario, password);
    }

    @DisplayName("Usuario no es null")
    @Order(1)
    @Test
    void usuarioNotNullTest() {
        Assertions.assertNotNull(usuario, "El usuario no puede ser null");
    }

    @DisplayName("Getter: id")
    @Order(2)
    @Test
    void usuarioGetIdTest() {
        Assertions.assertEquals(id, usuario.getId());
    }

    @DisplayName("Getter: nombre")
    @Order(3)
    @Test
    void usuarioGetNombreTest() {
        Assertions.assertEquals(nombre, usuario.getNombre());
    }

    @DisplayName("Getter: dni")
    @Order(4)
    @Test
    void usuarioGetDniTest() {
        Assertions.assertEquals(dni, usuario.getDni());
    }

    @DisplayName("Getter: email")
    @Order(5)
    @Test
    void usuarioGetEmailTest() {
        Assertions.assertEquals(email, usuario.getEmail());
    }

    @DisplayName("Getter: telefono")
    @Order(6)
    @Test
    void usuarioGetTelefonoTest() {
        Assertions.assertEquals(telefono, usuario.getTelefono());
    }

    @DisplayName("Getter: tipoUsuario")
    @Order(7)
    @Test
    void usuarioGetTipoUsuarioTest() {
        Assertions.assertEquals(tipoUsuario, usuario.getTipoUsuario());
    }

    @DisplayName("Getter: password")
    @Order(8)
    @Test
    void usuarioGetPasswordTest() {
        Assertions.assertEquals(password, usuario.getPassword());
    }

    @DisplayName("setPassword: actualiza correctamente")
    @Order(9)
    @Test
    void usuarioSetPasswordTest() {
        String nuevaPassword = "nuevaPassword456";
        usuario.setPassword(nuevaPassword);
        Assertions.assertEquals(nuevaPassword, usuario.getPassword(),
                "setPassword debe actualizar el valor");
    }

    @DisplayName("setPassword: no afecta a otros campos")
    @Order(10)
    @Test
    void usuarioSetPasswordNoAfectaOtrosCamposTest() {
        usuario.setPassword("otraPassword");
        Assertions.assertAll(
                () -> Assertions.assertEquals(id, usuario.getId()),
                () -> Assertions.assertEquals(nombre, usuario.getNombre()),
                () -> Assertions.assertEquals(email, usuario.getEmail())
        );
    }

    @DisplayName("equals: mismo id → igual")
    @Order(11)
    @Test
    void usuarioEqualsTrueTest() {
        Usuario otro = new Usuario(id, "Otro Nombre", "99999999Z",
                "otro@example.com", "999999999", "cliente", "pass");
        Assertions.assertEquals(usuario, otro,
                "Usuarios con el mismo id deben ser iguales aunque difieran en otros campos");
    }

    @DisplayName("equals: distinto id → no igual")
    @Order(12)
    @Test
    void usuarioEqualsFalseTest() {
        Usuario otro = new Usuario(99, nombre, dni, email, telefono, tipoUsuario, password);
        Assertions.assertNotEquals(usuario, otro, "Distinto id debe ser distinto");
    }

    @DisplayName("equals: misma referencia → igual")
    @Order(13)
    @Test
    void usuarioEqualsMismaReferenciaTest() {
        Assertions.assertEquals(usuario, usuario);
    }

    @DisplayName("equals: comparar con null → false")
    @Order(14)
    @Test
    void usuarioEqualsNullTest() {
        Assertions.assertNotEquals(usuario, null);
    }

    @DisplayName("equals: comparar con tipo distinto → false")
    @Order(15)
    @Test
    void usuarioEqualsTipoDistintoTest() {
        Assertions.assertNotEquals(usuario, "un string");
    }

    @DisplayName("hashCode: mismo id → mismo hashCode")
    @Order(16)
    @Test
    void usuarioHashCodeIgualTest() {
        Usuario otro = new Usuario(id, "Otro Nombre", "99999999Z",
                "otro@example.com", "999999999", "cliente", "pass");
        Assertions.assertEquals(usuario.hashCode(), otro.hashCode(),
                "Mismo id debe producir el mismo hashCode");
    }

    @DisplayName("hashCode: distinto id → distinto hashCode")
    @Order(17)
    @Test
    void usuarioHashCodeDistintoTest() {
        Usuario otro = new Usuario(99, nombre, dni, email, telefono, tipoUsuario, password);
        Assertions.assertNotEquals(usuario.hashCode(), otro.hashCode());
    }
}
