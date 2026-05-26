package dam.mod.services;

import java.util.List;

import dam.mod.models.Incidencia;

public interface IIncidenciaService {

    /**
     * Devuelve una lista de todas las incidencias.
     *
     * @return Lista de incidencias.
     */
    List<Incidencia> findAll();

    /**
     * Devuelve una incidencia por su ID.
     *
     * @param id ID de la incidencia.
     * @return Incidencia encontrada o null si no se encuentra.
     */
    Incidencia findById(int id);

    /**
     * Crea una nueva incidencia.
     *
     * @param incidencia Incidencia a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    boolean create(Incidencia incidencia);

    /**
     * Actualiza una incidencia existente.
     *
     * @param incidencia Incidencia a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    boolean update(Incidencia incidencia);

    /**
     * Elimina una incidencia por su ID.
     *
     * @param id ID de la incidencia.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    boolean delete(int id);

    /**
     * Cambia el estado de una incidencia.
     *
     * @param idIncidencia ID de la incidencia.
     * @param nuevoEstado Nuevo estado de la incidencia.
     * @return true si el cambio de estado fue exitoso, false en caso contrario.
     */
    boolean cambiarEstado(int idIncidencia, String nuevoEstado);

    /**
     * Devuelve una lista de incidencias asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de incidencias del usuario.
     */
    List<Incidencia> findByUsuario(int idUsuario);
}
