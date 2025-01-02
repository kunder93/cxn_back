
package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown by user service.
 *
 * @author Santiago Paz.
 *
 */
public final class UserServiceException extends Exception {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 4621625583628223252L;

  /**
   * Main constructor.
   *
   * @param value exception message.
   */
  public UserServiceException(final String value) {
    super(value);
  }

  /**
   * Constructor with message and cause.
   *
   * @param value exception message.
   * @param cause the cause of the exception.
   */
  public UserServiceException(final String value, final Throwable cause) {
    super(value, cause);
  }

}
