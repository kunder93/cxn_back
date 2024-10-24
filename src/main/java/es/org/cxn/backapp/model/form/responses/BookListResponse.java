
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.BookEntity;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Represents the form used by the controller as a response for requesting all
 * books.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param bookList A set of book responses.
 */
public record BookListResponse(Set<BookResponse> bookList) {

  /**
   * Constructs a {@link BookListResponse} from a list of {@link BookEntity}.
   *
   * @param bookEntities The list of book entities.
   */
  public BookListResponse(final List<BookEntity> bookEntities) {
    this(
          bookEntities.stream().map(BookResponse::new)
                .collect(Collectors.toCollection(TreeSet::new))
    );
  }
}
