
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.controller.entity.FederateController;
import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.form.requests.ConfirmCancelFederateRequest;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.service.FederateStateService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.test.utils.LocalDateTimeAdapter;

@WebMvcTest(FederateController.class)
@AutoConfigureMockMvc(addFilters = false)
class FederateControllerTest {

    /**
     * Constant representing the first user's DNI (National ID). This value is used
     * to identify the first federated user in test cases.
     */
    private static final String USER_DNI_1 = "12312345A";

    /**
     * Constant representing the second user's DNI (National ID). This value is used
     * to identify the second federated user in test cases.
     */
    private static final String USER_DNI_2 = "67867890B";

    /**
     * Constant representing the number of days ago for the first user's DNI last
     * update. Used to simulate the last update timestamp in the test.
     */
    private static final int DAYS_AGO_1 = 1;

    /**
     * Constant representing the number of days ago for the second user's DNI last
     * update. Used to simulate the last update timestamp in the test.
     */
    private static final int DAYS_AGO_2 = 3;

    /**
     * The MockMvc instance used to perform HTTP requests and assert responses in
     * the tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The mock service used to simulate interactions with the FederateStateService
     * in the test context.
     */
    @MockitoBean
    private FederateStateService federateStateService;

    /**
     * The mock object that holds the SecurityContext for the current security
     * context, used in tests to simulate user authentication and authorization.
     */
    @MockitoBean
    private SecurityContextHolder securityContextHolder;

    /**
     * Service for users used by controller.
     */
    @MockitoBean
    private UserService userService;
    /**
     * The Gson instance configured with a custom adapter to handle LocalDateTime
     * serialization and deserialization.
     */
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

    /**
     * Tests the scenario where a {@link FederateStateServiceException} is thrown
     * during the execution of the {@code changeAutoRenew} method in the
     * {@link FederateStateService}.
     *
     * <p>
     * This test mocks the service layer to simulate the scenario where a
     * {@link FederateStateServiceException} is thrown when attempting to change the
     * auto-renewal status. It ensures that the controller correctly handles the
     * exception and returns a {@code 400 Bad Request} response.
     * </p>
     *
     * @throws Exception if the mockMvc operation fails during the test execution
     */
    @Test
    @WithMockUser
    void testChangeAutoRenewFederateStateServiceException() throws Exception {
        // Mock the service to throw a FederateStateServiceException
        when(federateStateService.changeAutoRenew(anyString())).thenThrow(FederateStateServiceException.class);
        // Perform the PATCH request and assert the response
        mockMvc.perform(patch("/api/user/federate/changeAutoRenew")).andExpect(status().isBadRequest());
    }

