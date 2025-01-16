
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
import es.org.cxn.backapp.model.form.responses.UserChangeRoleResponse;

class UserChangeRoleResponseTest {
    @Test
    void testConstructorAndImmutability() {
        // Creating a sample user role list
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);
        UserChangeRoleResponse response = new UserChangeRoleResponse("john_doe", roles);

        // Ensure the user name is correct
        assertEquals("john_doe", response.userName());

        // Ensure the roles list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> response.userRoles().add(UserRoleName.ROLE_PRESIDENTE));

        // Verify the original roles list
        assertEquals(2, response.userRoles().size());
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_SOCIO));
    }

    @Test
    void testConstructorWithEmptyRoles() {
        // Creating an empty roles list
        List<UserRoleName> roles = List.of();
        UserChangeRoleResponse response = new UserChangeRoleResponse("john_doe", roles);

        // Ensure the user name is correct
        assertEquals("john_doe", response.userName());

        // Ensure the roles list is empty
        assertEquals(0, response.userRoles().size());
    }

    @Test
    void testConstructorWithMultipleRoles() {
        // Creating a list of multiple roles
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO,
                UserRoleName.ROLE_PRESIDENTE);
        UserChangeRoleResponse response = new UserChangeRoleResponse("john_doe", roles);

        // Ensure the user name is correct
        assertEquals("john_doe", response.userName());

        // Ensure the roles list is properly set
        assertEquals(3, response.userRoles().size());
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_SOCIO));
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
    }

    @Test
    void testConstructorWithOneRole() {
        // Creating a list with a single role
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN);
        UserChangeRoleResponse response = new UserChangeRoleResponse("john_doe", roles);

        // Ensure the user name is correct
        assertEquals("john_doe", response.userName());

        // Ensure the roles list contains the correct single element
        assertEquals(1, response.userRoles().size());
        assertTrue(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
    }

    @Test
    void testUserRoleModificationDoesNotAffectOriginal() {
        // Creating a sample user role list
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_PRESIDENTE, UserRoleName.ROLE_CANDIDATO_SOCIO);
        UserChangeRoleResponse response = new UserChangeRoleResponse("john_doe", roles);

        // Getting the mutable list
        List<UserRoleName> userRoleCopy = response.userRole();
        userRoleCopy.add(UserRoleName.ROLE_ADMIN);

        // Ensure that modifying the copy doesn't affect the original list
        assertEquals(2, response.userRoles().size());
        assertFalse(response.userRoles().contains(UserRoleName.ROLE_ADMIN));
    }

    @Test
    void testUserRoleReturnsCopy() {
        // Creating a sample user role list
        List<UserRoleName> roles = List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);
        UserChangeRoleResponse response = new UserChangeRoleResponse("john_doe", roles);

        // Getting a mutable copy of the roles list
        List<UserRoleName> copiedList = response.userRole();

        // Modify the copied list
        copiedList.add(UserRoleName.ROLE_PRESIDENTE);

        // Ensure the copied list is modified
        assertEquals(3, copiedList.size());
        assertTrue(copiedList.contains(UserRoleName.ROLE_PRESIDENTE));

        // Ensure the original list is unchanged
        assertEquals(2, response.userRoles().size());
        assertFalse(response.userRoles().contains(UserRoleName.ROLE_PRESIDENTE));
    }
}