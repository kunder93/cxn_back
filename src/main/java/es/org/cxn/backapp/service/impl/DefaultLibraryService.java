
package es.org.cxn.backapp.service.impl;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.org.cxn.backapp.model.AuthorEntity;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.form.requests.AuthorRequest;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.BookEntityRepository;
import es.org.cxn.backapp.service.LibraryService;
import es.org.cxn.backapp.service.exceptions.LibraryServiceException;

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
    public DefaultLibraryService(final BookEntityRepository repoLibr, final AuthorEntityRepository repoAuth) {
        super();

        libraryRepository = checkNotNull(repoLibr, "Received a null pointer as library repository");
        authorRepository = checkNotNull(repoAuth, "Received a null pointer as author repository");
    }

    /**
     * Add new book. Authors list in AddBookRequestDto cannot be null.
     *
     * @throws LibraryServiceException When cannot add book.
     */
    @Override
    public BookEntity addBook(final AddBookRequestDto bookRequest) throws LibraryServiceException {
        final var book = new PersistentBookEntity();
        book.setTitle(bookRequest.title());
        book.setIsbn(bookRequest.isbn());
        book.setGender(bookRequest.gender());
        book.setLanguage(bookRequest.language());
        book.setPublishYear(bookRequest.publishYear());

        final var authorsList = bookRequest.authorsList();
        authorsList.forEach((AuthorRequest authorRequestDto) -> {
            final var existingAuthor = authorRepository.findByFirstNameAndLastNameAndNationality(
                    authorRequestDto.firstName(), authorRequestDto.lastName(), authorRequestDto.nationality());
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
     * Get all authors.
     */
    @Override
    public List<AuthorEntity> getAllAuthors() {
        final var persistentAuthors = authorRepository.findAll();
        // Convertir cada PersistentAuthorEntity a AuthorEntity
        return persistentAuthors.stream().map(AuthorEntity.class::cast).toList();
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
}
