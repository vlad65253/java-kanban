package exception;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message, Exception e) {
        super(message);
        System.out.println(e);
    }
}