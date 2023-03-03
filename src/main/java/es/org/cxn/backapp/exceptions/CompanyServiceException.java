package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown by company service.
 *
 * @author Santiago Paz.
 *
 */
public final class CompanyServiceException extends Exception {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 4622655520528223252L;

    /**
     * Main constructor.
     *
     * @param value exception message.
     */
    public CompanyServiceException(final String value) {
        super(value);
    }

}
