
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.AuthorEntity;

/**
 * Represents the form used by the controller as a response for requesting an
 * author.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param firstName The author's first name.
 * @param lastName  The author's last name.
 * @param nationality The author's nationality.
 *
 * @author Santiago Paz.
 */
public record AuthorResponse(
      String firstName, String lastName, String nationality
) {

  /**
   * Constructs an {@link AuthorResponse} from an {@link AuthorEntity}.
   *
   * @param author The author entity.
   */
  public AuthorResponse(final AuthorEntity author) {
    this(author.getFirstName(), author.getLastName(), author.getNationality());
  }
}
