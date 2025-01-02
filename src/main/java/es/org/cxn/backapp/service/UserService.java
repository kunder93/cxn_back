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

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.responses.ProfileImageResponse;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;

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
     * Add role to an existing user.
     *
     * @param email        user unique email acting as identifier.
     * @param roleNameList List with role names that user want to add.
     * @return UserEntity with role added.
     * @throws UserServiceException When an role with given name no exists or When
     *                              user with given email that no exist.
     */
    UserEntity changeUserRoles(String email, List<UserRoleName> roleNameList) throws UserServiceException;

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
     * Get user profile image.
     *
     * @param dni The user identifier.
     * @return The profile image response dto.
     * @throws UserServiceException When user with dni not found.
     */
    ProfileImageResponse getProfileImage(String dni) throws UserServiceException;

    /**
     * Removes an user from persistence.
     *
     * @param email email of the user to remove.
     *
     * @throws UserServiceException when user with provided email not found.
     */
    void remove(String email) throws UserServiceException;

    /**
     * Saves the profile image for a user based on the provided URL.
     *
     * @param userDni the DNI (Document Number of Identification) of the user for
     *                whom the profile image is being saved.
     * @param url     the URL where the profile image is located. This can be a
     *                direct URL or a relative path to the image file.
     * @return the saved {@link PersistentUserEntity} object containing the profile
     *         image information.
     * @throws UserServiceException if there is an error while saving the profile
     *                              image for the user.
     */
    PersistentUserEntity saveProfileImage(String userDni, String url) throws UserServiceException;

    /**
     * Saves the profile image for a user based on the uploaded file.
     *
     * @param userDni the DNI (Document Number of Identification) of the user for
     *                whom the profile image is being saved.
     * @param file    the uploaded {@link MultipartFile} representing the profile
     *                image.
     * @return the saved {@link PersistentUserEntity} object containing the profile
     *         image information.
     * @throws IllegalStateException if the method is invoked when the application
     *                               is in an invalid state for this operation.
     * @throws IOException           if an I/O error occurs while saving the file to
     *                               the filesystem.
     * @throws UserServiceException  if there is an error while saving the profile
     *                               image for the user.
     */
    PersistentUserEntity saveProfileImageFile(String userDni, MultipartFile file)
            throws IllegalStateException, IOException, UserServiceException;

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
