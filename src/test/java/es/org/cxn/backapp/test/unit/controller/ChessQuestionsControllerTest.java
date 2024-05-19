
package es.org.cxn.backapp.test.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.controller.entity.ChessQuestionsController;
import es.org.cxn.backapp.exceptions.ChessQuestionServiceException;
import es.org.cxn.backapp.model.ChessQuestionEntity;
import es.org.cxn.backapp.model.form.requests.ChangeChessQuestionHasSeenRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequestForm;
import es.org.cxn.backapp.model.form.responses.ChessQuestionsListResponse;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.service.ChessQuestionsService;
import es.org.cxn.backapp.service.DefaultJwtUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import utils.LocalDateTimeAdapter;

@WebMvcTest(ChessQuestionsController.class)
@Import(TestSecurityConfiguration.class)
class ChessQuestionsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ChessQuestionsService chessQuestionsService;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private DefaultJwtUtils jwtUtils;

  @BeforeEach
  void setUp() {
    // Configura los comportamientos simulados de los mocks aquí si es necesario
  }

  @Test
  void testCreateChessQuestionSuccess() throws Exception {
    // Arrange
    var requestForm = new CreateChessQuestionRequestForm();
    requestForm.setEmail("test@example.com");
    requestForm.setCategory("category");
    requestForm.setTopic("topic");
    requestForm.setMessage("message");

    ChessQuestionEntity result = new PersistentChessQuestionEntity();
    result.setIdentifier(1);
    result.setEmail("test@example.com");
    result.setCategory("category");
    result.setMessage("message");
    result.setTopic("topic");
    result.setDate(LocalDateTime.now());
    result.setSeen(false);

    when(
          chessQuestionsService.add(
                any(String.class), any(String.class), any(String.class),
                any(String.class)
          )
    ).thenReturn(result);

    // Act & Assert
    mockMvc
          .perform(
                post("/api/chessQuestion")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(requestForm))
          ).andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(result.getIdentifier()))
          .andExpect(jsonPath("$.email").value(result.getEmail()))
          .andExpect(jsonPath("$.category").value(result.getCategory()))
          .andExpect(jsonPath("$.message").value(result.getMessage()))
          .andExpect(jsonPath("$.topic").value(result.getTopic()))
          .andExpect(jsonPath("$.seen").value(result.isSeen()));
  }

  @Test
  void testCreateChessQuestionServiceException() throws Exception {
    // Arrange
    var requestForm = new CreateChessQuestionRequestForm();
    requestForm.setEmail("test@example.com");
    requestForm.setCategory("category");
    requestForm.setTopic("topic");
    requestForm.setMessage("message");

    when(
          chessQuestionsService.add(
                any(String.class), any(String.class), any(String.class),
                any(String.class)
          )
    ).thenThrow(
          new IllegalArgumentException(
                "Illegal argument exception can be thrown by repository.save"
          )
    );

    // Act & Assert
    mockMvc.perform(
          post("/api/chessQuestion").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))
    ).andExpect(status().isBadRequest());
  }

  @Test
  void testGetAllChessQuestionsReturnQuestionsAnd200OK() throws Exception {
    var gson = new GsonBuilder()
          .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
          .create();
    // Mockear el comportamiento del servicio para devolver una lista de preguntas de ajedrez
    when(chessQuestionsService.getAll()).thenReturn(
          Arrays.asList(
                new PersistentChessQuestionEntity(
                      1, "Topic1", "Message1", "Category1", "email1",
                      LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), false
                ),
                new PersistentChessQuestionEntity(
                      2, "Topic2", "Message2", "Category2", "email2",
                      LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), false
                )
          )
    );

    // Realizar solicitud GET y verificar la respuesta
    var responseJson = mockMvc
          .perform(
                MockMvcRequestBuilders.get("/api/chessQuestion")
                      .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var response =
          gson.fromJson(responseJson, ChessQuestionsListResponse.class);

    var chessQuestionsList = response.getChessQuestionList();
    Assertions
          .assertEquals(2, chessQuestionsList.size(), "list have 2 questions.");
    var firstElement = chessQuestionsList.get(0);
    var secondElement = chessQuestionsList.get(1);

    Assertions.assertEquals("Topic1", firstElement.getTopic(), "first element");
    Assertions.assertEquals(
          "Category1", firstElement.getCategory(), "first element"
    );
    Assertions.assertEquals("email1", firstElement.getEmail(), "first element");
    Assertions
          .assertEquals("Message1", firstElement.getMessage(), "first element");

    Assertions
          .assertEquals("Topic2", secondElement.getTopic(), "second element");
    Assertions.assertEquals(
          "Category2", secondElement.getCategory(), "second element"
    );
    Assertions
          .assertEquals("email2", secondElement.getEmail(), "second element");
    Assertions.assertEquals(
          "Message2", secondElement.getMessage(), "second element"
    );
  }

  @Test
  void testDeleteChessQuestionSuccess() throws Exception {
    // Arrange
    doNothing().when(chessQuestionsService).delete(anyInt());
    // Act & Assert
    mockMvc.perform(delete("/api/chessQuestion/{id}", 1))
          .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteChessQuestionThrowsException() throws Exception {
    // Arrange
    doThrow(new ChessQuestionServiceException("Question not found"))
          .when(chessQuestionsService).delete(anyInt());
    // Act & Assert
    mockMvc.perform(delete("/api/chessQuestion/{id}", 1))
          .andExpect(status().isBadRequest());
  }

  @Test
  void testChangeChessQuestionHasSeenSuccess() throws Exception {
    // Arrange
    var requestForm = new ChangeChessQuestionHasSeenRequestForm();
    requestForm.setId(1); // Set the necessary fields

    ChessQuestionEntity result = new PersistentChessQuestionEntity();
    result.setIdentifier(1);
    result.setEmail("test@example.com");
    result.setCategory("category");
    result.setMessage("message");
    result.setTopic("topic");
    result.setDate(LocalDateTime.now());
    result.setSeen(true);

    when(chessQuestionsService.changeChessQuestionSeen(any(Integer.class)))
          .thenReturn(result);

    // Act & Assert
    mockMvc
          .perform(
                post("/api/chessQuestion/changeChessQuestionHasSeen")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(requestForm))
          ).andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value("1"))
          .andExpect(jsonPath("$.email").value(result.getEmail()))
          .andExpect(jsonPath("$.category").value(result.getCategory()))
          .andExpect(jsonPath("$.message").value(result.getMessage()))
          .andExpect(jsonPath("$.topic").value(result.getTopic()))
          .andExpect(jsonPath("$.seen").value(result.isSeen()));
  }

  @Test
  void testChangeChessQuestionHasSeenQuestionNotFound() throws Exception {
    // Arrange
    var requestForm = new ChangeChessQuestionHasSeenRequestForm();
    requestForm.setId(1); // Set the necessary fields

    doThrow(new ChessQuestionServiceException("Question not found"))
          .when(chessQuestionsService)
          .changeChessQuestionSeen(any(Integer.class));

    // Act & Assert
    mockMvc.perform(
          post("/changeChessQuestionHasSeen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))
    ).andExpect(status().isNotFound());
  }

  @Test
  void testChangeChessQuestionHasSeenServiceException() throws Exception {
    // Arrange
    var requestForm = new ChangeChessQuestionHasSeenRequestForm();
    requestForm.setId(1); // Set the necessary fields

    doThrow(new ChessQuestionServiceException("Service error"))
          .when(chessQuestionsService)
          .changeChessQuestionSeen(any(Integer.class));

    // Act & Assert
    mockMvc.perform(
          post("/api/chessQuestion/changeChessQuestionHasSeen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))
    ).andExpect(status().isBadRequest());
  }

}
