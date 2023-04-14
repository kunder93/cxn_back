package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

import es.org.cxn.backapp.service.JwtUtils;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class UserControllerIntegrationTest {

    private final static String SIGN_UP_URL = "/api/auth/signup";
    private final static String SIGN_IN_URL = "/api/auth/signinn";
    private final static String GET_USER_DATA_URL = "/api/user";

    private final static String USER_A_VALID_DATA_SIGN_UP = "{ \"dni\": \"32721859N\","
            + " \"name\": \"Santiago\", " + " \"firstSurname\": \"Paz\", "
            + " \"secondSurname\": \"Perez\", "
            + " \"birthDate\": \"1993-05-08\", " + " \"gender\": \"male\", "
            + " \"password\": \"123123\"," + " \"email\": Santi@santi.es }";

    private final static String USER_B_VALID_DATA_SIGN_UP = "{ \"dni\": \"32721840N\","
            + " \"name\": \"Adrian\", " + " \"firstSurname\": \"Paz\", "
            + " \"secondSurname\": \"Perez\", "
            + " \"birthDate\": \"1996-02-08\", " + " \"gender\": \"male\", "
            + " \"password\": \"abc123\"," + " \"email\": Adri@adri.com }";

    private final static String USER_A_VALID_DATA_SIGN_UP_RESPONSE = "{ \"dni\": \"32721859N\","
            + " \"name\": \"Santiago\", " + " \"firstSurname\": \"Paz\", "
            + " \"secondSurname\": \"Perez\", "
            + " \"birthDate\": \"1993-05-08\", " + " \"gender\": \"male\", "
            + " \"email\": \"Santi@santi.es\"," + " \"userRoles\": [USER] }";

    private final static String USER_B_VALID_DATA_SIGN_UP_RESPONSE = "{ \"dni\": \"32721840N\","
            + " \"name\": \"Adrian\", " + " \"firstSurname\": \"Paz\", "
            + " \"secondSurname\": \"Perez\", "
            + " \"birthDate\": \"1996-02-08\", " + " \"gender\": \"male\", "
            + " \"email\": \"Adri@adri.com\"," + " \"userRoles\": [USER] }";

    private final static String USER_A_NEW_DATA = "{ \"name\": \"NewAdrian\","
            + " \"firstSurname\": \"NewPaz\", "
            + " \"secondSurname\": \"NewPerez\", "
            + " \"birthDate\": \"1996-05-18\", " + " \"gender\": female }";

    private final static String USER_A_UPDATED_DATA = "{ \"name\": \"NewAdrian\","
            + " \"firstSurname\": \"NewPaz\", "
            + " \"secondSurname\": \"NewPerez\", "
            + " \"birthDate\": \"1996-05-18\", " + " \"gender\": female }";

    private final static String USER_A_VALID_CREDENTIALS = "{ \"email\": \"Santi@santi.es\","
            + " \"password\": 123123 }";
    private final static String USER_B_VALID_CREDENTIALS = "{ \"email\": \"Adri@adri.com\","
            + " \"password\": abc123 }";

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
    public UserControllerIntegrationTest() {
        super();
    }

    @Test
    @Transactional
    void testSignInGetUserData() throws Exception {
        final var userCredentials = JsonParser
                .parseString(USER_A_VALID_CREDENTIALS).getAsJsonObject()
                .toString();

        final var userData = JsonParser.parseString(USER_A_VALID_DATA_SIGN_UP)
                .getAsJsonObject().toString();

        // Register user
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userData)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Login return jwt token for registered user
        final var responseContent = mockMvc
                .perform(
                        post(SIGN_IN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userCredentials)
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        final var jwtToken = responseContent
                .substring(8, responseContent.length() - 2);
        // Call api using jwt token, get user data
        var userDataResponse = JsonParser
                .parseString(USER_A_VALID_DATA_SIGN_UP_RESPONSE)
                .getAsJsonObject();
        mockMvc.perform(
                get(GET_USER_DATA_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(userCredentials)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(userDataResponse.toString())
                );
    }

    @Test
    @Transactional
    void testGetUserDataWithoutSignInUnauthorized() throws Exception {

        // Call without jwt token, get user data, expect unauthorized
        mockMvc.perform(
                get(GET_USER_DATA_URL).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    void testTwoUsersGetDataUsingJwt() throws Exception {
        final var userACredentials = JsonParser
                .parseString(USER_A_VALID_CREDENTIALS).getAsJsonObject()
                .toString();

        final var userBCredentials = JsonParser
                .parseString(USER_B_VALID_CREDENTIALS).getAsJsonObject()
                .toString();

        final var userAData = JsonParser.parseString(USER_A_VALID_DATA_SIGN_UP)
                .getAsJsonObject().toString();

        final var userBData = JsonParser.parseString(USER_B_VALID_DATA_SIGN_UP)
                .getAsJsonObject().toString();

        // Register user A
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userAData)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Register user B
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userBData)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Login return jwt token for registered user A
        final var responseContentA = mockMvc
                .perform(
                        post(SIGN_IN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userACredentials)
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        final var jwtAToken = responseContentA
                .substring(8, responseContentA.length() - 2);

        // Login return jwt token for registered user B
        final var responseContentB = mockMvc
                .perform(
                        post(SIGN_IN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userBCredentials)
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        final var jwtBToken = responseContentB
                .substring(8, responseContentB.length() - 2);

        // Call api using jwt token, get user B data
        final var userBDataResponse = JsonParser
                .parseString(USER_B_VALID_DATA_SIGN_UP_RESPONSE)
                .getAsJsonObject().toString();
        mockMvc.perform(
                get(GET_USER_DATA_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtBToken)
                        .content(userBCredentials)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(
                MockMvcResultMatchers.content().json(userBDataResponse)
        );

        // Call api using jwt token, get user A data
        final var userADataResponse = JsonParser
                .parseString(USER_A_VALID_DATA_SIGN_UP_RESPONSE)
                .getAsJsonObject().toString();
        mockMvc.perform(
                get(GET_USER_DATA_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtAToken)
                        .content(userACredentials)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(
                MockMvcResultMatchers.content().json(userADataResponse)
        );
    }

    @Test
    @Transactional
    void testUpdateUserGetData() throws Exception {
        final var userAData = JsonParser.parseString(USER_A_VALID_DATA_SIGN_UP)
                .getAsJsonObject().toString();

        // Register user A
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userAData)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        final var userACredentials = JsonParser
                .parseString(USER_A_VALID_CREDENTIALS).getAsJsonObject()
                .toString();
        // Login return jwt token for registered user A
        final var responseContentA = mockMvc
                .perform(
                        post(SIGN_IN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userACredentials)
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        final var jwtAToken = responseContentA
                .substring(8, responseContentA.length() - 2);

        final var userANewData = JsonParser.parseString(USER_A_NEW_DATA)
                .getAsJsonObject().toString();
        final var userAUpdatedDataResponse = JsonParser
                .parseString(USER_A_UPDATED_DATA).getAsJsonObject().toString();
        // Call for change user data with jwtToken
        mockMvc.perform(
                post(GET_USER_DATA_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtAToken)
                        .content(userANewData)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(
                MockMvcResultMatchers.content().json(userAUpdatedDataResponse)
        );
    }

}
