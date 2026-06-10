package com.dam.mod.adapters.mappers;

import com.dam.mod.adapters.out.persistence.ApiJpaActividad;
import com.dam.mod.domain.models.Actividad;
import org.springframework.stereotype.Component;

@Component
public class ActividadMapper {

    public Actividad toDomain(ApiJpaActividad entity) {
        if (entity == null) return null;
        return new Actividad(
                entity.getId(),
                entity.getNombre(),
                entity.getTipoActividad(),
                entity.getDuracion(),
                entity.getPrecio(),
                entity.getPlazasMaximas(),
                entity.getPlazasOcupadas()
        );
    }

    public ApiJpaActividad toEntity(Actividad domain) {
        if (domain == null) return null;
        ApiJpaActividad entity = new ApiJpaActividad();
        if (domain.getId() != 0) entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setTipoActividad(domain.getTipoActividad());
        entity.setDuracion(domain.getDuracion());
        entity.setPrecio(domain.getPrecio());
        entity.setPlazasMaximas(domain.getPlazasMaximas());
        entity.setPlazasOcupadas(domain.getPlazasOcupadas());
        return entity;
    }
}
