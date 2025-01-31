
package es.org.cxn.backapp.test.integration.security;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.security.MyUserDetailsService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import jakarta.transaction.Transactional;

/**
 * Integration tests for {@link MyUserDetailsService}.
 * <p>
 * These tests ensure that user details can be correctly loaded and handle
 * scenarios where the user does not exist.
 * </p>
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:IntegrationController.properties")
class UserDetailsServiceIT {

    /**
     * The user service.
     */
    @Autowired
    private UserService userService;

    /**
     * My custom userDetailsService.
     */
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    /**
     * The email service mocked implementation.
     */
    @MockitoBean
    private DefaultEmailService defaultEmailService;
    /**
     * Mocked mail sender.
     */
    @MockitoBean
    private JavaMailSender javaMailSender;

    /**
     * Tests loading user details by username.
     * <p>
     * This test verifies that a user can be successfully loaded from the database
     * and that the user details are correctly populated.
     * </p>
     *
     * @throws UserServiceException if there is an error adding the user
     */
    @Test
    @Transactional
    @DisplayName("Should load user details by username")
    void testLoadUserDetailsByUsername() throws UserServiceException {
        final int year = 1990;
        final int month = 1;
        final int day = 1;

        final var countryNumericCode = 724;
        // Create an instance of AddressRegistrationDetailsDto
        var addressDetails = new AddressRegistrationDetailsDto("Apt 101", // Apartment Number
                "Building A", // Building
                "Springfield", // City
                "12345", // Postal Code
                "Main Street", // Street
                countryNumericCode, // Country Numeric Code (e.g., 1 for the US)
                "A Coruña" // Country Subdivision Name (e.g., State or Province)
        );

        // Create an instance of UserRegistrationDetailsDto
        var userDto = new UserRegistrationDetailsDto("12345678X", // DNI
                "John", // Name
                "Doe", // First Surname
                "Smith", // Second Surname
                LocalDate.of(year, month, day), // Birth Date
                "Male", // Gender
                "password123", // Password
                "john.doe@example.com", // Email
                addressDetails, // Address Details
                UserType.SOCIO_NUMERO // Kind of Member
        );

        userService.add(userDto);

        // Act
        var userDetails = myUserDetailsService.loadUserByUsername("john.doe@example.com");

        // Assert
        assertThat(userDetails).as("Verify that user details are not null").isNotNull();
        assertThat(userDetails.getUsername()).as("Verify that the username matches the expected value")
                .isEqualTo("john.doe@example.com");
    }

    /**
     * Tests loading user details for a non-existent user.
     * <p>
     * This test verifies that an exception is thrown when attempting to load
     * details for a user that does not exist in the system.
     * </p>
     */
    @Test
    @Transactional
    @DisplayName("Should throw UsernameNotFoundException for non-existent user")
    void testLoadUserDetailsForNonExistentUser() {
        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("nonexistent@example.com");
        }, "Expected UsernameNotFoundException to be thrown for a non-existent user");
    }

}
