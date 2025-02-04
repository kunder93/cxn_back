
package es.org.cxn.backapp.service;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.util.List;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;
import es.org.cxn.backapp.service.exceptions.UserServiceException;

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
     * Changes the user roles for the specified user by promoting them from
     * {@link UserRoleName#ROLE_CANDIDATO_SOCIO} to {@link UserRoleName#ROLE_SOCIO}.
     *
     * @param userDni the dni of the user to be promoted.
     * @return the updated user entity containing only the
     *         {@link UserRoleName#ROLE_SOCIO} role.
     * @throws UserServiceException if the user is not found, has roles other than
     *                              {@link UserRoleName#ROLE_CANDIDATO_SOCIO}, or if
     *                              the roles cannot be changed.
     */
    UserEntity acceptUserAsMember(String userDni) throws UserServiceException;

    /**
     * Creates new user entity.
     *
     * @param userDetails The dto with user and address data.
     * @return The user entity created.
     * @throws UserServiceException If fails.
     */
    UserEntity add(UserRegistrationDetailsDto userDetails) throws UserServiceException;

    /**
     * Change the user kind member.
     *
     * @param userEmail     The user email aka identifier.
     * @param newKindMember The new user kind member.
     * @return User entity with change.
     * @throws UserServiceException When cannot change the user kind member.
     */
    UserEntity changeKindMember(String userEmail, UserType newKindMember) throws UserServiceException;

    /**
     * Change the user email.
     *
     * @param email    The current user email.
     * @param newEmail The new user email.
     * @return The user entity with new email.
     * @throws UserServiceException When user with email not found.
     */
    UserEntity changeUserEmail(String email, String newEmail) throws UserServiceException;

    /**
     * Change the current user password.
     *
     * @param email           The user email.
     * @param newPassword     The user new password.
     * @param currentPassword The user current password.
     * @return The user entity with new password and user data.
     * @throws UserServiceException When user with email not found or password dont
     *                              match.
     */
    UserEntity changeUserPassword(String email, String currentPassword, String newPassword) throws UserServiceException;

    /**
     * Permanently deletes all data associated with a user identified by their
     * email. This operation is irreversible and ensures that the user's data is
     * fully removed from the system.
     *
     * @param userEmail the email address of the user whose data is to be deleted
     *                  (must not be null or empty)
     * @throws IllegalArgumentException if the provided email is invalid or null
     * @throws UserServiceException     if no user exists with the given email
     *                                  address
     */
    void delete(String userEmail) throws UserServiceException;

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
     * Retrieves a list of all users in the system.
     *
     * @return A list containing all {@link UserEntity} objects representing the
     *         users.
     */
    List<UserEntity> getAll();

    /**
     * Unsubscribe an user.
     *
     * @param email The user email
     * @throws UserServiceException When user with provided email not found.
     */
    void unsubscribe(String email) throws UserServiceException;

    /**
     * Updates an existing user.
     *
     * @param userForm  user information to update.
     * @param userEmail User unique email for locate it into database.
     *
     * @return the persisted user entity.
     * @throws UserServiceException when user with provided email not found.
     */
    UserEntity update(UserServiceUpdateDto userForm, String userEmail) throws UserServiceException;

}
