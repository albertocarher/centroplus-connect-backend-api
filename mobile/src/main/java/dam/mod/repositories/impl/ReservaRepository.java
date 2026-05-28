package dam.mod.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dam.mod.models.Reserva;
import dam.mod.repositories.ConnectionManager;
import dam.mod.repositories.IReservaRepository;

public class ReservaRepository implements IReservaRepository {

    @Override
    public List<Reserva> findAll() {

        String sql = """
        SELECT r.id, r.id_usuario, r.id_actividad, r.fecha, r.estado, a.nombre
        FROM reservas r
        JOIN actividades a ON r.id_actividad = a.id
    """;

        List<Reserva> listaReservas = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                Reserva r = new Reserva(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_usuario"),
                        resultSet.getInt("id_actividad"),
                        LocalDate.parse(resultSet.getString("fecha")),
                        resultSet.getString("estado")
                );

                r.setNombreActividad(resultSet.getString("nombre"));

                listaReservas.add(r);
            }

            return listaReservas;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al obtener reservas", exception);
        }
    }

    @Override
    public Reserva findById(int idReserva) {
        String sql = "SELECT * FROM reservas WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idReserva);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Reserva(
                            resultSet.getInt("id"),
                            resultSet.getInt("id_usuario"),
                            resultSet.getInt("id_actividad"),
                            LocalDate.parse(resultSet.getString("fecha")),
                            resultSet.getString("estado")
                    );
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error al buscar reserva", exception);
        }

        return null;
    }

    @Override
    public boolean save(Reserva reserva) {
        String sql = "INSERT INTO reservas (id_usuario, id_actividad, fecha, estado) VALUES (?,?,?,?)";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, reserva.getIdUsuario());
            preparedStatement.setInt(2, reserva.getIdActividad());
            preparedStatement.setString(3, reserva.getFecha().toString());
            preparedStatement.setString(4, reserva.getEstado());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al insertar reserva", exception);
        }
    }

    @Override
    public boolean update(Reserva reserva) {
        String sql = "UPDATE reservas SET id_usuario=?, id_actividad=?, fecha=?, estado=? WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, reserva.getIdUsuario());
            preparedStatement.setInt(2, reserva.getIdActividad());
            preparedStatement.setString(3, reserva.getFecha().toString());
            preparedStatement.setString(4, reserva.getEstado());
            preparedStatement.setInt(5, reserva.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al actualizar reserva", exception);
        }
    }

    @Override
    public boolean delete(int idReserva) {
        String sql = "DELETE FROM reservas WHERE id=?";

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idReserva);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error al eliminar reserva", exception);
        }
    }
    @Override
    public boolean existsReserva(int actividadId, int usuarioId) {

        String sql = "SELECT 1 FROM reservas WHERE id_actividad=? AND id_usuario=?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, actividadId);
            ps.setInt(2, usuarioId);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
