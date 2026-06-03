package dam.mod.utils;
 
import dam.mod.JavaFXInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;
 
@DisplayName("Tests de ScreenManager")
class ScreenManagerTest {
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() {
        ScreenManager.setCurrentScreen(null);
        ScreenManager.setIncidenciaId(0);
    }
 
    @Test
    @DisplayName("getCurrentScreen devuelve null si no se ha asignado ninguna pantalla")
    void getCurrentScreen_sinAsignar_null() {
        assertNull(ScreenManager.getCurrentScreen());
    }
 
    @Test
    @DisplayName("setCurrentScreen guarda la pantalla y getCurrentScreen la devuelve")
    void setCurrentScreen_guardaYRecupera() {
        ScreenManager.setCurrentScreen("login.fxml");
        assertEquals("login.fxml", ScreenManager.getCurrentScreen());
    }
 
    @Test
    @DisplayName("setCurrentScreen sobreescribe el valor anterior")
    void setCurrentScreen_sobreescribe() {
        ScreenManager.setCurrentScreen("login.fxml");
        ScreenManager.setCurrentScreen("perfil.fxml");
        assertEquals("perfil.fxml", ScreenManager.getCurrentScreen());
    }
 
    @Test
    @DisplayName("setCurrentScreen acepta null sin lanzar excepción")
    void setCurrentScreen_null_noLanzaExcepcion() {
        assertDoesNotThrow(() -> ScreenManager.setCurrentScreen(null));
    }
 
    @Test
    @DisplayName("getIncidenciaId devuelve 0 por defecto")
    void getIncidenciaId_porDefecto_cero() {
        assertEquals(0, ScreenManager.getIncidenciaId());
    }
 
    @Test
    @DisplayName("setIncidenciaId guarda el id y getIncidenciaId lo devuelve")
    void setIncidenciaId_guardaYRecupera() {
        ScreenManager.setIncidenciaId(42);
        assertEquals(42, ScreenManager.getIncidenciaId());
    }
 
    @Test
    @DisplayName("setIncidenciaId sobreescribe el valor anterior")
    void setIncidenciaId_sobreescribe() {
        ScreenManager.setIncidenciaId(1);
        ScreenManager.setIncidenciaId(99);
        assertEquals(99, ScreenManager.getIncidenciaId());
    }
 
    @Test
    @DisplayName("setIncidenciaId acepta valor negativo")
    void setIncidenciaId_negativo() {
        ScreenManager.setIncidenciaId(-5);
        assertEquals(-5, ScreenManager.getIncidenciaId());
    }
 
    @Test
    @DisplayName("change() sin Stage inicializado lanza excepción")
    void change_sinStage_lanzaExcepcion() {
        assertThrows(Exception.class, () -> ScreenManager.change("login.fxml"));
    }
}
