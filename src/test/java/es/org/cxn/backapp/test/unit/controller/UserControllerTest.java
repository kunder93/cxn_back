package es.org.cxn.backapp.test.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import es.org.cxn.backapp.controller.entity.UserController;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.service.UserProfileImageService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultUserService;

/**
 * Unit test class for {@link UserController}. This class tests the behavior of
 * the delete user endpoint in the UserController class.
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    /**
     * The {@link MockMvc} instance used for performing HTTP requests and verifying
     * the responses.
     * <p>
     * {@link MockMvc} is an essential part of Spring's testing framework. It allows
     * you to perform mock HTTP requests and check the results without needing to
     * spin up a full server.
     * </p>
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The {@link UserService} mock bean used to simulate the behavior of the user
     * service in tests.
     * <p>
     * This field is annotated with {@link MockBean}, which creates a mock
     * implementation of {@link UserService} that can be injected into the
     * controller being tested. It allows us to simulate interactions with the
     * service layer, such as calling the delete method, without invoking real
     * logic.
     * </p>
     */
    @MockBean
    private UserService userService;

    /**
     * Mock user profile image service for build userController.
     */
    @MockBean
    private UserProfileImageService userProfileImageService;

    /**
     * Test case to verify the successful deletion of a user when the user exists.
     *
     * <p>
     * This test mocks the delete operation to perform successfully. It then ensures
     * that the response status is 200 OK and that the correct success message is
     * returned.
     * </p>
     *
     * @throws Exception if the mockMvc request fails
     */
    @Test
    @DisplayName("Should permanently delete a user when user exists")
    @WithMockUser
    void deleteUserUserExistsReturnsSuccessMessage() throws Exception {
        // Arrange
        String userEmail = "testuser@example.com";
        doNothing().when(userService).delete(userEmail);

        // Act & Assert
        mockMvc.perform(delete("/api/user/{userEmail}", userEmail)).andExpect(status().isOk())
                .andExpect(content().string("User with email " + userEmail + " has been permanently deleted."));

        // Verify that the delete method was called
        verify(userService).delete(userEmail);
    }

    /**
     * Test case to verify the behavior when attempting to delete a non-existent
     * user.
     *
     * <p>
     * This test mocks the delete operation to throw a {@link UserServiceException}
     * with a message indicating that the user was not found. It then ensures that
     * the response status is 404 Not Found and that the correct error message is
     * returned.
     * </p>
     *
     * @throws Exception if the mockMvc request fails
     */
    @Test
    @DisplayName("Should return 404 when attempting to delete non-existent user")
    @WithMockUser
    void deleteUserUserNotFoundReturns404() throws Exception {
        // Arrange
        String userEmail = "nonexistent@example.com";
        doThrow(new UserServiceException(DefaultUserService.USER_NOT_FOUND_MESSAGE)).when(userService)
                .delete(userEmail);

        // Act & Assert
        mockMvc.perform(delete("/api/user/{userEmail}", userEmail)).andExpect(status().isNotFound())
                // Adjust content check to match JSON response
                .andExpect(jsonPath("$.content").value("404 NOT_FOUND \"User not found.\""))
                .andExpect(jsonPath("$.status").value("failure"));
    }

    @Test
    @DisplayName("Should return 400 when invalid data is provided")
    @WithMockUser
    void updateUserDataInvalidDataReturnsBadRequest() throws Exception {
        // Arrange
        String invalidJson = "{\"name\":\"John\",\"firstSurname\":\"Doe\",\"secondSurname\":\"Smith\",\"birthDate\""
                + ":\"invalid-date\",\"gender\":\"M\"}";

        // Act & Assert
        mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should return 400 when user update fails due to service exception")
    @WithMockUser
    void updateUserDataServiceExceptionReturnsBadRequest() throws Exception {
        // Arrange
        // Mock the service to throw an exception when the update method is called
        when(userService.update(any(UserServiceUpdateDto.class), anyString()))
                .thenThrow(new UserServiceException("User update failed"));

        final var jsonContent = "{\"name\":\"John\",\"firstSurname\":\"Doe\","
                + "\"secondSurname\":\"Smith\",\"birthDate\":\"2000-01-01\",\"gender\":\"M\"}";

        // Act & Assert
        mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isBadRequest()).andExpect(content()
                        .json("{\"content\":\"400 BAD_REQUEST \\\"User update failed\\\"\",\"status\":\"failure\"}"));

        // Verify that the update method was called
        verify(userService).update(any(UserServiceUpdateDto.class), anyString());
    }

    @Test
    @DisplayName("Should update user data successfully when valid data is provided")
    @WithMockUser
    void updateUserDataValidDataReturnsSuccess() throws Exception {
        // Arrange
        String userName = "testuser@example.com";

        // Create a mock UserEntity to return from the update method
        PersistentUserEntity userEntity = new PersistentUserEntity();
        userEntity.setEmail(userName); // Assuming userName is the email
        UserProfile profile = new UserProfile();
        profile.setName("John");
        profile.setFirstSurname("Doe");
        profile.setSecondSurname("Smith");
        profile.setBirthDate(LocalDate.of(2000, 1, 1));
        profile.setGender("M");
        userEntity.setProfile(profile);

        // Mock the service method to return the userEntity
        when(userService.update(any(UserServiceUpdateDto.class), anyString())).thenReturn(userEntity);

        final var jsonContent = "{\"name\":\"John\",\"firstSurname\":\"Doe\",\"secondSurname\":\"Smith\","
                + "\"birthDate\":\"2000-01-01\",\"gender\":\"M\"}";

        final var jsonResponse = "{\"name\":\"John\",\"firstSurname\":\"Doe\",\"secondSurname\":\"Smith\",\"birthDate\""
                + ":\"2000-01-01\",\"gender\":\"M\"}";

        // Act & Assert
        mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isOk()).andExpect(content().json(jsonResponse));

        // Verify that the update method was called with the expected parameters
        verify(userService).update(argThat(dto -> dto.name().equals("John") && dto.firstSurname().equals("Doe")
                && dto.secondSurname().equals("Smith") && dto.birthDate().equals(LocalDate.of(2000, 1, 1))
                && dto.gender().equals("M")), anyString());
    }

}
