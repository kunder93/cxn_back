
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
import es.org.cxn.backapp.model.form.responses.user.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.user.UserListDataResponse;
import es.org.cxn.backapp.model.form.responses.user.address.AddressResponse;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;

class UserListDataResponseTest {

    /**
     * Constant representing the first name of the first user. This value is used
     * for testing purposes, specifically to create the user's full name.
     */
    private static final String USER_NAME = "John";

    /**
     * Constant representing the first surname of the first user. This value is used
     * for testing purposes to complete the user's full name.
     */
    private static final String USER_FIRST_SURNAME = "Doe";

    /**
     * Constant representing the second surname of the first user. This value is
     * used for testing purposes to complete the user's full name.
     */
    private static final String USER_SECOND_SURNAME = "Doe";

    /**
     * Constant representing the first name of the second user. This value is used
     * for testing purposes, specifically to create the second user's full name.
     */
    private static final String SEC_USER_NAME = "Jane";

    /**
     * Constant representing the first surname of the second user. This value is
     * used for testing purposes to complete the second user's full name.
     */
    private static final String SEC_USER_FIRST_SURNAME = "Smith";

    /**
     * Constant representing the second surname of the second user. This value is
     * used for testing purposes to complete the second user's full name.
     */
    private static final String SEC_USER_SECOND_SURNAME = "Faer";

    /**
     * Constant representing the birth date of the first user. This value is used to
     * simulate the user's birth date for testing.
     */
    private static final LocalDate USER_BIRTH_DATE = LocalDate.of(1990, 1, 1);

    /**
     * Constant representing the birth date of the second user. This value is used
     * to simulate the second user's birth date for testing.
     */
    private static final LocalDate SEC_USER_BIRTH_DATE = LocalDate.of(1992, 2, 22);

    /**
     * Constant representing the email address of the first user. This value is used
     * for testing purposes to simulate the user's email.
     */
    private static final String USER_EMAIL = "john.doe@example.com";

    /**
     * Constant representing the email address of the second user. This value is
     * used for testing purposes to simulate the second user's email.
     */
    private static final String SEC_USER_EMAIL = "jane.smith@example.com";

    /**
     * Constant representing the DNI (National Identity Number) of the first user.
     * This value is used to simulate the user's DNI for testing.
     */
    private static final String USER_DNI = "12345678A";

    /**
     * Constant representing the DNI (National Identity Number) of the second user.
     * This value is used to simulate the second user's DNI for testing.
     */
    private static final String SEC_USER_DNI = "98765432B";

    /**
     * Constant representing the password of the first user. This value is used for
     * testing purposes to simulate the user's password.
     */
    private static final String USER_PSSW = "absdwd123";

    @Test
    void testConstructorWithEmptyList() {
        UserListDataResponse response = new UserListDataResponse(Collections.emptyList());

        assertEquals(0, response.usersList().size());
    }

    @Test
    void testFromUserEntities() {
        // Mock UserEntities with required parameters
        UserProfile profile1 = new UserProfile(USER_NAME, USER_FIRST_SURNAME, USER_SECOND_SURNAME, USER_BIRTH_DATE,
                USER_EMAIL);
        UserProfile profile2 = new UserProfile(SEC_USER_NAME, SEC_USER_FIRST_SURNAME, SEC_USER_SECOND_SURNAME,
                SEC_USER_BIRTH_DATE, SEC_USER_EMAIL);

        PersistentRoleEntity role = new PersistentRoleEntity(); // Mock role

        // Create PersistentUserEntity instances
        PersistentUserEntity user1 = new PersistentUserEntity(USER_DNI, profile1, USER_PSSW, USER_EMAIL,
                UserType.SOCIO_NUMERO, true, Set.of(role));

        PersistentUserEntity user2 = new PersistentUserEntity(SEC_USER_DNI, profile2, "password456", SEC_USER_EMAIL,
                UserType.SOCIO_NUMERO, true, Set.of(role));

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
        assertEquals(USER_NAME, response.usersList().get(0).name()); // Assuming 'getFirstName()' exists
        assertEquals(SEC_USER_NAME, response.usersList().get(1).name()); // Assuming 'getFirstName()' exists
    }

    @Test
    void testUserEntityToUserDataResponseMapping() {
        // Mock UserProfile with required parameters
        UserProfile profile = new UserProfile(USER_NAME, USER_FIRST_SURNAME, USER_SECOND_SURNAME, USER_BIRTH_DATE,
                USER_EMAIL);

        // Mock PersistentRoleEntity (add necessary constructor arguments based on your
        // entity structure)
        PersistentRoleEntity role = new PersistentRoleEntity(); // Replace with proper constructor if needed

        // Create a PersistentUserEntity with all required fields
        PersistentUserEntity user = new PersistentUserEntity(USER_DNI, // dni
                profile, // usrProfile
                USER_PSSW, // password
                USER_EMAIL, // email
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
        assertEquals(USER_NAME, userDataResponse.name());
        assertEquals(USER_FIRST_SURNAME, userDataResponse.firstSurname());
        assertEquals(USER_SECOND_SURNAME, userDataResponse.secondSurname());
        assertEquals(USER_DNI, userDataResponse.dni()); // Verify DNI field
        assertEquals(USER_EMAIL, userDataResponse.email()); // Verify email field
    }

    @Test
    void testUsersListMethodReturnsCopy() {
        // Providing all required fields for UserDataResponse constructor
        UserDataResponse user1 = new UserDataResponse(USER_DNI, // dni
                USER_NAME, // name
                USER_FIRST_SURNAME, // firstSurname
                SEC_USER_FIRST_SURNAME, // secondSurname (mocked value)
                USER_SECOND_SURNAME, // gender (mocked value)
                USER_BIRTH_DATE, // birthDate (mocked value)
                USER_EMAIL, // email (mocked value)
                UserType.SOCIO_NUMERO, // kindMember (mocked value)
                new AddressResponse("12345", // postalCode
                        "Apt 1", // apartmentNumber (mocked value)
                        "Building A", // building (mocked value)
                        "Street 123", // street
                        "City", // city
                        "Country", // countryName
                        "SubCountry" // subCountryName
                ), // userAddress (mocked value)
                Set.of(UserRoleName.ROLE_SOCIO), // userRoles (mocked value)
                "TeamNameAssigned", "TeamNamePreferred");

        List<UserDataResponse> users = List.of(user1);
        UserListDataResponse response = new UserListDataResponse(users);

        // Modifying the copy of the list should not affect the original list
        List<UserDataResponse> copiedList = response.usersList();
        copiedList.add(new UserDataResponse(SEC_USER_DNI, // dni
                SEC_USER_NAME, // name
                USER_FIRST_SURNAME, // firstSurname
                USER_SECOND_SURNAME, // secondSurname (mocked value)
                SEC_USER_SECOND_SURNAME, // gender (mocked value)
                SEC_USER_BIRTH_DATE, // birthDate (mocked value)
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
                Set.of(UserRoleName.ROLE_ADMIN), // userRoles (mocked value)
                "TeamNameAssigned", "TeamNamePreferred"));

        assertEquals(1, response.usersList().size()); // Ensure original list is unchanged
        assertEquals(2, copiedList.size()); // The copied list should be changed
    }

}
