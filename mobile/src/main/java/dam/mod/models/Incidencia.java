package dam.mod.models;

import java.time.LocalDate;
import java.util.Objects;

public class Incidencia {

    private int id;
    private int idUsuario;
    private String asunto;
    private String descripcion;
    private LocalDate fecha;
    private String estado;

    /** 
     * Constructor por defecto
    */
    public Incidencia() {

    }

    public Incidencia(int id){
        this.id = id;
    }

    public Incidencia(int id, String asunto,
                      String descripcion, LocalDate fecha) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    /**
     * Construcor de la clase incidencia
     * @param id de la clase incidencia
     * @param idUsuario de la clase incidencia
     * @param asunto de la clase incidencia
     * @param descripcion de la clase incidencia
     * @param fecha de la clase incidencia
     * @param estado de la clase incidencia
     */
    public Incidencia(int id, int idUsuario, String asunto,
                      String descripcion, LocalDate fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Integer.valueOf(id));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Incidencia other = (Incidencia) obj;
        return id == other.id;
    }

    
}