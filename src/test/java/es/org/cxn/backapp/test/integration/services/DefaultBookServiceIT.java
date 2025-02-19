
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
import es.org.cxn.backapp.service.impl.storage.DefaultImageStorageService;

@SpringBootTest(classes = { BookEntityRepository.class, BookService.class, DefaultBookService.class,
        ImageStorageService.class, DefaultImageStorageService.class })
@ActiveProfiles("test")
final class DefaultBookServiceIT {

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
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockFile.getOriginalFilename()).thenReturn("CoolName");

    }

    @Test
    void testAddNullBookRaisesServiceException() {

        // Arrange
        AddBookRequestDto book1 = null;

        // Act and Assert
        Assertions.assertThrows(NullPointerException.class, () -> bookService.add(book1, mockFile),
                "Null book should raise NullPointerException.");
    }

    @Test
    void testFindBookByISBN() throws BookServiceException {
        var bookOptional = Optional.of(PersistentBookEntity.builder().isbn(ADD_BOOK_ISBN).title("Book 1").build());

        // Mock the repository to return the expected book when findById() is called
        when(libraryRepository.findById(ADD_BOOK_ISBN)).thenReturn(bookOptional);

        var bookFound = bookService.find(ADD_BOOK_ISBN);
        verify(libraryRepository, times(1)).findById(ADD_BOOK_ISBN);
        Assertions.assertEquals(bookFound, bookOptional.get(), "Returned book should match the expected book.");
    }

    @Test
    void testFindByIsbnWithNullValue() {
        // Arrange: Prepare the test scenario
        var isbn = ""; // Choose a valid ISBN value here

        // Act and Assert: Test the behavior when a null value is passed
        Assertions.assertThrows(BookServiceException.class, () -> {
            bookService.find(isbn);
        }, "Null ISBN should raise BookServiceException.");
    }

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

    @Test
    void testRemoveBookWithNullIsbn() {
        // Arrange
        String nullIsbn = "";

        // Act and Assert
        Assertions.assertThrows(BookServiceException.class, () -> {
            bookService.remove(nullIsbn);
        }, "Null ISBN should raise BookServiceException.");

        // Ensure that libraryRepository.deleteById is not called
        verify(libraryRepository, never()).deleteById(anyString());
    }

    @Test
    void testRemoveNonExistingBookByIsbn() {
        // Arrange
        when(libraryRepository.existsById(NON_EXISTING_BOOK_ISBN)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(BookServiceException.class, () -> {
            bookService.remove(NON_EXISTING_BOOK_ISBN);
        }, "Removing a non-existing book should raise BookServiceException.");

        // Ensure that libraryRepository.deleteById is not called
        verify(libraryRepository, never()).deleteById(anyString());
    }
}
