package dam.mod.controllers;

import dam.mod.utils.ScreenManager;
import javafx.fxml.FXML;

public class PerfilController {

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }

    @FXML
    private void cerrarSesion() {
        // por ahora simple (luego puedes limpiar sesión real)
        System.out.println("Sesión cerrada");
        ScreenManager.change("inicio.fxml");
    }
}