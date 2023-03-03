package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown by invoice service.
 *
 * @author Santiago Paz.
 *
 */
public final class InvoiceServiceException extends Exception {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 4122655599528223152L;

    /**
     * Main constructor.
     *
     * @param value exception message.
     */
    public InvoiceServiceException(final String value) {
        super(value);
    }

}
