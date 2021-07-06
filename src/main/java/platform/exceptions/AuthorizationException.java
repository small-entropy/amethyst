package platform.exceptions;

public class AuthorizationException extends Exception {
    public AuthorizationException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
