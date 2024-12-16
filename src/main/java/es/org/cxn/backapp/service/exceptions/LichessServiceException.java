package es.org.cxn.backapp.service.exceptions;

/**
 * Exception thrown by the Lichess authentication service.
 * <p>
 * This exception is used to indicate errors that occur during the
 * authentication process with Lichess. It extends {@link Exception} and
 * provides a way to signal specific issues related to the Lichess
 * authentication service.
 * </p>
 *
 * @author Santiago Paz
 */
public final class LichessServiceException extends Exception {

    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = -2343250182475869712L;

    /**
     * Constructs a new LichessAuthServiceException with the specified detail
     * message.
     *
     * @param value the detail message (which is saved for later retrieval by the
     *              {@link Throwable#getMessage()} method).
     */
    public LichessServiceException(final String value) {
        super(value);
    }

    /**
     * Constructs a new {@code LichessServiceException} with the specified detail
     * message and cause.
     * <p>
     * This constructor is used to create an exception that includes both a specific
     * message and the underlying cause, which allows the original exception's stack
     * trace to be preserved.
     * </p>
     *
     * @param value the detail message (which is saved for later retrieval by the
     *              {@link Throwable#getMessage()} method).
     * @param cause the cause of the exception (which is saved for later retrieval
     *              by the {@link Throwable#getCause()} method). (A {@code null}
     *              value is permitted, and indicates that the cause is nonexistent
     *              or unknown.)
     */
    public LichessServiceException(final String value, final Throwable cause) {
        super(value, cause);
    }

}
