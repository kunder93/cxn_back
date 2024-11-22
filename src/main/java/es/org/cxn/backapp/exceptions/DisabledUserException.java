package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown when user try to login and is disabled.
 */
public class DisabledUserException extends RuntimeException {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -5921693429906987707L;

    /**
     * Main constructor for this exception.
     *
     * @param message The disabled user exception message.
     */
    public DisabledUserException(final String message) {
        super(message);
    }
}
