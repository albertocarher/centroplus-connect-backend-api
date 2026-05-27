package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    // SERVICE
    private final IUsuarioService service =
            new UsuarioServiceImpl(new UsuarioRepository());

    // LOGIN
    @FXML
    private TextField dniField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    // REGISTER
    @FXML
    private TextField nombreField;

    @FXML
    private TextField dniRegisterField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telefonoField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private ComboBox<String> tipoUsuarioBox;

    @FXML
    private Label registerErrorLabel;

    //inicializacion
    @FXML
    public void initialize() {

        if (tipoUsuarioBox != null) {
            tipoUsuarioBox.getItems().addAll(
                    "ALUMNO",
                    "SOCIO",
                    "AMBOS"
            );
        }
    }


    //manejo del login
    @FXML
    private void handleLogin() {

        try {
            Usuario u = service.login(
                    dniField.getText(),
                    passwordField.getText()
            );

            Session.setCurrentUser(u);

            ScreenManager.change("inicio.fxml");

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    // REGISTER
    @FXML
    private void handleRegister() {

        try {

            Usuario nuevo = new Usuario(
                    0,
                    nombreField.getText(),
                    dniRegisterField.getText(),
                    emailField.getText(),
                    telefonoField.getText(),
                    tipoUsuarioBox.getValue(),
                    registerPasswordField.getText()
            );

            if (registerPasswordField.getText().length() < 6) {
            System.out.println("Contraseña demasiado corta");
            return;
        }

            service.create(nuevo);

            ScreenManager.change("login.fxml");

        } catch (Exception e) {
            registerErrorLabel.setText(e.getMessage());
        }
    }

    //abrir registro
    @FXML
    private void abrirRegistro() {
        ScreenManager.change("register.fxml");
    }

    //volver al login
    @FXML
    private void volverLogin() {
        ScreenManager.change("login.fxml");
    }
}