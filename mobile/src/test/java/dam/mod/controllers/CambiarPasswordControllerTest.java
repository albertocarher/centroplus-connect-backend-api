package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.PasswordUtils;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
 
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de CambiarPasswordController")
class CambiarPasswordControllerTest {
 
    private CambiarPasswordController controller;
 
    private IUsuarioService usuarioService  = mock(IUsuarioService.class);
    private PasswordField oldPasswordField  = mock(PasswordField.class);
    private PasswordField newPasswordField  = mock(PasswordField.class);
    private PasswordField repeatPasswordField = mock(PasswordField.class);
    private Label mensajeLabel              = mock(Label.class);
    private ResourceBundle bundle           = mock(ResourceBundle.class);
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new CambiarPasswordController();
 
        setField("usuarioService",      usuarioService);
        setField("oldPasswordField",    oldPasswordField);
        setField("newPasswordField",    newPasswordField);
        setField("repeatPasswordField", repeatPasswordField);
        setField("mensajeLabel",        mensajeLabel);
        setField("bundle",              bundle);
 
        // bundle no lanza excepción en ninguna clave
        lenient().when(bundle.getString(anyString())).thenAnswer(i -> i.getArgument(0));
    }
 
    @Test
    @DisplayName("contraseña actual incorrecta no llama a update")
    void contrasenaActualIncorrecta_noActualiza() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock     = mockStatic(PasswordUtils.class)) {
 
            Usuario user = usuarioConPassword("hash");
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            when(oldPasswordField.getText()).thenReturn("wrong");
            pwMock.when(() -> PasswordUtils.checkPassword("wrong", "hash")).thenReturn(false);
 
            invoke("cambiarPassword");
 
            verify(usuarioService, never()).update(any());
        }
    }
 
    @Test
    @DisplayName("contraseñas nuevas no coinciden no llaman a update")
    void contrasenasNuevasNoCoinciden_noActualiza() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock     = mockStatic(PasswordUtils.class)) {
 
            Usuario user = usuarioConPassword("hash");
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            when(oldPasswordField.getText()).thenReturn("correct");
            when(newPasswordField.getText()).thenReturn("nueva123");
            when(repeatPasswordField.getText()).thenReturn("distinta");
            pwMock.when(() -> PasswordUtils.checkPassword("correct", "hash")).thenReturn(true);
 
            invoke("cambiarPassword");
 
            verify(usuarioService, never()).update(any());
        }
    }
 
    @Test
    @DisplayName("contraseña nueva demasiado corta no llama a update")
    void contrasenaCorta_noActualiza() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock     = mockStatic(PasswordUtils.class)) {
 
            Usuario user = usuarioConPassword("hash");
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            when(oldPasswordField.getText()).thenReturn("correct");
            when(newPasswordField.getText()).thenReturn("abc");
            when(repeatPasswordField.getText()).thenReturn("abc");
            pwMock.when(() -> PasswordUtils.checkPassword("correct", "hash")).thenReturn(true);
 
            invoke("cambiarPassword");
 
            verify(usuarioService, never()).update(any());
        }
    }
 
    @Test
    @DisplayName("datos válidos: llama a update y navega a perfil")
    void datosValidos_actualizaYRedirige() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock     = mockStatic(PasswordUtils.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            Usuario user = usuarioConPassword("hash");
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            when(oldPasswordField.getText()).thenReturn("correct");
            when(newPasswordField.getText()).thenReturn("nueva12345");
            when(repeatPasswordField.getText()).thenReturn("nueva12345");
            pwMock.when(() -> PasswordUtils.checkPassword("correct", "hash")).thenReturn(true);
            // El controlador guarda newPass directamente (sin hashear en el propio controlador)
            when(usuarioService.update(user)).thenReturn(true);
 
            invoke("cambiarPassword");
 
            verify(usuarioService).update(user);
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    @Test
    @DisplayName("error en el servicio no navega a perfil")
    void errorEnServicio_noRedirige() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<PasswordUtils> pwMock     = mockStatic(PasswordUtils.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            Usuario user = usuarioConPassword("hash");
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            when(oldPasswordField.getText()).thenReturn("correct");
            when(newPasswordField.getText()).thenReturn("nueva12345");
            when(repeatPasswordField.getText()).thenReturn("nueva12345");
            pwMock.when(() -> PasswordUtils.checkPassword("correct", "hash")).thenReturn(true);
            when(usuarioService.update(user)).thenReturn(false);
 
            invoke("cambiarPassword");
 
            screenMock.verifyNoInteractions();
        }
    }
 
    @Test
    @DisplayName("initialize redirige a login si no hay sesión activa")
    void initialize_sinSesion_redirigirALogin() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
 
            invoke("initialize");
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("volver navega a perfil.fxml")
    void volver_navegaAPerfil() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("volver");
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    // --- Helpers ---
 
    private Usuario usuarioConPassword(String hash) {
        Usuario u = new Usuario();
        u.setId(1);
        u.setPassword(hash);
        return u;
    }
 
    private void setField(String name, Object value) throws Exception {
        Field f = CambiarPasswordController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
 
    private void invoke(String methodName) {
        try {
            Method m = CambiarPasswordController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}