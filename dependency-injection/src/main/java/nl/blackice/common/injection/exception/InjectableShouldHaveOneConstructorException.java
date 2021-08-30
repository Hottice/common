package nl.blackice.common.injection.exception;

public class InjectableShouldHaveOneConstructorException extends RuntimeException {
    public InjectableShouldHaveOneConstructorException(String message) {
        super(message);
    }
}
