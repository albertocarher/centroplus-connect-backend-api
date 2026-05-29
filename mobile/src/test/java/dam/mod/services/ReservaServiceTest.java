package dam.mod.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dam.mod.models.Actividad;
import dam.mod.models.Reserva;
import dam.mod.models.Usuario;
import dam.mod.repositories.IReservaRepository;
import dam.mod.services.impl.ReservaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock IReservaRepository repositoryMock;
    @Mock IUsuarioService usuarioServiceMock;
    @Mock IActividadService actividadServiceMock;

    IReservaService service;

    Reserva reservaValida;
    Usuario usuarioValido;
    Actividad actividadValida;

    @BeforeEach
    void setup() {
        service = new ReservaServiceImpl(repositoryMock, usuarioServiceMock, actividadServiceMock);
        reservaValida  = new Reserva(1, 2, 3, LocalDate.now(), "ACTIVA");
        usuarioValido  = new Usuario(2, "Ana", "12345678A", "ana@example.com", "600000000", "ALUMNO", "pass");
        actividadValida = new Actividad(3, "Yoga", "DEPORTIVA", 60, 15.0, 10, 3);
    }

    @DisplayName("findAll: devuelve la lista del repositorio")
    @Order(1)
    @Test
    void findAllTest() {
        when(repositoryMock.findAll()).thenReturn(Arrays.asList(reservaValida));
        Assertions.assertEquals(1, service.findAll().size());
    }

    @DisplayName("findAll: lista vacía cuando no hay reservas")
    @Order(2)
    @Test
    void findAllVacioTest() {
        when(repositoryMock.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertTrue(service.findAll().isEmpty());
    }

    @DisplayName("findById: devuelve reserva cuando existe")
    @Order(3)
    @Test
    void findByIdEncontradoTest() {
        when(repositoryMock.findById(1)).thenReturn(reservaValida);
        Assertions.assertNotNull(service.findById(1));
    }

    @DisplayName("findById: devuelve null cuando no existe")
    @Order(4)
    @Test
    void findByIdNoExisteTest() {
        when(repositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertNull(service.findById(99));
    }

    @DisplayName("create: reserva válida devuelve true")
    @Order(5)
    @Test
    void createValidoTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(3)).thenReturn(actividadValida);
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(7);
        when(repositoryMock.existsReserva(3, 2)).thenReturn(false);
        when(repositoryMock.save(any())).thenReturn(true);

        Assertions.assertTrue(service.create(reservaValida));
        verify(actividadServiceMock).reservarPlaza(3);
    }

    @DisplayName("create: reserva null lanza IllegalArgumentException")
    @Order(6)
    @Test
    void createNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(null));
    }

    @DisplayName("create: estado inválido lanza IllegalArgumentException")
    @Order(7)
    @Test
    void createEstadoInvalidoTest() {
        Reserva invalida = new Reserva(0, 2, 3, LocalDate.now(), "INVALIDO");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(invalida));
    }

    @DisplayName("create: usuario inexistente lanza IllegalArgumentException")
    @Order(8)
    @Test
    void createUsuarioNoExisteTest() {
        when(usuarioServiceMock.findById(anyInt())).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(reservaValida));
    }

    @DisplayName("create: actividad inexistente lanza IllegalArgumentException")
    @Order(9)
    @Test
    void createActividadNoExisteTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(anyInt())).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(reservaValida));
    }

    @DisplayName("create: sin plazas disponibles lanza IllegalArgumentException")
    @Order(10)
    @Test
    void createSinPlazasTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(3)).thenReturn(actividadValida);
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(reservaValida));
    }

    @DisplayName("create: reserva duplicada lanza IllegalArgumentException")
    @Order(11)
    @Test
    void createDuplicadaTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(3)).thenReturn(actividadValida);
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(7);
        when(repositoryMock.existsReserva(3, 2)).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(reservaValida));
    }

    @DisplayName("update: reserva válida devuelve true")
    @Order(12)
    @Test
    void updateValidoTest() {
        when(repositoryMock.update(any())).thenReturn(true);
        Assertions.assertTrue(service.update(reservaValida));
    }

    @DisplayName("update: reserva null lanza IllegalArgumentException")
    @Order(13)
    @Test
    void updateNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(null));
    }

    @DisplayName("update: estado inválido lanza IllegalArgumentException")
    @Order(14)
    @Test
    void updateEstadoInvalidoTest() {
        Reserva invalida = new Reserva(1, 2, 3, LocalDate.now(), "INVALIDO");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(invalida));
    }

    @DisplayName("delete: devuelve true cuando elimina correctamente")
    @Order(15)
    @Test
    void deleteTrueTest() {
        when(repositoryMock.delete(anyInt())).thenReturn(true);
        Assertions.assertTrue(service.delete(1));
    }

    @DisplayName("delete: devuelve false cuando no encuentra la reserva")
    @Order(16)
    @Test
    void deleteFalseTest() {
        when(repositoryMock.delete(anyInt())).thenReturn(false);
        Assertions.assertFalse(service.delete(99));
    }

    @DisplayName("cancelarReserva: reserva inexistente devuelve false")
    @Order(17)
    @Test
    void cancelarReservaNoExisteTest() {
        when(repositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertFalse(service.cancelarReserva(99, 2));
    }

    @DisplayName("cancelarReserva: usuario no es el propietario devuelve false")
    @Order(18)
    @Test
    void cancelarReservaUsuarioDistintoTest() {
        when(repositoryMock.findById(1)).thenReturn(reservaValida);
        Assertions.assertFalse(service.cancelarReserva(1, 99));
    }

    @DisplayName("cancelarReserva: cancela plaza y borra reserva devuelve true")
    @Order(19)
    @Test
    void cancelarReservaOkTest() {
        when(repositoryMock.findById(1)).thenReturn(reservaValida);
        when(repositoryMock.delete(1)).thenReturn(true);

        boolean resultado = service.cancelarReserva(1, 2);

        Assertions.assertTrue(resultado);
        verify(actividadServiceMock).cancelarPlaza(3);
        verify(repositoryMock).delete(1);
    }

    @DisplayName("reservar: flujo completo correcto devuelve true")
    @Order(20)
    @Test
    void reservarOkTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(3)).thenReturn(actividadValida);
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(7);
        when(repositoryMock.existsReserva(3, 2)).thenReturn(false);
        when(repositoryMock.save(any())).thenReturn(true);

        Assertions.assertTrue(service.reservar(3, 2));
        verify(actividadServiceMock).reservarPlaza(3);
    }

    @DisplayName("reservar: usuario inexistente lanza IllegalArgumentException")
    @Order(21)
    @Test
    void reservarUsuarioNoExisteTest() {
        when(usuarioServiceMock.findById(anyInt())).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.reservar(3, 2));
    }

    @DisplayName("reservar: actividad inexistente lanza IllegalArgumentException")
    @Order(22)
    @Test
    void reservarActividadNoExisteTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(anyInt())).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.reservar(3, 2));
    }

    @DisplayName("reservar: sin plazas disponibles lanza IllegalArgumentException")
    @Order(23)
    @Test
    void reservarSinPlazasTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(3)).thenReturn(actividadValida);
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.reservar(3, 2));
    }

    @DisplayName("reservar: reserva duplicada lanza IllegalArgumentException")
    @Order(24)
    @Test
    void reservarDuplicadaTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(actividadServiceMock.findById(3)).thenReturn(actividadValida);
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(7);
        when(repositoryMock.existsReserva(3, 2)).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.reservar(3, 2));
    }

    @DisplayName("yaReservado: devuelve true cuando existe la reserva")
    @Order(25)
    @Test
    void yaReservadoTrueTest() {
        when(repositoryMock.existsReserva(3, 2)).thenReturn(true);
        Assertions.assertTrue(service.yaReservado(3, 2));
    }

    @DisplayName("yaReservado: devuelve false cuando no existe la reserva")
    @Order(26)
    @Test
    void yaReservadoFalseTest() {
        when(repositoryMock.existsReserva(3, 2)).thenReturn(false);
        Assertions.assertFalse(service.yaReservado(3, 2));
    }

    @DisplayName("existeReservaUsuarioActividad: delega correctamente en yaReservado")
    @Order(27)
    @Test
    void existeReservaUsuarioActividadTest() {
        when(repositoryMock.existsReserva(3, 2)).thenReturn(true);
        Assertions.assertTrue(service.existeReservaUsuarioActividad(2, 3));
    }

    @DisplayName("comprobarPlazasDisponibles: devuelve true cuando hay plazas")
    @Order(28)
    @Test
    void comprobarPlazasDisponiblesTrueTest() {
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(5);
        Assertions.assertTrue(service.comprobarPlazasDisponibles(3));
    }

    @DisplayName("comprobarPlazasDisponibles: devuelve false cuando no hay plazas")
    @Order(29)
    @Test
    void comprobarPlazasDisponiblesFalseTest() {
        when(actividadServiceMock.calcularPlazasDisponibles(3)).thenReturn(0);
        Assertions.assertFalse(service.comprobarPlazasDisponibles(3));
    }

    @DisplayName("findByIdUsuario: devuelve reservas del usuario")
    @Order(30)
    @Test
    void findByIdUsuarioTest() {
        when(repositoryMock.findByIdUsuario(2)).thenReturn(Arrays.asList(reservaValida));
        List<Reserva> resultado = service.findByIdUsuario(2);
        Assertions.assertEquals(1, resultado.size());
    }

    @DisplayName("findByIdUsuario: lista vacía si el usuario no tiene reservas")
    @Order(31)
    @Test
    void findByIdUsuarioVacioTest() {
        when(repositoryMock.findByIdUsuario(anyInt())).thenReturn(Collections.emptyList());
        Assertions.assertTrue(service.findByIdUsuario(99).isEmpty());
    }
}
