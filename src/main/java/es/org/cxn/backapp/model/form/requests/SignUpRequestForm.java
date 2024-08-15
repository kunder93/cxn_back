
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as input for the creating users. This
 * contains user data and user address.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz
 */
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public final class SignUpRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 9133529311075698110L;

  /**
   * User DNI field.
   * <p>
   * DNI must not be null and must contain 8 numeric and 1 letter. Length is 9.
   */
  @Pattern(
        regexp = "^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$",
        message = Constants.DNI_BAD_FORMAT
  )
  @NotNull(message = Constants.DNI_BAD_FORMAT)
  private String dni;

  /**
   * User name field.
   * <p>
   * Name must not be null and must contain at least one non-whitespace
   * character. Max name length is NAME_MAX_LENGHT.
   */
  @NotBlank(message = Constants.NAME_NOT_BLANK)
  @Size(
        max = Constants.NAME_MAX_LENGTH,
        message = Constants.NAME_MAX_LENGTH_MESSAGE
  )
  private String name;

  /**
   * User first surname field.
   * <p>
   * First surname must not be null and must contain at least one non-whitespace
   * character. Max first surname length is FIRST_SURNAME_MAX_LENGHT.
   */
  @NotBlank(message = Constants.FIRST_SURNAME_NOT_BLANK)
  @Size(
        max = Constants.FIRST_SURNAME_MAX_LENGTH,
        message = Constants.FIRST_SURNAME_MAX_LENGTH_MESSAGE
  )
  private String firstSurname;

  /**
   * User second surname field.
   * <p>
   * Second surname must not be null and must contain at least one
   * non-whitespace character. Max second surname length is
   * SECOND_SURNAME_MAX_LENGHT.
   */
  @NotBlank(message = Constants.SECOND_SURNAME_NOT_BLANK)
  @Size(
        max = Constants.SECOND_SURNAME_MAX_LENGTH,
        message = Constants.SECOND_SURNAME_MAX_LENGTH_MESSAGE
  )
  private String secondSurname;

  /**
   * User birth date field.
   * <p>
   * Birth date must be an instant, date or time in the past.
   */
  @Past(message = Constants.BIRTH_DATE_PAST)
  private LocalDate birthDate;

  /**
   * User gender field.
   * <p>
   * Gender must not be null and must contain at least one non-whitespace
   * character. Max gender length is SECOND_SURNAME_MAX_LENGHT.
   */
  @NotBlank(message = Constants.GENDER_NOT_BLANK)
  @Size(
        max = Constants.GENDER_MAX_LENGTH,
        message = Constants.GENDER_MAX_LENGTH_MESSAGE
  )
  private String gender;

  /**
   * User password field.
   * <p>
   * Password must not be null and must contain at least PASSWORD_MIN_LENGTH
   * non-whitespace character. Max password length is PASSWORD_MAX_LENGTH.
   */
  @NotBlank(message = Constants.PASSWORD_NOT_BLANK_MESSAGE)
  @Size(
        min = Constants.PASSWORD_MIN_LENGTH,
        max = Constants.PASSWORD_MAX_LENGTH,
        message = Constants.PASSWORD_SIZE_MESSAGE
  )
  private String password;

  /**
   * User email field.
   * <p>
   * Email must not be null and must contain at least PASSWORD_MIN_LENGTH
   * non-whitespace character. String well formated email.
   */
  @NotBlank(message = Constants.EMAIL_NOT_VALID)
  @Size(
        max = Constants.EMAIL_MAX_SIZE,
        message = Constants.MAX_SIZE_EMAIL_MESSAGE
  )
  @Email(message = Constants.EMAIL_NOT_VALID)
  private String email;

  /**
   * User postal code address.
   */
  @NotBlank(message = Constants.POSTAL_CODE_NOT_BLANK_MESSAGE)
  @Size(
        max = Constants.POSTAL_CODE_MAX_LENGHT,
        message = Constants.POSTAL_CODE_MAX_LENGHT_MESSAGE
  )
  private String postalCode;

  /**
   * User apartment number address.
   */
  @NotBlank(message = Constants.APARTMENT_NUMBER_NOT_BLANK_MESSAGE)
  @Size(
        max = Constants.APARTMENT_NUMBER_MAX_LENGHT,
        message = Constants.APARTMENT_NUMBER_MAX_LENGHT_MESSAGE
  )
  private String apartmentNumber;

  /**
   * User building address.
   */
  @NotBlank(message = Constants.BUILDING_NOT_BLANK)
  @Size(
        max = Constants.BUILDING_MAX_LENGHT,
        message = Constants.BUILDING_MAX_LENGHT_MESSAGE
  )
  private String building;

  /**
   * User street address.
   */
  @NotBlank(message = Constants.STREET_NOT_BLANK)
  @Size(
        max = Constants.STREET_MAX_LENGHT,
        message = Constants.STREET_MAX_LENGHT_MESSAGE
  )
  private String street;

  /**
   * User city address.
   */
  @NotBlank(message = Constants.CITY_NOT_BLANK)
  @Size(
        max = Constants.CITY_MAX_LENGHT,
        message = Constants.CITY_MAX_LENGHT_MESSAGE
  )
  private String city;

  /**
   * User kind of member.
   */
  private UserType kindMember;

  /**
   * Country numeric code (address).
   */
  private Integer countryNumericCode;

  /**
   * Country subdivision name (address).
   */
  private String countrySubdivisionName;

}
