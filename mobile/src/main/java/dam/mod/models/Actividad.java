package dam.mod.models;

import java.util.Objects;

public class Actividad {

    public Actividad() {
    }

    public Actividad(int id) {
        this.id = id;
    }

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

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public int getDuracion() {
        return duracion;
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

    public void setPlazasOcupadas(int plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
    }

    public int plazasDisponibles() {
        return plazasMaximas - plazasOcupadas;
    }

    @Override
    public String toString() {
        return nombre + " - " + tipoActividad;
    }

    

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setPlazasMaximas(int plazasMaximas) {
        this.plazasMaximas = plazasMaximas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Integer.valueOf(id));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Actividad other = (Actividad) obj;
        return id == other.id;
    }

}