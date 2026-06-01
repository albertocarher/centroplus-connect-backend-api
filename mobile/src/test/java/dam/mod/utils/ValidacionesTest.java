package dam.mod.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidacionesTest {

    @Test
    void validarTipoUsuarioAlumnoCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarTipoUsuario("ALUMNO"));
    }

    @Test
    void validarTipoUsuarioSocioCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarTipoUsuario("SOCIO"));
    }

    @Test
    void validarTipoUsuarioAmbosCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarTipoUsuario("AMBOS"));
    }

    @Test
    void validarTipoUsuarioNullLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarTipoUsuario(null));

        assertEquals("Tipo de usuario no puede ser null", ex.getMessage());
    }

    @Test
    void validarTipoUsuarioInvalidoLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarTipoUsuario("ADMIN"));

        assertEquals("Tipo de usuario inválido: ADMIN", ex.getMessage());
    }

    @Test
    void validarTipoActividadAcademicaCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarTipoActividad("ACADEMICA"));
    }

    @Test
    void validarTipoActividadDeportivaCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarTipoActividad("DEPORTIVA"));
    }

    @Test
    void validarTipoActividadNullLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarTipoActividad(null));

        assertEquals("Tipo de actividad no puede ser null", ex.getMessage());
    }

    @Test
    void validarTipoActividadInvalidoLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarTipoActividad("MUSICAL"));

        assertEquals("Tipo de actividad inválido: MUSICAL", ex.getMessage());
    }

    @Test
    void validarEstadoReservaActivaCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarEstadoReserva("ACTIVA"));
    }

    @Test
    void validarEstadoReservaCanceladaCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarEstadoReserva("CANCELADA"));
    }

    @Test
    void validarEstadoReservaNullLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarEstadoReserva(null));

        assertEquals("Estado de reserva no puede ser null", ex.getMessage());
    }

    @Test
    void validarEstadoReservaInvalidoLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarEstadoReserva("PENDIENTE"));

        assertEquals("Estado de reserva inválido: PENDIENTE", ex.getMessage());
    }

    @Test
    void validarEstadoIncidenciaAbiertaCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarEstadoIncidencia("ABIERTA"));
    }

    @Test
    void validarEstadoIncidenciaEnProcesoCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarEstadoIncidencia("EN_PROCESO"));
    }

    @Test
    void validarEstadoIncidenciaCerradaCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarEstadoIncidencia("CERRADA"));
    }

    @Test
    void validarEstadoIncidenciaNullLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarEstadoIncidencia(null));

        assertEquals("Estado de incidencia no puede ser null", ex.getMessage());
    }

    @Test
    void validarEstadoIncidenciaInvalidoLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarEstadoIncidencia("PAUSADA"));

        assertEquals("Estado de incidencia inválido: PAUSADA", ex.getMessage());
    }

    @Test
    void validarEmailCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarEmail("test@email.com"));
    }

    @Test
    void validarEmailNullLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarEmail(null));

        assertEquals("Email inválido: null", ex.getMessage());
    }

    @Test
    void validarEmailFormatoIncorrectoLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarEmail("correo-mal"));

        assertEquals("Email inválido: correo-mal", ex.getMessage());
    }

    @Test
    void validarDNICorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarDNI("12345678A"));
    }

    @Test
    void validarDNINullLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarDNI(null));

        assertEquals("DNI inválido: null", ex.getMessage());
    }

    @Test
    void validarDNIFormatoIncorrectoLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarDNI("1234A"));

        assertEquals("DNI inválido: 1234A", ex.getMessage());
    }

    @Test
    void validarNoVacioCorrecto() {
        assertDoesNotThrow(() ->
                Validaciones.validarNoVacio("Juan", "Nombre"));
    }

    @Test
    void validarNoVacioNullLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarNoVacio(null, "Nombre"));

        assertEquals("Nombre no puede estar vacío", ex.getMessage());
    }

    @Test
    void validarNoVacioCadenaVaciaLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Validaciones.validarNoVacio("   ", "Nombre"));

        assertEquals("Nombre no puede estar vacío", ex.getMessage());
    }
}