package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import es.org.cxn.backapp.model.AuthorEntity;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.form.requests.AuthorRequest;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.BookEntityRepository;
import es.org.cxn.backapp.service.exceptions.LibraryServiceException;
import es.org.cxn.backapp.service.impl.DefaultLibraryService;

/**
 * Unit tests for the {@link DefaultLibraryService}.
 *
 * This class is designed to verify the behavior and functionality of the
 * library service, which handles operations related to books and authors within
 * the library system. It uses mocked dependencies to isolate and test the
 * service logic.
 *
 * @see DefaultLibraryService
 */
class LibraryServiceTest {

    /**
     * Constant representing the ISBN of the book. This value is used to identify
     * the book in the system.
     */
    private static final long BOOK_ISBN = 12345L;

    /**
     * Constant representing the publication date of the book. The publication date
     * is set to March 24, 2022.
     */
    private static final LocalDate BOOK_PUBLISH_DATE = LocalDate.of(2022, 3, 24);

    /**
     * Constant representing the first name of the author of the book. The author's
     * first name is set to "John".
     */
    private static final String AUTHOR_FIRST_NAME = "John";

    /**
     * Constant representing the last name of the author of the book. The author's
     * last name is set to "Doe".
     */
    private static final String AUTHOR_LAST_NAME = "Doe";

    /**
     * Constant representing the nationality of the author of the book. The author's
     * nationality is set to "USA".
     */
    private static final String AUTHOR_NATIONALITY = "USA";

    /**
     * Constant representing the title of the book. The book title is set to "Book
     * Title".
     */
    private static final String BOOK_TITLE = "Book Title";

    /**
     * Constant representing the gender or genre of the book. The book's genre is
     * set to "Fiction".
     */
    private static final String BOOK_GENDER = "Fiction";

    /**
     * Mocked repository for managing book entities.
     *
     * This repository is used to simulate interactions with the database for
     * operations such as adding, retrieving, updating, or deleting books.
     *
     * @see BookEntityRepository
     */
    @Mock
    private BookEntityRepository libraryRepository;

    /**
     * Mocked repository for managing author entities.
     *
     * This repository is used to simulate interactions with the database for
     * operations related to author management.
     *
     * @see AuthorEntityRepository
     */
    @Mock
    private AuthorEntityRepository authorRepository;

    /**
     * Service under test that handles library operations.
     *
     * This service is the main class being tested and contains the business logic
     * for managing books and authors in the library system.
     *
     * @see DefaultLibraryService
     */
    @InjectMocks
    private DefaultLibraryService defaultLibraryService;

    /**
     * Example book entity for testing purposes.
     *
     * This field represents a mock or example book entity used in the tests to
     * simulate realistic data.
     */
    private PersistentBookEntity book1;

    /**
     * Another example book entity for testing purposes.
     *
     * This field represents a second mock or example book entity to provide varied
     * data during tests.
     */
    private PersistentBookEntity book2;

    @Test
    void addBookAuthorAlreadyExistsAddsAuthorToBook() throws LibraryServiceException {
        // Arrange

        final AddBookRequestDto bookRequest = new AddBookRequestDto(BOOK_ISBN, // ISBN
                BOOK_TITLE, // Title
                BOOK_GENDER, // Gender
                BOOK_PUBLISH_DATE, // Publish Year
                "English", // Language
                Arrays.asList(new AuthorRequest(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME, AUTHOR_NATIONALITY)) // Authors
                                                                                                          // list
        );

        PersistentAuthorEntity existingAuthor = new PersistentAuthorEntity();
        existingAuthor.setFirstName(AUTHOR_FIRST_NAME);
        existingAuthor.setLastName(AUTHOR_LAST_NAME);
        existingAuthor.setNationality(AUTHOR_NATIONALITY);

        // Mock the authorRepository to return the existing author
        when(authorRepository.findByFirstNameAndLastNameAndNationality(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME,
                AUTHOR_NATIONALITY)).thenReturn(existingAuthor);

        // Mock the book repository to save the book
        PersistentBookEntity book = new PersistentBookEntity();
        var authors = new HashSet<PersistentAuthorEntity>();
        authors.add(existingAuthor);
        book.setAuthors(authors);
        when(libraryRepository.save(any(PersistentBookEntity.class))).thenReturn(book);

        // Act
        BookEntity result = defaultLibraryService.addBook(bookRequest);

        // Assert that the author is added to the book's authors list
        assertEquals(1, result.getAuthors().size());
        assertTrue(result.getAuthors().contains(existingAuthor));

        // Verify that the authorRepository was called
        verify(authorRepository, times(1)).findByFirstNameAndLastNameAndNationality(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME,
                AUTHOR_NATIONALITY);

        // Verify that the bookRepository's save method was called
        verify(libraryRepository, times(1)).save(any(PersistentBookEntity.class));
    }

