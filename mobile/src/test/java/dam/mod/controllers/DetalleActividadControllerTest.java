package dam.mod.controllers;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleActividadControllerTest {

        private DetalleActividadController controller;

        private IActividadService actividadService = mock(IActividadService.class);

        private IReservaService reservaService = mock(IReservaService.class);

        private Label lblNombre = mock(Label.class);
        private Label lblTipo = mock(Label.class);
        private Label lblDuracion = mock(Label.class);
        private Label lblPrecio = mock(Label.class);
        private Label lblPlazas = mock(Label.class);

        @BeforeAll
        static void initJavaFX() {
                dam.mod.JavaFXInitializer.init();
        }

        @BeforeEach
        void setUp() throws Exception {

                controller = new DetalleActividadController();

                setField("actividadService", actividadService);
                setField("reservaService", reservaService);

                setField("lblNombre", lblNombre);
                setField("lblTipo", lblTipo);
                setField("lblDuracion", lblDuracion);
                setField("lblPrecio", lblPrecio);
                setField("lblPlazas", lblPlazas);
                setField("mensajeLabel", mock(javafx.scene.control.Label.class));
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
        void cargarDatos_rellenaTodosLosLabels()
                        throws Exception {

                Actividad a = actividadMock(10, 3);

                DetalleActividadController.setActividad(a);

                invoke("cargarDatos");

                verify(lblNombre)
                                .setText("Nombre: TestActividad");

                verify(lblTipo)
                                .setText("Tipo: YOGA");

                verify(lblDuracion)
                                .setText("Duración: 60 min");

                verify(lblPrecio)
                                .setText("Precio: 15.0 €");

                verify(lblPlazas)
                                .setText("Plazas disponibles: 7");
        }

        @Test
        void reservar_yaTieneReservaActiva_noReserva() throws Exception {

                Actividad a = actividadMock(42, 3);
                DetalleActividadController.setActividad(a);

                try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {

                        Usuario user = usuarioMock(1);
                        sessionMock.when(Session::getCurrentUser).thenReturn(user);

                        Reserva r = new Reserva();
                        r.setIdActividad(42);
                        r.setEstado("ACTIVA");

                        when(reservaService.findByIdUsuario(1))
                                        .thenReturn(List.of(r));

                        invoke("reservar");

                        verify(reservaService).findByIdUsuario(1);
                        verify(reservaService, never()).reservar(anyInt(), anyInt());
                }
        }

        @Test
        void reservar_sinPlazas_noReserva()
                        throws Exception {

                try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {

                        Actividad a = actividadMock(5, 5);

                        DetalleActividadController.setActividad(a);

                        sessionMock.when(Session::getCurrentUser)
                                        .thenReturn(usuarioMock(1));

                        when(reservaService.findByIdUsuario(1))
                                        .thenReturn(Collections.emptyList());

                        invoke("reservar");

                        verify(reservaService, never())
                                        .reservar(anyInt(), anyInt());
                }
        }

        @Test
        void reservar_exito_llamaAlServicioYRecarga()
                        throws Exception {

                try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {

                        Actividad a = actividadMock(10, 2);

                        DetalleActividadController.setActividad(a);

                        sessionMock.when(Session::getCurrentUser)
                                        .thenReturn(usuarioMock(1));

                        when(reservaService.findByIdUsuario(1))
                                        .thenReturn(Collections.emptyList());

                        when(reservaService.reservar(a.getId(), 1))
                                        .thenReturn(true);

                        when(actividadService.findById(a.getId()))
                                        .thenReturn(a);

                        invoke("reservar");

                        verify(reservaService)
                                        .reservar(a.getId(), 1);

                        verify(actividadService)
                                        .findById(a.getId());
                }
        }

        @Test
        void reservar_errorEnServicio_noRecarga()
                        throws Exception {

                try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {

                        Actividad a = actividadMock(10, 2);

                        DetalleActividadController.setActividad(a);

                        sessionMock.when(Session::getCurrentUser)
                                        .thenReturn(usuarioMock(1));

                        when(reservaService.findByIdUsuario(1))
                                        .thenReturn(Collections.emptyList());

                        when(reservaService.reservar(a.getId(), 1))
                                        .thenReturn(false);

                        invoke("reservar");

                        verify(actividadService, never())
                                        .findById(anyInt());
                }
        }

        @Test
        void volver_navegaAActividades() {

                try (MockedStatic<ScreenManager> screenMock = mockStatic(ScreenManager.class)) {

                        invoke("volver");

                        screenMock.verify(() -> ScreenManager.change("actividades.fxml"));
                }
        }

        private Actividad actividadMock(int maximas,
                        int ocupadas) {

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

        private void setField(String name, Object value)
                        throws Exception {

                Field f = DetalleActividadController.class
                                .getDeclaredField(name);

                f.setAccessible(true);

                f.set(controller, value);
        }

        private void invoke(String methodName) {

                try {

                        Method m = DetalleActividadController.class
                                        .getDeclaredMethod(methodName);

                        m.setAccessible(true);

                        m.invoke(controller);

                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }
}