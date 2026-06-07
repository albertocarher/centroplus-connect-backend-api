package com.dam.mod.adapters.out.persistence;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.Optional;
 
@Repository
public interface UsuarioRepositoryJpa extends JpaRepository<ApiJpaUsuario, Long> {
 
    Optional<ApiJpaUsuario> findByDni(String dni);
 
    Optional<ApiJpaUsuario> findByEmail(String email);
 
    boolean existsByDni(String dni);
}