
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the author part form used by controller as request to add book
 *  with author.
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
public final class AuthorRequestDto implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3111135649215975705L;

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
   * Construct dto with entity.
   *
   * @param authEntity The author entity.
   */
  public AuthorRequestDto(final PersistentAuthorEntity authEntity) {
    super();
    this.firstName = authEntity.getFirstName();
    this.lastName = authEntity.getLastName();
    this.nationality = authEntity.getNationality();
  }

}