    @Test
    void addBookDataIntegrityViolationThrowsLibraryServiceException() {
        // Arrange
        AddBookRequestDto bookRequest = new AddBookRequestDto(BOOK_ISBN, // ISBN
                BOOK_TITLE, // Title
                BOOK_GENDER, // Gender
                BOOK_PUBLISH_DATE, // Publish Year
                "English", // Language
                Arrays.asList(new AuthorRequest(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME, AUTHOR_NATIONALITY)) // Authors
                                                                                                          // list
        );

        when(libraryRepository.save(any(PersistentBookEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        // Act & Assert
        LibraryServiceException thrownException = assertThrows(LibraryServiceException.class,
                () -> defaultLibraryService.addBook(bookRequest));

        // Assert that the exception contains the expected message
        assertEquals("Data integrity violation", thrownException.getMessage());
    }

    @Test
    void addBookValidBookAddsBookSuccessfully() throws LibraryServiceException {
        // Arrange

        AddBookRequestDto bookRequest = new AddBookRequestDto(BOOK_ISBN, "Book 1", BOOK_GENDER, BOOK_PUBLISH_DATE,
                "English", Arrays.asList(new AuthorRequest(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME, AUTHOR_NATIONALITY)));

        PersistentBookEntity book = new PersistentBookEntity();
        when(libraryRepository.save(any(PersistentBookEntity.class))).thenReturn(book);
        when(authorRepository.findByFirstNameAndLastNameAndNationality(anyString(), anyString(), anyString()))
                .thenReturn(null);
        when(authorRepository.save(any(PersistentAuthorEntity.class))).thenReturn(new PersistentAuthorEntity());

        // Act
        BookEntity addedBook = defaultLibraryService.addBook(bookRequest);

        // Assert
        assertNotNull(addedBook);
        verify(libraryRepository, times(1)).save(any(PersistentBookEntity.class));
    }

    @Test
    void findByIsbnBookNotFoundThrowsException() {
        // Arrange
        when(libraryRepository.findById(BOOK_ISBN)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LibraryServiceException.class, () -> defaultLibraryService.findByIsbn(BOOK_ISBN));
    }

    @Test
    void findByIsbnExistingBookReturnsBook() throws LibraryServiceException {
        // Arrange
        PersistentBookEntity book = new PersistentBookEntity();
        when(libraryRepository.findById(BOOK_ISBN)).thenReturn(Optional.of(book));

        // Act
        BookEntity foundBook = defaultLibraryService.findByIsbn(BOOK_ISBN);

        // Assert
        assertNotNull(foundBook);
    }

    @Test
    void getAllAuthorsReturnsListOfAuthors() {
        // Arrange
        PersistentAuthorEntity author1 = new PersistentAuthorEntity();
        author1.setFirstName(AUTHOR_FIRST_NAME);
        PersistentAuthorEntity author2 = new PersistentAuthorEntity();
        author2.setFirstName("Jane");

        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        // Act
        List<AuthorEntity> authors = defaultLibraryService.getAllAuthors();

        // Assert
        assertEquals(2, authors.size());
        assertEquals(AUTHOR_FIRST_NAME, authors.get(0).getFirstName());
        assertEquals("Jane", authors.get(1).getFirstName());
    }

    @Test
    void getAllBooksReturnsListOfBooks() {
        // Arrange
        when(libraryRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<BookEntity> books = defaultLibraryService.getAllBooks();

        // Assert
        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        assertEquals("Book 2", books.get(1).getTitle());
    }

    @Test
    void removeBookByIsbnBookExistsRemovesBook() throws LibraryServiceException {
        // Arrange

        when(libraryRepository.existsById(BOOK_ISBN)).thenReturn(true);

        // Act
        defaultLibraryService.removeBookByIsbn(BOOK_ISBN);

        // Assert
        verify(libraryRepository, times(1)).deleteById(BOOK_ISBN);
    }

    @Test
    void removeBookByIsbnBookNotFoundThrowsException() {
        // Arrange
        when(libraryRepository.existsById(BOOK_ISBN)).thenReturn(false);

        // Act & Assert
        assertThrows(LibraryServiceException.class, () -> defaultLibraryService.removeBookByIsbn(BOOK_ISBN));
    }

    @Test
    void removeBookByIsbnInvalidIsbnThrowsLibraryServiceException() {
        // Arrange
        long invalidIsbn = BOOK_ISBN;

        // Simulate that the repository method throws an IllegalArgumentException
        when(libraryRepository.existsById(invalidIsbn)).thenReturn(true);
        doThrow(new IllegalArgumentException("Invalid ISBN")).when(libraryRepository).deleteById(invalidIsbn);

        // Act & Assert
        assertThrows(LibraryServiceException.class, () -> defaultLibraryService.removeBookByIsbn(invalidIsbn));
    }

    /**
     * Initializes test data and mocks before each test method is executed.
     *
     * This method can be used to set up common test scenarios, such as initializing
     * example book entities or configuring mock behavior.
     */
    @BeforeEach
    void setup() {
        // Placeholder for initializing test data and mocks
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book1 = new PersistentBookEntity();
        book1.setTitle("Book 1");
        book2 = new PersistentBookEntity();
        book2.setTitle("Book 2");
    }
}
