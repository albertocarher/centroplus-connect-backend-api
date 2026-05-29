package dam.mod.utils;

/**
 * Gestiona el idioma actual de la aplicación.
 *
 * Se usa para determinar desde qué carpeta de vistas
 * se cargan los FXML (/es, /en, /de).
 */
public class LanguageManager {

    private static String language = "es";

    /**
     * Cambia el idioma actual.
     *
     * @param lang código del idioma (es, en, de)
     */
    public static void setLanguage(String lang) {
        language = lang;
    }

    /**
     * Obtiene el idioma actual.
     *
     * @return idioma actual
     */
    public static String getLanguage() {
        return language;
    }
}