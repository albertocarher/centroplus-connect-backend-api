package dam.mod.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
import dam.mod.repositories.IActividadRepository;
import dam.mod.services.impl.ActividadServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ActividadServiceTest {

    IActividadService actividadservice;

    @Mock
    IActividadRepository actividadRepositoryMock;

    Actividad actividadValida;

    @BeforeEach
    void setup() {
        actividadservice = new ActividadServiceImpl(actividadRepositoryMock);
        actividadValida = new Actividad(1, "Yoga", "Deportiva", 60, 15.0, 10, 3);
    }

    @DisplayName("findById: id 0 devuelve null sin llamar al repositorio")
    @Order(1)
    @Test
    void findById0Test() {
        Actividad actividad = actividadservice.findById(0);
        Assertions.assertNull(actividad);
        verify(actividadRepositoryMock, never()).findById(anyInt());
    }

    @DisplayName("findById: id negativo devuelve null sin llamar al repositorio")
    @Order(2)
    @Test
    void findByIdNegativoTest() {
        Actividad actividad = actividadservice.findById(-5);
        Assertions.assertNull(actividad);
        verify(actividadRepositoryMock, never()).findById(anyInt());
    }

    @DisplayName("findById: id válido devuelve la actividad del repositorio")
    @Order(3)
    @Test
    void findByIdTest() {
        when(actividadRepositoryMock.findById(1)).thenReturn(actividadValida);
        Actividad resultado = actividadservice.findById(1);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getId());
    }

    @DisplayName("findById: repositorio devuelve null → servicio devuelve null")
    @Order(4)
    @Test
    void findByIdNoExisteTest() {
        when(actividadRepositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertNull(actividadservice.findById(99));
    }

    @DisplayName("findAll: devuelve la lista del repositorio")
    @Order(5)
    @Test
    void findAllTest() {
        when(actividadRepositoryMock.findAll()).thenReturn(Arrays.asList(actividadValida));
        List<Actividad> resultado = actividadservice.findAll();
        Assertions.assertEquals(1, resultado.size());
    }

    @DisplayName("findAll: lista vacía cuando no hay actividades")
    @Order(6)
    @Test
    void findAllVacioTest() {
        when(actividadRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertTrue(actividadservice.findAll().isEmpty());
    }

    @DisplayName("create: actividad válida delega en repositorio y devuelve true")
    @Order(7)
    @Test
    void createValidoTest() {
        when(actividadRepositoryMock.save(any())).thenReturn(true);
        Assertions.assertTrue(actividadservice.create(actividadValida));
    }

    @DisplayName("create: actividad null lanza IllegalArgumentException")
    @Order(8)
    @Test
    void createNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> actividadservice.create(null));
    }

    @DisplayName("create: duración 0 lanza IllegalArgumentException")
    @Order(9)
    @Test
    void createDuracionCeroTest() {
        Actividad invalida = new Actividad(0, "Yoga", "Deportiva", 0, 15.0, 10, 0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> actividadservice.create(invalida));
    }

    @DisplayName("create: precio negativo lanza IllegalArgumentException")
    @Order(10)
    @Test
    void createPrecioNegativoTest() {
        Actividad invalida = new Actividad(0, "Yoga", "Deportiva", 60, -1.0, 10, 0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> actividadservice.create(invalida));
    }

    @DisplayName("create: plazas máximas 0 lanza IllegalArgumentException")
    @Order(11)
    @Test
    void createPlazasMaximasCeroTest() {
        Actividad invalida = new Actividad(0, "Yoga", "Deportiva", 60, 15.0, 0, 0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> actividadservice.create(invalida));
    }

    @DisplayName("create: plazas ocupadas mayores que máximas lanza IllegalArgumentException")
    @Order(12)
    @Test
    void createPlazasOcupadasSuperanMaximasTest() {
        Actividad invalida = new Actividad(0, "Yoga", "Deportiva", 60, 15.0, 5, 10);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> actividadservice.create(invalida));
    }

    @DisplayName("update: actividad válida delega en repositorio y devuelve true")
    @Order(13)
    @Test
    void updateValidoTest() {
        when(actividadRepositoryMock.update(any())).thenReturn(true);
        Assertions.assertTrue(actividadservice.update(actividadValida));
    }

    @DisplayName("update: actividad null lanza IllegalArgumentException")
    @Order(14)
    @Test
    void updateNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> actividadservice.update(null));
    }

    @DisplayName("delete: delega en repositorio y devuelve true")
    @Order(15)
    @Test
    void deleteTrueTest() {
        when(actividadRepositoryMock.delete(anyInt())).thenReturn(true);
        Assertions.assertTrue(actividadservice.delete(1));
    }

    @DisplayName("delete: repositorio devuelve false → servicio devuelve false")
    @Order(16)
    @Test
    void deleteFalseTest() {
        when(actividadRepositoryMock.delete(anyInt())).thenReturn(false);
        Assertions.assertFalse(actividadservice.delete(99));
    }

    @DisplayName("reservarPlaza: actividad inexistente devuelve false")
    @Order(17)
    @Test
    void reservarPlazaActividadNoExisteTest() {
        when(actividadRepositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertFalse(actividadservice.reservarPlaza(99));
    }

    @DisplayName("reservarPlaza: actividad completa devuelve false")
    @Order(18)
    @Test
    void reservarPlazaLlenaTest() {
        Actividad llena = new Actividad(1, "Yoga", "Deportiva", 60, 15.0, 5, 5);
        when(actividadRepositoryMock.findById(1)).thenReturn(llena);
        Assertions.assertFalse(actividadservice.reservarPlaza(1));
    }

    @DisplayName("reservarPlaza: incrementa plazasOcupadas y devuelve true")
    @Order(19)
    @Test
    void reservarPlazaOkTest() {
        Actividad actividad = new Actividad(1, "Yoga", "Deportiva", 60, 15.0, 10, 3);
        when(actividadRepositoryMock.findById(1)).thenReturn(actividad);
        when(actividadRepositoryMock.update(any())).thenReturn(true);

        boolean resultado = actividadservice.reservarPlaza(1);

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(4, actividad.getPlazasOcupadas(),
                "Las plazas ocupadas deben incrementarse en 1");
    }

    @DisplayName("cancelarPlaza: actividad inexistente devuelve false")
    @Order(20)
    @Test
    void cancelarPlazaActividadNoExisteTest() {
        when(actividadRepositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertFalse(actividadservice.cancelarPlaza(99));
    }

    @DisplayName("cancelarPlaza: sin plazas ocupadas devuelve false")
    @Order(21)
    @Test
    void cancelarPlazaSinOcupadasTest() {
        Actividad vacia = new Actividad(1, "Yoga", "Deportiva", 60, 15.0, 10, 0);
        when(actividadRepositoryMock.findById(1)).thenReturn(vacia);
        Assertions.assertFalse(actividadservice.cancelarPlaza(1));
    }

    @DisplayName("cancelarPlaza: decrementa plazasOcupadas y devuelve true")
    @Order(22)
    @Test
    void cancelarPlazaOkTest() {
        Actividad actividad = new Actividad(1, "Yoga", "Deportiva", 60, 15.0, 10, 3);
        when(actividadRepositoryMock.findById(1)).thenReturn(actividad);
        when(actividadRepositoryMock.update(any())).thenReturn(true);

        boolean resultado = actividadservice.cancelarPlaza(1);

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(2, actividad.getPlazasOcupadas(),
                "Las plazas ocupadas deben decrementarse en 1");
    }

    @DisplayName("findCompletas: devuelve solo actividades sin plazas libres")
    @Order(23)
    @Test
    void findCompletasTest() {
        Actividad llena    = new Actividad(1, "Yoga",   "Deportiva", 60, 15.0, 5, 5);
        Actividad conHueco = new Actividad(2, "Pilates","Deportiva", 45, 10.0, 5, 3);
        when(actividadRepositoryMock.findAll()).thenReturn(Arrays.asList(llena, conHueco));

        List<Actividad> resultado = actividadservice.findCompletas();

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(1, resultado.get(0).getId());
    }

    @DisplayName("findCompletas: lista vacía cuando ninguna está llena")
    @Order(24)
    @Test
    void findCompletasVacioTest() {
        when(actividadRepositoryMock.findAll()).thenReturn(Arrays.asList(actividadValida));
        Assertions.assertTrue(actividadservice.findCompletas().isEmpty());
    }

    @DisplayName("calcularPlazasDisponibles: actividad inexistente devuelve 0")
    @Order(25)
    @Test
    void calcularPlazasDisponiblesNoExisteTest() {
        when(actividadRepositoryMock.findById(anyInt())).thenReturn(null);
        Assertions.assertEquals(0, actividadservice.calcularPlazasDisponibles(99));
    }

    @DisplayName("calcularPlazasDisponibles: devuelve maximas - ocupadas")
    @Order(26)
    @Test
    void calcularPlazasDisponiblesTest() {
        when(actividadRepositoryMock.findById(1)).thenReturn(actividadValida);
        Assertions.assertEquals(7, actividadservice.calcularPlazasDisponibles(1));
    }

    @DisplayName("calcularIngresosTotales: suma ocupadas * precio de todas las actividades")
    @Order(27)
    @Test
    void calcularIngresosTotalesTest() {
        Actividad a1 = new Actividad(1, "Yoga",    "Deportiva", 60, 10.0, 10, 3);
        Actividad a2 = new Actividad(2, "Pilates", "Deportiva", 45, 20.0, 10, 2);
        when(actividadRepositoryMock.findAll()).thenReturn(Arrays.asList(a1, a2));

        Assertions.assertEquals(70.0, actividadservice.calcularIngresosTotales(), 0.001);
    }

    @DisplayName("calcularIngresosTotales: sin actividades devuelve 0")
    @Order(28)
    @Test
    void calcularIngresosTotalesVacioTest() {
        when(actividadRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertEquals(0.0, actividadservice.calcularIngresosTotales(), 0.001);
    }
}