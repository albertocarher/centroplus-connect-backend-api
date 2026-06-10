package com.dam.mod.adapters.mappers;
 
import com.dam.mod.adapters.out.persistence.ApiJpaActividad;
import com.dam.mod.adapters.out.persistence.ApiJpaReserva;
import com.dam.mod.adapters.out.persistence.ApiJpaUsuario;
import com.dam.mod.domain.models.Reserva;
import org.springframework.stereotype.Component;
 
import java.time.LocalDate;
 
@Component
public class ReservaMapper {
 
    public Reserva toDomain(ApiJpaReserva entity) {
        if (entity == null) return null;
        Reserva reserva = new Reserva(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getActividad().getId(),
                entity.getFecha() != null ? LocalDate.parse(entity.getFecha()) : null,
                entity.getEstado()
        );
        reserva.setNombreActividad(entity.getActividad().getNombre());
        return reserva;
    }
 
    public ApiJpaReserva toEntity(Reserva domain, ApiJpaUsuario usuario, ApiJpaActividad actividad) {
        if (domain == null) return null;
        ApiJpaReserva entity = new ApiJpaReserva();
        if (domain.getId() != 0) entity.setId(domain.getId());
        entity.setUsuario(usuario);
        entity.setActividad(actividad);
        entity.setFecha(domain.getFecha() != null ? domain.getFecha().toString() : null);
        entity.setEstado(domain.getEstado());
        return entity;
    }
}
