
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequestForm;
import es.org.cxn.backapp.model.form.responses.ChessQuestionResponse;
import es.org.cxn.backapp.model.form.responses.ChessQuestionsListResponse;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import utils.LocalDateTimeAdapter;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class ChessQuestionsControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void testCreateChessQuestionReturnDataMatch() throws Exception {
    var gson = new GsonBuilder()
          .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
          .create();
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
  void testCreateSeveralChessQuestionsRetrieveAll() throws Exception {
    var numberOfChessQuestions = 2;
    var gson = new GsonBuilder()
          .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
          .create();
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

}
