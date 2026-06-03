package dam.mod.controllers;

import dam.mod.models.Incidencia;
import dam.mod.services.IIncidenciaService;
import dam.mod.utils.ScreenManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleIncidenciaControllerTest {

    private DetalleIncidenciaController controller;

    private IIncidenciaService incidenciaService =
            mock(IIncidenciaService.class);

    private Label lblAsunto = mock(Label.class);
    private Label lblEstado = mock(Label.class);
    private Label lblFecha = mock(Label.class);

    private TextArea txtDescripcion =
            mock(TextArea.class);

    private Button btnCerrar = mock(Button.class);

    @BeforeAll
    static void initJavaFX() {
        dam.mod.JavaFXInitializer.init();
    }

    @BeforeEach
    void setUp() throws Exception {

        controller = new DetalleIncidenciaController();

        setField("incidenciaService", incidenciaService);

        setField("lblAsunto", lblAsunto);
        setField("lblEstado", lblEstado);
        setField("lblFecha", lblFecha);
        setField("txtDescripcion", txtDescripcion);
        setField("btnCerrar", btnCerrar);
        setField("mensajeLabel", mock(javafx.scene.control.Label.class));
    }

    @Test
    void cargarIncidencia_noEncontrada_redirigirAListado() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getIncidenciaId)
                    .thenReturn(99);

            when(incidenciaService.findById(99))
                    .thenReturn(null);

            invoke("cargarIncidencia");

            screenMock.verify(() ->
                    ScreenManager.change("incidencias.fxml"));
        }
    }

    @Test
    void cargarIncidencia_encontrada_rellenaCampos() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            screenMock.when(ScreenManager::getIncidenciaId)
                    .thenReturn(1);

            Incidencia inc = incidenciaMock(1, "ABIERTA");

            when(incidenciaService.findById(1))
                    .thenReturn(inc);

            invoke("cargarIncidencia");

            verify(lblAsunto)
                    .setText("Asunto de prueba");

            verify(lblEstado)
                    .setText("Estado: ABIERTA");

            verify(txtDescripcion)
                    .setText("Descripción de prueba");
        }
    }

    @Test
    void actualizarBoton_incidenciaCerrada_desactivaBoton()
            throws Exception {

        setField("inc", incidenciaMock(1, "CERRADA"));

        invoke("actualizarBoton");

        verify(btnCerrar).setDisable(true);
    }

    @Test
    void actualizarBoton_incidenciaAbierta_activaBoton()
            throws Exception {

        setField("inc", incidenciaMock(1, "ABIERTA"));

        invoke("actualizarBoton");

        verify(btnCerrar).setDisable(false);
    }

    @Test
void cerrarIncidencia_exito_recargaVista() throws Exception {
    Incidencia inc = incidenciaMock(1, "ABIERTA");
    setField("inc", inc);

    try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {
        screenMock.when(ScreenManager::getIncidenciaId).thenReturn(1);
        when(incidenciaService.cambiarEstado(1, "CERRADA")).thenReturn(true);
        Incidencia incCerrada = incidenciaMock(1, "CERRADA");
        when(incidenciaService.findById(1)).thenReturn(incCerrada);

        invoke("cerrarIncidencia");

        verify(incidenciaService).cambiarEstado(1, "CERRADA");
        verify(incidenciaService, atLeastOnce()).findById(1);
    }
}

    @Test
    void cerrarIncidencia_error_noRecarga()
            throws Exception {

        Incidencia inc = incidenciaMock(1, "ABIERTA");

        setField("inc", inc);

        when(incidenciaService
                .cambiarEstado(1, "CERRADA"))
                .thenReturn(false);

        invoke("cerrarIncidencia");

        verify(incidenciaService, never())
                .findById(anyInt());
    }

    @Test
    void cerrarIncidencia_sinIncidencia_noHaceNada()
            throws Exception {

        setField("inc", null);

        invoke("cerrarIncidencia");

        verify(incidenciaService, never())
                .cambiarEstado(anyInt(), anyString());
    }

    @Test
    void volver_navegaAIncidencias() {

        try (MockedStatic<ScreenManager> screenMock =
                     mockStatic(ScreenManager.class)) {

            invoke("volver");

            screenMock.verify(() ->
                    ScreenManager.change("incidencias.fxml"));
        }
    }

    private Incidencia incidenciaMock(int id,
                                      String estado) {

        Incidencia inc = new Incidencia();

        inc.setId(id);
        inc.setAsunto("Asunto de prueba");
        inc.setDescripcion("Descripción de prueba");
        inc.setFecha(LocalDate.now());
        inc.setEstado(estado);

        return inc;
    }

    private void setField(String name, Object value)
            throws Exception {

        Field f = DetalleIncidenciaController.class
                .getDeclaredField(name);

        f.setAccessible(true);

        f.set(controller, value);
    }

    private void invoke(String methodName) {

        try {

            Method m = DetalleIncidenciaController.class
                    .getDeclaredMethod(methodName);

            m.setAccessible(true);

            m.invoke(controller);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}