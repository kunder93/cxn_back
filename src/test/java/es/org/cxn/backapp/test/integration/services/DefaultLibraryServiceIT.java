
package es.org.cxn.backapp.test.integration.services;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.form.requests.member_resources.AddBookRequestDto;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.BookEntityRepository;
import es.org.cxn.backapp.service.BookService;
import es.org.cxn.backapp.service.ImageStorageService;
import es.org.cxn.backapp.service.exceptions.BookServiceException;
import es.org.cxn.backapp.service.impl.DefaultBookService;
import es.org.cxn.backapp.service.impl.DefaultImageStorageService;

/**
 * Unit tests for {@link DefaultLibraryService}.
 * <p>
 * This class contains tests to verify the functionality of the
 * {@link DefaultLibraryService} service. It ensures that various methods of the
 * service behave as expected and handle different scenarios correctly. The
 * tests use mocks to simulate interactions with dependencies.
 * </p>
 *
 * @author Santiago Paz
 */
@SpringBootTest(classes = { BookEntityRepository.class, BookService.class, DefaultBookService.class,
        ImageStorageService.class, DefaultImageStorageService.class })
@ActiveProfiles("test")
final class DefaultLibraryServiceIT {
    /**
     * ISBN number for the first book used in test cases.
     * <p>
     * This ISBN is used to simulate a book with a specific identifier in various
     * test scenarios.
     * </p>
     */
    private static final String BOOK_ISBN_1 = "1231231231";

    /**
     * ISBN number for the second book used in test cases.
     * <p>
     * This ISBN is used to simulate another book with a different identifier in
     * various test scenarios.
     * </p>
     */
    private static final String BOOK_ISBN_2 = "1231231231231";

    /**
     * ISBN number for the third book used in test cases.
     * <p>
     * This ISBN is used to simulate a third book with yet another identifier in
     * various test scenarios.
     * </p>
     */
    private static final String BOOK_ISBN_3 = "3213213211";

    /**
     * ISBN number used for adding a new book in test cases.
     * <p>
     * This ISBN is used to test the addition of a new book to the repository and to
     * verify that the service handles book creation correctly.
     * </p>
     */
    private static final String ADD_BOOK_ISBN = "3213213214";

    /**
     * ISBN number for a non-existing book used in test cases.
     * <p>
     * This ISBN is used to simulate a scenario where a book with the specified
     * identifier does not exist in the repository, allowing tests to verify how the
     * service handles the removal or retrieval of non-existing books.
     * </p>
     */
    private static final String NON_EXISTING_BOOK_ISBN = "1112223331";

    /**
     * Title for the book used in test cases.
     * <p>
     * This title is used to simulate a book with a specific title in various test
     * scenarios.
     * </p>
     */
    private static final String BOOK_TITLE = "book title";

    /**
     * Gender of the book used in test cases.
     * <p>
     * This gender is used to simulate a book belonging to a specific genre in
     * various test scenarios.
     * </p>
     */
    private static final String BOOK_GENDER = "Terror";

    /**
     * Publishing year of the book used in test cases.
     * <p>
     * This year is used to simulate the publication date of a book in various test
     * scenarios.
     * </p>
     */
    private static final LocalDate BOOK_PUBLISH_YEAR = LocalDate.now();

    /**
     * Language of the book used in test cases.
     * <p>
     * This language is used to simulate a book being published in a specific
     * language in various test scenarios.
     * </p>
     */
    private static final String BOOK_LANGUAGE = "Spanish";

    /**
     * First name of the author used in test cases.
     * <p>
     * This first name is used to simulate an author with a specific first name in
     * various test scenarios.
     * </p>
     */
    private static final String AUTHOR_FIRST_NAME = "Alfonso";

    /**
     * Last name of the author used in test cases.
     * <p>
     * This last name is used to simulate an author with a specific last name in
     * various test scenarios.
     * </p>
     */
    private static final String AUTHOR_LAST_NAME = "Rueda";

    /**
     * Nationality of the author used in test cases.
     * <p>
     * This nationality is used to simulate an author with a specific nationality in
     * various test scenarios.
     * </p>
     */
    private static final String AUTHOR_NATIONALITY = "Spain";

    /**
     * Mocked image file.
     */
    final MultipartFile mockFile = mock(MultipartFile.class);

    /**
     * Mock image storage service.
     */
    @Mock
    private ImageStorageService imageStorageService;

    /**
     * The {@link BookService} bean used in the tests.
     * <p>
     * This bean is injected into the test class and is used to call service methods
     * to be tested.
     * </p>
     */
    @Autowired
    private BookService bookService;

    /**
     * Mock of the {@link BookEntityRepository}.
     * <p>
     * This mock is used to simulate interactions with the book repository without
     * invoking the actual data access layer.
     * </p>
     */
    @MockitoBean
    private BookEntityRepository libraryRepository;

