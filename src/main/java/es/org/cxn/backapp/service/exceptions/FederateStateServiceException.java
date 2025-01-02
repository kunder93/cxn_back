package es.org.cxn.backapp.service.exceptions;

/**
 * Exception thrown by federate state service.
 *
 * @author Santiago Paz.
 *
 */
public final class FederateStateServiceException extends Exception {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 4511128362621185622L;

    /**
     * Main constructor with an exception message.
     *
     * @param value exception message.
     */
    public FederateStateServiceException(final String value) {
        super(value);
    }

    /**
     * Constructor with an exception message and a cause.
     *
     * @param value exception message.
     * @param cause the original exception that caused this exception.
     */
    public FederateStateServiceException(final String value, final Throwable cause) {
        super(value, cause);
    }

}
