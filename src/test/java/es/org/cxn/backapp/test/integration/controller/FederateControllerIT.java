package es.org.cxn.backapp.test.integration.controller;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import es.org.cxn.backapp.controller.entity.FederateController;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.service.FederateStateService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;
import jakarta.mail.internet.MimeMessage;

/**
 * Integration tests for the FederateController.
 *
 * <p>
 * These tests verify that the REST endpoints related to address data retrieval
 * work as expected.
 * </p>
 *
 *
 * @author Santiago Paz
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class FederateControllerIT {

    /**
     * Gson instance used for converting Java objects to JSON and vice versa. This
     * static instance is used for serializing and deserializing request and
     * response payloads in the tests.
     */
    private static Gson gson;

    /**
     * The URL endpoint for user sign-up. This static final string represents the
     * URL used for user registration in the authentication process.
     */
    private static final String SIGN_UP_URL = "/api/auth/signup";
    /**
     * URL endpoint for user sign-in. This static final string represents the URL
     * used for user authentication and generating JWT tokens.
     */
    private static final String SIGN_IN_URL = "/api/auth/signinn";

    /**
     * The email service mocked implementation.
     */
    @MockitoBean
    private DefaultEmailService defaultEmailService;

    /**
     * The MockMvc instance used for performing HTTP requests and testing the
     * response in the FederateController integration tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The FederateStateService mock used in the integration test to simulate the
     * service layer and avoid calling the actual business logic.
     */
    @Mock
    private FederateStateService federateStateService;

    /**
     * User service used by federate controller.
     */
    @Mock
    private UserService userService;

    /**
     * The FederateController mock injected into the test. It contains the methods
     * that will be tested in the integration test.
     */
    @InjectMocks
    private FederateController federateController;

    /**
     * The JavaMailSender mock used to mock email-related functionality in the
     * integration tests.
     */
    @MockitoBean
    private JavaMailSender javaMailSender;

    /**
     * The JWT token generated for a user that will be used in testing the sign-in
     * functionality in subsequent requests.
     */
    private String userAJwtToken;

    FederateControllerIT() {
        super();
    }

    @DynamicPropertySource
    static void setProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> "1025");
        registry.add("spring.mail.username", () -> "test@example.com");
        registry.add("spring.mail.password", () -> "testpassword");
    }

    @BeforeAll
    static void setup() {
        gson = UsersControllerFactory.GSON;
    }

    private String authenticateAndGetToken(final String email, final String password) throws Exception {
        var authRequest = new AuthenticationRequest(email, password);
        var authRequestJson = gson.toJson(authRequest);

        var response = mockMvc
                .perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        var authResponse = gson.fromJson(response, AuthenticationResponse.class);
        return authResponse.jwt();
    }

    @BeforeEach
    void setupEach() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock the MimeMessage to avoid NullPointerException
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var userARequestJson = gson.toJson(userARequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        userAJwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

    }

//    @Test
//    @Transactional
//    void testChangeAutoRenewNoFederatedMemberBadRequest() throws Exception {
//        mockMvc.perform(patch("/api/user/federate/changeAutoRenew").header("Authorization", "Bearer " + userAJwtToken))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.content")
//                        .value("400 BAD_REQUEST \"Federate state is not : FEDERATE for user with dni: 32721860J\""))
//                .andExpect(jsonPath("$.status").value("failure"));
//    }

//    @Test
//    @Transactional
//    void testFederateMember() throws Exception {
//        // Mock MultipartFile for front and back DNI images
//        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
//                "front image content".getBytes());
//        MockMultipartFile backDni = new MockMultipartFile("backDni", "backDni.jpg", "image/jpeg",
//                "back image content".getBytes());
//
//        // Perform federate member request
//        mockMvc.perform(multipart("/api/user/federate").file(frontDni).file(backDni).param("autoRenewal", "true")
//                .header("Authorization", "Bearer " + userAJwtToken)).andExpect(status().isOk()); // structure
//    }

    @Test
    @Transactional
    void testFederateMemberNoDniImageBadRequest() throws Exception {
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
                "front image content".getBytes());

        mockMvc.perform(multipart("/api/user/federate").file(frontDni) // Missing backDni
                .param("autoRenewal", "true").header("Authorization", "Bearer " + userAJwtToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("Required part 'backDni' is not present."))
                .andExpect(jsonPath("$.status").value("failure"));
    }

//    @Test
//    @Transactional
//    void testGetFederateStateFederateMemberGetChangedFederateState() throws Exception {
//        final String noFederateDniLastUpdateData = "1900-02-02";
//        // GET INITIAL FEDERATE STATE
//        mockMvc.perform(get("/api/user/federate").header("Authorization", "Bearer " + userAJwtToken))
//                .andExpect(status().isOk()).andExpect(jsonPath("$.state").value("NO_FEDERATE"))
//                .andExpect(jsonPath("$.autoRenew").value(false))
//                .andExpect(jsonPath("$.dniLastUpdate").value(noFederateDniLastUpdateData));
//
//        // MEMBER DO FEDERATE REQUEST
//        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
//                "front image content".getBytes());
//        MockMultipartFile backDni = new MockMultipartFile("backDni", "backDni.jpg", "image/jpeg",
//                "back image content".getBytes());
//
//        // Perform federate member request
//        mockMvc.perform(multipart("/api/user/federate").file(frontDni).file(backDni).param("autoRenewal", "true")
//                .header("Authorization", "Bearer " + userAJwtToken)).andExpect(status().isOk()); // structure
//
//        // GET CHANGED FEDERATE STATE
//        mockMvc.perform(get("/api/user/federate").header("Authorization", "Bearer " + userAJwtToken))
//                .andExpect(status().isOk()).andExpect(jsonPath("$.state").value("IN_PROGRESS"))
//                .andExpect(jsonPath("$.autoRenew").value(true))
//                .andExpect(jsonPath("$.dniLastUpdate").value(LocalDate.now().toString()));
//
//    }

}
