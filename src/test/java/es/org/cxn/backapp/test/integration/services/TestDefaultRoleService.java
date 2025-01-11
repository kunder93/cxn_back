
package es.org.cxn.backapp.test.integration.services;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import es.org.cxn.backapp.exceptions.RoleNameExistsException;
import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.impl.DefaultRoleService;

/**
 * Unit tests for {@link DefaultRoleService}.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 * </p>
 *
 * @author Santiago Paz
 */
@SpringBootTest(classes = { UserEntityRepository.class, RoleEntityRepository.class, RoleService.class,
        DefaultRoleService.class })
@ActiveProfiles("test")
final class TestDefaultRoleService {

    /**
     * The default role ID used in tests.
     * <p>
     * This constant provides a standard role ID that is used across different tests
     * to ensure consistency in the testing scenarios.
     * </p>
     */
    private static final Integer DEFAULT_ROLE_ID = 79;

    /**
     * A different role ID used in tests to check scenarios where IDs differ.
     * <p>
     * This constant is used to verify that the system correctly identifies and
     * handles cases where role IDs are not matching.
     * </p>
     */
    private static final Integer DIFFERENT_ROLE_ID = 77;

    /**
     * The ID of the role used in the tests.
     * <p>
     * This field holds the default ID value assigned to roles for the purpose of
     * testing role-related operations. It is initialized with the value of
     * {@link #DEFAULT_ROLE_ID}.
     * </p>
     */
    private Integer roleId = DEFAULT_ROLE_ID;

    /**
     * The name of the role used in the tests.
     * <p>
     * This field holds the role name value that is used for testing role-related
     * operations. It is initialized with {@link UserRoleName#ROLE_CANDIDATO_SOCIO}.
     * </p>
     */
    private UserRoleName roleName = UserRoleName.ROLE_CANDIDATO_SOCIO;

    /**
     * The {@link RoleService} instance that is injected by Spring for testing.
     * <p>
     * This service is used in the tests to verify the behavior of role-related
     * operations.
     * </p>
     */
    @Autowired
    private RoleService roleService;

    /**
     * A mock instance of {@link RoleEntityRepository} that is used in testing.
     * <p>
     * This mock is created by Spring Boot's test framework to simulate the
     * repository layer and to verify interactions with it without requiring an
     * actual database.
     * </p>
     */
    @MockBean
    private RoleEntityRepository roleEntityRepository;

    /**
     * Default constructor.
     */
    TestDefaultRoleService() {
        super();
    }

    /**
     * Verifies that the service calls repository save if data is correct.
     *
     * @throws RoleNameExistsException when role name already exists
     */
    @DisplayName("Add role with name that not exists persist it")
    @Test
    void testAddRoleNameReturnRole() throws RoleNameExistsException {
        var roleEntity = new PersistentRoleEntity();

        Mockito.when(roleEntityRepository.existsByName(roleName)).thenReturn(false);
        Mockito.when(roleEntityRepository.save(any(PersistentRoleEntity.class))).thenReturn(roleEntity);

        var result = (PersistentRoleEntity) roleService.add(roleName);
        Assertions.assertEquals(result, roleEntity, "persisted is same as provided");
        Mockito.verify(roleEntityRepository, times(1)).save(any(PersistentRoleEntity.class));
    }

    /**
     * Verifies that the service throws an exception when the role name already
     * exists.
     *
     * @throws RoleNameExistsException when role name already exists
     */
    @DisplayName("Add role with name that exists throw exception")
    @Test
    void testAddRoleRoleNameThatExistsThrowException() {
        Mockito.when(roleEntityRepository.existsByName(roleName)).thenReturn(true);
        Assertions.assertThrows(RoleNameExistsException.class, () -> {
            roleService.add(roleName);
        }, "The RoleNameExistsException is caught");
    }

