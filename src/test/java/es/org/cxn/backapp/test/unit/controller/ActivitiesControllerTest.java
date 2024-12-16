
package es.org.cxn.backapp.test.unit.controller;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import es.org.cxn.backapp.controller.entity.ActivitiesController;
import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.service.ActivitiesService;
import es.org.cxn.backapp.service.dto.ActivityWithImageDto;
import es.org.cxn.backapp.service.exceptions.ActivityServiceException;

@WebMvcTest(ActivitiesController.class)
@Import(TestSecurityConfiguration.class)
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
    @MockBean
    private ActivitiesService activitiesStateService;

    /**
     * Mock bean for the SecurityContextHolder, which is used to manage the security
     * context during testing. This is typically used for mocking the current
     * authenticated user's context in security-related tests.
     */
    @MockBean
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
        ActivityWithImageDto activity1 = new ActivityWithImageDto("Activity 1", "Description 1", now, now.plusHours(1),
                "Category 1", "image1.png");
        ActivityWithImageDto activity2 = new ActivityWithImageDto("Activity 2", "Description 2", now, now.plusHours(2),
                "Category 2", "image2.png");
        Stream<ActivityWithImageDto> activitiesStream = Stream.of(activity1, activity2);

        // Mock the service to return the activities stream
        when(activitiesStateService.getAllActivities()).thenReturn(activitiesStream);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/activities")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Activity 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].startDate").exists()).andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].category").value("Category 1"))
                .andExpect(jsonPath("$[0].image").value("image1.png"))
                .andExpect(jsonPath("$[1].title").value("Activity 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].category").value("Category 2"))
                .andExpect(jsonPath("$[1].image").value("image2.png"));

        // Verify that the service was called
        verify(activitiesStateService, times(1)).getAllActivities();
    }

    @Test
    void getAllActivitiesShouldThrowResponseStatusExceptionWhenServiceThrowsException() throws Exception {
        // Mock the service to throw an ActivityServiceException
        when(activitiesStateService.getAllActivities())
                .thenThrow(new ActivityServiceException("Failed to retrieve activities"));

        // Perform the GET request and verify the response status and message
        mockMvc.perform(get("/api/activities")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"Failed to retrieve activities\""));

        // Verify that the service was called once
        verify(activitiesStateService, times(1)).getAllActivities();
    }

}
