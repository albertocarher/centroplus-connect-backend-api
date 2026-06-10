package com.dam.mod.adapters.in.api;

import java.time.LocalDate;

public class IncidenciaRequest {

    private int idUsuario;
    private String asunto;
    private String descripcion;
    private LocalDate fecha;
    private String estado;

    public IncidenciaRequest() {}

    public int getIdUsuario() { return idUsuario; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }

    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }
}
