package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Actividad;
import dam.mod.models.Reserva;
import dam.mod.models.Usuario;
import dam.mod.services.IActividadService;
import dam.mod.services.IReservaService;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
 
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de DetalleActividadController")
class DetalleActividadControllerTest {
 
    private DetalleActividadController controller;
 
    private IActividadService actividadService = mock(IActividadService.class);
    private IReservaService   reservaService   = mock(IReservaService.class);
 
    private Label lblNombre    = mock(Label.class);
    private Label lblTipo      = mock(Label.class);
    private Label lblDuracion  = mock(Label.class);
    private Label lblPrecio    = mock(Label.class);
    private Label lblPlazas    = mock(Label.class);
    private Label mensajeLabel = mock(Label.class);
 
    private final ResourceBundle bundle = new ResourceBundle() {
    private final Map<String, String> keys = Map.ofEntries(
        Map.entry("activity.name",        "Nombre: "),
        Map.entry("activity.type",        "Tipo: "),
        Map.entry("activity.duration",    "Duración: "),
        Map.entry("activity.minutes",     " min"),
        Map.entry("activity.price",       "Precio: "),
        Map.entry("activity.euro",        " €"),
        Map.entry("activity.available",   "Plazas disponibles: "),
        Map.entry("error.already.booked", "Ya reservado"),
        Map.entry("error.no.spots",       "Sin plazas"),
        Map.entry("success.booking",      "Reserva OK"),
        Map.entry("error.booking",        "Error reserva")
    );
    @Override protected Object handleGetObject(String key) { return keys.getOrDefault(key, key); }
    @Override public Enumeration<String> getKeys() { return Collections.enumeration(keys.keySet()); }
    @Override public boolean containsKey(String key) { return keys.containsKey(key); }
};
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new DetalleActividadController();
 
        setField("actividadService", actividadService);
        setField("reservaService",   reservaService);
        setField("lblNombre",        lblNombre);
        setField("lblTipo",          lblTipo);
        setField("lblDuracion",      lblDuracion);
        setField("lblPrecio",        lblPrecio);
        setField("lblPlazas",        lblPlazas);
        setField("mensajeLabel",     mensajeLabel);
        setField("bundle",           bundle);
    }
 
    @Test
    @DisplayName("cargarDatos rellena todos los labels correctamente")
    void cargarDatos_rellenaTodosLosLabels() throws Exception {
        DetalleActividadController.setActividad(actividadMock(10, 3));
 
        invoke("cargarDatos");
 
        verify(lblNombre).setText("Nombre: TestActividad");
        verify(lblTipo).setText("Tipo: YOGA");
        verify(lblDuracion).setText("Duración: 60 min");
        verify(lblPrecio).setText("Precio: 15.0 €");
        verify(lblPlazas).setText("Plazas disponibles: 7");
    }
 
    @Test
    @DisplayName("reservar: ya tiene reserva activa → no reserva")
    void reservar_yaTieneReservaActiva_noReserva() throws Exception {
        Actividad a = actividadMock(42, 3);
        DetalleActividadController.setActividad(a);
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(usuarioMock(1));
 
            Reserva r = new Reserva();
            r.setIdActividad(42);
            r.setEstado("ACTIVA");
            when(reservaService.findByIdUsuario(1)).thenReturn(List.of(r));
 
            invoke("reservar");
 
            verify(reservaService, never()).reservar(anyInt(), anyInt());
        }
    }
 
    @Test
    @DisplayName("reservar: sin plazas disponibles → no reserva")
    void reservar_sinPlazas_noReserva() throws Exception {
        // plazasMaximas == plazasOcupadas → disponibles = 0
        Actividad a = actividadMock(5, 5);
        DetalleActividadController.setActividad(a);
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(usuarioMock(1));
            when(reservaService.findByIdUsuario(1)).thenReturn(Collections.emptyList());
 
            invoke("reservar");
 
            verify(reservaService, never()).reservar(anyInt(), anyInt());
        }
    }
 
    @Test
    @DisplayName("reservar: éxito → llama al servicio y recarga la actividad")
    void reservar_exito_llamaAlServicioYRecarga() throws Exception {
        Actividad a = actividadMock(10, 2);
        DetalleActividadController.setActividad(a);
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(usuarioMock(1));
            when(reservaService.findByIdUsuario(1)).thenReturn(Collections.emptyList());
            when(reservaService.reservar(a.getId(), 1)).thenReturn(true);
            when(actividadService.findById(a.getId())).thenReturn(a);
 
            invoke("reservar");
 
            verify(reservaService).reservar(a.getId(), 1);
            verify(actividadService).findById(a.getId());
        }
    }
 
    @Test
    @DisplayName("reservar: error en servicio → no recarga")
    void reservar_errorEnServicio_noRecarga() throws Exception {
        Actividad a = actividadMock(10, 2);
        DetalleActividadController.setActividad(a);
 
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(Session::getCurrentUser).thenReturn(usuarioMock(1));
            when(reservaService.findByIdUsuario(1)).thenReturn(Collections.emptyList());
            when(reservaService.reservar(a.getId(), 1)).thenReturn(false);
 
            invoke("reservar");
 
            verify(actividadService, never()).findById(anyInt());
        }
    }
 
    @Test
    @DisplayName("initialize redirige a login si no hay sesión")
    void initialize_sinSesion_redirigirALogin() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
 
            invoke("initialize");
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    @DisplayName("volver navega a actividades.fxml")
    void volver_navegaAActividades() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("volver");
            screenMock.verify(() -> ScreenManager.change("actividades.fxml"));
        }
    }
 
    // --- Helpers ---
 
    private Actividad actividadMock(int maximas, int ocupadas) {
        Actividad a = new Actividad();
        a.setId(42);
        a.setNombre("TestActividad");
        a.setTipoActividad("YOGA");
        a.setDuracion(60);
        a.setPrecio(15.0);
        a.setPlazasMaximas(maximas);
        a.setPlazasOcupadas(ocupadas);
        return a;
    }
 
    private Usuario usuarioMock(int id) {
        Usuario u = new Usuario();
        u.setId(id);
        return u;
    }
 
    private void setField(String name, Object value) throws Exception {
        Field f = DetalleActividadController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
 
    private void invoke(String methodName) {
        try {
            Method m = DetalleActividadController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}