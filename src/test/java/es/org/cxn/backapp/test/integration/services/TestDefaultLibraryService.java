/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.test.integration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.form.requests.AuthorRequest;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.BookEntityRepository;
import es.org.cxn.backapp.service.LibraryService;
import es.org.cxn.backapp.service.impl.DefaultLibraryService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

/**
 * Unit tests for {@link DefaultLibraryService}.
 * <p>
 * This class contains tests to verify the functionality of the
 * {@link DefaultLibraryService} service. It ensures that various methods
 * of the service behave as expected and handle different scenarios correctly.
 * The tests use mocks to simulate interactions with dependencies.
 * </p>
 *
 * @author Santiago Paz
 */
@SpringBootTest(
      classes = { BookEntityRepository.class, LibraryService.class,
          DefaultLibraryService.class }
)
@ActiveProfiles("test")
final class TestDefaultLibraryService {
  /**
   * ISBN number for the first book used in test cases.
   * <p>
   * This ISBN is used to simulate a book with a specific identifier in
   * various test scenarios.
   * </p>
   */
  private static final Long BOOK_ISBN_1 = 235235234L;

  /**
   * ISBN number for the second book used in test cases.
   * <p>
   * This ISBN is used to simulate another book with a different identifier
   * in various test scenarios.
   * </p>
   */
  private static final Long BOOK_ISBN_2 = 42341232134L;

  /**
   * ISBN number for the third book used in test cases.
   * <p>
   * This ISBN is used to simulate a third book with yet another identifier
   * in various test scenarios.
   * </p>
   */
  private static final Long BOOK_ISBN_3 = 41523523352L;

  /**
   * ISBN number used for adding a new book in test cases.
   * <p>
   * This ISBN is used to test the addition of a new book to the repository
   * and to verify that the service handles book creation correctly.
   * </p>
   */
  private static final Long ADD_BOOK_ISBN = 21321321421L;

  /**
   * ISBN number for a non-existing book used in test cases.
   * <p>
   * This ISBN is used to simulate a scenario where a book with the specified
   * identifier does not exist in the repository, allowing tests to verify
   * how the service handles the removal or retrieval of non-existing books.
   * </p>
   */
  private static final Long NON_EXISTING_BOOK_ISBN = 1234567890L;

  /**
   * Title for the book used in test cases.
   * <p>
   * This title is used to simulate a book with a specific title
   * in various test scenarios.
   * </p>
   */
  private static final String BOOK_TITLE = "book title";

  /**
   * Gender of the book used in test cases.
   * <p>
   * This gender is used to simulate a book belonging to a specific genre
   * in various test scenarios.
   * </p>
   */
  private static final String BOOK_GENDER = "Terror";

  /**
   * Publishing year of the book used in test cases.
   * <p>
   * This year is used to simulate the publication date of a book
   * in various test scenarios.
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
   * This first name is used to simulate an author with a specific first name
   * in various test scenarios.
   * </p>
   */
  private static final String AUTHOR_FIRST_NAME = "Alfonso";

  /**
   * Last name of the author used in test cases.
   * <p>
   * This last name is used to simulate an author with a specific last name
   * in various test scenarios.
   * </p>
   */
  private static final String AUTHOR_LAST_NAME = "Rueda";

  /**
   * Nationality of the author used in test cases.
   * <p>
   * This nationality is used to simulate an author with a specific nationality
   * in various test scenarios.
   * </p>
   */
  private static final String AUTHOR_NATIONALITY = "Spain";

  /**
   * The {@link LibraryService} bean used in the tests.
   * <p>
   * This bean is injected into the test class and is used to call service
   * methods to be tested.
   * </p>
   */
  @Autowired
  private LibraryService libraryService;

  /**
   * Mock of the {@link BookEntityRepository}.
   * <p>
   * This mock is used to simulate interactions with the book repository
   * without invoking the actual data access layer.
   * </p>
   */
  @MockBean
  private BookEntityRepository libraryRepository;

  /**
   * Mock of the {@link AuthorEntityRepository}.
   * <p>
   * This mock is used to simulate interactions with the author repository
   * without invoking the actual data access layer.
   * </p>
   */
  @MockBean
  private AuthorEntityRepository authorRepository;

