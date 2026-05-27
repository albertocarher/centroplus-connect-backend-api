package dam.mod.controllers;

import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;

public class InicioController {

    //inicializacion
    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
        }
    }

    //abrir actividades
    @FXML
    private void abrirActividades() {
        ScreenManager.change("actividades.fxml");
    }

    //abrir reservas
    @FXML
    private void abrirReservas() {
        ScreenManager.change("reservas.fxml");
    }

    //abrir incidencias
    @FXML
    private void abrirIncidencias() {
        ScreenManager.change("incidencias.fxml");
    }

    //abrir perfil
    @FXML
    private void abrirPerfil() {
        ScreenManager.change("perfil.fxml");
    }

    //cambiar idioma
    @FXML
    private void cambiarIdioma() {
        System.out.println("Selector de idioma (pendiente de implementar)");
    }
}