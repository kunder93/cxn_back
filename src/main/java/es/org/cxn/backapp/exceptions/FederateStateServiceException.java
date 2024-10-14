package es.org.cxn.backapp.exceptions;

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
     * Main constructor.
     *
     * @param value exception message.
     */
    public FederateStateServiceException(final String value) {
        super(value);
    }

}
