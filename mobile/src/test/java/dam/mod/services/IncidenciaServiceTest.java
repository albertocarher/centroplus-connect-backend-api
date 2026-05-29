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

import dam.mod.models.Incidencia;
import dam.mod.models.Usuario;
import dam.mod.repositories.IIncidenciaRepository;
import dam.mod.services.impl.IncidenciaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class IncidenciaServiceTest {

    @Mock IIncidenciaRepository repositoryMock;
    @Mock IUsuarioService usuarioServiceMock;

    IIncidenciaService service;

    Incidencia incidenciaValida;
    Usuario usuarioValido;

    @BeforeEach
    void setup() {
        service = new IncidenciaServiceImpl(repositoryMock, usuarioServiceMock);
        incidenciaValida = new Incidencia(1, 2, "Asunto", "Descripción", LocalDate.now(), "ABIERTA");
        usuarioValido = new Usuario(2, "Ana", "12345678A", "ana@example.com", "600000000", "ALUMNO", "pass");
    }

    @DisplayName("findAll: devuelve la lista del repositorio")
    @Order(1)
    @Test
    void findAllTest() {
        when(repositoryMock.findAll()).thenReturn(Arrays.asList(incidenciaValida));
        List<Incidencia> resultado = service.findAll();
        Assertions.assertEquals(1, resultado.size());
    }

    @DisplayName("findAll: lista vacía cuando no hay incidencias")
    @Order(2)
    @Test
    void findAllVacioTest() {
        when(repositoryMock.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertTrue(service.findAll().isEmpty());
    }

    @DisplayName("findById: devuelve incidencia cuando existe")
    @Order(3)
    @Test
    void findByIdEncontradoTest() {
        when(repositoryMock.findById(1)).thenReturn(incidenciaValida);
        Assertions.assertNotNull(service.findById(1));
    }

    @DisplayName("findById: devuelve null cuando no existe")
    @Order(4)
    @Test
    void findByIdNoExisteTest() {
        when(repositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertNull(service.findById(99));
    }

    @DisplayName("create: incidencia válida con usuario existente devuelve true")
    @Order(5)
    @Test
    void createValidoTest() {
        when(usuarioServiceMock.findById(2)).thenReturn(usuarioValido);
        when(repositoryMock.save(any())).thenReturn(true);
        Assertions.assertTrue(service.create(incidenciaValida));
    }

    @DisplayName("create: usuario inexistente lanza IllegalArgumentException")
    @Order(6)
    @Test
    void createUsuarioNoExisteTest() {
        when(usuarioServiceMock.findById(anyInt())).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(incidenciaValida));
    }

    @DisplayName("create: incidencia null lanza IllegalArgumentException")
    @Order(7)
    @Test
    void createNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(null));
    }

    @DisplayName("create: estado inválido lanza IllegalArgumentException")
    @Order(8)
    @Test
    void createEstadoInvalidoTest() {
        Incidencia invalida = new Incidencia(1, 2, "Asunto", "Descripción", LocalDate.now(), "INVALIDO");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(invalida));
    }

    @DisplayName("create: descripción vacía lanza IllegalArgumentException")
    @Order(9)
    @Test
    void createDescripcionVaciaTest() {
        Incidencia invalida = new Incidencia(1, 2, "Asunto", "", LocalDate.now(), "ABIERTA");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(invalida));
    }

    @DisplayName("create: descripción null lanza IllegalArgumentException")
    @Order(10)
    @Test
    void createDescripcionNullTest() {
        Incidencia invalida = new Incidencia(1, 2, "Asunto", null, LocalDate.now(), "ABIERTA");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(invalida));
    }

    @DisplayName("update: incidencia válida devuelve true")
    @Order(11)
    @Test
    void updateValidoTest() {
        when(repositoryMock.update(any())).thenReturn(true);
        Assertions.assertTrue(service.update(incidenciaValida));
    }

    @DisplayName("update: incidencia null lanza IllegalArgumentException")
    @Order(12)
    @Test
    void updateNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(null));
    }

    @DisplayName("update: estado inválido lanza IllegalArgumentException")
    @Order(13)
    @Test
    void updateEstadoInvalidoTest() {
        Incidencia invalida = new Incidencia(1, 2, "Asunto", "Descripción", LocalDate.now(), "INVALIDO");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(invalida));
    }

    @DisplayName("delete: devuelve true cuando elimina correctamente")
    @Order(14)
    @Test
    void deleteTrueTest() {
        when(repositoryMock.delete(anyInt())).thenReturn(true);
        Assertions.assertTrue(service.delete(1));
    }

    @DisplayName("delete: devuelve false cuando no encuentra la incidencia")
    @Order(15)
    @Test
    void deleteFalseTest() {
        when(repositoryMock.delete(anyInt())).thenReturn(false);
        Assertions.assertFalse(service.delete(99));
    }

    @DisplayName("cambiarEstado: incidencia inexistente devuelve false")
    @Order(16)
    @Test
    void cambiarEstadoNoExisteTest() {
        when(repositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertFalse(service.cambiarEstado(99, "CERRADA"));
    }

    @DisplayName("cambiarEstado: estado inválido lanza IllegalArgumentException")
    @Order(17)
    @Test
    void cambiarEstadoInvalidoTest() {
        when(repositoryMock.findById(1)).thenReturn(incidenciaValida);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.cambiarEstado(1, "INVALIDO"));
    }

    @DisplayName("cambiarEstado: actualiza estado y devuelve true")
    @Order(18)
    @Test
    void cambiarEstadoOkTest() {
        when(repositoryMock.findById(1)).thenReturn(incidenciaValida);
        when(repositoryMock.update(any())).thenReturn(true);

        boolean resultado = service.cambiarEstado(1, "CERRADA");

        Assertions.assertTrue(resultado);
        Assertions.assertEquals("CERRADA", incidenciaValida.getEstado(),
                "El estado debe actualizarse a CERRADA");
    }

    @DisplayName("cambiarEstado: todos los estados válidos funcionan")
    @Order(19)
    @Test
    void cambiarEstadoTodosValidosTest() {
        when(repositoryMock.findById(anyInt())).thenReturn(incidenciaValida);
        when(repositoryMock.update(any())).thenReturn(true);

        Assertions.assertAll(
                () -> Assertions.assertTrue(service.cambiarEstado(1, "ABIERTA")),
                () -> Assertions.assertTrue(service.cambiarEstado(1, "EN_PROCESO")),
                () -> Assertions.assertTrue(service.cambiarEstado(1, "CERRADA"))
        );
    }

    @DisplayName("findByUsuario: devuelve solo las incidencias del usuario")
    @Order(20)
    @Test
    void findByUsuarioTest() {
        Incidencia deOtro = new Incidencia(2, 99, "Otro", "Otra desc", LocalDate.now(), "ABIERTA");
        when(repositoryMock.findAll()).thenReturn(Arrays.asList(incidenciaValida, deOtro));

        List<Incidencia> resultado = service.findByUsuario(2);

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(2, resultado.get(0).getIdUsuario());
    }

    @DisplayName("findByUsuario: lista vacía si el usuario no tiene incidencias")
    @Order(21)
    @Test
    void findByUsuarioSinIncidenciasTest() {
        when(repositoryMock.findAll()).thenReturn(Arrays.asList(incidenciaValida));
        Assertions.assertTrue(service.findByUsuario(999).isEmpty());
    }
}
