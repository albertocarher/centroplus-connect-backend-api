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
@DisplayName("Tests de CambiarTelefonoController")
class CambiarTelefonoControllerTest {
 
    @Mock
    private IUsuarioService usuarioService;
 
    private CambiarTelefonoController controller;
    private TextField telefonoField;
    private TextField repeatTelefonoField;
    private Label mensajeLabel;
 
    // Bundle mockeado para evitar NullPointerException en bundle.getString(...)
    private ResourceBundle bundle;
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new CambiarTelefonoController();
 
        telefonoField = new TextField();
        repeatTelefonoField = new TextField();
        mensajeLabel = new Label();
 
        bundle = mock(ResourceBundle.class);
        lenient().when(bundle.getString(anyString())).thenAnswer(i -> i.getArgument(0));
 
        injectField("telefonoField", telefonoField);
        injectField("repeatTelefonoField", repeatTelefonoField);
        injectField("mensajeLabel", mensajeLabel);
        injectField("usuarioService", usuarioService);
        injectField("bundle", bundle);
    }
 
    @Test
    @DisplayName("teléfono vacío no llama a update")
    void telefonoVacio_noLlamaUpdate() throws Exception {
        telefonoField.setText("");
        repeatTelefonoField.setText("612345678");
 
        invokeCambiarTelefono();
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("teléfono null no llama a update")
    void telefonoNull_noLlamaUpdate() throws Exception {
        telefonoField.setText(null);
        repeatTelefonoField.setText("612345678");
 
        invokeCambiarTelefono();
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("teléfonos que no coinciden no llaman a update")
    void telefonosNoCoinciden_noLlamaUpdate() throws Exception {
        telefonoField.setText("612345678");
        repeatTelefonoField.setText("699999999");
 
        invokeCambiarTelefono();
 
        verify(usuarioService, never()).update(any());
    }
 
    @Test
    @DisplayName("teléfono con formato inválido no llama a update ni navega")
    void telefonoFormatoInvalido_noLlamaUpdateNiNavega() throws Exception {
        telefonoField.setText("123abc");
        repeatTelefonoField.setText("123abc");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
            valMock.when(() -> Validaciones.validarTelefono("123abc"))
                    .thenThrow(new IllegalArgumentException("Teléfono no válido"));
 
            invokeCambiarTelefono();
 
            verify(usuarioService, never()).update(any());
            screenMock.verify(() -> ScreenManager.change(anyString()), never());
        }
    }
 
    @Test
    @DisplayName("teléfono válido: llama a update, actualiza sesión y navega a perfil")
    void telefonoValido_updateSesionYNavega() throws Exception {
        telefonoField.setText("612345678");
        repeatTelefonoField.setText("612345678");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            Usuario user = new Usuario();
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            valMock.when(() -> Validaciones.validarTelefono("612345678")).thenAnswer(i -> null);
            when(usuarioService.update(user)).thenReturn(true);
 
            invokeCambiarTelefono();
 
            verify(usuarioService).update(user);
        }
    }
 
    @Test
    @DisplayName("teléfono válido navega a perfil.fxml")
    void telefonoValido_navegaAPerfil() throws Exception {
        telefonoField.setText("612345678");
        repeatTelefonoField.setText("612345678");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            Usuario user = new Usuario();
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            valMock.when(() -> Validaciones.validarTelefono("612345678")).thenAnswer(i -> null);
            when(usuarioService.update(user)).thenReturn(true);
 
            invokeCambiarTelefono();
 
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    @Test
    @DisplayName("teléfono válido actualiza la sesión")
    void telefonoValido_actualizaSesion() throws Exception {
        telefonoField.setText("612345678");
        repeatTelefonoField.setText("612345678");
 
        try (MockedStatic<Validaciones> valMock = mockStatic(Validaciones.class);
                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
 
            Usuario user = new Usuario();
            sessionMock.when(Session::getCurrentUser).thenReturn(user);
            valMock.when(() -> Validaciones.validarTelefono("612345678")).thenAnswer(i -> null);
            when(usuarioService.update(user)).thenReturn(true);
 
            invokeCambiarTelefono();
 
            sessionMock.verify(() -> Session.setCurrentUser(user));
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    @Test
    @DisplayName("volver navega a perfil.fxml")
    void volver_navegaAPerfil() throws Exception {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            Method m = CambiarTelefonoController.class.getDeclaredMethod("volver");
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
 
            Method m = CambiarTelefonoController.class.getDeclaredMethod("initialize");
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
 
            Method m = CambiarTelefonoController.class.getDeclaredMethod("initialize");
            m.setAccessible(true);
            m.invoke(controller);
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"), never());
        }
    }
 
    // ── helpers ──────────────────────────────────────────────────────────────
 
    private void invokeCambiarTelefono() throws Exception {
        Method m = CambiarTelefonoController.class.getDeclaredMethod("cambiarTelefono");
        m.setAccessible(true);
        m.invoke(controller);
    }
 
    private void injectField(String name, Object value) throws Exception {
        Field f = CambiarTelefonoController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
}