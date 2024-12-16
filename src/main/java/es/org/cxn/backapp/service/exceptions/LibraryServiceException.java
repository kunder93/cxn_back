
package es.org.cxn.backapp.service.exceptions;

/**
 * Exception thrown by library service.
 *
 * @author Santiago Paz.
 *
 */
public final class LibraryServiceException extends Exception {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 4621625583628223252L;

    /**
     * Main constructor.
     *
     * @param value exception message.
     */
    public LibraryServiceException(final String value) {
        super(value);
    }

    /**
     * Main constructor.
     *
     * @param value     exception message.
     * @param exception The high order exception.
     */
    public LibraryServiceException(final String value, final Throwable exception) {
        super(value, exception);
    }
}
