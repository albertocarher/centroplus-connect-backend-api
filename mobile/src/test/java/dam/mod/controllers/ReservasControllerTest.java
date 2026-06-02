package dam.mod.controllers;
 
import dam.mod.models.Reserva;
import dam.mod.models.Usuario;
import dam.mod.services.IReservaService;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
 
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
class ReservasControllerTest {
 
    private ReservasController controller;
    private IReservaService reservaService = mock(IReservaService.class);
 
    @SuppressWarnings("unchecked")
    private ListView<Reserva> listaReservas = mock(ListView.class);
    @SuppressWarnings("unchecked")
    private ObservableList<Reserva> items = mock(ObservableList.class);
 
    private final int USUARIO_ID = 10;
 
    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new ReservasController();
        setField("reservaService", reservaService);
        setField("listaReservas", listaReservas);
        when(listaReservas.getItems()).thenReturn(items);
    }
 
    @Test
    void initialize_sinSesion_redirigirALogin() {
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
            invoke("initialize");
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    void cargarReservas_cargaLasReservasDelUsuario() {
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(usuarioMock(USUARIO_ID));
            List<Reserva> reservas = List.of(new Reserva(), new Reserva());
            when(reservaService.findByIdUsuario(USUARIO_ID)).thenReturn(reservas);
 
            invoke("cargarReservas");
 
            verify(items).setAll((java.util.Collection<Reserva>) reservas);
        }
    }
 
    @Test
    @SuppressWarnings("unchecked")
    void cancelarReserva_sinSeleccion_noLlamaServicio() {
        MultipleSelectionModel<Reserva> model = mock(MultipleSelectionModel.class);
        when(listaReservas.getSelectionModel()).thenReturn(model);
        when(model.getSelectedItem()).thenReturn(null);
 
        invoke("cancelarReserva");
 
        verify(reservaService, never()).cancelarReserva(anyInt(), anyInt());
    }
 
    @Test
    @SuppressWarnings("unchecked")
    void cancelarReserva_propia_exito_cambiEstadoYRecarga() {
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(usuarioMock(USUARIO_ID));
 
            Reserva reserva = reservaMock(5, USUARIO_ID);
            MultipleSelectionModel<Reserva> model = mock(MultipleSelectionModel.class);
            when(listaReservas.getSelectionModel()).thenReturn(model);
            when(model.getSelectedItem()).thenReturn(reserva);
            when(reservaService.cancelarReserva(5, USUARIO_ID)).thenReturn(true);
            when(reservaService.findByIdUsuario(USUARIO_ID)).thenReturn(List.of());
 
            invoke("cancelarReserva");
 
            verify(reservaService).cancelarReserva(5, USUARIO_ID);
            verify(items, atLeastOnce()).setAll(any(java.util.Collection.class));
        }
    }
 
    @Test
    @SuppressWarnings("unchecked")
    void cancelarReserva_propia_error_noRecarga() {
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(usuarioMock(USUARIO_ID));
 
            Reserva reserva = reservaMock(5, USUARIO_ID);
            MultipleSelectionModel<Reserva> model = mock(MultipleSelectionModel.class);
            when(listaReservas.getSelectionModel()).thenReturn(model);
            when(model.getSelectedItem()).thenReturn(reserva);
            when(reservaService.cancelarReserva(5, USUARIO_ID)).thenReturn(false);
 
            invoke("cancelarReserva");
 
            verify(items, never()).setAll(any(java.util.Collection.class));
        }
    }
 
    @Test
    void volver_navegaAInicio() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("volver");
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    private Usuario usuarioMock(int id) {
        Usuario u = new Usuario();
        u.setId(id);
        return u;
    }
 
    private Reserva reservaMock(int id, int idUsuario) {
        Reserva r = new Reserva();
        r.setId(id);
        r.setIdUsuario(idUsuario);
        r.setEstado("ACTIVA");
        return r;
    }
 
    private void setField(String name, Object value) throws Exception {
        Field f = ReservasController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
 
    private void invoke(String methodName) {
        try {
            Method m = ReservasController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}