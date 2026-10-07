package exception;

/**
 * Thrown when trying to register a Consumer ID that already exists.
 */
public class DuplicateConsumerException extends Exception {
    public DuplicateConsumerException(String message) {
        super(message);
    }
}
