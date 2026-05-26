package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class CambiarPasswordController {

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField repeatPasswordField;

    private IUsuarioService usuarioService;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        IUsuarioRepository repo = new UsuarioRepository();
        usuarioService = new UsuarioServiceImpl(repo);
    }

    @FXML
    private void cambiarPassword() {

        Usuario user = Session.getCurrentUser();

        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String repeat = repeatPasswordField.getText();

        if (!user.getPassword().equals(oldPass)) {
            System.out.println("Contraseña actual incorrecta");
            return;
        }

        if (!newPass.equals(repeat)) {
            System.out.println("Las contraseñas no coinciden");
            return;
        }

        if (newPass.length() < 6) {
            System.out.println("Contraseña demasiado corta");
            return;
        }

        user.setPassword(newPass);

        boolean ok = usuarioService.update(user);

        if (ok) {
            System.out.println("Contraseña actualizada correctamente");
            Session.setCurrentUser(user);
            ScreenManager.change("perfil.fxml");
        } else {
            System.out.println("Error al actualizar");
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("perfil.fxml");
    }
}