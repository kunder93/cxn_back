
package es.org.cxn.backapp.service.impl;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.org.cxn.backapp.exceptions.RoleNameExistsException;
import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.model.RoleEntity;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.exceptions.UserServiceException;

/**
 * Default implementation of the privilege entity service.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultRoleService implements RoleService {

    /**
     * Role not found message for exception.
     */
    public static final String ROLE_NOT_FOUND_MESSAGE = "Role not found.";

    /**
     * Repository for the role entities handled by the service.
     */
    private final RoleEntityRepository roleRepository;

    /**
     * Repository for the user entities handled by the service.
     */
    private final UserEntityRepository userRepository;

    /**
     * Constructs an entities service with the specified repository.
     *
     * @param roleRepo the repository for the entity instances.
     * @param usrRepo  the repository for user entities.
     */
    public DefaultRoleService(final RoleEntityRepository roleRepo, final UserEntityRepository usrRepo) {
        super();

        roleRepository = Objects.requireNonNull(roleRepo, "Received a null pointer as repository");
        userRepository = Objects.requireNonNull(usrRepo, "Received a null pointer as repository");
    }

    @Override
    public RoleEntity add(final UserRoleName name) throws RoleNameExistsException {
        final PersistentRoleEntity save;
        final var nameNotNull = Objects.requireNonNull(name, "Received a null pointer as name");
        save = new PersistentRoleEntity();
        save.setName(nameNotNull);
        if (roleRepository.existsByName(nameNotNull)) {
            throw new RoleNameExistsException(nameNotNull);
        }

        return roleRepository.save(save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity changeUserRoles(final String email, final List<UserRoleName> roleNameList)
            throws UserServiceException {
        final var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UserServiceException("User with email: " + email + " not found.");
        }
        final var userEntity = userOpt.get();

        final Set<PersistentRoleEntity> rolesSet = new HashSet<>();
        for (final UserRoleName roleName : roleNameList) {
            final var role = roleRepository.findByName(roleName);
            if (role.isEmpty()) {
                throw new UserServiceException(ROLE_NOT_FOUND_MESSAGE);
            }
            rolesSet.add(role.get());

        }
        userEntity.setRoles(rolesSet);
        return userRepository.save(userEntity);
    }

    /**
     * Returns an entity with the given id.
     * <p>
     * If no instance exists with that id then an entity with a negative id is
     * returned.
     *
     * @param identifier identifier of the entity to find
     * @return the entity for the given id
     */
    @Override
    public RoleEntity findById(final Integer identifier) {
        Objects.requireNonNull(identifier, "Received a null pointer as identifier");

        final var entityOpt = roleRepository.findById(identifier);

        // Variable para almacenar el resultado
        final RoleEntity result;

        if (entityOpt.isEmpty()) {
            result = new PersistentRoleEntity();
        } else {
            result = entityOpt.get();
        }

        return result;
    }

    @Override
    public RoleEntity findByName(final UserRoleName name) throws RoleNameNotFoundException {
        Objects.requireNonNull(name, "Received a null pointer as identifier");
        final var entity = roleRepository.findByName(name);
        if (entity.isEmpty()) {
            throw new RoleNameNotFoundException(name);
        }
        return entity.get();
    }

    @Override
    public List<RoleEntity> getAllRoles() {
        final var usersList = roleRepository.findAll();
        return new ArrayList<>(usersList);
    }

    @Override
    public void remove(final UserRoleName name) throws RoleNameNotFoundException {
        final var delete = roleRepository.findByName(name);
        if (delete.isEmpty()) {
            throw new RoleNameNotFoundException(name);
        }
        roleRepository.delete(delete.get());
    }

}
