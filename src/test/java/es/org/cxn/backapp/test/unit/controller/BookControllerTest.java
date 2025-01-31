
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.controller.entity.member_resources.BookController;
import es.org.cxn.backapp.model.form.requests.member_resources.AddBookRequestDto;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import es.org.cxn.backapp.service.exceptions.BookServiceException;
import es.org.cxn.backapp.service.impl.DefaultBookService;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;

/**
 * Unit tests for the {@link LibraryController} class.
 * <p>
 * This test class contains unit tests for the methods in the
 * {@link LibraryController} class. It uses MockMvc to perform requests and
 * verify responses, and Mockito to mock dependencies like
 * {@link DefaultLibraryService} and {@link DefaultJwtUtils}.
 * </p>
 * <p>
 * The tests cover various scenarios for managing books, including retrieving,
 * adding, and removing books, as well as handling success and failure cases.
 * </p>
 */
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    /**
     * The URL for library-related API endpoints.
     */
    private static final String LIBRARY_URL = "/api/resources/book";

    /**
     * ISBN for test book add/remove operations.
     */
    private static final String TEST_ISBN = "1231231231";

    /**
     * Book publish year for test.
     */
    private static final LocalDate PUBLISH_YEAR = LocalDate.of(2024, 1, 1);

    /**
     * Mocked image file.
     */
    final MultipartFile mockFile = mock(MultipartFile.class);

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
     * Mock of the {@link DefaultLibraryService} used to simulate interactions with
     * the library service layer.
     * <p>
     * This mock is used to stub and verify interactions with the library service
     * during testing, without invoking the actual service layer logic.
     * </p>
     */
    @MockitoBean
    private DefaultBookService bookService;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
        var bookRequest = new AddBookRequestDto(TEST_ISBN, "Test Book", "description", // title
                "Fiction", // gender
                PUBLISH_YEAR, "English", // language
                List.of() // authorsList
        );

        var serviceException = new BookServiceException("Failed to add book");

        // Mock the libraryService to throw an exception when addBook is called
        doThrow(serviceException).when(bookService).add(any(AddBookRequestDto.class), any());

        var gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        var bookRequestJson = gson.toJson(bookRequest);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/resources/book").contentType(MediaType.APPLICATION_JSON)
                .content(bookRequestJson).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    /**
     * Tests that a book is successfully added to the library.
     * <p>
     * This test performs a POST request to add a new book and verifies that the
     * response status is CREATED (201).
     * </p>
     *
     * @throws Exception if the test fails
     */
//    @Test
//    @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
//    void testAddBookSuccess() throws Exception {
//        // Arrange
//        var bookRequest = new AddBookRequestDto(TEST_ISBN, "Test Book", "description", "Fiction", // gender
//                PUBLISH_YEAR, "English", // language
//                List.of(new AuthorRequest("Test Author", "A", "Spain")));
//
//        var addedBook = PersistentBookEntity.builder().isbn(TEST_ISBN).title("Test Book").publishYear(PUBLISH_YEAR)
//                .build();
//
//        var gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//                .create();
//
//        var bookRequestJson = gson.toJson(bookRequest);
//        // Simulate a null or empty file
//        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "", "image/jpeg", new byte[] {});
//
//        when(bookService.add(any(AddBookRequestDto.class), any())).thenReturn(addedBook);
//
//        // Act and Assert
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/resources/book").file(imageFile) // Empty file
//                .param("data", bookRequestJson) // Correctly include 'data' as a parameter
//                .contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andExpect(jsonPath("$.isbn").value(TEST_ISBN))
//                .andExpect(jsonPath("$.title").value("Test Book"));
//    }

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
        var serviceException = new BookServiceException("Failed to remove book");

        doThrow(serviceException).when(bookService).remove(TEST_ISBN);

        // Act and Assert
        final var mockedRequest = mockMvc.perform(
                MockMvcRequestBuilders.delete(LIBRARY_URL + "/{isbn}", TEST_ISBN).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockedRequest.andExpect(result -> {
            // Verify the response body contains the expected exception message
            Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException,
                    "The exception is expected as Response Status");
        });
    }

    /**
     * Tests that a book is successfully removed from the library.
     * <p>
     * This test performs a DELETE request to remove a book and verifies that the
     * response status is OK (200).
     * </p>
     *
     * @throws Exception if the test fails
     */
    @Test
    void testRemoveBookSuccess() throws Exception {
        // Arrange
        doNothing().when(bookService).remove(TEST_ISBN);

        // Act and Assert
        mockMvc.perform(
                MockMvcRequestBuilders.delete(LIBRARY_URL + "/{isbn}", TEST_ISBN).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
