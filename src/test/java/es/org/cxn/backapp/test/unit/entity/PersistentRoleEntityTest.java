
package es.org.cxn.backapp.test.unit.entity;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;

/**
 * Unit tests for {@link PersistentRoleEntity}.
 *
 * This class contains unit tests to verify the functionality and behavior of
 * the {@link PersistentRoleEntity} class, which represents roles assigned to
 * users in the application.
 */
class PersistentRoleEntityTest {

    /**
     * Instance of {@link PersistentRoleEntity} to be tested. This entity is used to
     * manage the roles associated with users and their permissions.
     */
    private PersistentRoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleEntity = new PersistentRoleEntity();
    }

    @Test
    void testConstructorWithName() {
        PersistentRoleEntity customRole = new PersistentRoleEntity(UserRoleName.ROLE_ADMIN);
        assertEquals(UserRoleName.ROLE_ADMIN, customRole.getName(),
                "Role name should be initialized correctly by the constructor.");
    }

    @Test
    void testDefaultValues() {
        assertNotNull(roleEntity);
        assertEquals(-1, roleEntity.getIdentifier(), "Default identifier should be -1.");
        assertEquals(UserRoleName.ROLE_CANDIDATO_SOCIO, roleEntity.getName(),
                "Default role name should be ROLE_CANDIDATO_SOCIO.");
        assertTrue(roleEntity.getUsers().isEmpty(), "Default users set should be empty.");
    }

    @Test
    void testGetUsersUnmodifiable() {
        Set<PersistentUserEntity> users = new HashSet<>();
        PersistentUserEntity user = new PersistentUserEntity();
        users.add(user);
        roleEntity.setUsers(users);

        Set<PersistentUserEntity> retrievedUsers = roleEntity.getUsers();
        assertThrows(UnsupportedOperationException.class, () -> retrievedUsers.add(new PersistentUserEntity()),
                "Users set should be unmodifiable.");
    }

    @Test
    void testSetAndGetIdentifier() {
        final int roleIdentifier = 100;
        roleEntity.setIdentifier(roleIdentifier);
        assertEquals(roleIdentifier, roleEntity.getIdentifier(), "Identifier should be updated correctly.");
    }

    @Test
    void testSetAndGetName() {
        roleEntity.setName(UserRoleName.ROLE_ADMIN);
        assertEquals(UserRoleName.ROLE_ADMIN, roleEntity.getName(), "Role name should be updated correctly.");
    }

    @Test
    void testSetAndGetUsers() {
        Set<PersistentUserEntity> users = new HashSet<>();
        PersistentUserEntity user1 = new PersistentUserEntity();
        user1.setDni("11111111K");
        PersistentUserEntity user2 = new PersistentUserEntity();
        user2.setDni("22222222O");
        users.add(user1);
        users.add(user2);

        roleEntity.setUsers(users);

        assertEquals(2, roleEntity.getUsers().size(), "Users set should contain the correct number of elements.");
        assertTrue(roleEntity.getUsers().contains(user1), "Users set should contain user1.");
        assertTrue(roleEntity.getUsers().contains(user2), "Users set should contain user2.");
    }

    @Test
    void testToString() {
        roleEntity.setIdentifier(1);
        roleEntity.setName(UserRoleName.ROLE_ADMIN);
        String expectedString = "PersistentRoleEntity(identifier=1, name=ROLE_ADMIN, users=[])";
        assertEquals(expectedString, roleEntity.toString(),
                "toString should return the correct string representation.");
    }
}
