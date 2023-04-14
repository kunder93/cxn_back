package es.org.cxn.backapp.model.form.responses;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;

/**
 * Represents the form used by controller as response for the creating user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class SignUpResponseForm implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -3214940499061435783L;

    /**
     * Dni field.
     */
    private String dni;

    /**
     * Name field.
     */
    private String name;

    /**
     * User first surname field.
     */
    private String firstSurname;

    /**
     * User second surname field.
     */
    private String secondSurname;

    /**
     * User birth date field.
     */
    private LocalDate birthDate;

    /**
     * User gender field.
     */
    private String gender;

    /**
     * User email field.
     */
    private String email;

    /**
     * User roles set field.
     */
    private Set<String> userRoles = new HashSet<>();

    /**
     * Constructs an empty DTO.
     */
    public SignUpResponseForm() {
        super();
    }

    /**
     * Construct a DTO with all fields provided.
     *
     * @param dniValue           the dni aka identifier field.
     * @param nameValue          the name field.
     * @param firstSurnameValue  the first surname field.
     * @param secondSurnameValue the second surname field.
     * @param birthDateValue     the birth date field.
     * @param genderValue        the gender field.
     * @param emailValue         the email field.
     * @param userRolesSet       the set of user roles to get name.
     */
    public SignUpResponseForm(
            final String dniValue, final String nameValue,
            final String firstSurnameValue, final String secondSurnameValue,
            final LocalDate birthDateValue, final String genderValue,
            final String emailValue,
            final Set<PersistentRoleEntity> userRolesSet
    ) {
        super();
        this.dni = dniValue;
        this.name = nameValue;
        this.firstSurname = firstSurnameValue;
        this.secondSurname = secondSurnameValue;
        this.birthDate = birthDateValue;
        this.gender = genderValue;
        this.email = emailValue;
        userRolesSet.forEach(role -> this.userRoles.add(role.getName()));
    }

    /**
     * Returns the value of the dni field.
     *
     * @return the dni value.
     */
    public final String getDni() {
        return dni;
    }

    /**
     * Returns the value of the name field.
     *
     * @return the name value.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the value of the first surname field.
     *
     * @return the first surname value.
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
     * @return the value of the gender field.
     */
    public final String getGender() {
        return gender;
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
     * Returns the value of the userRoles field.
     *
     * @return the value of the userRoles field.
     */
    public Set<String> getUserRoles() {
        return new HashSet<>(userRoles);
    }

    /**
     * Sets the value of the dni field.
     *
     * @param value the new value for the dni field.
     */
    public void setDni(final String value) {
        this.dni = checkNotNull(value, "Received a null pointer as dni");
    }

    /**
     * Sets the value of the name field.
     *
     * @param value the new value for the name field.
     */
    public void setName(final String value) {
        this.name = checkNotNull(value, "Received a null pointer as name");
    }

    /**
     * Sets the value of the first surname field.
     *
     * @param value the new value for the first surname field.
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
     * @param value the new value for the birth date field.
     */
    public final void setBirthDate(final LocalDate value) {
        this.birthDate = checkNotNull(
                value, "Received a null pointer as birthDate"
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
     * Sets the value of the email field.
     *
     * @param value the new value for the email field.
     */
    public final void setEmail(final String value) {
        this.email = checkNotNull(value, "Received a null pointer as email");
    }

    /**
     * Sets the value of the userRoles field.
     *
     * @param value the new value for the userRoles field.
     */
    public final void setUserRoles(final Iterable<PersistentRoleEntity> value) {
        value.forEach(role -> this.userRoles.add(role.getName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                birthDate, dni, email, firstSurname, gender, name, secondSurname
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
        var other = (SignUpResponseForm) obj;
        return Objects.equals(birthDate, other.birthDate)
                && Objects.equals(dni, other.dni)
                && Objects.equals(email, other.email)
                && Objects.equals(firstSurname, other.firstSurname)
                && Objects.equals(gender, other.gender)
                && Objects.equals(name, other.name)
                && Objects.equals(secondSurname, other.secondSurname);
    }

    @Override
    public String toString() {
        return "SignUpResponseForm [dni=" + dni + ", name=" + name
                + ", firstSurname=" + firstSurname + ", secondSurname="
                + secondSurname + ", birthDate=" + birthDate + ", gender="
                + gender + ", email=" + email + "]";
    }

}
