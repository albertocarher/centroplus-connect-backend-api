package dam.mod.repositories.impl;

import dam.mod.models.RememberToken;
import dam.mod.repositories.IRememberTokenRepository;
import dam.mod.repositories.sqlite.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RememberTokenRepositoryImpl implements IRememberTokenRepository {

    @Override
    public boolean saveToken(int userId, String tokenHash, String expiresAt) {

        String sql = "INSERT INTO remember_tokens (user_id, token_hash, expires_at) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, tokenHash);
            preparedStatement.setString(3, expiresAt);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error guardando token", e);
        }
    }

    @Override
    public RememberToken findByHash(String tokenHash) {

        String sql = "SELECT * FROM remember_tokens WHERE token_hash = ? AND expires_at > datetime('now')";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, tokenHash);

            try (ResultSet resulset = preparedStatement.executeQuery()) {

                if (resulset.next()) {
                    return new RememberToken(
                            resulset.getInt("id"),
                            resulset.getInt("user_id"),
                            resulset.getString("token_hash"),
                            resulset.getString("expires_at"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando token", e);
        }

        return null;
    }

    @Override
    public List<RememberToken> findAllValid() {

        String sql = "SELECT * FROM remember_tokens WHERE expires_at > datetime('now')";

        List<RememberToken> list = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                list.add(new RememberToken(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("token_hash"),
                        resultSet.getString("expires_at")));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo tokens", e);
        }

        return list;
    }

    @Override
    public boolean deleteByUserId(int userId) {

        String sql = "DELETE FROM remember_tokens WHERE user_id = ?";

        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error borrando tokens", e);
        }
    }
}