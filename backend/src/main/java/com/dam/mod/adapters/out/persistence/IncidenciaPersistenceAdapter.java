package com.dam.mod.adapters.out.persistence;

import com.dam.mod.adapters.mappers.IncidenciaMapper;
import com.dam.mod.domain.models.Incidencia;
import com.dam.mod.domain.ports.IIncidenciaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IncidenciaPersistenceAdapter implements IIncidenciaRepository {

    private final IncidenciaRepositoryJpa repositoryJpa;
    private final UsuarioRepositoryJpa usuarioRepositoryJpa;
    private final IncidenciaMapper mapper;

    public IncidenciaPersistenceAdapter(IncidenciaRepositoryJpa repositoryJpa,
                                        UsuarioRepositoryJpa usuarioRepositoryJpa,
                                        IncidenciaMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.usuarioRepositoryJpa = usuarioRepositoryJpa;
        this.mapper = mapper;
    }

    @Override
    public List<Incidencia> findAll() {
        return repositoryJpa.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Incidencia findById(int id) {
        return repositoryJpa.findById((long) id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public boolean save(Incidencia incidencia) {
        ApiJpaUsuario usuario = usuarioRepositoryJpa.findById((long) incidencia.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + incidencia.getIdUsuario()));
        repositoryJpa.save(mapper.toEntity(incidencia, usuario));
        return true;
    }

    @Override
    public boolean update(Incidencia incidencia) {
        if (!repositoryJpa.existsById((long) incidencia.getId())) return false;
        ApiJpaUsuario usuario = usuarioRepositoryJpa.findById((long) incidencia.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + incidencia.getIdUsuario()));
        repositoryJpa.save(mapper.toEntity(incidencia, usuario));
        return true;
    }

    @Override
    public boolean delete(int id) {
        if (!repositoryJpa.existsById((long) id)) return false;
        repositoryJpa.deleteById((long) id);
        return true;
    }
}
