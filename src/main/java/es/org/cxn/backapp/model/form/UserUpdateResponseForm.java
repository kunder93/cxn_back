package es.org.cxn.backapp.model.form;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Response form for Controller update user.
 * {@link es.org.cxn.backapp.controller.entity.UserController#updateUserData(UserUpdateRequestForm)}.
 *
 * @author Santiago Paz Perez.
 *
 */
public class UserUpdateResponseForm implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 3698024843709681859L;

    /**
     * Name field.
     */
    private String name;
    /**
     * First surname field.
     */
    private String firstSurname;
    /**
     * Second surname field.
     */
    private String secondSurname;
    /**
     * Birth date field.
     */
    private LocalDate birthDate;
    /**
     * Gender field.
     */
    private String gender;

    /**
     * Main empty constructor.
     */
    public UserUpdateResponseForm() {
        super();
    }

    /**
     * Constructor with all fields provided.
     *
     * @param nameValue          the user name.
     * @param firstSurnameValue  the user first surname.
     * @param secondSurnameValue the user second surname.
     * @param birthDateValue     the user birth date.
     * @param genderValue        the user gender.
     */
    public UserUpdateResponseForm(final String nameValue,
            final String firstSurnameValue, final String secondSurnameValue,
            final LocalDate birthDateValue, final String genderValue) {
        super();
        this.name = nameValue;
        this.firstSurname = firstSurnameValue;
        this.secondSurname = secondSurnameValue;
        this.birthDate = birthDateValue;
        this.gender = genderValue;
    }

    /**
     * Getter for name field.
     *
     * @return the user name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for first surname field.
     *
     * @return the user first_surname.
     */
    public String getFirstSurname() {
        return firstSurname;
    }

    /**
     * Getter for second surname field.
     *
     * @return the user second surname.
     */
    public String getSecondSurname() {
        return secondSurname;
    }

    /**
     * Getter for birth date field.
     *
     * @return the user birth date.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Getter for gender field.
     *
     * @return the user gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Setter for name field.
     *
     * @param name the name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Setter for first surname field.
     *
     * @param value the first surname value.
     */
    public void setFirstSurname(final String value) {
        this.firstSurname = value;
    }

    /**
     * Setter for second surname field.
     *
     * @param value the second surname to set.
     */
    public void setSecondSurname(final String value) {
        this.secondSurname = value;
    }

    /**
     * Setter for birth date field.
     *
     * @param value the birth date to set.
     */
    public void setBirthDate(final LocalDate value) {
        this.birthDate = value;
    }

    /**
     * Setter for gender field.
     *
     * @param value the gender to set.
     */
    public void setGender(final String value) {
        this.gender = value;
    }

    /**
     * Hash code method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(birthDate, firstSurname, gender, name,
                secondSurname);
    }

    /**
     * Equals method.
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
        var other = (UserUpdateResponseForm) obj;
        return Objects.equals(birthDate, other.birthDate)
                && Objects.equals(firstSurname, other.firstSurname)
                && Objects.equals(gender, other.gender)
                && Objects.equals(name, other.name)
                && Objects.equals(secondSurname, other.secondSurname);
    }

    /**
     * To string method.
     */
    @Override
    public String toString() {
        return "UserUpdateResponseForm [name=" + name + ", first_surname="
                + firstSurname + ", second_surname=" + secondSurname
                + ", birth_date=" + birthDate + ", gender=" + gender + "]";
    }

}
