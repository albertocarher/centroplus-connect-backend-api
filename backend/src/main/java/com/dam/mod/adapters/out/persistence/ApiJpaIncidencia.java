package com.dam.mod.adapters.out.persistence;
 
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

import jakarta.persistence.*;
 
@Entity
@Table(name = "incidencias")
public class ApiJpaIncidencia {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private ApiJpaUsuario usuario;
 
    @Column(nullable = false)
    private String asunto;
 
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
 
    @Column(nullable = false)
@JdbcTypeCode(Types.VARCHAR)
private String fecha;
 
    @Column(nullable = false)
    private String estado;
 
    public ApiJpaIncidencia() {}
 
    public ApiJpaIncidencia(Integer id, ApiJpaUsuario usuario, String asunto,
                            String descripcion, String fecha, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = estado;
    }
 
    public Integer getId() { return id; }
    public ApiJpaUsuario getUsuario() { return usuario; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }
 
    public void setId(Integer id) { this.id = id; }
    public void setUsuario(ApiJpaUsuario usuario) { this.usuario = usuario; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }
}