  /**
   * Initializes the mocks before each test.
   * <p>
   * This method is called before each test method to set up the mocks
   * and prepare the test environment.
   * </p>
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Tests the {@link DefaultLibraryService#getAllBooks()} method.
   * <p>
   * This test verifies that the service correctly retrieves all books
   * from the repository and matches the expected results.
   * </p>
   */
  @Test
  void testFindAllBooks() {
    // Arrange
    var bookBuilder = PersistentBookEntity.builder();
    bookBuilder.isbn(BOOK_ISBN_1).title("Book 1");
    var book1 = bookBuilder.build();
    bookBuilder.isbn(BOOK_ISBN_2).title("Book 2");
    var book2 = bookBuilder.build();
    bookBuilder.isbn(BOOK_ISBN_3).title("Book 3");
    var book3 = bookBuilder.build();

    List<PersistentBookEntity> expectedBooks =
          Arrays.asList(book1, book2, book3);

    // Mock the repository to return the expected books when findAll() is called
    when(libraryRepository.findAll()).thenReturn(expectedBooks);

    // Act
    var actualBooks = libraryService.getAllBooks();

    // Assert
    assertEquals(
          expectedBooks.size(), actualBooks.size(),
          "Number of books should match."
    );
    assertEquals(expectedBooks, actualBooks, "Books should be the same.");
  }

  /**
   * Tests the {@link DefaultLibraryService#addBook(AddBookRequestDto)} method.
   * <p>
   * This test verifies that the service correctly saves a book and
   * returns the saved book entity.
   * </p>
   *
   * @throws LibraryServiceException if an error occurs during the book addition
   */
  @Test
  void testAddBookReturnsBookSaved() throws LibraryServiceException {
    // Arrange
    var book1 = new AddBookRequestDto(
          BOOK_ISBN_1, BOOK_TITLE, BOOK_GENDER, BOOK_PUBLISH_YEAR,
          BOOK_LANGUAGE,
          List.of(
                new AuthorRequest(
                      AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME, AUTHOR_NATIONALITY
                )
          ) // authorsList
    );
    var authorEntity = PersistentAuthorEntity.builder()
          .firstName(AUTHOR_FIRST_NAME).lastName(AUTHOR_LAST_NAME)
          .nationality(AUTHOR_NATIONALITY).build();
    Set<PersistentAuthorEntity> authorsSet = new HashSet<>();
    authorsSet.add(

          authorEntity
    );

    var book1Entity = PersistentBookEntity.builder().isbn(BOOK_ISBN_1)
          .title(BOOK_TITLE).gender(BOOK_GENDER).publishYear(BOOK_PUBLISH_YEAR)
          .language(BOOK_LANGUAGE).authors(authorsSet).build();

    // Mock author repository to return the author entity
    when(
          authorRepository.findByFirstNameAndLastNameAndNationality(
                AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME, AUTHOR_NATIONALITY
          )
    ).thenReturn(authorEntity);
    // Mock the repository to return the expected book entity when save()
    // is called
    when(libraryRepository.save(book1Entity)).thenReturn(book1Entity);
    var bookSaved = libraryService.addBook(book1);

    // Verify that the save method was called once and check the saved book data
    verify(libraryRepository, times(1)).save(book1Entity);
    Assertions.assertEquals(
          book1.isbn(), bookSaved.getIsbn(), "Book ISBN should match."
    );
    Assertions.assertEquals(
          book1.title(), bookSaved.getTitle(), "Book title should match."
    );
  }

  /**
   * Tests the {@link DefaultLibraryService#addBook(AddBookRequestDto)} method
   * when a null book is provided.
   * <p>
   * This test verifies that a {@link NullPointerException} is thrown when
   * trying to add a null book.
   * </p>
   */
  @Test
  void testAddNullBookRaisesServiceException() {
    // Arrange
    AddBookRequestDto book1 = null;

    // Act and Assert
    Assertions.assertThrows(
          NullPointerException.class, () -> libraryService.addBook(book1),
          "Null book should raise NullPointerException."
    );
  }

