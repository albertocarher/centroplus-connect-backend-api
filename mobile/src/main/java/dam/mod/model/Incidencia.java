package dam.mod.models;

import java.time.LocalDate;

public class Incidencia {

    private int id;
    private int id_usuario;
    private String asunto;
    private String descripcion;
    private LocalDate fecha;
    private String estado;

    public Incidencia (int id, int id_usuario, String asunto, String descripcion, LocalDate fecha, String estado ){
        this.id = id;
        this.id_usuario = id_usuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }
    public int getId_usuario() {
        return id_usuario;
    }
    public String getAsunto() {
        return asunto;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public String getEstado() {
        return estado;
    }

    

}
