
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.org.cxn.backapp.controller.entity.ActivitiesController;
import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.service.ActivitiesService;
import es.org.cxn.backapp.service.dto.ActivityDto;
import es.org.cxn.backapp.service.exceptions.ActivityServiceException;

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

    @Test
    void getActivityShouldReturnActivityResponseWhenActivityExists() throws Exception {
        // Arrange test data
        String title = "Test Activity";
        PersistentActivityEntity activityEntity = new PersistentActivityEntity();
        activityEntity.setTitle(title);
        activityEntity.setDescription("Test Description");
        activityEntity.setStartDate(LocalDateTime.now());
        activityEntity.setEndDate(LocalDateTime.now().plusHours(2));
        activityEntity.setCategory("Test Category");

        byte[] imageBytes = "test-image-content".getBytes(); // Simulate image as byte[]
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // Mock service responses
        when(activitiesStateService.getActivity(title)).thenReturn(activityEntity);
        when(activitiesStateService.getActivityImage(title)).thenReturn(imageBytes);

        // Act & Assert - Perform GET request and validate response
        mockMvc.perform(get("/api/activities/{title}", title)).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Activity"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.startDate").exists()).andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.category").value("Test Category"))
                .andExpect(jsonPath("$.image").value(base64Image)); // Validate Base64-encoded image

        // Verify service interactions
        verify(activitiesStateService, times(1)).getActivity(title);
        verify(activitiesStateService, times(1)).getActivityImage(title);

    }

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
}
