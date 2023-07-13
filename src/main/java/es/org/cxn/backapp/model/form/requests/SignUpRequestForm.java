
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.form.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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
        message = Constants.DNI_BAD_FORMAT_MESSAGE
  )
  @NotNull(message = Constants.DNI_BAD_FORMAT_MESSAGE)
  private String dni;

  /**
   * User name field.
   * <p>
   * Name must not be null and must contain at least one non-whitespace
   * character. Max name length is NAME_MAX_LENGHT.
   */
  @NotBlank(message = Constants.NAME_NOT_BLANK_MESSAGE)
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
  @NotBlank(message = Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE)
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
  @NotBlank(message = Constants.SECOND_SURNAME_NOT_BLANK_MESSAGE)
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
  @Past(message = Constants.BIRTH_DATE_PAST_MESSAGE)
  private LocalDate birthDate;

  /**
   * User gender field.
   * <p>
   * Gender must not be null and must contain at least one non-whitespace
   * character. Max gender length is SECOND_SURNAME_MAX_LENGHT.
   */
  @NotBlank(message = Constants.GENDER_NOT_BLANK_MESSAGE)
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
  @NotBlank(message = Constants.EMAIL_NOT_VALID_MESSAGE)
  @Size(
        max = Constants.EMAIL_MAX_SIZE,
        message = Constants.MAX_SIZE_EMAIL_MESSAGE
  )
  @Email(message = Constants.EMAIL_NOT_VALID_MESSAGE)
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
  @NotBlank(message = Constants.BUILDING_NOT_BLANK_MESSAGE)
  @Size(
        max = Constants.BUILDING_MAX_LENGHT,
        message = Constants.BUILDING_MAX_LENGHT_MESSAGE
  )
  private String building;

  /**
   * User street address.
   */
  @NotBlank(message = Constants.STREET_NOT_BLANK_MESSAGE)
  @Size(
        max = Constants.STREET_MAX_LENGHT,
        message = Constants.STREET_MAX_LENGHT_MESSAGE
  )
  private String street;

  /**
   * User city address.
   */
  @NotBlank(message = Constants.CITY_NOT_BLANK_MESSAGE)
  @Size(
        max = Constants.CITY_MAX_LENGHT,
        message = Constants.CITY_MAX_LENGHT_MESSAGE
  )
  private String city;

  /**
   * Country numeric code (address).
   */
  private Integer countryNumericCode;

  /**
   * Country subdivision name (address).
   */
  private String countrySubdivisionName;

  /**
   * Empty request constructor.
   */
  public SignUpRequestForm() {
    super();
  }

  /**
   * Request constructor.
   *
   * @param dni                    The dni.
   * @param name                   The name.
   * @param firstSurname           The first surname.
   * @param secondSurname          The second surname.
   * @param birthDate              The birth date.
   * @param gender                 The gender.
   * @param password               The password.
   * @param email                  The email.
   * @param postalCode             The postal code.
   * @param apartmentNumber        The apartment number.
   * @param building               The building.
   * @param street                 The street.
   * @param city                   The city.
   * @param countryNumericCode     The country numeric code.
   * @param countrySubdivisionName The country subdivision name.
   */
  public SignUpRequestForm(
        final String dni, final String name, final String firstSurname,
        final String secondSurname, final LocalDate birthDate,
        final String gender, final String password, final String email,
        final String postalCode, final String apartmentNumber,
        final String building, final String street, final String city,
        final Integer countryNumericCode, final String countrySubdivisionName
  ) {
    super();
    this.dni = dni;
    this.name = name;
    this.firstSurname = firstSurname;
    this.secondSurname = secondSurname;
    this.birthDate = birthDate;
    this.gender = gender;
    this.password = password;
    this.email = email;
    this.postalCode = postalCode;
    this.apartmentNumber = apartmentNumber;
    this.building = building;
    this.street = street;
    this.city = city;
    this.countryNumericCode = countryNumericCode;
    this.countrySubdivisionName = countrySubdivisionName;
  }

  /**
   * Get request user dni.
   *
   * @return The dni.
   */
  public String getDni() {
    return dni;
  }

  /**
   * Get request user name.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Get request user first surname.
   *
   * @return The first surname.
   */
  public String getFirstSurname() {
    return firstSurname;
  }

  /**
   * Get request user second surname.
   *
   * @return The second surname.
   */
  public String getSecondSurname() {
    return secondSurname;
  }

  /**
   * Get request user birth date.
   *
   * @return The birth date.
   */
  public LocalDate getBirthDate() {
    return birthDate;
  }

  /**
   * Get request user gender.
   *
   * @return The gender.
   */
  public String getGender() {
    return gender;
  }

  /**
   * Get request user password.
   *
   * @return The password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Get request user address email.
   *
   * @return The email.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get request user address postal code.
   *
   * @return The postal code.
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Get request user address apartment number.
   *
   * @return The apartment numer.
   */
  public String getApartmentNumber() {
    return apartmentNumber;
  }

  /**
   * Get request user building address.
   *
   * @return The building.
   */
  public String getBuilding() {
    return building;
  }

  /**
   * Get request user address street.
   *
   * @return The street.
   */
  public String getStreet() {
    return street;
  }

  /**
   * Get request user address city.
   *
   * @return The city
   */
  public String getCity() {
    return city;
  }

  /**
   * Get request user address country numeric code.
   *
   * @return The country numeric code.
   */
  public Integer getCountryNumericCode() {
    return countryNumericCode;
  }

  /**
   * Get request user address country subdivision name.
   *
   * @return The country subdivision name.
   */
  public String getCountrySubdivisionName() {
    return countrySubdivisionName;
  }

  /**
   * Set request user dni.
   *
   * @param dni The dni.
   */
  public void setDni(final String dni) {
    this.dni = dni;
  }

  /**
   * Set request user name.
   *
   * @param name The user name.
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Set request user first surname.
   *
   * @param firstSurname The first surname.
   */
  public void setFirstSurname(final String firstSurname) {
    this.firstSurname = firstSurname;
  }

  /**
   * Set request user second surname.
   *
   * @param secondSurname The second surname.
   */
  public void setSecondSurname(final String secondSurname) {
    this.secondSurname = secondSurname;
  }

  /**
   * Set request user birth date.
   *
   * @param birthDate The birth date.
   */
  public void setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  /**
   * Set request user gender.
   *
   * @param gender The gender.
   */
  public void setGender(final String gender) {
    this.gender = gender;
  }

  /**
   * Set request user password.
   *
   * @param password The password.
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Set request user email.
   *
   * @param email The user email.
   */
  public void setEmail(final String email) {
    this.email = email;
  }

  /**
   * Set request postal code address.
   *
   * @param postalCode The postal code.
   */
  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Set request address apartment number.
   *
   * @param apartmentNumber The apartment number.
   */
  public void setApartmentNumber(final String apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }

  /**
   * Set request building address.
   *
   * @param building The building.
   */
  public void setBuilding(final String building) {
    this.building = building;
  }

  /**
   * Set request street address.
   *
   * @param street The street.
   */
  public void setStreet(final String street) {
    this.street = street;
  }

  /**
   * Set request city.
   *
   * @param city The city.
   */
  public void setCity(final String city) {
    this.city = city;
  }

  /**
   * Set request country numeric code.
   *
   * @param countryNumericCode The country numeric code.
   */
  public void setCountryNumericCode(final Integer countryNumericCode) {
    this.countryNumericCode = countryNumericCode;
  }

  /**
   * Set request country subdivision name.
   *
   * @param countrySubdivisionName The country subdivision name.
   */
  public void setCountrySubdivisionName(final String countrySubdivisionName) {
    this.countrySubdivisionName = countrySubdivisionName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
          apartmentNumber, birthDate, building, city, countryNumericCode,
          countrySubdivisionName, dni, email, firstSurname, gender, name,
          password, postalCode, secondSurname, street
    );
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    var other = (SignUpRequestForm) obj;
    return Objects.equals(apartmentNumber, other.apartmentNumber)
          && Objects.equals(birthDate, other.birthDate)
          && Objects.equals(building, other.building)
          && Objects.equals(city, other.city)
          && Objects.equals(countryNumericCode, other.countryNumericCode)
          && Objects
                .equals(countrySubdivisionName, other.countrySubdivisionName)
          && Objects.equals(dni, other.dni)
          && Objects.equals(email, other.email)
          && Objects.equals(firstSurname, other.firstSurname)
          && Objects.equals(gender, other.gender)
          && Objects.equals(name, other.name)
          && Objects.equals(password, other.password)
          && Objects.equals(postalCode, other.postalCode)
          && Objects.equals(secondSurname, other.secondSurname)
          && Objects.equals(street, other.street);
  }

  @Override
  public String toString() {
    return "SignUpRequestForm [dni=" + dni + ", name=" + name
          + ", firstSurname=" + firstSurname + ", secondSurname="
          + secondSurname + ", birthDate=" + birthDate + ", gender=" + gender
          + ", password=" + password + ", email=" + email + ", postalCode="
          + postalCode + ", apartmentNumber=" + apartmentNumber + ", building="
          + building + ", street=" + street + ", city=" + city
          + ", countryNumericCode=" + countryNumericCode
          + ", countrySubdivisionName=" + countrySubdivisionName + "]";
  }

}
