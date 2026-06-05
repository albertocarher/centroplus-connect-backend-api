package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.util.ResourceBundle;

/**
 * Controlador encargado del cambio de contraseña del usuario.
 */
public class CambiarPasswordController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField repeatPasswordField;

    private IUsuarioService usuarioService;
    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        bundle = LanguageManager.getBundle();

        IUsuarioRepository repo = new UsuarioRepository();
        usuarioService = new UsuarioServiceImpl(repo, new RememberTokenRepositoryImpl());
    }

    @FXML
    private void cambiarPassword() {

        Usuario user = Session.getCurrentUser();

        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String repeat = repeatPasswordField.getText();

        if (!PasswordUtils.checkPassword(oldPass, user.getPassword())) {
            mensajeLabel.setText(bundle.getString("error.password.current"));
            return;
        }

        if (!newPass.equals(repeat)) {
            mensajeLabel.setText(bundle.getString("error.password.mismatch"));
            return;
        }

        if (newPass.length() < 6) {
            mensajeLabel.setText(bundle.getString("error.password.short"));
            return;
        }

        user.setPassword(PasswordUtils.hashPassword(newPass));

        boolean ok = usuarioService.update(user);

        if (ok) {
            mensajeLabel.setText(bundle.getString("success.password.updated"));
            Session.setCurrentUser(user);
            ScreenManager.change("perfil.fxml");
        } else {
            mensajeLabel.setText(bundle.getString("error.update"));
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("perfil.fxml");
    }
}