package dam.mod.repositories;

import dam.mod.models.Incidencia;
import java.util.List;

public interface IIncidenciaRepository {
    List<Incidencia> findAll();
    Incidencia findById(int id);
    boolean save(Incidencia incidencia);
    boolean update(Incidencia incidencia);
    boolean delete(int id);
}