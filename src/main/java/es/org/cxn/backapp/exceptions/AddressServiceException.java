package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown by user service.
 *
 * @author Santiago Paz.
 *
 */
public final class AddressServiceException extends Exception {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 4561625583621123252L;

    /**
     * Main constructor.
     *
     * @param value exception message.
     */
    public AddressServiceException(final String value) {
        super(value);
    }

}
