package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilControllerTest {

    private PerfilController perfilController;

    private IUsuarioService usuarioServiceMock;

    private Label nombreLabelMock;
    private Label dniLabelMock;
    private Label emailLabelMock;
    private Label telefonoLabelMock;
    private Label tipoLabelMock;

    @BeforeAll
    static void inicializarJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {

        perfilController = new PerfilController();

        usuarioServiceMock = mock(IUsuarioService.class);

        nombreLabelMock = mock(Label.class);
        dniLabelMock = mock(Label.class);
        emailLabelMock = mock(Label.class);
        telefonoLabelMock = mock(Label.class);
        tipoLabelMock = mock(Label.class);

        setField("service", usuarioServiceMock);

        setField("lblNombre", nombreLabelMock);
        setField("lblDni", dniLabelMock);
        setField("lblEmail", emailLabelMock);
        setField("lblTelefono", telefonoLabelMock);
        setField("lblTipo", tipoLabelMock);
    }

    @Test
    void initialize_sinSesion_redirigeLogin() {

        try (MockedStatic<Session> sessionMock = Mockito.mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = Mockito.mockStatic(ScreenManager.class)) {

            sessionMock.when(Session::getCurrentUser).thenReturn(null);

            invoke("initialize");

            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void initialize_conSesion_rellenaLabels() {

        Usuario usuario = new Usuario();
        usuario.setNombre("María García");
        usuario.setDni("12345678Z");
        usuario.setEmail("maria@test.com");
        usuario.setTelefono("666000111");
        usuario.setTipoUsuario("ALUMNO");

        try (MockedStatic<Session> sessionMock = Mockito.mockStatic(Session.class)) {

            sessionMock.when(Session::getCurrentUser).thenReturn(usuario);

            invoke("initialize");

            verify(nombreLabelMock).setText("María García");
            verify(dniLabelMock).setText("12345678Z");
            verify(emailLabelMock).setText("maria@test.com");
            verify(telefonoLabelMock).setText("666000111");
            verify(tipoLabelMock).setText("ALUMNO");
        }
    }

    @Test
    void volver_navegaInicio() {

        try (MockedStatic<ScreenManager> screenMock = Mockito.mockStatic(ScreenManager.class)) {

            invoke("volver");

            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }

    @Test
    void cerrarSesion_llamaLogoutYNavegaLogin() {

        try (MockedStatic<ScreenManager> screenMock = Mockito.mockStatic(ScreenManager.class)) {

            invoke("cerrarSesion");

            verify(usuarioServiceMock).logout();
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void abrirCambiarPassword_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock = Mockito.mockStatic(ScreenManager.class)) {

            invoke("abrirCambiarPassword");

            screenMock.verify(() -> ScreenManager.change("cambiar_password.fxml"));
        }
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = PerfilController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(perfilController, value);
    }

    private void invoke(String methodName) {
        try {
            Method method = PerfilController.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(perfilController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}