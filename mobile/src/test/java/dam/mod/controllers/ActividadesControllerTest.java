package dam.mod.controllers;

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
class ActividadesControllerTest {

        private ActividadesController controller;

        @SuppressWarnings("unchecked")
        private ListView<Actividad> listaActividades = mock(ListView.class);

        @SuppressWarnings("unchecked")
        private ObservableList<Actividad> items = mock(ObservableList.class);

        private IActividadService actividadService = mock(IActividadService.class);

        @BeforeAll
        static void initJavaFX() {
                dam.mod.JavaFXInitializer.init();
        }

        @BeforeEach
        void setUp() throws Exception {

                controller = new ActividadesController();

                Field listaField = ActividadesController.class
                                .getDeclaredField("listaActividades");
                listaField.setAccessible(true);
                listaField.set(controller, listaActividades);

                Field serviceField = ActividadesController.class
                                .getDeclaredField("actividadService");
                serviceField.setAccessible(true);
                serviceField.set(controller, actividadService);

                when(listaActividades.getItems()).thenReturn(items);
        }

        @Test
        void initialize_sinSesion_redirigirALogin() {

                try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
                                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

                        sessionMock.when(Session::getCurrentUser)
                                        .thenReturn(null);

                        invoke("initialize");

                        screenMock.verify(() -> ScreenManager.change("login.fxml"));
                }
        }

        @Test
        void initialize_conSesion_noRedirige() {

                try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
                                MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

                        sessionMock.when(Session::getCurrentUser)
                                        .thenReturn(new Usuario());

                        invoke("initialize");

                        screenMock.verifyNoInteractions();
                }
        }

        @Test
        void cargarActividades_conDatos_limpiaYAñade() {

                List<Actividad> actividades = Arrays.asList(
                                new Actividad(),
                                new Actividad());

                when(actividadService.findAll()).thenReturn(actividades);

                invoke("cargarActividades");

                verify(items).clear();
                verify(items).addAll(actividades);
        }

        @Test
        void cargarActividades_listaVacia_limpiaSinAñadir() {

                when(actividadService.findAll())
                                .thenReturn(Collections.emptyList());

                invoke("cargarActividades");

                verify(items).clear();
                verify(items).addAll(Collections.emptyList());
        }

        @Test
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
        void seleccionarActividad_conSeleccion_navegaADetalle() {

                try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class);
                                MockedStatic<DetalleActividadController> detalleMock = mockStatic(
                                                DetalleActividadController.class)) {

                        MultipleSelectionModel<Actividad> model = mock(MultipleSelectionModel.class);

                        when(listaActividades.getSelectionModel()).thenReturn(model);

                        Actividad actividad = new Actividad();

                        when(model.getSelectedItem()).thenReturn(actividad);

                        invoke("seleccionarActividad");

                        detalleMock.verify(() -> DetalleActividadController.setActividad(actividad));

                        screenMock.verify(() -> ScreenManager.change("detalle_actividad.fxml"));
                }
        }

        @Test
        void volver_navegaAInicio() {

                try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

                        invoke("volver");

                        screenMock.verify(() -> ScreenManager.change("inicio.fxml"));
                }
        }

        private void invoke(String methodName) {

                try {
                        Method m = ActividadesController.class
                                        .getDeclaredMethod(methodName);

                        m.setAccessible(true);
                        m.invoke(controller);

                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }
}