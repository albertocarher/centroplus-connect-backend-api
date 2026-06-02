package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

/**
 * Tests unitarios para PerfilController.
 *
 * Escenarios cubiertos:
 * - initialize() sin sesión → redirige al login.
 * - initialize() con sesión → rellena todos los labels.
 * - volver() → navega a inicio.fxml.
 * - cerrarSesion() → llama a logout y navega al login.
 * - abrirCambiarPassword() → navega a cambiar_password.fxml.
 */
@ExtendWith(MockitoExtension.class)
class PerfilControllerTest {

    private PerfilController controller;

    private Label lblNombre = mock(Label.class);
    private Label lblDni = mock(Label.class);
    private Label lblEmail = mock(Label.class);
    private Label lblTelefono = mock(Label.class);
    private Label lblTipo = mock(Label.class);

    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new PerfilController();

        setField("lblNombre", lblNombre);
        setField("lblDni", lblDni);
        setField("lblEmail", lblEmail);
        setField("lblTelefono", lblTelefono);
        setField("lblTipo", lblTipo);
    }

    @Test
    void initialize_sinSesion_redirigirALogin() {

        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            sessionMock.when(Session::getCurrentUser).thenReturn(null);

            invoke("initialize");

            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void initialize_conSesion_rellenaTodosLosLabels() {

        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {

            Usuario u = usuarioMock();

            sessionMock.when(Session::getCurrentUser).thenReturn(u);

            invoke("initialize");

            verify(lblNombre).setText("María García");
            verify(lblDni).setText("12345678Z");
            verify(lblEmail).setText("maria@test.com");
            verify(lblTelefono).setText("666000111");
            verify(lblTipo).setText("ALUMNO");
        }
    }

    @Test
    void volver_navegaAInicio() {

        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invoke("volver");

            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }

    @Test
    void cerrarSesion_llamaLogoutYNavegaAlLogin() {

        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invoke("cerrarSesion");

            sessionMock.verify(Session::logout);
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void abrirCambiarPassword_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invoke("abrirCambiarPassword");

            screenMock.verify(() -> ScreenManager.change("cambiar_password.fxml"));
        }
    }

    private Usuario usuarioMock() {

        Usuario u = new Usuario();

        u.setNombre("María García");
        u.setDni("12345678Z");
        u.setEmail("maria@test.com");
        u.setTelefono("666000111");
        u.setTipoUsuario("ALUMNO");

        return u;
    }

    private void setField(String name, Object value) throws Exception {

        Field f = PerfilController.class.getDeclaredField(name);

        f.setAccessible(true);
        f.set(controller, value);
    }

    private void invoke(String methodName) {

        try {

            Method m = PerfilController.class.getDeclaredMethod(methodName);

            m.setAccessible(true);
            m.invoke(controller);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}