package domain;

public class Sala {

    private Integer id;
    private String nombre;
    private Integer capacidad;

    public Sala(Integer id,String nombre, Integer capacidad) {
        setNombre(nombre);
        setCapacidad(capacidad);
        this.id = id;
    }

    public Sala(String nombre, Integer capacidad) {
        setNombre(nombre);
        setCapacidad(capacidad);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre invalido");
        }
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("Capacidad invalida");
        }
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Sala{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", capacidad=" + capacidad +
                '}';
    }

}
