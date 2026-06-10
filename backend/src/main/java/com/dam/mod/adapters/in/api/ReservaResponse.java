package com.dam.mod.adapters.in.api;

import java.time.LocalDate;

public class ReservaResponse {

    private int id;
    private int idUsuario;
    private int idActividad;
    private LocalDate fecha;
    private String estado;
    private String nombreActividad;

    public ReservaResponse() {}

    public ReservaResponse(int id, int idUsuario, int idActividad,
                           LocalDate fecha, String estado, String nombreActividad) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.fecha = fecha;
        this.estado = estado;
        this.nombreActividad = nombreActividad;
    }

    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdActividad() { return idActividad; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getNombreActividad() { return nombreActividad; }

    public void setId(int id) { this.id = id; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setIdActividad(int idActividad) { this.idActividad = idActividad; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setNombreActividad(String nombreActividad) { this.nombreActividad = nombreActividad; }
}
