
package es.org.cxn.backapp.test.unit.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.controller.entity.LibraryController;
import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
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

@WebMvcTest(LibraryController.class)
@Import(TestSecurityConfiguration.class)
class LibraryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DefaultLibraryService libraryService;

  @MockBean
  private DefaultJwtUtils jwtUtils;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private final String LIBRARY_URL = "/api/library";

  /**
   * Check controller for providing books ordered by title alphabetical.
   *
   * @throws Exception when fails.
   */
  @Test
  void testGetAllBooksReturnBooksOrderedAnd200OK() throws Exception {
    var gson = new GsonBuilder().setPrettyPrinting()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
    // Arrange
    var bookBuilder =
          PersistentBookEntity.builder().isbn(123456967050L).title("t1");
    var book1 = bookBuilder.build();
    bookBuilder.isbn(24214244L).title("t2");
    var book2 = bookBuilder.build();
    bookBuilder.isbn(5555532432L).title("t3");
    var book3 = bookBuilder.build();
    List<BookEntity> books = new ArrayList<BookEntity>();
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
          books.size(), response.getBookList().size(), "return list of books."
    );

    // Extract book titles from the response
    Set<String> responseTitles = response.getBookList().stream()
          .map(BookResponse::getTitle).collect(Collectors.toSet());

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
          sortedTitles, responseTitles,
          "Response titles are sorted in alphabetical order."
    );
  }

  @Test
  void testAddBookSuccess() throws Exception {
    final var title = "The title";
    final var isbn = 1234567890L;
    // Arrange
    var bookRequest =
          AddBookRequestDto.builder().title(title).isbn(isbn).build();

    // Create a sample book entity that might be returned by libraryService.addBook
    var addedBook =
          PersistentBookEntity.builder().isbn(isbn).title(title).build();

    var gson = new GsonBuilder().setPrettyPrinting()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    var bookRequestJson = gson.toJson(bookRequest);
    // Mock the behavior of libraryService.addBook for a successful addition
    when(libraryService.addBook(bookRequest)).thenReturn(addedBook);

    // Act and Assert
    mockMvc.perform(
          MockMvcRequestBuilders.post(LIBRARY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  void testAddBookFailure() throws Exception {
    final var title = "The title";
    final var isbn = 1234567890L;
    // Arrange
    var bookRequest =
          AddBookRequestDto.builder().title(title).isbn(isbn).build();
    var serviceException = new LibraryServiceException("Failed to add book");

    // Mock the behavior of libraryService.addBook for a failure
    doThrow(serviceException).when(libraryService).addBook(bookRequest);
    var gson = new GsonBuilder().setPrettyPrinting()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
    final var bookRequestJson = gson.toJson(bookRequest);
    // Act and Assert
    mockMvc.perform(
          MockMvcRequestBuilders.post(LIBRARY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testRemoveBookSuccess() throws Exception {
    // Arrange
    var isbn = 1234567890L;

    // Mock the behavior of libraryService.removeBookByIsbn for a successful removal
    doNothing().when(libraryService).removeBookByIsbn(isbn);

    // Act and Assert
    mockMvc.perform(
          MockMvcRequestBuilders.delete(LIBRARY_URL + "/{isbn}", isbn)
                .accept(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testRemoveBookFailure() throws Exception {
    // Arrange
    var isbn = 1234567890L;
    var serviceException = new LibraryServiceException("Failed to remove book");

    // Mock the behavior of libraryService.removeBookByIsbn for a failure
    doThrow(serviceException).when(libraryService).removeBookByIsbn(isbn);

    // Act and Assert
    mockMvc
          .perform(
                MockMvcRequestBuilders.delete(LIBRARY_URL + "/{isbn}", isbn)
                      .accept(MediaType.APPLICATION_JSON)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(result -> {
            // Verify the response body contains the expected exception message
            Assertions.assertTrue(
                  result.getResolvedException() instanceof ResponseStatusException,
                  "The exception is expected as Response Status"
            );
          });
  }
}
