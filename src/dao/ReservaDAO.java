package dao;

import domain.Reserva;
import errors.BadRequestException;
import errors.DataAccessException;
import errors.NotFoundException;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public void crear(Reserva reserva) throws errors.DataAccessException {

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
            e.printStackTrace();
            throw new errors.DataAccessException("Error al insertar reserva",e);
        }
    }

    public List<Reserva> listarTodos() throws errors.DataAccessException{

        List<Reserva> totales = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM reserva";
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
            throw new errors.DataAccessException("Error al listar todos",e);
        }
        return totales;
    }

    public Reserva listarPorId(Integer id) throws errors.DataAccessException {
        try (Connection conn = ConnectionManager.getConnection()) {
            // 1️⃣ Preparo la consulta SQL
            String sql = "SELECT * FROM reserva WHERE id = ?";

            // 2️⃣ Creo el statement con parámetros
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id); // Aquí se reemplaza el ? por el valor del parámetro id

                // 3️⃣ Ejecuto la consulta
                try (ResultSet rs = stmt.executeQuery()) {
                    // 4️⃣ Si hay resultados, construyo el objeto Reserva
                    if (rs.next()) {
                        Reserva reserva = new Reserva(
                                rs.getInt("id_sala"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getTime("hora").toLocalTime()
                        );
                        reserva.setId(rs.getInt("id"));
                        return reserva;
                    } else {
                        // 5️⃣ Si no hay resultados, lanzo una excepción personalizada
                        throw new errors.NotFoundException("No se encontró una reserva con ID " + id);
                    }
                }
            }
        } catch (SQLException e) {
            // 6️⃣ Capturo errores SQL y los traduzco a una excepción personalizada
            throw new errors.DataAccessException("Error al acceder a la base de datos", e);
        }
    }

    public List<Reserva> listarFechaSala() throws DataAccessException {

        return List.of();
    }






    public void cancelarReserva(Integer id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("El ID proporcionado no es correcto");
        }

        try(Connection conn = ConnectionManager.getConnection()) {
            String sql = "DELETE FROM reserva WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);

                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas == 0) {
                    throw new NotFoundException("No existe una reserva con el ID especificado");
                }
            }
        } catch (SQLException e) {
            throw new errors.DataAccessException("Error al eliminar la reserva en la base de datos", e);
        } finally {
            JOptionPane.showMessageDialog(null, "Operación de cancelación finalizada");
        }
    }


    public List<Reserva> listarConFiltros(Integer idSala, LocalDate fechaInicio, LocalDate fechaFin) throws DataAccessException {
        List<Reserva> resultados = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM reserva WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        // Construcción dinámica del SQL
        if (idSala != null) {
            sql.append(" AND id_sala = ?");
            parametros.add(idSala);
        }
        if (fechaInicio != null) {
            sql.append(" AND fecha >= ?");
            parametros.add(fechaInicio);
        }
        if (fechaFin != null) {
            sql.append(" AND fecha <= ?");
            parametros.add(fechaFin);
        }

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                Object param = parametros.get(i);
                if (param instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) param);
                } else if (param instanceof LocalDate) {
                    stmt.setDate(i + 1, Date.valueOf((LocalDate) param));
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva(
                            rs.getInt("id_sala"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime()
                    );
                    reserva.setId(rs.getInt("id"));
                    resultados.add(reserva);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar con filtros", e);
        }

        return resultados;
    }
}