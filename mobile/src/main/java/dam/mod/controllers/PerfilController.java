package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de perfil.
 */
public class PerfilController {

    private IUsuarioService service;

    public void setService(IUsuarioService service) {
        this.service = service;
    }

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblDni;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblTelefono;

    @FXML
    private Label lblTipo;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        bundle = LanguageManager.getBundle();

        Usuario usuario = Session.getCurrentUser();

        if (usuario == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        cargarDatos(usuario);
    }

    private void cargarDatos(Usuario usuario) {

        lblNombre.setText(usuario.getNombre());
        lblDni.setText(usuario.getDni());
        lblEmail.setText(usuario.getEmail());
        lblTelefono.setText(usuario.getTelefono());
        lblTipo.setText(usuario.getTipoUsuario());
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }

    @FXML
    private void cerrarSesion() {
        if (service != null) {
            service.logout();
        }
        ScreenManager.change("login.fxml");
    }

    @FXML
    private void abrirCambiarPassword() {
        ScreenManager.change("cambiar_password.fxml");
    }

    @FXML
    private void abrirCambiarEmail() {
        ScreenManager.change("cambiar_email.fxml");
    }

    @FXML
    private void abrirCambiarTelefono() {
        ScreenManager.change("cambiar_telefono.fxml");
    }
}