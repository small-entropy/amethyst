package platform.exceptions;

public class DataException extends Exception {
    public DataException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
