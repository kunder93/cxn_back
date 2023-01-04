/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.service;

import java.time.LocalDate;

import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.exceptions.UserEmailExistsExeption;
import es.org.cxn.backapp.exceptions.UserEmailNotFoundException;
import es.org.cxn.backapp.exceptions.UserIdNotFoundException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserServiceUpdateForm;

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
     * Creates a new entity.
     *
     * @param nameValue          The user name.
     * @param firstSurnameValue  The user first surname.
     * @param secondSurnameValue The user second surname.
     * @param birthDateValue     The user birth date.
     * @param genderValue        The user gender.
     * @param passwordValue      The user password.
     * @param emailValue         The user email.
     * @return The User entity persisted.
     * @throws UserEmailExistsExeption when user with provided email already
     *                                 exists.
     */
    UserEntity add(
            String nameValue, String firstSurnameValue,
            String secondSurnameValue, LocalDate birthDateValue,
            String genderValue, String passwordValue, String emailValue
    ) throws UserEmailExistsExeption;

    /**
     * Returns an entity with the given identifier (id).
     * <p>
     * If no instance exists with that id then an exception is thrown.
     *
     * @param identifier The user identifier or id.
     * @return the user entity for the given id.
     * @throws UserIdNotFoundException when user with provided identifier not
     *                                 found {@link UserIdNotFoundException}.
     */
    UserEntity findById(Integer identifier) throws UserIdNotFoundException;

    /**
     * Returns an entity with the given email.
     *
     * @param email email of the user to find.
     * @return the user for the given email.
     * @throws UserEmailNotFoundException when user with email no exists.
     */
    UserEntity findByEmail(String email) throws UserEmailNotFoundException;

    /**
     * Removes an user from persistence.
     *
     * @param email email of the user to remove.
     *
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found.
     */
    void remove(String email) throws UserEmailNotFoundException;

    /**
     * Updates an existing user.
     *
     * @param userForm  user information to update.
     * @param userEmail User unique email for locate it into database.
     *
     * @return the persisted user entity.
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found.
     */
    UserEntity update(UserServiceUpdateForm userForm, String userEmail)
            throws UserEmailNotFoundException;

    /**
     * Add role to an existing user.
     *
     * @param email    user unique email acting as identifier.
     * @param roleName Name of the role which add to user, must exists.
     * @return UserEntity with role added.
     * @throws UserEmailNotFoundException when an user with given email no
     *                                    exists.
     * @throws RoleNameNotFoundException  when an role with given name no
     *                                    exists.
     */
    UserEntity addRole(String email, String roleName)
            throws UserEmailNotFoundException, RoleNameNotFoundException;

    /**
     * Remove role from user.
     *
     * @param userEmail The email for locate user.
     * @param roleName  The role name for locate role entity.
     * @return User entity with role removed from role list @see UserEntity.
     * @throws UserEmailNotFoundException When user with provided email not
     *                                    found.
     * @throws RoleNameNotFoundException  When role with provided name not found
     *                                    or user not have role.
     */
    UserEntity removeRole(String userEmail, String roleName)
            throws UserEmailNotFoundException, RoleNameNotFoundException;

}
