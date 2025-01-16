
package es.org.cxn.backapp.test.unit.services;

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.org.cxn.backapp.exceptions.RoleNameExistsException;
import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.service.impl.DefaultRoleService;

/**
 * Unit test class for {@link DefaultRoleService}.
 * <p>
 * This class tests the various methods in the {@link DefaultRoleService} class
 * to ensure they function correctly when interacting with the
 * {@link RoleEntityRepository}.
 * <p>
 * The {@link MockitoExtension} is used to initialize mocks and inject them into
 * the {@link DefaultRoleService} instance.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    /**
     * A sample role name used in test cases.
     * <p>
     * This {@link UserRoleName} is initialized in the {@link #setUp()} method and
     * represents the name of the role being tested.
     * </p>
     */

    private static final UserRoleName ROLE_NAME = UserRoleName.ROLE_SOCIO;

    /**
     * The role identifier used in test.
     */
    private static final int ROLE_ID = 1;

    /**
     * Mocked repository for handling role entities.
     * <p>
     * This mock is used to simulate the behavior of the
     * {@link RoleEntityRepository} without interacting with the actual database.
     * </p>
     */
    @Mock
    private RoleEntityRepository roleRepository;

    /**
     * The service under test.
     * <p>
     * This instance of {@link DefaultRoleService} is injected with the mock
     * {@link RoleEntityRepository} to test the service's methods.
     * </p>
     */
    @InjectMocks
    private DefaultRoleService roleService;
    /**
     * A sample persistent role entity used in test cases.
     * <p>
     * This role entity is initialized in the {@link #setUp()} method and used
     * across multiple test cases to represent a role in the system.
     * </p>
     */
    private PersistentRoleEntity role;

    @BeforeEach
    void setUp() {
        role = new PersistentRoleEntity();
        role.setIdentifier(ROLE_ID);
        role.setName(ROLE_NAME);
    }

    /**
     * Unit test for {@link DefaultRoleService#add(UserRoleName)}.
     * <p>
     * Verifies that a role can be successfully added when it does not already
     * exist. Ensures that the result is non-null and the role name matches the
     * expected value.
     *
     * @throws RoleNameExistsException if the role already exists
     */
    @Test
    void testAddRoleSuccess() throws RoleNameExistsException {
        when(roleRepository.existsByName(ROLE_NAME)).thenReturn(false);
        when(roleRepository.save(any(PersistentRoleEntity.class))).thenReturn(role);

        var result = roleService.add(ROLE_NAME);

        assertNotNull(result, "Expected non-null role after adding");
        assertEquals(ROLE_NAME, result.getName(), "Expected role name to match");
    }

    /**
     * Unit test for {@link DefaultRoleService#add(UserRoleName)}.
     * <p>
     * Verifies that an exception is thrown when attempting to add a role that
     * already exists. Ensures that the thrown exception is of type
     * {@link RoleNameExistsException} and that the role name in the exception
     * matches the expected value.
     */
    @Test
    void testAddRoleThrowsExceptionWhenRoleExists() {
        when(roleRepository.existsByName(ROLE_NAME)).thenReturn(true);

        var thrown = assertThrows(RoleNameExistsException.class, () -> roleService.add(ROLE_NAME),
                "Expected add() to throw RoleNameExistsException, but it didn't");

        assertEquals(ROLE_NAME, thrown.getRoleName(), "Expected exception role name to match");
    }

    /**
     * Unit test for {@link DefaultRoleService#findById(Integer)}.
     * <p>
     * Verifies that when a role is not found by its ID, a non-null result is still
     * returned with an ID of -1, indicating that the role was not found.
     */
    @Test
    void testFindByIdReturnsEmptyRoleWhenNotFound() {
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        var result = roleService.findById(1);

        assertNotNull(result, "Expected non-null result even when role not found");
        assertEquals(-1, result.getIdentifier(), "Expected role ID to be -1 when not found");
    }

    /**
     * Unit test for {@link DefaultRoleService#findById(Integer)}.
     * <p>
     * Verifies that a role can be successfully retrieved by its ID. Ensures that
     * the result is non-null, the ID matches the expected value, and the role name
     * is correct.
     */
    @Test
    void testFindByIdSuccess() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        var result = roleService.findById(1);

        assertNotNull(result, "Expected non-null role when found by ID");
        assertEquals(1, result.getIdentifier(), "Expected role ID to be 1");
        assertEquals(ROLE_NAME, result.getName(), "Expected role name to match");
    }

    /**
     * Unit test for {@link DefaultRoleService#findByName(UserRoleName)}.
     * <p>
     * Verifies that a role can be successfully retrieved by its name. Ensures that
     * the result is non-null and the role name matches the expected value.
     *
     * @throws RoleNameNotFoundException if the role name is not found
     */
    @Test
    void testFindByNameSuccess() throws RoleNameNotFoundException {
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Optional.of(role));

        var result = roleService.findByName(ROLE_NAME);

        assertNotNull(result, "Expected non-null role when found by name");
        assertEquals(ROLE_NAME, result.getName(), "Expected role name to match");
    }

    /**
     * Unit test for {@link DefaultRoleService#findByName(UserRoleName)}.
     * <p>
     * Verifies that an exception is thrown when attempting to find a role by name
     * that does not exist. Ensures that the thrown exception is of type
     * {@link RoleNameNotFoundException} and that the role name in the exception
     * matches the expected value.
     */
    @Test
    void testFindByNameThrowsExceptionWhenNotFound() {
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Optional.empty());

        var thrown = assertThrows(RoleNameNotFoundException.class, () -> roleService.findByName(ROLE_NAME),
                "Expected findByName() to throw RoleNameNotFoundException," + " but it didn't");

        assertEquals(ROLE_NAME, thrown.getRoleName(), "Expected exception role name to match");
    }

    /**
     * Unit test for {@link DefaultRoleService#getAllRoles()}.
     * <p>
     * Verifies that all roles can be successfully retrieved. Ensures that the
     * result is a non-null list containing the expected number of roles, and that
     * the roles have the correct names.
     */
    @Test
    void testGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));

        var result = roleService.getAllRoles();

        assertNotNull(result, "Expected non-null role list");
        assertEquals(1, result.size(), "Expected role list size to be 1");
        assertEquals(ROLE_NAME, result.getFirst().getName(), "Expected role name to match");
    }

    /**
     * Unit test for {@link DefaultRoleService#remove(UserRoleName)}.
     * <p>
     * Verifies that a role can be successfully removed when it exists. Ensures that
     * the remove operation does not throw an exception and that the repository's
     * delete method is called exactly once.
     */
    @Test
    void testRemoveRoleSuccess() {
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Optional.of(role));

        assertDoesNotThrow(() -> roleService.remove(ROLE_NAME),
                "Expected remove() to execute successfully without throwing an " + "exception for an existing role");

        verify(roleRepository, times(1)).delete(role);
    }

    /**
     * Unit test for {@link DefaultRoleService#remove(UserRoleName)}.
     * <p>
     * Verifies that an exception is thrown when attempting to remove a role that
     * does not exist. Ensures that the thrown exception is of type
     * {@link RoleNameNotFoundException} and that the role name in the exception
     * matches the expected value.
     */
    @Test
    void testRemoveRoleThrowsExceptionWhenNotFound() {
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Optional.empty());

        var thrown = assertThrows(RoleNameNotFoundException.class, () -> roleService.remove(ROLE_NAME),
                "Expected remove() to throw RoleNameNotFoundException, but it didn't");

        assertEquals(ROLE_NAME, thrown.getRoleName(), "Expected exception role name to match");
    }
}
