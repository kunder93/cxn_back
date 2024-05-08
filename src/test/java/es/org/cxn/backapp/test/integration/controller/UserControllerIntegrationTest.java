
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.requests.UserChangeEmailRequest;
import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.UserListDataResponse;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.DefaultUserService;
import es.org.cxn.backapp.service.JwtUtils;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import utils.LocalDateAdapter;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserDetailsService myUserDetailsService;
  @Autowired
  JwtUtils jwtUtils;

  private static Gson gson;
  private final static String GET_USER_DATA_URL = "/api/user";
  private final static String GET_ALL_USERS_DATA_URL = "/api/user/getAll";
  private final static String SIGN_IN_URL = "/api/auth/signinn";
  private final static String CHANGE_MEMBER_EMAIL_URL = "/api/user/changeEmail";
  private final static String CHANGE_MEMBER_PASSWORD_URL =
        "/api/user/changePassword";
  private final static String SIGN_UP_URL = "/api/auth/signup";
  private final static String CHANGE_KIND_MEMBER_URL =
        "/api/user/changeKindOfMember";

  private final static String userA_dni = "32721859N";
  private final static String userA_name = "Santiago";
  private final static String userA_firstSurname = "Paz";
  private final static String userA_secondSurname = "Perez";
  private final static LocalDate userA_birthDate = LocalDate.of(1993, 5, 8); // aaaa,mm,dd
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

  private final static UserType kindMember = UserType.SOCIO_NUMERO;

  @BeforeEach
  void setup() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
  }

  /**
   * Main class constructor
   */
  public UserControllerIntegrationTest() {
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
   * Check that change email of existing member returns
   * member data with new email.
   *
   * @throws Exception Test fails.
   */
  @Test
  @Transactional
  void testChangeMemberEmail() throws Exception {
    var memberEmail = "email@email.es";
    var newEmail = "newEmail@email.es";

    var memberRequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(userA_password).email(memberEmail)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember).build();
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
    var memberEmail = "email@email.es";
    var currentPassword = "123123";
    var newPassword = "321321";

    var memberRequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(currentPassword).email(memberEmail)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember).build();

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
   * Check that change email of existing member returns
   * member data with new email.
   *
   * @throws Exception Test fails.
   */
  @Test
  @Transactional
  void testChangeMemberPasswordCurrentPasswordDontMatch() throws Exception {
    var memberEmail = "email@email.es";
    var memberCurrentPassword = "123123";
    var memberRequestPassword = "456456";
    var memberNewPassword = "321321";

    var memberRequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(memberCurrentPassword)
          .email(memberEmail).postalCode(userA_postalCode)
          .apartmentNumber(userA_apartmentNumber).building(userA_building)
          .street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember).build();
    var memberRequestJson = gson.toJson(memberRequest);
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(memberRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var changePasswordRequest = UserChangePasswordRequest.builder()
          .email(memberEmail).currentPassword(memberRequestPassword)
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
          userA_email, UserType.SOCIO_HONORARIO
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
   * Change member type from
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioHonorario() throws Exception {
    var userARequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(userA_password).email(userA_email)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember).build();
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          userA_email, UserType.SOCIO_HONORARIO
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
   * Test  change kind of existing member from "socio numero" to "socio aspirante"  can be done only if age is under 18.
   * TO-DO -NOT FAIL WHEN AGE IS UNDER 18 (WRONG)
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioAspirante() throws Exception {
    // Age under 18.
    final var userAgeUnder18 = LocalDate.of(2010, 2, 2);
    var userARequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userAgeUnder18)
          .gender(userA_gender).password(userA_password).email(userA_email)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember).build();
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          userA_email, UserType.SOCIO_ASPIRANTE
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
   * TO-DO
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioAspiranteNotAllowedBirthDate()
        throws Exception {
    //User age NOT under 18.
    var userARequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(userA_password).email(userA_email)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember).build();
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest = new UserChangeKindMemberRequest(
          userA_email, UserType.SOCIO_ASPIRANTE
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
   * TO-DO
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testChangeKindOfMemberSocioNumeroSocioFamiliar() throws Exception {
    var userARequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(userA_password).email(userA_email)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember).build();
    var userARequestJson = gson.toJson(userARequest);
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userARequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    var changeKindMemberRequest =
          new UserChangeKindMemberRequest(userA_email, UserType.SOCIO_FAMILIAR);
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
    var userRequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(userA_password).email(userA_email)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember);
    var userRequestJson = gson.toJson(userRequest.build());
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var secondUserDNI = "32721860J";
    var secondUserEmail = "second@email.es";
    userRequest.dni(secondUserDNI).email(secondUserEmail);
    userRequestJson = gson.toJson(userRequest.build());
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var thirdUserDNI = "11111111H";
    var thirdUserEmail = "third@email.es";
    userRequest.dni(thirdUserDNI).email(thirdUserEmail);
    userRequestJson = gson.toJson(userRequest.build());
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
    var userRequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userA_name).firstSurname(userA_firstSurname)
          .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
          .gender(userA_gender).password(userA_password).email(userA_email)
          .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
          .building(userA_building).street(userA_street).city(userA_city)
          .countryNumericCode(userA_countryNumericCode)
          .countrySubdivisionName(userA_countrySubdivisionName)
          .kindMember(kindMember);
    var userRequestJson = gson.toJson(userRequest.build());
    // Register user correctly
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    // get authorization jwt token
    var authReq = new AuthenticationRequest(userA_email, userA_password);
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

}
