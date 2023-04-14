package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonParser;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.service.JwtUtils;

/**
 * @author Santiago Paz. Authentication controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class RoleControllerIntegrationTest {

    private final static String SIGN_UP_URL = "/api/auth/signup";
    private final static String SIGN_IN_URL = "/api/auth/signinn";

    private final static String USER_A_VALID_DATA_SIGN_UP = "{ \"dni\": \"32721859N\","
            + " \"name\": \"Santiago\", " + " \"firstSurname\": \"Paz\", "
            + " \"secondSurname\": \"Perez\", "
            + " \"birthDate\": \"1993-05-08\", " + " \"gender\": \"male\", "
            + " \"password\": \"123123\"," + " \"email\": Santi@santi.es }";

    private final static String USER_A_VALID_DATA_SIGN_UP_RESPONSE = "{ \"name\": \"Santiago\","
            + " \"firstSurname\": \"Paz\", " + " \"secondSurname\": \"Perez\", "
            + " \"birthDate\": \"1993-05-08\", " + " \"gender\": \"male\", "
            + " \"email\": \"Santi@santi.es\"," + " \"userRoles\": [USER] }";

    private final static String USER_A_VALID_CREDENTIALS = "{ \"email\": \"Santi@santi.es\","
            + " \"password\": 123123 }";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserDetailsService myUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;

    @BeforeEach
    void setup() {

    }

    /**
     * Main class constructor
     */
    public RoleControllerIntegrationTest() {
        super();
    }

    @Test
    @Transactional
    void testCreateUsersWithSameEmailReturnError() throws Exception {
        final var jSonUserDataRequest = JsonParser
                .parseString(USER_A_VALID_DATA_SIGN_UP).getAsJsonObject()
                .toString();
        // First user created
        // Response CREATED 201 with user data with Role
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(jSonUserDataRequest)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        // Second user with same email already exists CONFLICT 409
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(jSonUserDataRequest)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    void testCreateUserWithDataReturnUserDataWithRole() throws Exception {
        final var jSonUserDataRequest = JsonParser
                .parseString(USER_A_VALID_DATA_SIGN_UP).getAsJsonObject()
                .toString();
        final var userDataResponse = JsonParser
                .parseString(USER_A_VALID_DATA_SIGN_UP_RESPONSE)
                .getAsJsonObject().toString();

        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(jSonUserDataRequest)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(
                MockMvcResultMatchers.content().json(userDataResponse)
        );

    }

    @Test
    @Transactional
    void testRequestFormValidationNovalidName() throws Exception {
        final var noValidLongName = "FakeNameTooLongForUseInTest"; // Max 25
        var userAData = JsonParser.parseString(USER_A_VALID_DATA_SIGN_UP)
                .getAsJsonObject();
        userAData.remove("name");
        userAData.addProperty("name", noValidLongName);
        // No valid name, name length max is 25.
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userAData.toString())
        ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(
                MockMvcResultMatchers.content().string(
                        "{\"content\":[\"" + Constants.NAME_MAX_LENGTH_MESSAGE
                                + "\"]," + "\"status\":\"warning\"}"
                )
        );

        // Remove name key and add name key with empty string
        userAData.remove("name");
        userAData.addProperty("name", "");
        // Empty values are no valid.
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userAData.toString())
        ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(
                MockMvcResultMatchers.content().string(
                        "{\"content\":[\"" + Constants.NAME_NOT_BLANK_MESSAGE
                                + "\"]," + "\"status\":\"warning\"}"
                )
        );
    }

    @Test
    @Transactional
    void testSignInValidateJwtToken() throws Exception {
        final var userCredentials = JsonParser
                .parseString(USER_A_VALID_CREDENTIALS).getAsJsonObject()
                .toString();

        final var userData = JsonParser.parseString(USER_A_VALID_DATA_SIGN_UP)
                .getAsJsonObject().toString();

        // User not registered, unauthorized
        mockMvc.perform(
                post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userCredentials)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
        // Register user correctly
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userData)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Login return jwt for registered user
        final var responseContent = mockMvc
                .perform(
                        post(SIGN_IN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userCredentials)
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        final var jwtToken = responseContent
                .substring(8, responseContent.length() - 2);
        // Validate jwt token
        Assertions.assertTrue(
                jwtUtils.validateToken(
                        jwtToken,
                        myUserDetailsService
                                .loadUserByUsername("Santi@santi.es")
                ), "jwt response token is valid"
        );
    }

}
