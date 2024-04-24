
package es.org.cxn.backapp.service;

import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;

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
  List<PersistentBookEntity> getAllBooks();

  /**
   * Find book using isbn.
   *
   * @param l the isbn.
   * @return Book entity found.
   * @throws LibraryServiceException When book cannot found or cant find..
   */
  BookEntity findByIsbn(long l) throws LibraryServiceException;

  /**
   * Remove book found using isbn.
   *
   * @param val The isbn.
   * @throws LibraryServiceException When book not found or cannot find.
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
   */
  List<PersistentAuthorEntity> getAllAuthors();

}
