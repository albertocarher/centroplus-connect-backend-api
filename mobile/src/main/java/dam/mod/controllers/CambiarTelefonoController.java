package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import dam.mod.utils.Validaciones;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

/**
 * Controlador para cambiar el teléfono del usuario.
 */
public class CambiarTelefonoController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private TextField telefonoField;

    @FXML
    private TextField repeatTelefonoField;

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

        telefonoField.clear();
        repeatTelefonoField.clear();
    }

    @FXML
    private void cambiarTelefono() {

        Usuario user = Session.getCurrentUser();

        String tel = telefonoField.getText();
        String repeat = repeatTelefonoField.getText();

        if (tel == null || tel.isBlank()) {
            mensajeLabel.setText(bundle.getString("error.phone.empty"));
            return;
        }

        if (!tel.equals(repeat)) {
            mensajeLabel.setText(bundle.getString("error.phone.mismatch"));
            return;
        }

        try {
            Validaciones.validarTelefono(tel);

            user.setTelefono(tel);
            usuarioService.update(user);

            Session.setCurrentUser(user);
            ScreenManager.change("perfil.fxml");

        } catch (IllegalArgumentException e) {
            mensajeLabel.setText(bundle.getString("error.phone.invalid"));
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("perfil.fxml");
    }
}