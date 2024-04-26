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

package es.org.cxn.backapp.service;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.dto.UserRegistrationDetails;
import es.org.cxn.backapp.service.dto.UserServiceUpdateForm;

import java.util.List;

/**
 * Service for the User entity domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago Paz Perez.
 */
public interface UserService {

  /**
   * Creates new user entity.
   *
   * @param userDetails The dto with user and address data.
   * @return The user entity created.
   * @throws UserServiceException If fails.
   */
  UserEntity add(UserRegistrationDetails userDetails)
        throws UserServiceException;

  /**
   * Returns an entity with the given identifier (dni).
   *
   * <p>
   * If no instance exists with that id then an exception is thrown.
   *
   * @param value The user identifier aka dni.
   * @return the user entity for the given dni.
   * @throws UserServiceException when user with provided identifier not found
   *                              {@link UserServiceException}.
   */
  UserEntity findByDni(String value) throws UserServiceException;

  /**
   * Returns an entity with the given email.
   *
   * @param email email of the user to find.
   * @return the user for the given email.
   * @throws UserServiceException when user with email no exists.
   */
  UserEntity findByEmail(String email) throws UserServiceException;

  /**
   * Removes an user from persistence.
   *
   * @param email email of the user to remove.
   *
   * @throws UserServiceException when user with provided email not found.
   */
  void remove(String email) throws UserServiceException;

  /**
   * Updates an existing user.
   *
   * @param userForm  user information to update.
   * @param userEmail User unique email for locate it into database.
   *
   * @return the persisted user entity.
   * @throws UserServiceException when user with provided email not found.
   */
  UserEntity update(UserServiceUpdateForm userForm, String userEmail)
        throws UserServiceException;

  /**
   * Add role to an existing user.
   *
   * @param email    user unique email acting as identifier.
   * @param roleNameList List with role names that user want to add.
   * @return UserEntity with role added.
   * @throws UserServiceException When an role with given name no exists or When
   *                              user with given email that no exist.
   */
  UserEntity changeUserRoles(
        final String email, final List<UserRoleName> roleNameList
  ) throws UserServiceException;

  /**
   * @return Get list with all users.
   */
  List<UserEntity> getAll();

  /**
   * Change the user kind member.
   *
   * @param userEmail The user email aka identifier.
   * @param newKindMember The new user kind member.
   * @return User entity with change.
   * @throws UserServiceException When cannot change the user kind member.
   */
  UserEntity changeKindMember(String userEmail, UserType newKindMember)
        throws UserServiceException;

}
