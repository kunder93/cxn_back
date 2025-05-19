
package es.org.cxn.backapp.test.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.controller.entity.ActivitiesController;
import es.org.cxn.backapp.service.ActivitiesService;
import es.org.cxn.backapp.service.dto.ActivityDto;
import es.org.cxn.backapp.service.exceptions.activity.ActivityNotFoundException;
import es.org.cxn.backapp.service.exceptions.activity.ActivityServiceException;

@WebMvcTest(ActivitiesController.class)
@AutoConfigureMockMvc(addFilters = false)
class ActivitiesControllerTest {

    /**
     * MockMvc instance used to perform HTTP requests and validate responses in the
     * unit tests. It is injected by the Spring Test framework to simulate HTTP
     * requests to the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock bean for the ActivitiesService, which is used to mock the service layer
     * methods during testing. It allows for testing controller logic without
     * invoking the actual service implementation.
     */
    @MockitoBean
    private ActivitiesService activitiesStateService;

    /**
     * Mock bean for the SecurityContextHolder, which is used to manage the security
     * context during testing. This is typically used for mocking the current
     * authenticated user's context in security-related tests.
     */
    @MockitoBean
    private SecurityContextHolder securityContextHolder;

    /**
     * Provides a stream of invalid titles that are routable (i.e., they reach the
     * controller), but fail validation logic due to being blank or exceeding the
     * maximum allowed length.
     *
     * @return a stream of strings representing invalid but routable activity titles
     */
    private static Stream<String> invalidButRoutableTitles() {
        return Stream.of("   ", "A".repeat(81));
    }

    /**
     * Provides a stream of titles that lead to a 404 Not Found due to incorrect
     * path resolution.
     *
     * @return a stream of strings representing URLs that should result in 404
     */
    private static Stream<String> titlesThatProduce404() {
        return Stream.of("");
    }

    /**
     * Provides a stream of URLs that result in 405 Method Not Allowed when the
     * DELETE method is not mapped for the given path.
     *
     * @return a stream of URLs that should produce HTTP 405 responses
     */
    private static Stream<String> titlesThatProduce405() {
        return Stream.of("/api/activities");
    }

