package dam.mod;

import javafx.application.Platform;

public class JavaFXInitializer {

    private static boolean initialized = false;

    public static void init() {

        if (initialized) {
            return;
        }

        try {

            Platform.startup(() -> {});

            initialized = true;

        } catch (IllegalStateException e) {

            initialized = true;
        }
    }
}