    /**
     * Verifies that the service returns the role when found by id.
     *
     * @throws RoleNameExistsException when role name already exists
     */
    @DisplayName("Find role with id that exists")
    @Test
    void testFindRoleByIdRoleExists() {
        var roleEntity = new PersistentRoleEntity(roleName);
        Optional<PersistentRoleEntity> roleOptional = Optional.of(roleEntity);

        Mockito.when(roleEntityRepository.findById(roleId)).thenReturn(roleOptional);

        var roleResult = roleService.findById(roleId);
        Assertions.assertEquals(roleEntity, roleResult, "Check role found");
        Mockito.verify(roleEntityRepository, times(1)).findById(roleId);
    }

    /**
     * Verifies that the service returns the role when found by name.
     *
     * @throws RoleNameNotFoundException when role name not found
     */
    @DisplayName("Find role with name provided")
    @Test
    void testFindRoleByNameRoleExists() throws RoleNameNotFoundException {
        var roleEntity = new PersistentRoleEntity(roleName);
        Optional<PersistentRoleEntity> roleOptional = Optional.of(roleEntity);

        Mockito.when(roleEntityRepository.findByName(roleName)).thenReturn(roleOptional);

        var roleResult = roleService.findByName(roleName);
        Assertions.assertEquals(roleEntity, roleResult, "Check role found");
        Mockito.verify(roleEntityRepository, times(1)).findByName(roleName);
    }

    /**
     * Verifies that the service throws an exception when role name is not found.
     *
     * @throws RoleNameNotFoundException when role name not found
     */
    @DisplayName("Find role with name that not exists throw exception")
    @Test
    void testFindRoleRoleNameNotExists() {
        Mockito.when(roleEntityRepository.findByName(roleName)).thenReturn(Optional.empty());

        Assertions.assertThrows(RoleNameNotFoundException.class, () -> {
            roleService.findByName(roleName);
        }, "Role name not found throw exception");
    }

    /**
     * Verifies that the service calls repository to remove a role.
     *
     * @throws RoleNameNotFoundException when role name not found
     */
    @DisplayName("Remove role with name that exists")
    @Test
    void testRemoveRoleCallRepository() throws RoleNameNotFoundException {
        var roleEntity = new PersistentRoleEntity(roleName);
        Optional<PersistentRoleEntity> roleOptional = Optional.of(roleEntity);

        Mockito.when(roleEntityRepository.findByName(roleName)).thenReturn(roleOptional);

        Mockito.doNothing().when(roleEntityRepository).delete(roleEntity);
        roleService.remove(roleName);
        Mockito.verify(roleEntityRepository, times(1)).delete(roleEntity);
    }

    /**
     * Verifies that the service throws an exception when role name cannot be
     * removed.
     *
     * @throws RoleNameNotFoundException when role name not found
     */
    @DisplayName("Remove role with name that not exists throw exception")
    @Test
    void testRemoveRoleRoleNameNotExists() {
        Mockito.when(roleEntityRepository.findByName(roleName)).thenReturn(Optional.empty());

        Assertions.assertThrows(RoleNameNotFoundException.class, () -> {
            roleService.remove(roleName);
        }, "Role name not found throw exception");
    }

    /**
     * Verifies that two roles are equal or not.
     */
    @DisplayName("Assert equals")
    @Test
    void testTwoRolesEquals() {
        var roleEntityA = new PersistentRoleEntity();
        roleEntityA.setName(roleName);
        roleEntityA.setIdentifier(roleId);

        var roleEntityB = new PersistentRoleEntity();
        roleEntityB.setName(roleEntityA.getName());
        roleEntityB.setIdentifier(roleEntityA.getIdentifier());

        Assertions.assertEquals(roleEntityA, roleEntityB, "same id and roleName");

        roleEntityA.setIdentifier(DIFFERENT_ROLE_ID);
        Assertions.assertNotEquals(roleEntityA, roleEntityB, "same name but different id");

        PersistentRoleEntity nullEntity = null;
        Assertions.assertNotEquals(nullEntity, roleEntityA, "equals with null is false");
    }
}
