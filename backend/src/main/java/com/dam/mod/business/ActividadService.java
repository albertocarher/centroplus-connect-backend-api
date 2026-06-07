package com.dam.mod.business;

import com.dam.mod.domain.ports.IActividadRepository;
import com.dam.mod.domain.models.Actividad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService implements ActividadServicePort {

    private final IActividadRepository repository;

    public ActividadService(IActividadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Actividad> findAll() {
        return repository.findAll();
    }

    @Override
    public Actividad findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean save(Actividad actividad) {
        if (actividad.getPlazasOcupadas() > actividad.getPlazasMaximas()) {
            throw new RuntimeException("Las plazas ocupadas no pueden superar las plazas máximas");
        }
        return repository.save(actividad);
    }

    @Override
    public boolean update(Actividad actividad) {
        if (repository.findById(actividad.getId()) == null) {
            throw new RuntimeException("Actividad no encontrada: " + actividad.getId());
        }
        return repository.update(actividad);
    }

    @Override
    public boolean delete(int id) {
        if (repository.findById(id) == null) {
            throw new RuntimeException("Actividad no encontrada: " + id);
        }
        return repository.delete(id);
    }
}
