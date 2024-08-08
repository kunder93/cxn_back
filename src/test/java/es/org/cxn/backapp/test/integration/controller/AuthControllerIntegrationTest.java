
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.JwtUtils;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;

import java.time.LocalDate;

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

/**
 * @author Santiago Paz. Authentication controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class AuthControllerIntegrationTest {

  private final static UserRoleName DEFAULT_USER_ROLE =
        UserRoleName.ROLE_CANDIDATO_SOCIO;
  private final static String SIGN_UP_URL = "/api/auth/signup";
  private final static String SIGN_IN_URL = "/api/auth/signinn";

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

  private final static String userB_dni = "32721860J";
  private final static String userB_email = "adri@adri.es";
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

  private final static UserType kindMember = UserType.SOCIO_NUMERO;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserDetailsService myUserDetailsService;
  @Autowired
  JwtUtils jwtUtils;

  private static Gson gson;

  @BeforeAll
  static void createUsersData() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

  }

  /**
   * Main class constructor
   */
  public AuthControllerIntegrationTest() {
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
    var controllerResponse = mockMvc
          .perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userARequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var signUpResponse =
          gson.fromJson(controllerResponse, SignUpResponseForm.class);

    Assertions.assertEquals(
          signUpResponse.getDni(), userARequest.getDni(), "Dni field."
    );
    Assertions.assertEquals(
          signUpResponse.getEmail(), userARequest.getEmail(), "Email field."
    );
    Assertions.assertEquals(
          signUpResponse.getName(), userARequest.getName(), "Dni field."
    );
    Assertions.assertEquals(
          signUpResponse.getFirstSurname(), userARequest.getFirstSurname(),
          "First surname field."
    );
    Assertions.assertEquals(
          signUpResponse.getSecondSurname(), userARequest.getSecondSurname(),
          "Second surname field."
    );

    Assertions.assertEquals(
          signUpResponse.getGender(), userARequest.getGender(), "Gender field."
    );
    Assertions.assertEquals(
          signUpResponse.getBirthDate(), userARequest.getBirthDate(),
          "Birth date field."
    );
    Assertions.assertEquals(
          signUpResponse.getKindMember(), userARequest.getKindMember(),
          "kind of member field."
    );
    Assertions.assertEquals(
          signUpResponse.getUserRoles().size(), numberOfRoles, "Only one role."
    );
    Assertions.assertTrue(
          signUpResponse.getUserRoles().contains(DEFAULT_USER_ROLE),
          "Role is default user role."
    );
  }

  /**
   * SingUp second user with same email as userA bad request cause email is in using.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testSignUpSecondUserWithSameEmailBadRequest() throws Exception {
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

    var userBRequest = SignUpRequestForm.builder().dni(userB_dni)
          .name(userB_name).firstSurname(userB_firstSurname)
          .secondSurname(userB_secondSurname).birthDate(userB_birthDate)
          .gender(userB_gender).password(userB_password).email(userA_email)
          .postalCode(userB_postalCode).apartmentNumber(userB_apartmentNumber)
          .building(userB_building).street(userB_street).city(userB_city)
          .countryNumericCode(userB_countryNumericCode)
          .countrySubdivisionName(userB_countrySubdivisionName)
          .kindMember(kindMember).build();
    var userBRequestJson = gson.toJson(userBRequest);
    // Second user with same email as userA bad request.
    mockMvc.perform(
          post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userBRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * SingUp second user with same dni as userA bad request cause dni is in using.
   *
   * @throws Exception When fails.
   */
  @Test
  @Transactional
  void testSignUpSecondUserWithSameDniBadRequest() throws Exception {
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

    var userBRequest = SignUpRequestForm.builder().dni(userA_dni)
          .name(userB_name).firstSurname(userB_firstSurname)
          .secondSurname(userB_secondSurname).birthDate(userB_birthDate)
          .gender(userB_gender).password(userB_password).email(userB_email)
          .postalCode(userB_postalCode).apartmentNumber(userB_apartmentNumber)
          .building(userB_building).street(userB_street).city(userB_city)
          .countryNumericCode(userB_countryNumericCode)
          .countrySubdivisionName(userB_countrySubdivisionName)
          .kindMember(kindMember).build();
    var userBRequestJson = gson.toJson(userBRequest);
    // Second user with same email as userA bad request.
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

    var authenticationRequest =
          new AuthenticationRequest(userA_email, userA_password);
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
          userA_email, jwtUsername, "Jwt username is same as user signUp"
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
    var authenticationRequest =
          new AuthenticationRequest(userA_email, userA_password);
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

    var notValidPassword = "NotValidPassword";
    var authenticationRequest =
          new AuthenticationRequest(userA_email, notValidPassword);
    var authenticationRequestJson = gson.toJson(authenticationRequest);

    // Second user with same email as userA bad request.
    mockMvc.perform(
          post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(authenticationRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

}
