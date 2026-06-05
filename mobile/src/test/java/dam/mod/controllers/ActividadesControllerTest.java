package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Actividad;
import dam.mod.models.Usuario;
import dam.mod.services.IActividadService;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
 
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ActividadesController")
class ActividadesControllerTest {
 
    private ActividadesController controller;
 
    @SuppressWarnings("unchecked")
    private ListView<Actividad>      listaActividades = mock(ListView.class);
    @SuppressWarnings("unchecked")
    private ObservableList<Actividad> items           = mock(ObservableList.class);
    private IActividadService        actividadService = mock(IActividadService.class);
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new ActividadesController();
 
        setField("listaActividades", listaActividades);
        setField("actividadService", actividadService);
 
        when(listaActividades.getItems()).thenReturn(items);
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
            when(actividadService.findAll()).thenReturn(Collections.emptyList());
 
            invoke("initialize");
 
            screenMock.verifyNoInteractions();
        }
    }
 
    @Test
    @DisplayName("cargarActividades: limpia la lista y añade las actividades obtenidas")
    void cargarActividades_conDatos_limpiaYAñade() {
        List<Actividad> actividades = Arrays.asList(new Actividad(), new Actividad());
        when(actividadService.findAll()).thenReturn(actividades);
 
        invoke("cargarActividades");
 
        verify(items).clear();
        verify(items).addAll(actividades);
    }
 
    @Test
    @DisplayName("cargarActividades con lista vacía: limpia sin añadir nada")
    void cargarActividades_listaVacia_limpiaSinAñadir() {
        when(actividadService.findAll()).thenReturn(Collections.emptyList());
 
        invoke("cargarActividades");
 
        verify(items).clear();
        verify(items).addAll(Collections.emptyList());
    }
 
    @Test
    @DisplayName("seleccionarActividad sin selección no navega")
    void seleccionarActividad_sinSeleccion_noNavega() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            MultipleSelectionModel<Actividad> model = mock(MultipleSelectionModel.class);
            when(listaActividades.getSelectionModel()).thenReturn(model);
            when(model.getSelectedItem()).thenReturn(null);
 
            invoke("seleccionarActividad");
 
            screenMock.verifyNoInteractions();
        }
    }
 
    @Test
    @DisplayName("seleccionarActividad con actividad: guarda la actividad y navega al detalle")
    void seleccionarActividad_conSeleccion_navegaADetalle() {
        try (MockedStatic<ScreenManager> screenMock            = mockStatic(ScreenManager.class);
             MockedStatic<DetalleActividadController> detalleMock =
                     mockStatic(DetalleActividadController.class)) {
 
            MultipleSelectionModel<Actividad> model = mock(MultipleSelectionModel.class);
            Actividad actividad = new Actividad();
            when(listaActividades.getSelectionModel()).thenReturn(model);
            when(model.getSelectedItem()).thenReturn(actividad);
 
            invoke("seleccionarActividad");
 
            detalleMock.verify(() -> DetalleActividadController.setActividad(actividad));
            screenMock.verify(() -> ScreenManager.change("detalle_actividad.fxml"));
        }
    }
 
    @Test
    @DisplayName("volver navega a inicio.fxml")
    void volver_navegaAInicio() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("volver");
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    private void setField(String name, Object value) throws Exception {
        Field f = ActividadesController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
 
    private void invoke(String methodName) {
        try {
            Method m = ActividadesController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}