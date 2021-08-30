package nl.blackice.common.injection.exception;

public class AlreadyOtherInjectableCreated extends RuntimeException {
    public AlreadyOtherInjectableCreated(String name) {
        super(String.format("Injectable can not be created for %s. There already exists an ", name));
    }
}
