package dam.mod.models;

public class Actividad {

    private int id;
    private String nombre;
    private String tipoActividad;
    private int duracion;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;

    public Actividad(int id, String nombre, String tipoActividad,
                     int duracion, double precio,
                     int plazasMaximas, int plazasOcupadas) {
        this.id = id;
        this.nombre = nombre;
        this.tipoActividad = tipoActividad;
        this.duracion = duracion;
        this.precio = precio;
        this.plazasMaximas = plazasMaximas;
        this.plazasOcupadas = plazasOcupadas;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipoActividad() { return tipoActividad; }
    public int getDuracion() { return duracion; }
    public double getPrecio() { return precio; }
    public int getPlazasMaximas() { return plazasMaximas; }
    public int getPlazasOcupadas() { return plazasOcupadas; }

    public void setPlazasOcupadas(int plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
    }

    @Override
    public String toString() {
        return nombre + " - " + tipoActividad;
    }
}