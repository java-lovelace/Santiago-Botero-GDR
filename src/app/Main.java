package app;

import domain.Reserva;
import service.GestorReservas;
import errors.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        GestorReservas gestor = new GestorReservas();
        boolean continuar = true;

        while (continuar) {
            String opcion = JOptionPane.showInputDialog("""
                    --- MENÚ DE RESERVAS ---
                    1. Hacer reserva
                    2. Buscar reserva por ID
                    3. Listar reservas con filtros
                    4. Cancelar reserva
                    5. Salir
                    """);

            if (opcion == null) break; // Si cierra la ventana, termina
            try {
                switch (opcion) {
                    case "1" -> hacerReserva(gestor);
                    case "2" -> buscarReserva(gestor);
                    case "3" -> listarConFiltros(gestor);
                    case "4" -> cancelarReserva(gestor);
                    case "5" -> continuar = false;
                    default -> JOptionPane.showMessageDialog(null, "Opción no válida");
                }
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void hacerReserva(GestorReservas gestor) {
        try {
            Integer idSala = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la sala:"));
            LocalDate fecha = LocalDate.parse(JOptionPane.showInputDialog("Ingrese fecha (YYYY-MM-DD):"));
            LocalTime hora = LocalTime.parse(JOptionPane.showInputDialog("Ingrese hora (HH:MM):"));

            Reserva reserva = new Reserva(idSala, fecha, hora);
            gestor.hacerReserva(reserva);

            JOptionPane.showMessageDialog(null, "Reserva creada correctamente con ID: " + reserva.getId());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear la reserva: " + e.getMessage());
        }
    }

    private static void buscarReserva(GestorReservas gestor) {
        try {
            Integer id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la reserva:"));
            Reserva reserva = gestor.buscarPorId(id);

            JOptionPane.showMessageDialog(null, "Reserva encontrada:\n" +
                    "ID: " + reserva.getId() + "\n" +
                    "Sala: " + reserva.getIdSala() + "\n" +
                    "Fecha: " + reserva.getDate() + "\n" +
                    "Hora: " + reserva.getTime());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void listarConFiltros(GestorReservas gestor) {
        try {
            String idSalaStr = JOptionPane.showInputDialog("Ingrese ID de sala (opcional):");
            String fechaInicioStr = JOptionPane.showInputDialog("Ingrese fecha inicio (YYYY-MM-DD, opcional):");
            String fechaFinStr = JOptionPane.showInputDialog("Ingrese fecha fin (YYYY-MM-DD, opcional):");

            List<Reserva> resultados = gestor.listarConFiltros(idSalaStr, fechaInicioStr, fechaFinStr);

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontraron reservas con esos filtros.");
            } else {
                StringBuilder sb = new StringBuilder("Reservas encontradas:\n");
                for (Reserva r : resultados) {
                    sb.append(String.format("ID: %d | Sala: %d | Fecha: %s | Hora: %s\n",
                            r.getId(), r.getIdSala(), r.getDate(), r.getTime()));
                }
                JOptionPane.showMessageDialog(null, sb.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void cancelarReserva(GestorReservas gestor) {
        try {
            Integer id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID de la reserva a cancelar:"));
            gestor.cancelarReserva(id);
            JOptionPane.showMessageDialog(null, "Reserva cancelada correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
