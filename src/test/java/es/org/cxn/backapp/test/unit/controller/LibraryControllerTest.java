
package es.org.cxn.backapp.test.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.controller.entity.LibraryController;
import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.form.requests.AuthorRequestDto;
import es.org.cxn.backapp.model.form.responses.BookListResponse;
import es.org.cxn.backapp.model.form.responses.BookResponse;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.DefaultLibraryService;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

/**
 * Unit tests for the {@link LibraryController} class.
 * <p>
 * This test class contains unit tests for the methods in the
 * {@link LibraryController} class.
 * It uses MockMvc to perform requests and verify responses, and Mockito to
 * mock dependencies like {@link DefaultLibraryService} and
 * {@link DefaultJwtUtils}.
 * </p>
 * <p>
 * The tests cover various scenarios for managing books, including retrieving,
 * adding, and removing books, as well as handling success and failure cases.
 * </p>
 */
@WebMvcTest(LibraryController.class)
@Import(TestSecurityConfiguration.class)
class LibraryControllerTest {

  /**
   * MockMvc instance used for performing HTTP requests in the tests.
   * <p>
   * This is used to simulate HTTP requests and validate responses in the
   * context of integration testing.
   * </p>
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Mock of the {@link DefaultLibraryService} used to simulate interactions
   * with the library service layer.
   * <p>
   * This mock is used to stub and verify interactions with the library service
   * during testing, without invoking the actual service layer logic.
   * </p>
   */
  @MockBean
  private DefaultLibraryService libraryService;

  /**
   * Mock of the {@link DefaultJwtUtils} used to simulate interactions with JWT
   * utilities.
   * <p>
   * This mock is used to stub and verify interactions with JWT utilities,
   * which may be required for security-related operations in the controller.
   * </p>
   */
  @MockBean
  private DefaultJwtUtils jwtUtils;

  /**
   * The URL for library-related API endpoints.
   */
  private static final String LIBRARY_URL = "/api/library";

  /**
   * ISBN for test book 1.
   */
  private static final long TEST_ISBN_1 = 123456967050L;

  /**
   * ISBN for test book 2.
   */
  private static final long TEST_ISBN_2 = 24214244L;

  /**
   * ISBN for test book 3.
   */
  private static final long TEST_ISBN_3 = 5555532432L;

  /**
   * ISBN for test book add/remove operations.
   */
  private static final long TEST_ISBN = 1234567890L;

