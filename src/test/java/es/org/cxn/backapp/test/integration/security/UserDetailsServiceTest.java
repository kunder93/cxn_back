
package es.org.cxn.backapp.test.integration.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.MyUserDetailsService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for {@link MyUserDetailsService}.
 * <p>
 * These tests ensure that user details can be correctly loaded and handle
 * scenarios where the user does not exist.
 * </p>
 */
@SpringBootTest
@ActiveProfiles("test")
class UserDetailsServiceTest {

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
   * Tests loading user details by username.
   * <p>
   * This test verifies that a user can be successfully loaded from the
   * database and that the user details are correctly populated.
   * </p>
   *
   * @throws UserServiceException if there is an error adding the user
   */
  @Test
  @Transactional
  @DisplayName("Should load user details by username")
  void testLoadUserDetailsByUsername() throws UserServiceException {
    // Create an instance of AddressRegistrationDetailsDto
    var addressDetails = new AddressRegistrationDetailsDto(
          "Apt 101", // Apartment Number
          "Building A", // Building
          "Springfield", // City
          "12345", // Postal Code
          "Main Street", // Street
          724, // Country Numeric Code (e.g., 1 for the US)
          "A Coruña" // Country Subdivision Name (e.g., State or Province)
    );

    // Create an instance of UserRegistrationDetailsDto
    var userDto = new UserRegistrationDetailsDto(
          "12345678X", // DNI
          "John", // Name
          "Doe", // First Surname
          "Smith", // Second Surname
          LocalDate.of(1990, 1, 1), // Birth Date
          "Male", // Gender
          "password123", // Password
          "john.doe@example.com", // Email
          addressDetails, // Address Details
          UserType.SOCIO_NUMERO // Kind of Member
    );

    userService.add(userDto);

    // Act
    var userDetails =
          myUserDetailsService.loadUserByUsername("john.doe@example.com");

    // Assert
    assertThat(userDetails).as("Verify that user details are not null")
          .isNotNull();
    assertThat(userDetails.getUsername())
          .as("Verify that the username matches the expected value")
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
    }, "Expected UsernameNotFoundException to be thrown for a non-existent user"
    );
  }
}
