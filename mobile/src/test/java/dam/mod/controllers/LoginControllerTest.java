package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
 
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
 
@DisplayName("Tests de LoginController")
class LoginControllerTest {
 
    private LoginController controller;
    private IUsuarioService usuarioServiceMock;
 
    // Login
    private TextField     dniInputMock;
    private PasswordField passwordInputMock;
    private CheckBox      rememberSessionCheckboxMock;
    private Label         loginErrorLabelMock;
 
    // Register
    private TextField     nombreInputMock;
    private TextField     dniRegisterInputMock;
    private TextField     emailInputMock;
    private TextField     telefonoInputMock;
    private PasswordField registerPasswordInputMock;
    private Label         registerErrorLabelMock;
 
    @BeforeAll
    static void initializeJavaFXRuntime() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller          = new LoginController();
        usuarioServiceMock  = mock(IUsuarioService.class);

        injectField("bundle", LanguageManager.getBundle());
 
        dniInputMock                = mock(TextField.class);
        passwordInputMock           = mock(PasswordField.class);
        rememberSessionCheckboxMock = mock(CheckBox.class);
        loginErrorLabelMock         = mock(Label.class);
 
        nombreInputMock           = mock(TextField.class);
        dniRegisterInputMock      = mock(TextField.class);
        emailInputMock            = mock(TextField.class);
        telefonoInputMock         = mock(TextField.class);
        registerPasswordInputMock = mock(PasswordField.class);
        registerErrorLabelMock    = mock(Label.class);
 
        injectField("service",               usuarioServiceMock);
        injectField("dniField",              dniInputMock);
        injectField("passwordField",         passwordInputMock);
        injectField("rememberMeCheckBox",    rememberSessionCheckboxMock);
        injectField("errorLabel",            loginErrorLabelMock);
        injectField("nombreField",           nombreInputMock);
        injectField("dniRegisterField",      dniRegisterInputMock);
        injectField("emailField",            emailInputMock);
        injectField("telefonoField",         telefonoInputMock);
        injectField("registerPasswordField", registerPasswordInputMock);
        injectField("registerErrorLabel",    registerErrorLabelMock);
    }
 
    @Test
    @DisplayName("login con credenciales válidas: guarda sesión y navega a inicio")
    void login_credencialesValidas_sesionYNavega() {
        Usuario user = new Usuario();
        when(dniInputMock.getText()).thenReturn("12345678A");
        when(passwordInputMock.getText()).thenReturn("correcta");
        when(rememberSessionCheckboxMock.isSelected()).thenReturn(false);
        when(usuarioServiceMock.login("12345678A", "correcta", false)).thenReturn(user);
 
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            invokePrivate("handleLogin");
 
            sessionMock.verify(() -> Session.setCurrentUser(user));
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    @Test
    @DisplayName("login con credenciales inválidas: muestra mensaje de error")
    void login_credencialesInvalidas_muestraError() {
        when(dniInputMock.getText()).thenReturn("12345678A");
        when(passwordInputMock.getText()).thenReturn("wrong");
        when(rememberSessionCheckboxMock.isSelected()).thenReturn(false);
        // El servicio lanza excepción cuando las credenciales no son válidas
        when(usuarioServiceMock.login(anyString(), anyString(), anyBoolean()))
                .thenThrow(new RuntimeException("Credenciales incorrectas"));
 
        invokePrivate("handleLogin");
 
        verify(loginErrorLabelMock).setText("Credenciales incorrectas");
    }
 
    @Test
    @DisplayName("login: excepción en el servicio → muestra el mensaje de la excepción")
    void login_excepcionEnServicio_muestraError() {
        when(dniInputMock.getText()).thenReturn("12345678A");
        when(passwordInputMock.getText()).thenReturn("pass");
        when(rememberSessionCheckboxMock.isSelected()).thenReturn(false);
        when(usuarioServiceMock.login(anyString(), anyString(), anyBoolean()))
                .thenThrow(new RuntimeException("BD caída"));
 
        invokePrivate("handleLogin");
 
        verify(loginErrorLabelMock).setText("BD caída");
    }
 
    @Test
    @DisplayName("registro con contraseña corta: muestra error de validación sin crear usuario")
    void register_contrasennaCorta_muestraErrorDeValidacion() {
        when(registerPasswordInputMock.getText()).thenReturn("123");
 
        invokePrivate("handleRegister");
 
        verify(registerErrorLabelMock).setText("Contraseña demasiado corta");
        verify(usuarioServiceMock, never()).create(any());
    }
 
    @Test
    @DisplayName("registro con datos válidos: crea usuario y navega a login")
    void register_datosValidos_creaUsuarioYNavega() {
        when(registerPasswordInputMock.getText()).thenReturn("segura123");
        when(nombreInputMock.getText()).thenReturn("Ana");
        when(dniRegisterInputMock.getText()).thenReturn("98765432B");
        when(emailInputMock.getText()).thenReturn("ana@test.com");
        when(telefonoInputMock.getText()).thenReturn("600000000");
 
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invokePrivate("handleRegister");
 
            verify(usuarioServiceMock).create(any(Usuario.class));
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("registro: excepción en el servicio → muestra el mensaje de la excepción")
    void register_excepcionEnServicio_muestraError() {
        when(registerPasswordInputMock.getText()).thenReturn("segura123");
        when(nombreInputMock.getText()).thenReturn("Ana");
        when(dniRegisterInputMock.getText()).thenReturn("98765432B");
        when(emailInputMock.getText()).thenReturn("ana@test.com");
        when(telefonoInputMock.getText()).thenReturn("600000000");
        doThrow(new RuntimeException("DNI duplicado")).when(usuarioServiceMock).create(any());
 
        invokePrivate("handleRegister");
 
        verify(registerErrorLabelMock).setText("Error al registrar usuario");
    }
 
    @Test
    @DisplayName("abrirRegistro navega a register.fxml")
    void abrirRegistro_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invokePrivate("abrirRegistro");
            screenMock.verify(() -> ScreenManager.change("register.fxml"));
        }
    }
 
    @Test
    @DisplayName("volverLogin navega a login.fxml")
    void volverLogin_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invokePrivate("volverLogin");
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("setSpanish aplica idioma 'es'")
    void setSpanish_aplicaIdioma() {
        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("login.fxml");
 
            invokePrivate("setSpanish");
 
            langMock.verify(() -> LanguageManager.setLanguage("es"));
        }
    }
 
    @Test
    @DisplayName("setEnglish aplica idioma 'en'")
    void setEnglish_aplicaIdioma() {
        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("login.fxml");
 
            invokePrivate("setEnglish");
 
            langMock.verify(() -> LanguageManager.setLanguage("en"));
        }
    }
 
    @Test
    @DisplayName("setGerman aplica idioma 'de'")
    void setGerman_aplicaIdioma() {
        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("login.fxml");
 
            invokePrivate("setGerman");
 
            langMock.verify(() -> LanguageManager.setLanguage("de"));
        }
    }
 
    // --- Helpers ---
 
    private void injectField(String fieldName, Object value) throws Exception {
        Field field = LoginController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }
 
    private void invokePrivate(String methodName) {
        try {
            Method method = LoginController.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}