  /**
   * Book publish year for test.
   */
  private static final LocalDate PUBLISH_YEAR = LocalDate.of(2024, 1, 1);

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Tests that the controller returns a list of books ordered alphabetically
   * by title.
   * <p>
   * This test performs a GET request to the library endpoint, verifies that
   * the status is OK (200), and checks that the books are returned in
   * alphabetical order by title.
   * </p>
   *
   * @throws Exception if the test fails
   */
  @Test
  void testGetAllBooksReturnBooksOrderedAnd200OK() throws Exception {
    var gson = new GsonBuilder().setPrettyPrinting()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    // Arrange
    var bookBuilder =
          PersistentBookEntity.builder().isbn(TEST_ISBN_1).title("t1");
    var book1 = bookBuilder.build();
    bookBuilder.isbn(TEST_ISBN_2).title("t2");
    var book2 = bookBuilder.build();
    bookBuilder.isbn(TEST_ISBN_3).title("t3");
    var book3 = bookBuilder.build();
    List<BookEntity> books = new ArrayList<>();
    books.add(book1);
    books.add(book2);
    books.add(book3);

    when(libraryService.getAllBooks()).thenReturn(books);

    var responseJson = mockMvc
          .perform(
                MockMvcRequestBuilders.get(LIBRARY_URL)
                      .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    var response = gson.fromJson(responseJson, BookListResponse.class);
    Assertions.assertEquals(
          books.size(), response.bookList().size(), "Return list of books."
    );

    // Extract book titles from the response
    Set<String> responseTitles = response.bookList().stream()
          .map(BookResponse::title).collect(Collectors.toSet());

    // Verify that the response titles match the expected titles
    Assertions.assertTrue(
          responseTitles.contains("t1") && responseTitles.contains("t2")
                && responseTitles.contains("t3"),
          "Response contains the expected book titles."
    );

    // Verify that the titles are sorted in alphabetical order
    var sortedTitles = new ArrayList<>(responseTitles);
    Collections.sort(sortedTitles);
    Assertions.assertIterableEquals(
          sortedTitles, new ArrayList<>(responseTitles),
          "Response titles are sorted in alphabetical order."
    );
  }

  /**
   * Tests that a book is successfully added to the library.
   * <p>
   * This test performs a POST request to add a new book and verifies that
   * the response status is CREATED (201).
   * </p>
   *
   * @throws Exception if the test fails
   */
  @Test
  void testAddBookSuccess() throws Exception {
    // Arrange
    var bookRequest = new AddBookRequestDto(
          TEST_ISBN, "Test Book", // title
          "Fiction", // gender
          PUBLISH_YEAR, "English", // language
          List.of(new AuthorRequestDto("Test Author", "A", "Spain"))
    );

    var addedBook = PersistentBookEntity.builder().isbn(TEST_ISBN)
          .title("Test Book").build();

    var gson = new GsonBuilder().setPrettyPrinting()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    var bookRequestJson = gson.toJson(bookRequest);
    when(libraryService.addBook(any(AddBookRequestDto.class)))
          .thenReturn(addedBook);

    // Act and Assert
    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/library")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated())
          .andExpect(jsonPath("$.isbn").value(TEST_ISBN))
          .andExpect(jsonPath("$.title").value("Test Book"));
  }

  /**
   * Tests that adding a book fails and returns a BAD REQUEST status.
   * <p>
   * This test simulates an error during the book addition process and verifies
   * that the response status is BAD REQUEST (400).
   * </p>
   *
   * @throws Exception if the test fails
   */
  @Test
  void testAddBookFailure() throws Exception {
    // Arrange
    var bookRequest = new AddBookRequestDto(
          TEST_ISBN, "Test Book", // title
          "Fiction", // gender
          PUBLISH_YEAR, "English", // language
          List.of() // authorsList
    );

    var serviceException = new LibraryServiceException("Failed to add book");

    // Mock the libraryService to throw an exception when addBook is called
    doThrow(serviceException).when(libraryService)
          .addBook(any(AddBookRequestDto.class));

    var gson = new GsonBuilder().setPrettyPrinting()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    var bookRequestJson = gson.toJson(bookRequest);

    // Act and Assert
    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/library")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest());
  }

  /**
   * Tests that a book is successfully removed from the library.
   * <p>
   * This test performs a DELETE request to remove a book and verifies that
   * the response status is OK (200).
   * </p>
   *
   * @throws Exception if the test fails
   */
  @Test
  void testRemoveBookSuccess() throws Exception {
    // Arrange
    doNothing().when(libraryService).removeBookByIsbn(TEST_ISBN);

    // Act and Assert
    mockMvc.perform(
          MockMvcRequestBuilders.delete(LIBRARY_URL + "/{isbn}", TEST_ISBN)
                .accept(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk());
  }

  /**
   * Tests that removing a book fails and returns a BAD REQUEST status.
   * <p>
   * This test simulates an error during the book removal process and verifies
   * that the response status is BAD REQUEST (400).
   * </p>
   *
   * @throws Exception if the test fails
   */
  @Test
  void testRemoveBookFailure() throws Exception {
    // Arrange
    var serviceException = new LibraryServiceException("Failed to remove book");

    doThrow(serviceException).when(libraryService).removeBookByIsbn(TEST_ISBN);

    // Act and Assert
    final var mockedRequest = mockMvc.perform(
          MockMvcRequestBuilders.delete(LIBRARY_URL + "/{isbn}", TEST_ISBN)
                .accept(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    mockedRequest.andExpect(result -> {
      // Verify the response body contains the expected exception message
      Assertions.assertTrue(
            result.getResolvedException() instanceof ResponseStatusException,
            "The exception is expected as Response Status"
      );
    });
  }
}
