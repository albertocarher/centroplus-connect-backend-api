package dam.mod.models;

import java.time.LocalDate;

public class Reserva {

    private int id;
    private int idUsuario;
    private int idActividad;
    private LocalDate fecha;
    private String estado;

    public Reserva(int id, int idUsuario, int idActividad,
                   LocalDate fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdActividad() { return idActividad; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return "Actividad ID: " + idActividad +
                " | Fecha: " + fecha +
                " | Estado: " + estado;
    }
}