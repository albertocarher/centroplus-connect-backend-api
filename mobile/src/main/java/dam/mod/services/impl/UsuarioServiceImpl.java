package dam.mod.services.impl;

import java.util.List;

import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.Validaciones;

public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository repository;

    public UsuarioServiceImpl(IUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Usuario findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean create(Usuario usuario) {
        validar(usuario);
        return repository.save(usuario);
    }

    @Override
    public boolean update(Usuario usuario) {
        validar(usuario);
        return repository.update(usuario);
    }

    @Override
    public boolean delete(int id) {
        return repository.delete(id);
    }

    private void validar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no puede ser null");
        }

        Validaciones.validarNoVacio(usuario.getNombre(), "Nombre");
        Validaciones.validarEmail(usuario.getEmail());
        Validaciones.validarDNI(usuario.getDni());
        Validaciones.validarTipoUsuario(usuario.getTipoUsuario());
    }
}