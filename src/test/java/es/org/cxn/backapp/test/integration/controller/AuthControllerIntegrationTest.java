
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.JwtUtils;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the Authentication Controller.
 *
 * <p>This class tests the various endpoints related to user authentication,
 * including user registration (sign-up) and authentication (sign-in).
 * The tests cover scenarios such as successful user registration,
 * handling duplicate user information, and generating valid JWT tokens upon
 * successful authentication.</p>
 *
 * <p>The tests are conducted in a transactional context to ensure data
 * consistency and to isolate each test case.</p>
 *
 * <p><strong>Note:</strong> This class requires a running Spring context
 * and uses {@link MockMvc} to perform HTTP requests and validate responses.</p>
 *
 * <p>Author: Santiago Paz</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class AuthControllerIntegrationTest {

  /**
   * URL endpoint for user sign-up.
   */
  private static final String SIGN_UP_URL = "/api/auth/signup";

  /**
   * URL endpoint for user sign-in.
   */
  private static final String SIGN_IN_URL = "/api/auth/signinn";

  /**
   * MockMvc is used to simulate HTTP requests in the tests.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Gson instance for serializing/deserializing JSON objects during the tests.
   */
  private static Gson gson;

  /**
   * Sets up the test environment by initializing the Gson instance
   * with a custom adapter for handling {@link LocalDate} objects.
   *
   * <p>This method is annotated with {@link BeforeAll}, which means it will
   * be executed once before any test methods in this class are run.</p>
   */
  @BeforeAll
  static void createUsersData() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
  }

  /**
   * Main class constructor.
   */
  AuthControllerIntegrationTest() {
    super();
  }

  /**
   * SingUp new user return user info with default role "ROLE_SOCIO".
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testSignUpReturnUserDataWithDefautlRole() throws Exception {
    var numberOfRoles = 1;
    var userARequestJson = UsersControllerFactory.getUserARequestJson();
    // Register user correctly
    var controllerResponse = mockMvc
          .perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userARequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var signUpResponse =
          gson.fromJson(controllerResponse, SignUpResponseForm.class);

    Assertions.assertEquals(
          UsersControllerFactory.USER_A_DNI, signUpResponse.getDni(),
          "Dni field."
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_EMAIL, signUpResponse.getEmail(),
          "Email field."
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_NAME, signUpResponse.getName(),
          "Dni field."
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_FIRST_SURNAME,
          signUpResponse.getFirstSurname(), "First surname field."
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_SECOND_SURNAME,
          signUpResponse.getSecondSurname(), "Second surname field."
    );

    Assertions.assertEquals(
          UsersControllerFactory.USER_A_GENDER, signUpResponse.getGender(),
          "Gender field."
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_BIRTH_DATE,
          signUpResponse.getBirthDate(), "Birth date field."
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_KIND_MEMBER,
          signUpResponse.getKindMember(), "kind of member field."
    );
    Assertions.assertEquals(
          numberOfRoles, signUpResponse.getUserRoles().size(), "Only one role."
    );
    Assertions.assertTrue(
          signUpResponse.getUserRoles()
                .contains(UsersControllerFactory.DEFAULT_USER_ROLE),
          "Role is default user role."
    );
  }

  /**
   * SingUp second user with same email as userA bad request cause email
   * is in using.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testSignUpSecondUserWithSameEmailBadRequest() throws Exception {

    var userARequestJson = UsersControllerFactory.getUserARequestJson();
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Set user B with same email as user A.
    var userBRequest = UsersControllerFactory.getSignUpRequestFormUserB();
    userBRequest.setEmail(UsersControllerFactory.USER_A_EMAIL);
    var userBRequestJson = gson.toJson(userBRequest);
    // Second user with same email as userA bad request.
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userBRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * SingUp second user with same dni as userA bad request cause dni is
   * in using.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testSignUpSecondUserWithSameDniBadRequest() throws Exception {

    var userARequestJson = UsersControllerFactory.getUserARequestJson();
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var userBRequest = UsersControllerFactory.getSignUpRequestFormUserB();
    userBRequest.setDni(UsersControllerFactory.USER_A_DNI);
    var userBRequestJson = gson.toJson(userBRequest);
    // Second user with same dni as userA bad request.
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userBRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * SignIn return valid jwt.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testAuthenticateUserReturnJwt() throws Exception {
    var userARequestJson = UsersControllerFactory.getUserARequestJson();
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var authenticationRequest = new AuthenticationRequest(
          UsersControllerFactory.USER_A_EMAIL,
          UsersControllerFactory.USER_A_PASSWORD
    );
    var authenticationRequestJson = gson.toJson(authenticationRequest);

    // Second user with same email as userA bad request.
    var authenticationResponseJson = mockMvc
          .perform(
                post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(authenticationRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    var ar = gson
          .fromJson(authenticationResponseJson, AuthenticationResponse.class);
    var jwtToken = ar.getJwt();
    JwtUtils jwtUtil = new DefaultJwtUtils();
    var jwtUsername = jwtUtil.extractUsername(jwtToken);
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_EMAIL, jwtUsername,
          "Jwt username is same as user signUp"
    );
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
    var authenticationRequest = new AuthenticationRequest(
          UsersControllerFactory.USER_A_EMAIL,
          UsersControllerFactory.USER_A_PASSWORD
    );
    var authenticationRequestJson = gson.toJson(authenticationRequest);

    // Second user with same email as userA bad request.
    mockMvc.perform(
          post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(authenticationRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  /**
   * SignIn with bad password unauthorized.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testAuthenticateUserBadPasswordUnauthorized() throws Exception {

    var userARequestJson = UsersControllerFactory.getUserARequestJson();
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var notValidPassword = "NotValidPassword";
    var authenticationRequest = new AuthenticationRequest(
          UsersControllerFactory.USER_A_EMAIL, notValidPassword
    );
    var authenticationRequestJson = gson.toJson(authenticationRequest);

    // Second user with same email as userA bad request.
    mockMvc.perform(
          post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(authenticationRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

}
