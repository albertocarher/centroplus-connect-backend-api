package dam.mod.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;

class LanguageManagerTest {

    @BeforeEach
    void setup() {
        LanguageManager.reset();
    }

    @Test
    @Order(1)
    void idiomaPorDefectoEsEspanol() {
        assertEquals("es", LanguageManager.getLanguage());
    }

    @Test
    @Order(2)
    void setLanguageCambiaIdiomaAIngles() {
        LanguageManager.setLanguage("en");

        assertEquals("en", LanguageManager.getLanguage());
    }

    @Test
    @Order(3)
    void setLanguageCambiaIdiomaAAleman() {
        LanguageManager.setLanguage("de");

        assertEquals("de", LanguageManager.getLanguage());
    }

    @Test
    @Order(4)
    void setLanguagePermiteVolverAEspanol() {
        LanguageManager.setLanguage("es");

        assertEquals("es", LanguageManager.getLanguage());
    }

    @Test
    @Order(5)
    void setLanguageSobrescribeIdiomaAnterior() {
        LanguageManager.setLanguage("en");
        LanguageManager.setLanguage("de");

        assertEquals("de", LanguageManager.getLanguage());
    }

    @Test
    @Order(6)
    void setLanguageAceptaValoresPersonalizados() {
        LanguageManager.setLanguage("fr");

        assertEquals("fr", LanguageManager.getLanguage());
    }

    @Test
    @Order(7)
    void setLanguageAceptaNull() {
        LanguageManager.setLanguage(null);

        assertNull(LanguageManager.getLanguage());
    }
}