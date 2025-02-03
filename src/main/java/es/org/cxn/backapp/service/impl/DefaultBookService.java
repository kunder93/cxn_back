
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.member_resources.AddBookRequestDto;
import es.org.cxn.backapp.model.form.requests.member_resources.AuthorRequest;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.BookEntityRepository;
import es.org.cxn.backapp.service.BookService;
import es.org.cxn.backapp.service.ImageStorageService;
import es.org.cxn.backapp.service.dto.AuthorDataDto;
import es.org.cxn.backapp.service.dto.BookDataImageDto;
import es.org.cxn.backapp.service.exceptions.BookServiceException;
import jakarta.transaction.Transactional;

/**
 * Default implementation of the {@link BookService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public class DefaultBookService implements BookService {

    /**
     * Path for profile's image.
     */
    @Value("${book.location.covers}")
    private String imageLocation;

    /**
     * Repository for the book entities handled by the service.
     */
    private final BookEntityRepository bookRepository;

    /**
     * Repository for the author entities handled by the service.
     */
    private final AuthorEntityRepository authorRepository;

    /**
     * The image storage service for save and load book covers.
     */
    private final ImageStorageService imageStorageService;

    /**
     * Constructs an entities service with the specified repositories.
     *
     * @param repoBook         The book repository{@link BookEntityRepository}
     * @param repoAuth         The author repository{@link AuthorEntityRepository}
     * @param imageStorageServ THe image storage service.
     *
     */
    public DefaultBookService(final BookEntityRepository repoBook, final AuthorEntityRepository repoAuth,
            final ImageStorageService imageStorageServ) {
        super();
        imageStorageService = checkNotNull(imageStorageServ, "Received a null pointer as image service.");
        bookRepository = checkNotNull(repoBook, "Received a null pointer as library repository");
        authorRepository = checkNotNull(repoAuth, "Received a null pointer as author repository");
    }

    /**
     * Add new book. Authors list in AddBookRequestDto cannot be null.
     *
     * @param bookRequest The book data.
     * @param imageCover  The book image file.
     *
     * @throws BookServiceException When cannot add book.
     */
    @Transactional
    @Override
    public BookEntity add(final AddBookRequestDto bookRequest, final MultipartFile imageCover)
            throws BookServiceException {
        final var book = new PersistentBookEntity();
        book.setTitle(bookRequest.title());
        book.setDescription(bookRequest.description());
        book.setGenre(bookRequest.genre());
        book.setIsbn(bookRequest.isbn());
        book.setGenre(bookRequest.genre());
        book.setLanguage(bookRequest.language());
        book.setPublishYear(bookRequest.publishDate());

        final var authorsList = bookRequest.authors();
        authorsList.forEach((AuthorRequest authorRequestDto) -> {
            final var existingAuthor = authorRepository.findByFirstNameAndLastName(authorRequestDto.firstName(),
                    authorRequestDto.lastName());
            if (existingAuthor != null) {
                final var bookAuthors = book.getAuthors();
                bookAuthors.add(existingAuthor);
                book.setAuthors(bookAuthors);

            } else {
                final var authorEntity = new PersistentAuthorEntity();
                authorEntity.setFirstName(authorRequestDto.firstName());
                authorEntity.setLastName(authorRequestDto.lastName());
                final var authorSaved = authorRepository.save(authorEntity);
                final var bookAuthors = book.getAuthors();
                bookAuthors.add(authorSaved);
                book.setAuthors(bookAuthors);
            }
        });
        try {
            final var imageSoruce = imageStorageService.saveImage(imageCover, imageLocation, "book", book.getIsbn());
            book.setCoverSrc(imageSoruce);
        } catch (IOException ex) {
            throw new BookServiceException("Book cover cannot be saved");
        }
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new BookServiceException(e.getMessage(), e);
        }
    }

    /**
     * Finds a book using its ISBN.
     *
     * @param val the ISBN of the book to find
     * @return the found {@link BookEntity}
     * @throws BookServiceException if the book is not found or if the ISBN is null
     */
    @Override
    public BookEntity find(final String val) throws BookServiceException {
        checkNotNull(val, "Received a null val as book identifier isbn.");
        final var optionalBook = bookRepository.findById(val);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        } else {
            throw new BookServiceException("aa");
        }
    }

    /**
     * Retrieves the image of a book cover using its ISBN.
     *
     * @param isbn the ISBN of the book
     * @return a byte array containing the book cover image
     * @throws BookServiceException if the book is not found or if the image cannot
     *                              be loaded
     */
    @Override
    public byte[] findImage(final String isbn) throws BookServiceException {
        final var book = find(isbn);
        try {
            return imageStorageService.loadImage(book.getCoverSrc());
        } catch (IOException e) {
            throw new BookServiceException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all books from the repository and converts them to
     * {@link BookDataImageDto}.
     *
     * @return a list of {@link BookDataImageDto} containing book details and their
     *         associated images
     */
    @Override
    public List<BookDataImageDto> getAll() {
        final var persistentBooks = bookRepository.findAll();

        Set<BookDataImageDto> dtoSet = persistentBooks.stream().map((PersistentBookEntity book) ->
        // Convert PersistentBookEntity to BookDataImageDto
        new BookDataImageDto(book.getIsbn(), book.getTitle(), book.getDescription(), book.getPublishYear().toString(),
                book.getLanguage(), book.getGenre(),
                book.getAuthors().stream().map(author -> new AuthorDataDto(author.getFirstName(), author.getLastName()))
                        .collect(Collectors.toSet())
        // Include the image bytes
        )).collect(Collectors.toSet()); // Use Collectors.toSet() for immutability

        // Return the result as a list of BookDataImageDto
        return new ArrayList<>(dtoSet); // Convert Set to List
    }

    /**
     * Removes a book from the repository using its ISBN.
     *
     * @param isbn the ISBN of the book to remove
     * @throws BookServiceException if the book is not found or if the removal fails
     */
    @Override
    public void remove(final String isbn) throws BookServiceException {
        try {
            // Check if the book exists
            if (bookRepository.existsById(isbn)) {
                // If it exists, remove the book
                bookRepository.deleteById(isbn);
            } else {
                throw new BookServiceException("Book not found");
            }
        } catch (IllegalArgumentException e) {
            throw new BookServiceException("Failed to remove the book", e);
        }
    }
}
