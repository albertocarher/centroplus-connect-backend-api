package com.dam.mod.business;

import com.dam.mod.domain.models.Incidencia;
import java.util.List;

public interface IncidenciaServicePort {
    List<Incidencia> findAll();
    Incidencia findById(int id);
    boolean save(Incidencia incidencia);
    boolean update(Incidencia incidencia);
    boolean delete(int id);
}
