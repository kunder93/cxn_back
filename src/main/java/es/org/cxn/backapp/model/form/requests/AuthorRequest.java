
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

/**
 * Represents the form used by the controller to add a new author for a book.
 * <p>
 * This Data Transfer Object (DTO) is designed to facilitate communication
 * between the view and the controller by encapsulating the author's information
 * such as first name, last name, and nationality. It is specifically used
 * when adding a new book to the system, where author details are required.
 * </p>
 *
 * <p>
 * The {@code AuthorRequest} can be constructed directly with author
 * details, or by using an existing {@link PersistentAuthorEntity}. This
 * flexibility allows it to be easily integrated with both new author data
 * inputs and existing author data retrievals.
 * </p>
 *
 * @param firstName   The first name of the author. This is a required field
 *                    representing the given name of the author.
 * @param lastName    The last name of the author. This is a required field
 *                    representing the family name or surname.
 * @param nationality The nationality of the author. This field indicates the
 *                    country of origin or citizenship of the author.
 *
 * <p>
 * This DTO includes fields that should be validated to ensure that all
 * necessary author information is provided correctly when adding a new book.
 * </p>
 *
 * @author Santiago Paz
 */
public record AuthorRequest(
      String firstName, String lastName, String nationality
) {

  /**
   * Constructs an {@code AuthorRequest} using the data from an existing
   * {@link PersistentAuthorEntity}.
   *
   * @param authEntity The {@link PersistentAuthorEntity} containing the
   *                   author's information.
   */
  public AuthorRequest(final PersistentAuthorEntity authEntity) {
    this(
          authEntity.getFirstName(), authEntity.getLastName(),
          authEntity.getNationality()
    );
  }
}
