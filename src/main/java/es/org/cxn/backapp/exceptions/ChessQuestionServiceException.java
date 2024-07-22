
package es.org.cxn.backapp.exceptions;

/**
 * Exception for ChessQuestionService.
 * @author Santi
 *
 */
public class ChessQuestionServiceException extends Exception {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -5312915913395705691L;

  /**
   * Main constructor.
   *
   * @param value exception message.
   */
  public ChessQuestionServiceException(final String value) {
    super(value);
  }
}
