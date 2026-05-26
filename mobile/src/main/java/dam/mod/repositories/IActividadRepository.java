package dam.mod.repositories;

import dam.mod.models.Actividad;
import java.util.List;

public interface IActividadRepository {
    List<Actividad> findAll();
    Actividad findById(int id);
    boolean save(Actividad actividad);
    boolean update(Actividad actividad);
    boolean delete(int id);
}