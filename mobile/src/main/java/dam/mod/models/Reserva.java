package dam.mod.models;

import java.time.LocalDate;

public class Reserva {

    private int id;
    private int idUsuario;
    private int idActividad;
    private LocalDate fecha;
    private String estado;

    // NUEVO: para mostrar el nombre en UI
    private String nombreActividad;

    public Reserva(int id, int idUsuario, int idActividad,
                   LocalDate fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.fecha = fecha;
        this.estado = estado;
    }

    // GETTERS
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

    // SETTER necesario para el JOIN en el repository
    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    @Override
    public String toString() {

        // Esto es lo que verá el ListView
        return nombreActividad +
                " | Fecha: " + fecha +
                " | Estado: " + estado;
    }
}