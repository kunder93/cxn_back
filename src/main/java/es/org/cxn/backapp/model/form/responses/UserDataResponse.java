/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
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

package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * Represents the form used for response authenticating user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller, and mapping all the values from the form. Each of field in the
 * DTO matches a field in the form.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez.
 */
@Data
public final class UserDataResponse implements Serializable {

  /**
   * UID Serial version.
   */
  private static final long serialVersionUID = -6424289331689999079L;

  /**
   * User dni.
   */
  private final String dni;
  /**
   * User name.
   */
  private final String name;
  /**
   * User first surname.
   */
  private final String firstSurname;
  /**
   * User second surname.
   */
  private final String secondSurname;
  /**
   * User gender.
   */
  private final String gender;
  /**
   * User birth date.
   */
  private final LocalDate birthDate;
  /**
   * User email.
   */
  private final String email;
  /**
   * Kind of member.
   */
  private final UserType kindMember;

  /**
   * List of rolenames owned by one user.
   */
  private Set<UserRoleName> userRoles;

  /**
   * Constructs a DTO for user data response with fields values.
   *
   * @param user the user entity with data.
   */
  public UserDataResponse(final UserEntity user) {
    super();
    dni = user.getDni();
    name = user.getName();
    firstSurname = user.getFirstSurname();
    secondSurname = user.getSecondSurname();
    gender = user.getGender();
    birthDate = user.getBirthDate();
    email = user.getEmail();
    kindMember = user.getKindMember();
    userRoles = new HashSet<>();

    user.getRoles().forEach(
          (PersistentRoleEntity role) -> userRoles.add(role.getName())
    );
  }

  /**
   * @return The dni.
   */
  public String getDni() {
    return dni;
  }

  /**
   * Get user name.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Get user first surname.
   *
   * @return the firstSurname.
   */
  public String getFirstSurname() {
    return firstSurname;
  }

  /**
   * Get user second surname.
   *
   * @return the secondSurname.
   */
  public String getSecondSurname() {
    return secondSurname;
  }

  /**
   * Get user gender.
   *
   * @return the gender.
   */
  public String getGender() {
    return gender;
  }

  /**
   * Get user birth date.
   *
   * @return the birthDate.
   */
  public LocalDate getBirthDate() {
    return birthDate;
  }

  /**
   * Get user email.
   *
   * @return the email.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get list with user role names owned by user.
   *
   * @return the userRoles.
   */
  public Set<UserRoleName> getUserRoles() {
    return new HashSet<>(userRoles);
  }

}