    /**
     * Tests that the controller returns {@code 200 OK} and the correct activity
     * details when a valid title is provided and the activity exists in the system.
     *
     * @throws Exception if the request or JSON serialization fails
     */
    @Test
    void getActivityShouldReturnActivityResponseWhenActivityExists() throws Exception {
        // Arrange test data
        String title = "Test Activity";
        String description = "Test Description";
        String category = "Test Category";
        ActivityDto activityDto = new ActivityDto(title, description, LocalDateTime.now(),
                LocalDateTime.now().plusHours(2), category);

        // Mock service responses
        when(activitiesStateService.getActivity(title)).thenReturn(activityDto);

        // Act & Assert - Perform GET request and validate response
        mockMvc.perform(get("/api/activities/{title}", title)).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title)).andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.startDate").exists()).andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.category").value(category));

        // Verify service interactions
        verify(activitiesStateService, times(1)).getActivity(title);
    }

    /**
     * Tests that the controller returns {@code 400 Bad Request} when the service
     * layer throws an {@link ActivityServiceException} during activity retrieval.
     *
     * <p>
     * Verifies the JSON error structure and ensures that no image retrieval is
     * attempted.
     * </p>
     *
     * @throws Exception if the request or JSON parsing fails
     */
    @Test
    void getActivityShouldReturnBadRequestWhenServiceThrowsException() throws Exception {
        // Arrange test data
        String title = "Nonexistent Activity";

        // Mock service to throw an exception
        when(activitiesStateService.getActivity(anyString()))
                .thenThrow(new ActivityServiceException("Activity not found"));

        // Perform GET request and expect a 400 BAD REQUEST status with specific error
        // message
        mockMvc.perform(get("/api/activities/{title}", title)).andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"Activity not found\""))
                .andExpect(jsonPath("$.status").value("failure"));

        // Verify that the service was called once for `getActivity` and not for
        // `getActivityImage`
        verify(activitiesStateService, times(1)).getActivity(title);
        verify(activitiesStateService, times(0)).getActivityImage(title);
    }

    /**
     * Tests that the controller returns {@code 200 OK} and a list of activities
     * when the service returns a valid stream of activities.
     *
     * @throws Exception if the request or JSON serialization fails
     */
    @Test
    void getAllActivitiesShouldReturnActivitiesStreamWhenSuccessful() throws Exception {
        // Prepare test data with LocalDateTime values
        LocalDateTime now = LocalDateTime.now();
        ActivityDto activity1 = new ActivityDto("Activity 1", "Description 1", now, now.plusHours(1), "Category 1");
        ActivityDto activity2 = new ActivityDto("Activity 2", "Description 2", now, now.plusHours(2), "Category 2");
        Stream<ActivityDto> activitiesStream = Stream.of(activity1, activity2);

        // Mock the service to return the activities stream
        when(activitiesStateService.getAllActivities()).thenReturn(activitiesStream);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/activities")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Activity 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].startDate").exists()).andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].category").value("Category 1"))
                .andExpect(jsonPath("$[1].title").value("Activity 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].category").value("Category 2"));
        // Verify that the service was called
        verify(activitiesStateService, times(1)).getAllActivities();
    }

    /**
     * Tests that the controller returns {@code 400 Bad Request} when the deletion
     * attempt fails because the specified activity does not exist.
     *
     * <p>
     * Simulates the scenario by having the service throw an
     * {@link ActivityNotFoundException}, which is caught and mapped to a
     * {@link ResponseStatusException}.
     * </p>
     *
     * @throws Exception if the request or exception mapping fails
     */
    void removeActivityShouldReturnBadRequestWhenActivityNotFound() throws Exception {
        String title = "Nonexistent Activity";

        doThrow(new ActivityNotFoundException("Activity not found")).when(activitiesStateService).remove(title);

        mockMvc.perform(delete("/api/activities/{title}", title)).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Error: activity not found\"",
                        result.getResolvedException().getMessage()));

        verify(activitiesStateService, times(1)).remove(title);
    }

    /**
     * Tests that the controller returns {@code 204 No Content} when an activity is
     * successfully deleted.
     *
     * @throws Exception if the request or service interaction fails
     */
    @Test
    void removeActivityShouldReturnNoContentWhenSuccessful() throws Exception {
        String title = "Activity To Delete";

        mockMvc.perform(delete("/api/activities/{title}", title)).andExpect(status().isNoContent());

        verify(activitiesStateService, times(1)).remove(title);
    }

    /**
     * Tests that the controller returns {@code 400 Bad Request} for invalid but
     * routable titles.
     *
     * <p>
     * These titles are non-null and reach the controller, but fail validation
     * constraints. The test ensures that the service method {@code remove()} is
     * never invoked.
     * </p>
     *
     * @param invalidTitle a string that violates the validation rules
     * @throws Exception if the request or response fails
     */
    @ParameterizedTest
    @MethodSource("invalidButRoutableTitles")
    void shouldReturn400WhenTitleIsInvalid(final String invalidTitle) throws Exception {
        mockMvc.perform(delete("/api/activities/{title}", invalidTitle)).andExpect(status().isBadRequest());

        verify(activitiesStateService, never()).remove(anyString());
    }

    /**
     * Tests that DELETE requests with missing or malformed titles result in
     * {@code 404 Not Found}.
     *
     * <p>
     * These cases correspond to malformed URLs or incorrect routing that Spring
     * cannot resolve to the controller method.
     * </p>
     *
     * @param title an invalid or missing path variable
     * @throws Exception if the request fails
     */
    @ParameterizedTest
    @MethodSource("titlesThatProduce404")
    void shouldReturn404WhenTitleMissing(final String title) throws Exception {
        String url = title == null ? "/api/activities" : "/api/activities/";
        mockMvc.perform(delete(url)).andExpect(status().isNotFound());

        verify(activitiesStateService, never()).remove(anyString());
    }

    /**
     * Tests that DELETE requests sent to valid paths but without a DELETE mapping
     * result in {@code 405 Method Not Allowed}.
     *
     * <p>
     * This ensures Spring's routing mechanism responds appropriately when the path
     * exists but doesn't support DELETE.
     * </p>
     *
     * @param url a valid URL path that does not support DELETE
     * @throws Exception if the request fails
     */
    @ParameterizedTest
    @MethodSource("titlesThatProduce405")
    void shouldReturn405WhenPathVariableMissingOrInvalid(final String url) throws Exception {
        mockMvc.perform(delete(url)).andExpect(status().isMethodNotAllowed());

        verify(activitiesStateService, never()).remove(anyString());
    }
}
