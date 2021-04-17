package Exceptions;

public class AccessException extends Exception {
    public AccessException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
