
package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.form.requests.AuthorRequestDto;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.BookEntityRepository;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link LibraryService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public class DefaultLibraryService implements LibraryService {

  /**
   * Repository for the book entities handled by the service.
   */
  private final BookEntityRepository libraryRepository;

  /**
   * Repository for the author entities handled by the service.
   */
  private final AuthorEntityRepository authorRepository;

  /**
   * Constructs an entities service with the specified repositories.
   *
   * @param repoLibr The book repository{@link BookEntityRepository}
   * @param repoAuth The author repository{@link AuthorEntityRepository}
   *
   */
  public DefaultLibraryService(
        final BookEntityRepository repoLibr,
        final AuthorEntityRepository repoAuth
  ) {
    super();

    libraryRepository = checkNotNull(
          repoLibr, "Received a null pointer as library repository"
    );
    authorRepository = checkNotNull(
          repoAuth, "Received a null pointer as author repository"
    );
  }

  /**
   * Get all books.
   */
  @Override
  public List<PersistentBookEntity> getAllBooks() {
    return libraryRepository.findAll();
  }

  /**
   * Add new book.
   * @throws LibraryServiceException When cannot add book.
   */
  @Override
  public BookEntity addBook(final AddBookRequestDto bookRequest)
        throws LibraryServiceException {
    var book = new PersistentBookEntity();
    book.setTitle(bookRequest.getTitle());
    book.setIsbn(bookRequest.getIsbn());
    book.setGender(bookRequest.getGender());
    book.setLanguage(bookRequest.getLanguage());
    book.setPublishYear(bookRequest.getPublishYear());
    var authorsList = bookRequest.getAuthorsList();
    if (authorsList != null) {
      authorsList.forEach((AuthorRequestDto authorRequestDto) -> {
        var existingAuthor =
              authorRepository.findByFirstNameAndLastNameAndNationality(
                    authorRequestDto.getFirstName(),
                    authorRequestDto.getLastName(),
                    authorRequestDto.getNationality()
              );
        if (existingAuthor != null) {
          var bookAuthors = book.getAuthors();
          bookAuthors.add(existingAuthor);
          book.setAuthors(bookAuthors);

        } else {
          var authorEntity = new PersistentAuthorEntity();
          authorEntity.setFirstName(authorRequestDto.getFirstName());
          authorEntity.setLastName(authorRequestDto.getLastName());
          authorEntity.setNationality(authorRequestDto.getNationality());
          var authorSaved = authorRepository.save(authorEntity);
          var bookAuthors = book.getAuthors();
          bookAuthors.add(authorSaved);
          book.setAuthors(bookAuthors);
        }
      });
    }
    try {
      return libraryRepository.save(book);
    } catch (DataIntegrityViolationException e) {
      throw new LibraryServiceException(e.getMessage(), e);
    }
  }

  /**
   * Find book using isbn.
   */
  @Override
  public BookEntity findByIsbn(final long val) throws LibraryServiceException {
    checkNotNull(val, "Received a null val as book identifier isbn.");
    var optionalBook = libraryRepository.findById(val);
    if (optionalBook.isPresent()) {
      return optionalBook.get();
    } else {
      throw new LibraryServiceException("aa");
    }
  }

  /**
   * Remove book using isbn.
   */
  @Override
  public void removeBookByIsbn(final long isbn) throws LibraryServiceException {
    try {
      // Check if the book exists
      if (libraryRepository.existsById(isbn)) {
        // If it exists, remove the book
        libraryRepository.deleteById(isbn);
      } else {
        throw new LibraryServiceException("Book not found");
      }
    } catch (IllegalArgumentException e) {
      throw new LibraryServiceException("Failed to remove the book", e);
    }
  }

  /**
   * Get all authors.
   */
  @Override
  public List<PersistentAuthorEntity> getAllAuthors() {
    return authorRepository.findAll();
  }

}
