package dam.mod.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ScreenManager {

    private static Stage stage;

    public static void init(Stage s) {
        stage = s;
    }

    public static void change(String fxml) {

        try {
            URL url = ScreenManager.class.getResource("/dam/mod/views/" + fxml);

            if (url == null) {
                throw new RuntimeException("FXML no encontrado: " + fxml);
            }

            Parent root = FXMLLoader.load(url);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    ScreenManager.class.getResource("/dam/mod/views/style.css").toExternalForm()
            );
            stage.setScene(scene);

            stage.setScene(scene);
            stage.setTitle(stage.getTitle());
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("Error cargando FXML: " + fxml, e);
        }
    }
}