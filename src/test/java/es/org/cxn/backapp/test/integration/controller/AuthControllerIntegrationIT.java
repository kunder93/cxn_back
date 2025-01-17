
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

/**
 * Integration tests for the Authentication Controller.
 *
 * <p>
 * This class tests the various endpoints related to user authentication,
 * including user registration (sign-up) and authentication (sign-in). The tests
 * cover scenarios such as successful user registration, handling duplicate user
 * information, and generating valid JWT tokens upon successful authentication.
 * </p>
 *
 * <p>
 * The tests are conducted in a transactional context to ensure data consistency
 * and to isolate each test case.
 * </p>
 *
 * <p>
 * <strong>Note:</strong> This class requires a running Spring context and uses
 * {@link MockMvc} to perform HTTP requests and validate responses.
 * </p>
 *
 * <p>
 * Author: Santiago Paz
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("/application.properties")
@ActiveProfiles("test")
class AuthControllerIntegrationIT {

    /**
     * URL endpoint for user sign-up.
     */
    private static final String SIGN_UP_URL = "/api/auth/signup";
    /**
     * URL endpoint for user sign-in.
     */
    private static final String SIGN_IN_URL = "/api/auth/signinn";

    /**
     * Gson instance for serializing/deserializing JSON objects during the tests.
     */
    private static Gson gson;

    /**
     * The email service mocked implementation.
     */
    @MockitoBean
    private DefaultEmailService defaultEmailService;

    /**
     * Mocked mail sender.
     */
    @MockitoBean
    private JavaMailSender javaMailSender; // Mockear el bean de JavaMailSender

    /**
     * Simulated mime message.
     */
    private MimeMessage mimeMessage = new MimeMessage((Session) null);

    /**
     * MockMvc is used to simulate HTTP requests in the tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Main class constructor.
     */
    AuthControllerIntegrationIT() {
        super();
    }

    /**
     * Sets up the test environment by initializing the Gson instance with a custom
     * adapter for handling {@link LocalDate} objects.
     *
     * <p>
     * This method is annotated with {@link BeforeAll}, which means it will be
     * executed once before any test methods in this class are run.
     * </p>
     */
    @BeforeAll
    static void createUsersData() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    }

    /**
     * SignIn with bad password unauthorized.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testAuthenticateUserBadPasswordUnauthorized() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequestJson = UsersControllerFactory.getUserARequestJson();
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var notValidPassword = "NotValidPassword";
        var authenticationRequest = new AuthenticationRequest(UsersControllerFactory.USER_A_EMAIL, notValidPassword);
        var authenticationRequestJson = gson.toJson(authenticationRequest);

        // Second user with same email as userA bad request.
        mockMvc.perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authenticationRequestJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    /**
     * SignIn not existing user is unauthorized.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testAuthenticateUserNotExistingUserUnauthorized() throws Exception {
        // Authenticate not existing user (no signUp)
        var authenticationRequest = new AuthenticationRequest(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);
        var authenticationRequestJson = gson.toJson(authenticationRequest);

        // Second user with same email as userA bad request.
        mockMvc.perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authenticationRequestJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    /**
     * SignIn return valid jwt.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testAuthenticateUserReturnJwt() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequestJson = UsersControllerFactory.getUserARequestJson();
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var authenticationRequest = new AuthenticationRequest(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);
        var authenticationRequestJson = gson.toJson(authenticationRequest);

        // Second user with same email as userA bad request.
        var authenticationResponseJson = mockMvc
                .perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authenticationRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        var ar = gson.fromJson(authenticationResponseJson, AuthenticationResponse.class);
        var jwtToken = ar.jwt();
        var jwtUsername = DefaultJwtUtils.extractUsername(jwtToken);
        Assertions.assertEquals(UsersControllerFactory.USER_A_EMAIL, jwtUsername,
                "Jwt username is same as user signUp");
    }

    /**
     * SingUp new user return user info with default role "ROLE_SOCIO".
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testSignUpReturnUserDataWithDefautlRole() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var numberOfRoles = 1;
        var userARequestJson = UsersControllerFactory.getUserARequestJson();
        // Register user correctly
        var controllerResponse = mockMvc
                .perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();

        var signUpResponse = gson.fromJson(controllerResponse, SignUpResponseForm.class);

        Assertions.assertEquals(UsersControllerFactory.USER_A_DNI, signUpResponse.dni(), "Dni field.");
        Assertions.assertEquals(UsersControllerFactory.USER_A_EMAIL, signUpResponse.email(), "Email field.");
        Assertions.assertEquals(UsersControllerFactory.USER_A_NAME, signUpResponse.name(), "Dni field.");
        Assertions.assertEquals(UsersControllerFactory.USER_A_FIRST_SURNAME, signUpResponse.firstSurname(),
                "First surname field.");
        Assertions.assertEquals(UsersControllerFactory.USER_A_SECOND_SURNAME, signUpResponse.secondSurname(),
                "Second surname field.");

        Assertions.assertEquals(UsersControllerFactory.USER_A_GENDER, signUpResponse.gender(), "Gender field.");
        Assertions.assertEquals(UsersControllerFactory.USER_A_BIRTH_DATE, signUpResponse.birthDate(),
                "Birth date field.");
        Assertions.assertEquals(UsersControllerFactory.USER_A_KIND_MEMBER, signUpResponse.kindMember(),
                "kind of member field.");
        Assertions.assertEquals(numberOfRoles, signUpResponse.userRoles().size(), "Only one role.");
        Assertions.assertTrue(signUpResponse.userRoles().contains(UsersControllerFactory.DEFAULT_USER_ROLE),
                "Role is default user role.");
    }

    /**
     * SingUp second user with same dni as userA bad request cause dni is in using.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testSignUpSecondUserWithSameDniBadRequest() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequestJson = UsersControllerFactory.getUserARequestJson();
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // User B with user A DNI
        var userBRequest = new SignUpRequestForm(UsersControllerFactory.USER_A_DNI, UsersControllerFactory.USER_B_NAME,
                UsersControllerFactory.USER_B_FIRST_SURNAME, UsersControllerFactory.USER_B_SECOND_SURNAME,
                UsersControllerFactory.USER_B_BIRTH_DATE, UsersControllerFactory.USER_B_GENDER,
                UsersControllerFactory.USER_B_PASSWORD, UsersControllerFactory.USER_B_EMAIL,
                UsersControllerFactory.USER_B_POSTAL_CODE, UsersControllerFactory.USER_B_APARTMENT_NUMBER,
                UsersControllerFactory.USER_B_BUILDING, UsersControllerFactory.USER_B_STREET,
                UsersControllerFactory.USER_B_CITY, UsersControllerFactory.USER_B_KIND_MEMBER,
                UsersControllerFactory.USER_B_COUNTRY_NUMERIC_CODE,
                UsersControllerFactory.USER_B_COUNTRY_SUBDIVISION_NAME);
        var userBRequestJson = gson.toJson(userBRequest);
        // Second user with same dni as userA bad request.
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userBRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * SingUp second user with same email as userA bad request cause email is in
     * using.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testSignUpSecondUserWithSameEmailBadRequest() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequestJson = UsersControllerFactory.getUserARequestJson();
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Set user B with same email as user A.
        var userBRequest = new SignUpRequestForm(UsersControllerFactory.USER_B_DNI, UsersControllerFactory.USER_B_NAME,
                UsersControllerFactory.USER_B_FIRST_SURNAME, UsersControllerFactory.USER_B_SECOND_SURNAME,
                UsersControllerFactory.USER_B_BIRTH_DATE, UsersControllerFactory.USER_B_GENDER,
                UsersControllerFactory.USER_B_PASSWORD, UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_B_POSTAL_CODE, UsersControllerFactory.USER_B_APARTMENT_NUMBER,
                UsersControllerFactory.USER_B_BUILDING, UsersControllerFactory.USER_B_STREET,
                UsersControllerFactory.USER_B_CITY, UsersControllerFactory.USER_B_KIND_MEMBER,
                UsersControllerFactory.USER_B_COUNTRY_NUMERIC_CODE,
                UsersControllerFactory.USER_B_COUNTRY_SUBDIVISION_NAME);
        var userBRequestJson = gson.toJson(userBRequest);
        // Second user with same email as userA bad request.
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userBRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
