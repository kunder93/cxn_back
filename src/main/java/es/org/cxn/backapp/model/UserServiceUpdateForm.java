/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Default implementation of the user form user by service.
 *
 * @author Santiago Paz
 */
public class UserServiceUpdateForm implements Serializable {

  /**
   * Serialization ID.
   */
  private static final long serialVersionUID = 1328776449450853491L;

  /**
   * Name of the user.
   */
  private String name = "";

  /**
   * Name of the user.
   */
  private String firstSurname = "";

  /**
   * Name of the user.
   */
  private String secondSurname = "";

  /**
   * Name of the user.
   */
  private LocalDate birthDate;

  /**
   * Name of the user.
   */
  private String gender = "";

  /**
   * Constructs an example entity.
   */
  public UserServiceUpdateForm() {
    super();
  }

  /**
   * Constructor with all field given.
   *
   * @param nameValue          the name field.
   * @param firstSurnameValue  the first surname field.
   * @param secondSurnameValue the second surname field.
   * @param birthDateValue     the birth date field.
   * @param genderValue        the gender field.
   */
  public UserServiceUpdateForm(
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
   * Returns the name.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the first surname.
   *
   * @return the first surname.
   */
  public String getFirstSurname() {
    return firstSurname;
  }

  /**
   * Returns the second surname.
   *
   * @return the second surname.
   */
  public String getSecondSurname() {
    return secondSurname;
  }

  /**
   * Returns the birth date.
   *
   * @return the birth date.
   */
  public LocalDate getBirthDate() {
    return birthDate;
  }

  /**
   * Returns the gender.
   *
   * @return the gender.
   */
  public String getGender() {
    return gender;
  }

  /**
   * Set name field.
   *
   * @param value the name.
   */
  public void setName(final String value) {
    name = checkNotNull(value, "Received a null pointer as name");
  }

  /**
   * Set first surname field.
   *
   * @param value the first surname.
   */
  public void setFirstSurname(final String value) {
    firstSurname =
          checkNotNull(value, "Received a null pointer as first surname");
  }

  /**
   * Set second surname field.
   *
   * @param value the second surname.
   */
  public void setSecondSurname(final String value) {
    secondSurname =
          checkNotNull(value, "Received a null pointer as second surname");
  }

  /**
   * Set birth date field.
   *
   * @param value the birth date.
   */
  public void setBirthDate(final LocalDate value) {
    birthDate = checkNotNull(value, "Received a null pointer as first surname");
  }

  /**
   * Set gender field.
   *
   * @param value the gender.
   */
  public void setGender(final String value) {
    gender = checkNotNull(value, "Received a null pointer as gender");
  }

  /**
   * The hash code method.
   */
  @Override
  public int hashCode() {
    return Objects.hash(birthDate, firstSurname, gender, name, secondSurname);
  }

  /**
   * The equals method.
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
    var other = (UserServiceUpdateForm) obj;
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
    return "UserForm [name=" + name + ", first_surname=" + firstSurname
          + ", second_surname=" + secondSurname + ", birth_date=" + birthDate
          + ", gender=" + gender + ", password=" + "]";
  }

}
