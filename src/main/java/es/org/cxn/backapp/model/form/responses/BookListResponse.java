
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.BookEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;

/**
 * Represents the form used by controller as response for requesting all
 * books.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
@Data
public final class BookListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -6152227531653572216L;

  /**
   * List with all stored countries.
   */
  private Set<BookResponse> bookList = new TreeSet<>();

  /**
   * Constructor with provided parameters values.
   *
   * @param bookList2 The countries list.
   */
  public BookListResponse(final List<BookEntity> bookList2) {
    super();
    bookList2.forEach((BookEntity e) -> this.bookList.add(new BookResponse(e)));
  }
}
