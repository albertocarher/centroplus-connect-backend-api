package dam.mod.utils;

public final class Validaciones {

    private Validaciones() {
        // Evita instanciación
    }


    public static void validarTipoUsuario(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de usuario no puede ser null");
        }

        if (!tipo.equals("ALUMNO") &&
            !tipo.equals("SOCIO") &&
            !tipo.equals("AMBOS")) {

            throw new IllegalArgumentException("Tipo de usuario inválido: " + tipo);
        }
    }


    public static void validarTipoActividad(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de actividad no puede ser null");
        }

        if (!tipo.equals("ACADEMICA") &&
            !tipo.equals("DEPORTIVA")) {

            throw new IllegalArgumentException("Tipo de actividad inválido: " + tipo);
        }
    }


    public static void validarEstadoReserva(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado de reserva no puede ser null");
        }

        if (!estado.equals("ACTIVA") &&
            !estado.equals("CANCELADA")) {

            throw new IllegalArgumentException("Estado de reserva inválido: " + estado);
        }
    }

    public static void validarEstadoIncidencia(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado de incidencia no puede ser null");
        }

        if (!estado.equals("ABIERTA") &&
            !estado.equals("EN_PROCESO") &&
            !estado.equals("CERRADA")) {

            throw new IllegalArgumentException("Estado de incidencia inválido: " + estado);
        }
    }

    public static void validarEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

    public static void validarDNI(String dni) {
        if (dni == null || !dni.matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("DNI inválido: " + dni);
        }
    }

    public static void validarNoVacio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío");
        }
    }
}
