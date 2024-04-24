
package es.org.cxn.backapp.model;

import java.io.Serializable;

/**
 * Inteface for authors (books).
 *
 * @author Santiago Paz.
 *
 */
public interface AuthorEntity extends Serializable {

  /**
   * @return The author first name.
   */
  String getFirstName();

  /**
   * @return The author last name.
   */
  String getLastName();

  /**
   * @return The author nationality.
   */
  String getNationality();

}
