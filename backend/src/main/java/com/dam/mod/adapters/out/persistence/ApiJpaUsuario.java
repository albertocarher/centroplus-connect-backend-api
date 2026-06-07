package com.dam.mod.adapters.out.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class ApiJpaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true, length = 9)
    private String dni;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 9)
    private String telefono;

    @Column(name = "tipo_usuario", nullable = false)
    private String tipoUsuario;

    @Column(nullable = false)
    private String password;

    public ApiJpaUsuario() {
    }

    public ApiJpaUsuario(Long id, String nombre, String dni, String email,
            String telefono, String tipoUsuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
        this.password = password;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;

    }

    public String getTelefono() {
        return telefono;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}