
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.JwtUtils;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

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
   * hfghfgh.
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
   * TO-DO
   *
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
  //
  //  /**
  //   * TO-DO
  //   *
  //   * @throws Exception When fails.
  //   */
  //  @Test
  //  @Transactional
  //  void testChangeKindOfMemberSocioNumeroSocioFamiliarNotAllowedDiferentAddress()
  //        throws Exception {
  //     NOT IMPLEMENTED YET
  //        var userARequest = SignUpRequestForm.builder().dni(userA_dni)
  //              .name(userA_name).firstSurname(userA_firstSurname)
  //              .secondSurname(userA_secondSurname).birthDate(userA_birthDate)
  //              .gender(userA_gender).password(userA_password).email(userA_email)
  //              .postalCode(userA_postalCode).apartmentNumber(userA_apartmentNumber)
  //              .building(userA_building).street(userA_street).city(userA_city)
  //              .countryNumericCode(userA_countryNumericCode)
  //              .countrySubdivisionName(userA_countrySubdivisionName)
  //              .kindMember(kindMember).build();
  //        var userARequestJson = gson.toJson(userARequest);
  //        // Register user correctly
  //        mockMvc.perform(
  //              post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON)
  //                    .content(userARequestJson)
  //        ).andExpect(MockMvcResultMatchers.status().isCreated());
  //        var changeKindMemberRequest = new UserChangeKindMemberRequest(
  //              userA_email, UserType.SOCIO_HONORARIO
  //        );
  //        var changeKindMemberRequestJson = gson.toJson(changeKindMemberRequest);
  //        // Update kind of member.
  //        var changeKindMemberResponseJson = mockMvc
  //              .perform(
  //                    patch(CHANGE_KIND_MEMBER_URL)
  //                          .contentType(MediaType.APPLICATION_JSON)
  //                          .content(changeKindMemberRequestJson)
  //              ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
  //              .getResponse().getContentAsString();
  //        var changeKindMemberResponse =
  //              gson.fromJson(changeKindMemberResponseJson, UserDataResponse.class);
  //        Assertions.assertEquals(
  //              UserType.SOCIO_HONORARIO, changeKindMemberResponse.getKindMember(),
  //              "kind of member has been changed."
  //        );
  //        Assertions.assertEquals(
  //              userARequest.getEmail(), changeKindMemberResponse.getEmail(),
  //              "email is the user email."
  //        );
  //  }

}
