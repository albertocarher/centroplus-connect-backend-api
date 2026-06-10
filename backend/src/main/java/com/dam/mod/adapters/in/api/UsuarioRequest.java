package com.dam.mod.adapters.in.api;

public class UsuarioRequest {

    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private String tipoUsuario;
    private String password;

    public UsuarioRequest() {}

    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getTipoUsuario() { return tipoUsuario; }
    public String getPassword() { return password; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDni(String dni) { this.dni = dni; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public void setPassword(String password) { this.password = password; }
}
