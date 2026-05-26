package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PerfilController {

    @FXML private Label lblNombre;
    @FXML private Label lblDni;
    @FXML private Label lblEmail;
    @FXML private Label lblTelefono;
    @FXML private Label lblTipo;

    @FXML
    public void initialize() {

        Usuario u = Session.getCurrentUser();

        if (u == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        lblNombre.setText(u.getNombre());
        lblDni.setText(u.getDni());
        lblEmail.setText(u.getEmail());
        lblTelefono.setText(u.getTelefono());
        lblTipo.setText(u.getTipoUsuario());
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }

    @FXML
    private void cerrarSesion() {
        Session.logout();
        ScreenManager.change("login.fxml");
    }

    @FXML
    private void abrirCambiarPassword() {
        ScreenManager.change("cambiar_password.fxml");
    }
}