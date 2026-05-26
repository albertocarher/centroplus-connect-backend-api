package dam.mod.repositories;

import java.util.List;
import dam.mod.models.Usuario;

public interface IUsuarioRepository {
    List<Usuario> findAll();
    Usuario findById(int id);
    boolean save(Usuario usuario);
    boolean update(Usuario usuario);
    boolean delete(int id);
}