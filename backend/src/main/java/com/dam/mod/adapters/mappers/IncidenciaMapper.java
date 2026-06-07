package com.dam.mod.adapters.mappers;

import com.dam.mod.adapters.out.persistence.ApiJpaIncidencia;
import com.dam.mod.adapters.out.persistence.ApiJpaUsuario;
import com.dam.mod.domain.models.Incidencia;
import org.springframework.stereotype.Component;

@Component
public class IncidenciaMapper {

    public Incidencia toDomain(ApiJpaIncidencia entity) {
        if (entity == null) return null;
        return new Incidencia(
                entity.getId().intValue(),
                entity.getUsuario().getId().intValue(),
                entity.getAsunto(),
                entity.getDescripcion(),
                entity.getFecha(),
                entity.getEstado()
        );
    }

    public ApiJpaIncidencia toEntity(Incidencia domain, ApiJpaUsuario usuario) {
        if (domain == null) return null;
        ApiJpaIncidencia entity = new ApiJpaIncidencia();
        if (domain.getId() != 0) entity.setId((long) domain.getId());
        entity.setUsuario(usuario);
        entity.setAsunto(domain.getAsunto());
        entity.setDescripcion(domain.getDescripcion());
        entity.setFecha(domain.getFecha());
        entity.setEstado(domain.getEstado());
        return entity;
    }
}
