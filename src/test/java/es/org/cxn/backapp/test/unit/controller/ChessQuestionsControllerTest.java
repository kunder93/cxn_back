
package es.org.cxn.backapp.test.unit.controller;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.controller.entity.ChessQuestionsController;
import es.org.cxn.backapp.model.ChessQuestionEntity;
import es.org.cxn.backapp.model.form.requests.ChangeChessQuestionHasSeenRequest;
import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequest;
import es.org.cxn.backapp.model.form.responses.ChessQuestionResponse;
import es.org.cxn.backapp.model.form.responses.ChessQuestionsListResponse;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import es.org.cxn.backapp.service.ChessQuestionsService;
import es.org.cxn.backapp.service.exceptions.ChessQuestionServiceException;
import es.org.cxn.backapp.test.utils.LocalDateTimeAdapter;

@WebMvcTest(ChessQuestionsController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChessQuestionsControllerTest {

    /**
     * MockMvc instance used for performing HTTP requests in the tests.
     * <p>
     * This is used to simulate HTTP requests and validate responses in the context
     * of integration testing.
     * </p>
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock of the {@link ChessQuestionsService} used to simulate interactions with
     * the chess questions service layer.
     * <p>
     * This mock is used to stub and verify interactions with the chess questions
     * service during testing, without invoking the actual service layer logic.
     * </p>
     */
    @MockitoBean
    private ChessQuestionsService chessQuestionsService;

    /**
     * ObjectMapper instance used for serializing and deserializing JSON in the
     * tests.
     * <p>
     * This is used to convert Java objects to JSON and vice versa, which is
     * necessary for testing JSON-based requests and responses.
     * </p>
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Mock of the {@link DefaultJwtUtils} used to simulate interactions with JWT
     * utilities.
     * <p>
     * This mock is used to stub and verify interactions with JWT utilities, which
     * may be required for security-related operations in the controller.
     * </p>
     */
    @MockitoBean
    private DefaultJwtUtils jwtUtils;

    @Test
    void testChangeChessQuestionHasSeenQuestionNotFound() throws Exception {
        // Arrange
        final var id = 1;
        var requestForm = new ChangeChessQuestionHasSeenRequest(id);

        doThrow(new ChessQuestionServiceException("Question not found")).when(chessQuestionsService)
                .changeChessQuestionSeen(any(Integer.class));

        // Act & Assert
        mockMvc.perform(post("/changeChessQuestionHasSeen").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))).andExpect(status().isNotFound());
    }

    @Test
    void testChangeChessQuestionHasSeenServiceException() throws Exception {
        // Arrange
        final var id = 1;
        var requestForm = new ChangeChessQuestionHasSeenRequest(id);

        doThrow(new ChessQuestionServiceException("Service error")).when(chessQuestionsService)
                .changeChessQuestionSeen(any(Integer.class));

        // Act & Assert
        mockMvc.perform(post("/api/chessQuestion/changeChessQuestionHasSeen").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))).andExpect(status().isBadRequest());
    }

    @Test
    void testChangeChessQuestionHasSeenSuccess() throws Exception {
        // Arrange
        final var id = 1;
        var requestForm = new ChangeChessQuestionHasSeenRequest(id);

        ChessQuestionEntity result = new PersistentChessQuestionEntity();
        result.setIdentifier(1);
        result.setEmail("test@example.com");
        result.setCategory("category");
        result.setMessage("message");
        result.setTopic("topic");
        result.setDate(LocalDateTime.now());
        result.setSeen(true);

        when(chessQuestionsService.changeChessQuestionSeen(any(Integer.class))).thenReturn(result);

        // Act & Assert
        mockMvc.perform(post("/api/chessQuestion/changeChessQuestionHasSeen").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1")).andExpect(jsonPath("$.email").value(result.getEmail()))
                .andExpect(jsonPath("$.category").value(result.getCategory()))
                .andExpect(jsonPath("$.message").value(result.getMessage()))
                .andExpect(jsonPath("$.topic").value(result.getTopic()))
                .andExpect(jsonPath("$.seen").value(result.isSeen()));
    }

    @Test
    void testCreateChessQuestionServiceException() throws Exception {
        // Arrange
        final var email = "test@example.com";
        final var category = "category";
        final var topic = "topic";
        final var message = "message";
        var requestForm = new CreateChessQuestionRequest(email, category, topic, message);

        when(chessQuestionsService.add(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenThrow(new IllegalArgumentException("Illegal argument exception can be thrown by repository.save"));

        // Act & Assert
        mockMvc.perform(post("/api/chessQuestion").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateChessQuestionSuccess() throws Exception {
        // Arrange
        final var email = "test@example.com";
        final var category = "category";
        final var topic = "topic";
        final var message = "message";

        var requestForm = new CreateChessQuestionRequest(email, category, topic, message);

        ChessQuestionEntity result = new PersistentChessQuestionEntity();
        result.setIdentifier(1);
        result.setEmail("test@example.com");
        result.setCategory("category");
        result.setMessage("message");
        result.setTopic("topic");
        result.setDate(LocalDateTime.now());
        result.setSeen(false);

        when(chessQuestionsService.add(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(result);

        // Act & Assert
        mockMvc.perform(post("/api/chessQuestion").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestForm))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(result.getIdentifier()))
                .andExpect(jsonPath("$.email").value(result.getEmail()))
                .andExpect(jsonPath("$.category").value(result.getCategory()))
                .andExpect(jsonPath("$.message").value(result.getMessage()))
                .andExpect(jsonPath("$.topic").value(result.getTopic()))
                .andExpect(jsonPath("$.seen").value(result.isSeen()));
    }

    @Test
    void testDeleteChessQuestionSuccess() throws Exception {
        // Arrange
        doNothing().when(chessQuestionsService).delete(anyInt());
        // Act & Assert
        mockMvc.perform(delete("/api/chessQuestion/{id}", 1)).andExpect(status().isNoContent());
    }

    @Test
    void testDeleteChessQuestionThrowsException() throws Exception {
        // Arrange
        doThrow(new ChessQuestionServiceException("Question not found")).when(chessQuestionsService).delete(anyInt());
        // Act & Assert
        mockMvc.perform(delete("/api/chessQuestion/{id}", 1)).andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllChessQuestionsReturnQuestionsAnd200OK() throws Exception {
        var gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

        // Mock the service to return a list of chess questions
        when(chessQuestionsService.getAll()).thenReturn(Arrays.asList(
                new PersistentChessQuestionEntity(1, "Topic1", "Message1", "Category1", "email1",
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), false),
                new PersistentChessQuestionEntity(2, "Topic2", "Message2", "Category2", "email2",
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), false)));

        // Perform GET request and check the response
        var responseJson = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/chessQuestion").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        var response = gson.fromJson(responseJson, ChessQuestionsListResponse.class);

        // Convert the Collection to a List for indexing
        List<ChessQuestionResponse> chessQuestionsList = new ArrayList<>(response.chessQuestionList());

        Assertions.assertEquals(2, chessQuestionsList.size(), "list should have 2 questions.");

        var firstElement = chessQuestionsList.getFirst();
        var secondElement = chessQuestionsList.get(1);

        Assertions.assertEquals("Topic1", firstElement.topic(), "first element topic");
        Assertions.assertEquals("Category1", firstElement.category(), "first element category");
        Assertions.assertEquals("email1", firstElement.email(), "first element email");
        Assertions.assertEquals("Message1", firstElement.message(), "first element message");

        Assertions.assertEquals("Topic2", secondElement.topic(), "second element topic");
        Assertions.assertEquals("Category2", secondElement.category(), "second element category");
        Assertions.assertEquals("email2", secondElement.email(), "second element email");
        Assertions.assertEquals("Message2", secondElement.message(), "second element message");
    }

}
