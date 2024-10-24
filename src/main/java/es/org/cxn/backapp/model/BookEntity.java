
package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * Interface representing a book entity in the library.
 * This interface provides methods to access various details about a book,
 * such as its ISBN number, title, genre, publication year, language,
 * and associated authors.
 *
 * @author Santiago Paz.
 */
public interface BookEntity extends Serializable {

  /**
   * Gets the ISBN number of the book.
   *
   * @return The book's ISBN number.
   */
  Long getIsbn();

  /**
   * Gets the title of the book.
   *
   * @return The book's title.
   */
  String getTitle();

  /**
   * Gets the genre of the book.
   *
   * @return The book's genre.
   */
  String getGender();

  /**
   * Gets the publication year of the book.
   *
   * @return The book's publication year as a {@link LocalDate}.
   */
  LocalDate getPublishYear();

  /**
   * Gets the language of the book.
   *
   * @return The language in which the book is written.
   */
  String getLanguage();

  /**
   * Gets the set of authors associated with the book.
   *
   * @return A set of {@link PersistentAuthorEntity} representing the authors
   * of the book.
   */
  Set<PersistentAuthorEntity> getAuthors();
}
