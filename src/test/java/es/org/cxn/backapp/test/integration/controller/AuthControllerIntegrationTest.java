package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.service.JwtUtils;
import es.org.cxn.backapp.test.integration.controller.InvoiceControllerIntegrationTest.LocalDateAdapter;

/**
 * @author Santiago Paz. Authentication controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class AuthControllerIntegrationTest {

    private final static String DEFAULT_USER_ROLE = "USER";
    private final static String SIGN_UP_URL = "/api/auth/signup";
    private final static String SIGN_IN_URL = "/api/auth/signinn";

    private final static String userA_dni = "32721859N";
    private final static String userA_name = "Santiago";
    private final static String userA_firstSurname = "Paz";
    private final static String userA_secondSurname = "Perez";
    public final static LocalDate userA_birthDate = LocalDate.of(1993, 5, 8); // aaaa,mm,dd
    private final static String userA_gender = "Male";
    private final static String userA_password = "fakeValidPassword";
    private final static String userA_email = "santi@santi.es";
    private final static String userA_postalCode = "15570";
    private final static String userA_apartmentNumber = "1D";
    private final static String userA_building = "7";
    private final static String userA_street = "Marina Espanola";
    private final static String userA_city = "Naron";
    private final static Integer userA_countryNumericCode = 724;
    private final static String userA_countrySubdivisionName = "Lugo";

    private final static String userB_dni = "32721860J";
    private final static String userB_name = "Adrian";
    private final static String userB_firstSurname = "Paz";
    private final static String userB_secondSurname = "Perez";
    public final static LocalDate userB_birthDate = LocalDate.of(1996, 2, 8); // aaaa,mm,dd
    private final static String userB_gender = "Male";
    private final static String userB_password = "fakeValidPassword";
    private final static String userB_postalCode = "15570";
    private final static String userB_apartmentNumber = "1D";
    private final static String userB_building = "7";
    private final static String userB_street = "Marina Espanola";
    private final static String userB_city = "Naron";
    private final static Integer userB_countryNumericCode = 724;
    private final static String userB_countrySubdivisionName = "Lugo";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserDetailsService myUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;

    private static String userARequestJson;
    private static String userBRequestJson;
    private static Gson gson;

    @BeforeAll
    static void createUsersData() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        var userARequest = new SignUpRequestForm(
                userA_dni, userA_name, userA_firstSurname, userA_secondSurname,
                userA_birthDate, userA_gender, userA_password, userA_email,
                userA_postalCode, userA_apartmentNumber, userA_building,
                userA_street, userA_city, userA_countryNumericCode,
                userA_countrySubdivisionName
        );
        var userBRequest = new SignUpRequestForm(
                userB_dni, userB_name, userB_firstSurname, userB_secondSurname,
                userB_birthDate, userB_gender, userB_password, userA_email,
                userB_postalCode, userB_apartmentNumber, userB_building,
                userB_street, userB_city, userB_countryNumericCode,
                userB_countrySubdivisionName
        );
        userARequestJson = gson.toJson(userARequest);

        userBRequestJson = gson.toJson(userBRequest);
    }

    /**
     * Main class constructor
     */
    public AuthControllerIntegrationTest() {
        super();
    }

    /**
     * Create two users with same email. Email must be unique and second user
     * sign up raises exception
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void testUsersSignUpWithSameEmailReturnError() throws Exception {

        // First user created
        // Response CREATED 201 with user data with Role
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        // Second user with same email already exists
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userBRequestJson)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    void testSignUpReturnUserDataWithRole() throws Exception {
        Set<PersistentRoleEntity> userRolesSet = new HashSet<>();
        userRolesSet.add(new PersistentRoleEntity(DEFAULT_USER_ROLE));
        var signUpResponse = new SignUpResponseForm(
                userA_dni, userA_name, userA_firstSurname, userA_secondSurname,
                userA_birthDate, userA_gender, userA_email, userRolesSet
        );
        var signUpResponseJson = gson.toJson(signUpResponse);

        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(
                MockMvcResultMatchers.content().json(signUpResponseJson)
        );

    }

    @Test
    @Transactional
    void testSignInReturnJwtTokenValidate() throws Exception {
        var signInRequest = new AuthenticationRequest(
                userA_email, userA_password
        );
        var signInRequestJson = gson.toJson(signInRequest);
        // Register user correctly
        mockMvc.perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(userARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Login return jwt for registered user
        final var responseContent = mockMvc
                .perform(
                        post(SIGN_IN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(signInRequestJson)
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        final var jwtToken = responseContent
                .substring(8, responseContent.length() - 2);
        // Validate jwt token
        Assertions
                .assertTrue(
                        jwtUtils.validateToken(
                                jwtToken,
                                myUserDetailsService
                                        .loadUserByUsername(userA_email)
                        ), "jwt response token is valid"
                );
    }

}
