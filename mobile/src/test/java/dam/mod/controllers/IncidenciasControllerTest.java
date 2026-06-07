package dam.mod.controllers;
 
import dam.mod.models.Incidencia;
import dam.mod.models.Usuario;
import dam.mod.services.IIncidenciaService;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ResourceBundle;
 
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
 
class IncidenciasControllerTest {
 
    private IncidenciasController controller;
 
    private IIncidenciaService incidenciaService;
    private ListView<Incidencia> listaIncidencias;
    private ObservableList<Incidencia> items;
    private TextField txtAsunto;
    private TextArea txtDescripcion;
    private Label mensajeLabel;
    private ResourceBundle bundle;
 
    private final Usuario usuario = new Usuario();
 
    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        usuario.setId(5);
 
        controller       = new IncidenciasController();
        incidenciaService = mock(IIncidenciaService.class);
        listaIncidencias  = mock(ListView.class);
        txtAsunto         = mock(TextField.class);
        txtDescripcion    = mock(TextArea.class);
        mensajeLabel      = mock(Label.class);
        items             = FXCollections.observableArrayList();
 
        bundle = mock(ResourceBundle.class);
        lenient().when(bundle.getString(anyString())).thenAnswer(i -> i.getArgument(0));
 
        when(listaIncidencias.getItems()).thenReturn(items);
        lenient().when(listaIncidencias.getSelectionModel())
                .thenReturn(mock(MultipleSelectionModel.class));
 
        lenient().when(incidenciaService.findByUsuario(anyInt())).thenReturn(List.of());
 
        setField("incidenciaService", incidenciaService);
        setField("listaIncidencias",  listaIncidencias);
        setField("txtAsunto",         txtAsunto);
        setField("txtDescripcion",    txtDescripcion);
        setField("mensajeLabel",      mensajeLabel);
        setField("usuarioActual",     usuario);
        setField("bundle",            bundle);
 
    }
 
    @Test
    void initialize_sinSesion_redirigirALogin() {
        try (MockedStatic<Session> sessionMock   = mockStatic(Session.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(null);
 
            invokeInitialize();
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"));
        }
    }
 
    @Test
    void initialize_conSesion_noRedirige() {
        try (MockedStatic<Session> sessionMock      = mockStatic(Session.class);
             MockedStatic<LanguageManager> langMock = mockStatic(LanguageManager.class);
             MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
 
            sessionMock.when(Session::getCurrentUser).thenReturn(usuario);
            langMock.when(LanguageManager::getBundle).thenReturn(bundle);
 
            invokeInitialize();
 
            screenMock.verify(() -> ScreenManager.change("login.fxml"), never());
        }
    }
 
    @Test
    void enviarIncidencia_camposVacios_noLlamaServicio() {
        when(txtAsunto.getText()).thenReturn("");
        when(txtDescripcion.getText()).thenReturn("");
 
        invoke("enviarIncidencia");
 
        verify(incidenciaService, never()).create(any());
    }
 
    @Test
    void enviarIncidencia_asuntoVacio_noLlamaServicio() {
        when(txtAsunto.getText()).thenReturn("   ");
        when(txtDescripcion.getText()).thenReturn("ok");
 
        invoke("enviarIncidencia");
 
        verify(incidenciaService, never()).create(any());
    }
 
    @Test
    void enviarIncidencia_descripcionVacia_noLlamaServicio() {
        when(txtAsunto.getText()).thenReturn("Asunto válido");
        when(txtDescripcion.getText()).thenReturn("   ");
 
        invoke("enviarIncidencia");
 
        verify(incidenciaService, never()).create(any());
    }
 
    @Test
    void enviarIncidencia_datosValidos_creaYRecarga() {
        when(txtAsunto.getText()).thenReturn("Problema");
        when(txtDescripcion.getText()).thenReturn("Descripcion");
        when(incidenciaService.create(any())).thenReturn(true);
 
        invoke("enviarIncidencia");
 
        verify(incidenciaService).create(any(Incidencia.class));
        verify(incidenciaService).findByUsuario(usuario.getId());
        verify(txtAsunto).clear();
        verify(txtDescripcion).clear();
    }
 
    @Test
    void enviarIncidencia_errorServicio_limpiaCamposSinRecargar() {
        when(txtAsunto.getText()).thenReturn("A");
        when(txtDescripcion.getText()).thenReturn("B");
        when(incidenciaService.create(any())).thenReturn(false);
 
        invoke("enviarIncidencia");
 
        verify(txtAsunto).clear();
        verify(txtDescripcion).clear();
        verify(incidenciaService, never()).findByUsuario(anyInt());
    }
 
    @Test
    void volver_navegaAInicio() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("volver");
            screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
        }
    }
 
    // ── helpers ───────────────────────────────────────────────────────────────
 
    private void invokeInitialize() {
        invoke("initialize");
    }
 
    private void invoke(String methodName) {
        try {
            Method m = IncidenciasController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    private void setField(String name, Object value) throws Exception {
        Field f = IncidenciasController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
}

