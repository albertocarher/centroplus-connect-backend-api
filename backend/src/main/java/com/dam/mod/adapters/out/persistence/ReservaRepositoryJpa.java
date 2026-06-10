package com.dam.mod.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReservaRepositoryJpa extends JpaRepository<ApiJpaReserva, Integer> {

    List<ApiJpaReserva> findByUsuarioId(Integer idUsuario);

    List<ApiJpaReserva> findByActividadId(Integer idActividad);

    boolean existsByActividadIdAndUsuarioId(Integer idActividad, Integer idUsuario);

    @Modifying
    @Transactional
    @Query("UPDATE ApiJpaReserva r SET r.estado = :estado WHERE r.id = :id")
    int cambiarEstado(@Param("id") Integer id, @Param("estado") String estado);
}
