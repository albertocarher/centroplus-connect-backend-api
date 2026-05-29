package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador de la pantalla de inicio de sesión y registro.
 *
 * Gestiona la autenticación de usuarios, registro de nuevos usuarios
 * y navegación entre pantallas de login y registro.
 */
public class LoginController {

    /**
     * Servicio de usuarios para autenticación y gestión.
     */
    private final IUsuarioService service = new UsuarioServiceImpl(new UsuarioRepository());

    /**
     * Campo de entrada para el DNI en login.
     */
    @FXML
    private TextField dniField;

    /**
     * Campo de entrada para la contraseña en login.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Etiqueta para mostrar errores de login.
     */
    @FXML
    private Label errorLabel;

    /**
     * Campo de nombre en registro.
     */
    @FXML
    private TextField nombreField;

    /**
     * Campo de DNI en registro.
     */
    @FXML
    private TextField dniRegisterField;

    /**
     * Campo de email en registro.
     */
    @FXML
    private TextField emailField;

    /**
     * Campo de teléfono en registro.
     */
    @FXML
    private TextField telefonoField;

    /**
     * Campo de contraseña en registro.
     */
    @FXML
    private PasswordField registerPasswordField;

    /**
     * Selector de tipo de usuario en registro.
     */
    @FXML
    private ComboBox<String> tipoUsuarioBox;

    /**
     * Etiqueta para mostrar errores en registro.
     */
    @FXML
    private Label registerErrorLabel;

    /**
     * Inicializa el controlador.
     *
     * Carga los tipos de usuario disponibles en el ComboBox.
     */
    @FXML
    public void initialize() {

        if (tipoUsuarioBox != null) {
            tipoUsuarioBox.getItems().addAll(
                    "ALUMNO",
                    "SOCIO",
                    "AMBOS");
        }
    }

    /**
     * Gestiona el inicio de sesión del usuario.
     *
     * Valida credenciales, normaliza el DNI y establece la sesión si es correcto.
     */
    @FXML
    private void handleLogin() {

        try {
            Usuario usuario = service.login(
                    dniField.getText().trim().toUpperCase(),
                    passwordField.getText());

            if (usuario == null) {
                errorLabel.setText("Usuario o contraseña incorrectos");
                return;
            }

            Session.setCurrentUser(usuario);
            ScreenManager.change("inicio.fxml");

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    /**
     * Gestiona el registro de un nuevo usuario.
     *
     * Valida la contraseña, normaliza el DNI y crea el usuario en el sistema.
     */
    @FXML
    private void handleRegister() {

        try {

            if (registerPasswordField.getText() == null ||
                    registerPasswordField.getText().length() < 6) {
                registerErrorLabel.setText("Contraseña demasiado corta");
                return;
            }

            Usuario nuevo = new Usuario(
                    0,
                    nombreField.getText(),
                    dniRegisterField.getText().trim().toUpperCase(),
                    emailField.getText(),
                    telefonoField.getText(),
                    tipoUsuarioBox.getValue(),
                    registerPasswordField.getText());

            service.create(nuevo);

            ScreenManager.change("login.fxml");

        } catch (Exception e) {
            registerErrorLabel.setText(e.getMessage());
        }
    }

    /**
     * Abre la pantalla de registro.
     */
    @FXML
    private void abrirRegistro() {
        ScreenManager.change("register.fxml");
    }

    /**
     * Vuelve a la pantalla de login.
     */
    @FXML
    private void volverLogin() {
        ScreenManager.change("login.fxml");
    }

    /**
     * Cambia el idioma a español.
     */
    @FXML
    private void setSpanish() {
        aplicarIdioma("es");
    }

    /**
     * Cambia el idioma a inglés.
     */
    @FXML
    private void setEnglish() {
        aplicarIdioma("en");
    }

    /**
     * Cambia el idioma a alemán.
     */
    @FXML
    private void setGerman() {
        aplicarIdioma("de");
    }

    /**
     * Aplica el idioma seleccionado.
     *
     * @param lang código del idioma
     */
    private void aplicarIdioma(String lang) {

        switch (lang) {
            case "es" -> LanguageManager.setLanguage("es");
            case "en" -> LanguageManager.setLanguage("en");
            case "de" -> LanguageManager.setLanguage("de");
            default -> LanguageManager.setLanguage("es");
        }

        ScreenManager.change(ScreenManager.getCurrentScreen());
    }
}