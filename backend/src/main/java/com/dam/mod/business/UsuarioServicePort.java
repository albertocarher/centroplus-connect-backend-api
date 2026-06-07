package com.dam.mod.business;

import com.dam.mod.domain.models.Usuario;
import java.util.List;

public interface UsuarioServicePort {
    List<Usuario> findAll();
    Usuario findById(int id);
    boolean save(Usuario usuario);
    boolean update(Usuario usuario);
    boolean delete(int id);
    Usuario login(String dni, String password);
    Usuario findByDni(String dni);
}
