package dam.mod.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private static Locale locale = new Locale("es");

    public static void setLanguage(String lang) {
        locale = new Locale(lang);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("i18n.messages", locale);
    }

    public static void reset() {
        locale = new Locale("es");
    }
}