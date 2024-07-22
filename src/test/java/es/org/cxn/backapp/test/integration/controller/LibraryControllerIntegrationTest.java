//
//package es.org.cxn.backapp.test.integration.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.GsonBuilder;
//
//import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
//import es.org.cxn.backapp.model.form.requests.AuthorRequestDto;
//import es.org.cxn.backapp.model.form.responses.AuthorListResponse;
//import es.org.cxn.backapp.model.form.responses.AuthorResponse;
//import es.org.cxn.backapp.model.form.responses.BookListResponse;
//import es.org.cxn.backapp.model.form.responses.BookResponse;
//import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
//
//import jakarta.transaction.Transactional;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import utils.LocalDateAdapter;
//
///**
// * @author Santiago Paz. User controller integration tests.
// */
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestPropertySource("/application.properties")
//class LibraryControllerIntegrationTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  final String CONTROLLER_URL = "/api/library";
//
//  @Transactional
//  @Test
//  void testGetAllBooks() throws Exception {
//    mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_URL))
//          .andExpect(MockMvcResultMatchers.status().isOk())
//          // Add more assertions for the response content if needed
//          .andReturn();
//  }
//
//  /**
//   * Create book with minimal data, isbn and title, check that returned value is same.
//   *
//   * @throws Exception when fails.
//   */
//  @Transactional
//  @Test
//  void testAddMinimalBookExpectCreated() throws Exception {
//    final var isbn = 1234567890L;
//    final var title = "Sample Book";
//    var gson = new GsonBuilder()
//          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//          .create();
//    // Create a sample book request DTO
//    var bookRequestDto = new AddBookRequestDto();
//    bookRequestDto.setTitle(title);
//    bookRequestDto.setIsbn(isbn);
//
//    var result =
//          mockMvc
//                .perform(
//                      MockMvcRequestBuilders.post(CONTROLLER_URL)
//                            .contentType(MediaType.APPLICATION_JSON).content(
//                                  objectMapper
//                                        .writeValueAsString(bookRequestDto)
//                            )
//                ).andExpect(MockMvcResultMatchers.status().isCreated())
//                // Add more assertions for the response content if needed
//                .andReturn().getResponse().getContentAsString();
//
//    var responseObject = gson.fromJson(result, BookResponse.class);
//
//    Assertions.assertEquals(
//          isbn, responseObject.getIsbn(), "Isbn returned is initial isbn."
//    );
//    Assertions.assertEquals(
//          title, responseObject.getTitle(), " Title is the initial title."
//    );
//  }
//
//  /**
//   * Create book with author and retrieve book with author.
//   * @throws Exception when fails.
//   */
//  @Transactional
//  @Test
//  void testAddBookWithAuthorGetBookWithAuthor() throws Exception {
//    final var isbn = 1234567890L;
//    final var title = "Sample Book";
//    final var gender = "male";
//    final var language = "Spanish";
//    final var publishYear = LocalDate.of(1991, 5, 15);
//    var gson = new GsonBuilder()
//          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//          .create();
//    // Create a sample book request DTO
//    var bookRequestDto = new AddBookRequestDto();
//    bookRequestDto.setTitle(title);
//    bookRequestDto.setIsbn(isbn);
//    bookRequestDto.setLanguage(language);
//    bookRequestDto.setGender(gender);
//    bookRequestDto.setPublishYear(publishYear);
//    // Create book's author
//    final var authorFirstName = "Alfonso";
//    final var authorLastName = "Lopez";
//    final var authorNationality = "France";
//
//    var listAuthorRequest = new ArrayList<AuthorRequestDto>();
//    final var authorRequestA = new AuthorRequestDto();
//    authorRequestA.setFirstName(authorFirstName);
//    authorRequestA.setLastName(authorLastName);
//    authorRequestA.setNationality(authorNationality);
//    listAuthorRequest.add(authorRequestA);
//    bookRequestDto.setAuthorsList(listAuthorRequest);
//
//    mockMvc.perform(
//          MockMvcRequestBuilders.post(CONTROLLER_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(bookRequestDto))
//    ).andExpect(MockMvcResultMatchers.status().isCreated());
//    // Add more assertions for the response content if needed
//
//    final var obtainedBooks =
//          mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_URL))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                // Add more assertions for the response content if needed
//                .andReturn().getResponse().getContentAsString();
//
//    var responseObject = gson.fromJson(obtainedBooks, BookListResponse.class);
//    var jaja = 8;
//  }
//
//  /**
//   * Create book with minimal data, isbn and title, check that returned value is same.
//   *
//   * @throws Exception when fails.
//   */
//  @Transactional
//  @Test
//  void testAddBookWithAllDataNoAuthorExpectCreated() throws Exception {
//    final var isbn = 1234567890L;
//    final var title = "Sample Book";
//    final var gender = "male";
//    final var language = "Spanish";
//    var gson = new GsonBuilder()
//          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//          .create();
//    // Create a sample book request DTO
//    var bookRequestDto = new AddBookRequestDto();
//    bookRequestDto.setTitle(title);
//    bookRequestDto.setIsbn(isbn);
//    bookRequestDto.setLanguage(language);
//    bookRequestDto.setGender(gender);
//    final var bookRequestDtoJson = gson.toJson(bookRequestDto);
//    var result = mockMvc
//          .perform(
//                MockMvcRequestBuilders.post(CONTROLLER_URL)
//                      .contentType(MediaType.APPLICATION_JSON)
//                      .content(bookRequestDtoJson)
//          ).andExpect(MockMvcResultMatchers.status().isCreated())
//          // Add more assertions for the response content if needed
//          .andReturn().getResponse().getContentAsString();
//
//    var responseObject = gson.fromJson(result, BookResponse.class);
//
//    Assertions.assertEquals(
//          isbn, responseObject.getIsbn(), "Isbn returned is initial isbn."
//    );
//    Assertions.assertEquals(
//          title, responseObject.getTitle(), " Title is the initial title."
//    );
//
//    Assertions.assertEquals(
//          gender, responseObject.getGender(), " gender is the initial gender."
//    );
//    Assertions.assertEquals(
//          language, responseObject.getLanguage(),
//          " Language is the initial language."
//    );
//  }
//
//  /**
//   * Create book with minimal data, isbn and title, check that returned value is same.
//   *
//   * @throws Exception when fails.
//   */
//  @Transactional
//  @Test
//  void testAddBookWithAllDataAndAuthorsExpectCreated() throws Exception {
//    final var isbn = 1234567890L;
//    final var bookTitle = "Sample Book";
//    final var bookGender = "chessProblems";
//    final var language = "Spanish";
//    final var bookPublishYear = LocalDate.now();
//
//    final var authorFirstName = "Alfonso";
//    final var authorLastName = "Lopez";
//    final var authorNationality = "France";
//
//    var gson = new GsonBuilder()
//          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//          .create();
//    // Create a sample book request DTO.
//    var bookRequestDto = new AddBookRequestDto();
//    bookRequestDto.setTitle(bookTitle);
//    bookRequestDto.setIsbn(isbn);
//    bookRequestDto.setLanguage(language);
//    bookRequestDto.setGender(bookGender);
//    bookRequestDto.setPublishYear(bookPublishYear);
//    var listAuthorReuqest = new ArrayList<AuthorRequestDto>();
//    final var authorRequestA = new AuthorRequestDto();
//    authorRequestA.setFirstName(authorFirstName);
//    authorRequestA.setLastName(authorLastName);
//    authorRequestA.setNationality(authorNationality);
//    listAuthorReuqest.add(authorRequestA);
//
//    final var authorRequestB = new AuthorRequestDto();
//    authorRequestB.setFirstName("otherFirstName");
//    authorRequestB.setLastName("otherLastName");
//    authorRequestB.setNationality("germany");
//    listAuthorReuqest.add(authorRequestB);
//
//    bookRequestDto.setAuthorsList(listAuthorReuqest);
//    final var bookRequestDtoJson = gson.toJson(bookRequestDto);
//    var result = mockMvc
//          .perform(
//                MockMvcRequestBuilders.post(CONTROLLER_URL)
//                      .contentType(MediaType.APPLICATION_JSON)
//                      .content(bookRequestDtoJson)
//          ).andExpect(MockMvcResultMatchers.status().isCreated())
//          // Add more assertions for the response content if needed
//          .andReturn().getResponse().getContentAsString();
//
//    var responseObject = gson.fromJson(result, BookResponse.class);
//
//    Assertions.assertEquals(
//          isbn, responseObject.getIsbn(), "Isbn returned is initial isbn."
//    );
//    Assertions.assertEquals(
//          bookTitle, responseObject.getTitle(),
//          " Book's title is the initial title."
//    );
//
//    Assertions.assertEquals(
//          bookGender, responseObject.getGender(),
//          "Book's gender is the initial gender."
//    );
//    Assertions.assertEquals(
//          language, responseObject.getLanguage(),
//          " Language is the initial language."
//    );
//    final var authorsSetResponse = responseObject.getAuthors();
//
//    // Assert that the size of the returned authors set is as expected
//    Assertions.assertEquals(
//          2, authorsSetResponse.size(), "Expected 2 authors in the set."
//    );
//
//    // Iterate over the authors in the set to find specific authors and make assertions
//    for (AuthorResponse author : authorsSetResponse) {
//      if (author.getFirstName().equals(authorFirstName)
//            && author.getLastName().equals(authorLastName)) {
//        // Found the first author, make assertions
//        Assertions.assertEquals(
//              authorFirstName, author.getFirstName(),
//              "First author's first name matches."
//        );
//        Assertions.assertEquals(
//              authorLastName, author.getLastName(),
//              "First author's last name matches."
//        );
//        Assertions.assertEquals(
//              authorNationality, author.getNationality(),
//              "First author's nationality matches."
//        );
//      } else if (author.getFirstName().equals("otherFirstName")
//            && author.getLastName().equals("otherLastName")) {
//        // Found the second author, make assertions
//        Assertions.assertEquals(
//              "otherFirstName", author.getFirstName(),
//              "Second author's first name matches."
//        );
//        Assertions.assertEquals(
//              "otherLastName", author.getLastName(),
//              "Second author's last name matches."
//        );
//        Assertions.assertEquals(
//              "germany", author.getNationality(),
//              "Second author's nationality matches."
//        );
//      }
//    }
//  }
//
//  @Transactional
//  @Test
//  void testCreateBooksWithSameAuthorsNoCreateDuplicate() throws Exception {
//    // Create authors for first book
//    List<PersistentAuthorEntity> authEntitiesList = new ArrayList<>();
//    var authorsBuilder = PersistentAuthorEntity.builder();
//    var author1 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList.add(author1);
//    var author2 = authorsBuilder.firstName("Josue").lastName("Smith")
//          .nationality("France").build();
//    authEntitiesList.add(author2);
//    var author3 = authorsBuilder.firstName("Jean").lastName("Klengu")
//          .nationality("Italy").build();
//    authEntitiesList.add(author3);
//
//    // Create first book with this authors.
//    var bookAuthorsList = new ArrayList<AuthorRequestDto>();
//    var authReqDto1 = new AuthorRequestDto(author1);
//    var authReqDto2 = new AuthorRequestDto(author2);
//    var authReqDto3 = new AuthorRequestDto(author3);
//    bookAuthorsList.add(authReqDto1);
//    bookAuthorsList.add(authReqDto2);
//    bookAuthorsList.add(authReqDto3);
//    var bookRequest = AddBookRequestDto.builder().isbn(1423414232L)
//          .title("Karpov Best Games").gender("Game analysis")
//          .publishYear(LocalDate.of(1993, 3, 15)).language("English")
//          .authorsList(bookAuthorsList).build();
//
//    var gson = new GsonBuilder()
//          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//          .create();
//
//    var bookRequestJson = gson.toJson(bookRequest);
//    // Book with 3 authors created.
//    mockMvc.perform(
//          MockMvcRequestBuilders.post(CONTROLLER_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookRequestJson)
//    ).andExpect(MockMvcResultMatchers.status().isCreated());
//
//    // Create authors for second book
//    List<PersistentAuthorEntity> authEntitiesList2 = new ArrayList<>();
//    var author1b2 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList2.add(author1b2);
//    var author2b2 = authorsBuilder.firstName("Lucia").lastName("Perez")
//          .nationality("Spain").build();
//    authEntitiesList2.add(author2b2);
//    var author3b2 = authorsBuilder.firstName("Joan").lastName("Pradelios")
//          .nationality("Andorra").build();
//    authEntitiesList2.add(author3b2);
//
//    // Create book with this authors.
//    var bookAuthorsList2 = new ArrayList<AuthorRequestDto>();
//    var authReqDto1b2 = new AuthorRequestDto(author1b2);
//    var authReqDto2b2 = new AuthorRequestDto(author2b2);
//    var authReqDto3b2 = new AuthorRequestDto(author3b2);
//    bookAuthorsList2.add(authReqDto1b2);
//    bookAuthorsList2.add(authReqDto2b2);
//    bookAuthorsList2.add(authReqDto3b2);
//    var bookRequest2 = AddBookRequestDto.builder().isbn(1423414232L)
//          .title("Best moves to win").gender("Game analysis")
//          .publishYear(LocalDate.of(1996, 1, 20)).language("English")
//          .authorsList(bookAuthorsList2).build();
//
//    var bookRequestJson2 = gson.toJson(bookRequest2);
//    // Book with 3 authors created.
//    mockMvc.perform(
//          MockMvcRequestBuilders.post(CONTROLLER_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookRequestJson2)
//    ).andExpect(MockMvcResultMatchers.status().isCreated());
//
//    //Retrieve only authors list.S
//    var getResult = mockMvc
//          .perform(MockMvcRequestBuilders.get(CONTROLLER_URL + "/authors"))
//          .andExpect(MockMvcResultMatchers.status().isOk())
//          // Add more assertions for the response content if needed
//          .andReturn().getResponse().getContentAsString();
//    var responseObject = gson.fromJson(getResult, AuthorListResponse.class);
//    final var numberOfAuthors = responseObject.getAuthorList().size();
//    Assertions.assertEquals(
//          5, numberOfAuthors,
//          "count of authors is 5 cause no repeated authors found."
//    );
//  }
//
//  @Transactional
//  @Test
//  void testRemoveBook() throws Exception {
//    Long isbn = 1234567890L; // Replace with an existing ISBN
//    // Create a sample book request DTO
//    var bookRequestDto = new AddBookRequestDto();
//    bookRequestDto.setTitle("Sample Book");
//    bookRequestDto.setIsbn(isbn);
//    // Create book.
//    mockMvc.perform(
//          MockMvcRequestBuilders.post(CONTROLLER_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(bookRequestDto))
//    ).andExpect(MockMvcResultMatchers.status().isCreated());
//    //Remove book, expect 200ok
//    mockMvc
//          .perform(
//                MockMvcRequestBuilders.delete(CONTROLLER_URL + "/{isbn}", isbn)
//          ).andExpect(MockMvcResultMatchers.status().isOk())
//          // Add more assertions for the response content if needed
//          .andReturn();
//  }
//
//  /**
//   * When book is added with some authors that not exists this authors are added.
//   * Then we can retrieve all authors.
//   *
//   * @throws Exception The exception.
//   */
//  @Transactional
//  @Test
//  void testRetrieveAllAuthors() throws Exception {
//    // Create authors
//    List<PersistentAuthorEntity> authEntitiesList = new ArrayList<>();
//    var authorsBuilder = PersistentAuthorEntity.builder();
//    var author1 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList.add(author1);
//    var author2 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList.add(author2);
//    var author3 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList.add(author3);
//    // Create list expected.
//    var authListResponse = new AuthorListResponse(authEntitiesList);
//
//    // Create book with this authors.
//    var bookAuthorsList = new ArrayList<AuthorRequestDto>();
//    var authReqDto1 = new AuthorRequestDto(author1);
//    var authReqDto2 = new AuthorRequestDto(author2);
//    var authReqDto3 = new AuthorRequestDto(author3);
//    bookAuthorsList.add(authReqDto1);
//    bookAuthorsList.add(authReqDto2);
//    bookAuthorsList.add(authReqDto3);
//    var bookRequest = AddBookRequestDto.builder().isbn(1423414232L)
//          .title("Karpov Best Games").gender("Game analysis")
//          .publishYear(LocalDate.of(1993, 3, 15)).language("English")
//          .authorsList(bookAuthorsList).build();
//
//    var gson = new GsonBuilder()
//          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//          .create();
//
//    var bookRequestJson = gson.toJson(bookRequest);
//    // Book with 3 authors created.
//    mockMvc.perform(
//          MockMvcRequestBuilders.post(CONTROLLER_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookRequestJson)
//    ).andExpect(MockMvcResultMatchers.status().isCreated());
//
//    //Retrieve only authors list.S
//    var getResult = mockMvc
//          .perform(MockMvcRequestBuilders.get(CONTROLLER_URL + "/authors"))
//          .andExpect(MockMvcResultMatchers.status().isOk())
//          // Add more assertions for the response content if needed
//          .andReturn().getResponse().getContentAsString();
//    var responseObject = gson.fromJson(getResult, AuthorListResponse.class);
//
//    Assertions.assertEquals(
//          authListResponse, responseObject,
//          "Authors list is the same as expected"
//    );
//  }
//
//  /**
//   * When remove books authors are not removed.
//   *
//   * @throws Exception The exception.
//   */
//  @Transactional
//  @Test
//  void testRemoveBookWithAuthorsNoRemoveAuthors() throws Exception {
//    // Create authors
//    List<PersistentAuthorEntity> authEntitiesList = new ArrayList<>();
//    var authorsBuilder = PersistentAuthorEntity.builder();
//    var author1 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList.add(author1);
//    var author2 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList.add(author2);
//    var author3 = authorsBuilder.firstName("Manuel").lastName("Paz")
//          .nationality("Spain").build();
//    authEntitiesList.add(author3);
//    // Create list expected.
//    var authListResponse = new AuthorListResponse(authEntitiesList);
//
//    // Create book with this authors.
//    var bookAuthorsList = new ArrayList<AuthorRequestDto>();
//    var authReqDto1 = new AuthorRequestDto(author1);
//    var authReqDto2 = new AuthorRequestDto(author2);
//    var authReqDto3 = new AuthorRequestDto(author3);
//    bookAuthorsList.add(authReqDto1);
//    bookAuthorsList.add(authReqDto2);
//    bookAuthorsList.add(authReqDto3);
//    final var bookIsbn = 1423414232L;
//    var bookRequest =
//          AddBookRequestDto.builder().isbn(bookIsbn).title("Karpov Best Games")
//                .gender("Game analysis").publishYear(LocalDate.of(1993, 3, 15))
//                .language("English").authorsList(bookAuthorsList).build();
//
//    var gson = new GsonBuilder()
//          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//          .create();
//
//    var bookRequestJson = gson.toJson(bookRequest);
//    // Book with 3 authors created.
//    mockMvc.perform(
//          MockMvcRequestBuilders.post(CONTROLLER_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookRequestJson)
//    ).andExpect(MockMvcResultMatchers.status().isCreated());
//
//    mockMvc.perform(
//          MockMvcRequestBuilders.delete(CONTROLLER_URL + "/{isbn}", bookIsbn)
//    ).andExpect(MockMvcResultMatchers.status().isOk());
//
//    var getResult = mockMvc
//          .perform(MockMvcRequestBuilders.get(CONTROLLER_URL + "/authors"))
//          .andExpect(MockMvcResultMatchers.status().isOk())
//          // Add more assertions for the response content if needed
//          .andReturn().getResponse().getContentAsString();
//    var responseObject = gson.fromJson(getResult, AuthorListResponse.class);
//
//    Assertions.assertEquals(
//          authListResponse, responseObject,
//          "Authors list is the same as expected"
//    );
//
//  }
//
//}
