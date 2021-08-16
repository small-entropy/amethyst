package core.exceptions;

public class ServerException extends Exception {
    public ServerException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
