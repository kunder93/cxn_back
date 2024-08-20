
package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.AuthorEntity;
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
  public List<BookEntity> getAllBooks() {
    final var persistentBooks = libraryRepository.findAll();

    // PersistentBookEntity a BookEntity
    return persistentBooks.stream().map(BookEntity.class::cast).toList();
  }

  /**
   * Add new book.
   * @throws LibraryServiceException When cannot add book.
   */
  @Override
  public BookEntity addBook(final AddBookRequestDto bookRequest)
        throws LibraryServiceException {
    final var book = new PersistentBookEntity();
    book.setTitle(bookRequest.title());
    book.setIsbn(bookRequest.isbn());
    book.setGender(bookRequest.gender());
    book.setLanguage(bookRequest.language());
    book.setPublishYear(bookRequest.publishYear());
    final var authorsList = bookRequest.authorsList();
    if (authorsList != null) {
      authorsList.forEach((AuthorRequestDto authorRequestDto) -> {
        final var existingAuthor =
              authorRepository.findByFirstNameAndLastNameAndNationality(
                    authorRequestDto.firstName(), authorRequestDto.lastName(),
                    authorRequestDto.nationality()
              );
        if (existingAuthor != null) {
          final var bookAuthors = book.getAuthors();
          bookAuthors.add(existingAuthor);
          book.setAuthors(bookAuthors);

        } else {
          final var authorEntity = new PersistentAuthorEntity();
          authorEntity.setFirstName(authorRequestDto.firstName());
          authorEntity.setLastName(authorRequestDto.lastName());
          authorEntity.setNationality(authorRequestDto.nationality());
          final var authorSaved = authorRepository.save(authorEntity);
          final var bookAuthors = book.getAuthors();
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
    final var optionalBook = libraryRepository.findById(val);
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
  public List<AuthorEntity> getAllAuthors() {
    final var persistentAuthors = authorRepository.findAll();
    // Convertir cada PersistentAuthorEntity a AuthorEntity
    return persistentAuthors.stream().map(AuthorEntity.class::cast).toList();
  }
}
