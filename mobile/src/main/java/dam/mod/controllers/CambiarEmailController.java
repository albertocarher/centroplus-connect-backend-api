package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import dam.mod.utils.Validaciones;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controlador para cambiar el email del usuario.
 */
public class CambiarEmailController {

    @FXML
    private Label mensajeLabel;
    @FXML
    private TextField emailField;

    @FXML
    private TextField repeatEmailField;

    private IUsuarioService usuarioService;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        IUsuarioRepository repo = new UsuarioRepository();
        usuarioService = new UsuarioServiceImpl(repo, new RememberTokenRepositoryImpl());

        emailField.clear();
        repeatEmailField.clear();
    }

    @FXML
    private void cambiarEmail() {

        Usuario user = Session.getCurrentUser();

        String email = emailField.getText();
        String repeat = repeatEmailField.getText();

        if (email == null || email.isBlank() || repeat == null || repeat.isBlank()) {
            mensajeLabel.setText("Debes rellenar ambos campos");
            return;
        }

        if (!email.equals(repeat)) {
            mensajeLabel.setText("Los emails no coinciden");
            return;
        }

        try {
            Validaciones.validarEmail(email);

            user.setEmail(email);
            usuarioService.update(user);

            Session.setCurrentUser(user);
            ScreenManager.change("perfil.fxml");

        } catch (IllegalArgumentException e) {
            mensajeLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("perfil.fxml");
    }
}