package dam.mod.controllers;
 
import dam.mod.JavaFXInitializer;
import dam.mod.models.Incidencia;
import dam.mod.services.IIncidenciaService;
import dam.mod.utils.ScreenManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ResourceBundle;
 
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de DetalleIncidenciaController")
class DetalleIncidenciaControllerTest {
 
    private DetalleIncidenciaController controller;
 
    private IIncidenciaService incidenciaService;
    private Label    lblAsunto;
    private Label    lblEstado;
    private Label    lblFecha;
    private Label    mensajeLabel;
    private TextArea txtDescripcion;
    private Button   btnCerrar;
 
    @BeforeAll
    static void initJavaFX() {
        JavaFXInitializer.init();
    }
 
    @BeforeEach
    void setUp() throws Exception {
        controller = new DetalleIncidenciaController();
 
        incidenciaService = mock(IIncidenciaService.class);
        lblAsunto         = mock(Label.class);
        lblEstado         = mock(Label.class);
        lblFecha          = mock(Label.class);
        txtDescripcion    = mock(TextArea.class);
        btnCerrar         = mock(Button.class);
        mensajeLabel      = mock(Label.class);
 
        ResourceBundle bundle = mock(ResourceBundle.class);
        lenient().when(bundle.getString(anyString())).thenAnswer(i -> i.getArgument(0));
 
        setField("incidenciaService", incidenciaService);
        setField("lblAsunto",         lblAsunto);
        setField("lblEstado",         lblEstado);
        setField("lblFecha",          lblFecha);
        setField("txtDescripcion",    txtDescripcion);
        setField("btnCerrar",         btnCerrar);
        setField("mensajeLabel",      mensajeLabel);
        setField("bundle",            bundle);
    }
 
    @Test
    @DisplayName("cargarIncidencia: no encontrada → redirige a listado")
    void cargarIncidencia_noEncontrada_redirigirAListado() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            screenMock.when(ScreenManager::getIncidenciaId).thenReturn(99);
            when(incidenciaService.findById(99)).thenReturn(null);
 
            invoke("cargarIncidencia");
 
            screenMock.verify(() -> ScreenManager.change("incidencias.fxml"));
        }
    }
 
    @Test
    @DisplayName("cargarIncidencia: encontrada → rellena todos los campos")
    void cargarIncidencia_encontrada_rellenaCampos() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            screenMock.when(ScreenManager::getIncidenciaId).thenReturn(1);
            when(incidenciaService.findById(1)).thenReturn(incidenciaMock(1, "ABIERTA"));
 
            invoke("cargarIncidencia");
 
            verify(lblAsunto).setText(anyString());
            verify(lblEstado).setText(anyString());
            verify(lblFecha).setText(anyString());
            verify(txtDescripcion).setText(anyString());
        }
    }
 
    @Test
    @DisplayName("actualizarBoton: incidencia CERRADA → botón deshabilitado")
    void actualizarBoton_incidenciaCerrada_desactivaBoton() throws Exception {
        setField("inc", incidenciaMock(1, "CERRADA"));
 
        invoke("actualizarBoton");
 
        verify(btnCerrar).setDisable(true);
    }
 
    @Test
    @DisplayName("actualizarBoton: incidencia ABIERTA → botón habilitado")
    void actualizarBoton_incidenciaAbierta_activaBoton() throws Exception {
        setField("inc", incidenciaMock(1, "ABIERTA"));
 
        invoke("actualizarBoton");
 
        verify(btnCerrar).setDisable(false);
    }
 
    @Test
    @DisplayName("cerrarIncidencia: inc null → no hace nada")
    void cerrarIncidencia_sinIncidencia_noHaceNada() throws Exception {
        setField("inc", null);
 
        invoke("cerrarIncidencia");
 
        verify(incidenciaService, never()).cambiarEstado(anyInt(), anyString());
    }
 
    @Test
    @DisplayName("cerrarIncidencia: éxito → cambia estado y recarga")
    void cerrarIncidencia_exito_recargaVista() throws Exception {
        Incidencia inc = incidenciaMock(1, "ABIERTA");
        setField("inc", inc);
 
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            screenMock.when(ScreenManager::getIncidenciaId).thenReturn(1);
            when(incidenciaService.cambiarEstado(1, "CERRADA")).thenReturn(true);
            when(incidenciaService.findById(1)).thenReturn(inc);
 
            invoke("cerrarIncidencia");
 
            verify(incidenciaService).cambiarEstado(1, "CERRADA");
            verify(incidenciaService, atLeastOnce()).findById(1);
        }
    }
 
    @Test
    @DisplayName("cerrarIncidencia: error → no recarga")
    void cerrarIncidencia_error_noRecarga() throws Exception {
        setField("inc", incidenciaMock(1, "ABIERTA"));
        when(incidenciaService.cambiarEstado(1, "CERRADA")).thenReturn(false);
 
        invoke("cerrarIncidencia");
 
        verify(incidenciaService, never()).findById(anyInt());
    }
 
    @Test
    @DisplayName("volver navega a incidencias.fxml")
    void volver_navegaAIncidencias() {
        try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
            invoke("volver");
            screenMock.verify(() -> ScreenManager.change("incidencias.fxml"));
        }
    }
 
    // --- Helpers ---
 
    private Incidencia incidenciaMock(int id, String estado) {
        Incidencia inc = new Incidencia();
        inc.setId(id);
        inc.setAsunto("Asunto de prueba");
        inc.setDescripcion("Descripción de prueba");
        inc.setFecha(LocalDate.now());
        inc.setEstado(estado);
        return inc;
    }
 
    private void setField(String name, Object value) throws Exception {
        Field f = DetalleIncidenciaController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }
 
    private void invoke(String method) {
        try {
            Method m = DetalleIncidenciaController.class.getDeclaredMethod(method);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}