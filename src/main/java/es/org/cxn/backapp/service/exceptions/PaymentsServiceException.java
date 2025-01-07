package es.org.cxn.backapp.service.exceptions;

/**
 * PaymentServiceException is a custom exception for handling errors within the
 * Payments Service. This exception extends {@link Exception} and provides a
 * mechanism to capture error messages specific to activity-related operations.
 *
 * <p>
 * This class is marked as {@code final} to prevent extension and ensure
 * consistent usage for service-specific exceptions.
 * </p>
 *
 * @see Exception
 */
public final class PaymentsServiceException extends Exception {

    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1385589232224464048L;

    /**
     * Constructs a new PaymentServiceException with a specified error message.
     *
     * @param value the detailed message for the exception
     */
    public PaymentsServiceException(final String value) {
        super(value);
    }

    /**
     * Constructs a new PaymentServiceException with a specified error message and a
     * cause.
     *
     * @param message the detailed message for the exception
     * @param cause   the underlying cause of the exception
     */
    public PaymentsServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
