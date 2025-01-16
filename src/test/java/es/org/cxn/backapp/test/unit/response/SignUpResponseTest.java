
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

/**
 * Unit tests for the SignUpResponse class. This test class validates the
 * behavior and functionality of the SignUpResponse object, ensuring it meets
 * the expected requirements.
 */
class SignUpResponseTest {

    /**
     * Constant representing the user's date of birth. Used in test cases to verify
     * birth date handling.
     */
    private static final LocalDate USER_BIRTH_DATE = LocalDate.of(1990, 1, 1);

    /**
     * Constant representing the user's DNI (Documento Nacional de Identidad). Used
     * in test cases to validate DNI handling.
     */
    private static final String USER_DNI = "12345678";

    /**
     * Constant representing the user's name. Used in test cases to verify name
     * handling.
     */
    private static final String USER_NAME = "John";

    /**
     * Constant representing the user's first surname. Used in test cases to verify
     * first surname handling.
     */
    private static final String USER_FIRST_SURNAME = "Doe";

    /**
     * Constant representing the user's second surname. Used in test cases to verify
     * second surname handling.
     */
    private static final String USER_SECOND_SURNAME = "Smith";

    /**
     * Constant representing the user's email address. Used in test cases to
     * validate email handling.
     */
    private static final String USER_EMAIL = "john.doe@example.com";

    /**
     * Constant representing the user's gender. Used in test cases to validate
     * gender handling.
     */
    private static final String USER_GENDER = "Male";

    @Test
    void testConstructor() {
        // Arrange
        Set<UserRoleName> roles = Set.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);

        UserType kindMember = UserType.SOCIO_NUMERO;

        // Act
        SignUpResponseForm form = new SignUpResponseForm(USER_DNI, USER_NAME, USER_FIRST_SURNAME, USER_SECOND_SURNAME,
                USER_BIRTH_DATE, USER_GENDER, USER_EMAIL, kindMember, roles);

        // Assert
        assertEquals(USER_DNI, form.dni());
        assertEquals(USER_NAME, form.name());
        assertEquals(USER_FIRST_SURNAME, form.firstSurname());
        assertEquals(USER_SECOND_SURNAME, form.secondSurname());
        assertEquals(USER_BIRTH_DATE, form.birthDate());
        assertEquals(USER_GENDER, form.gender());
        assertEquals(USER_EMAIL, form.email());
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
        when(userEntity.getDni()).thenReturn(USER_DNI);
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

        when(userEntity.getDni()).thenReturn(USER_DNI);
        when(userEntity.getProfile()).thenReturn(userProfile);
        when(userProfile.getName()).thenReturn(USER_NAME);
        when(userProfile.getFirstSurname()).thenReturn(USER_FIRST_SURNAME);
        when(userProfile.getSecondSurname()).thenReturn(USER_SECOND_SURNAME);
        when(userProfile.getBirthDate()).thenReturn(USER_BIRTH_DATE);
        when(userProfile.getGender()).thenReturn(USER_GENDER);
        when(userEntity.getEmail()).thenReturn(USER_EMAIL);
        when(userEntity.getKindMember()).thenReturn(UserType.SOCIO_NUMERO);
        when(userEntity.getRoles()).thenReturn(roleEntities);

        // Mock role name conversion
        when(roleEntityMock.getName()).thenReturn(UserRoleName.ROLE_ADMIN);

        // Act
        SignUpResponseForm form = SignUpResponseForm.fromEntity(userEntity);

        // Assert
        assertEquals(USER_DNI, form.dni());
        assertEquals(USER_NAME, form.name());
        assertEquals(USER_FIRST_SURNAME, form.firstSurname());
        assertEquals(USER_SECOND_SURNAME, form.secondSurname());
        assertEquals(USER_BIRTH_DATE, form.birthDate());
        assertEquals(USER_GENDER, form.gender());
        assertEquals(USER_EMAIL, form.email());
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
        SignUpResponseForm form = new SignUpResponseForm(USER_DNI, USER_NAME, USER_FIRST_SURNAME, USER_SECOND_SURNAME,
                USER_BIRTH_DATE, USER_GENDER, USER_EMAIL, UserType.SOCIO_NUMERO, roles);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> form.userRoles().clear(),
                "userRoles should be immutable");
    }

}
