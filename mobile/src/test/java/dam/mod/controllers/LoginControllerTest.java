package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginController loginControllerUnderTest;

    private IUsuarioService usuarioServiceMock;

    private TextField dniInputMock;
    private PasswordField passwordInputMock;
    private CheckBox rememberSessionCheckboxMock;
    private Label loginErrorLabelMock;

    private TextField nombreInputMock;
    private TextField dniRegisterInputMock;
    private TextField emailInputMock;
    private TextField telefonoInputMock;
    private PasswordField registerPasswordInputMock;
    private Label registerErrorLabelMock;

    @BeforeAll
    static void initializeJavaFXRuntime() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {

        loginControllerUnderTest = new LoginController();
        usuarioServiceMock = mock(IUsuarioService.class);

        dniInputMock = mock(TextField.class);
        passwordInputMock = mock(PasswordField.class);
        rememberSessionCheckboxMock = mock(CheckBox.class);
        loginErrorLabelMock = mock(Label.class);

        nombreInputMock = mock(TextField.class);
        dniRegisterInputMock = mock(TextField.class);
        emailInputMock = mock(TextField.class);
        telefonoInputMock = mock(TextField.class);
        registerPasswordInputMock = mock(PasswordField.class);
        registerErrorLabelMock = mock(Label.class);

        injectField("service", usuarioServiceMock);

        injectField("dniField", dniInputMock);
        injectField("passwordField", passwordInputMock);
        injectField("rememberMeCheckBox", rememberSessionCheckboxMock);
        injectField("errorLabel", loginErrorLabelMock);

        injectField("nombreField", nombreInputMock);
        injectField("dniRegisterField", dniRegisterInputMock);
        injectField("emailField", emailInputMock);
        injectField("telefonoField", telefonoInputMock);
        injectField("registerPasswordField", registerPasswordInputMock);
        injectField("registerErrorLabel", registerErrorLabelMock);
    }

    @Test
    void login_withValidCredentials_setsSessionAndNavigates() {

        Usuario user = new Usuario();

        when(dniInputMock.getText()).thenReturn("12345678A");
        when(passwordInputMock.getText()).thenReturn("correcta");
        when(rememberSessionCheckboxMock.isSelected()).thenReturn(false);

        when(usuarioServiceMock.login("12345678A", "correcta", false)).thenReturn(user);

        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invokePrivate("handleLogin");

            sessionMock.verify(() -> Session.setCurrentUser(user));
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }

    @Test
    void login_withInvalidCredentials_showsErrorMessage() {

        when(dniInputMock.getText()).thenReturn("12345678A");
        when(passwordInputMock.getText()).thenReturn("wrong");
        when(rememberSessionCheckboxMock.isSelected()).thenReturn(false);

        when(usuarioServiceMock.login(anyString(), anyString(), anyBoolean())).thenReturn(null);

        invokePrivate("handleLogin");

        verify(loginErrorLabelMock).setText(anyString());
    }

    @Test
    void login_whenServiceThrows_exceptionIsShown() {

        when(dniInputMock.getText()).thenReturn("12345678A");
        when(passwordInputMock.getText()).thenReturn("pass");
        when(rememberSessionCheckboxMock.isSelected()).thenReturn(false);

        when(usuarioServiceMock.login(anyString(), anyString(), anyBoolean()))
                .thenThrow(new RuntimeException("BD caída"));

        invokePrivate("handleLogin");

        verify(loginErrorLabelMock).setText("BD caída");
    }

    @Test
    void register_shortPassword_showsValidationError() {

        when(registerPasswordInputMock.getText()).thenReturn("123");

        invokePrivate("handleRegister");

        verify(registerErrorLabelMock).setText("Contraseña demasiado corta");
        verify(usuarioServiceMock, never()).create(any());
    }

    @Test
    void register_validData_createsUserAndNavigates() {

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
    void register_whenServiceThrows_showsError() {

        when(registerPasswordInputMock.getText()).thenReturn("segura123");
        when(nombreInputMock.getText()).thenReturn("Ana");
        when(dniRegisterInputMock.getText()).thenReturn("98765432B");
        when(emailInputMock.getText()).thenReturn("ana@test.com");
        when(telefonoInputMock.getText()).thenReturn("600000000");

        doThrow(new RuntimeException("DNI duplicado")).when(usuarioServiceMock).create(any());

        invokePrivate("handleRegister");

        verify(registerErrorLabelMock).setText("DNI duplicado");
    }

    @Test
    void openRegister_navigatesToRegisterScreen() {

        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invokePrivate("abrirRegistro");

            screenMock.verify(() -> ScreenManager.change("register.fxml"));
        }
    }

    @Test
    void backToLogin_navigatesToLoginScreen() {

        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invokePrivate("volverLogin");

            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void setSpanish_changesLanguage() {

        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("login.fxml");

            invokePrivate("setSpanish");

            langMock.verify(() -> LanguageManager.setLanguage("es"));
        }
    }

    @Test
    void setEnglish_changesLanguage() {

        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("login.fxml");

            invokePrivate("setEnglish");

            langMock.verify(() -> LanguageManager.setLanguage("en"));
        }
    }

    @Test
    void setGerman_changesLanguage() {

        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("login.fxml");

            invokePrivate("setGerman");

            langMock.verify(() -> LanguageManager.setLanguage("de"));
        }
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = LoginController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(loginControllerUnderTest, value);
    }

    private void invokePrivate(String methodName) {
        try {
            Method method = LoginController.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(loginControllerUnderTest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}