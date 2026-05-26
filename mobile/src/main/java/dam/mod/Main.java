package dam.mod;

import dam.mod.utils.ScreenManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        ScreenManager.init(stage);


        ScreenManager.change("login.fxml");

        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/icons/app.png"))
        );

        stage.setTitle("CentroPlus-Connect");
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}