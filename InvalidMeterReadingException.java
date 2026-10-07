package exception;

/**
 * Thrown when meter reading values are invalid.
 * Example: current reading less than previous reading.
 */
public class InvalidMeterReadingException extends Exception {
    public InvalidMeterReadingException(String message) {
        super(message);
    }
}
