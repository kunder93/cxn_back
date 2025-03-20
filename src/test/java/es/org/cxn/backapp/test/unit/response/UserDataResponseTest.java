
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.responses.user.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.user.address.AddressResponse;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;

/**
 * Unit test class for verifying the behavior of UserDataResponse.
 *
 * This class contains constants representing sample user data and can include
 * test methods to validate the functionality of UserDataResponse or related
 * components. It is designed to ensure that the UserDataResponse behaves as
 * expected under various conditions.
 *
 * <p>
 * Example constants included:
 * <ul>
 * <li>{@link #USER_DNI}: The sample DNI (National Identity Number) for a
 * user.</li>
 * <li>{@link #USER_NAME}: The first name of the user.</li>
 * <li>{@link #USER_FIRST_SURNAME}: The first surname of the user.</li>
 * <li>{@link #USER_SECOND_SURNAME}: The second surname of the user.</li>
 * <li>{@link #USER_GENDER}: The gender of the user.</li>
 * <li>{@link #USER_BIRTH_DATE}: The birth date of the user, represented as a
 * {@link LocalDate}.</li>
 * <li>{@link #USER_EMAIL}: The email address of the user.</li>
 * </ul>
 *
 * <p>
 * These constants can be used to set up user-related test scenarios for
 * verifying the correctness of the application's functionality related to user
 * data processing.
 *
 * @author Santiago Paz
 */
class UserDataResponseTest {

    /**
     * Constant representing the sample DNI (National Identity Number) for a user.
     */
    private static final String USER_DNI = "12345678";

    /**
     * Constant representing the first name of the user.
     */
    private static final String USER_NAME = "John";

    /**
     * Constant representing the first surname of the user.
     */
    private static final String USER_FIRST_SURNAME = "Doe";

    /**
     * Constant representing the second surname of the user.
     */
    private static final String USER_SECOND_SURNAME = "Smith";

    /**
     * Constant representing the gender of the user.
     */
    private static final String USER_GENDER = "Male";

    /**
     * Constant representing the birth date of the user.
     */
    private static final LocalDate USER_BIRTH_DATE = LocalDate.of(1990, 1, 1);

    /**
     * Constant representing the email address of the user.
     */
    private static final String USER_EMAIL = "john.doe@example.com";

    @Test
    void testConstructorWithParameters() {
        // Arrange

        UserType kindMember = UserType.SOCIO_NUMERO;
        AddressResponse address = new AddressResponse("PostalCode", "Apartment", "Building", "Street", "City",
                "Country", "SubCountry");
        Set<UserRoleName> roles = EnumSet.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_PRESIDENTE);
        String assignedTeamName = "TeamChess";
        String preferredTeamName = "TeamChess";
        // Act
        UserDataResponse response = new UserDataResponse(USER_DNI, USER_NAME, USER_FIRST_SURNAME, USER_SECOND_SURNAME,
                USER_GENDER, USER_BIRTH_DATE, USER_EMAIL, kindMember, address, roles, assignedTeamName,
                preferredTeamName);

        // Assert
        assertEquals(USER_DNI, response.dni());
        assertEquals(USER_NAME, response.name());
        assertEquals(USER_FIRST_SURNAME, response.firstSurname());
        assertEquals(USER_SECOND_SURNAME, response.secondSurname());
        assertEquals(USER_GENDER, response.gender());
        assertEquals(USER_BIRTH_DATE, response.birthDate());
        assertEquals(USER_EMAIL, response.email());
        assertEquals(kindMember, response.kindMember());
        assertEquals(address, response.userAddress());
        assertEquals(roles, response.userRoles());
    }

    @Test
    void testConstructorWithUserEntity() {
        // Arrange
        UserEntity user = mock(UserEntity.class);
        UserProfile userProfile = mock(UserProfile.class);
        PersistentAddressEntity addressEntity = mock(PersistentAddressEntity.class);
        PersistentCountryEntity countryEntity = mock(PersistentCountryEntity.class);
        PersistentCountrySubdivisionEntity countrySubdivision = mock(PersistentCountrySubdivisionEntity.class);

        // Mock address and country-related methods
        when(addressEntity.getCountry()).thenReturn(countryEntity);
        when(addressEntity.getCountrySubdivision()).thenReturn(countrySubdivision);

        // Mock the behavior of the UserEntity
        when(user.getDni()).thenReturn(USER_DNI);
        when(user.getProfile()).thenReturn(userProfile);
        when(user.getProfile().getName()).thenReturn(USER_NAME);
        when(userProfile.getFirstSurname()).thenReturn(USER_FIRST_SURNAME);
        when(userProfile.getSecondSurname()).thenReturn(USER_SECOND_SURNAME);
        when(userProfile.getGender()).thenReturn(USER_GENDER);
        when(userProfile.getBirthDate()).thenReturn(USER_BIRTH_DATE);
        when(user.getEmail()).thenReturn(USER_EMAIL);
        when(user.getKindMember()).thenReturn(UserType.SOCIO_NUMERO);
        when(user.getAddress()).thenReturn(addressEntity);

        // Mock roles
        PersistentRoleEntity roleEntity = mock(PersistentRoleEntity.class);
        when(roleEntity.getName()).thenReturn(UserRoleName.ROLE_ADMIN); // Return the role name from the mock

        when(user.getRoles()).thenReturn(new HashSet<>(Set.of(roleEntity))); // Mock the roles

        // Act
        UserDataResponse response = new UserDataResponse(user);

        // Assert
        assertEquals(USER_DNI, response.dni());
        assertEquals(USER_NAME, response.name());
        assertEquals(USER_FIRST_SURNAME, response.firstSurname());
        assertEquals(USER_SECOND_SURNAME, response.secondSurname());
        assertEquals(USER_GENDER, response.gender());
        assertEquals(USER_BIRTH_DATE, response.birthDate());
        assertEquals(USER_EMAIL, response.email());
        assertEquals(UserType.SOCIO_NUMERO, response.kindMember());

        // Test AddressResponse
        AddressResponse addressResponse = response.userAddress();
        assertNotNull(addressResponse);

        // Validate that roles are not empty and contain the correct role
        Set<UserRoleName> roles = response.userRoles();
        assertFalse(roles.isEmpty());
        assertTrue(roles.contains(UserRoleName.ROLE_ADMIN));
    }

    @Test
    void testUserRolesDefensiveCopy() {
        // Arrange
        Set<UserRoleName> roles = EnumSet.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);
        String assignedTeamName = "TeamChess";
        String preferredTeamName = "TeamChess";
        UserDataResponse response = new UserDataResponse(USER_DNI, USER_NAME, USER_FIRST_SURNAME, USER_SECOND_SURNAME,
                USER_GENDER, USER_BIRTH_DATE, USER_EMAIL, UserType.SOCIO_NUMERO,
                new AddressResponse("PostalCode", "Apartment", "Building", "Street", "City", "Country", "SubCountry"),
                roles, assignedTeamName, preferredTeamName);

        // Act
        Set<UserRoleName> responseRoles = response.userRoles();
        responseRoles.add(UserRoleName.ROLE_CANDIDATO_SOCIO); // Modify the returned set

        // Assert
        assertTrue(responseRoles.contains(UserRoleName.ROLE_CANDIDATO_SOCIO)); // The modified copy should have the role
        assertFalse(response.userRoles().contains(UserRoleName.ROLE_CANDIDATO_SOCIO)); // The original set should not
                                                                                       // have the role
    }

}
