package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Usuario;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Method;
 
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de InicioController")
class InicioControllerTest {
 
    private InicioController controller;
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() {
        controller = new InicioController();
    }
 
    @Test
    @DisplayName("initialize sin sesión redirige a login")
    void initialize_sinSesion_redirigirALogin() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
 
            invoke("initialize");
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("initialize con sesión activa no redirige")
    void initialize_conSesion_noRedirige() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(new Usuario());
 
            invoke("initialize");
 
            screenMock.verifyNoInteractions();
        }
    }
 
    @Test
    @DisplayName("abrirActividades navega a actividades.fxml")
    void abrirActividades_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("abrirActividades");
            screenMock.verify(() -> ScreenManager.change("actividades.fxml"));
        }
    }
 
    @Test
    @DisplayName("abrirReservas navega a reservas.fxml")
    void abrirReservas_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("abrirReservas");
            screenMock.verify(() -> ScreenManager.change("reservas.fxml"));
        }
    }
 
    @Test
    @DisplayName("abrirIncidencias navega a incidencias.fxml")
    void abrirIncidencias_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("abrirIncidencias");
            screenMock.verify(() -> ScreenManager.change("incidencias.fxml"));
        }
    }
 
    @Test
    @DisplayName("abrirPerfil navega a perfil.fxml")
    void abrirPerfil_navegaCorrectamente() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("abrirPerfil");
            screenMock.verify(() -> ScreenManager.change("perfil.fxml"));
        }
    }
 
    @Test
    @DisplayName("setSpanish aplica idioma 'es' y recarga la pantalla actual")
    void setSpanish_aplicaIdiomaEs() {
        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("inicio.fxml");
 
            invoke("setSpanish");
 
            langMock.verify(() -> LanguageManager.setLanguage("es"));
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    @Test
    @DisplayName("setEnglish aplica idioma 'en' y recarga la pantalla actual")
    void setEnglish_aplicaIdiomaEn() {
        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("inicio.fxml");
 
            invoke("setEnglish");
 
            langMock.verify(() -> LanguageManager.setLanguage("en"));
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    @Test
    @DisplayName("setGerman aplica idioma 'de' y recarga la pantalla actual")
    void setGerman_aplicaIdiomaDe() {
        try (MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            screenMock.when(ScreenManager::getCurrentScreen).thenReturn("inicio.fxml");
 
            invoke("setGerman");
 
            langMock.verify(() -> LanguageManager.setLanguage("de"));
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    // --- Helper ---
 
    private void invoke(String methodName) {
        try {
            Method m = InicioController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}