package com.dam.mod.adapters.out.persistence;

import com.dam.mod.adapters.mappers.UsuarioMapper;
import com.dam.mod.domain.models.Usuario;
import com.dam.mod.domain.ports.IUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioPersistenceAdapter implements IUsuarioRepository {

    private final UsuarioRepositoryJpa repositoryJpa;
    private final UsuarioMapper mapper;

    public UsuarioPersistenceAdapter(UsuarioRepositoryJpa repositoryJpa, UsuarioMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.mapper = mapper;
    }

    @Override
    public List<Usuario> findAll() {
        return repositoryJpa.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Usuario findById(int id) {
        return repositoryJpa.findById((long) id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public boolean save(Usuario usuario) {
        repositoryJpa.save(mapper.toEntity(usuario));
        return true;
    }

    @Override
    public boolean update(Usuario usuario) {
        if (!repositoryJpa.existsById((long) usuario.getId())) return false;
        repositoryJpa.save(mapper.toEntity(usuario));
        return true;
    }

    @Override
    public boolean delete(int id) {
        if (!repositoryJpa.existsById((long) id)) return false;
        repositoryJpa.deleteById((long) id);
        return true;
    }

    @Override
    public Usuario login(String dni, String password) {
        return repositoryJpa.findByDni(dni)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Usuario findByDni(String dni) {
        return repositoryJpa.findByDni(dni)
                .map(mapper::toDomain)
                .orElse(null);
    }
}
