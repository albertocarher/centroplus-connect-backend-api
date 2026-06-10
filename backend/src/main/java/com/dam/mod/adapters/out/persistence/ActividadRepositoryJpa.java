package com.dam.mod.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepositoryJpa extends JpaRepository<ApiJpaActividad, Integer> {

    List<ApiJpaActividad> findByTipoActividad(String tipoActividad);
}
