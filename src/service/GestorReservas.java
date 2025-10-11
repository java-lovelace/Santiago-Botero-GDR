package service;

import dao.ReservaDAO;
import domain.Reserva;
import errors.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        } finally {
            System.out.println("Intento de reserva finalizado."); // bloque finally visible
        }
    }


    public Reserva buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("El id proporcionado es incorrecto");
        }

        try {
            Reserva reserva = reservaDAO.listarPorId(id);

            if (reserva == null) {
                throw new NotFoundException("No existe una reserva con ese ID");
            }
            return reserva;

        } catch (DataAccessException e) {
            throw new ServiceException("Error al buscar en la base de datos",e);
        }
    }

    public List<Reserva> listarConFiltros(String idSalaStr, String fechaInicioStr, String fechaFinStr) {

        try {
            Integer idSala = null;
            LocalDate fechaInicio = null;
            LocalDate fechaFin = null;

            // Multi-catch: conversión de tipos
            try {
                if (idSalaStr != null && !idSalaStr.isBlank()) {
                    idSala = Integer.parseInt(idSalaStr);
                    if (idSala <= 0) throw new NumberFormatException();
                }
                if (fechaInicioStr != null && !fechaInicioStr.isBlank()) {
                    fechaInicio = LocalDate.parse(fechaInicioStr);
                }
                if (fechaFinStr != null && !fechaFinStr.isBlank()) {
                    fechaFin = LocalDate.parse(fechaFinStr);
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                throw new BadRequestException("Parámetros de búsqueda con formato inválido: " + e.getMessage());
            }

            // Llamar al DAO con los filtros validados
            return reservaDAO.listarConFiltros(idSala, fechaInicio, fechaFin);

        } catch (DataAccessException e) {
            throw new ServiceException("Error interno al acceder a las reservas", e);
        } finally {
            System.out.println("Búsqueda finalizada.");
        }
    }



    public void cancelarReserva(Integer id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("El ID proporcionado es incorrecto");
        }

        try {
            reservaDAO.listarPorId(id);
            reservaDAO.cancelarReserva(id);
        } catch (DataAccessException e) {
            throw new ServiceException("Error al acceder en la base de datos",e);
        } finally {
            JOptionPane.showMessageDialog(null, "Operacion finalizada");
        }
    }


}