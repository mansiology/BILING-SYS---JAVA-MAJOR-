package exception;

/**
 * Thrown when consumer data (ID, name, phone, etc.) is invalid.
 */
public class InvalidConsumerException extends Exception {
    public InvalidConsumerException(String message) {
        super(message);
    }
}
