package dam.mod.controllers;

import dam.mod.models.Incidencia;
import dam.mod.models.Usuario;
import dam.mod.services.IIncidenciaService;
import dam.mod.services.impl.IncidenciaServiceImpl;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IncidenciasControllerTest {

    private IncidenciasController controller;

    private IIncidenciaService incidenciaService = mock(IIncidenciaService.class);

    private ListView<Incidencia> listaIncidencias = mock(ListView.class);

    private ObservableList<Incidencia> items = FXCollections.observableArrayList();

    private TextField txtAsunto = mock(TextField.class);
    private TextArea txtDescripcion = mock(TextArea.class);
    private Label mensajeLabel = mock(Label.class);

    private MockedStatic<Session> sessionMock;
    private MockedStatic<LanguageManager> langMock;

    private final Usuario usuario = new Usuario();

    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {

        usuario.setId(5);

        sessionMock = mockStatic(Session.class);
        sessionMock.when(Session::getCurrentUser).thenReturn(usuario);

        langMock = mockStatic(LanguageManager.class);

        ResourceBundle bundleMock = mock(ResourceBundle.class);
        when(bundleMock.getString(anyString()))
                .thenAnswer(inv -> inv.getArgument(0));

        langMock.when(LanguageManager::getBundle).thenReturn(bundleMock);

        listaIncidencias = mock(ListView.class);
        txtAsunto = mock(TextField.class);
        txtDescripcion = mock(TextArea.class);
        mensajeLabel = mock(Label.class);

        when(listaIncidencias.getItems()).thenReturn(items);
        when(listaIncidencias.getSelectionModel())
                .thenReturn(mock(MultipleSelectionModel.class));

        controller = new IncidenciasController();

        setField("incidenciaService", incidenciaService);

        setField("listaIncidencias", listaIncidencias);
        setField("txtAsunto", txtAsunto);
        setField("txtDescripcion", txtDescripcion);
        setField("mensajeLabel", mensajeLabel);
        setField("usuarioActual", usuario);

        invoke("initialize");
    }

    @AfterEach
    void tearDown() {
        sessionMock.close();
        langMock.close();
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
    void enviarIncidencia_datosValidos_creaYRecarga() {

        when(txtAsunto.getText()).thenReturn("Problema");
        when(txtDescripcion.getText()).thenReturn("Descripcion");

        try (MockedConstruction<IncidenciaServiceImpl> mocked = mockConstruction(IncidenciaServiceImpl.class,
                (mock, context) -> {
                    when(mock.create(any())).thenReturn(true);
                    when(mock.findByUsuario(anyInt())).thenReturn(List.of());
                })) {

            invoke("enviarIncidencia");

            mocked.constructed().forEach(serviceMock -> {
                verify(serviceMock).create(any(Incidencia.class));
            });

            verify(txtAsunto).clear();
            verify(txtDescripcion).clear();
        }
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

    private void setField(String name, Object value) throws Exception {
        Field f = IncidenciasController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private void invoke(String method) {
        try {
            Method m = IncidenciasController.class.getDeclaredMethod(method);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void enviarIncidencia_descripcionVacia_noLlamaServicio() {
        when(txtAsunto.getText()).thenReturn("Asunto válido");
        when(txtDescripcion.getText()).thenReturn("   ");

        invoke("enviarIncidencia");

        verify(incidenciaService, never()).create(any());
    }
}

