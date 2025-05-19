
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.requests.UserChangeEmailRequest;
import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;
import es.org.cxn.backapp.model.form.responses.user.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.user.auth.AuthenticationResponse;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.service.impl.DefaultUserService;
import es.org.cxn.backapp.service.impl.storage.DefaultImageStorageService;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc()
@ActiveProfiles("test")
class UserControllerIntegrationIT {
    /**
     * Gson instance used for converting Java objects to JSON and vice versa. This
     * static instance is used for serializing and deserializing request and
     * response payloads in the tests.
     */
    private static Gson gson;

    /**
     * URL endpoint for retrieving user data. This static final string represents
     * the URL used to fetch data of a specific user.
     */
    private static final String GET_USER_DATA_URL = "/api/user";
    /**
     * URL endpoint for user sign-in. This static final string represents the URL
     * used for user authentication and generating JWT tokens.
     */
    private static final String SIGN_IN_URL = "/api/auth/signinn";

    /**
     * URL endpoint for changing a user's email address. This static final string
     * represents the URL used to update a user's email address.
     */
    private static final String CHANGE_MEMBER_EMAIL_URL = "/api/user/changeEmail";

    /**
     * URL endpoint for changing a user's password. This static final string
     * represents the URL used to update a user's password.
     */
    private static final String CHANGE_MEMBER_PASSWORD_URL = "/api/user/changePassword";

    /**
     * URL endpoint for user registration (sign-up). This static final string
     * represents the URL used to create a new user account.
     */
    private static final String SIGN_UP_URL = "/api/auth/signup";

    /**
     * URL endpoint for changing the type of a user. This static final string
     * represents the URL used to update a user's role or membership type.
     */
    private static final String CHANGE_KIND_MEMBER_URL = "/api/user/changeKindOfMember";

    /**
     * Mocked image storage service.
     */
    @MockitoBean
    private DefaultImageStorageService imageStorageService;

    /**
     * The email service mocked implementation.
     */
    @MockitoBean
    private DefaultEmailService defaultEmailService;

    /**
     * Mocked {@link SecurityContext} used in the test to simulate the security
     * context for authentication. It represents the context for a specific user's
     * security information, such as authentication and authorization details.
     */
    @MockitoBean
    private SecurityContext securityContext;

    /**
     * Mocked {@link Authentication} used in the test to simulate the authenticated
     * user information. This object contains details about the currently
     * authenticated user, such as the username, roles, and credentials.
     */
    @MockitoBean
    private Authentication authentication;

    /**
     * Provides the ability to perform HTTP requests and receive responses for
     * testing. Used for sending HTTP requests to the controllers and verifying the
     * responses in integration tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked mail sender.
     */
    @MockitoBean
    private JavaMailSender javaMailSender;

    /**
     * Mime message.
     */
    private MimeMessage mimeMessage = new MimeMessage((Session) null);

