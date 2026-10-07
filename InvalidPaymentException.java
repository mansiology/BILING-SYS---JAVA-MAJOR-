package exception;

/**
 * Thrown when payment amount is invalid.
 * Example: payment is zero, negative, or exceeds pending amount.
 */
public class InvalidPaymentException extends Exception {
    public InvalidPaymentException(String message) {
        super(message);
    }
}
