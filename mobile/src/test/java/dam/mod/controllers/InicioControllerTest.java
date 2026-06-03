package dam.mod.controllers;

import dam.mod.models.Usuario;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

/**
 * Tests unitarios para InicioController.
 *
 * Escenarios cubiertos:
 * - initialize() sin sesión → redirige al login.
 * - initialize() con sesión → no redirige.
 * - Cada botón de navegación llama al change correcto.
 * - setSpanish/setEnglish/setGerman aplican el idioma y recargan la pantalla.
 */
@ExtendWith(MockitoExtension.class)
class InicioControllerTest {

    private InicioController controller;

    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() {
        controller = new InicioController();
    }

    @Test
    void initialize_sinSesion_redirigirALogin() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(null);

            invoke("initialize");

            screenMock.verify(() ->
                    ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void initialize_conSesion_noRedirige() {

        try (MockedStatic<Session> sessionMock =
                     mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            sessionMock.when(Session::getCurrentUser)
                    .thenReturn(new Usuario());

            invoke("initialize");

            screenMock.verifyNoInteractions();
        }
    }

    @Test
    void abrirActividades_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("abrirActividades");

            screenMock.verify(() ->
                    ScreenManager.change("actividades.fxml"));
        }
    }

    @Test
    void abrirReservas_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("abrirReservas");

            screenMock.verify(() ->
                    ScreenManager.change("reservas.fxml"));
        }
    }

    @Test
    void abrirIncidencias_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("abrirIncidencias");

            screenMock.verify(() ->
                    ScreenManager.change("incidencias.fxml"));
        }
    }

    @Test
    void abrirPerfil_navegaCorrectamente() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("abrirPerfil");

            screenMock.verify(() ->
                    ScreenManager.change("perfil.fxml"));
        }
    }

    @Test
    void setSpanish_aplicaIdiomaEs() {

        try (MockedStatic<LanguageManager> langMock =
                     mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen)
                    .thenReturn("inicio.fxml");

            invoke("setSpanish");

            langMock.verify(() ->
                    LanguageManager.setLanguage("es"));

            screenMock.verify(() ->
                    ScreenManager.change("inicio.fxml"));
        }
    }

    @Test
    void setEnglish_aplicaIdiomaEn() {

        try (MockedStatic<LanguageManager> langMock =
                     mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen)
                    .thenReturn("inicio.fxml");

            invoke("setEnglish");

            langMock.verify(() ->
                    LanguageManager.setLanguage("en"));

            screenMock.verify(() ->
                    ScreenManager.change("inicio.fxml"));
        }
    }

    @Test
    void setGerman_aplicaIdiomaDe() {

        try (MockedStatic<LanguageManager> langMock =
                     mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getCurrentScreen)
                    .thenReturn("inicio.fxml");

            invoke("setGerman");

            langMock.verify(() ->
                    LanguageManager.setLanguage("de"));

            screenMock.verify(() ->
                    ScreenManager.change("inicio.fxml"));
        }
    }

    private void invoke(String methodName) {

        try {

            Method m = InicioController.class
                    .getDeclaredMethod(methodName);

            m.setAccessible(true);

            m.invoke(controller);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}