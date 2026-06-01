package dam.mod.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageManagerTest {


    // @Test
    // void idiomaPorDefectoEsEspanol() {
    //     assertEquals("es", LanguageManager.getLanguage());
    // }

    @Test
    void setLanguageCambiaIdiomaAIngles() {
        LanguageManager.setLanguage("en");

        assertEquals("en", LanguageManager.getLanguage());
    }

    @Test
    void setLanguageCambiaIdiomaAAleman() {
        LanguageManager.setLanguage("de");

        assertEquals("de", LanguageManager.getLanguage());
    }

    @Test
    void setLanguagePermiteVolverAEspanol() {
        LanguageManager.setLanguage("es");

        assertEquals("es", LanguageManager.getLanguage());
    }

    @Test
    void setLanguageSobrescribeIdiomaAnterior() {
        LanguageManager.setLanguage("en");
        LanguageManager.setLanguage("de");

        assertEquals("de", LanguageManager.getLanguage());
    }

    @Test
    void setLanguageAceptaValoresPersonalizados() {
        LanguageManager.setLanguage("fr");

        assertEquals("fr", LanguageManager.getLanguage());
    }

    @Test
    void setLanguageAceptaNull() {
        LanguageManager.setLanguage(null);

        assertNull(LanguageManager.getLanguage());
    }
}