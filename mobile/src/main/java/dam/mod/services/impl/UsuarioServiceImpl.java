package dam.mod.services.impl;

import java.util.List;
import dam.mod.utils.PasswordUtils;
import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.Validaciones;
import dam.mod.repositories.IRememberTokenRepository;
import dam.mod.models.RememberToken;
import dam.mod.utils.Session;
import dam.mod.utils.TokenUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository repository;
    private final IRememberTokenRepository rememberTokenRepository;

    public UsuarioServiceImpl(IUsuarioRepository repository, IRememberTokenRepository rememberTokenRepository) {
        this.repository = repository;
        this.rememberTokenRepository = rememberTokenRepository;
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

    @Override
    public Usuario login(String dni, String password, boolean rememberMe) {

        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI obligatorio");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password obligatoria");
        }

        Usuario usuario = repository.findByDni(dni);

        if (usuario == null) {
            throw new RuntimeException("Usuario no existe");
        }

        if (!PasswordUtils.checkPassword(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        if (rememberMe) {

            String token = UUID.randomUUID().toString();

            String tokenHash = TokenUtils.sha256(token);

            LocalDateTime expiresAt = LocalDateTime.now().plusDays(5);

            rememberTokenRepository.saveToken(
                    usuario.getId(),
                    tokenHash,
                    expiresAt.toString());

            Session.setTokenSesion(token);
        }

        Session.setCurrentUser(usuario);

        return usuario;
    }

    @Override
    public Usuario findByDni(String dni) {

        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI obligatorio");
        }

        return repository.findByDni(dni);
    }

    @Override
    public Usuario autoLogin() {

        String token = Session.getTokenSesionGuardado();

        if (token == null || token.isBlank()) {
            return null;
        }

        String tokenHash = TokenUtils.sha256(token);

        RememberToken t = rememberTokenRepository.findByHash(tokenHash);

        if (t == null) {
            Session.setTokenSesion(null);
            return null;
        }

        Usuario usuario = repository.findById(t.getUserId());

        if (usuario == null) {
            Session.setTokenSesion(null);
            return null;
        }

        Session.setCurrentUser(usuario);

        return usuario;
    }

@Override
public void logout() {
    try {
        Usuario usuario = Session.getCurrentUser();

        if (usuario != null && rememberTokenRepository != null) {
            rememberTokenRepository.deleteByUserId(usuario.getId());
        }

        Session.logout();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}