package com.dam.mod.business;

import com.dam.mod.domain.models.Reserva;
import java.util.List;

public interface ReservaServicePort {
    List<Reserva> findAll();
    Reserva findById(int id);
    boolean save(Reserva reserva);
    boolean update(Reserva reserva);
    boolean delete(int id);
    boolean existsReserva(int actividadId, int usuarioId);
    List<Reserva> findByIdUsuario(int idUsuario);
    boolean cambiarEstado(int idReserva, String nuevoEstado);
}
