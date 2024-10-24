
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.ChangeChessQuestionHasSeenRequest;
import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequest;
import es.org.cxn.backapp.model.form.responses.ChessQuestionResponse;
import es.org.cxn.backapp.model.form.responses.ChessQuestionsListResponse;
import es.org.cxn.backapp.test.utils.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
@ActiveProfiles("test")
class ChessQuestionsControllerTest {

  /**
   * Mocked mail sender.
   */
  @MockBean
  private JavaMailSender javaMailSender;

  /**
   * Used to simulate HTTP requests and perform assertions on the results
   * within the test cases.
   *
   * <p>It allows the tests to be run in a way that simulates sending requests
   * to the application's controllers without needing to start a full
   *  HTTP server.</p>
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Gson instance for serializing and deserializing JSON objects during tests.
   *
   * <p>This is particularly useful for converting Java objects to their JSON
   * representation when sending HTTP requests, and for parsing JSON responses
   * received from the controllers back into Java objects.</p>
   *
   */
  private static Gson gson;

  @BeforeAll
  static void initializeTest() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
          .create();

  }

  @Test
  @Transactional
  void testCreateChessQuestionReturnDataMatch() throws Exception {
    var questionEmail = "test@example.com";
    var questionCategory = "Test Category";
    var questionTopic = "Test Topic";
    var questionMessage = "Test Message";

    // Prepare request data
    var requestForm = new CreateChessQuestionRequest(
          questionEmail, questionCategory, questionTopic, questionMessage
    );

    var requestJson = gson.toJson(requestForm);
    // Perform POST request
    var mvcResult = mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)
                .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated()).andReturn();

    // Verify response
    var responseContent = mvcResult.getResponse().getContentAsString();
    // Add assertions for response content

    // deserialize the response body into an object
    var deserializedResponse =
          gson.fromJson(responseContent, ChessQuestionResponse.class);

    Assertions.assertEquals(
          questionEmail, deserializedResponse.email(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertEquals(
          questionCategory, deserializedResponse.category(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertEquals(
          questionTopic, deserializedResponse.topic(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertEquals(
          questionMessage, deserializedResponse.message(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertNotNull(
          deserializedResponse.date(), "Date is added and not null"
    );
  }

  @Test
  @Transactional
  @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
  void testCreateSeveralChessQuestionsRetrieveAll() throws Exception {
    var numberOfChessQuestions = 2;
    final var email = "email@email.es";
    final var category = "Category1";
    final var topic = "topic";
    final var message = "custom message";

    final var secondEmail = "other@other.es";
    final var secondMessage = "other message";
    var firstRequestForm =
          new CreateChessQuestionRequest(email, category, topic, message);

    var firstRequestJson = gson.toJson(firstRequestForm);

    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstRequestJson)
    );

    var secondRequestForm = new CreateChessQuestionRequest(
          secondEmail, category, topic, secondMessage
    );
    var secondRequestJson = gson.toJson(secondRequestForm);
    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondRequestJson)
    );

    // Get all Chess Questions
    var mvcResult =
          mockMvc.perform(MockMvcRequestBuilders.get("/api/chessQuestion"))
                .andExpect(status().isOk()).andReturn();
    var responseContent = mvcResult.getResponse().getContentAsString();
    var responseObject =
          gson.fromJson(responseContent, ChessQuestionsListResponse.class);
    Assertions.assertEquals(
          responseObject.chessQuestionList().size(), numberOfChessQuestions,
          "The amount of Chess questions is 2"
    );

  }

  @Test
  @Transactional
  @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
  void testDeleteChessQuestionsCheckDeleted() throws Exception {
    // Create 2 Chess questions.
    var firstQuestionEmail = "firstTest@example.com";
    var firstQuestionCategory = "firstTest Category";
    var firstQuestionTopic = "firstTest Topic";
    var firstQuestionMessage = "firstTest Message";

    var firstRequest = new CreateChessQuestionRequest(
          firstQuestionEmail, firstQuestionCategory, firstQuestionTopic,
          firstQuestionMessage
    );

    var firstRequestJson = gson.toJson(firstRequest);

    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated());

    var secondQuestionEmail = "secondTest@example.com";
    var secondQuestionCategory = "secondTest Category";
    var secondQuestionTopic = "secondTest Topic";
    var secondQuestionMessage = "secondTest Message";

    var secondRequest = new CreateChessQuestionRequest(
          secondQuestionEmail, secondQuestionCategory, secondQuestionTopic,
          secondQuestionMessage
    );

    var secondRequestJson = gson.toJson(secondRequest);

    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated());

    // Get all chess questions.
    var responseAsString = mockMvc
          .perform(
                MockMvcRequestBuilders.get("/api/chessQuestion")
                      .accept(MediaType.APPLICATION_JSON)
          ).andExpect(status().isOk()).andReturn().getResponse()
          .getContentAsString();

    var response =
          gson.fromJson(responseAsString, ChessQuestionsListResponse.class);

    Assertions.assertEquals(
          2, response.chessQuestionList().size(),
          "The questions list has 2 questions."
    );

    // Convert collection to list for indexing
    List<ChessQuestionResponse> chessQuestionsList =
          new ArrayList<>(response.chessQuestionList());

    // Perform delete of first question
    mockMvc.perform(
          MockMvcRequestBuilders
                .delete("/api/chessQuestion/" + chessQuestionsList.get(0).id())
    ).andExpect(status().isNoContent());

    // Get all chess questions after deletion
    responseAsString = mockMvc
          .perform(
                MockMvcRequestBuilders.get("/api/chessQuestion")
                      .accept(MediaType.APPLICATION_JSON)
          ).andExpect(status().isOk()).andReturn().getResponse()
          .getContentAsString();

    response =
          gson.fromJson(responseAsString, ChessQuestionsListResponse.class);

    Assertions.assertEquals(
          1, response.chessQuestionList().size(),
          "The questions list has 1 question after deletion."
    );
  }

  @Test
  @Transactional
  @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
  void testDeleteNotExistingQuestionReturnBadRequest() throws Exception {
    final var notExistingQuestionId = 88;
    // Perform delete of first question
    mockMvc.perform(
          MockMvcRequestBuilders
                .delete("/api/chessQuestion/" + notExistingQuestionId)
    ).andExpect(status().isBadRequest());

  }

  @Test
  @Transactional
  @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
  void testChangeQuestionHasSeen() throws Exception {
    var questionEmail = "test@example.com";
    var questionCategory = "Test Category";
    var questionTopic = "Test Topic";
    var questionMessage = "Test Message";

    // Prepare request data
    var requestForm = new CreateChessQuestionRequest(
          questionEmail, questionCategory, questionTopic, questionMessage
    );

    var requestJson = gson.toJson(requestForm);
    // Perform POST request
    var responseJson = mockMvc
          .perform(
                MockMvcRequestBuilders.post("/api/chessQuestion")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(requestJson).accept(MediaType.APPLICATION_JSON)
          ).andExpect(status().isCreated()).andReturn().getResponse()
          .getContentAsString();

    var response = gson.fromJson(responseJson, ChessQuestionResponse.class);

    Assertions.assertEquals(
          Boolean.FALSE, response.seen(),
          "QUestion has seen is false when is created."
    );

    var changeChessQuestionHasSeenRequestForm =
          new ChangeChessQuestionHasSeenRequest(response.id());

    var changeChessQuestionHasSeenRequestFormJson =
          gson.toJson(changeChessQuestionHasSeenRequestForm);

    responseJson = mockMvc
          .perform(
                MockMvcRequestBuilders
                      .post("/api/chessQuestion/changeChessQuestionHasSeen")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changeChessQuestionHasSeenRequestFormJson)
                      .accept(MediaType.APPLICATION_JSON)
          ).andExpect(status().isCreated()).andReturn().getResponse()
          .getContentAsString();

    response = gson.fromJson(responseJson, ChessQuestionResponse.class);

    Assertions.assertEquals(
          Boolean.TRUE, response.seen(), "Has seen has been changed."
    );

  }

}
