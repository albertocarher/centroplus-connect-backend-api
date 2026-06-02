package dam.mod.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.sqlite.ConnectionManager;
import dam.mod.utils.PasswordUtils;

public class UsuarioRepository implements IUsuarioRepository {

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> listaUsuarios = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                listaUsuarios.add(new Usuario(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("dni"),
                        resultSet.getString("email"),
                        resultSet.getString("telefono"),
                        resultSet.getString("tipo_usuario"),
                        resultSet.getString("password")));
            }

            return listaUsuarios;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuarios", e);
        }
    }

    @Override
    public Usuario findById(int idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("dni"),
                            resultSet.getString("email"),
                            resultSet.getString("telefono"),
                            resultSet.getString("tipo_usuario"),
                            resultSet.getString("password"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario", e);
        }

        return null;
    }

    @Override
    public boolean save(Usuario usuario) {

        String sql = "INSERT INTO usuarios (nombre, dni, email, telefono, tipo_usuario, password) VALUES (?,?,?,?,?,?)";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDni());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getTelefono());
            preparedStatement.setString(5, usuario.getTipoUsuario());

            preparedStatement.setString(6, PasswordUtils.hashPassword(usuario.getPassword()));

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar usuario", e);
        }
    }

    @Override
    public boolean update(Usuario usuario) {

        String sql = "UPDATE usuarios SET nombre=?, dni=?, email=?, telefono=?, tipo_usuario=?, password=? WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDni());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getTelefono());
            preparedStatement.setString(5, usuario.getTipoUsuario());

            preparedStatement.setString(6, PasswordUtils.hashPassword(usuario.getPassword()));

            preparedStatement.setInt(7, usuario.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }

    @Override
    public boolean delete(int idUsuario) {

        String sql = "DELETE FROM usuarios WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    @Override
    public Usuario login(String dni, String password) {

        String sql = "SELECT * FROM usuarios WHERE dni=?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String hashBD = rs.getString("password");

                    if (PasswordUtils.checkPassword(password, hashBD)) {

                        return new Usuario(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("dni"),
                                rs.getString("email"),
                                rs.getString("telefono"),
                                rs.getString("tipo_usuario"),
                                hashBD);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en login", e);
        }

        return null;
    }

    @Override
    public Usuario findByDni(String dni) {

        String sql = "SELECT * FROM usuarios WHERE dni=?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, dni);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("dni"),
                            resultSet.getString("email"),
                            resultSet.getString("telefono"),
                            resultSet.getString("tipo_usuario"),
                            resultSet.getString("password"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por DNI", e);
        }

        return null;
    }
}