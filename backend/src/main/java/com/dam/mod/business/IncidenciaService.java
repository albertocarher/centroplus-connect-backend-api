package com.dam.mod.business;

import com.dam.mod.domain.ports.IIncidenciaRepository;
import com.dam.mod.domain.models.Incidencia;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidenciaService implements IncidenciaServicePort {

    private final IIncidenciaRepository repository;

    public IncidenciaService(IIncidenciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Incidencia> findAll() {
        return repository.findAll();
    }

    @Override
    public Incidencia findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean save(Incidencia incidencia) {
        return repository.save(incidencia);
    }

    @Override
    public boolean update(Incidencia incidencia) {
        if (repository.findById(incidencia.getId()) == null) {
            throw new RuntimeException("Incidencia no encontrada: " + incidencia.getId());
        }
        return repository.update(incidencia);
    }

    @Override
    public boolean delete(int id) {
        if (repository.findById(id) == null) {
            throw new RuntimeException("Incidencia no encontrada: " + id);
        }
        return repository.delete(id);
    }
}
