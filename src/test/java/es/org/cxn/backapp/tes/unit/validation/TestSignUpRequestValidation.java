package es.org.cxn.backapp.tes.unit.validation;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Unit tests for {@link SignUpRequestValidation} bean validation.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz Perez
 */
final class TestSignUpRequestValidation {

    private Validator validator;

    private static final String validName = "Jacinto";
    private static final String validFirstSurname = "Anacletio";
    private static final String validSecondSurname = "Juartillo";
    private static final LocalDate validBirthDate = LocalDate
            .ofYearDay(1993, 2);
    private static final String validGender = "male";
    private static final String validPassword = "OneValidPassword";
    private static final String validEmail = "santi@santi.es";

    /**
     * Default constructor.
     */
    public TestSignUpRequestValidation() {
        super();
    }

    /**
     * Sets up the validator for the tests.
     */
    @BeforeEach
    public final void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Verifies that valid values generate 0 errors.
     */
    @Test
    void testValidationNoError() {
        var signUpRequest = new SignUpRequestForm(
                validName, validFirstSurname, validSecondSurname,
                validBirthDate, validGender, validPassword, validEmail
        );
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator
                .validate(signUpRequest);
        Assertions.assertEquals(
                Integer.valueOf(0), violations.size(), "No errors"
        );
    }

    /**
     * Verifies validation Not blank in Name field.
     */
    @Test
    void testNameValidationNameNotBlank() {
        final String nameNullValue = null;
        var signUpRequest = new SignUpRequestForm(
                nameNullValue, validFirstSurname, validSecondSurname,
                validBirthDate, validGender, validPassword, validEmail
        );
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator
                .validate(signUpRequest);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(), "No errors"
        );

        violations.forEach(violation -> {
            Assertions.assertNull(
                    violation.getInvalidValue(), "null is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.NAME_NOT_BLANK_MESSAGE, violation.getMessage(),
                    "The message provided"
            );
            Assertions.assertTrue(
                    violation.getConstraintDescriptor()
                            .getAnnotation() instanceof NotBlank,
                    "Not blank annotation"
            );
        });

        final var nameBlankValue = "";
        signUpRequest = new SignUpRequestForm(
                nameBlankValue, validFirstSurname, validSecondSurname,
                validBirthDate, validGender, validPassword, validEmail
        );
        violations = validator.validate(signUpRequest);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(), "No errors"
        );
        violations.forEach(violation -> {
            Assertions.assertEquals(
                    nameBlankValue, violation.getInvalidValue(),
                    "nameBlankValue is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.NAME_NOT_BLANK_MESSAGE, violation.getMessage(),
                    "The message provided"
            );
            Assertions.assertTrue(
                    violation.getConstraintDescriptor()
                            .getAnnotation() instanceof NotBlank,
                    "Not blank annotation"
            );
        });
    }

    /**
     * Verifies validation name max length that is 25.
     */
    @Test
    void testValidationNameMaxLength() {
        final var long26Name = "Namedadsadasdsadadsadsadds";
        var signUpRequest = new SignUpRequestForm(
                long26Name, validFirstSurname, validSecondSurname,
                validBirthDate, validGender, validPassword, validEmail
        );
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator
                .validate(signUpRequest);
        Assertions.assertEquals(
                Integer.valueOf(1), violations.size(), "No errors"
        );
        violations.forEach(violation -> {
            Assertions.assertEquals(
                    long26Name, violation.getInvalidValue(),
                    "long26Name is the not valid value"
            );
            Assertions.assertEquals(
                    Constants.NAME_MAX_LENGTH_MESSAGE, violation.getMessage(),
                    "The message provided"
            );
            Assertions.assertTrue(
                    violation.getConstraintDescriptor()
                            .getAnnotation() instanceof Size,
                    "Not blank annotation"
            );
        });
        // With length of 25 pass with 0 errors.
        final var longValid25Name = "Namedadsadasdsadadsadsadd";
        signUpRequest = new SignUpRequestForm(
                longValid25Name, validFirstSurname, validSecondSurname,
                validBirthDate, validGender, validPassword, validEmail
        );
        violations = validator.validate(signUpRequest);
        Assertions.assertEquals(
                Integer.valueOf(0), violations.size(), "No errors"
        );
    }

    /**
     * Verifies that Password field cannot be blank.
     */
    @Test
    void testEqualsHashCodeRequest() {

        var ar = new SignUpRequestForm(
                validName, validFirstSurname, validSecondSurname,
                validBirthDate, validGender, validPassword, validEmail
        );
        Assertions.assertTrue(ar.equals(ar));
        Assertions.assertEquals(ar.hashCode(), ar.hashCode());
        Assertions.assertFalse(ar.equals(null));
        final var otherName = "Carlos";
        final var otherEmail = "other@email.com";
        var ar2 = new SignUpRequestForm(
                validName, validFirstSurname, validSecondSurname,
                validBirthDate, validGender, validPassword, otherEmail
        );

        Assertions.assertFalse(ar.equals(ar2));
        Assertions.assertNotEquals(ar.hashCode(), ar2.hashCode());
        Assertions.assertNotEquals(ar.toString(), ar2.toString());
        ar2.setEmail(validEmail);
        ar2.setName(otherName);
        Assertions.assertFalse(ar.equals(ar2));
        ar2.setName(validName);
        ar2.setGender("female");
        Assertions.assertFalse(ar.equals(ar2));
        ar2.setGender(validGender);
        var otherPassword = "12345607";
        ar2.setPassword(otherPassword);
        Assertions.assertFalse(ar.equals(ar2));

    }
}
