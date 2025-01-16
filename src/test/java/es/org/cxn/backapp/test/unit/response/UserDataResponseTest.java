
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
import es.org.cxn.backapp.model.form.responses.AddressResponse;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;

class UserDataResponseTest {
    @Test
    void testConstructorWithParameters() {
        // Arrange
        String dni = "12345678";
        String name = "John";
        String firstSurname = "Doe";
        String secondSurname = "Smith";
        String gender = "Male";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String email = "john.doe@example.com";
        UserType kindMember = UserType.SOCIO_NUMERO;
        AddressResponse address = new AddressResponse("PostalCode", "Apartment", "Building", "Street", "City",
                "Country", "SubCountry");
        Set<UserRoleName> roles = EnumSet.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_PRESIDENTE);

        // Act
        UserDataResponse response = new UserDataResponse(dni, name, firstSurname, secondSurname, gender, birthDate,
                email, kindMember, address, roles);

        // Assert
        assertEquals(dni, response.dni());
        assertEquals(name, response.name());
        assertEquals(firstSurname, response.firstSurname());
        assertEquals(secondSurname, response.secondSurname());
        assertEquals(gender, response.gender());
        assertEquals(birthDate, response.birthDate());
        assertEquals(email, response.email());
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
        when(user.getDni()).thenReturn("12345678");
        when(user.getProfile()).thenReturn(userProfile);
        when(user.getProfile().getName()).thenReturn("John");
        when(userProfile.getFirstSurname()).thenReturn("Doe");
        when(userProfile.getSecondSurname()).thenReturn("Smith");
        when(userProfile.getGender()).thenReturn("Male");
        when(userProfile.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));
        when(user.getEmail()).thenReturn("john.doe@example.com");
        when(user.getKindMember()).thenReturn(UserType.SOCIO_NUMERO);
        when(user.getAddress()).thenReturn(addressEntity);

        // Mock roles
        PersistentRoleEntity roleEntity = mock(PersistentRoleEntity.class);
        when(roleEntity.getName()).thenReturn(UserRoleName.ROLE_ADMIN); // Return the role name from the mock

        when(user.getRoles()).thenReturn(new HashSet<>(Set.of(roleEntity))); // Mock the roles

        // Act
        UserDataResponse response = new UserDataResponse(user);

        // Assert
        assertEquals("12345678", response.dni());
        assertEquals("John", response.name());
        assertEquals("Doe", response.firstSurname());
        assertEquals("Smith", response.secondSurname());
        assertEquals("Male", response.gender());
        assertEquals(LocalDate.of(1990, 1, 1), response.birthDate());
        assertEquals("john.doe@example.com", response.email());
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
        UserDataResponse response = new UserDataResponse("12345678", "John", "Doe", "Smith", "Male",
                LocalDate.of(1990, 1, 1), "john.doe@example.com", UserType.SOCIO_NUMERO,
                new AddressResponse("PostalCode", "Apartment", "Building", "Street", "City", "Country", "SubCountry"),
                roles);

        // Act
        Set<UserRoleName> responseRoles = response.userRoles();
        responseRoles.add(UserRoleName.ROLE_CANDIDATO_SOCIO); // Modify the returned set

        // Assert
        assertTrue(responseRoles.contains(UserRoleName.ROLE_CANDIDATO_SOCIO)); // The modified copy should have the role
        assertFalse(response.userRoles().contains(UserRoleName.ROLE_CANDIDATO_SOCIO)); // The original set should not
                                                                                       // have the role
    }

}
