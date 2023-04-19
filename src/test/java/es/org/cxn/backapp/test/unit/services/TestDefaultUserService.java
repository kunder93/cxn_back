/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
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

package es.org.cxn.backapp.test.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserServiceUpdateForm;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.DefaultUserService;
import es.org.cxn.backapp.service.UserService;

/**
 * Unit tests for {@link DefaultUserService}.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz
 */
@SpringBootTest(
        classes = { UserEntityRepository.class, RoleEntityRepository.class,
                UserService.class, DefaultUserService.class }
)
final class TestDefaultUserService {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private RoleEntityRepository roleEntityRepository;

    LocalDate date = LocalDate.now();
    String userName = "fake user Name";
    String firstSurname = "fake first surname";
    String secondSurname = "fake second surname";
    String email = "email@test.es";
    String gender = "male";
    Integer userIdentifier = Integer.valueOf(99);
    String userDni = "32721859N";
    String roleName = "roleName";
    PersistentAddressEntity address = new PersistentAddressEntity();

    /**
     * Sets up the validator for the tests.
     */
    @BeforeEach
    public final void setUpValidator() {
    }

    /**
     * Default constructor.
     */
    public TestDefaultUserService() {
        super();
    }

    /**
     * Verifies that service throw exception when cannot find user with provided
     * id
     *
     * @throws UserIdNotFoundException If not user avaliable with provided id
     */
    @DisplayName("Find user by dni not found user throw exception")
    @Test
    final void testFindUserByDniNotFound() {
        final Optional<PersistentUserEntity> userOptional = Optional.empty();
        Mockito.when(userEntityRepository.findByDni(userDni))
                .thenReturn(userOptional);
        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.findByDni(userDni);
        }, "User id not found");
    }

    /**
     * Verifies that service find user by provided id
     *
     * @throws UserIdNotFoundException If not user avaliable with provided id
     */
    @DisplayName("Find user by id and check return value")
    @Test
    final void testFindUserByDni() {
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        final Optional<PersistentUserEntity> userOptional = Optional
                .of(userForMock);
        Mockito.when(userEntityRepository.findByDni(userDni))
                .thenReturn(userOptional);
        UserEntity result = null;
        try {
            result = userService.findByDni(userDni);
        } catch (UserServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Assertions.assertNotNull(result, "user not null");
        Assertions.assertEquals(userForMock, result, "user equals");
        Mockito.verify(userEntityRepository, times(1)).findByDni(userDni);
    }

    /**
     * Verifies that service find user by provided email
     *
     * @throws UserEmailNotFoundException If not user available with provided
     *                                    email
     */
    @DisplayName("Find user by email and check return value")
    @Test
    final void testFindUserByEmail() {
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        final Optional<PersistentUserEntity> userOptional = Optional
                .of(userForMock);
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        UserEntity result = null;
        try {
            result = userService.findByEmail(email);
        } catch (UserServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Assertions.assertNotNull(result, "user not null");
        Assertions.assertEquals(userName, result.getName(), "user name check");
        Assertions.assertEquals(
                firstSurname, result.getFirstSurname(),
                "user first surname check"
        );
        Assertions.assertEquals(
                secondSurname, result.getSecondSurname(),
                "user second surname check"
        );
        Assertions
                .assertEquals(gender, result.getGender(), "user gender check");
        Assertions.assertEquals(
                date, result.getBirthDate(), "user birth date check"
        );
        Assertions.assertEquals(email, result.getEmail(), "user email check");
        Mockito.verify(userEntityRepository, times(1)).findByEmail(email);
    }

    /**
     * Verifies that service throw exception when cannot find user with provided
     * email
     *
     * @throws UserEmailNotFoundException If not user available with provided
     *                                    email
     */
    @DisplayName("Find user by email not found user throw exception")
    @Test
    final void testFindUserByEmailNotFound() {
        final Optional<PersistentUserEntity> userOptional = Optional.empty();
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.findByEmail(email);
        }, "User email not found");
    }

    /**
     * Verifies that service returns user with provided data
     *
     * @throws UserEmailExistsExeption if adding user email already exists
     */
    @DisplayName("Add user and check return value")
    @Test
    final void testAddUserReturnUser() {
        final Optional<PersistentUserEntity> emptyUserOptional = java.util.Optional
                .empty();
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(emptyUserOptional);
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        Mockito.when(userEntityRepository.save(any(PersistentUserEntity.class)))
                .thenReturn(userForMock);
        UserEntity user = null;
        try {
            user = userService.add(
                    userDni, userName, firstSurname, secondSurname, date,
                    gender, "password", email
            );
        } catch (UserServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assertions.assertNotNull(user, "user not null");
        Assertions.assertEquals(userName, user.getName(), "user name check");
        Assertions.assertEquals(
                firstSurname, user.getFirstSurname(), "user first surname check"
        );
        Assertions.assertEquals(
                secondSurname, user.getSecondSurname(),
                "user second surname check"
        );
        Assertions.assertEquals(gender, user.getGender(), "user gender check");
        Assertions.assertEquals(
                date, user.getBirthDate(), "user birth date check"
        );
        Assertions.assertEquals(email, user.getEmail(), "user email check");
        Mockito.verify(userEntityRepository, times(1))
                .save(any(PersistentUserEntity.class));
    }

    /**
     * Verifies that service returns exception when add user with existing email
     *
     * @throws UserEmailExistsExeption if adding user email already exists
     */
    @DisplayName("Add user with existing email throw exception")
    @Test
    final void testAddUsersSameEmailThrowException() {
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        final Optional<PersistentUserEntity> userOptional = Optional
                .of(userForMock);
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.add(
                    userDni, "name", "first_surname", "second_surname", date,
                    "male", "password", email
            );
        }, "Email already exists exception");
    }

    /**
     * Verifies that service returns user with added role
     *
     * @throws RoleNameNotFoundException  when role with provided name not found
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Add role to user and check return value")
    @Test
    final void testAddRoleToUser() {
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        final Optional<PersistentUserEntity> userOptional = Optional
                .of(userForMock);

        var userForMockWithRole = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        var role = new PersistentRoleEntity();
        role.setName(roleName);
        userForMockWithRole.addRole(role);

        final Optional<PersistentRoleEntity> roleOptional = Optional.of(role);

        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        Mockito.when(roleEntityRepository.findByName(roleName))
                .thenReturn(roleOptional);
        Mockito.when(userEntityRepository.save(any(PersistentUserEntity.class)))
                .thenReturn(userForMockWithRole);
        UserEntity user = null;
        try {
            user = userService.addRole(email, roleName);
        } catch (UserServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Assertions.assertNotNull(user, "user not null");
        Assertions.assertEquals(userName, user.getName(), "user name check");
        Assertions.assertEquals(
                firstSurname, user.getFirstSurname(), "user first surname check"
        );
        Assertions.assertEquals(
                secondSurname, user.getSecondSurname(),
                "user second surname check"
        );
        Assertions.assertEquals(gender, user.getGender(), "user gender check");
        Assertions.assertEquals(
                date, user.getBirthDate(), "user birth date check"
        );
        Assertions.assertEquals(email, user.getEmail(), "user email check");
        Assertions.assertTrue(
                user.getRoles().contains(role), "user contains role"
        );
        Mockito.verify(userEntityRepository, times(1))
                .save(any(PersistentUserEntity.class));
    }

    /**
     * Verifies that service remove role from user with added role
     *
     * @throws RoleNameNotFoundException  when role with provided name not found
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Remove role from user and check return value")
    @Test
    final void testRemoveRoleFromUserEmailNameNotFound() {
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        var role = new PersistentRoleEntity();
        role.setName(roleName);
        Mockito.when(roleEntityRepository.findByName(roleName))
                .thenReturn(Optional.of(role));

        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.removeRole(email, roleName);
        }, "User email not found");

        final var userEntity = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(Optional.of(userEntity));
        Mockito.when(roleEntityRepository.findByName(roleName))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.removeRole(email, roleName);
        }, "Role name not found");

    }

    /**
     * Verifies that service remove role from user with added role
     *
     * @throws RoleNameNotFoundException  when role with provided name not found
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Remove role from user who no have this role")
    @Test
    final void testRemoveRoleFromUserNoHaveRole() {
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        var roles = new HashSet<PersistentRoleEntity>();
        var role = new PersistentRoleEntity();
        role.setName(roleName);
        roles.add(role);
        userForMock.setRoles(roles);
        final Optional<PersistentUserEntity> userOptional = Optional
                .of(userForMock);

        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        var diferentRoleName = "ROLEE";
        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.removeRole(email, diferentRoleName);
        }, "Role name not found");

    }

    /**
     * Verifies that service throw exceptions when user email or role name not
     * found
     *
     * @throws RoleNameNotFoundException  when role with provided name not found
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Add user and check return value")
    @Test
    final void testAddRoleToUserEmailRoleNameNotFound() {
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        Optional<PersistentUserEntity> userOptional = Optional.empty();

        var role = new PersistentRoleEntity();
        role.setName(roleName);

        Optional<PersistentRoleEntity> roleOptional = Optional.of(role);

        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        Mockito.when(roleEntityRepository.findByName(roleName))
                .thenReturn(roleOptional);

        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRole(email, roleName);
        }, "User email not found");

        userOptional = Optional.of(userForMock);
        roleOptional = Optional.empty();

        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        Mockito.when(roleEntityRepository.findByName(roleName))
                .thenReturn(roleOptional);

        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.addRole(email, roleName);
        }, "Role name not found");
    }

    /**
     * Verifies that service removing user call for repository.remove
     *
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Remove user check call to repository")
    @Test
    final void testRemoveUser() {
        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        final Optional<PersistentUserEntity> userOptional = Optional
                .of(userForMock);

        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);
        Mockito.doNothing().when(userEntityRepository).delete(userForMock);
        userEntityRepository.delete(userForMock);
        Mockito.verify(userEntityRepository, times(1))
                .delete(any(PersistentUserEntity.class));
    }

    /**
     * Verifies that remove user with email which not exists throw exception
     *
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Remove user with not found email")
    @Test
    final void testRemoveUserEmailNotFoundThrowException() {
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.remove(email);
        }, "Email not found");
    }

    /**
     * Verifies that update user with email which not exists throw exception
     *
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Update user not found email")
    @Test
    final void testUpdateUserEmailNotFoundThrowException() {
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        final var form = new UserServiceUpdateForm();
        Assertions.assertThrows(UserServiceException.class, () -> {
            userService.update(form, email);
        }, "Email not found");
    }

    /**
     * Verifies that service update user data
     *
     * @throws UserEmailNotFoundException when user with provided email not
     *                                    found
     *
     */
    @DisplayName("Update user data and check return value")
    @Test
    final void testUpdateUserDataReturnUpdatedUser() {

        final var userForMock = new PersistentUserEntity(
                userDni, userName, firstSurname, secondSurname, date, gender,
                "password", email, address
        );
        final Optional<PersistentUserEntity> userOptional = Optional
                .of(userForMock);
        Mockito.when(userEntityRepository.findByEmail(email))
                .thenReturn(userOptional);

        final var updatedUserName = "updatedUserName";
        final var updatedFirstSurname = "updatedFirstSurname";
        final var updatedSecondSurname = "updatedFirstSurname";
        final var updatedBirthDate = LocalDate.now();
        final var updatedGender = "female";
        var updateUserForm = new UserServiceUpdateForm(
                updatedUserName, updatedFirstSurname, updatedSecondSurname,
                updatedBirthDate, updatedGender
        );
        final var userUpdated = new PersistentUserEntity(
                userDni, updatedUserName, updatedFirstSurname,
                updatedSecondSurname, updatedBirthDate, updatedGender,
                "password", email, address
        );
        Mockito.when(userEntityRepository.save(userUpdated))
                .thenReturn(userUpdated);

        UserEntity returnUser = null;
        try {
            returnUser = userService.update(updateUserForm, email);
        } catch (UserServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assertions.assertNotNull(returnUser);
        Assertions.assertEquals(
                returnUser, userUpdated, "Check if user has been updated"
        );
    }

}
