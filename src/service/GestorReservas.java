package service;

import dao.ReservaDAO;
import domain.Reserva;
import errors.BadRequestException;
import errors.ConflictException;
import errors.ServiceException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class GestorReservas {
    private ReservaDAO reservaDAO;

    public GestorReservas() {
        this.reservaDAO = new ReservaDAO();
    }

    public void hacerReserva(Reserva reserva) {

        try {
            if (reserva.getDate().isBefore(LocalDate.now())) {
                throw new BadRequestException("La fecha ya pasó");
            }

            List<Reserva> reservasExistentes = reservaDAO.listarTodos();
            for (Reserva r: reservasExistentes) {
                if (r.getIdSala().equals(reserva.getIdSala()) && r.getTime().equals(reserva.getTime()) && r.getDate().equals(reserva.getDate())) {
                    throw new ConflictException("Sala ya fue reservada, elija otra");
                }
            }
            reservaDAO.crear(reserva);
        } catch (SQLException e) {
            // Wrapping de excepción técnica
            throw new ServiceException("Error interno al crear la reserva", e);
        } finally {
            System.out.println("Intento de reserva finalizado."); // bloque finally visible
        }
    }

}