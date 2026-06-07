package com.dam.mod.adapters.out.persistence;

import com.dam.mod.adapters.mappers.ReservaMapper;
import com.dam.mod.domain.models.Reserva;
import com.dam.mod.domain.ports.IReservaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservaPersistenceAdapter implements IReservaRepository {

    private final ReservaRepositoryJpa repositoryJpa;
    private final UsuarioRepositoryJpa usuarioRepositoryJpa;
    private final ActividadRepositoryJpa actividadRepositoryJpa;
    private final ReservaMapper mapper;

    public ReservaPersistenceAdapter(ReservaRepositoryJpa repositoryJpa,
                                     UsuarioRepositoryJpa usuarioRepositoryJpa,
                                     ActividadRepositoryJpa actividadRepositoryJpa,
                                     ReservaMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.usuarioRepositoryJpa = usuarioRepositoryJpa;
        this.actividadRepositoryJpa = actividadRepositoryJpa;
        this.mapper = mapper;
    }

    @Override
    public List<Reserva> findAll() {
        return repositoryJpa.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Reserva findById(int id) {
        return repositoryJpa.findById((long) id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public boolean save(Reserva reserva) {
        ApiJpaUsuario usuario = usuarioRepositoryJpa.findById((long) reserva.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + reserva.getIdUsuario()));
        ApiJpaActividad actividad = actividadRepositoryJpa.findById((long) reserva.getIdActividad())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada: " + reserva.getIdActividad()));
        repositoryJpa.save(mapper.toEntity(reserva, usuario, actividad));
        return true;
    }

    @Override
    public boolean update(Reserva reserva) {
        if (!repositoryJpa.existsById((long) reserva.getId())) return false;
        ApiJpaUsuario usuario = usuarioRepositoryJpa.findById((long) reserva.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + reserva.getIdUsuario()));
        ApiJpaActividad actividad = actividadRepositoryJpa.findById((long) reserva.getIdActividad())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada: " + reserva.getIdActividad()));
        repositoryJpa.save(mapper.toEntity(reserva, usuario, actividad));
        return true;
    }

    @Override
    public boolean delete(int id) {
        if (!repositoryJpa.existsById((long) id)) return false;
        repositoryJpa.deleteById((long) id);
        return true;
    }

    @Override
    public boolean existsReserva(int actividadId, int usuarioId) {
        return repositoryJpa.existsByActividadIdAndUsuarioId((long) actividadId, (long) usuarioId);
    }

    @Override
    public List<Reserva> findByIdUsuario(int idUsuario) {
        return repositoryJpa.findByUsuarioId((long) idUsuario)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean cambiarEstado(int idReserva, String nuevoEstado) {
        return repositoryJpa.cambiarEstado((long) idReserva, nuevoEstado) > 0;
    }
}
