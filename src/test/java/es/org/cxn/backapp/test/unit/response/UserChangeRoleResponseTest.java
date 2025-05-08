
package es.org.cxn.backapp.test.unit.response;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.responses.user.UserChangeRoleResponse;

class UserChangeRoleResponseTest {

    /**
     * The user name.
     */
    private static final String USER_NAME = "john_doe";

    /**
     * Helper method to attempt adding a role, throwing an exception if the list is
     * unmodifiable.
     *
     * @param userRoles the list of user roles
     */
    private void attemptToAddRole(final List<UserRoleName> userRoles) {
        userRoles.add(UserRoleName.ROLE_PRESIDENTE);
    }

    @Test
    void testConstructorAndImmutability() {
        final int sizeRolesList = 2;

        // Creating a sample user role list
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);
        UserChangeRoleResponse response = new UserChangeRoleResponse(USER_NAME, roles);

        // Ensure the user name is correct
        assertEquals(USER_NAME, response.userName());

        // Check immutability of the roles list by validating it outside the lambda
        List<UserRoleName> userRoles = response.userRoles();
        assertThrows(UnsupportedOperationException.class, () -> attemptToAddRole(userRoles));

        // Verify the original roles list
        assertEquals(sizeRolesList, userRoles.size());
        assertTrue(userRoles.contains(UserRoleName.ROLE_ADMIN));
        assertTrue(userRoles.contains(UserRoleName.ROLE_SOCIO));
    }

    @Test
    void testConstructorWithEmptyRoles() {
        final int sizeRolesList = 0;
        // Creating an empty roles list
        List<UserRoleName> roles = List.of();
        UserChangeRoleResponse response = new UserChangeRoleResponse(USER_NAME, roles);

        // Ensure the user name is correct
        assertEquals(USER_NAME, response.userName());

        // Ensure the roles list is empty
        assertEquals(sizeRolesList, response.userRoles().size());
    }

    @Test
    void testConstructorWithMultipleRoles() {
        final int numberOfRoles = 3;
        // Creating a list of multiple roles
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO,
                UserRoleName.ROLE_PRESIDENTE);
        UserChangeRoleResponse response = new UserChangeRoleResponse(USER_NAME, roles);

        // Ensure the user name is correct
        assertEquals(USER_NAME, response.userName());

        // Ensure the roles list is properly set
        assertEquals(numberOfRoles, response.userRoles().size());
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_SOCIO));
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
    }

    @Test
    void testConstructorWithOneRole() {
        final int sizeRolesList = 1;
        // Creating a list with a single role
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN);
        UserChangeRoleResponse response = new UserChangeRoleResponse(USER_NAME, roles);

        // Ensure the user name is correct
        assertEquals(USER_NAME, response.userName());

        // Ensure the roles list contains the correct single element
        assertEquals(sizeRolesList, response.userRoles().size());
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
    }

    @Test
    void testUserRoleModificationDoesNotAffectOriginal() {
        final int sizeNotModifiedRolesList = 2;
        // Creating a sample user role list
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_PRESIDENTE, UserRoleName.ROLE_CANDIDATO_SOCIO);
        UserChangeRoleResponse response = new UserChangeRoleResponse(USER_NAME, roles);

        // Getting the mutable list
        List<UserRoleName> userRoleCopy = response.userRole();
        userRoleCopy.add(UserRoleName.ROLE_ADMIN);

        // Ensure that modifying the copy doesn't affect the original list
        assertEquals(sizeNotModifiedRolesList, response.userRoles().size());
        assertFalse(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
    }

    @Test
    void testUserRoleReturnsCopy() {
        final int sizeNotModifiedRolesList = 2;
        final int sizeModifiedRolesList = 3;
        // Creating a sample user role list
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);
        UserChangeRoleResponse response = new UserChangeRoleResponse(USER_NAME, roles);

        // Getting a mutable copy of the roles list
        List<UserRoleName> copiedList = response.userRole();

        // Modify the copied list
        copiedList.add(UserRoleName.ROLE_PRESIDENTE);

        // Ensure the copied list is modified
        assertEquals(sizeModifiedRolesList, copiedList.size());
        assertTrue(copiedList.contains(UserRoleName.ROLE_PRESIDENTE));

        // Ensure the original list is unchanged
        assertEquals(sizeNotModifiedRolesList, response.userRoles().size());
        assertFalse(response.userRoles().contains(UserRoleName.ROLE_PRESIDENTE));
    }
}
