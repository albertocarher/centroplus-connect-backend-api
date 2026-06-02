package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para LoginController.
 *
 * Escenarios cubiertos:
 * - handleLogin() con credenciales incorrectas → muestra error.
 * - handleLogin() con credenciales correctas → establece sesión y navega.
 * - handleLogin() con excepción → muestra mensaje de error.
 * - handleRegister() con contraseña corta → muestra error.
 * - handleRegister() con datos válidos → crea usuario y navega al login.
 * - handleRegister() con excepción → muestra error.
 * - abrirRegistro() y volverLogin() navegan correctamente.
 * - Cambio de idioma aplica el LanguageManager correcto.
 */
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    private LoginController controller;

    private IUsuarioService service = mock(IUsuarioService.class);

    // Campos de login
    private TextField dniField = mock(TextField.class);
    private PasswordField passwordField = mock(PasswordField.class);
    private Label errorLabel = mock(Label.class);

    // Campos de registro
    private TextField nombreField = mock(TextField.class);
    private TextField dniRegisterField = mock(TextField.class);
    private TextField emailField = mock(TextField.class);
    private TextField telefonoField = mock(TextField.class);
    private PasswordField registerPasswordField = mock(PasswordField.class);

    @SuppressWarnings("unchecked")
    private ComboBox<String> tipoUsuarioBox = mock(ComboBox.class);

    private Label registerErrorLabel = mock(Label.class);

    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {

        controller = new LoginController();

        setField("service", service);

        setField("dniField", dniField);
        setField("passwordField", passwordField);
        setField("errorLabel", errorLabel);

        setField("nombreField", nombreField);
        setField("dniRegisterField", dniRegisterField);
        setField("emailField", emailField);
        setField("telefonoField", telefonoField);
        setField("registerPasswordField", registerPasswordField);
        setField("tipoUsuarioBox", tipoUsuarioBox);
        setField("registerErrorLabel", registerErrorLabel);
    }

    @Test
    void handleLogin_credencialesIncorrectas_muestraError() {

        when(dniField.getText()).thenReturn("12345678A");
        when(passwordField.getText()).thenReturn("mala");

        when(service.login("12345678A", "mala")).thenReturn(null);

        invoke("handleLogin");

        verify(errorLabel).setText("Usuario o contraseña incorrectos");
    }

    @Test
    void handleLogin_credencialesCorrectas_estableceSesionYNavega() {

        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            Usuario user = new Usuario();

            when(dniField.getText()).thenReturn("12345678A");
            when(passwordField.getText()).thenReturn("correcta");

            when(service.login("12345678A", "correcta"))
                    .thenReturn(user);

            invoke("handleLogin");

            sessionMock.verify(() -> Session.setCurrentUser(user));

            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }

    @Test
    void handleLogin_excepcion_muestraError() {

        when(dniField.getText()).thenReturn("12345678A");
        when(passwordField.getText()).thenReturn("pass");

        when(service.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("BD caída"));

        invoke("handleLogin");

        verify(errorLabel).setText("BD caída");
    }

    @Test
    void handleRegister_contrasenaCorta_muestraError() {

        when(registerPasswordField.getText()).thenReturn("abc");

        invoke("handleRegister");

        verify(registerErrorLabel)
                .setText("Contraseña demasiado corta");

        verify(service, never()).create(any());
    }

    @Test
    void handleRegister_datosValidos_creaYNavega() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            when(registerPasswordField.getText())
                    .thenReturn("segura123");

            when(nombreField.getText()).thenReturn("Ana");
            when(dniRegisterField.getText()).thenReturn("98765432B");
            when(emailField.getText()).thenReturn("ana@test.com");
            when(telefonoField.getText()).thenReturn("600000000");
            when(tipoUsuarioBox.getValue()).thenReturn("SOCIO");

            invoke("handleRegister");

            verify(service).create(any(Usuario.class));

            screenMock.verify(() ->
                    ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void handleRegister_excepcion_muestraError() {

        when(registerPasswordField.getText())
                .thenReturn("segura123");

        when(nombreField.getText()).thenReturn("Ana");
        when(dniRegisterField.getText()).thenReturn("98765432B");
        when(emailField.getText()).thenReturn("ana@test.com");
        when(telefonoField.getText()).thenReturn("600000000");
        when(tipoUsuarioBox.getValue()).thenReturn("SOCIO");

        doThrow(new RuntimeException("DNI duplicado"))
                .when(service)
                .create(any());

        invoke("handleRegister");

        verify(registerErrorLabel).setText("DNI duplicado");
    }

    @Test
    void abrirRegistro_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("abrirRegistro");

            screenMock.verify(() ->
                    ScreenManager.change("register.fxml"));
        }
    }

    @Test
    void volverLogin_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("volverLogin");

            screenMock.verify(() ->
                    ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void setSpanish_aplicaIdioma() {

        try (MockedStatic<LanguageManager> langMock =
                     mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen)
                    .thenReturn("login.fxml");

            invoke("setSpanish");

            langMock.verify(() ->
                    LanguageManager.setLanguage("es"));
        }
    }

    @Test
    void setEnglish_aplicaIdioma() {

        try (MockedStatic<LanguageManager> langMock =
                     mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen)
                    .thenReturn("login.fxml");

            invoke("setEnglish");

            langMock.verify(() ->
                    LanguageManager.setLanguage("en"));
        }
    }

    @Test
    void setGerman_aplicaIdioma() {

        try (MockedStatic<LanguageManager> langMock =
                     mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen)
                    .thenReturn("login.fxml");

            invoke("setGerman");

            langMock.verify(() ->
                    LanguageManager.setLanguage("de"));
        }
    }

    private void setField(String name, Object value) throws Exception {

        Field f = LoginController.class.getDeclaredField(name);

        f.setAccessible(true);

        f.set(controller, value);
    }

    private void invoke(String methodName) {

        try {

            Method m = LoginController.class
                    .getDeclaredMethod(methodName);

            m.setAccessible(true);

            m.invoke(controller);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}