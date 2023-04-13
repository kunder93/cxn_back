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

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserServiceUpdateForm;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;

/**
 * Default implementation of the {@link UserService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultUserService implements UserService {

    /**
     * User not found message for exception.
     */
    public static final String USER_NOT_FOUND_MESSAGE = "User not found.";
    /**
     * Role not found message for exception.
     */
    public static final String ROLE_NOT_FOUND_MESSAGE = "Role not found.";
    /**
     * Role not found message for exception.
     */
    public static final String USER_EMAIL_EXISTS_MESSAGE = "User email already exists.";

    /**
     * Repository for the user entities handled by the service.
     */
    private final UserEntityRepository userRepository;

    /**
     * Repository for the role entities handled by the service.
     */
    private final RoleEntityRepository roleRepository;

    /**
     * Constructs an entities service with the specified repository.
     *
     * @param userRepo The user repository{@link UserEntityRepository}
     * @param roleRepo The role repository{@link RoleEntityRepository}
     */
    public DefaultUserService(
            final UserEntityRepository userRepo,
            final RoleEntityRepository roleRepo
    ) {
        super();

        this.userRepository = checkNotNull(
                userRepo, "Received a null pointer as repository"
        );
        this.roleRepository = checkNotNull(
                roleRepo, "Received a null pointer as repository"
        );
    }

    @Override
    public UserEntity findById(final Integer identifier)
            throws UserServiceException {
        final Optional<PersistentUserEntity> entity;
        checkNotNull(identifier, "Received a null pointer as identifier");
        entity = userRepository.findById(identifier);
        if (entity.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        return entity.get();
    }

    @Override
    public List<PersistentUserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity add(
            final String name, final String firstSurname,
            final String secondSurname, final LocalDate birthDate,
            final String gender, final String password, final String email
    ) throws UserServiceException {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserServiceException(USER_EMAIL_EXISTS_MESSAGE);
        } else {
            final PersistentUserEntity save;
            save = new PersistentUserEntity();
            save.setName(name);
            save.setFirstSurname(firstSurname);
            save.setSecondSurname(secondSurname);
            save.setGender(gender);
            save.setBirthDate(birthDate);
            save.setPassword(new BCryptPasswordEncoder().encode(password));
            save.setEmail(email);

            return userRepository.save(save);
        }
    }

    @Override
    @Transactional
    public UserEntity addRole(final String email, final String roleName)
            throws UserServiceException {
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        var role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            throw new UserServiceException(ROLE_NOT_FOUND_MESSAGE);
        }
        var userEntity = user.get();
        userEntity.addRole(role.get());
        return userRepository.save(userEntity);

    }

    @Override
    @Transactional
    public UserEntity removeRole(final String email, final String roleName)
            throws UserServiceException {
        var userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        var user = userOptional.get();
        var roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isEmpty()
                || !user.getRoles().contains(roleOptional.get())) {
            throw new UserServiceException(ROLE_NOT_FOUND_MESSAGE);
        }
        user.removeRole(roleOptional.get());
        return userRepository.save(user);

    }

    @Override
    public UserEntity findByEmail(final String email)
            throws UserServiceException {
        checkNotNull(email, "Received a null pointer as identifier");

        var result = userRepository.findByEmail(email);

        if (!result.isPresent()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        return result.get();
    }

    @Override
    public void remove(final String email) throws UserServiceException {
        final Optional<PersistentUserEntity> userOptional;
        userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        userRepository.delete(userOptional.get());
    }

    @Override
    public UserEntity update(
            final UserServiceUpdateForm userForm, final String userEmail
    ) throws UserServiceException {
        Optional<PersistentUserEntity> userOptional;

        userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        PersistentUserEntity userEntity;
        userEntity = userOptional.get();
        userEntity.setName(userForm.getName());
        userEntity.setFirstSurname(userForm.getFirstSurname());
        userEntity.setSecondSurname(userForm.getSecondSurname());
        userEntity.setBirthDate(userForm.getBirthDate());
        userEntity.setGender(userForm.getGender());

        return userRepository.save(userEntity);
    }

}