  /**
   * Tests the {@link DefaultLibraryService#findByIsbn(Long)} method.
   * <p>
   * This test verifies that the service correctly finds a book by its ISBN
   * and returns the expected book entity.
   * </p>
   *
   * @throws LibraryServiceException if an error occurs during the book
   * retrieval
   */
  @Test
  void testFindBookByISBN() throws LibraryServiceException {
    var bookOptional = Optional.of(
          PersistentBookEntity.builder().isbn(ADD_BOOK_ISBN).title("Book 1")
                .build()
    );

    // Mock the repository to return the expected book when findById() is called
    when(libraryRepository.findById(ADD_BOOK_ISBN)).thenReturn(bookOptional);

    var bookFound = libraryService.findByIsbn(ADD_BOOK_ISBN);
    verify(libraryRepository, times(1)).findById(ADD_BOOK_ISBN);
    Assertions.assertEquals(
          bookFound, bookOptional.get(),
          "Returned book should match the expected book."
    );
  }

  /**
   * Tests the {@link DefaultLibraryService#findByIsbn(Long)} method
   * with a null ISBN value.
   * <p>
   * This test verifies that a {@link LibraryServiceException} is thrown
   * when a null ISBN is provided.
   * </p>
   */
  @Test
  void testFindByIsbnWithNullValue() {
    // Arrange: Prepare the test scenario
    var isbn = 0L; // Choose a valid ISBN value here

    // Act and Assert: Test the behavior when a null value is passed
    Assertions.assertThrows(LibraryServiceException.class, () -> {
      libraryService.findByIsbn(isbn);
    }, "Null ISBN should raise LibraryServiceException.");
  }

  /**
   * Tests the {@link DefaultLibraryService#removeBookByIsbn(Long)} method.
   * <p>
   * This test verifies that the service correctly removes a book by its ISBN
   * when the book exists in the repository.
   * </p>
   */
  @Test
  void testRemoveBookWithIsbn() {
    // Arrange
    when(libraryRepository.existsById(ADD_BOOK_ISBN)).thenReturn(Boolean.TRUE);
    doNothing().when(libraryRepository).deleteById(ADD_BOOK_ISBN);

    // Act
    try {
      libraryService.removeBookByIsbn(ADD_BOOK_ISBN);
    } catch (LibraryServiceException e) {
      // Handle exceptions as needed in your test
    }

    // Assert
    verify(libraryRepository, times(1)).deleteById(ADD_BOOK_ISBN);
  }

  /**
   * Tests the {@link DefaultLibraryService#removeBookByIsbn(Long)} method
   * with a null ISBN value.
   * <p>
   * This test verifies that a {@link LibraryServiceException} is thrown
   * when a null ISBN is provided and ensures that the delete method is not
   * called.
   * </p>
   */
  @Test
  void testRemoveBookWithNullIsbn() {
    // Arrange
    Long nullIsbn = 0L;

    // Act and Assert
    Assertions.assertThrows(LibraryServiceException.class, () -> {
      libraryService.removeBookByIsbn(nullIsbn);
    }, "Null ISBN should raise LibraryServiceException.");

    // Ensure that libraryRepository.deleteById is not called
    verify(libraryRepository, never()).deleteById(anyLong());
  }

  /**
   * Tests the {@link DefaultLibraryService#removeBookByIsbn(Long)} method
   * when attempting to remove a non-existing book.
   * <p>
   * This test verifies that a {@link LibraryServiceException} is thrown
   * when trying to remove a book that does not exist in the repository.
   * </p>
   */
  @Test
  void testRemoveNonExistingBookByIsbn() {
    // Arrange
    when(libraryRepository.existsById(NON_EXISTING_BOOK_ISBN))
          .thenReturn(false);

    // Act and Assert
    Assertions.assertThrows(LibraryServiceException.class, () -> {
      libraryService.removeBookByIsbn(NON_EXISTING_BOOK_ISBN);
    }, "Removing a non-existing book should raise LibraryServiceException.");

    // Ensure that libraryRepository.deleteById is not called
    verify(libraryRepository, never()).deleteById(anyLong());
  }
}
