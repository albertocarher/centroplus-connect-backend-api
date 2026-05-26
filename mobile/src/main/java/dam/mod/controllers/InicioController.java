package dam.mod.controllers;

import dam.mod.utils.ScreenManager;
import javafx.fxml.FXML;

public class InicioController {

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