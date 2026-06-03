package dam.mod.models;

import dam.mod.utils.Validaciones;

public class Usuario {

    private int id;
    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private String tipoUsuario;
    private String password;

    public Usuario (){}

    public Usuario(int id, String nombre, String dni, String email, String telefono, String tipoUsuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
        this.password = password;
    }

    public int getId() {
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

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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
        Usuario other = (Usuario) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public void setEmail(String email) {
        Validaciones.validarEmail(email);
        this.email = email;
    }

    public void setTelefono(String telefono) {
        Validaciones.validarTelefono(telefono);
        this.telefono = telefono;
    }
    
}
