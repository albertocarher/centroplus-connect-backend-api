package dam.mod.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dam.mod.models.Usuario;
import dam.mod.repositories.ConnectionManager;
import dam.mod.repositories.IUsuarioRepository;

public class UsuarioRepository implements IUsuarioRepository {

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> listaUsuarios = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                listaUsuarios.add(new Usuario(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("dni"),
                        resultSet.getString("email"),
                        resultSet.getString("telefono"),
                        resultSet.getString("tipo_usuario")
                ));
            }
        return listaUsuarios;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al obtener usuarios", exception);
        }
    }

    @Override
    public Usuario findById(int idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("dni"),
                            resultSet.getString("email"),
                            resultSet.getString("telefono"),
                            resultSet.getString("tipo_usuario")
                    );
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error al buscar usuario", exception);
        }

        return null;
    }

    @Override
    public boolean save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, dni, email, telefono, tipo_usuario) VALUES (?,?,?,?,?)";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDni());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getTelefono());
            preparedStatement.setString(5, usuario.getTipoUsuario());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al insertar usuario", exception);
        }
    }

    @Override
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre=?, dni=?, email=?, telefono=?, tipo_usuario=? WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getDni());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getTelefono());
            preparedStatement.setString(5, usuario.getTipoUsuario());
            preparedStatement.setInt(6, usuario.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al actualizar usuario", exception);
        }
    }

    @Override
    public boolean delete(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al eliminar usuario", exception);
        }
    }
}
