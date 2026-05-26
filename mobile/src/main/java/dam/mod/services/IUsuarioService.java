package dam.mod.services;

import java.util.List;

import dam.mod.models.Usuario;

public interface IUsuarioService {

    /**
     * Devuelve una lista de todos los usuarios.
     *
     * @return Lista de usuarios.
     */
    List<Usuario> findAll();

    /**
     * Devuelve un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado o null si no se encuentra.
     */
    Usuario findById(int id);

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario Usuario a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    boolean create(Usuario usuario);

    /**
     * Actualiza un usuario existente.
     *
     * @param usuario Usuario a actualizar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    boolean update(Usuario usuario);

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    boolean delete(int id);
}
