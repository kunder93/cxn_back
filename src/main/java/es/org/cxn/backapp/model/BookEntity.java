
package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * Inteface for library books.
 *
 * @author Santiago Paz.
 *
 */
public interface BookEntity extends Serializable {
  /**
   * @return The book isbn number.
   */
  Long getIsbn();

  /**
   * @return The book title.
   */
  String getTitle();

  /**
   * @return The book gender.
   */
  String getGender();

  /**
   * @return The book publish year date.
   */
  LocalDate getPublishYear();

  /**
   * @return The book language.
   */
  String getLanguage();

  /**
   * @return Set with authors.
   */
  Set<PersistentAuthorEntity> getAuthors();
}
