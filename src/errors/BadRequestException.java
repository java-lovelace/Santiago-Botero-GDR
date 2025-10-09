package errors;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String mensaje) {
        super(mensaje);
    }

    public BadRequestException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
