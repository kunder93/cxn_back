
package es.org.cxn.backapp.test.unit.validation;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link AuthenticationRequestValidation} bean validation.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz Perez
 */
final class TestAuthenticationRequestValidation {

  /**
   * Validator used to validate field.
   */
  private Validator validator;

  /**
   * Example valid password.
   */
  private static final String VALID_PASSWORD = "vPijN4223";

  /**
   * Example valid email.
   */
  private static final String VALID_EMAIL = "asfgkhrewejkhrew@dadsa.asd";

  /**
   * Default constructor.
   */
  TestAuthenticationRequestValidation() {
    super();
  }

  /**
   * Sets up the validator for the tests.
   */
  @BeforeEach
  public void setUpValidator() {
    final var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Verifies validation Not blank in email.
   *
   * @param email The email not valid value.
   */
  @DisplayName("Validate blank email values are not valid.")
  @ParameterizedTest
  @ValueSource(strings = { "", " ", "    " })
  void testEmailBlankNotValid(final String email) {
    final var authRequest = new AuthenticationRequest(email, VALID_PASSWORD);
    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);

    /* Could be possible not valid email and not blank constraint violations
     * at same time. */
    var matchConsViolSiz = violations.size() == 1 || violations.size() == 2;
    Assertions
          .assertTrue(matchConsViolSiz, "constraint violation size 1 or 2.");
    violations.forEach(constraintViolation -> {
      Assertions.assertEquals(
            Constants.EMAIL_NOT_VALID, constraintViolation.getMessage(),
            "The constraint message is about email."
      );
      Assertions.assertEquals(
            authRequest.email(), constraintViolation.getInvalidValue(),
            "The constraint message is about email."
      );
    });
  }

  /**
   *  Verifies validation email cannot be more than 50 characters.
   * @param email The auth request email.
   */
  @DisplayName("Validate email no valid upper max length.")
  @ParameterizedTest
  @ValueSource(
        strings = { /*51*/"aaaaaaaaaaaaaverylongEmailExample@VeryLongEmail.com",
            /*52*/ "baaaaaaaaaaaaaverylongEmailExample@VeryLongEmail.com" }
  )
  void testEmailLengthOutBoundsNotValid(final String email) {
    final var authRequest = new AuthenticationRequest(email, VALID_PASSWORD);

    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.MAX_SIZE_EMAIL_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about email."
    );
    Assertions.assertEquals(
          authRequest.email(), constraintViolation.getInvalidValue(),
          "The constraint message is about email."
    );
  }

  /**
   * Verifies that Email field is null, validation catch it.
   */
  @Test
  void testValidationNullEmailNotValid() {
    final String email = null;
    // Create not valid authentication request.
    final var authRequest = new AuthenticationRequest(email, VALID_PASSWORD);
    // Validate request.
    final Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    final var violationsSize = violations.size();
    Assertions.assertEquals(
          Integer.valueOf(1), violationsSize,
          "Check only 1 constraint violation when email is null."
    );
    violations.forEach(constraintViolation -> {
      Assertions.assertNull(
            constraintViolation.getInvalidValue(), "null is the not valid value"
      );
      Assertions.assertEquals(
            Constants.EMAIL_NOT_VALID, constraintViolation.getMessage(),
            "The message provided is the email not empty."
      );
    });
  }

  /**
   * Verifies validation Not blank in password.
   *
   * @param password The password not valid value.
   */
  @DisplayName("Validate blank password values are not valid.")
  @ParameterizedTest
  @ValueSource(strings = { "", " ", "    " })
  void testPasswordBlankNotValid(final String password) {
    final var authRequest = new AuthenticationRequest(VALID_EMAIL, password);
    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);

    /* Could be possible not valid email and not blank constraint violations
     * at same time. */
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(), "One constrant validation."
    );
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.LENGTH_PASSWORD_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about email."
    );
    Assertions.assertEquals(
          authRequest.password(), constraintViolation.getInvalidValue(),
          "The constraint message is about email."
    );
  }

  /**
   * Verifies that Email field is null, validation catch it.
   */
  @Test
  void testPasswordNullNotValid() {
    final String password = null;
    // Create not valid authentication request.
    final var authRequest = new AuthenticationRequest(VALID_EMAIL, password);
    // Validate request.
    final Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    final var violationsSize = violations.size();
    Assertions.assertEquals(
          Integer.valueOf(1), violationsSize,
          "Check only 1 constraint violation when email is null."
    );
    var constraintViolation = violations.iterator().next();
    Assertions.assertNull(
          constraintViolation.getInvalidValue(), "null is the not valid value"
    );
    Assertions.assertEquals(
          Constants.NOT_NULL_PASSWORD, constraintViolation.getMessage(),
          "The message provided is the email not empty."
    );
  }

  /**
   * Verifies validation password cannot be greater than 50
   * characters or less than 6.
   * @param password The auth request password.
   */
  @DisplayName("Validate password out from length bounds.")
  @ParameterizedTest
  @ValueSource(
        strings = { /*21*/"123456789012345678901",
            /*22*/ "1234567890123456789012", /*5*/ "12345", /*4*/ "1234" }
  )
  void testPasswordLengthOutBoundsNotValid(final String password) {
    final var authRequest = new AuthenticationRequest(VALID_EMAIL, password);

    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.LENGTH_PASSWORD_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about password."
    );
    Assertions.assertEquals(
          authRequest.password(), constraintViolation.getInvalidValue(),
          "The constraint message is about password."
    );
  }

}
