package com.dam.mod.business;

import com.dam.mod.domain.ports.IReservaRepository;
import com.dam.mod.domain.models.Reserva;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService implements ReservaServicePort {

    private final IReservaRepository repository;

    public ReservaService(IReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Reserva> findAll() {
        return repository.findAll();
    }

    @Override
    public Reserva findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean save(Reserva reserva) {
        if (repository.existsReserva(reserva.getIdActividad(), reserva.getIdUsuario())) {
            throw new RuntimeException("El usuario ya tiene una reserva para esta actividad");
        }
        return repository.save(reserva);
    }

    @Override
    public boolean update(Reserva reserva) {
        if (repository.findById(reserva.getId()) == null) {
            throw new RuntimeException("Reserva no encontrada: " + reserva.getId());
        }
        return repository.update(reserva);
    }

    @Override
    public boolean delete(int id) {
        if (repository.findById(id) == null) {
            throw new RuntimeException("Reserva no encontrada: " + id);
        }
        return repository.delete(id);
    }

    @Override
    public boolean existsReserva(int actividadId, int usuarioId) {
        return repository.existsReserva(actividadId, usuarioId);
    }

    @Override
    public List<Reserva> findByIdUsuario(int idUsuario) {
        return repository.findByIdUsuario(idUsuario);
    }

    @Override
    public boolean cambiarEstado(int idReserva, String nuevoEstado) {
        if (repository.findById(idReserva) == null) {
            throw new RuntimeException("Reserva no encontrada: " + idReserva);
        }
        return repository.cambiarEstado(idReserva, nuevoEstado);
    }
}
