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

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * A User entity interface.
 *
 * @author Santiago Paz Perez
 */
public interface UserEntity extends Serializable {

  /**
   * Returns the identifier assigned to this user entity.
   *
   * @return the user entity's identifier
   */
  String getDni();

  /**
   * Returns the name of the user entity.
   *
   * @return the user entity's name
   */
  String getName();

  /**
   * Get the user first surname.
   *
   * @return first surname
   */
  String getFirstSurname();

  /**
   * Get the user second surname.
   *
   * @return second surname
   */
  String getSecondSurname();

  /**
   * Get the user birth date.
   *
   * @return the user birth date
   */
  LocalDate getBirthDate();

  /**
   * Get the user gender.
   *
   * @return the user gender.
   */
  String getGender();

  /**
   * Get the user password.
   *
   * @return the user password.
   */
  String getPassword();

  /**
   * Get the user email.
   *
   * @return the user email.
   */
  String getEmail();

  /**
   * Get the user kind member.
   *
   * @return the user kind member.
   */
  UserType getKindMember();

  /**
   * Return user roles.
   *
   * @return the user roles.
   */
  Set<PersistentRoleEntity> getRoles();

  /**
   * Sets the dni aka identifier assigned to this user entity.
   *
   * @param value the identifier for the user entity.
   */
  void setDni(String value);

  /**
   * Changes the name of the user entity.
   *
   * @param name the name to set on the user entity.
   */
  void setName(String name);

  /**
   * Set user first surname.
   *
   * @param firstSurname the first surname.
   */
  void setFirstSurname(String firstSurname);

  /**
   * Set user Second surname.
   *
   * @param secondSurname the second surname.
   */
  void setSecondSurname(String secondSurname);

  /**
   * Set the user birth date.
   *
   * @param birthDate the birth date.
   */
  void setBirthDate(LocalDate birthDate);

  /**
   * Set the user gender.
   *
   * @param gender the user gender.
   */
  void setGender(String gender);

  /**
   * Set the user password.
   *
   * @param password the user password.
   */
  void setPassword(String password);

  /**
   * Set the user email.
   *
   * @param email the user email.
   */
  void setEmail(String email);

  /**
   * Changes the roles of the user.
   *
   * @param roles the roles to set on user.
   */
  void setRoles(Set<PersistentRoleEntity> roles);

  /**
   * Add existing role to user.
   *
   * @param role the role entity to add.
   * @return true if added false if not.
   */
  boolean addRole(PersistentRoleEntity role);

  /**
   * Remove existing role from user.
   *
   * @param role the role entity to remove.
   * @return true if deleted false if not.
   */
  boolean removeRole(PersistentRoleEntity role);

  /**
   * Get the address entity associated.
   *
   * @return The address entity.
   */
  PersistentAddressEntity getAddress();

  /**
   * Associate address entity.
   *
   * @param address The address entity.
   */
  void setAddress(PersistentAddressEntity address);

  /**
   * @param kindMember the kindMember to set
   */
  void setKindMember(UserType kindMember);

}
