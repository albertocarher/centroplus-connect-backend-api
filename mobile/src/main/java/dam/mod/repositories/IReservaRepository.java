package dam.mod.repositories;

import dam.mod.models.Reserva;
import java.util.List;

public interface IReservaRepository {
    List<Reserva> findAll();
    Reserva findById(int id);
    boolean save(Reserva reserva);
    boolean update(Reserva reserva);
    boolean delete(int id);
}