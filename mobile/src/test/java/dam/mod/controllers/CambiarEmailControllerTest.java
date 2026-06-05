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
 
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de CambiarEmailController")
class CambiarEmailControllerTest {
 
    @Mock
    private IUsuarioService usuarioService;
 
    @Mock
    private ResourceBundle bundle;
 
    private CambiarEmailController controller;
    private TextField emailField;
    private TextField repeatEmailField;
    private Label mensajeLabel;
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new CambiarEmailController();
 
        emailField       = new TextField();
        repeatEmailField = new TextField();
        mensajeLabel     = new Label();
 
        injectField("emailField",       emailField);
        injectField("repeatEmailField", repeatEmailField);
        injectField("mensajeLabel",     mensajeLabel);
        injectField("usuarioService",   usuarioService);
        injectField("bundle",           bundle);
 
        // bundle.getString() devuelve la clave para no depender de ficheros externos
        lenient().when(bundle.getString(anyString())).thenAnswer(i -> i.getArgument(0));
    }
 
    // --- Early-exit: campos vacíos / no coincidentes ---
 
    @Test
    @DisplayName("email vacío no llama a update")
    void emailVacio_noLlamaUpdate() throws Exception {
        emailField.setText("");
        repeatEmailField.setText("test@email.com");
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
            invokeCambiarEmail();
        }
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("repeat vacío no llama a update")
    void repeatVacio_noLlamaUpdate() throws Exception {
        emailField.setText("test@email.com");
        repeatEmailField.setText("");
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
            invokeCambiarEmail();
        }
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("ambos campos vacíos no llaman a update")
    void ambosCamposVacios_noLlamaUpdate() throws Exception {
        emailField.setText("");
        repeatEmailField.setText("");
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
            invokeCambiarEmail();
        }
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("emails que no coinciden no llaman a update")
    void emailsNoCoinciden_noLlamaUpdate() throws Exception {
        emailField.setText("a@email.com");
        repeatEmailField.setText("b@email.com");
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
            invokeCambiarEmail();
        }
 
        verify(usuarioService, never()).update(any());
    }
 
    // --- Error de formato ---
 
    @Test
    @DisplayName("email con formato inválido no llama a update ni navega")
    void emailFormatoInvalido_noLlamaUpdateNiNavega() throws Exception {
        emailField.setText("no-es-email");
        repeatEmailField.setText("no-es-email");
 
        try (MockedStatic<Validaciones> valMock     = mockStatic(Validaciones.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
             MockedStatic<Session> sessionMock      = mockStatic(Session.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
            valMock.when(() -> Validaciones.validarEmail("no-es-email"))
                   .thenThrow(new IllegalArgumentException("Email no válido"));
 
            invokeCambiarEmail();
 
            verify(usuarioService, never()).update(any());
            screenMock.verify(() -> ScreenManager.change(anyString()), never());
        }
    }
 
    // --- Camino feliz: update + sesión + navegación ---
 
    @Test
    @DisplayName("email válido: llama a update, actualiza sesión y navega a perfil")
    void emailValido_updateSesionYNavega() throws Exception {
        emailField.setText("nuevo@email.com");
        repeatEmailField.setText("nuevo@email.com");
 
        try (MockedStatic<Validaciones> valMock     = mockStatic(Validaciones.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
             MockedStatic<Session> sessionMock      = mockStatic(Session.class)) {
 
            Usuario user = new Usuario();
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            valMock.when(() -> Validaciones.validarEmail("nuevo@email.com")).thenAnswer(i -> null);
            when(usuarioService.update(user)).thenReturn(true);
 
            invokeCambiarEmail();
 
            verify(usuarioService).update(user);
            sessionMock.verify(() -> Session.setCurrentUser(user));
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    // --- Otros ---
 
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
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
 
            Method m = CambiarEmailController.class.getDeclaredMethod("initialize");
            m.setAccessible(true);
            m.invoke(controller);
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    // --- Helpers ---
 
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