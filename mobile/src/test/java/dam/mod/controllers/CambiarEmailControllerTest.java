package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import dam.mod.utils.Validaciones;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de CambiarEmailController")
class CambiarEmailControllerTest {
 
    @Mock
    private IUsuarioService usuarioService;
 
    private CambiarEmailController controller;
    private TextField emailField;
    private TextField repeatEmailField;
    private Label mensajeLabel;
 
    // Bundle mockeado para evitar NullPointerException en bundle.getString(...)
    private ResourceBundle bundle;
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new CambiarEmailController();
 
        emailField = new TextField();
        repeatEmailField = new TextField();
        mensajeLabel = new Label();
 
        bundle = mock(ResourceBundle.class);
        lenient().when(bundle.getString(anyString())).thenAnswer(i -> i.getArgument(0));
 
        injectField("emailField", emailField);
        injectField("repeatEmailField", repeatEmailField);
        injectField("mensajeLabel", mensajeLabel);
        injectField("usuarioService", usuarioService);
        injectField("bundle", bundle);
    }
 
    @Test
    @DisplayName("email vacío no llama a update")
    void emailVacio_noLlamaUpdate() throws Exception {
        emailField.setText("");
        repeatEmailField.setText("test@email.com");
 
        invokeCambiarEmail();
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("repeat vacío no llama a update")
    void repeatVacio_noLlamaUpdate() throws Exception {
        emailField.setText("test@email.com");
        repeatEmailField.setText("");
 
        invokeCambiarEmail();
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("ambos campos vacíos no llaman a update")
    void ambosCamposVacios_noLlamaUpdate() throws Exception {
        emailField.setText("");
        repeatEmailField.setText("");
 
        invokeCambiarEmail();
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("emails que no coinciden no llaman a update")
    void emailsNoCoinciden_noLlamaUpdate() throws Exception {
        emailField.setText("a@email.com");
        repeatEmailField.setText("b@email.com");
 
        invokeCambiarEmail();
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("email con formato inválido no llama a update ni navega")
    void emailFormatoInvalido_noLlamaUpdateNiNavega() throws Exception {
        emailField.setText("no-es-email");
        repeatEmailField.setText("no-es-email");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
            valMock.when(() -> Validaciones.validarEmail("no-es-email"))
                    .thenThrow(new IllegalArgumentException("Email no válido"));
 
            invokeCambiarEmail();
 
            verify(usuarioService, never()).update(any());
            screenMock.verify(() -> ScreenManager.change(anyString()), never());
        }
    }
 
    @Test
    @DisplayName("email válido: llama a update, actualiza sesión y navega a perfil")
    void emailValido_updateSesionYNavega() throws Exception {
        emailField.setText("nuevo@email.com");
        repeatEmailField.setText("nuevo@email.com");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            Usuario user = new Usuario();
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            valMock.when(() -> Validaciones.validarEmail("nuevo@email.com")).thenAnswer(i -> null);
            when(usuarioService.update(user)).thenReturn(true);
 
            invokeCambiarEmail();
 
            verify(usuarioService).update(user);
        }
    }
 
    @Test
    @DisplayName("email válido navega a perfil.fxml")
    void emailValido_navegaAPerfil() throws Exception {
        emailField.setText("nuevo@email.com");
        repeatEmailField.setText("nuevo@email.com");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            Usuario user = new Usuario();
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            valMock.when(() -> Validaciones.validarEmail("nuevo@email.com")).thenAnswer(i -> null);
            when(usuarioService.update(user)).thenReturn(true);
 
            invokeCambiarEmail();
 
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    @Test
    @DisplayName("email válido actualiza la sesión")
    void emailValido_actualizaSesion() throws Exception {
        emailField.setText("nuevo@email.com");
        repeatEmailField.setText("nuevo@email.com");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            Usuario user = new Usuario();
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            valMock.when(() -> Validaciones.validarEmail("nuevo@email.com")).thenAnswer(i -> null);
            when(usuarioService.update(user)).thenReturn(true);
 
            invokeCambiarEmail();
 
            sessionMock.verify(() -> Session.setCurrentUser(user));
        }
    }
 
    @Test
    @DisplayName("volver navega a perfil.fxml")
    void volver_navegaAPerfil() throws Exception {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            Method m = CambiarEmailController.class.getDeclaredMethod("volver");
            m.setAccessible(true);
            m.invoke(controller);
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    @Test
    @DisplayName("initialize redirige a login si no hay sesión activa")
    void initialize_sinSesion() throws Exception {
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
 
            Method m = CambiarEmailController.class.getDeclaredMethod("initialize");
            m.setAccessible(true);
            m.invoke(controller);
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("initialize con sesión activa no redirige al login")
    void initialize_conSesion_noRedirige() throws Exception {
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
 
            Method m = CambiarEmailController.class.getDeclaredMethod("initialize");
            m.setAccessible(true);
            m.invoke(controller);
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"), never());
        }
    }
 
    // ── helpers ──────────────────────────────────────────────────────────────
 
    private void invokeCambiarEmail() throws Exception {
        Method m = CambiarEmailController.class.getDeclaredMethod("cambiarEmail");
        m.setAccessible(true);
        m.invoke(controller);
    }
 
    private void injectField(String name, Object value) throws Exception {
        Field f = CambiarEmailController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
}