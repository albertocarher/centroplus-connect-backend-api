package dam.mod.services;

import java.util.List;

import dam.mod.models.Actividad;

public interface IActividadService {

    /**
     * Devuelve una lista de todas las actividades disponibles.
     *
     * @return Lista de actividades.
     */
    List<Actividad> findAll();

    /**
     * Devuelve una actividad por su ID.
     *
     * @param id ID de la actividad.
     * @return Actividad encontrada o null si no se encuentra.
     */
    Actividad findById(int id);

    /**
     * Crea una nueva actividad.
     *
     * @param actividad Actividad a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    boolean create(Actividad actividad);

    /**
     * Actualiza una actividad existente.
     *
     * @param actividad Actividad a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    boolean update(Actividad actividad);

    /**
     * Elimina una actividad por su ID.
     *
     * @param id ID de la actividad.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    boolean delete(int id);

    /**
     * Reserva una plaza en una actividad.
     *
     * @param idActividad ID de la actividad.
     * @return true si la reserva fue exitosa, false en caso contrario.
     */
    boolean reservarPlaza(int idActividad);

    /**
     * Cancela la reserva de una plaza en una actividad.
     *
     * @param idActividad ID de la actividad.
     * @return true si la cancelación fue exitosa, false en caso contrario.
     */
    boolean cancelarPlaza(int idActividad);

    /**
     * Devuelve una lista de todas las actividades completas.
     *
     * @return Lista de actividades completas.
     */
    List<Actividad> findCompletas();

    /**
     * Calcula el número de plazas disponibles para una actividad.
     *
     * @param idActividad ID de la actividad.
     * @return Número de plazas disponibles.
     */
    int calcularPlazasDisponibles(int idActividad);

    /**
     * Calcula los ingresos totales generados por todas las actividades.
     *
     * @return Ingresos totales.
     */
    double calcularIngresosTotales();
}
