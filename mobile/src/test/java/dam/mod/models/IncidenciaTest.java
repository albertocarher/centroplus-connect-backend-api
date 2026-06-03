package dam.mod.models;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class IncidenciaTest {

    Incidencia incidencia;
    int id = 1;
    int idUsuario = 2;
    String asunto = "asunto";
    String descripcion = "descripcion";
    LocalDate fecha = LocalDate.now();
    String estado = "activo";

    @BeforeEach
    void setup() {
        incidencia = new Incidencia(id, idUsuario, asunto, descripcion, fecha, estado);
    }

    @DisplayName("Incidencia no es null")
    @Order(1)
    @Test
    void incidenciaNotNull() {
        Assertions.assertNotNull(incidencia);
    }

    @DisplayName("equals: mismo id → igual")
    @Order(2)
    @Test
    void incidenciaEqualsTrueTest() {
        Incidencia incidenciaNueva = new Incidencia(1);
        Assertions.assertEquals(incidencia, incidenciaNueva);
    }

    @DisplayName("equals: distinto id → no igual")
    @Order(3)
    @Test
    void incidenciaEqualsFalseTest() {
        Incidencia otra = new Incidencia(99);
        Assertions.assertNotEquals(incidencia, otra, "Distinto id debe ser distinto");
    }

    @DisplayName("equals: misma referencia → igual")
    @Order(4)
    @Test
    void incidenciaEqualsMismaReferenciaTest() {
        Assertions.assertEquals(incidencia, incidencia, "Una incidencia debe ser igual a sí misma");
    }

    @DisplayName("equals: comparar con null → false")
    @Order(5)
    @Test
    void incidenciaEqualsNullTest() {
        Assertions.assertNotEquals(incidencia, null, "Comparar con null debe ser false");
    }

    @DisplayName("equals: comparar con tipo distinto → false")
    @Order(6)
    @Test
    void incidenciaEqualsTipoDistintoTest() {
        Assertions.assertNotEquals(incidencia, "un string", "Comparar con otro tipo debe ser false");
    }

    @DisplayName("hashCode: mismo id → mismo hashCode")
    @Order(7)
    @Test
    void incidenciaHashCodeIgualTest() {
        Incidencia otra = new Incidencia(id);
        Assertions.assertEquals(incidencia.hashCode(), otra.hashCode(),
                "Mismo id debe producir el mismo hashCode");
    }

    @DisplayName("hashCode: distinto id → distinto hashCode")
    @Order(8)
    @Test
    void incidenciaHashCodeDistintoTest() {
        Incidencia otra = new Incidencia(99);
        Assertions.assertNotEquals(incidencia.hashCode(), otra.hashCode(),
                "Distinto id debería producir distinto hashCode");
    }

    @DisplayName("Getter: id")
    @Order(9)
    @Test
    void incidenciaGetIdTest() {
        Assertions.assertEquals(id, incidencia.getId());
    }

    @DisplayName("Getter: idUsuario")
    @Order(10)
    @Test
    void incidenciaGetIdUsuarioTest() {
        Assertions.assertEquals(idUsuario, incidencia.getIdUsuario());
    }

    @DisplayName("Getter: asunto")
    @Order(11)
    @Test
    void incidenciaGetAsuntoTest() {
        Assertions.assertEquals(asunto, incidencia.getAsunto());
    }

    @DisplayName("Getter: descripcion")
    @Order(12)
    @Test
    void incidenciaGetDescripcionTest() {
        Assertions.assertEquals(descripcion, incidencia.getDescripcion());
    }

    @DisplayName("Getter: fecha")
    @Order(13)
    @Test
    void incidenciaGetFechaTest() {
        Assertions.assertEquals(fecha, incidencia.getFecha());
    }

    @DisplayName("Getter: estado")
    @Order(14)
    @Test
    void incidenciaGetEstadoTest() {
        Assertions.assertEquals(estado, incidencia.getEstado());
    }

    @DisplayName("setDescripcion: actualiza correctamente")
    @Order(15)
    @Test
    void incidenciaSetDescripcionTest() {
        String nueva = "nueva descripcion";
        incidencia.setDescripcion(nueva);
        Assertions.assertEquals(nueva, incidencia.getDescripcion(),
                "setDescripcion debe actualizar el valor");
    }

    @DisplayName("setEstado: actualiza correctamente")
    @Order(16)
    @Test
    void incidenciaSetEstadoTest() {
        String nuevoEstado = "CERRADA";
        incidencia.setEstado(nuevoEstado);
        Assertions.assertEquals(nuevoEstado, incidencia.getEstado(),
                "setEstado debe actualizar el valor");
    }

    @DisplayName("Constructor vacío: no es null")
    @Order(17)
    @Test
    void incidenciaConstructorVacioTest() {
        Incidencia vacia = new Incidencia();
        Assertions.assertNotNull(vacia, "El constructor vacío no debe devolver null");
    }

    @DisplayName("Constructor (id, asunto, descripcion, fecha): valores correctos")
    @Order(18)
    @Test
    void incidenciaConstructorParcialTest() {
        Incidencia parcial = new Incidencia(5, asunto, descripcion, fecha);
        Assertions.assertAll(
                () -> Assertions.assertEquals(5,           parcial.getId()),
                () -> Assertions.assertEquals(asunto,      parcial.getAsunto()),
                () -> Assertions.assertEquals(descripcion, parcial.getDescripcion()),
                () -> Assertions.assertEquals(fecha,       parcial.getFecha())
        );
    }
}
