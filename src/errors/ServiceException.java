package errors;

public class ServiceException extends RuntimeException{

    public ServiceException(String mensaje) {
        super(mensaje);
    }

    public ServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
