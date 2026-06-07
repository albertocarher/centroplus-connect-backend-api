package com.dam.mod.adapters.out.persistence;

import com.dam.mod.adapters.mappers.ActividadMapper;
import com.dam.mod.domain.models.Actividad;
import com.dam.mod.domain.ports.IActividadRepository;



import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActividadPersistenceAdapter implements IActividadRepository {

    private final ActividadRepositoryJpa repositoryJpa;
    private final ActividadMapper mapper;

    public ActividadPersistenceAdapter(ActividadRepositoryJpa repositoryJpa, ActividadMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.mapper = mapper;
    }

    @Override
    public List<Actividad> findAll() {
        return repositoryJpa.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Actividad findById(int id) {
        return repositoryJpa.findById((long) id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public boolean save(Actividad actividad) {
        repositoryJpa.save(mapper.toEntity(actividad));
        return true;
    }

    @Override
    public boolean update(Actividad actividad) {
        if (!repositoryJpa.existsById((long) actividad.getId())) return false;
        repositoryJpa.save(mapper.toEntity(actividad));
        return true;
    }

    @Override
    public boolean delete(int id) {
        if (!repositoryJpa.existsById((long) id)) return false;
        repositoryJpa.deleteById((long) id);
        return true;
    }
}
