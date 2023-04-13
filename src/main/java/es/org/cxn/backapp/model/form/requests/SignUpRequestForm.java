package es.org.cxn.backapp.model.form.requests;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import es.org.cxn.backapp.model.form.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * Represents the form used by controller as input for the creating users.
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
     * User name field.
     * <p>
     * Name must not be null and must contain at least one non-whitespace
     * character. Max name lengh is NAME_MAX_LENGHT.
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
     * non-whitespace character. Max first surname lengh is
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
    @NotBlank
    @Email
    private String email;

    /**
     * Constructs a DTO without data for fields.
     */
    public SignUpRequestForm() {
        super();
    }

    /**
     * Constructs a DTO with all fields provided.
     *
     * @param nameValue          the name field.
     * @param firstSurnameValue  the first surname field.
     * @param secondSurnameValue the second surname field.
     * @param birthDateValue     the birth date field.
     * @param genderValue        the gender field.
     * @param passwordValue      the password field.
     * @param emailValue         the email field.
     */
    public SignUpRequestForm(
            final String nameValue, final String firstSurnameValue,
            final String secondSurnameValue, final LocalDate birthDateValue,
            final String genderValue, final String passwordValue,
            final String emailValue
    ) {
        super();
        this.name = nameValue;
        this.firstSurname = firstSurnameValue;
        this.secondSurname = secondSurnameValue;
        this.birthDate = birthDateValue;
        this.gender = genderValue;
        this.password = passwordValue;
        this.email = emailValue;
    }

    /**
     * Returns the value of the name field.
     *
     * @return the value of the name field.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the value of the first surname field.
     *
     * @return the value of the first surname field.
     */
    public final String getFirstSurname() {
        return firstSurname;
    }

    /**
     * Returns the value of the second surname field.
     *
     * @return the value of the second surname field.
     */
    public final String getSecondSurname() {
        return secondSurname;
    }

    /**
     * Returns the value of the birth date field.
     *
     * @return the value of the birth date field.
     */
    public final LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the value of the gender field.
     *
     * @return the value of the gender field
     */
    public final String getGender() {
        return gender;
    }

    /**
     * Returns the value of the password field.
     *
     * @return the value of the password field.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Returns the value of the email field.
     *
     * @return the value of the email field.
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Sets the value of the name field.
     *
     * @param value the new value for the name field.
     */
    public final void setName(final String value) {
        this.name = checkNotNull(value, "Received a null pointer as name");
    }

    /**
     * Sets the value of the first surname field.
     *
     * @param value the new value for the first surname field
     */
    public final void setFirstSurname(final String value) {
        this.firstSurname = checkNotNull(
                value, "Received a null pointer as first surname"
        );
    }

    /**
     * Sets the value of the second surname field.
     *
     * @param value the new value for the second surname field.
     */
    public final void setSecondSurname(final String value) {
        this.secondSurname = checkNotNull(
                value, "Received a null pointer as second surname"
        );
    }

    /**
     * Sets the value of the birth date field.
     *
     * @param value the new value for the birth date field
     */
    public final void setBirthDate(final LocalDate value) {
        this.birthDate = checkNotNull(
                value, "Received a null pointer as birth_date"
        );
    }

    /**
     * Sets the value of the gender field.
     *
     * @param value the new value for the gender field.
     */
    public final void setGender(final String value) {
        this.gender = checkNotNull(value, "Received a null pointer as gender");
    }

    /**
     * Sets the value of the password field.
     *
     * @param value the new value for the password field.
     */
    public final void setPassword(final String value) {
        this.password = checkNotNull(
                value, "Received a null pointer as password"
        );
    }

    /**
     * Sets the value of the email field.
     *
     * @param value the new value for the email field.
     */
    public final void setEmail(final String value) {
        this.email = checkNotNull(value, "Received a null pointer as email");
    }

    /**
     * Hash code.
     *
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                birthDate, email, firstSurname, gender, name, password,
                secondSurname
        );
    }

    /**
     * Equals.
     */
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
        final var other = (SignUpRequestForm) obj;
        return Objects.equals(birthDate, other.birthDate)
                && Objects.equals(email, other.email)
                && Objects.equals(firstSurname, other.firstSurname)
                && Objects.equals(gender, other.gender)
                && Objects.equals(name, other.name)
                && Objects.equals(password, other.password)
                && Objects.equals(secondSurname, other.secondSurname);
    }

    /**
     * To string.
     */
    @Override
    public String toString() {
        return "UserForm [name=" + name + ", first_surname=" + firstSurname
                + ", second_surname=" + secondSurname + ", birth_date="
                + birthDate + ", gender=" + gender + ", password=" + password
                + ", email=" + email + "]";
    }

}
