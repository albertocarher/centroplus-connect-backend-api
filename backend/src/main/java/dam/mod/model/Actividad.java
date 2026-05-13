package dam.mod.models;


public class Actividad {
    private int id;
    private String nombre;
    private String tipoActividad;
    private int duracionMinutos;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;

    public Actividad(int id, String nombre, String tipoActividad,
            int duracionMinutos, double precio,
            int plazasMaximas, int plazasOcupadas) {
        this.id = id;
        this.nombre = nombre;
        this.tipoActividad = tipoActividad;
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
        this.plazasMaximas = plazasMaximas;
        this.plazasOcupadas = plazasOcupadas;
    }

    public int calcularPlazasDisponibles() {
        return plazasMaximas - plazasOcupadas;
    }

    public boolean hayPlazasLibres() {
        return plazasOcupadas < plazasMaximas;
    }

    public boolean estaCompleta() {
        return plazasOcupadas >= plazasMaximas;
    }

    public void reservarPlaza() {
        if (!hayPlazasLibres()) {
            throw new IllegalStateException("No hay plazas disponibles");
        }
        plazasOcupadas++;
    }

    public void cancelarPlaza() {
        if (plazasOcupadas <= 0) {
            throw new IllegalStateException("No hay plazas ocupadas");
        }
        plazasOcupadas--;
    }

    public double calcularIngresosPrevistos() {
        return plazasOcupadas * precio;
    }

    public String toCsvLine() {
        return id + ";" + nombre + ";" + tipoActividad + ";" + duracionMinutos + ";"
                + precio + ";" + plazasMaximas + ";" + plazasOcupadas;
    }

    public static Actividad fromCsvLine(String linea) {
        String[] partes = linea.split(";");
        return new Actividad(
                Integer.parseInt(partes[0]),
                partes[1],
                partes[2],
                Integer.parseInt(partes[3]),
                Double.parseDouble(partes[4]),
                Integer.parseInt(partes[5]),
                Integer.parseInt(partes[6]));
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public double getPrecio() {
        return precio;
    }

    public int getPlazasMaximas() {
        return plazasMaximas;
    }

    public int getPlazasOcupadas() {
        return plazasOcupadas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setPlazasMaximas(int plazasMaximas) {
        this.plazasMaximas = plazasMaximas;
    }

    public void setPlazasOcupadas(int plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + tipoActividad + ")";
    }
}
