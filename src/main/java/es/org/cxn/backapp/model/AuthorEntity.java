
package es.org.cxn.backapp.model;

import java.io.Serializable;

/**
 * Interface representing an Author entity, typically associated with books.
 * This interface provides methods to retrieve details about the author,
 * including their first name, last name, and nationality.
 *
 * @author Santiago Paz.
 */
public interface AuthorEntity extends Serializable {

  /**
   * Gets the first name of the author.
   *
   * @return The author's first name.
   */
  String getFirstName();

  /**
   * Gets the last name of the author.
   *
   * @return The author's last name.
   */
  String getLastName();

  /**
   * Gets the nationality of the author.
   *
   * @return The author's nationality.
   */
  String getNationality();

}
