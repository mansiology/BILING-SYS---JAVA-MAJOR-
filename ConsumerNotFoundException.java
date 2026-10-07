package exception;

/**
 * Thrown when a consumer ID is not found in the system.
 */
public class ConsumerNotFoundException extends Exception {
    public ConsumerNotFoundException(String message) {
        super(message);
    }
}
