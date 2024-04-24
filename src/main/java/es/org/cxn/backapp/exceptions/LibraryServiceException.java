
package es.org.cxn.backapp.exceptions;

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
   * @param e The high order exception.
   */
  public LibraryServiceException(final String value, final Throwable e) {
    super(value, e);
  }

  /**
   * Main constructor.
   *
   * @param value exception message.
   */
  public LibraryServiceException(final String value) {
    super(value);
  }
}