    /**
     * Tests the successful execution of the {@code changeAutoRenew} method in the
     * {@link FederateStateService}.
     *
     * <p>
     * This test verifies that when the {@code changeAutoRenew} method is executed
     * successfully, the correct response is returned with the updated federate
     * state. It mocks the service layer to return a
     * {@link PersistentFederateStateEntity} object and asserts that the returned
     * response contains the expected values: - The federate state as a string - The
     * auto-renew status as a boolean - The last update date as a string (LocalDate)
     * </p>
     *
     * @throws Exception if the mockMvc operation fails during the test execution
     */
    @Test
    @WithMockUser
    void testChangeAutoRenewSuccess() throws Exception {
        final String usrDni = "12312345B";
        final String frontImageUrl = "RouteToFileDirectory/somePath";
        final String backImageUrl = "RouteToFileDirectory/somePath";
        final boolean autoRenewal = Boolean.TRUE;
        final LocalDate lastUpdate = LocalDate.now();
        final FederateState federateStatus = FederateState.FEDERATE;
        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity(usrDni, frontImageUrl,
                backImageUrl, autoRenewal, lastUpdate, federateStatus);

        when(federateStateService.changeAutoRenew(anyString())).thenReturn(federateStateEntity);

        mockMvc.perform(patch("/api/user/federate/changeAutoRenew")).andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(federateStatus.name())) // Expect the 'state' as a string
                .andExpect(jsonPath("$.autoRenew").value(autoRenewal)) // Expect 'autoRenewal' as Boolean
                .andExpect(jsonPath("$.dniLastUpdate").value(lastUpdate.toString())); // Expect 'dniLastUpdate' as
                                                                                      // String (LocalDate)
    }

    /**
     * Tests the {@code changeAutoRenew} method in the {@link FederateStateService}
     * when a {@link UserServiceException} is thrown.
     *
     * <p>
     * This test verifies that when the {@code changeAutoRenew} method encounters a
     * {@link UserServiceException}, it responds with a {@code 400 Bad Request}
     * status. The test mocks the service layer to throw a
     * {@link UserServiceException} and asserts that the appropriate HTTP status is
     * returned.
     * </p>
     *
     * @throws Exception if the mockMvc operation fails during the test execution
     */
    @Test
    @WithMockUser
    void testChangeAutoRenewUserServiceException() throws Exception {

        // Mock the service to throw a UserServiceException
        when(federateStateService.changeAutoRenew(anyString())).thenThrow(UserServiceException.class);

        // Perform the PATCH request and assert the response
        mockMvc.perform(patch("/api/user/federate/changeAutoRenew")).andExpect(status().isBadRequest());
    }

    /**
     * Tests the {@code confirmCancelFederate} method in the
     * {@link FederateStateService} when a {@link FederateStateServiceException} is
     * thrown.
     *
     * <p>
     * This test verifies that when the {@code confirmCancelFederate} method
     * encounters a {@link FederateStateServiceException}, it responds with a
     * {@code 400 Bad Request} status. The test mocks the service layer to throw a
     * {@link FederateStateServiceException} with a specific error message and
     * asserts that the appropriate HTTP status is returned.
     * </p>
     *
     * @throws Exception if the mockMvc operation fails during the test execution
     */
    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testConfirmCancelFederateFederateStateServiceException() throws Exception {
        final String validDni = "12312345B";
        final ConfirmCancelFederateRequest requestBody = new ConfirmCancelFederateRequest(validDni);

        // Mock the service to throw a FederateStateServiceException
        when(federateStateService.confirmCancelFederate(validDni))
                .thenThrow(new FederateStateServiceException("Federate state service error"));

        final String requestBodyJson = gson.toJson(requestBody);

        // Perform the PATCH request and validate that the exception is handled
        mockMvc.perform(patch("/api/user/federate").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
                .andExpect(status().isBadRequest()); // Expecting 400 Bad Request status
    }

    /**
     * Tests the {@code confirmCancelFederate} method in the
     * {@link FederateStateService} when it completes successfully and returns a
     * valid {@link PersistentFederateStateEntity}.
     *
     * <p>
     * This test verifies that when the {@code confirmCancelFederate} method is
     * invoked with a valid DNI, the response contains the expected federate state,
     * auto-renewal status, and the last update date. The test mocks the service
     * layer to return a {@link PersistentFederateStateEntity} with the predefined
     * properties such as DNI, federate state, and other related fields. It then
     * asserts that the response from the controller correctly matches these values.
     * </p>
     *
     * @throws Exception if the mockMvc operation fails during the test execution
     */
    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testConfirmCancelFederateOk() throws Exception {
        final String validDni = "12312345B";
        final ConfirmCancelFederateRequest requestBody = new ConfirmCancelFederateRequest(validDni);

        final String frontImageUrl = "RouteToFileDirectory/somePath";
        final String backImageUrl = "RouteToFileDirectory/somePath";
        final boolean autoRenewal = Boolean.TRUE;
        final LocalDate lastUpdate = LocalDate.now();
        final FederateState federateStatus = FederateState.FEDERATE;
        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity(validDni, frontImageUrl,
                backImageUrl, autoRenewal, lastUpdate, federateStatus);
        when(federateStateService.confirmCancelFederate(validDni)).thenReturn(federateStateEntity);

        final String requestBodyJson = gson.toJson(requestBody);

        mockMvc.perform(patch("/api/user/federate").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
                .andExpect(status().isOk()) // Expecting 200 OK status
                .andExpect(jsonPath("$.dni").value(validDni)) // Check the DNI in the response
                .andExpect(jsonPath("$.state").value(federateStateEntity.getState().name())) // Check the federate state
                .andExpect(jsonPath("$.autoRenew").value(federateStateEntity.isAutomaticRenewal())) // Check auto-renew
                                                                                                    // // status
                .andExpect(jsonPath("$.dniLastUpdate").value(federateStateEntity.getDniLastUpdate().toString()));
    }

    /**
     * Tests the {@code confirmCancelFederate} method in the
     * {@link FederateStateService} when a {@link UserServiceException} is thrown.
     *
     * <p>
     * This test verifies that when the {@code confirmCancelFederate} method
     * encounters a {@link UserServiceException}, it responds with a
     * {@code 400 Bad Request} status. The test mocks the service layer to throw a
     * {@link UserServiceException} with a specific error message and asserts that
     * the appropriate HTTP status is returned.
     * </p>
     *
     * @throws Exception if the mockMvc operation fails during the test execution
     */
    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testConfirmCancelFederateUserServiceException() throws Exception {
        final String validDni = "12312345B";
        final ConfirmCancelFederateRequest requestBody = new ConfirmCancelFederateRequest(validDni);

        // Mock the service to throw a UserServiceException
        when(federateStateService.confirmCancelFederate(validDni))
                .thenThrow(new UserServiceException("User service error"));

        final String requestBodyJson = gson.toJson(requestBody);

        // Perform the PATCH request and validate that the exception is handled
        mockMvc.perform(patch("/api/user/federate").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
                .andExpect(status().isBadRequest()); // Expecting 400 Bad Request status
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testFederateMember() throws Exception {
        // Mock data for the test
        final String userEmail = "test@example.com"; // The email of the mock user
        final String validDni = "12312345B";
        final boolean autoRenewal = true;
        final String frontImageUrl = "frontImageUrl";
        final String backImageUrl = "backImageUrl";
        final LocalDate lastUpdate = LocalDate.now();
        final FederateState federateState = FederateState.FEDERATE;

        // Create a mock FederateStateEntity with valid data
        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setUserDni(validDni);
        federateStateEntity.setState(federateState);
        federateStateEntity.setAutomaticRenewal(autoRenewal);
        federateStateEntity.setDniLastUpdate(lastUpdate);
        federateStateEntity.setDniFrontImageUrl(frontImageUrl);
        federateStateEntity.setDniBackImageUrl(backImageUrl);

        // Mock the service method to return the mock entity
        when(federateStateService.federateMember(eq(userEmail), any(MultipartFile.class), any(MultipartFile.class),
                eq(autoRenewal))).thenReturn(federateStateEntity);

        // Create mock multipart files for front and back DNI
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
                "mockfrontDniContent".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "backDni.jpg", "image/jpeg",
                "mockbackDniContent".getBytes());

        // Perform the request
        mockMvc.perform(multipart("/api/user/federate").file(frontDni).file(backDni)
                .param("autoRenewal", String.valueOf(autoRenewal)).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.state").value(federateState.name())) // Check if state is correct in response
                .andExpect(jsonPath("$.autoRenew").value(autoRenewal)) // Check autoRenewal flag in response
                .andExpect(jsonPath("$.dniLastUpdate").value(lastUpdate.toString())); // Check the last update date
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testFederateMemberFederateStateServiceException() throws Exception {
        // Mock data
        final String userEmail = "test@example.com";
        final boolean autoRenewal = true;

        // Mock the service method to throw FederateStateServiceException
        when(federateStateService.federateMember(eq(userEmail), any(MultipartFile.class), any(MultipartFile.class),
                eq(autoRenewal)))
                .thenThrow(new FederateStateServiceException("Federate state service exception occurred"));

        // Create mock multipart files for front and back DNI
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
                "mockfrontDniContent".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "backDni.jpg", "image/jpeg",
                "mockbackDniContent".getBytes());

        // Perform the request and expect a 400 BAD REQUEST with the exception message
        mockMvc.perform(multipart("/api/user/federate").file(frontDni).file(backDni)
                .param("autoRenewal", String.valueOf(autoRenewal)).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest()).andExpect(
                        jsonPath("$.content").value("400 BAD_REQUEST \"Federate state service exception occurred\""));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testFederateMemberUserServiceException() throws Exception {
        // Mock data
        final String userEmail = "test@example.com";
        final boolean autoRenewal = true;

        // Mock the service method to throw UserServiceException
        when(federateStateService.federateMember(eq(userEmail), any(MultipartFile.class), any(MultipartFile.class),
                eq(autoRenewal))).thenThrow(new UserServiceException("User service exception occurred"));

        // Create mock multipart files for front and back DNI
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
                "mockfrontDniContent".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "backDni.jpg", "image/jpeg",
                "mockbackDniContent".getBytes());

        // Perform the request and expect a 400 BAD REQUEST with the exception message
        mockMvc.perform(multipart("/api/user/federate").file(frontDni).file(backDni)
                .param("autoRenewal", String.valueOf(autoRenewal)).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"User service exception occurred\""));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetFederateStateFederateStateServiceException() throws Exception {
        // Mock data
        final String userEmail = "test@example.com";

        // Mock the service method to throw FederateStateServiceException
        when(federateStateService.getFederateDataByEmail(userEmail))
                .thenThrow(new FederateStateServiceException("Federate state service exception occurred"));

        // Perform the GET request and expect a 400 BAD REQUEST with the exception
        // message
        mockMvc.perform(get("/api/user/federate")).andExpect(status().isBadRequest()).andExpect(
                jsonPath("$.content").value("400 BAD_REQUEST \"Federate state service exception occurred\""));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = { "ADMIN" })
    void testGetFederateStateMembersEmptyList() throws Exception {
        // Mock the service method to return an empty list
        when(federateStateService.getAll()).thenReturn(List.of());

        // Perform the GET request and expect an empty response list
        mockMvc.perform(get("/api/user/federate/getAll")).andExpect(status().isOk())
                .andExpect(jsonPath("$.federateStateMembersList").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = { "ADMIN" })
    void testGetFederateStateMembersSuccess() throws Exception {
        // Mock data for two federate state entries
        PersistentFederateStateEntity entity1 = new PersistentFederateStateEntity();
        entity1.setUserDni(USER_DNI_1);
        entity1.setState(FederateState.FEDERATE);
        entity1.setAutomaticRenewal(true);
        entity1.setDniLastUpdate(LocalDate.now().minusDays(DAYS_AGO_1));

        PersistentFederateStateEntity entity2 = new PersistentFederateStateEntity();
        entity2.setUserDni(USER_DNI_2);
        entity2.setState(FederateState.NO_FEDERATE);
        entity2.setAutomaticRenewal(false);
        entity2.setDniLastUpdate(LocalDate.now().minusDays(DAYS_AGO_2));

        // Mock the service method to return the list of entities
        when(federateStateService.getAll()).thenReturn(List.of(entity1, entity2));

        // Perform the GET request and validate response
        mockMvc.perform(get("/api/user/federate/getAll")).andExpect(status().isOk())
                .andExpect(jsonPath("$.federateStateMembersList[0].dni").value(USER_DNI_1))
                .andExpect(jsonPath("$.federateStateMembersList[0].state").value(FederateState.FEDERATE.name()))
                .andExpect(jsonPath("$.federateStateMembersList[0].autoRenew").value(true))
                .andExpect(jsonPath("$.federateStateMembersList[0].dniLastUpdate")
                        .value(LocalDate.now().minusDays(DAYS_AGO_1).toString()))
                .andExpect(jsonPath("$.federateStateMembersList[1].dni").value(USER_DNI_2))
                .andExpect(jsonPath("$.federateStateMembersList[1].state").value(FederateState.NO_FEDERATE.name()))
                .andExpect(jsonPath("$.federateStateMembersList[1].autoRenew").value(false))
                .andExpect(jsonPath("$.federateStateMembersList[1].dniLastUpdate")
                        .value(LocalDate.now().minusDays(DAYS_AGO_2).toString()));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetFederateStateSuccess() throws Exception {
        // Mock data
        final String userEmail = "test@example.com";
        final String validDni = "12312345B";
        final boolean autoRenewal = true;
        final LocalDate lastUpdate = LocalDate.now();
        final FederateState federateState = FederateState.FEDERATE;

        // Create a mock FederateStateEntity
        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setUserDni(validDni);
        federateStateEntity.setState(federateState);
        federateStateEntity.setAutomaticRenewal(autoRenewal);
        federateStateEntity.setDniLastUpdate(lastUpdate);

        // Mock the service method to return the mock entity
        when(federateStateService.getFederateDataByEmail(userEmail)).thenReturn(federateStateEntity);

        // Perform the GET request and validate response
        mockMvc.perform(get("/api/user/federate")).andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(federateState.name()))
                .andExpect(jsonPath("$.autoRenew").value(autoRenewal))
                .andExpect(jsonPath("$.dniLastUpdate").value(lastUpdate.toString()));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetFederateStateUserServiceException() throws Exception {
        // Mock data
        final String userEmail = "test@example.com";

        // Mock the service method to throw UserServiceException
        when(federateStateService.getFederateDataByEmail(userEmail))
                .thenThrow(new UserServiceException("User service exception occurred"));

        // Perform the GET request and expect a 400 BAD REQUEST with the exception
        // message
        mockMvc.perform(get("/api/user/federate")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"User service exception occurred\""));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testUpdateDniFederateStateServiceException() throws Exception {
        // Create mock front and back DNI files
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "front.jpg", MediaType.IMAGE_JPEG_VALUE,
                "front-content".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "back.jpg", MediaType.IMAGE_JPEG_VALUE,
                "back-content".getBytes());

        // Mock the service to throw a FederateStateServiceException
        when(federateStateService.updateDni("user@example.com", frontDni, backDni))
                .thenThrow(new FederateStateServiceException("DNI update failed"));

        // Perform the PATCH request and expect a 400 Bad Request status
        mockMvc.perform(multipart("/api/user/federate/updateDni").file(frontDni).file(backDni).with(request -> {
            request.setMethod("PATCH"); // Manually set the method to PATCH
            return request;
        })).andExpect(status().isBadRequest()) // Expect 400 Bad Request
                .andExpect(jsonPath("$.status").value("failure")) // Check failure status
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"DNI update failed\"")); // Check error message
                                                                                                  // only
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testUpdateDniSuccess() throws Exception {
        // Create mock front and back DNI files
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "front.jpg", MediaType.IMAGE_JPEG_VALUE,
                "front-content".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "back.jpg", MediaType.IMAGE_JPEG_VALUE,
                "back-content".getBytes());

        // Mock the response entity for a successful update
        PersistentFederateStateEntity mockEntity = new PersistentFederateStateEntity();
        mockEntity.setState(FederateState.FEDERATE);
        mockEntity.setAutomaticRenewal(true);
        mockEntity.setDniLastUpdate(LocalDate.now());

        // Mock the service method to return the mock entity
        when(federateStateService.updateDni("user@example.com", frontDni, backDni)).thenReturn(mockEntity);

        // Perform the PATCH request and expect a 200 OK status
        mockMvc.perform(multipart("/api/user/federate/updateDni") // multipart() will automatically use POST, so we need
                                                                  // to set PATCH
                .file(frontDni).file(backDni).with(request -> {
                    request.setMethod("PATCH"); // Manually set method to PATCH
                    return request;
                })).andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.state").value(FederateState.FEDERATE.name())) // Check state is FEDERATE
                .andExpect(jsonPath("$.autoRenew").value(true)); // Check autoRenew is true
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testUpdateDniUserServiceException() throws Exception {
        // Create mock front and back DNI files
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "front.jpg", MediaType.IMAGE_JPEG_VALUE,
                "front-content".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "back.jpg", MediaType.IMAGE_JPEG_VALUE,
                "back-content".getBytes());

        // Mock the service to throw a UserServiceException
        when(federateStateService.updateDni("user@example.com", frontDni, backDni))
                .thenThrow(new UserServiceException("User not found"));

        // Perform the PATCH request and expect a 400 Bad Request status
        mockMvc.perform(multipart("/api/user/federate/updateDni") // Use multipart() instead of patch()
                .file(frontDni).file(backDni).with(request -> {
                    request.setMethod("PATCH"); // Manually set method to PATCH
                    return request;
                })).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"User not found\"")); // Update the assertion
    }

}
