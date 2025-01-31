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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.UserListDataResponse;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:IntegrationController.properties")
class DeletePermanentlyUserIT {

    /**
     * URL endpoint for user registration (sign-up). This static final string
     * represents the URL used to create a new user account.
     */
    private static final String SIGN_UP_URL = "/api/auth/signup";
    /**
     * URL endpoint for user sign-in. This static final string represents the URL
     * used for user authentication and generating JWT tokens.
     */
    private static final String SIGN_IN_URL = "/api/auth/signinn";

    /**
     * URL endpoint for delete users with delete method.
     */
    private static final String ENDPOINT_USER_URL = "/api/user";

    /**
     * Gson instance for serializing and deserializing JSON objects during tests.
     *
     * <p>
     * This is particularly useful for converting Java objects to their JSON
     * representation when sending HTTP requests, and for parsing JSON responses
     * received from the controllers back into Java objects.
     * </p>
     *
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
    private JavaMailSender javaMailSender;

    /**
     * Mime message.
     */
    private MimeMessage mimeMessage = new MimeMessage((Session) null);

    /**
     * Used to simulate HTTP requests and perform assertions on the results within
     * the test cases.
     *
     * <p>
     * It allows the tests to be run in a way that simulates sending requests to the
     * application's controllers without needing to start a full HTTP server.
     * </p>
     */
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void initializeTest() {
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

    @Test
    @Transactional
    void testDeleteCreatedUser() throws Exception {
        var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberRequestJson = gson.toJson(memberRequest);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        final var adminEmail = "santi@santi.es";
        final var adminPswrd = "123123";

        // user with credentials is created (email, password) and other data. Nothing
        // more associated to this user.

        // Perform delete from initial admin user.

        final var adminUserJwtToken = authenticateAndGetToken(adminEmail, adminPswrd);

        final var beforeDeleteUsers = mockMvc
                .perform(get(ENDPOINT_USER_URL + "/getAll")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminUserJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        final UserListDataResponse bedoreDeleteUsersResponseList = gson.fromJson(beforeDeleteUsers,
                UserListDataResponse.class);
        final var beforeDeleteUserListDto = bedoreDeleteUsersResponseList.usersList();
        Assertions.assertEquals(2, beforeDeleteUserListDto.size(), "List have two users.");
        // Remain user is admin user.
        Assertions.assertNotEquals(beforeDeleteUserListDto.getFirst(), beforeDeleteUserListDto.getLast(),
                "list first user is not the same las user.");

        // admin perform delete of created user.
        mockMvc.perform(delete(ENDPOINT_USER_URL + "/" + memberRequest.email())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminUserJwtToken)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

        // Check that user dont exist.
        // Get all users data and check.

        final var response = mockMvc
                .perform(get(ENDPOINT_USER_URL + "/getAll")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminUserJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        final UserListDataResponse responseAsList = gson.fromJson(response, UserListDataResponse.class);
        final var userListDto = responseAsList.usersList();
        Assertions.assertEquals(1, userListDto.size(), "List have only one user.");
        // Remain user is admin user.
        Assertions.assertEquals(userListDto.getFirst(), userListDto.getLast(), "list first user is same as last user.");
        Assertions.assertEquals(adminEmail, userListDto.getFirst().email(),
                "First list user is admin user. No more users in list.");
    }

    @Test
    @Transactional
    void testDeletenotExistentUserBadRequest() throws Exception {
        final var adminEmail = "santi@santi.es";
        final var adminPswrd = "123123";

        var notExistingMemberRequest = UsersControllerFactory.getSignUpRequestFormUserA();

        // Perform delete from initial admin user.

        final var adminUserJwtToken = authenticateAndGetToken(adminEmail, adminPswrd);

        // admin perform delete of created user.
        mockMvc.perform(delete(ENDPOINT_USER_URL + "/" + notExistingMemberRequest.email())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminUserJwtToken)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
