package dam.mod.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ActividadTest {
    Actividad actividad;
    int id = 1;
    String nombre = "Nombre";
    String tipo = "Tipo";
    int duracion = 1;
    int precio = 2;
    int plazas = 5;
    int ocupadas = 2;

    @BeforeEach
    void setup() {
        actividad = new Actividad(id, nombre, tipo, duracion, precio, plazas, ocupadas);
    }

    @DisplayName("Test verifica no null")
    @Order(1)
    @Test
    void actividadNoNullTest() {
        Assertions.assertNotNull(actividad, "La clase actividad no puede ser nula");
    }

    @DisplayName("Test verifica True")
    @Order(2)
    @Test
    void actividadEqualsTrueTest() {
        Actividad actividadNueva = new Actividad(1);
        Assertions.assertEquals(actividad, actividadNueva, "Debe de ser igual");
    }

    @DisplayName("Test verifica True")
    @Order(3)
    @Test
    void actividadEqualsFalseTest() {
        Actividad actividadNueva = new Actividad(2);
        Assertions.assertNotEquals(actividad, actividadNueva, "Debe de ser igual");
    }

    @DisplayName("Test verifica True")
    @Order(4)
    @Test
    void actividadEqualsTest() {
        Assertions.assertEquals(actividad, actividad, "Debe de ser igual");
    }

    @DisplayName("Getter: id")
    @Order(5)
    @Test
    void actividadGetIdTest() {
        Assertions.assertEquals(id, actividad.getId(), "El id debe coincidir");
    }

    @DisplayName("Getter: nombre")
    @Order(6)
    @Test
    void actividadGetNombreTest() {
        Assertions.assertEquals(nombre, actividad.getNombre(), "El nombre debe coincidir");
    }

    @DisplayName("Getter: tipoActividad")
    @Order(7)
    @Test
    void actividadGetTipoActividadTest() {
        Assertions.assertEquals(tipo, actividad.getTipoActividad(), "El tipo debe coincidir");
    }

    @DisplayName("Getter: duración")
    @Order(8)
    @Test
    void actividadGetDuracionTest() {
        Assertions.assertEquals(duracion, actividad.getDuracion(), "La duración debe coincidir");
    }

    @DisplayName("Getter: precio")
    @Order(9)
    @Test
    void actividadGetPrecioTest() {
        Assertions.assertEquals(precio, actividad.getPrecio(), 0.001, "El precio debe coincidir");
    }

    @DisplayName("Getter: plazas máximas")
    @Order(10)
    @Test
    void actividadGetPlazasMaximasTest() {
        Assertions.assertEquals(plazas, actividad.getPlazasMaximas(), "Las plazas máximas deben coincidir");
    }

    @DisplayName("Getter: plazas ocupadas")
    @Order(11)
    @Test
    void actividadGetPlazasOcupadasTest() {
        Assertions.assertEquals(ocupadas, actividad.getPlazasOcupadas(), "Las plazas ocupadas deben coincidir");
    }

    @DisplayName("plazasDisponibles: cálculo correcto")
    @Order(12)
    @Test
    void actividadPlazasDisponiblesTest() {
        Assertions.assertEquals(plazas - ocupadas, actividad.plazasDisponibles(),
                "Las plazas disponibles deben ser (máximas - ocupadas)");
    }

    @DisplayName("plazasDisponibles: sin ocupar = todas disponibles")
    @Order(13)
    @Test
    void actividadPlazasDisponiblesSinOcuparTest() {
        Actividad nueva = new Actividad(10, "Yoga", "Deporte", 60, 15.0, 20, 0);
        Assertions.assertEquals(20, nueva.plazasDisponibles(), "Sin plazas ocupadas deben estar todas disponibles");
    }

    @DisplayName("plazasDisponibles: llena = cero disponibles")
    @Order(14)
    @Test
    void actividadPlazasDisponiblesLlenaTest() {
        Actividad llena = new Actividad(11, "Pilates", "Deporte", 45, 10.0, 10, 10);
        Assertions.assertEquals(0, llena.plazasDisponibles(), "Actividad llena debe tener 0 plazas disponibles");
    }

    @DisplayName("setPlazasOcupadas: actualiza correctamente")
    @Order(15)
    @Test
    void actividadSetPlazasOcupadasTest() {
        actividad.setPlazasOcupadas(4);
        Assertions.assertEquals(4, actividad.getPlazasOcupadas(), "El setter debe actualizar las plazas ocupadas");
    }

    @DisplayName("setPlazasOcupadas: afecta a plazasDisponibles")
    @Order(16)
    @Test
    void actividadSetPlazasOcupadasAfectaDisponiblesTest() {
        actividad.setPlazasOcupadas(5);
        Assertions.assertEquals(plazas - 5, actividad.plazasDisponibles(),
                "Al cambiar plazas ocupadas debe actualizarse plazasDisponibles");
    }

    @DisplayName("toString: formato nombre - tipo")
    @Order(17)
    @Test
    void actividadToStringTest() {
        String esperado = nombre + " - " + tipo;
        Assertions.assertEquals(esperado, actividad.toString(), "toString debe devolver 'nombre - tipo'");
    }

    @DisplayName("equals: objeto null devuelve false")
    @Order(18)
    @Test
    void actividadEqualsNullTest() {
        Assertions.assertNotEquals(actividad, null, "Comparar con null debe ser false");
    }

    @DisplayName("equals: tipo distinto devuelve false")
    @Order(19)
    @Test
    void actividadEqualsTipoDistintoTest() {
        Assertions.assertNotEquals(actividad, "un string", "Comparar con otro tipo debe ser false");
    }

    @DisplayName("hashCode: misma id → mismo hashCode")
    @Order(20)
    @Test
    void actividadHashCodeIgualTest() {
        Actividad otra = new Actividad(id);
        Assertions.assertEquals(actividad.hashCode(), otra.hashCode(),
                "Actividades con la misma id deben tener el mismo hashCode");
    }

    @DisplayName("hashCode: distinta id → distinto hashCode")
    @Order(21)
    @Test
    void actividadHashCodeDistintoTest() {
        Actividad otra = new Actividad(99);
        Assertions.assertNotEquals(actividad.hashCode(), otra.hashCode(),
                "Actividades con distinta id deberían tener hashCode diferente");
    }

    @DisplayName("Constructor vacío: no es null")
    @Order(22)
    @Test
    void actividadConstructorVacioNoNullTest() {
        Actividad vacia = new Actividad();
        Assertions.assertNotNull(vacia, "El constructor vacío no debe devolver null");
    }

}
