package errors;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String mensaje) {
        super(mensaje);
    }

    public NotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
