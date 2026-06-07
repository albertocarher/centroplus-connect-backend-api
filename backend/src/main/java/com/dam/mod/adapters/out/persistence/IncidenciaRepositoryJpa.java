package com.dam.mod.adapters.out.persistence;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface IncidenciaRepositoryJpa extends JpaRepository<ApiJpaIncidencia, Long> {

    List<ApiJpaIncidencia> findByUsuarioId(Long idUsuario);

    List<ApiJpaIncidencia> findByEstado(String estado);
}
