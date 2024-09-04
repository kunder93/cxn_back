
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit test for {@link UserChangePasswordRequest}.
 * <p>
 * This test class verifies the validation of the
 * {@link UserChangePasswordRequest} record using parameterized tests with
 * various valid and invalid values for email, currentPassword, and newPassword.
 * </p>
 */
public class UserChangePasswordRequestTest {

  /**
   * The validator.
   */
  private static Validator validator;

  /**
   * Sets up the validator factory and initializes the {@link Validator}
   * instance to be used for validating {@link UserChangePasswordRequest}
   * objects in the tests.
   * <p>
   * This method is annotated with {@link BeforeAll}, which means it will be
   * executed once before any of the test methods in this class are run.
   * It initializes the {@code validator} field using the default validator
   * factory provided by Jakarta Bean Validation (formerly known as JSR 380).
   * </p>
   * <p>
   * The validator is used to ensure that the objects being tested adhere to the
   * validation constraints defined in the {@link UserChangePasswordRequest}
   * class.
   * </p>
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests the validation of {@link UserChangePasswordRequest} with various
   * email addresses, current passwords, and new passwords.
   *
   * @param email The email address to be tested.
   * @param currentPassword The current password to be tested.
   * @param newPassword The new password to be tested.
   * @param expectedViolations The expected number of validation violations.
   */
  @ParameterizedTest(
        name = "email={0}, currentPassword={1}, newPassword={2}, "
              + "expectedViolations={3}"
  )
  @CsvSource(
    {
        // Valid cases
        "'valid.email@example.com', 'valid1Pass', 'valid2Pass', 0",
        // Invalid cases
        "invalid-email, 'valid1Pass', 'valid2Pass', 1",
        // Invalid email
        "'valid.email@example.com', , 'valid2Pass', 1",
        // Current password blank
        "'valid.email@example.com', 'short', 'valid2Pass', 1",
        // Current password too short
        "'valid.email@example.com', 'valid1Pass', , 1",
        // New password blank
        "'valid.email@example.com', 'valid1Pass', 'short', 1",
        // New password too short
        "'valid.email@example.com', 'valid1Pass', 'wayTooLongPassword12345', 1",
    // New password too long
    }
  )
  void testUserChangePasswordRequestValidation(
        final String email, final String currentPassword,
        final String newPassword, final int expectedViolations
  ) {
    final var request =
          new UserChangePasswordRequest(email, currentPassword, newPassword);

    final Set<ConstraintViolation<UserChangePasswordRequest>> violations =
          validator.validate(request);

    assertEquals(
          expectedViolations, violations.size(),
          "The number of violations should match the expected number."
    );
  }
}
