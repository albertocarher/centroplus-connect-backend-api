package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
 
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de PerfilController")
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
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        perfilController   = new PerfilController();
        usuarioServiceMock = mock(IUsuarioService.class);
 
        nombreLabelMock   = mock(Label.class);
        dniLabelMock      = mock(Label.class);
        emailLabelMock    = mock(Label.class);
        telefonoLabelMock = mock(Label.class);
        tipoLabelMock     = mock(Label.class);
 
        setField("service",       usuarioServiceMock);
        setField("lblNombre",     nombreLabelMock);
        setField("lblDni",        dniLabelMock);
        setField("lblEmail",      emailLabelMock);
        setField("lblTelefono",   telefonoLabelMock);
        setField("lblTipo",       tipoLabelMock);
 
        // bundle necesario para initialize()
        ResourceBundle bundle = mock(ResourceBundle.class);
        lenient().when(bundle.getString(anyString())).thenAnswer(i -> i.getArgument(0));
        setField("bundle", bundle);
    }
 
    @Test
    @DisplayName("initialize sin sesión redirige a login")
    void initialize_sinSesion_redirigeLogin() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
             MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class)) {
 
            langMock.when(LanguageManager::getBundle).thenReturn(mock(ResourceBundle.class));
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
 
            invoke("initialize");
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("initialize con sesión rellena todos los labels")
    void initialize_conSesion_rellenaLabels() {
        Usuario usuario = new Usuario();
        usuario.setNombre("María García");
        usuario.setDni("12345678Z");
        usuario.setEmail("maria@test.com");
        usuario.setTelefono("666000111");
        usuario.setTipoUsuario("ALUMNO");
 
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class)) {
 
            langMock.when(LanguageManager::getBundle).thenReturn(mock(ResourceBundle.class));
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
    @DisplayName("volver navega a inicio.fxml")
    void volver_navegaInicio() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("volver");
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    @Test
    @DisplayName("cerrarSesion llama logout y navega a login")
    void cerrarSesion_llamaLogoutYNavegaLogin() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("cerrarSesion");
            verify(usuarioServiceMock).logout();
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("abrirCambiarPassword navega a cambiar_password.fxml")
    void abrirCambiarPassword_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("abrirCambiarPassword");
            screenMock.verify(() -> ScreenManager.change("cambiar_password.fxml"));
        }
    }
 
    @Test
    @DisplayName("abrirCambiarEmail navega a cambiar_email.fxml")
    void abrirCambiarEmail_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("abrirCambiarEmail");
            screenMock.verify(() -> ScreenManager.change("cambiar_email.fxml"));
        }
    }
 
    @Test
    @DisplayName("abrirCambiarTelefono navega a cambiar_telefono.fxml")
    void abrirCambiarTelefono_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("abrirCambiarTelefono");
            screenMock.verify(() -> ScreenManager.change("cambiar_telefono.fxml"));
        }
    }
 
    // --- Helpers ---
 
    private void setField(String fieldName, Object value) throws Exception {
        Field field;
        try {
            field = PerfilController.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // bundle puede no existir si el controlador no lo declara
            return;
        }
        field.setAccessible(true);
        field.set(perfilController, value);
    }
 
    private void invoke(String methodName) {
        try {
            Method method = PerfilController.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(perfilController);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

   

    @Test
void setService_asignaElServicio() {
    IUsuarioService nuevoServicio = mock(IUsuarioService.class);

    perfilController.setService(nuevoServicio);

    try (MockedStatic<ScreenManager> screenMock = Mockito.mockStatic(ScreenManager.class)) {
        perfilController.cerrarSesion();
        verify(nuevoServicio).logout();
    }
}
}