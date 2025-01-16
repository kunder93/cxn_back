
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.responses.AddressResponse;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.UserListDataResponse;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;

class UserListDataResponseTest {
    @Test
    void testConstructorWithEmptyList() {
        UserListDataResponse response = new UserListDataResponse(Collections.emptyList());

        assertEquals(0, response.usersList().size());
    }

    @Test
    void testFromUserEntities() {
        // Mock UserEntities with required parameters
        UserProfile profile1 = new UserProfile("John", "Doe", "M", LocalDate.of(1990, 1, 1), "john.doe@example.com");
        UserProfile profile2 = new UserProfile("Jane", "Smith", "F", LocalDate.of(1992, 2, 2),
                "jane.smith@example.com");

        PersistentRoleEntity role = new PersistentRoleEntity(); // Mock role

        // Create PersistentUserEntity instances
        PersistentUserEntity user1 = new PersistentUserEntity("12345678A", profile1, "password123",
                "john.doe@example.com", UserType.SOCIO_NUMERO, true, Set.of(role));

        PersistentUserEntity user2 = new PersistentUserEntity("98765432B", profile2, "password456",
                "jane.smith@example.com", UserType.SOCIO_NUMERO, true, Set.of(role));

        List<UserEntity> userEntities = List.of(user1, user2);
        var address = new PersistentAddressEntity();
        var countrySubdivision = new PersistentCountrySubdivisionEntity();
        address.setCountrySubdivision(countrySubdivision);
        var country = new PersistentCountryEntity();
        address.setCountry(country);
        user1.setAddress(address);
        user2.setAddress(address);

        // Call the fromUserEntities method to get the response
        UserListDataResponse response = UserListDataResponse.fromUserEntities(userEntities);

        // Assertions
        assertNotNull(response);
        assertEquals(2, response.usersList().size());
        assertEquals("John", response.usersList().get(0).name()); // Assuming 'getFirstName()' exists
        assertEquals("Jane", response.usersList().get(1).name()); // Assuming 'getFirstName()' exists
    }

    @Test
    void testUserEntityToUserDataResponseMapping() {
        // Mock UserProfile with required parameters
        UserProfile profile = new UserProfile("John", "Doe", "M", LocalDate.of(1990, 1, 1), "john.doe@example.com");

        // Mock PersistentRoleEntity (add necessary constructor arguments based on your
        // entity structure)
        PersistentRoleEntity role = new PersistentRoleEntity(); // Replace with proper constructor if needed

        // Create a PersistentUserEntity with all required fields
        PersistentUserEntity user = new PersistentUserEntity("12345678A", // dni
                profile, // usrProfile
                "password123", // password
                "john.doe@example.com", // email
                UserType.SOCIO_NUMERO, // kindMember
                true, // enabled
                Set.of(role) // rolesEntity
        );
        var address = new PersistentAddressEntity();
        var countrySubdivision = new PersistentCountrySubdivisionEntity();
        address.setCountrySubdivision(countrySubdivision);
        var country = new PersistentCountryEntity();
        address.setCountry(country);
        user.setAddress(address);

        // Map PersistentUserEntity to UserDataResponse
        UserDataResponse userDataResponse = new UserDataResponse(user);

        // Verifying the mapping
        assertNotNull(userDataResponse);
        assertEquals("John", userDataResponse.name());
        assertEquals("Doe", userDataResponse.firstSurname());
        assertEquals("M", userDataResponse.secondSurname());
        assertEquals("12345678A", userDataResponse.dni()); // Verify DNI field
        assertEquals("john.doe@example.com", userDataResponse.email()); // Verify email field
    }

    @Test
    void testUsersListMethodReturnsCopy() {
        // Providing all required fields for UserDataResponse constructor
        UserDataResponse user1 = new UserDataResponse("12345678A", // dni
                "John", // name
                "Doe", // firstSurname
                "Smith", // secondSurname (mocked value)
                "M", // gender (mocked value)
                LocalDate.of(1990, 1, 1), // birthDate (mocked value)
                "john.doe@example.com", // email (mocked value)
                UserType.SOCIO_NUMERO, // kindMember (mocked value)
                new AddressResponse("12345", // postalCode
                        "Apt 1", // apartmentNumber (mocked value)
                        "Building A", // building (mocked value)
                        "Street 123", // street
                        "City", // city
                        "Country", // countryName
                        "SubCountry" // subCountryName
                ), // userAddress (mocked value)
                Set.of(UserRoleName.ROLE_SOCIO) // userRoles (mocked value)
        );

        List<UserDataResponse> users = List.of(user1);
        UserListDataResponse response = new UserListDataResponse(users);

        // Modifying the copy of the list should not affect the original list
        List<UserDataResponse> copiedList = response.usersList();
        copiedList.add(new UserDataResponse("98765432B", // dni
                "Jane", // name
                "Doe", // firstSurname
                "Johnson", // secondSurname (mocked value)
                "F", // gender (mocked value)
                LocalDate.of(1992, 2, 2), // birthDate (mocked value)
                "jane.doe@example.com", // email (mocked value)
                UserType.SOCIO_FAMILIAR, // kindMember (mocked value)
                new AddressResponse("54321", // postalCode
                        "Apt 2", // apartmentNumber (mocked value)
                        "Building B", // building (mocked value)
                        "Street 456", // street
                        "Other City", // city
                        "Other Country", // countryName
                        "Other SubCountry" // subCountryName
                ), // userAddress (mocked value)
                Set.of(UserRoleName.ROLE_ADMIN) // userRoles (mocked value)
        ));

        assertEquals(1, response.usersList().size()); // Ensure original list is unchanged
        assertEquals(2, copiedList.size()); // The copied list should be changed
    }

}
