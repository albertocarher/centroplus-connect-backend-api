package dam.mod.controllers;

import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;

public class InicioController {

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
        }
    }

    @FXML
    private void abrirActividades() {
        ScreenManager.change("actividades.fxml");
    }

    @FXML
    private void abrirReservas() {
        ScreenManager.change("reservas.fxml");
    }

    @FXML
    private void abrirIncidencias() {
        ScreenManager.change("incidencias.fxml");
    }

    @FXML
    private void abrirPerfil() {
        ScreenManager.change("perfil.fxml");
    }

    @FXML
    private void cambiarIdioma() {
        System.out.println("🌐 Selector de idioma (pendiente de implementar)");
    }
}