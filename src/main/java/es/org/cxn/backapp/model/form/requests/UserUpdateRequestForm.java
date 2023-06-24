
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents the request form used by controller for update user data.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez.
 */
public final class UserUpdateRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 5587292614430493384L;

  /**
   * User name field.
   */
  @NotEmpty
  private String name;

  /**
   * User first surname field.
   */
  @NotEmpty
  private String firstSurname;

  /**
   * User second surname field.
   */
  @NotEmpty
  private String secondSurname;

  /**
   * User birth date field.
   */
  @NotNull
  @Past
  private LocalDate birthDate;

  /**
   * User gender field.
   */
  @NotEmpty
  private String gender;

  /**
   * Constructs a DTO for UserUpdateRequestForm.
   */
  public UserUpdateRequestForm() {
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
  public UserUpdateRequestForm(
        final String nameValue, final String firstSurnameValue,
        final String secondSurnameValue, final LocalDate birthDateValue,
        final String genderValue
  ) {
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
   * @return the user first surname.
   */
  public String getFirstSurname() {
    return firstSurname;
  }

  /**
   * Getter for second surname fied.
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
   * @param value the name to set.
   */
  public void setName(final String value) {
    this.name = value;
  }

  /**
   * Setter for first surname field.
   *
   * @param value the first surname to set.
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
    return Objects.hash(birthDate, firstSurname, gender, name, secondSurname);
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
    var other = (UserUpdateRequestForm) obj;
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
    return "UserUpdateRequestForm [name=" + name + ", firstSurname="
          + firstSurname + ", secondSurname=" + secondSurname + ", birthDate="
          + birthDate + ", gender=" + gender + ", password=" + "]";
  }

}
