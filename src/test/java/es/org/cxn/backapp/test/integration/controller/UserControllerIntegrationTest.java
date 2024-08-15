
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.UserChangeEmailRequest;
import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;
import es.org.cxn.backapp.model.form.requests.UserUnsubscribeRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.UserListDataResponse;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.DefaultUserService;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class UserControllerIntegrationTest {

  /**
   * Provides the ability to perform HTTP requests and receive responses for
   * testing.
   * Used for sending HTTP requests to the controllers and verifying the
   * responses in integration tests.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Gson instance used for converting Java objects to JSON and vice versa.
   * This static instance is used for serializing and deserializing request and
   *  response payloads in the tests.
   */
  private static Gson gson;

  /**
   * URL endpoint for retrieving user data.
   * This static final string represents the URL used to fetch data of a
   * specific user.
   */
  private static final String GET_USER_DATA_URL = "/api/user";

  /**
   * URL endpoint for retrieving data of all users.
   * This static final string represents the URL used to fetch a list of
   * all users.
   */
  private static final String GET_ALL_USERS_DATA_URL = "/api/user/getAll";

  /**
   * URL endpoint for user sign-in.
   * This static final string represents the URL used for user authentication
   * and generating JWT tokens.
   */
  private static final String SIGN_IN_URL = "/api/auth/signinn";

  /**
   * URL endpoint for changing a user's email address.
   * This static final string represents the URL used to update a user's
   *  email address.
   */
  private static final String CHANGE_MEMBER_EMAIL_URL = "/api/user/changeEmail";

  /**
   * URL endpoint for changing a user's password.
   * This static final string represents the URL used to update a
   * user's password.
   */
  private static final String CHANGE_MEMBER_PASSWORD_URL =
        "/api/user/changePassword";

  /**
   * URL endpoint for user registration (sign-up).
   * This static final string represents the URL used to create a new
   * user account.
   */
  private static final String SIGN_UP_URL = "/api/auth/signup";

  /**
   * URL endpoint for changing the type of a user.
   * This static final string represents the URL used to update a
   * user's role or membership type.
   */
  private static final String CHANGE_KIND_MEMBER_URL =
        "/api/user/changeKindOfMember";

  @BeforeAll
  static void setup() {
    gson = UsersControllerFactory.GSON;
  }

  /**
   * Main class constructor.
   */
  UserControllerIntegrationTest() {
    super();
  }

  /**
   * Check that try changing email of not existing member
   * return 400 bad request.
   *
   * @throws Exception Test fails.
   */
  @Test
  @Transactional
  void testChangeMemberEmailNotExistingMemberBadRequest() throws Exception {
    var notExistingMemberEmail = "email@email.es";
    var newEmail = "newEmail@email.es";
    var changeEmailRequest = UserChangeEmailRequest.builder()
          .email(notExistingMemberEmail).newEmail(newEmail).build();
    var changeEmailRequestJson = gson.toJson(changeEmailRequest);

    mockMvc.perform(
          patch(CHANGE_MEMBER_EMAIL_URL).contentType(MediaType.APPLICATION_JSON)
                .content(changeEmailRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Check that change email for existing member returns
   * member data with new email.
   *
   * @throws Exception Test fails.
   */
  @Test
  @Transactional
  void testChangeMemberEmail() throws Exception {
    var memberEmail = UsersControllerFactory.USER_A_EMAIL;
    var newEmail = "newEmail@email.es";

    var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var memberRequestJson = gson.toJson(memberRequest);
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(memberRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var changeEmailRequest = UserChangeEmailRequest.builder().email(memberEmail)
          .newEmail(newEmail).build();
    var changeEmailRequestJson = gson.toJson(changeEmailRequest);

    var changeMemberEmailResponseJson = mockMvc
          .perform(
                patch(CHANGE_MEMBER_EMAIL_URL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changeEmailRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var response =
          gson.fromJson(changeMemberEmailResponseJson, UserDataResponse.class);

    Assertions.assertNotEquals(
          memberEmail, response.getEmail(),
          "Response not cointains in itial member email."
    );
    Assertions.assertEquals(
          newEmail, response.getEmail(), "Response contains new member email."
    );
  }

  /**
   * Check that try changing password of not existing member
   * return 400 bad request.
   *
   * @throws Exception Test fails.
   */
  @Test
  @Transactional
  void testChangeMemberPasswordPasswordChanged() throws Exception {
    var memberEmail = UsersControllerFactory.USER_A_EMAIL;
    var currentPassword = UsersControllerFactory.USER_A_PASSWORD;
    var newPassword = "321321";

    var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();

    var memberRequestJson = gson.toJson(memberRequest);
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(memberRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var changePasswordRequest = UserChangePasswordRequest.builder()
          .email(memberEmail).currentPassword(currentPassword)
          .newPassword(newPassword).build();
    var changePasswordRequestJson = gson.toJson(changePasswordRequest);
    mockMvc.perform(
          patch(CHANGE_MEMBER_PASSWORD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(changePasswordRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Try login with the new password and old password,
    // only works new password.
    var authenticationRequest =
          new AuthenticationRequest(memberEmail, currentPassword);
    var authenticationRequestJson = gson.toJson(authenticationRequest);

    mockMvc.perform(
          post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(authenticationRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isUnauthorized());

    authenticationRequest = new AuthenticationRequest(memberEmail, newPassword);
    authenticationRequestJson = gson.toJson(authenticationRequest);

    mockMvc.perform(
          post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(authenticationRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());
  }

  /**
   * Check that try changing password of not existing member
   * return 400 bad request.
   *
   * @throws Exception Test fails.
   */
  @Test
  @Transactional
  void testChangeMemberPasswordNotExistingMemberBadRequest() throws Exception {
    var notExistingMemberEmail = "email@email.es";
    var currentPassword = "123123";
    var newPassword = "321321";
    var changePasswordRequest = UserChangePasswordRequest.builder()
          .email(notExistingMemberEmail).currentPassword(currentPassword)
          .newPassword(newPassword).build();
    var changePasswordRequestJson = gson.toJson(changePasswordRequest);
    mockMvc.perform(
          patch(CHANGE_MEMBER_PASSWORD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(changePasswordRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Check that change password of user not valid password.
   *
   * @throws Exception Test fails.
   */
  @Test
  @Transactional
  void testChangeMemberPasswordCurrentPasswordDontMatch() throws Exception {
    var memberEmail = UsersControllerFactory.USER_A_EMAIL;
    var memberRequestPasswordNotValid = "456456";
    var memberNewPassword = "321321";

    var memberRequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var memberRequestJson = gson.toJson(memberRequest);
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(memberRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var changePasswordRequest = UserChangePasswordRequest.builder()
          .email(memberEmail).currentPassword(memberRequestPasswordNotValid)
          .newPassword(memberNewPassword).build();
    var changePasswordRequestJson = gson.toJson(changePasswordRequest);

    var changeMemberPasswordResponseJson = mockMvc
          .perform(
                patch(CHANGE_MEMBER_PASSWORD_URL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changePasswordRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
    Assertions.assertTrue(
          changeMemberPasswordResponseJson
                .contains(DefaultUserService.USER_PASSWORD_NOT_MATCH),
          "Message content user password dont match."
    );
  }

  /**
   * Test that check that change not existing user kind of member
   * returns a http status 400 bad request.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberNotExistingMemberBadRequest() throws Exception {

    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          UsersControllerFactory.USER_A_EMAIL, UserType.SOCIO_HONORARIO
    );
    var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
    mockMvc
          .perform(
                patch(CHANGE_KIND_MEMBER_URL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changeKindMemberRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
  }

  /**
   * Change member type from.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioHonorario() throws Exception {
    var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          UsersControllerFactory.USER_A_EMAIL, UserType.SOCIO_HONORARIO
    );
    var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
    // Update kind of member.
    var changeKindMemberResponseJson = mockMvc
          .perform(
                patch(CHANGE_KIND_MEMBER_URL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changeKindMemberRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var changeKindMemberResponse =
          gson.fromJson(changeKindMemberResponseJson, UserDataResponse.class);
    Assertions.assertEquals(
          UserType.SOCIO_HONORARIO, changeKindMemberResponse.getKindMember(),
          "kind of member has been changed."
    );
    Assertions.assertEquals(
          userARequest.getEmail(), changeKindMemberResponse.getEmail(),
          "email is the user email."
    );
  }

  /**
   * Test  change kind of existing member from "socio numero" to
   * "socio aspirante"  can be done only if age is under 18.
   * TO-DO -NOT FAIL WHEN AGE IS UNDER 18 (WRONG)
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioAspirante() throws Exception {
    // Age under 18.
    final var userAgeUnder18 = LocalDate.of(2010, 2, 2);
    var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
    userARequest.setBirthDate(userAgeUnder18);
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          UsersControllerFactory.USER_A_EMAIL, UserType.SOCIO_ASPIRANTE
    );
    var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
    // Update kind of member.
    var changeKindMemberResponseJson = mockMvc
          .perform(
                patch(CHANGE_KIND_MEMBER_URL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changeKindMemberRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var changeKindMemberResponse =
          gson.fromJson(changeKindMemberResponseJson, UserDataResponse.class);
    Assertions.assertEquals(
          UserType.SOCIO_ASPIRANTE, changeKindMemberResponse.getKindMember(),
          "kind of member has been changed."
    );
    Assertions.assertEquals(
          userARequest.getEmail(), changeKindMemberResponse.getEmail(),
          "email is the user email."
    );
  }

  /**
   * TO-DO.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioAspiranteNotAllowedBirthDate()
        throws Exception {
    //User age NOT under 18.
    var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          UsersControllerFactory.USER_A_EMAIL, UserType.SOCIO_ASPIRANTE
    );
    var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
    // Update kind of member.
    mockMvc
          .perform(
                patch(CHANGE_KIND_MEMBER_URL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changeKindMemberRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
  }

  /**
   * TO-DO.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioFamiliar() throws Exception {
    var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          UsersControllerFactory.USER_A_EMAIL, UserType.SOCIO_FAMILIAR
    );
    var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
    // Update kind of member.
    var changeKindMemberResponseJson = mockMvc
          .perform(
                patch(CHANGE_KIND_MEMBER_URL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changeKindMemberRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var changeKindMemberResponse =
          gson.fromJson(changeKindMemberResponseJson, UserDataResponse.class);
    Assertions.assertEquals(
          UserType.SOCIO_FAMILIAR, changeKindMemberResponse.getKindMember(),
          "kind of member has been changed."
    );
    Assertions.assertEquals(
          userARequest.getEmail(), changeKindMemberResponse.getEmail(),
          "email is the user email."
    );
  }

  /**
   * Create 3 members and get their data.
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testGetAllUsersData() throws Exception {
    //FIRST MEMBER
    var userRequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var userRequestJson = gson.toJson(userRequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var secondUserDNI = "32721860J";
    var secondUserEmail = "second@email.es";
    userRequest.setDni(secondUserDNI);
    userRequest.setEmail(secondUserEmail);
    userRequestJson = gson.toJson(userRequest);
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var thirdUserDNI = "11111111H";
    var thirdUserEmail = "third@email.es";
    userRequest.setDni(thirdUserDNI);
    userRequest.setEmail(thirdUserEmail);
    userRequestJson = gson.toJson(userRequest);
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var resultJson = mockMvc.perform(get(GET_ALL_USERS_DATA_URL))
          .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    var userListResponse =
          gson.fromJson(resultJson, UserListDataResponse.class);
    var usersCount = 3;
    Assertions.assertEquals(
          usersCount, userListResponse.getUsersList().size(),
          "The user list size is 3"
    );

  }

  /**
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testGetOneUsersData() throws Exception {
    //FIRST MEMBER
    var userRequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var userRequestJson = gson.toJson(userRequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    // get authorization jwt token
    var authReq = new AuthenticationRequest(
          UsersControllerFactory.USER_A_EMAIL,
          UsersControllerFactory.USER_A_PASSWORD
    );
    var authReqJson = gson.toJson(authReq);
    var authResponseJson = mockMvc
          .perform(
                post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(authReqJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var authResponse =
          gson.fromJson(authResponseJson, AuthenticationResponse.class);

    var accessToken = authResponse.getJwt();

    mockMvc.perform(
          get(GET_USER_DATA_URL).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
    ).andReturn().getResponse().getContentAsString();
  }

  @Transactional
  @Test
  void testUnsubscribeUserCheckUnsubscriber() throws Exception {
    var userRequest = UsersControllerFactory.getSignUpRequestFormUserA();
    var userRequestJson = gson.toJson(userRequest);
    // Register user correctly.
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Sign in with user data, get jwt token.
    var authReq = new AuthenticationRequest();
    authReq.setEmail(UsersControllerFactory.USER_A_EMAIL);
    authReq.setPassword(UsersControllerFactory.USER_A_PASSWORD);
    var authReqJson = gson.toJson(authReq);
    var authResponseJson = mockMvc
          .perform(
                post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(authReqJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    var authResponse =
          gson.fromJson(authResponseJson, AuthenticationResponse.class);
    var jwtToken = authResponse.getJwt();

    // Check that user was unsubscribed succesfully.
    mockMvc.perform(
          get(GET_USER_DATA_URL)

                .header("Authorization", "Bearer " + jwtToken)
    );

    // Unsubscribe user.
    final var userUnsubscribeRequest = UserUnsubscribeRequest.builder()
          .email(UsersControllerFactory.USER_A_EMAIL)
          .password(UsersControllerFactory.USER_A_PASSWORD).build();

    final var userUnsubscribeRequestJson = gson.toJson(userUnsubscribeRequest);

    mockMvc.perform(
          delete(GET_USER_DATA_URL + "/unsubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userUnsubscribeRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Check that user was unsubscribed succesfully.
    mockMvc.perform(
          get(GET_USER_DATA_URL)

                .header("Authorization", "Bearer " + jwtToken)
    ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

}
