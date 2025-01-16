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

class LibraryServiceTest {

    @Mock
    private BookEntityRepository libraryRepository;

    @Mock
    private AuthorEntityRepository authorRepository;

    @InjectMocks
    private DefaultLibraryService defaultLibraryService;

    private PersistentBookEntity book1;
    private PersistentBookEntity book2;

    @Test
    void addBook_authorAlreadyExists_addsAuthorToBook() throws LibraryServiceException {
        // Arrange
        AddBookRequestDto bookRequest = new AddBookRequestDto(12345L, // ISBN
                "Book Title", // Title
                "Fiction", // Gender
                LocalDate.of(2022, 1, 1), // Publish Year
                "English", // Language
                Arrays.asList(new AuthorRequest("John", "Doe", "USA")) // Authors list
        );

        PersistentAuthorEntity existingAuthor = new PersistentAuthorEntity();
        existingAuthor.setFirstName("John");
        existingAuthor.setLastName("Doe");
        existingAuthor.setNationality("USA");

        // Mock the authorRepository to return the existing author
        when(authorRepository.findByFirstNameAndLastNameAndNationality("John", "Doe", "USA"))
                .thenReturn(existingAuthor);

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
        verify(authorRepository, times(1)).findByFirstNameAndLastNameAndNationality("John", "Doe", "USA");

        // Verify that the bookRepository's save method was called
        verify(libraryRepository, times(1)).save(any(PersistentBookEntity.class));
    }

    @Test
    void addBook_dataIntegrityViolation_throwsLibraryServiceException() {
        // Arrange
        AddBookRequestDto bookRequest = new AddBookRequestDto(12345L, // ISBN
                "Book Title", // Title
                "Fiction", // Gender
                LocalDate.of(2022, 1, 1), // Publish Year
                "English", // Language
                Arrays.asList(new AuthorRequest("John", "Doe", "USA")) // Authors list
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
    void addBook_validBook_addsBookSuccessfully() throws LibraryServiceException {
        // Arrange
        AddBookRequestDto bookRequest = new AddBookRequestDto(12345L, "Book 1", "Fiction", LocalDate.of(2022, 1, 1),
                "English", Arrays.asList(new AuthorRequest("John", "Doe", "USA")));

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
    void findByIsbn_bookNotFound_throwsException() {
        // Arrange
        long isbn = 12345L;
        when(libraryRepository.findById(isbn)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LibraryServiceException.class, () -> defaultLibraryService.findByIsbn(isbn));
    }

    @Test
    void findByIsbn_existingBook_returnsBook() throws LibraryServiceException {
        // Arrange
        long isbn = 12345L;
        PersistentBookEntity book = new PersistentBookEntity();
        when(libraryRepository.findById(isbn)).thenReturn(Optional.of(book));

        // Act
        BookEntity foundBook = defaultLibraryService.findByIsbn(isbn);

        // Assert
        assertNotNull(foundBook);
    }

    @Test
    void getAllAuthors_returnsListOfAuthors() {
        // Arrange
        PersistentAuthorEntity author1 = new PersistentAuthorEntity();
        author1.setFirstName("John");
        PersistentAuthorEntity author2 = new PersistentAuthorEntity();
        author2.setFirstName("Jane");

        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        // Act
        List<AuthorEntity> authors = defaultLibraryService.getAllAuthors();

        // Assert
        assertEquals(2, authors.size());
        assertEquals("John", authors.get(0).getFirstName());
        assertEquals("Jane", authors.get(1).getFirstName());
    }

    @Test
    void getAllBooks_returnsListOfBooks() {
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
    void removeBookByIsbn_bookExists_removesBook() throws LibraryServiceException {
        // Arrange
        long isbn = 12345L;
        when(libraryRepository.existsById(isbn)).thenReturn(true);

        // Act
        defaultLibraryService.removeBookByIsbn(isbn);

        // Assert
        verify(libraryRepository, times(1)).deleteById(isbn);
    }

    @Test
    void removeBookByIsbn_bookNotFound_throwsException() {
        // Arrange
        long isbn = 12345L;
        when(libraryRepository.existsById(isbn)).thenReturn(false);

        // Act & Assert
        assertThrows(LibraryServiceException.class, () -> defaultLibraryService.removeBookByIsbn(isbn));
    }

    @Test
    void removeBookByIsbn_invalidIsbn_throwsLibraryServiceException() {
        // Arrange
        long invalidIsbn = 12345L;

        // Simulate that the repository method throws an IllegalArgumentException
        when(libraryRepository.existsById(invalidIsbn)).thenReturn(true);
        doThrow(new IllegalArgumentException("Invalid ISBN")).when(libraryRepository).deleteById(invalidIsbn);

        // Act & Assert
        assertThrows(LibraryServiceException.class, () -> defaultLibraryService.removeBookByIsbn(invalidIsbn));
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
