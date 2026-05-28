package dam.mod.models;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ReservaTest {

    Reserva reserva;
    int id = 1;
    int idUsuario = 2;
    int idActividad = 3;
    LocalDate fecha = LocalDate.now();
    String estado = "activa";
    String nombreActividad = "Yoga matutino";

    @BeforeEach
    void setup() {
        reserva = new Reserva(id, idUsuario, idActividad, fecha, estado);
    }

    @DisplayName("Reserva no es null")
    @Order(1)
    @Test
    void reservaNotNullTest() {
        Assertions.assertNotNull(reserva, "La reserva no puede ser null");
    }

    @DisplayName("Getter: id")
    @Order(2)
    @Test
    void reservaGetIdTest() {
        Assertions.assertEquals(id, reserva.getId());
    }

    @DisplayName("Getter: idUsuario")
    @Order(3)
    @Test
    void reservaGetIdUsuarioTest() {
        Assertions.assertEquals(idUsuario, reserva.getIdUsuario());
    }

    @DisplayName("Getter: idActividad")
    @Order(4)
    @Test
    void reservaGetIdActividadTest() {
        Assertions.assertEquals(idActividad, reserva.getIdActividad());
    }

    @DisplayName("Getter: fecha")
    @Order(5)
    @Test
    void reservaGetFechaTest() {
        Assertions.assertEquals(fecha, reserva.getFecha());
    }

    @DisplayName("Getter: estado")
    @Order(6)
    @Test
    void reservaGetEstadoTest() {
        Assertions.assertEquals(estado, reserva.getEstado());
    }

    @DisplayName("Getter: nombreActividad null por defecto")
    @Order(7)
    @Test
    void reservaGetNombreActividadNullPorDefectoTest() {
        Assertions.assertNull(reserva.getNombreActividad(),
                "Sin asignar, nombreActividad debe ser null");
    }

    @DisplayName("setNombreActividad: actualiza correctamente")
    @Order(8)
    @Test
    void reservaSetNombreActividadTest() {
        reserva.setNombreActividad(nombreActividad);
        Assertions.assertEquals(nombreActividad, reserva.getNombreActividad(),
                "setNombreActividad debe actualizar el valor");
    }

    @DisplayName("toString: contiene nombre, fecha y estado")
    @Order(9)
    @Test
    void reservaToStringContieneNombreTest() {
        reserva.setNombreActividad(nombreActividad);
        String resultado = reserva.toString();
        Assertions.assertAll(
                () -> Assertions.assertTrue(resultado.contains(nombreActividad),
                        "toString debe contener el nombre de la actividad"),
                () -> Assertions.assertTrue(resultado.contains(fecha.toString()),
                        "toString debe contener la fecha"),
                () -> Assertions.assertTrue(resultado.contains(estado),
                        "toString debe contener el estado")
        );
    }

    @DisplayName("equals: mismo id → igual")
    @Order(10)
    @Test
    void reservaEqualsTrueTest() {
        Reserva otra = new Reserva(id, 99, 99, LocalDate.of(2000, 1, 1), "cancelada");
        Assertions.assertEquals(reserva, otra,
                "Reservas con el mismo id deben ser iguales aunque difieran en otros campos");
    }

    @DisplayName("equals: distinto id → no igual")
    @Order(11)
    @Test
    void reservaEqualsFalseTest() {
        Reserva otra = new Reserva(99, idUsuario, idActividad, fecha, estado);
        Assertions.assertNotEquals(reserva, otra, "Distinto id debe ser distinto");
    }

    @DisplayName("equals: misma referencia → igual")
    @Order(12)
    @Test
    void reservaEqualsMismaReferenciaTest() {
        Assertions.assertEquals(reserva, reserva);
    }

    @DisplayName("equals: comparar con null → false")
    @Order(13)
    @Test
    void reservaEqualsNullTest() {
        Assertions.assertNotEquals(reserva, null);
    }

    @DisplayName("equals: comparar con tipo distinto → false")
    @Order(14)
    @Test
    void reservaEqualsTipoDistintoTest() {
        Assertions.assertNotEquals(reserva, "un string");
    }

    @DisplayName("hashCode: mismo id → mismo hashCode")
    @Order(15)
    @Test
    void reservaHashCodeIgualTest() {
        Reserva otra = new Reserva(id, 99, 99, LocalDate.of(2000, 1, 1), "cancelada");
        Assertions.assertEquals(reserva.hashCode(), otra.hashCode(),
                "Mismo id debe producir el mismo hashCode");
    }

    @DisplayName("hashCode: distinto id → distinto hashCode")
    @Order(16)
    @Test
    void reservaHashCodeDistintoTest() {
        Reserva otra = new Reserva(99, idUsuario, idActividad, fecha, estado);
        Assertions.assertNotEquals(reserva.hashCode(), otra.hashCode());
    }
}