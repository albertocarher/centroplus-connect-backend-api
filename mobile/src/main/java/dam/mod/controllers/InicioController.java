package dam.mod.controllers;

import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;

/**
 * Controlador de la pantalla principal del sistema.
 *
 * Gestiona la navegación hacia las distintas secciones de la aplicación
 * y controla el acceso según la sesión del usuario.
 */
public class InicioController {

    /**
     * Inicializa la pantalla principal.
     *
     * Verifica si existe un usuario en sesión, en caso contrario
     * redirige al login.
     */
    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
        }
    }

    /**
     * Abre la pantalla de actividades.
     */
    @FXML
    private void abrirActividades() {
        ScreenManager.change("actividades.fxml");
    }

    /**
     * Abre la pantalla de reservas.
     */
    @FXML
    private void abrirReservas() {
        ScreenManager.change("reservas.fxml");
    }

    /**
     * Abre la pantalla de incidencias.
     */
    @FXML
    private void abrirIncidencias() {
        ScreenManager.change("incidencias.fxml");
    }

    /**
     * Abre la pantalla de perfil del usuario.
     */
    @FXML
    private void abrirPerfil() {
        ScreenManager.change("perfil.fxml");
    }

    /**
     * Cambia el idioma a español.
     */
    @FXML
    private void setSpanish() {
        aplicarIdioma("es");
    }

    /**
     * Cambia el idioma a inglés.
     */
    @FXML
    private void setEnglish() {
        aplicarIdioma("en");
    }

    /**
     * Cambia el idioma a alemán.
     */
    @FXML
    private void setGerman() {
        aplicarIdioma("de");
    }

    /**
     * Aplica el idioma seleccionado.
     *
     * @param lang código del idioma
     */
    private void aplicarIdioma(String lang) {

        switch (lang) {
            case "es" -> LanguageManager.setLanguage("es");
            case "en" -> LanguageManager.setLanguage("en");
            case "de" -> LanguageManager.setLanguage("de");
            default -> LanguageManager.setLanguage("es");
        }

        ScreenManager.change(ScreenManager.getCurrentScreen());
    }
}