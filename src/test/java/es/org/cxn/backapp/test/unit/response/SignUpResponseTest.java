
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;

class SignUpResponseTest {
    @Test
    void testConstructor() {
        // Arrange
        Set<UserRoleName> roles = Set.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);
        String dni = "12345678";
        String name = "John";
        String firstSurname = "Doe";
        String secondSurname = "Smith";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String gender = "Male";
        String email = "john.doe@example.com";
        UserType kindMember = UserType.SOCIO_NUMERO;

        // Act
        SignUpResponseForm form = new SignUpResponseForm(dni, name, firstSurname, secondSurname, birthDate, gender,
                email, kindMember, roles);

        // Assert
        assertEquals(dni, form.dni());
        assertEquals(name, form.name());
        assertEquals(firstSurname, form.firstSurname());
        assertEquals(secondSurname, form.secondSurname());
        assertEquals(birthDate, form.birthDate());
        assertEquals(gender, form.gender());
        assertEquals(email, form.email());
        assertEquals(kindMember, form.kindMember());
        assertEquals(roles, form.userRoles());
        // Verify immutability of userRoles
        assertThrows(UnsupportedOperationException.class, () -> form.userRoles().clear(),
                "userRoles should be immutable");
    }

    @Test
    void testEmptyRolesInEntity() {
        // Arrange
        PersistentUserEntity userEntity = mock(PersistentUserEntity.class);
        when(userEntity.getDni()).thenReturn("12345678");
        when(userEntity.getProfile()).thenReturn(mock(UserProfile.class));
        when(userEntity.getRoles()).thenReturn(Set.of());

        // Act
        SignUpResponseForm form = SignUpResponseForm.fromEntity(userEntity);

        // Assert
        assertNotNull(form.userRoles());
        assertTrue(form.userRoles().isEmpty(), "userRoles should be empty if no roles are present in the entity");
    }

    @Test
    void testFromEntity() {
        // Arrange
        PersistentUserEntity userEntity = mock(PersistentUserEntity.class);
        UserProfile userProfile = mock(UserProfile.class);

        // Mocking roles
        PersistentRoleEntity roleEntityMock = mock(PersistentRoleEntity.class);
        Set<PersistentRoleEntity> roleEntities = Set.of(roleEntityMock);

        when(userEntity.getDni()).thenReturn("12345678");
        when(userEntity.getProfile()).thenReturn(userProfile);
        when(userProfile.getName()).thenReturn("John");
        when(userProfile.getFirstSurname()).thenReturn("Doe");
        when(userProfile.getSecondSurname()).thenReturn("Smith");
        when(userProfile.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));
        when(userProfile.getGender()).thenReturn("Male");
        when(userEntity.getEmail()).thenReturn("john.doe@example.com");
        when(userEntity.getKindMember()).thenReturn(UserType.SOCIO_NUMERO);
        when(userEntity.getRoles()).thenReturn(roleEntities);

        // Mock role name conversion
        when(roleEntityMock.getName()).thenReturn(UserRoleName.ROLE_ADMIN);

        // Act
        SignUpResponseForm form = SignUpResponseForm.fromEntity(userEntity);

        // Assert
        assertEquals("12345678", form.dni());
        assertEquals("John", form.name());
        assertEquals("Doe", form.firstSurname());
        assertEquals("Smith", form.secondSurname());
        assertEquals(LocalDate.of(1990, 1, 1), form.birthDate());
        assertEquals("Male", form.gender());
        assertEquals("john.doe@example.com", form.email());
        assertEquals(UserType.SOCIO_NUMERO, form.kindMember());
        assertTrue(form.userRoles().contains(UserRoleName.ROLE_ADMIN));

        // Verify immutability of userRoles
        assertThrows(UnsupportedOperationException.class, () -> form.userRoles().clear(),
                "userRoles should be immutable");
    }

    @Test
    void testImmutabilityOfUserRoles() {
        // Arrange
        Set<UserRoleName> roles = Set.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);
        SignUpResponseForm form = new SignUpResponseForm("12345678", "John", "Doe", "Smith", LocalDate.of(1990, 1, 1),
                "Male", "john.doe@example.com", UserType.SOCIO_NUMERO, roles);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> form.userRoles().clear(),
                "userRoles should be immutable");
    }

}