    /**
     * Main class constructor.
     */
    UserControllerIntegrationIT() {
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

    /**
     * Test that check that change not existing user kind of member returns a http
     * status 400 bad request.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testChangeKindOfMemberNotExistingMemberBadRequest() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

        var changeKindMemberRequest = new UserChangeKindMemberRequest(UsersControllerFactory.USER_B_EMAIL,
                UserType.SOCIO_HONORARIO);
        var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
        mockMvc.perform(patch(CHANGE_KIND_MEMBER_URL).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changeKindMemberRequestJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn().getResponse()
                .getContentAsString();
    }

    /**
     * Test change kind of existing member from "socio numero" to "socio aspirante"
     * can be done only if age is under 18. TO-DO -NOT FAIL WHEN AGE IS UNDER 18
     * (WRONG)
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    @WithMockUser(username = "userA", roles = { "ADMIN" })
    void testChangeKindOfMemberSocioNumeroSocioAspirante() throws Exception {
        // Age under 18.
        final var userAgeUnder18 = LocalDate.of(2010, 2, 2);
        var userARequest = new SignUpRequestForm(UsersControllerFactory.USER_A_DNI, UsersControllerFactory.USER_A_NAME,
                UsersControllerFactory.USER_A_FIRST_SURNAME, UsersControllerFactory.USER_A_SECOND_SURNAME,
                userAgeUnder18, UsersControllerFactory.USER_A_GENDER, UsersControllerFactory.USER_A_PASSWORD,
                UsersControllerFactory.USER_A_EMAIL, UsersControllerFactory.USER_A_POSTAL_CODE,
                UsersControllerFactory.USER_A_APARTMENT_NUMBER, UsersControllerFactory.USER_A_BUILDING,
                UsersControllerFactory.USER_A_STREET, UsersControllerFactory.USER_A_CITY,
                UsersControllerFactory.USER_A_KIND_MEMBER, UsersControllerFactory.USER_A_COUNTRY_NUMERIC_CODE,
                UsersControllerFactory.USER_A_COUNTRY_SUBDIVISION_NAME);
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequestJson = gson.toJson(userARequest);
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

        var changeKindMemberRequest = new UserChangeKindMemberRequest(UsersControllerFactory.USER_A_EMAIL,
                UserType.SOCIO_ASPIRANTE);
        var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
        // Update kind of member.
        var changeKindMemberResponseJson = mockMvc
                .perform(patch(CHANGE_KIND_MEMBER_URL).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changeKindMemberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        var changeKindMemberResponse = gson.fromJson(changeKindMemberResponseJson, UserDataResponse.class);
        Assertions.assertEquals(UserType.SOCIO_ASPIRANTE, changeKindMemberResponse.kindMember(),
                "kind of member has been changed.");
        Assertions.assertEquals(userARequest.email(), changeKindMemberResponse.email(), "email is the user email.");
    }

    /**
     * TO-DO.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    @WithMockUser(username = "userA", roles = { "ADMIN" })
    void testChangeKindOfMemberSocioNumeroSocioAspiranteNotAllowedBirthDate() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        // User age NOT under 18.
        var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var userARequestJson = gson.toJson(userARequest);
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

        var changeKindMemberRequest = new UserChangeKindMemberRequest(UsersControllerFactory.USER_A_EMAIL,
                UserType.SOCIO_ASPIRANTE);
        var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
        // Update kind of member.
        mockMvc.perform(patch(CHANGE_KIND_MEMBER_URL).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changeKindMemberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse()
                .getContentAsString();
    }

    /**
     * TO-DO.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    @WithMockUser(username = "userA", roles = { "ADMIN" })
    void testChangeKindOfMemberSocioNumeroSocioFamiliar() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var userARequestJson = gson.toJson(userARequest);
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        var changeKindMemberRequest = new UserChangeKindMemberRequest(UsersControllerFactory.USER_A_EMAIL,
                UserType.SOCIO_FAMILIAR);
        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);
        var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
        // Update kind of member.
        var changeKindMemberResponseJson = mockMvc
                .perform(patch(CHANGE_KIND_MEMBER_URL).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changeKindMemberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        var changeKindMemberResponse = gson.fromJson(changeKindMemberResponseJson, UserDataResponse.class);
        Assertions.assertEquals(UserType.SOCIO_FAMILIAR, changeKindMemberResponse.kindMember(),
                "kind of member has been changed.");
        Assertions.assertEquals(userARequest.email(), changeKindMemberResponse.email(), "email is the user email.");
    }

    /**
     * Change member type from.
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    @WithMockUser(username = "userA", roles = { "ADMIN" })
    void testChangeKindOfMemberSocioNumeroSocioHonorario() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

        var changeKindMemberRequest = new UserChangeKindMemberRequest(UsersControllerFactory.USER_A_EMAIL,
                UserType.SOCIO_HONORARIO);
        var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
        // Update kind of member.
        var changeKindMemberResponseJson = mockMvc
                .perform(patch(CHANGE_KIND_MEMBER_URL).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changeKindMemberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        var changeKindMemberResponse = gson.fromJson(changeKindMemberResponseJson, UserDataResponse.class);
        Assertions.assertEquals(UserType.SOCIO_HONORARIO, changeKindMemberResponse.kindMember(),
                "kind of member has been changed.");
        Assertions.assertEquals(userARequest.email(), changeKindMemberResponse.email(), "email is the user email.");
    }

    /**
     * Check that change email for existing member returns member data with new
     * email.
     *
     * @throws Exception Test fails.
     */
    @Test
    @Transactional
    void testChangeMemberEmail() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var memberEmail = UsersControllerFactory.USER_A_EMAIL;
        var memberPassword = UsersControllerFactory.USER_A_PASSWORD;
        var newEmail = "newemail@email.es";

        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var jwtToken = authenticateAndGetToken(memberEmail, memberPassword);

        var changeEmailRequest = new UserChangeEmailRequest(memberEmail, newEmail);
        var changeEmailRequestJson = gson.toJson(changeEmailRequest);

        var changeMemberEmailResponseJson = mockMvc
                .perform(patch(CHANGE_MEMBER_EMAIL_URL).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changeEmailRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        var response = gson.fromJson(changeMemberEmailResponseJson, UserDataResponse.class);

        Assertions.assertNotEquals(memberEmail, response.email(), "Response not cointains in itial member email.");
        Assertions.assertEquals(newEmail, response.email(), "Response contains new member email.");
    }

    /**
     * Check that try changing email of not existing member return 400 bad request.
     *
     * @throws Exception Test fails.
     */
    @Test
    @Transactional
    void testChangeMemberEmailNotExistingMemberBadRequest() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final var notExistingMemberEmail = "email@email.es";
        var newEmail = "newEmail@email.es";

        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

        var changeEmailRequest = new UserChangeEmailRequest(notExistingMemberEmail, newEmail);
        var changeEmailRequestJson = gson.toJson(changeEmailRequest);

        mockMvc.perform(patch(CHANGE_MEMBER_EMAIL_URL).contentType(MediaType.APPLICATION_JSON)
                .content(changeEmailRequestJson).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    /**
     * Check that change password of user not valid password.
     *
     * @throws Exception Test fails.
     */
    @Test
    @Transactional
    void testChangeMemberPasswordCurrentPasswordDontMatch() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var memberRequestPasswordNotValid = "456456";
        var memberNewPassword = "321321";

        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

        var changePasswordRequest = new UserChangePasswordRequest(memberRequestPasswordNotValid, memberNewPassword);

        var changePasswordRequestJson = gson.toJson(changePasswordRequest);

        var changeMemberPasswordResponseJson = mockMvc
                .perform(patch(CHANGE_MEMBER_PASSWORD_URL).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changePasswordRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse()
                .getContentAsString();
        Assertions.assertTrue(changeMemberPasswordResponseJson.contains(DefaultUserService.USER_PASSWORD_NOT_MATCH),
                "Message content user password dont match.");
    }

    /**
     * Check that try changing password of not existing member return 400 bad
     * request.
     *
     * @throws Exception Test fails.
     */
    @Test
    @Transactional
    void testChangeMemberPasswordNotExistingMemberBadRequest() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        final var currentPassword = "123123";
        final var newPassword = "321321";
        var changePasswordRequest = new UserChangePasswordRequest(currentPassword, newPassword);

        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        var jwtToken = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);

        var changePasswordRequestJson = gson.toJson(changePasswordRequest);
        mockMvc.perform(patch(CHANGE_MEMBER_PASSWORD_URL).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).content(changePasswordRequestJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    /**
     * Check that try changing password of not existing member return 400 bad
     * request.
     *
     * @throws Exception Test fails.
     */
    @Test
    @Transactional
    void testChangeMemberPasswordPasswordChanged() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var memberEmail = UsersControllerFactory.USER_A_EMAIL;
        var currentPassword = UsersControllerFactory.USER_A_PASSWORD;
        var newPassword = "321321";

        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var jwtToken = authenticateAndGetToken(memberEmail, currentPassword);

        var changePasswordRequest = new UserChangePasswordRequest(currentPassword, newPassword);

        var changePasswordRequestJson = gson.toJson(changePasswordRequest);
        mockMvc.perform(patch(CHANGE_MEMBER_PASSWORD_URL).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken).content(changePasswordRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Try login with the new password and old password,
        // only works new password.
        var authenticationRequest = new AuthenticationRequest(memberEmail, currentPassword);
        var authenticationRequestJson = gson.toJson(authenticationRequest);

        mockMvc.perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authenticationRequestJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        authenticationRequest = new AuthenticationRequest(memberEmail, newPassword);
        authenticationRequestJson = gson.toJson(authenticationRequest);

        mockMvc.perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authenticationRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "userA", roles = { "ADMIN" })
    void testDeleteUserCreateUserWIthSameDataIsAllowed() throws Exception {
        // Age under 18.
        var userARequest = new SignUpRequestForm(UsersControllerFactory.USER_A_DNI, UsersControllerFactory.USER_A_NAME,
                UsersControllerFactory.USER_A_FIRST_SURNAME, UsersControllerFactory.USER_A_SECOND_SURNAME,
                UsersControllerFactory.USER_A_BIRTH_DATE, UsersControllerFactory.USER_A_GENDER,
                UsersControllerFactory.USER_A_PASSWORD, UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_POSTAL_CODE, UsersControllerFactory.USER_A_APARTMENT_NUMBER,
                UsersControllerFactory.USER_A_BUILDING, UsersControllerFactory.USER_A_STREET,
                UsersControllerFactory.USER_A_CITY, UsersControllerFactory.USER_A_KIND_MEMBER,
                UsersControllerFactory.USER_A_COUNTRY_NUMERIC_CODE,
                UsersControllerFactory.USER_A_COUNTRY_SUBDIVISION_NAME);
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var userARequestJson = gson.toJson(userARequest);
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(delete("/api/user" + "/" + UsersControllerFactory.USER_A_EMAIL))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(
                        "User with email " + UsersControllerFactory.USER_A_EMAIL + " has been permanently deleted."));

        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    /**
     *
     * @throws Exception When fails.
     */
    @Test
    @Transactional
    void testGetOneUsersData() throws Exception {
        // Configurar el comportamiento del mock JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        // FIRST MEMBER
        var userRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var userRequestJson = gson.toJson(userRequest);
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        // get authorization jwt token
        var authReq = new AuthenticationRequest(UsersControllerFactory.USER_A_EMAIL,
                UsersControllerFactory.USER_A_PASSWORD);
        var authReqJson = gson.toJson(authReq);
        var authResponseJson = mockMvc
                .perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authReqJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        var authResponse = gson.fromJson(authResponseJson, AuthenticationResponse.class);

        var accessToken = authResponse.jwt();

        mockMvc.perform(get(GET_USER_DATA_URL).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,
                "Bearer " + accessToken)).andReturn().getResponse().getContentAsString();
    }

    @Transactional
    @Test
    void testUploadProfileImageNoExistingUserBadRequest() throws Exception {
        final String uploadImageUrl = "/api/user/uploadProfileImage"; // Renamed to camelCase
        final String nonExistingUserName = "nonExistingUser"; // User that doesn't exist in the DB
        final String newProfileImageUrl = "http://example.com/profile.jpg";

        // Create the requestBody map as JSON
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("profileImageUrl", newProfileImageUrl);

        // Convert the map to JSON string using Jackson ObjectMapper
        String jsonRequestBody = new ObjectMapper().writeValueAsString(requestBody);

        // Perform the PATCH request simulating the non-existing user in the security
        // context
        mockMvc.perform(patch(uploadImageUrl).contentType(MediaType.APPLICATION_JSON).content(jsonRequestBody)
                .with(SecurityMockMvcRequestPostProcessors.user(nonExistingUserName)) // Simulate non-existing user
        ).andExpect(status().isBadRequest());
    }

}
