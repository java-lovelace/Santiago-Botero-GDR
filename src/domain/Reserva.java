package domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private Integer id;
    private Integer idSala;
    private LocalDate date;
    private LocalTime time;

    public Reserva(Integer id, Integer idSala, LocalDate date, LocalTime time) {
        if(date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha invalida");
        }

        if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Hora invalida");
        }

        setIdSala(idSala);
        this.date = date;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        if (idSala <= 0) {
            throw new IllegalArgumentException("Id de sala invalido.");
        }
        this.idSala = idSala;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }
}