package dam.mod.models;

import java.time.LocalDate;

import dam.mod.utils.Validaciones;

public class Reserva {

    private int id;
    private int idUsuario;
    private int idActividad;
    private LocalDate fecha;
    private String estado;

    private String nombreActividad;

    public Reserva() {
    }

    public Reserva(int id, int idUsuario, int idActividad,
            LocalDate fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdActividad() {
        return idActividad;
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

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {

        return nombreActividad +" | "+ fecha +" | "+ estado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Reserva other = (Reserva) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public void setEstado(String estado) {
        Validaciones.validarEstadoReserva(estado);
        this.estado = estado;
    }

}