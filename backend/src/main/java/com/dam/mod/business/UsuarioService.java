package com.dam.mod.business;

import com.dam.mod.domain.ports.IUsuarioRepository;
import com.dam.mod.domain.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UsuarioServicePort {

    private final IUsuarioRepository repository;

    public UsuarioService(IUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Usuario findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean save(Usuario usuario) {
        if (repository.findByDni(usuario.getDni()) != null) {
            throw new RuntimeException("Ya existe un usuario con el DNI: " + usuario.getDni());
        }
        return repository.save(usuario);
    }

    @Override
    public boolean update(Usuario usuario) {
        if (repository.findById(usuario.getId()) == null) {
            throw new RuntimeException("Usuario no encontrado: " + usuario.getId());
        }
        return repository.update(usuario);
    }

    @Override
    public boolean delete(int id) {
        if (repository.findById(id) == null) {
            throw new RuntimeException("Usuario no encontrado: " + id);
        }
        return repository.delete(id);
    }

    @Override
    public Usuario login(String dni, String password) {
        Usuario usuario = repository.login(dni, password);
        if (usuario == null) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        return usuario;
    }

    @Override
    public Usuario findByDni(String dni) {
        return repository.findByDni(dni);
    }
}
