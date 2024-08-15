
package es.org.cxn.backapp.service;

import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.AuthorEntity;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;

import java.util.List;

/**
 * Interface for service that handles library.
 *
 * @author Santiago Paz.
 *
 */
public interface LibraryService {

  /**
   * @return List with all books.
   */
  List<BookEntity> getAllBooks();

  /**
   * Find book using isbn.
   *
   * @param isbn the isbn.
   * @return Book entity found.
   * @throws LibraryServiceException When book cannot be found.
   */
  BookEntity findByIsbn(long isbn) throws LibraryServiceException;

  /**
   * Remove book found using isbn.
   *
   * @param val The isbn.
   * @throws LibraryServiceException When book cannot be found.
   */
  void removeBookByIsbn(long val) throws LibraryServiceException;

  /**
   * Add book with provided info params.
   *
   * @param bookRequest The book request with book data.
   * @return The book entity created.
   * @throws LibraryServiceException When cannot add book.
   */
  BookEntity addBook(AddBookRequestDto bookRequest)
        throws LibraryServiceException;

  /**
   * Get all authors.
   * @return List wiht authors entities.
   */
  List<AuthorEntity> getAllAuthors();

}
