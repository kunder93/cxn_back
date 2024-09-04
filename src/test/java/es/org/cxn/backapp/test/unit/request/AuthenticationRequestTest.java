
package es.org.cxn.backapp.test.unit.request;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link AuthenticationRequest}.
 * <p>
 * This class provides unit tests to validate the behavior of the
 * {@link AuthenticationRequest} class, particularly focusing on
 * email and password validation according to the constraints defined in
 * the model.
 * </p>
 * <p>
 * The tests utilize parameterized testing to cover various edge cases,
 * including blank emails, whitespace-only emails, various email formats,
 * and password length constraints.
 * </p>
 */
class AuthenticationRequestTest {

  /**
   * Validator instance for performing constraint validation.
   */
  private static Validator validator;

  /**
   * Sets up the validator instance used to validate
   * {@link AuthenticationRequest} instances.
   */
  @BeforeAll
  static void setUpValidator() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests email validation with blank values and whitespace characters.
   *
   * @param email The email to test.
   */
  @ParameterizedTest
  @ValueSource(strings = { "", "   ", "\t", "\n" })
  // blank, spaces, tabs, newlines
  @DisplayName("Validate blank and whitespace emails")
  void testBlankAndWhitespaceEmails(final String email) {
    // Given: A request with a blank or whitespace email
    final var authRequest = new AuthenticationRequest(email, "ValidPassword1!");

    // When: The request is validated
    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);

    // Then: There should be one or two violations for an invalid email
    Assertions.assertTrue(
          violations.size() >= 1 && violations.size() <= 2,
          "One or two violations expected for blank or whitespace email."
    );

    // Check if any of the violations is about the email being invalid
    var foundEmailNotValid = violations.stream()
          .anyMatch(v -> v.getMessage().equals(Constants.EMAIL_NOT_VALID));

    Assertions.assertTrue(
          foundEmailNotValid, "Violation should indicate invalid email."
    );
  }

  /**
   * Tests email validation with various formats and sizes.
   *
   * @param email   The email to test.
   * @param isValid Whether the email is expected to be valid.
   */
  @ParameterizedTest
  @MethodSource("provideEmailFormatsAndSizes")
  @DisplayName("Validate various email formats and sizes")
  void testEmailFormatsAndSizes(final String email, final boolean isValid) {
    // Given: A request with a specific email format or size
    final var authRequest = new AuthenticationRequest(email, "ValidPassword1!");

    // When: The request is validated
    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);

    // Then: The validation result should match the expected outcome
    if (isValid) {
      Assertions.assertTrue(
            violations.isEmpty(), "No violations expected for valid email."
      );
    } else {
      Assertions.assertEquals(
            1, violations.size(), "One violation expected for invalid email."
      );
      var violation = violations.iterator().next();
      Assertions.assertEquals(
            Constants.EMAIL_NOT_VALID, violation.getMessage(),
            "Violation should indicate invalid email."
      );
    }
  }

  /**
   * Provides various email formats and sizes for testing.
   *
   * @return A stream of arguments containing email strings and their
   * expected validity.
   */
  private static Stream<Arguments> provideEmailFormatsAndSizes() {
    return Stream.of(
          Arguments.of("user@example.com", true), // Valid email
          Arguments.of("user.name+tag+sorting@example.com", true),
          // Valid complex email
          Arguments.of("user@sub.example.com", true),
          // Valid email with subdomain
          Arguments.of("user@.example.com", false),
          // Invalid email with leading dot in domain
          Arguments.of("user@domain..com", false),
          // Invalid email with double dots in domain
          Arguments.of("user@domain.com", true),
          // Valid email
          Arguments.of("user@", false), // Invalid email with no domain
          Arguments.of("user", false), // Invalid email with no @ symbol
          Arguments.of("a@b.c", true), // Valid email with minimal domain
          Arguments.of("'a'@example.com", true)
          // Valid email with single quotes
    );
  }

  /**
   * Provides various password lengths for testing.
   *
   * @return A stream of arguments containing password strings and their
   * expected validity.
   */
  private static Stream<Arguments> passwordProvider() {
    return Stream.of(
          Arguments.of("", false), // Empty password (invalid)
          Arguments.of("short", false), // Too short (invalid)
          Arguments.of("a".repeat(Constants.MIN_PASSWORD_LENGTH - 1), false),
          // Just below min length (invalid)
          Arguments.of("a".repeat(Constants.MIN_PASSWORD_LENGTH), true),
          // At min length (valid)
          Arguments.of("a".repeat(Constants.MAX_PASSWORD_LENGTH), true),
          // At max length (valid)
          Arguments.of("a".repeat(Constants.MAX_PASSWORD_LENGTH + 1), false)
          // Just above max length (invalid)
    );
  }

  /**
   * Tests password length validation.
   *
   * @param password The password to test.
   * @param isValid  Whether the password is expected to be valid.
   */
  @ParameterizedTest
  @MethodSource("passwordProvider")
  @DisplayName("Validate password length constraints")
  void testPasswordLengthValidation(
        final String password, final boolean isValid
  ) {
    // Given: A request with a specific password length
    final var authRequest =
          new AuthenticationRequest("user@example.com", password);

    // When: The request is validated
    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);

    // Then: There should be violations for invalid password lengths
    if (isValid) {
      Assertions.assertTrue(
            violations.isEmpty(),
            "No violations expected for valid password length."
      );
    } else {
      Assertions.assertEquals(
            1, violations.size(),
            "One violation expected for invalid password length."
      );
      var violation = violations.iterator().next();
      Assertions.assertEquals(
            Constants.LENGTH_PASSWORD_MESSAGE, violation.getMessage(),
            "Violation should indicate password length issue."
      );
    }
  }
}
