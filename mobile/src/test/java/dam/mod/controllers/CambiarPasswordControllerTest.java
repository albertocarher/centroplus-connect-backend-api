package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.PasswordUtils;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.PasswordField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CambiarPasswordController.
 *
 * Escenarios cubiertos:
 * - Sin sesión → redirige al login.
 * - Contraseña actual incorrecta → no actualiza.
 * - Contraseñas nuevas no coinciden → no actualiza.
 * - Contraseña nueva demasiado corta → no actualiza.
 * - Datos válidos → actualiza y redirige al perfil.
 * - Error en el servicio → no redirige.
 * - volver() → navega a perfil.fxml.
 */
@ExtendWith(MockitoExtension.class)
class CambiarPasswordControllerTest {

    private CambiarPasswordController controller;

    private IUsuarioService usuarioService =
            mock(IUsuarioService.class);

    private PasswordField oldPasswordField =
            mock(PasswordField.class);

    private PasswordField newPasswordField =
            mock(PasswordField.class);

    private PasswordField repeatPasswordField =
            mock(PasswordField.class);

    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {

        controller = new CambiarPasswordController();

        setField("usuarioService", usuarioService);

        setField("oldPasswordField", oldPasswordField);
        setField("newPasswordField", newPasswordField);
        setField("repeatPasswordField",repeatPasswordField);
        setField("mensajeLabel", mock(javafx.scene.control.Label.class));
    }

    @Test
    void initialize_sinSesion_redirigirALogin() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(null);

            invoke("initialize");

            screenMock.verify(() ->
                    ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void cambiarPassword_contrasenaActualIncorrecta_noActualiza() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock =
                     mockStatic(PasswordUtils.class)) {

            Usuario user = usuarioConPassword("hash");

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(user);

            when(oldPasswordField.getText())
                    .thenReturn("incorrecta");

            pwMock.when(() ->
                            PasswordUtils.checkPassword(
                                    "incorrecta",
                                    "hash"))
                    .thenReturn(false);

            invoke("cambiarPassword");

            verify(usuarioService, never())
                    .update(any());
        }
    }

    @Test
    void cambiarPassword_contrasenasNuevasNoCoinciden_noActualiza() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock =
                     mockStatic(PasswordUtils.class)) {

            Usuario user = usuarioConPassword("hash");

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(user);

            when(oldPasswordField.getText())
                    .thenReturn("correcta");

            when(newPasswordField.getText())
                    .thenReturn("nueva123");

            when(repeatPasswordField.getText())
                    .thenReturn("diferente");

            pwMock.when(() ->
                            PasswordUtils.checkPassword(
                                    "correcta",
                                    "hash"))
                    .thenReturn(true);

            invoke("cambiarPassword");

            verify(usuarioService, never())
                    .update(any());
        }
    }

    @Test
    void cambiarPassword_contrasenaNuevaCorta_noActualiza() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock =
                     mockStatic(PasswordUtils.class)) {

            Usuario user = usuarioConPassword("hash");

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(user);

            when(oldPasswordField.getText())
                    .thenReturn("correcta");

            when(newPasswordField.getText())
                    .thenReturn("abc");

            when(repeatPasswordField.getText())
                    .thenReturn("abc");

            pwMock.when(() ->
                            PasswordUtils.checkPassword(
                                    "correcta",
                                    "hash"))
                    .thenReturn(true);

            invoke("cambiarPassword");

            verify(usuarioService, never())
                    .update(any());
        }
    }

    @Test
    void cambiarPassword_datosValidos_actualizaYRedirige() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock =
                     mockStatic(PasswordUtils.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            Usuario user = usuarioConPassword("hash");

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(user);

            when(oldPasswordField.getText())
                    .thenReturn("correcta");

            when(newPasswordField.getText())
                    .thenReturn("nuevaPass123");

            when(repeatPasswordField.getText())
                    .thenReturn("nuevaPass123");

            pwMock.when(() ->
                            PasswordUtils.checkPassword(
                                    "correcta",
                                    "hash"))
                    .thenReturn(true);

            pwMock.when(() ->
                            PasswordUtils.hashPassword(
                                    "nuevaPass123"))
                    .thenReturn("nuevoHash");

            when(usuarioService.update(user))
                    .thenReturn(true);

            invoke("cambiarPassword");

            verify(usuarioService)
                    .update(user);

            screenMock.verify(() ->
                    ScreenManager.change("perfil.fxml"));
        }
    }

    @Test
    void cambiarPassword_errorEnServicio_noRedirige() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock =
                     mockStatic(PasswordUtils.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            Usuario user = usuarioConPassword("hash");

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(user);

            when(oldPasswordField.getText())
                    .thenReturn("correcta");

            when(newPasswordField.getText())
                    .thenReturn("nuevaPass123");

            when(repeatPasswordField.getText())
                    .thenReturn("nuevaPass123");

            pwMock.when(() ->
                            PasswordUtils.checkPassword(
                                    "correcta",
                                    "hash"))
                    .thenReturn(true);

            pwMock.when(() ->
                            PasswordUtils.hashPassword(
                                    "nuevaPass123"))
                    .thenReturn("nuevoHash");

            when(usuarioService.update(user))
                    .thenReturn(false);

            invoke("cambiarPassword");

            screenMock.verifyNoInteractions();
        }
    }

    @Test
    void volver_navegaAPerfil() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("volver");

            screenMock.verify(() ->
                    ScreenManager.change("perfil.fxml"));
        }
    }

    private Usuario usuarioConPassword(String hash) {

        Usuario u = new Usuario();

        u.setId(1);
        u.setPassword(hash);

        return u;
    }

    private void setField(String name, Object value)
            throws Exception {

        Field f = CambiarPasswordController.class
                .getDeclaredField(name);

        f.setAccessible(true);

        f.set(controller, value);
    }

    private void invoke(String methodName) {

        try {

            Method m = CambiarPasswordController.class
                    .getDeclaredMethod(methodName);

            m.setAccessible(true);

            m.invoke(controller);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}