package dam.mod.services;

import java.util.List;
import dam.mod.models.Reserva;


/**
 * Interfaz que define las operaciones de negocio relacionadas con reservas.
 */
public interface IReservaService {

    /**
     * Devuelve todas las reservas del sistema.
     *
     * @return lista de reservas
     */
    List<Reserva> findAll();

    /**
     * Busca una reserva por su identificador.
     *
     * @param id ID de la reserva
     * @return reserva encontrada o null si no existe
     */
    Reserva findById(int id);

    /**
     * Crea una nueva reserva en el sistema.
     *
     * @param reserva objeto reserva a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    boolean create(Reserva reserva);

    /**
     * Actualiza una reserva existente.
     *
     * @param reserva objeto reserva con datos actualizados
     * @return true si la actualización fue correcta, false en caso contrario
     */
    boolean update(Reserva reserva);

    /**
     * Elimina una reserva por su identificador.
     *
     * @param id ID de la reserva
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean delete(int id);

    /**
     * Cancela una reserva realizada por un usuario.
     *
     * @param idReserva ID de la reserva a cancelar
     * @param idUsuario ID del usuario propietario de la reserva
     * @return true si la cancelación fue correcta, false en caso contrario
     */
    boolean cancelarReserva(int idReserva, int idUsuario);

    /**
     * Comprueba si un usuario ya tiene una reserva en una actividad concreta.
     *
     * @param idUsuario ID del usuario
     * @param idActividad ID de la actividad
     * @return true si ya existe la reserva, false en caso contrario
     */
    boolean existeReservaUsuarioActividad(int idUsuario, int idActividad);

    /**
     * Comprueba si una actividad tiene plazas disponibles.
     *
     * @param idActividad ID de la actividad
     * @return true si hay plazas disponibles, false si está completa
     */
    boolean comprobarPlazasDisponibles(int idActividad);

    /**
     * Comprueba si un usuario ya tiene reservada una actividad.
     *
     * @param actividadId ID de la actividad
     * @param usuarioId ID del usuario
     * @return true si ya está reservada, false en caso contrario
     */
    boolean yaReservado(int actividadId, int usuarioId);

    /**
     * Realiza una reserva de una actividad para un usuario.
     *
     * @param actividadId ID de la actividad
     * @param usuarioId ID del usuario
     * @return true si la reserva fue exitosa, false en caso contrario
     */
    boolean reservar(int actividadId, int usuarioId);

    /**
     * Obtiene todas las reservas de un usuario concreto.
     *
     * @param idUsuario ID del usuario
     * @return lista de reservas del usuario
     */
    List<Reserva> findByIdUsuario(int idUsuario);

    /**
     * Cambia el estado de la reserva
     * 
     * @param idReserva ID de la reserva
     * @param nuevoEstado El nuevo estado
     * @return Cambio de reserva true/false
     */
    boolean cambiarEstado(int idReserva, String nuevoEstado);

}