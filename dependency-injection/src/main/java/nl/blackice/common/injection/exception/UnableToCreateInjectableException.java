package nl.blackice.common.injection.exception;

public class UnableToCreateInjectableException extends RuntimeException {
    public UnableToCreateInjectableException(String message) {
        super(message);
    }

    public UnableToCreateInjectableException(String message, Throwable cause) {
        super(message, cause);
    }
}
