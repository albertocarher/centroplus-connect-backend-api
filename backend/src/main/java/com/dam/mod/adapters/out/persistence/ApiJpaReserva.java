package com.dam.mod.adapters.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class ApiJpaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private ApiJpaUsuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad", nullable = false)
    private ApiJpaActividad actividad;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String estado;

    @Transient
    private String nombreActividad;

    public ApiJpaReserva() {
    }

    public ApiJpaReserva(Long id, ApiJpaUsuario usuario, ApiJpaActividad actividad,
            LocalDate fecha, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.actividad = actividad;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public ApiJpaUsuario getUsuario() {
        return usuario;
    }

    public ApiJpaActividad getActividad() {
        return actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(ApiJpaUsuario usuario) {
        this.usuario = usuario;
    }

    public void setActividad(ApiJpaActividad actividad) {
        this.actividad = actividad;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }
}
