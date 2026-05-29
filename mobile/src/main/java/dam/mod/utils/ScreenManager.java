package dam.mod.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ScreenManager {

    /**
     * Guarda la ventana principal de la app
     */
    private static Stage stage;

    /**
     * Guarda el Stage principal
     * 
     * @param s la interfaz
     */
    public static void init(Stage s) {
        stage = s;
    }

    /**
     * Guarda un ID global de incidencia
     */
    private static int incidenciaId;

    /**
     * Setter del id de incidencia
     * 
     * @param id el id de la incidencia
     */
    public static void setIncidenciaId(int id) {
        incidenciaId = id;
    }

    /**
     * Getter del id la incidencia
     * 
     * @return el id de la incidencia
     */
    public static int getIncidenciaId() {
        return incidenciaId;
    }

    /**
     * Cambia la pantalla actual
     * 
     * @param fxml la pantalla
     */
    public static void change(String fxml) {
        currentScreen = fxml;
        try {
            /**
             * Rutas de las pantallas
             */
            URL url = ScreenManager.class.getResource(
                    "/dam/mod/views/" + LanguageManager.getLanguage() + "/" + fxml);

            if (url == null) {
                throw new RuntimeException("FXML no encontrado: " + fxml);
            }

            /**
             * Convierte el FXML en una interfaz real
             */
            Parent root = FXMLLoader.load(url);

            /**
             * Crea la escena
             */
            Scene scene = new Scene(root);

            /**
             * Estilos para las escenas
             */
            URL css = ScreenManager.class.getResource("/dam/mod/views/style.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            /**
             * Sustituye la pantalla anterior y muestra la nueva
             */
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("Error cargando FXML: " + fxml, e);
        }
    }

    /**
     * Pantalla actualmente cargada en la aplicación.
     */
    private static String currentScreen;

    /**
     * Establece la pantalla actual que está siendo mostrada.
     *
     * @param fxml nombre del archivo FXML de la pantalla actual
     */
    public static void setCurrentScreen(String fxml) {
        currentScreen = fxml;
    }

    /**
     * Obtiene la pantalla actualmente cargada en la aplicación.
     *
     * @return nombre del archivo FXML de la pantalla actual
     */
    public static String getCurrentScreen() {
        return currentScreen;
    }
}