    /**
     * Mock of the {@link AuthorEntityRepository}.
     * <p>
     * This mock is used to simulate interactions with the author repository without
     * invoking the actual data access layer.
     * </p>
     */
    @MockitoBean
    private AuthorEntityRepository authorRepository;

    /**
     * Initializes the mocks before each test.
     * <p>
     * This method is called before each test method to set up the mocks and prepare
     * the test environment.
     * </p>
     *
     * @throws IOException image storage service exception.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockFile.getOriginalFilename()).thenReturn("CoolName");

    }

    /**
     * Tests the {@link DefaultLibraryService#addBook(AddBookRequestDto)} method
     * when a null book is provided.
     * <p>
     * This test verifies that a {@link NullPointerException} is thrown when trying
     * to add a null book.
     * </p>
     *
     * @throws IOException
     */
    @Test
    void testAddNullBookRaisesServiceException() {

        // Arrange
        AddBookRequestDto book1 = null;

        // Act and Assert
        Assertions.assertThrows(NullPointerException.class, () -> bookService.add(book1, mockFile),
                "Null book should raise NullPointerException.");
    }

    /**
     * Tests the {@link DefaultLibraryService#findByIsbn(Long)} method.
     * <p>
     * This test verifies that the service correctly finds a book by its ISBN and
     * returns the expected book entity.
     * </p>
     *
     * @throws LibraryServiceException if an error occurs during the book retrieval
     */
    @Test
    void testFindBookByISBN() throws BookServiceException {
        var bookOptional = Optional.of(PersistentBookEntity.builder().isbn(ADD_BOOK_ISBN).title("Book 1").build());

        // Mock the repository to return the expected book when findById() is called
        when(libraryRepository.findById(ADD_BOOK_ISBN)).thenReturn(bookOptional);

        var bookFound = bookService.find(ADD_BOOK_ISBN);
        verify(libraryRepository, times(1)).findById(ADD_BOOK_ISBN);
        Assertions.assertEquals(bookFound, bookOptional.get(), "Returned book should match the expected book.");
    }

    /**
     * Tests the {@link DefaultLibraryService#findByIsbn(Long)} method with a null
     * ISBN value.
     * <p>
     * This test verifies that a {@link LibraryServiceException} is thrown when a
     * null ISBN is provided.
     * </p>
     */
    @Test
    void testFindByIsbnWithNullValue() {
        // Arrange: Prepare the test scenario
        var isbn = ""; // Choose a valid ISBN value here

        // Act and Assert: Test the behavior when a null value is passed
        Assertions.assertThrows(BookServiceException.class, () -> {
            bookService.find(isbn);
        }, "Null ISBN should raise LibraryServiceException.");
    }

    /**
     * Tests the {@link DefaultLibraryService#removeBookByIsbn(Long)} method.
     * <p>
     * This test verifies that the service correctly removes a book by its ISBN when
     * the book exists in the repository.
     * </p>
     */
    @Test
    void testRemoveBookWithIsbn() {
        // Arrange
        when(libraryRepository.existsById(ADD_BOOK_ISBN)).thenReturn(Boolean.TRUE);
        doNothing().when(libraryRepository).deleteById(ADD_BOOK_ISBN);

        // Act
        try {
            bookService.remove(ADD_BOOK_ISBN);
        } catch (BookServiceException e) {
            // Handle exceptions as needed in your test
        }

        // Assert
        verify(libraryRepository, times(1)).deleteById(ADD_BOOK_ISBN);
    }

    /**
     * Tests the {@link DefaultLibraryService#removeBookByIsbn(Long)} method with a
     * null ISBN value.
     * <p>
     * This test verifies that a {@link LibraryServiceException} is thrown when a
     * null ISBN is provided and ensures that the delete method is not called.
     * </p>
     */
    @Test
    void testRemoveBookWithNullIsbn() {
        // Arrange
        String nullIsbn = "";

        // Act and Assert
        Assertions.assertThrows(BookServiceException.class, () -> {
            bookService.remove(nullIsbn);
        }, "Null ISBN should raise LibraryServiceException.");

        // Ensure that libraryRepository.deleteById is not called
        verify(libraryRepository, never()).deleteById(anyString());
    }

    /**
     * Tests the {@link DefaultLibraryService#removeBookByIsbn(Long)} method when
     * attempting to remove a non-existing book.
     * <p>
     * This test verifies that a {@link LibraryServiceException} is thrown when
     * trying to remove a book that does not exist in the repository.
     * </p>
     */
    @Test
    void testRemoveNonExistingBookByIsbn() {
        // Arrange
        when(libraryRepository.existsById(NON_EXISTING_BOOK_ISBN)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(BookServiceException.class, () -> {
            bookService.remove(NON_EXISTING_BOOK_ISBN);
        }, "Removing a non-existing book should raise LibraryServiceException.");

        // Ensure that libraryRepository.deleteById is not called
        verify(libraryRepository, never()).deleteById(anyString());
    }
}
