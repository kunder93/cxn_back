package es.org.cxn.backapp.service.exceptions;

/**
 * Exception thrown by payment sheet service.
 *
 * @author Santiago Paz.
 *
 */
public final class PaymentSheetServiceException extends Exception {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 4622634511128213252L;

  /**
   * Main constructor.
   *
   * @param value exception message.
   */
  public PaymentSheetServiceException(final String value) {
    super(value);
  }

}
