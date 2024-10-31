package es.org.cxn.backapp.exceptions;

/**
 * ActivityServiceException is a custom exception for handling errors within the
 * Activity Service. This exception extends {@link Exception} and provides a
 * mechanism to capture error messages specific to activity-related operations.
 *
 * <p>
 * This class is marked as {@code final} to prevent extension and ensure
 * consistent usage for service-specific exceptions.
 * </p>
 *
 * @see Exception
 */
public final class ActivityServiceException extends Exception {

    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1387289236385464048L;

    /**
     * Constructs a new ActivityServiceException with a specified error message.
     *
     * @param value the detailed message for the exception
     */
    public ActivityServiceException(final String value) {
        super(value);
    }
}
