package dao;

import domain.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public void crear(Reserva reserva) {

        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO reserva (id_sala, fecha, hora) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, reserva.getIdSala());
                stmt.setDate(2, Date.valueOf(reserva.getDate()));
                stmt.setTime(3, Time.valueOf(reserva.getTime()));

                Integer filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas == 0) {
                    throw new SQLException("No se pudo hacer la reserva");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reserva.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID generado.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reserva> listarTodos() throws SQLException{

        List<Reserva> totales = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM RESERVA";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    Integer idSala = rs.getInt("id_sala");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    LocalTime hora = rs.getTime("hora").toLocalTime();

                    Reserva reserva = new Reserva(id,idSala,fecha,hora);
                    totales.add(reserva);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totales;
    }
}