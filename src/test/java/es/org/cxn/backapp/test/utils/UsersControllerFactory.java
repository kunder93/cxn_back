
package es.org.cxn.backapp.test.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import java.time.LocalDate;

public final class UsersControllerFactory {

  public static final String USER_A_DNI = "32721859N";
  public static final String USER_A_NAME = "Santiago";
  public static final String USER_A_FIRST_SURNAME = "Paz";
  public static final String USER_A_SECOND_SURNAME = "Perez";
  public static final LocalDate USER_A_BIRTH_DATE = LocalDate.of(1993, 5, 8);
  public static final String USER_A_GENDER = "Male";
  public static final String USER_A_PASSWORD = "fakeValidPassword";
  public static final String USER_A_EMAIL = "santi@santi.es";
  public static final String USER_A_POSTAL_CODE = "15570";
  public static final String USER_A_APARTMENT_NUMBER = "1D";
  public static final String USER_A_BUILDING = "7";
  public static final String USER_A_STREET = "Marina Espanola";
  public static final String USER_A_CITY = "Naron";
  public static final Integer USER_A_COUNTRY_NUMERIC_CODE = 724;
  public static final String USER_A_COUNTRY_SUBDIVISION_NAME = "Lugo";
  public static final UserType USER_A_KIND_MEMBER = UserType.SOCIO_NUMERO;

  private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  public static SignUpRequestForm getSignUpRequestFormUserA() {
    return SignUpRequestForm.builder().dni(USER_A_DNI).name(USER_A_NAME)
          .firstSurname(USER_A_FIRST_SURNAME)
          .secondSurname(USER_A_SECOND_SURNAME).birthDate(USER_A_BIRTH_DATE)
          .gender(USER_A_GENDER).password(USER_A_PASSWORD).email(USER_A_EMAIL)
          .postalCode(USER_A_POSTAL_CODE)
          .apartmentNumber(USER_A_APARTMENT_NUMBER).building(USER_A_BUILDING)
          .street(USER_A_STREET).city(USER_A_CITY)
          .countryNumericCode(USER_A_COUNTRY_NUMERIC_CODE)
          .countrySubdivisionName(USER_A_COUNTRY_SUBDIVISION_NAME)
          .kindMember(USER_A_KIND_MEMBER).build();
  }

  public static String getUserARequestJson() {
    return gson.toJson(getSignUpRequestFormUserA());
  }

  // Private constructor to prevent instantiation
  private UsersControllerFactory() {
    throw new UnsupportedOperationException(
          "Utility class should not be instantiated."
    );
  }
}
