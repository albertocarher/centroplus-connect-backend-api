package com.dam.mod.adapters.out.persistence;

import java.sql.Types;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.*;

@Entity
@Table(name = "reservas")
public class ApiJpaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private ApiJpaUsuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad", nullable = false)
    private ApiJpaActividad actividad;

    @Column(nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private String fecha;

    @Column(nullable = false)
    private String estado;

    @Transient
    private String nombreActividad;

    public ApiJpaReserva() {
    }

    public ApiJpaReserva(Integer id, ApiJpaUsuario usuario, ApiJpaActividad actividad,
            String fecha, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.actividad = actividad;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public ApiJpaUsuario getUsuario() {
        return usuario;
    }

    public ApiJpaActividad getActividad() {
        return actividad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsuario(ApiJpaUsuario usuario) {
        this.usuario = usuario;
    }

    public void setActividad(ApiJpaActividad actividad) {
        this.actividad = actividad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }
}
