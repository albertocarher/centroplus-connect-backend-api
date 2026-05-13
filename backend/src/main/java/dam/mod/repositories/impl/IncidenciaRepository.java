package dam.mod.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dam.mod.models.Incidencia;
import dam.mod.repositories.ConnectionManager;
import dam.mod.repositories.IIncidenciaRepository;

public class IncidenciaRepository implements IIncidenciaRepository {

    @Override
    public List<Incidencia> findAll() {
        String sql = "SELECT * FROM incidencias";
        List<Incidencia> listaIncidencias = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                listaIncidencias.add(new Incidencia(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_usuario"),
                        resultSet.getString("asunto"),
                        resultSet.getString("descripcion"),
                        LocalDate.parse(resultSet.getString("fecha")),
                        resultSet.getString("estado")
                ));
            }
            return listaIncidencias;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al obtener incidencias", exception);
        }


    }

    @Override
    public Incidencia findById(int idIncidencia) {
        String sql = "SELECT * FROM incidencias WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idIncidencia);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Incidencia(
                            resultSet.getInt("id"),
                            resultSet.getInt("id_usuario"),
                            resultSet.getString("asunto"),
                            resultSet.getString("descripcion"),
                            LocalDate.parse(resultSet.getString("fecha")),
                            resultSet.getString("estado")
                    );
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error al buscar incidencia", exception);
        }

        return null;
    }

    @Override
    public boolean save(Incidencia incidencia) {
        String sql = "INSERT INTO incidencias (id_usuario, asunto, descripcion, fecha, estado) VALUES (?,?,?,?,?)";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, incidencia.getIdUsuario());
            preparedStatement.setString(2, incidencia.getAsunto());
            preparedStatement.setString(3, incidencia.getDescripcion());
            preparedStatement.setString(4, incidencia.getFecha().toString());
            preparedStatement.setString(5, incidencia.getEstado());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al insertar incidencia", exception);
        }
    }

    @Override
    public boolean update(Incidencia incidencia) {
        String sql = "UPDATE incidencias SET id_usuario=?, asunto=?, descripcion=?, fecha=?, estado=? WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, incidencia.getIdUsuario());
            preparedStatement.setString(2, incidencia.getAsunto());
            preparedStatement.setString(3, incidencia.getDescripcion());
            preparedStatement.setString(4, incidencia.getFecha().toString());
            preparedStatement.setString(5, incidencia.getEstado());
            preparedStatement.setInt(6, incidencia.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al actualizar incidencia", exception);
        }
    }

    @Override
    public boolean delete(int idIncidencia) {
        String sql = "DELETE FROM incidencias WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idIncidencia);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al eliminar incidencia", exception);
        }
    }
}
