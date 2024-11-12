package es.org.cxn.backapp.test.integration.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import es.org.cxn.backapp.controller.entity.FederateController;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.service.FederateStateService;
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
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class FederateControllerIntegrationTest {

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

    @BeforeAll
    static void setup() {
        gson = UsersControllerFactory.GSON;
    }

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
     * The FederateController mock injected into the test. It contains the methods
     * that will be tested in the integration test.
     */
    @InjectMocks
    private FederateController federateController;

    /**
     * The JavaMailSender mock used to mock email-related functionality in the
     * integration tests.
     */
    @MockBean
    private JavaMailSender javaMailSender;

    /**
     * The JWT token generated for a user that will be used in testing the sign-in
     * functionality in subsequent requests.
     */
    private String userAJwtToken;

    FederateControllerIntegrationTest() {
        super();
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

    @Test
    @Transactional
    void testChangeAutoRenewNoFederatedMemberBadRequest() throws Exception {
        mockMvc.perform(patch("/api/user/federate/changeAutoRenew").header("Authorization", "Bearer " + userAJwtToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content")
                        .value("400 BAD_REQUEST \"Federate state is not : FEDERATE for user with dni: 32721860J\""))
                .andExpect(jsonPath("$.status").value("failure"));
    }

    @Test
    @Transactional
    void testFederateMember() throws Exception {
        // Mock MultipartFile for front and back DNI images
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
                "front image content".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "backDni.jpg", "image/jpeg",
                "back image content".getBytes());

        // Perform federate member request
        mockMvc.perform(multipart("/api/user/federate").file(frontDni).file(backDni).param("autoRenewal", "true")
                .header("Authorization", "Bearer " + userAJwtToken)).andExpect(status().isOk()); // structure
    }

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

    @Test
    @Transactional
    void testGetFederateStateFederateMemberGetChangedFederateState() throws Exception {
        final String noFederateDniLastUpdateData = "1900-02-02";
        // GET INITIAL FEDERATE STATE
        mockMvc.perform(get("/api/user/federate").header("Authorization", "Bearer " + userAJwtToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.state").value("NO_FEDERATE"))
                .andExpect(jsonPath("$.autoRenew").value(false))
                .andExpect(jsonPath("$.dniLastUpdate").value(noFederateDniLastUpdateData));

        // MEMBER DO FEDERATE REQUEST
        MockMultipartFile frontDni = new MockMultipartFile("frontDni", "frontDni.jpg", "image/jpeg",
                "front image content".getBytes());
        MockMultipartFile backDni = new MockMultipartFile("backDni", "backDni.jpg", "image/jpeg",
                "back image content".getBytes());

        // Perform federate member request
        mockMvc.perform(multipart("/api/user/federate").file(frontDni).file(backDni).param("autoRenewal", "true")
                .header("Authorization", "Bearer " + userAJwtToken)).andExpect(status().isOk()); // structure

        // GET CHANGED FEDERATE STATE
        mockMvc.perform(get("/api/user/federate").header("Authorization", "Bearer " + userAJwtToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.state").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.autoRenew").value(true))
                .andExpect(jsonPath("$.dniLastUpdate").value(LocalDate.now().toString()));

    }

}
