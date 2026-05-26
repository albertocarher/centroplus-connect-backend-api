package dam.mod.services;

import java.util.List;

import dam.mod.models.Reserva;

public interface IReservaService {

    /**
     * Devuelve una lista de todas las reservas.
     *
     * @return Lista de reservas.
     */
    List<Reserva> findAll();

    /**
     * Devuelve una reserva por su ID.
     *
     * @param id ID de la reserva.
     * @return Reserva encontrada o null si no se encuentra.
     */
    Reserva findById(int id);

    /**
     * Crea una nueva reserva.
     *
     * @param reserva Reserva a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    boolean create(Reserva reserva);

    /**
     * Actualiza una reserva existente.
     *
     * @param reserva Reserva a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    boolean update(Reserva reserva);

    /**
     * Elimina una reserva por su ID.
     *
     * @param id ID de la reserva.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    boolean delete(int id);

    /**
     * Cancela una reserva por su ID.
     *
     * @param idReserva ID de la reserva a cancelar.
     * @return true si la cancelación fue exitosa, false en caso contrario.
     */
    boolean cancelarReserva(int idReserva);

    /**
     * Verifica si un usuario ya tiene una reserva para una actividad
     * específica.
     *
     * @param idUsuario ID del usuario.
     * @param idActividad ID de la actividad.
     * @return true si el usuario ya tiene una reserva para la actividad, false
     * en caso contrario.
     */
    boolean existeReservaUsuarioActividad(int idUsuario, int idActividad);

    /**
     * Comprueba si hay plazas disponibles para una actividad específica.
     *
     * @param idActividad ID de la actividad.
     * @return true si hay plazas disponibles, false en caso contrario.
     */
    boolean comprobarPlazasDisponibles(int idActividad);
}
