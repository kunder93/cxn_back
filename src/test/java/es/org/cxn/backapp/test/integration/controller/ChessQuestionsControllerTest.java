
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.ChangeChessQuestionHasSeenRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequestForm;
import es.org.cxn.backapp.model.form.responses.ChessQuestionResponse;
import es.org.cxn.backapp.model.form.responses.ChessQuestionsListResponse;
import es.org.cxn.backapp.test.utils.LocalDateTimeAdapter;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class ChessQuestionsControllerTest {
  @Autowired
  private MockMvc mockMvc;

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
    var requestForm = new CreateChessQuestionRequestForm();
    requestForm.setEmail(questionEmail);
    requestForm.setCategory(questionCategory);
    requestForm.setTopic(questionTopic);
    requestForm.setMessage(questionMessage);

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
          questionEmail, deserializedResponse.getEmail(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertEquals(
          questionCategory, deserializedResponse.getCategory(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertEquals(
          questionTopic, deserializedResponse.getTopic(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertEquals(
          questionMessage, deserializedResponse.getMessage(),
          "The response deserialized object is the same as created."
    );
    Assertions.assertNotNull(
          deserializedResponse.getDate(), "Date is added and not null"
    );
  }

  @Test
  @Transactional
  void testCreateSeveralChessQuestionsRetrieveAll() throws Exception {
    var numberOfChessQuestions = 2;

    var requestFormBuilder = CreateChessQuestionRequestForm.builder()
          .category("Category1").email("email@email.es")
          .message("custom message").topic("topic");
    var firstRequestForm = requestFormBuilder.build();
    var firstRequestJson = gson.toJson(firstRequestForm);

    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstRequestJson)
    );
    requestFormBuilder.email("email2@email.com").message("otherMessage");
    var secondRequestForm = requestFormBuilder.build();
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
          responseObject.getChessQuestionList().size(), numberOfChessQuestions,
          "The amount of Chess questions is 2"
    );

  }

  @Test
  @Transactional
  void testDeleteChessQuestionsCheckDeleted() throws Exception {
    // Create 2 Chess questions.
    //First chess question.
    var firstQuestionEmail = "firstTest@example.com";
    var firstQuestionCategory = "firstTest Category";
    var firstQuestionTopic = "firstTest Topic";
    var firstQuestionMessage = "firstTest Message";

    var firstChessQuestionRequestBuilder =
          CreateChessQuestionRequestForm.builder();

    var firstRequest = firstChessQuestionRequestBuilder
          .email(firstQuestionEmail).category(firstQuestionCategory)
          .topic(firstQuestionTopic).message(firstQuestionMessage).build();

    var firstRequestJson = gson.toJson(firstRequest);

    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated());

    var secondQuestionEmail = "firstTest@example.com";
    var secondQuestionCategory = "firstTest Category";
    var secondQuestionTopic = "firstTest Topic";
    var secondQuestionMessage = "firstTest Message";

    var secondRequest = firstChessQuestionRequestBuilder
          .email(secondQuestionEmail).category(secondQuestionCategory)
          .topic(secondQuestionTopic).message(secondQuestionMessage).build();

    var secondRequestJson = gson.toJson(secondRequest);

    mockMvc.perform(
          MockMvcRequestBuilders.post("/api/chessQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondRequestJson).accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated());

    //Get all chess questions.
    var responseAsString = mockMvc
          .perform(
                MockMvcRequestBuilders.get("/api/chessQuestion")
                      .accept(MediaType.APPLICATION_JSON)
          ).andExpect(status().isOk()).andReturn().getResponse()
          .getContentAsString();

    var response =
          gson.fromJson(responseAsString, ChessQuestionsListResponse.class);

    Assertions.assertEquals(
          2, response.getChessQuestionList().size(),
          "The questions list has 2 questions."
    );

    // Perform delete of first question
    mockMvc.perform(
          MockMvcRequestBuilders.delete(
                "/api/chessQuestion/"
                      + response.getChessQuestionList().get(0).getId()
          )
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
          1, response.getChessQuestionList().size(),
          "The questions list has 1 question after deletion."
    );
  }

  @Test
  @Transactional
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
  void testChangeQuestionHasSeen() throws Exception {
    var questionEmail = "test@example.com";
    var questionCategory = "Test Category";
    var questionTopic = "Test Topic";
    var questionMessage = "Test Message";

    // Prepare request data
    var requestForm = new CreateChessQuestionRequestForm();
    requestForm.setEmail(questionEmail);
    requestForm.setCategory(questionCategory);
    requestForm.setTopic(questionTopic);
    requestForm.setMessage(questionMessage);

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
          Boolean.FALSE, response.isSeen(),
          "QUestion has seen is false when is created."
    );

    var changeChessQuestionHasSeenRequestForm =
          new ChangeChessQuestionHasSeenRequestForm();
    changeChessQuestionHasSeenRequestForm.setId(response.getId());

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
          Boolean.TRUE, response.isSeen(), "Has seen has been changed."
    );

  }

}
