package es.org.cxn.backapp.exceptions;

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
public final class LichessAuthServiceException extends Exception {

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
    public LichessAuthServiceException(final String value) {
        super(value);
    }
}
