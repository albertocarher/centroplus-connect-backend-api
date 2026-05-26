package dam.mod.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dam.mod.models.Actividad;
import dam.mod.repositories.ConnectionManager;
import dam.mod.repositories.IActividadRepository;

public class ActividadRepository implements IActividadRepository {

    @Override
    public List<Actividad> findAll() {
        String sql = "SELECT * FROM actividades";
        List<Actividad> listaActividades = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                listaActividades.add(new Actividad(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo_actividad"),
                        resultSet.getInt("duracion"),
                        resultSet.getDouble("precio"),
                        resultSet.getInt("plazas_maximas"),
                        resultSet.getInt("plazas_ocupadas")
                ));
            }
        return listaActividades;
        } catch (SQLException exception) {
            throw new RuntimeException("Error al obtener actividades", exception);
        }


    }

    @Override
    public Actividad findById(int idActividad) {
        String sql = "SELECT * FROM actividades WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idActividad);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Actividad(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("tipo_actividad"),
                            resultSet.getInt("duracion"),
                            resultSet.getDouble("precio"),
                            resultSet.getInt("plazas_maximas"),
                            resultSet.getInt("plazas_ocupadas")
                    );
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error al buscar actividad", exception);
        }

        return null;
    }

    @Override
    public boolean save(Actividad actividad) {
        String sql = "INSERT INTO actividades (nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas) VALUES (?,?,?,?,?,?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actividad.getNombre());
            preparedStatement.setString(2, actividad.getTipoActividad());
            preparedStatement.setInt(3, actividad.getDuracion());
            preparedStatement.setDouble(4, actividad.getPrecio());
            preparedStatement.setInt(5, actividad.getPlazasMaximas());
            preparedStatement.setInt(6, actividad.getPlazasOcupadas());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al insertar actividad", exception);
        }
    }

    @Override
    public boolean update(Actividad actividad) {
        String sql = "UPDATE actividades SET nombre=?, tipo_actividad=?, duracion=?, precio=?, plazas_maximas=?, plazas_ocupadas=? WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actividad.getNombre());
            preparedStatement.setString(2, actividad.getTipoActividad());
            preparedStatement.setInt(3, actividad.getDuracion());
            preparedStatement.setDouble(4, actividad.getPrecio());
            preparedStatement.setInt(5, actividad.getPlazasMaximas());
            preparedStatement.setInt(6, actividad.getPlazasOcupadas());
            preparedStatement.setInt(7, actividad.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al actualizar actividad", exception);
        }
    }

    @Override
    public boolean delete(int idActividad) {
        String sql = "DELETE FROM actividades WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idActividad);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al eliminar actividad", exception);
        }
    }
}