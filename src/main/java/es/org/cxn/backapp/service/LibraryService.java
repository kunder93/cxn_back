
package es.org.cxn.backapp.service;

import es.org.cxn.backapp.model.AuthorEntity;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.service.exceptions.LibraryServiceException;

import java.util.List;

/**
 * Interface for the service that handles library operations such as managing
 * books and authors.
 *
 * @author Santiago Paz.
 */
public interface LibraryService {

  /**
   * Retrieves a list of all books in the library.
   *
   * @return A list of all {@link BookEntity} objects representing the books
   * in the library.
   */
  List<BookEntity> getAllBooks();

  /**
   * Finds a book using its ISBN number.
   *
   * @param isbn The ISBN of the book to find.
   * @return The {@link BookEntity} corresponding to the provided ISBN.
   * @throws LibraryServiceException If the book cannot be found.
   */
  BookEntity findByIsbn(long isbn) throws LibraryServiceException;

  /**
   * Removes a book from the library using its ISBN number.
   *
   * @param val The ISBN of the book to be removed.
   * @throws LibraryServiceException If the book cannot be found or removed.
   */
  void removeBookByIsbn(long val) throws LibraryServiceException;

  /**
   * Adds a new book to the library based on the provided request data.
   *
   * @param bookRequest The {@link AddBookRequestDto} containing the new book's
   * data.
   * @return The newly created {@link BookEntity}.
   * @throws LibraryServiceException If the book cannot be added.
   */
  BookEntity addBook(AddBookRequestDto bookRequest)
        throws LibraryServiceException;

  /**
   * Retrieves a list of all authors in the library.
   *
   * @return A list of all {@link AuthorEntity} objects representing the
   * authors.
   */
  List<AuthorEntity> getAllAuthors();

}
