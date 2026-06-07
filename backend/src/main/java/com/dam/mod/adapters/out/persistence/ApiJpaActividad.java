package com.dam.mod.adapters.out.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "actividades")
public class ApiJpaActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "tipo_actividad", nullable = false)
    private String tipoActividad;

    @Column(nullable = false)
    private int duracion;

    @Column(nullable = false)
    private double precio;

    @Column(name = "plazas_maximas", nullable = false)
    private int plazasMaximas;

    @Column(name = "plazas_ocupadas", nullable = false)
    private int plazasOcupadas;

    public ApiJpaActividad() {
    }

    public ApiJpaActividad(Long id, String nombre, String tipoActividad,
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

    public int getPlazasDisponibles() {
        return plazasMaximas - plazasOcupadas;
    }

    // Getters
    public Long getId() {
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

    // Setters
    public void setId(Long id) {
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

    public void setPlazasOcupadas(int plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
    }
}
