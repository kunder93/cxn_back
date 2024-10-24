
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.BookEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the form used by the controller as a response for requesting one
 * book.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param isbn The book's ISBN.
 * @param title The book's title.
 * @param gender The book's genre.
 * @param language The book's language.
 * @param publishYear The book's publish date.
 * @param authors The set of book authors.
 *
 * @author Santiago Paz.
 */
public record BookResponse(
      long isbn, String title, String gender, String language,
      LocalDate publishYear, Set<AuthorResponse> authors
) implements Comparable<BookResponse> {

  /**
   * Constructs a {@link BookResponse} from a {@link BookEntity}.
   *
   * @param book The book entity.
   */
  public BookResponse(final BookEntity book) {
    this(
          book.getIsbn(), book.getTitle(), book.getGender(), book.getLanguage(),
          book.getPublishYear(),
          book.getAuthors().stream().map(AuthorResponse::new)
                .collect(HashSet::new, HashSet::add, HashSet::addAll)
    );
  }

  /**
   * Compare books using their title.
   */
  @Override
  public int compareTo(final BookResponse otherBook) {
    return this.title().compareTo(otherBook.title());
  }
}
