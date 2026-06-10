package com.dam.mod.adapters.in.api;

public class LoginRequest {

    private String dni;
    private String password;

    public LoginRequest() {}

    public String getDni() { return dni; }
    public String getPassword() { return password; }

    public void setDni(String dni) { this.dni = dni; }
    public void setPassword(String password) { this.password = password; }
}
