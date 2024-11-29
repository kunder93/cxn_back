
package es.org.cxn.backapp.test.utils;

import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.user.UserType;

/**
 * Users objects and data for use in tests.
 *
 * @author Santi
 *
 */
public final class UsersControllerFactory {

    /**
     * The user role assigned auto when user is created.
     */
    public static final UserRoleName DEFAULT_USER_ROLE = UserRoleName.ROLE_CANDIDATO_SOCIO;

    /**
     * User A DNI.
     */
    public static final String USER_A_DNI = "32721860J";
    /**
     * User A name.
     */
    public static final String USER_A_NAME = "Santiago";
    /**
     * User A first surname.
     */
    public static final String USER_A_FIRST_SURNAME = "Paz";
    /**
     * User A second surname.
     */
    public static final String USER_A_SECOND_SURNAME = "Perez";
    /**
     * User A birth date.
     */
    public static final LocalDate USER_A_BIRTH_DATE = LocalDate.of(1993, 5, 8);
    /**
     * User A gender.
     */
    public static final String USER_A_GENDER = "Male";
    /**
     * User A password.
     */
    public static final String USER_A_PASSWORD = "fakeValidPassword";
    /**
     * User A email.
     */
    public static final String USER_A_EMAIL = "fake@email.es";
    /**
     * User A address postal code.
     */
    public static final String USER_A_POSTAL_CODE = "15570";
    /**
     * User A address apartment number.
     */
    public static final String USER_A_APARTMENT_NUMBER = "1D";
    /**
     * User A address building.
     */
    public static final String USER_A_BUILDING = "7";
    /**
     * User A address street.
     */
    public static final String USER_A_STREET = "Marina Espanola";
    /**
     * User A address city.
     */
    public static final String USER_A_CITY = "Naron";
    /**
     * User A address country numeric code.
     */
    public static final Integer USER_A_COUNTRY_NUMERIC_CODE = 724;
    /**
     * User A address country subdivision aka "provincia".
     */
    public static final String USER_A_COUNTRY_SUBDIVISION_NAME = "Lugo";
    /**
     * User A kind of member.
     */
    public static final UserType USER_A_KIND_MEMBER = UserType.SOCIO_NUMERO;

    /**
     * A Gson instance configured to handle serialization and deserialization of
     * LocalDate objects. This instance is created using a GsonBuilder with a custom
     * TypeAdapter for LocalDate to ensure proper conversion between LocalDate and
     * its JSON representation.
     *
     * The LocalDateAdapter class should implement the TypeAdapter interface to
     * define how LocalDate objects are serialized to and deserialized from JSON.
     * This setup allows the Gson library to correctly process LocalDate fields in
     * JSON, ensuring compatibility with JSON APIs or data interchange formats that
     * use LocalDate.
     *
     * Usage example:
     *
     * <pre>
     * {@code
     * String json = gson.toJson(localDateInstance);
     * LocalDate date = gson.fromJson(json, LocalDate.class);
     * }
     * </pre>
     *
     * @see LocalDateAdapter
     */
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    /**
     * User B dni.
     */
    public static final String USER_B_DNI = "32721860J";

    /**
     * User B email.
     */
    public static final String USER_B_EMAIL = "adri@adri.es";

    /**
     * User B name.
     */
    public static final String USER_B_NAME = "Adrián";
    /**
     * User B first surname.
     */
    public static final String USER_B_FIRST_SURNAME = "Paz";
    /**
     * User B second surname.
     */
    public static final String USER_B_SECOND_SURNAME = "Pérez";
    /**
     * User B birth date.
     */
    public static final LocalDate USER_B_BIRTH_DATE = LocalDate.of(1996, 2, 8);
    /**
     * User B gender.
     */
    public static final String USER_B_GENDER = "Male";
    /**
     * User B password.
     */
    public static final String USER_B_PASSWORD = "fakeValidPassword";
    /**
     * User B address postal code.
     */
    public static final String USER_B_POSTAL_CODE = "15401";
    /**
     * User B address apartment number.
     */
    public static final String USER_B_APARTMENT_NUMBER = "12";
    /**
     * User B address building.
     */
    public static final String USER_B_BUILDING = "2A";
    /**
     * User B address street.
     */
    public static final String USER_B_STREET = "Calle caranza libre";
    /**
     * User B address city.
     */
    public static final String USER_B_CITY = "Ferrol";
    /**
     * User B address country numeric code.
     */
    public static final Integer USER_B_COUNTRY_NUMERIC_CODE = 724;
    /**
     * User B address country subdivision name, "provincia".
     */
    public static final String USER_B_COUNTRY_SUBDIVISION_NAME = "Coruña";
    /**
     * User B kind of member.
     */
    public static final UserType USER_B_KIND_MEMBER = UserType.SOCIO_NUMERO;

    /**
     * Private constructor to prevent instantiation.
     */
    private UsersControllerFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * @return SignUpRequestForm a request form with data from user A.
     */
    public static SignUpRequestForm getSignUpRequestFormUserA() {
        return new SignUpRequestForm(USER_A_DNI, USER_A_NAME, USER_A_FIRST_SURNAME, USER_A_SECOND_SURNAME,
                USER_A_BIRTH_DATE, USER_A_GENDER, USER_A_PASSWORD, USER_A_EMAIL, USER_A_POSTAL_CODE,
                USER_A_APARTMENT_NUMBER, USER_A_BUILDING, USER_A_STREET, USER_A_CITY, USER_A_KIND_MEMBER,
                USER_A_COUNTRY_NUMERIC_CODE, USER_A_COUNTRY_SUBDIVISION_NAME);

    }

    /**
     * @return SignUpRequestForm the form request object with User B data.
     */
    public static SignUpRequestForm getSignUpRequestFormUserB() {
        return new SignUpRequestForm(USER_B_DNI, USER_B_NAME, USER_B_FIRST_SURNAME, USER_B_SECOND_SURNAME,
                USER_B_BIRTH_DATE, USER_B_GENDER, USER_B_PASSWORD, USER_B_EMAIL, USER_B_POSTAL_CODE,
                USER_B_APARTMENT_NUMBER, USER_B_BUILDING, USER_B_STREET, USER_B_CITY, USER_B_KIND_MEMBER,
                USER_B_COUNTRY_NUMERIC_CODE, USER_B_COUNTRY_SUBDIVISION_NAME);

    }

    /**
     * @return User A request in Json format.
     */
    public static String getUserARequestJson() {
        return GSON.toJson(getSignUpRequestFormUserA());
    }

    /**
     * @return signUp request with User B data in Json.
     */
    public static String getUserBRequestJson() {
        return GSON.toJson(getSignUpRequestFormUserB());
    }
}
