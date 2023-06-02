package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import es.org.cxn.backapp.model.form.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
public class SignUpRequestForm implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 9133529311075698110L;

    /**
     * User DNI field.
     * <p>
     * DNI must not be null and must contain 8 numeric and 1 letter. Length is
     * 9.
     */
    @Pattern(
            regexp = "^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$", message = Constants.DNI_BAD_FORMAT_MESSAGE
    )
    private String dni;

    /**
     * User name field.
     * <p>
     * Name must not be null and must contain at least one non-whitespace
     * character. Max name length is NAME_MAX_LENGHT.
     */
    @NotBlank(message = Constants.NAME_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.NAME_MAX_LENGTH, message = Constants.NAME_MAX_LENGTH_MESSAGE
    )
    private String name;

    /**
     * User first surname field.
     * <p>
     * First surname must not be null and must contain at least one
     * non-whitespace character. Max first surname length is
     * FIRST_SURNAME_MAX_LENGHT.
     */
    @NotBlank(message = Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.FIRST_SURNAME_MAX_LENGTH, message = Constants.FIRST_SURNAME_MAX_LENGTH_MESSAGE
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
            max = Constants.SECOND_SURNAME_MAX_LENGTH, message = Constants.SECOND_SURNAME_MAX_LENGTH_MESSAGE
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
            max = Constants.GENDER_MAX_LENGTH, message = Constants.GENDER_MAX_LENGTH_MESSAGE
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
            min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH, message = Constants.PASSWORD_SIZE_MESSAGE
    )
    private String password;

    /**
     * User email field.
     * <p>
     * Email must not be null and must contain at least PASSWORD_MIN_LENGTH
     * non-whitespace character. String well formated email.
     */
    @NotBlank(message = Constants.NOT_EMPTY_EMAIL_MESSAGE)
    @Size(
            max = Constants.EMAIL_MAX_SIZE, message = Constants.MAX_SIZE_EMAIL_MESSAGE
    )
    @Email(message = Constants.NOT_VALID_EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = Constants.POSTAL_CODE_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.POSTAL_CODE_MAX_LENGHT, message = Constants.POSTAL_CODE_MAX_LENGHT_MESSAGE
    )
    private String postalCode;

    @NotBlank(message = Constants.APARTMENT_NUMBER_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.APARTMENT_NUMBER_MAX_LENGHT, message = Constants.APARTMENT_NUMBER_MAX_LENGHT_MESSAGE
    )
    private String apartmentNumber;

    @NotBlank(message = Constants.BUILDING_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.BUILDING_MAX_LENGHT, message = Constants.BUILDING_MAX_LENGHT_MESSAGE
    )
    private String building;

    @NotBlank(message = Constants.STREET_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.STREET_MAX_LENGHT, message = Constants.STREET_MAX_LENGHT_MESSAGE
    )
    private String street;

    @NotBlank(message = Constants.CITY_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.CITY_MAX_LENGHT, message = Constants.CITY_MAX_LENGHT_MESSAGE
    )
    private String city;

    private Integer countryNumericCode;

    private String countrySubdivisionName;

    /**
     *
     */
    public SignUpRequestForm() {
        super();
    }

    /**
     * @param dni
     * @param name
     * @param firstSurname
     * @param secondSurname
     * @param birthDate
     * @param gender
     * @param password
     * @param email
     * @param postalCode
     * @param apartmentNumber
     * @param building
     * @param street
     * @param city
     * @param countryNumericCode
     * @param countrySubdivisionName
     */
    public SignUpRequestForm(
            String dni, String name, String firstSurname, String secondSurname,
            LocalDate birthDate, String gender, String password, String email,
            String postalCode, String apartmentNumber, String building,
            String street, String city, Integer countryNumericCode,
            String countrySubdivisionName
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

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getBuilding() {
        return building;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public Integer getCountryNumericCode() {
        return countryNumericCode;
    }

    public String getCountrySubdivisionName() {
        return countrySubdivisionName;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountryNumericCode(Integer countryNumericCode) {
        this.countryNumericCode = countryNumericCode;
    }

    public void setCountrySubdivisionName(String countrySubdivisionName) {
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
    public boolean equals(Object obj) {
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
                && Objects.equals(
                        countrySubdivisionName, other.countrySubdivisionName
                ) && Objects.equals(dni, other.dni)
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
                + secondSurname + ", birthDate=" + birthDate + ", gender="
                + gender + ", password=" + password + ", email=" + email
                + ", postalCode=" + postalCode + ", apartmentNumber="
                + apartmentNumber + ", building=" + building + ", street="
                + street + ", city=" + city + ", countryNumericCode="
                + countryNumericCode + ", countrySubdivisionName="
                + countrySubdivisionName + "]";
    }

}
