package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import dam.mod.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

/**
 * Controlador encargado del cambio de contraseña del usuario.
 *
 * Permite validar la contraseña actual, comprobar la nueva contraseña
 * y actualizarla en el sistema de forma segura.
 */
public class CambiarPasswordController {

    @FXML
    private Label mensajeLabel;
    /**
     * Campo de contraseña actual.
     */
    @FXML
    private PasswordField oldPasswordField;

    /**
     * Campo de nueva contraseña.
     */
    @FXML
    private PasswordField newPasswordField;

    /**
     * Campo de repetición de nueva contraseña.
     */
    @FXML
    private PasswordField repeatPasswordField;

    private IUsuarioService usuarioService;

    /**
     * Inicializa el controlador.
     *
     * Verifica que el usuario esté autenticado e inicializa el servicio de
     * usuarios.
     */
    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        IUsuarioRepository repo = new UsuarioRepository();
        usuarioService = new UsuarioServiceImpl(repo, new RememberTokenRepositoryImpl());
    }

    /**
     * Cambia la contraseña del usuario actual.
     *
     * Valida la contraseña actual, comprueba coincidencia de la nueva contraseña
     * y actualiza el usuario en el sistema.
     */
    @FXML
    private void cambiarPassword() {

        Usuario user = Session.getCurrentUser();

        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String repeat = repeatPasswordField.getText();

        if (!PasswordUtils.checkPassword(oldPass, user.getPassword())) {
            mensajeLabel.setText("Contraseña actual incorrecta");
            return;
        }

        if (!newPass.equals(repeat)) {
            mensajeLabel.setText("Las contraseñas no coinciden");
            return;
        }

        if (newPass.length() < 6) {
            mensajeLabel.setText("Contraseña demasiado corta");
            return;
        }

        user.setPassword(newPass);

        boolean ok = usuarioService.update(user);

        if (ok) {
            mensajeLabel.setText("Contraseña actualizada correctamente");
            Session.setCurrentUser(user);
            ScreenManager.change("perfil.fxml");
        } else {
            mensajeLabel.setText("Error al actualizar");
        }
    }

    /**
     * Vuelve a la pantalla de perfil.
     */
    @FXML
    private void volver() {
        ScreenManager.change("perfil.fxml");
    }
}