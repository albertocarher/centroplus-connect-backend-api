package dam.mod.repositories;

import dam.mod.models.Actividad;
import java.util.List;

public interface IActividadRepository {

    /**
     * Funcion que encuentra todas las actividades
     * @return Todas las actividades
     */
    List<Actividad> findAll();

    /**
     * Funcion que busca una actividad por su id
     * @param id id de la actividad
     * @return
     */
    Actividad findById(int id);

    /**
     * Funcion que guarda una actividad
     * @param actividad actividad a guardar
     * @return
     */
    boolean save(Actividad actividad);

    /**
     * Funcion que actualiza la actividad
     * @param actividad
     * @return
     */
    boolean update(Actividad actividad);
    boolean delete(int id);
}