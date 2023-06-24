package es.org.cxn.backapp.tes.unit.validation;

import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Unit tests for {@link AuthenticationRequestValidation} bean validation.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz Perez
 */
final class TestAuthenticationRequestValidation {

    private Validator validator;

    private String validPassword = "vPijN4223";

    // Not valid by validator
    final static private String long51CharacterEmail = "asdfgkhrewasdgkhrewasjasdfrewdfgkhrew@dasdsadsa.asd";
    // Valid by validator
    final static private String long50ValidEmail = "asfgkhrewasdfgkhrwasasdfrewdfgjkhrew@dasdsadsa.asd";
    final static private String validEmail = "asfgkhrewejkhrew@dadsa.asd";

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
    public final void setUpValidator() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Verifies that Email field is null, validation catch it.
     */
    @Test
    void testValidationNullEmailError() {
        var ar = new AuthenticationRequest();
        ar.setPassword(validPassword);
        ar.setEmail(null);
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator
                .validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(),
                "only 1 error when is null value"
        );
        violations.forEach(aa -> {
            Assertions.assertNull(
                    aa.getInvalidValue(), "null is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.EMAIL_NOT_EMPTY_MESSAGE, aa.getMessage(),
                    "The message provided"
            );
            Assertions.assertTrue(
                    aa.getConstraintDescriptor()
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
        final var blankEmail = "";
        var ar = new AuthenticationRequest(blankEmail, validPassword);
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator
                .validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(),
                "only 1 error when is null value"
        );
        violations.forEach(aa -> {
            Assertions.assertEquals(
                    blankEmail, aa.getInvalidValue(),
                    "null is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.EMAIL_NOT_EMPTY_MESSAGE, aa.getMessage(),
                    "Check message provided"
            );
            Assertions.assertTrue(
                    aa.getConstraintDescriptor()
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
        var ar = new AuthenticationRequest();
        ar.setPassword(validPassword);
        ar.setEmail(long51CharacterEmail);
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator
                .validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(),
                "only 1 error when email is too long"
        );
        violations.forEach(aa -> {
            Assertions.assertEquals(
                    long51CharacterEmail, aa.getInvalidValue(),
                    "long51CharacterEmail is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.MAX_SIZE_EMAIL_MESSAGE, aa.getMessage(),
                    "Check message provided"
            );
            Assertions.assertTrue(
                    aa.getConstraintDescriptor()
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
        var ar = new AuthenticationRequest();
        ar.setPassword(validPassword);
        ar.setEmail(long50ValidEmail);
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator
                .validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(0), violations.size(),
                "long 50 character email generate no errors"
        );
        // Others values that should be valid
        var otherValidEmail = "emailValid@Valid.ok";
        ar.setEmail(otherValidEmail);
        violations = validator.validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(0), violations.size(),
                "email validation no errors"
        );
        // check another email
        ar.setEmail("otroemail@a.esa");
        violations = validator.validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(0), violations.size(),
                "email validation no errors"
        );
    }

    /**
     * Verifies that the Email field cannot be greater than 50 characters or
     * less than 5
     */
    @Test
    final void testValidationPasswordSize() {
        final var short5Password = "sds4s";
        final var long21Password = "asdaowqmfkfqjiwqdjsas";
        var ar = new AuthenticationRequest();
        ar.setPassword(short5Password);
        ar.setEmail(validEmail);
        // Password too short
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator
                .validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(),
                "password too short generate 1 error"
        );
        var violation = violations.iterator().next();
        Assertions.assertTrue(
                violation.getConstraintDescriptor()
                        .getAnnotation() instanceof Length,
                "annotation instance is Length"
        );

        // 21 characters Password is so long
        ar.setPassword(long21Password);
        violations = validator.validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(),
                "password too long generate 1 error"
        );
        violation = violations.iterator().next();
        Assertions.assertTrue(
                violation.getConstraintDescriptor()
                        .getAnnotation() instanceof Length,
                "annotation instance is Length"
        );

        // Valid password
        ar.setPassword("someValidPassword");
        violations = validator.validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(0), violations.size(), "no errors"
        );
    }

    /**
     * Verifies that Password field is null, validation catch it.
     */
    @Test
    void testValidationNullPasswordError() {
        var ar = new AuthenticationRequest();
        ar.setPassword(null);
        ar.setEmail(validEmail);
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator
                .validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(),
                "only 1 error when is null value"
        );
        violations.forEach(aa -> {
            Assertions.assertNull(
                    aa.getInvalidValue(), "null is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.NOT_NULL_PASSWORD, aa.getMessage(),
                    "The message provided"
            );
            Assertions.assertTrue(
                    aa.getConstraintDescriptor()
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
        var ar = new AuthenticationRequest(validEmail, blankPassword);
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator
                .validate(ar);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(),
                "only 1 error when is null value"
        );
        violations.forEach(aa -> {
            Assertions.assertEquals(
                    blankPassword, aa.getInvalidValue(),
                    "null is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.LENGTH_PASSWORD_MESSAGE, aa.getMessage(),
                    "The message provided"
            );
            Assertions.assertTrue(
                    aa.getConstraintDescriptor()
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
        final var ar = new AuthenticationRequest(validEmail, validPassword);
        Assertions.assertTrue(ar.equals(ar), "same object");
        Assertions.assertEquals(
                ar.hashCode(), ar.hashCode(), "same object hashcode"
        );
        Assertions.assertFalse(ar.equals(null), "check null");
        final var otherEmail = "other@email.com";
        final var ar2 = new AuthenticationRequest(otherEmail, validPassword);

        Assertions.assertFalse(ar.equals(ar2));
        Assertions.assertNotEquals(ar.hashCode(), ar2.hashCode());
        Assertions.assertNotEquals(ar.toString(), ar2.toString());

        final var otherPassword = "adaswrgasdd";
        final var ar3 = new AuthenticationRequest(validEmail, otherPassword);

        Assertions.assertFalse(ar.equals(ar3), "not equals diferent objects");
        Assertions.assertFalse(ar3.equals(ar2), "not equals");
    }

}
