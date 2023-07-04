
package es.org.cxn.backapp.tes.unit.validation;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
   * Valid password.
   */
  final private static String VALID_PASSWORD = "vPijN4223";

  /**
   * Too long password, its no valid.
   */
  final static private String TOO_LONG_51_NO_VALID_EMAIL =
        "asdfgkhrewasdgkhrewasjasdfrewdfgkhrew@dasdsadsa.asd";

  /**
   * Long password, but valid.
   */
  final static private String LONG_50_VALID_EMAIL =
        "asfgkhrewasdfgkhrwasasdfrewdfgjkhrew@dasdsadsa.asd";
  /**
   * Valid email.
   */
  final static private String VALID_EMAIL = "asfgkhrewejkhrew@dadsa.asd";

  /**
   * Not valid null email.
   */
  final static private String NULL_EMAIL = null;

  /**
   * Blank not valid email.
   */
  final static private String BLANK_EMAIL = "";

  /**
   * Not valid null password.
   */
  final static private String NULL_PASSWORD = null;

  /**
   * Default constructor.
   */
  public TestAuthenticationRequestValidation() {
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
   * Verifies that Email field is null, validation catch it.
   */
  @Test
  void testValidationNullEmailError() {
    // Create not valid authentication request.
    final var authRequest =
          new AuthenticationRequest(NULL_EMAIL, VALID_PASSWORD);
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
            Constants.EMAIL_NOT_EMPTY_MESSAGE, constraintViolation.getMessage(),
            "The message provided is the email not empty."
      );
      Assertions.assertTrue(
            constraintViolation.getConstraintDescriptor()
                  .getAnnotation() instanceof NotEmpty,
            "Not empty annotation"
      );
    });
  }

  /**
   * Verifies that Email field cannot be blank.
   */
  @Test
  void testValidationBlankEmailError() {
    final var authRequest =
          new AuthenticationRequest(BLANK_EMAIL, VALID_PASSWORD);
    final Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(),
          "only 1 constraint violation when email is blank value"
    );
    violations.forEach(constraintViolation -> {
      Assertions.assertEquals(
            BLANK_EMAIL, constraintViolation.getInvalidValue(),
            "BLANK_EMAIL is the not valid value"
      );
      Assertions.assertEquals(
            Constants.EMAIL_NOT_EMPTY_MESSAGE, constraintViolation.getMessage(),
            "EMAIL NOT EMPTY constraint violation message."
      );
      Assertions.assertTrue(
            constraintViolation.getConstraintDescriptor()
                  .getAnnotation() instanceof NotEmpty,
            "Not empty annotation"
      );
    });
  }

  /**
   * Verifies that Email field cannot exceed 50 characters.
   */
  @Test
  void testValidationLargeEmailError() {
    final var authRequest =
          new AuthenticationRequest(TOO_LONG_51_NO_VALID_EMAIL, VALID_PASSWORD);

    final Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(),
          "only 1 error when email is too long"
    );
    violations.forEach(constraintViolation -> {
      Assertions.assertEquals(
            TOO_LONG_51_NO_VALID_EMAIL, constraintViolation.getInvalidValue(),
            "TOO_LONG_51_NO_VALID_EMAIL is the not valid value"
      );
      Assertions.assertEquals(
            Constants.MAX_SIZE_EMAIL_MESSAGE, constraintViolation.getMessage(),
            "Check message provided is MAX_SIZE_EMAIL_MESSAGE."
      );
      Assertions.assertTrue(
            constraintViolation.getConstraintDescriptor()
                  .getAnnotation() instanceof Size,
            "Check size annotation"
      );
    });
  }

  /**
   * Verifies that if the Email field cannot exceed 50 characters, so with 50
   * characters works
   */
  @Test
  void testValidationLargeEmailValid() {
    var authRequest =
          new AuthenticationRequest(LONG_50_VALID_EMAIL, VALID_PASSWORD);
    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(0), violations.size(),
          "long 50 character email generate no errors"
    );
    // Others values that should be valid
    final var otherValidEmail = "emailValid@Valid.ok";
    authRequest.setEmail(otherValidEmail);
    violations = validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(0), violations.size(), "email validation no errors"
    );
    // check another email
    authRequest.setEmail("otroemail@a.esa");
    violations = validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(0), violations.size(), "email validation no errors"
    );
  }

  /**
   * Verifies that the Email field cannot be greater than 50 characters or
   * less than 5
   */
  @Test
  void testValidationPasswordSize() {
    final var short5Password = "sds4s";
    final var long21Password = "asdaowqmfkfqjiwqdjsas";
    var authRequest = new AuthenticationRequest(VALID_EMAIL, short5Password);
    // Password too short
    Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(),
          "password too short generate 1 error"
    );
    var violation = violations.iterator().next();
    Assertions.assertTrue(
          violation.getConstraintDescriptor().getAnnotation() instanceof Length,
          "annotation instance is Length"
    );

    // 21 characters Password is so long
    authRequest.setPassword(long21Password);
    violations = validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(),
          "password too long generate 1 error"
    );
    violation = violations.iterator().next();
    Assertions.assertTrue(
          violation.getConstraintDescriptor().getAnnotation() instanceof Length,
          "annotation instance is Length"
    );

    // Valid password
    authRequest.setPassword("someValidPassword");
    violations = validator.validate(authRequest);
    Assertions.assertEquals(Integer.valueOf(0), violations.size(), "no errors");
  }

  /**
   * Verifies that Password field is null, validation catch it.
   */
  @Test
  void testValidationNullPasswordError() {
    final var authRequest =
          new AuthenticationRequest(VALID_EMAIL, NULL_PASSWORD);
    final Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(),
          "only 1 constraint violation when is null value"
    );
    violations.forEach(constraintViolation -> {
      Assertions.assertNull(
            constraintViolation.getInvalidValue(), "null is the not valid value"
      );
      Assertions.assertEquals(
            Constants.NOT_NULL_PASSWORD, constraintViolation.getMessage(),
            "The message is NOT_NULL_PASSWORD."
      );
      Assertions.assertTrue(
            constraintViolation.getConstraintDescriptor()
                  .getAnnotation() instanceof NotNull,
            "Not empty annotation"
      );
    });
  }

  /**
   * Verifies that Password field cannot be blank.
   */
  @Test
  void testValidationBlankPasswordError() {
    final var blankPassword = "";
    final var authRequest =
          new AuthenticationRequest(VALID_EMAIL, blankPassword);
    final Set<ConstraintViolation<AuthenticationRequest>> violations =
          validator.validate(authRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(),
          "only 1 error when is null value"
    );
    violations.forEach(constraintViolation -> {
      Assertions.assertEquals(
            blankPassword, constraintViolation.getInvalidValue(),
            "null is the not valid value"
      );
      Assertions.assertEquals(
            Constants.LENGTH_PASSWORD_MESSAGE, constraintViolation.getMessage(),
            "The message provided"
      );
      Assertions.assertTrue(
            constraintViolation.getConstraintDescriptor()
                  .getAnnotation() instanceof Length,
            "Not empty annotation"
      );
    });
  }

  /**
   * Verifies authentication request equals method.
   */
  @Test
  void testEqualsHashCodeRequest() {
    final var authRequest =
          new AuthenticationRequest(VALID_EMAIL, VALID_PASSWORD);
    Assertions.assertEquals(authRequest, authRequest, "same object");
    Assertions.assertEquals(
          authRequest.hashCode(), authRequest.hashCode(), "same object hashcode"
    );
    Assertions.assertNotNull(authRequest, "check null");
    final var otherEmail = "other@email.com";
    final var authRequest2 =
          new AuthenticationRequest(otherEmail, VALID_PASSWORD);

    Assertions.assertNotEquals(
          authRequest, authRequest2, "The request are not equals."
    );
    Assertions.assertNotEquals(
          authRequest.hashCode(), authRequest2.hashCode(),
          "Request hash code are not equals."
    );
    Assertions.assertNotEquals(
          authRequest.toString(), authRequest2.toString(),
          "ToString are not equals."
    );

    final var otherPassword = "adaswrgasdd";
    final var authRequest3 =
          new AuthenticationRequest(VALID_EMAIL, otherPassword);

    Assertions.assertNotEquals(
          authRequest, authRequest3, "not equals diferent objects"
    );
    Assertions.assertNotEquals(authRequest3, authRequest2, "not equals");
  }

}
