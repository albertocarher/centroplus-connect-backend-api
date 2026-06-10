package com.dam.mod.adapters.in.api;

public class ActividadRequest {

    private String nombre;
    private String tipoActividad;
    private int duracion;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;

    public ActividadRequest() {}

    public String getNombre() { return nombre; }
    public String getTipoActividad() { return tipoActividad; }
    public int getDuracion() { return duracion; }
    public double getPrecio() { return precio; }
    public int getPlazasMaximas() { return plazasMaximas; }
    public int getPlazasOcupadas() { return plazasOcupadas; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipoActividad(String tipoActividad) { this.tipoActividad = tipoActividad; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setPlazasMaximas(int plazasMaximas) { this.plazasMaximas = plazasMaximas; }
    public void setPlazasOcupadas(int plazasOcupadas) { this.plazasOcupadas = plazasOcupadas; }
}
