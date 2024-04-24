
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.AuthorEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response for requesting one
 * book.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AuthorResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133021641898437705L;

  /**
   * The author first name.
   */
  private String firstName;

  /**
   * The author last name.
   */
  private String lastName;

  /**
   * The author nationality.
   */
  private String nationality;

  /**
   * Build response using author entity.
   *
   * @param author The author entity.
   */
  public AuthorResponse(final AuthorEntity author) {
    firstName = author.getFirstName();
    lastName = author.getLastName();
    nationality = author.getNationality();
  }
}
