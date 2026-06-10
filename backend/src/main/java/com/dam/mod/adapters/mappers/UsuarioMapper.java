package com.dam.mod.adapters.mappers;

import com.dam.mod.adapters.out.persistence.ApiJpaUsuario;
import com.dam.mod.domain.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toDomain(ApiJpaUsuario entity) {
        if (entity == null) return null;
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getDni(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getTipoUsuario(),
                entity.getPassword()
        );
    }

    public ApiJpaUsuario toEntity(Usuario domain) {
        if (domain == null) return null;
        ApiJpaUsuario entity = new ApiJpaUsuario();
        if (domain.getId() != 0) entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setDni(domain.getDni());
        entity.setEmail(domain.getEmail());
        entity.setTelefono(domain.getTelefono());
        entity.setTipoUsuario(domain.getTipoUsuario());
        entity.setPassword(domain.getPassword());
        return entity;
    }
}
