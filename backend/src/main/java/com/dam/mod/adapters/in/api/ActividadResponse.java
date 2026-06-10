package com.dam.mod.adapters.in.api;

public class ActividadResponse {

    private int id;
    private String nombre;
    private String tipoActividad;
    private int duracion;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;
    private int plazasDisponibles;

    public ActividadResponse() {}

    public ActividadResponse(int id, String nombre, String tipoActividad,
                             int duracion, double precio,
                             int plazasMaximas, int plazasOcupadas) {
        this.id = id;
        this.nombre = nombre;
        this.tipoActividad = tipoActividad;
        this.duracion = duracion;
        this.precio = precio;
        this.plazasMaximas = plazasMaximas;
        this.plazasOcupadas = plazasOcupadas;
        this.plazasDisponibles = plazasMaximas - plazasOcupadas;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipoActividad() { return tipoActividad; }
    public int getDuracion() { return duracion; }
    public double getPrecio() { return precio; }
    public int getPlazasMaximas() { return plazasMaximas; }
    public int getPlazasOcupadas() { return plazasOcupadas; }
    public int getPlazasDisponibles() { return plazasDisponibles; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipoActividad(String tipoActividad) { this.tipoActividad = tipoActividad; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setPlazasMaximas(int plazasMaximas) { this.plazasMaximas = plazasMaximas; }
    public void setPlazasOcupadas(int plazasOcupadas) { this.plazasOcupadas = plazasOcupadas; }
    public void setPlazasDisponibles(int plazasDisponibles) { this.plazasDisponibles = plazasDisponibles; }
}
