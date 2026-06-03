package dam.mod.controllers;

import dam.mod.models.Reserva;
import dam.mod.models.Usuario;
import dam.mod.services.IReservaService;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReservasControllerTest {

    private ReservasController controller;

    private IReservaService reservaService = mock(IReservaService.class);

    private ListView<Reserva> listaReservas = mock(ListView.class);
    private ObservableList<Reserva> items = mock(ObservableList.class);

    private ResourceBundle bundleMock;

    private MockedStatic<Session> sessionMock;
    private MockedStatic<LanguageManager> langMock;

    private final Usuario usuario = new Usuario();

    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {

        usuario.setId(10);

        // =========================
        // LANGUAGE
        // =========================
        bundleMock = mock(ResourceBundle.class);

        langMock = mockStatic(LanguageManager.class);
        langMock.when(LanguageManager::getBundle).thenReturn(bundleMock);

        when(bundleMock.getString(anyString()))
                .thenReturn("test");

        // =========================
        // SESSION
        // =========================
        sessionMock = mockStatic(Session.class);
        sessionMock.when(Session::getCurrentUser).thenReturn(usuario);

        // =========================
        // CONTROLLER
        // =========================
        controller = new ReservasController();

        setField("reservaService", reservaService);
        setField("listaReservas", listaReservas);
        setField("mensajeLabel", mock(javafx.scene.control.Label.class));
        setField("bundle", bundleMock);

        lenient().when(listaReservas.getItems()).thenReturn(items);
    }

    @AfterEach
    void tearDown() {
        langMock.close();
        sessionMock.close();
    }

    // =====================================================
    // TESTS
    // =====================================================

    @Test
    void initialize_sinSesion_redirigirALogin() {

        sessionMock.when(Session::getCurrentUser).thenReturn(null);

        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invoke("initialize");

            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }

    @Test
    void cargarReservas_cargaDatos() {

        List<Reserva> reservas = List.of(new Reserva(), new Reserva());

        when(reservaService.findByIdUsuario(10)).thenReturn(reservas);

        invoke("cargarReservas");

        // 🔥 FIX DEFINITIVO (setAll ambigüo)
        verify(items).setAll(any(java.util.Collection.class));
    }

    @Test
    void cancelarReserva_sinSeleccion_noHaceNada() {

        MultipleSelectionModel<Reserva> model = mock(MultipleSelectionModel.class);
        when(listaReservas.getSelectionModel()).thenReturn(model);
        when(model.getSelectedItem()).thenReturn(null);

        invoke("cancelarReserva");

        verifyNoInteractions(reservaService);
    }

    @Test
    void cancelarReserva_propia_exito() {

        Reserva r = new Reserva();
        r.setId(5);
        r.setIdUsuario(10);

        MultipleSelectionModel<Reserva> model = mock(MultipleSelectionModel.class);
        when(listaReservas.getSelectionModel()).thenReturn(model);
        when(model.getSelectedItem()).thenReturn(r);

        when(reservaService.cambiarEstado(5, "CANCELADA")).thenReturn(true);
        when(reservaService.findByIdUsuario(10)).thenReturn(List.of());

        invoke("cancelarReserva");

        verify(reservaService).cambiarEstado(5, "CANCELADA");

        // 🔥 FIX DEFINITIVO
        verify(items).setAll(any(java.util.Collection.class));
    }

    @Test
void cancelarReserva_error_noRecarga() {

    Reserva r = new Reserva();
    r.setId(5);
    r.setIdUsuario(10);

    MultipleSelectionModel<Reserva> model = mock(MultipleSelectionModel.class);
    when(listaReservas.getSelectionModel()).thenReturn(model);
    when(model.getSelectedItem()).thenReturn(r);

    when(reservaService.cambiarEstado(5, "CANCELADA")).thenReturn(false);

    invoke("cancelarReserva");

    verify(reservaService).cambiarEstado(5, "CANCELADA");

    verify(reservaService, never()).findByIdUsuario(anyInt());
}

    @Test
    void volver_navega() {

        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

            invoke("volver");

            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }

    // =====================================================
    // HELPERS
    // =====================================================

    private void setField(String name, Object value) throws Exception {
        Field f = ReservasController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private void invoke(String method) {
        try {
            Method m = ReservasController.class.getDeclaredMethod(method);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}