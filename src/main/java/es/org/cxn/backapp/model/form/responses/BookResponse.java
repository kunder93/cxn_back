
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.BookEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;

import lombok.AllArgsConstructor;
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
public final class BookResponse
      implements Serializable, Comparable<BookResponse> {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133089826013337705L;

  /**
   * The book's isbn.
   */
  private long isbn;

  /**
   * The book's title.
   */
  private String title;

  /**
   * The book's gender.
   */
  private String gender;

  /**
   * The book's language.
   */
  private String language;

  /**
   * The book's publish date.
   */
  private LocalDate publishYear;

  /**
   * The book authors set.
   */
  private HashSet<AuthorResponse> authors;

  /**
   * Constructor with provided Company entity.
   *
   * @param e The book entity for get data.
   */
  public BookResponse(final BookEntity e) {
    super();
    title = e.getTitle();
    isbn = e.getIsbn();
    gender = e.getGender();
    publishYear = e.getPublishYear();
    language = e.getLanguage();
    authors = new HashSet<>();
    e.getAuthors().forEach(author -> authors.add(new AuthorResponse(author)));
  }

  /**
   * Compare books using title.
   */
  @Override
  public int compareTo(final BookResponse otherBook) {
    return this.title.compareTo(otherBook.getTitle());
  }

}