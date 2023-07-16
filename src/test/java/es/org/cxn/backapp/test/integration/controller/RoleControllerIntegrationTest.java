
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.requests.UserChangeRoleRequestForm;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.model.form.responses.UserChangeRoleResponseForm;
import es.org.cxn.backapp.service.JwtUtils;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

import utils.LocalDateAdapter;

/**
 * @author Santiago Paz. Authentication controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class RoleControllerIntegrationTest {

  private final static String DEFAULT_USER_ROLE = "ROLE_SOCIO";
  private final static String SIGN_UP_URL = "/api/auth/signup";
  private final static String SIGN_IN_URL = "/api/auth/signinn";
  private final static String ROLES_URL = "/api/user/role";

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
          userA_postalCode, userA_apartmentNumber, userA_building, userA_street,
          userA_city, userA_countryNumericCode, userA_countrySubdivisionName
    );
    var userBRequest = new SignUpRequestForm(
          userB_dni, userB_name, userB_firstSurname, userB_secondSurname,
          userB_birthDate, userB_gender, userB_password, userA_email,
          userB_postalCode, userB_apartmentNumber, userB_building, userB_street,
          userB_city, userB_countryNumericCode, userB_countrySubdivisionName
    );
    userARequestJson = gson.toJson(userARequest);

    userBRequestJson = gson.toJson(userBRequest);
  }

  /**
   * Main class constructor
   */
  public RoleControllerIntegrationTest() {
    super();
  }

  /**
   * Register user and expect user role default user role.
   *
   * @throws Exception When fails.
   */
  @DisplayName("Create user and expect only default user role.")
  @Test
  @Transactional
  void testCreateUserExpectDefaultUserRole() throws Exception {

    // Register user correctly
    var response = mockMvc
          .perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userARequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var aa = gson.fromJson(response, SignUpResponseForm.class);
    Assertions.assertEquals(
          aa.getUserRoles().size(), Integer.valueOf(1), "Only have one role."
    );
    Assertions.assertTrue(
          aa.getUserRoles().contains(DEFAULT_USER_ROLE),
          "Have default user role."
    );
  }

  /**
   * Add role to created user and expect it in user data.
   *
   * @throws Exception When fails.
   */
  @DisplayName("Add role to created user expect user data with role added.")
  @Test
  @Transactional
  void testCreateUserAddRoleCheckIt() throws Exception {
    var userRoleToAdd = "ROLE_TESORERO";
    // Register user correctly
    var response = mockMvc
          .perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userARequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var aa = gson.fromJson(response, SignUpResponseForm.class);

    var userEmail = aa.getEmail();
    var userChangeRoleRequest =
          new UserChangeRoleRequestForm(userEmail, userRoleToAdd);
    var userChangeRoleRequestJson = gson.toJson(userChangeRoleRequest);
    var addRoleResponse = mockMvc
          .perform(
                post(ROLES_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userChangeRoleRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var bb = gson.fromJson(addRoleResponse, UserChangeRoleResponseForm.class);

    Assertions.assertEquals(
          bb.getUserRoles().size(), Integer.valueOf(2), "User have 2 roles."
    );
    Assertions.assertTrue(
          bb.getUserRoles().contains(DEFAULT_USER_ROLE),
          "user have default role"
    );
    Assertions.assertTrue(
          bb.getUserRoles().contains(userRoleToAdd), "user have added role"
    );
  }

  /**
   * Add role to created user and expect it in user data.
   *
   * @throws Exception When fails.
   */
  @DisplayName("Add role to created user expect user data with role added.")
  @Test
  @Transactional
  void testCreateUserAddNotExistingRoleBadRequest() throws Exception {
    var userRoleToAdd = "ROLE_NOT_EXIST";
    // Register user correctly
    var response = mockMvc
          .perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userARequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var aa = gson.fromJson(response, SignUpResponseForm.class);

    var userEmail = aa.getEmail();
    var userChangeRoleRequest =
          new UserChangeRoleRequestForm(userEmail, userRoleToAdd);
    var userChangeRoleRequestJson = gson.toJson(userChangeRoleRequest);
    mockMvc.perform(
          post(ROLES_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userChangeRoleRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Delete role from created user.
   *
   * @throws Exception When fails.
   */
  @DisplayName("Delete role from created user.")
  @Test
  @Transactional
  void testDeleteRoleFromUser() throws Exception {
    var userRoleToAdd = "ROLE_TESORERO";
    // Register user correctly
    var response = mockMvc
          .perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userARequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var aa = gson.fromJson(response, SignUpResponseForm.class);

    var userEmail = aa.getEmail();
    var userChangeRoleRequest =
          new UserChangeRoleRequestForm(userEmail, userRoleToAdd);
    var userChangeRoleRequestJson = gson.toJson(userChangeRoleRequest);
    // Add second user role.
    mockMvc.perform(
          post(ROLES_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userChangeRoleRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());
    // Delete role from user.
    mockMvc.perform(
          delete(ROLES_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userChangeRoleRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());
  }

  /**
   * Delete not existing role from created user.
   *
   * @throws Exception When fails.
   */
  @DisplayName("Delete not existing role from created user.")
  @Test
  @Transactional
  void testDeleteNotExistingRoleFromUserBadRequest() throws Exception {
    var userRoleToAdd = "ROLE_INVENTED";
    // Register user correctly
    var response = mockMvc
          .perform(
                post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(userARequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var aa = gson.fromJson(response, SignUpResponseForm.class);

    var userEmail = aa.getEmail();
    var userChangeRoleRequest =
          new UserChangeRoleRequestForm(userEmail, userRoleToAdd);

    var userChangeRoleRequestJson = gson.toJson(userChangeRoleRequest);

    // Delete role from user.
    mockMvc.perform(
          delete(ROLES_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userChangeRoleRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

}
