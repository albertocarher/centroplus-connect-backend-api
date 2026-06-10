package com.dam.mod.adapters.in.api;

import java.time.LocalDate;

public class ReservaRequest {

    private int idUsuario;
    private int idActividad;
    private LocalDate fecha;
    private String estado;

    public ReservaRequest() {}

    public int getIdUsuario() { return idUsuario; }
    public int getIdActividad() { return idActividad; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }

    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setIdActividad(int idActividad) { this.idActividad = idActividad; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }
}
