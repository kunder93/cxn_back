
package es.org.cxn.backapp.test.unit.validation;

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

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.user.UserType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Unit tests for validating {@link SignUpRequestForm} using Bean Validation
 * (JSR 380). These tests cover a range of scenarios for the form fields to
 * ensure that validation constraints are correctly enforced.
 * <p>
 * The tests check for valid and invalid values across various fields of the
 * {@link SignUpRequestForm} class, such as DNI, name, and other form fields.
 * Each test method is designed to assert that the form behaves as expected
 * under different conditions.
 * <p>
 * This class uses {@link jakarta.validation.Validator} to validate the form and
 * checks that the constraints are correctly applied and reported.
 * </p>
 *
 * <p>
 * Author: Santiago Paz Perez
 * </p>
 */
final class TestSignUpRequestValidation {
    /**
     * A valid name used for testing purposes. Represents a typical first name for a
     * user in form validation tests.
     */
    private static final String VALID_NAME = "Jacinto";

    /**
     * A valid first surname used for testing purposes. Represents a typical first
     * surname for a user in form validation tests.
     */
    private static final String VALID_FIRST_SURNAME = "Anacletio";

    /**
     * A valid second surname used for testing purposes. Represents a typical second
     * surname for a user in form validation tests.
     */
    private static final String VALID_SECOND_SURNAME = "Juartillo";

    /**
     * A valid birth date used for testing purposes. Represents a typical date of
     * birth for a user in form validation tests.
     */
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.ofYearDay(1993, 2);

    /**
     * A valid gender value used for testing purposes. Represents a typical gender
     * for a user in form validation tests.
     */
    private static final String VALID_GENDER = "male";

    /**
     * A valid password used for testing purposes. Represents a typical valid
     * password for a user in form validation tests.
     */
    private static final String VALID_PASSWORD = "OneValidPassword";

    /**
     * A valid email address used for testing purposes. Represents a typical valid
     * email address for a user in form validation tests.
     */
    private static final String VALID_EMAIL = "santi@santi.es";

    /**
     * A valid DNI (Documento Nacional de Identidad) used for testing purposes.
     * Represents a typical valid DNI for a user in form validation tests.
     */
    private static final String VALID_DNI = "32721859N";

    /**
     * A valid postal code used for testing purposes. Represents a typical postal
     * code for a user in form validation tests.
     */
    private static final String VALID_POSTAL_CODE = "15570";

    /**
     * A valid apartment number used for testing purposes. Represents a typical
     * apartment number for a user in form validation tests.
     */
    private static final String VALID_APARTMENT_NUMBER = "4";

    /**
     * A valid building name used for testing purposes. Represents a typical
     * building name for a user in form validation tests.
     */
    private static final String VALID_BUILDING = "fed";

    /**
     * A valid street name used for testing purposes. Represents a typical street
     * name for a user in form validation tests.
     */
    private static final String VALID_STREET = "Rua Marina Española";

    /**
     * A valid city name used for testing purposes. Represents a typical city name
     * for a user in form validation tests.
     */
    private static final String VALID_CITY = "Naron";

    /**
     * A valid country numeric code used for testing purposes. Represents a typical
     * numeric code for a country in form validation tests.
     */
    private static final Integer VALID_COUNTRY_NUMERIC_CODE = Integer.valueOf(340);

    /**
     * A valid country subdivision name used for testing purposes. Represents a
     * typical country subdivision name for a user in form validation tests.
     */
    private static final String VALID_COUNTRY_SUBDIVISION_NAME = "Angunio";

    /**
     * A valid user type used for testing purposes. Represents a typical user type
     * for a user in form validation tests.
     */
    private static final UserType VALID_KIND_MEMBER = UserType.SOCIO_NUMERO;

    /**
     * A date before today.
     */
    private static final LocalDate TOMORROW = LocalDate.now().minusDays(1);

    /**
     * An example past date.
     */
    private static final LocalDate VALID_DATE_1 = LocalDate.of(1993, 6, 15);
    /**
     * An example older past date.
     */
    private static final LocalDate VALID_DATE_2 = LocalDate.of(1930, 3, 10);
    /**
     * Validator instance used to perform validation on {@link SignUpRequestForm}
     * objects. This instance is initialized before each test method runs.
     */
    private Validator validator;

    /**
     * Default constructor.
     */
    TestSignUpRequestValidation() {
        super();
    }

    /**
     * @return LocalDate cases for birth date field test.
     */
    private static Stream<LocalDate> dateFutureProvider() {
        return Stream.of(/* after today */ LocalDate.now().plusDays(1), /* others */ LocalDate.now(),
                LocalDate.now().plusMonths(1));
    }

    /**
     * Provides a stream of LocalDate values for testing purposes.
     *
     * @return A stream of LocalDate values representing different past dates.
     */
    private static Stream<LocalDate> datePastProvider() {
        return Stream.of(TOMORROW, // Represents a date before today
                VALID_DATE_1, // Represents a specific valid past date
                VALID_DATE_2 // Represents another specific valid past date
        );
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
     * Verifies validation password must not be blank.
     *
     * @param password The sing up request password.
     */
    @DisplayName("Validate password cannot be blank or spaces.")
    @ParameterizedTest
    @ValueSource(strings = { "", " ", "                 " })
    void testBlankPasswordNotValid(final String password) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, password, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);

        var violationsSizeMatch = violations.size() == 2 || violations.size() == 1;
        Assertions.assertTrue(violationsSizeMatch, "2 or 1 constraint violation");

        violations.forEach(constraintViolation -> {
            var match = constraintViolation.getMessage().equals(Constants.PASSWORD_NOT_BLANK_MESSAGE)
                    || constraintViolation.getMessage().equals(Constants.PASSWORD_SIZE_MESSAGE);
            Assertions.assertTrue(match, "Must be one of two constrant validation messages.");
            Assertions.assertEquals(signUpRequest.password(), constraintViolation.getInvalidValue(),
                    "The constraint message is about password.");

        });

    }

    /**
     * Verifies validation email cannot be more than 50 characters.
     *
     * @param email sign up request email.
     */
    @DisplayName("Validate email no valid upper max length.")
    @ParameterizedTest
    @ValueSource(strings = { /* 51 */"aaaaaaaaaaaaaverylongEmailExample@VeryLongEmail.com",
            /* 52 */ "baaaaaaaaaaaaaverylongEmailExample@VeryLongEmail.com" })
    void testEmailLengthOutBoundsNotValid(final String email) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, email, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One error.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.MAX_SIZE_EMAIL_MESSAGE, constraintViolation.getMessage(),
                "The constraint message is about email.");
        Assertions.assertEquals(signUpRequest.email(), constraintViolation.getInvalidValue(),
                "The constraint message is about email.");
    }

    /**
     * Verifies validation Not blank in first surname field.
     *
     * @param firstSurname The name not valid value.
     */
    @DisplayName("Validate blank name values are not valid.")
    @ParameterizedTest
    @ValueSource(strings = { "", " ", "    " })
    void testFirstSurnameBlankNotValid(final String firstSurname) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurname, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One constraint violation.");

        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.FIRST_SURNAME_NOT_BLANK, constraintViolation.getMessage(),
                "The constraint message is about first surname not blank.");
        Assertions.assertEquals(signUpRequest.firstSurname(), constraintViolation.getInvalidValue(),
                "The constraint message is about first surname.");
    }

    /**
     * Verifies validation first surname field length.
     *
     * @param firstSurname The firstSurname not valid value.
     */
    @DisplayName("Validate firstSurname length must not be upper than 25.")
    @ParameterizedTest
    @ValueSource(strings = { /* 26 */ "12345678901234567890123456", /* 27 */"12345678901234567890123456",
            /* 26 with spaces */"14  56789012345     012345",
            /* 26 with special characters */"dsadsadadwd€€14@|#|4€#€¬~@" })
    void testFirstSurnameLengthNotValid(final String firstSurname) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurname, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One constraint violation.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.FIRST_SURNAME_MAX_LENGTH_MESSAGE, constraintViolation.getMessage(),
                "The constraint message is about first surname length.");
        Assertions.assertEquals(signUpRequest.firstSurname(), constraintViolation.getInvalidValue(),
                "The constraint message is about first surname.");
    }

    /**
     * Verifies validation birth must be past.
     *
     * @param birthDate The sign up request password.
     */
    @DisplayName("Validate birthday date must be past.")
    @ParameterizedTest
    @MethodSource("dateFutureProvider")
    void testFutureBirthDateNotValid(final LocalDate birthDate) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                birthDate, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One error.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.BIRTH_DATE_PAST, constraintViolation.getMessage(),
                "The constraint message is about birth date.");
        Assertions.assertEquals(signUpRequest.birthDate(), constraintViolation.getInvalidValue(),
                "The constraint message is about birth date.");

    }

    /**
     * Verifies validation Not blank in Name field.
     *
     * @param name The name not valid value.
     */
    @DisplayName("Validate blank name values are not valid.")
    @ParameterizedTest
    @ValueSource(strings = { "", " ", "    " })
    void testNameBlankNotValid(final String name) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, name, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One constraint violation.");
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One dni violation.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.NAME_NOT_BLANK, constraintViolation.getMessage(),
                "The constraint message is about name not blank.");
        Assertions.assertEquals(signUpRequest.name(), constraintViolation.getInvalidValue(),
                "The constraint message is about name.");
    }

    /**
     * Verifies validation Name field length.
     *
     * @param name The name not valid value.
     */
    @DisplayName("Validate name length must not be upper than 25.")
    @ParameterizedTest
    @ValueSource(strings = { /* 26 */ "12345678901234567890123456", /* 27 */"12345678901234567890123456",
            /* with spaces */"14  56789012345     012345",
            /* 26 with special characters */"dsadsadadwd€€14@|#|4€#€¬~@" })
    void testNameLengthNotValid(final String name) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, name, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One constraint violation.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.NAME_MAX_LENGTH_MESSAGE, constraintViolation.getMessage(),
                "The constraint message is about name not blank.");
        Assertions.assertEquals(signUpRequest.name(), constraintViolation.getInvalidValue(),
                "The constraint message is about name.");
    }

    /**
     * Verifies validation Name field length.
     *
     * @param name The name valid value.
     */
    @DisplayName("Validate name length must not be upper than 25.")
    @ParameterizedTest
    @ValueSource(strings = { /* 25 */ "1234678901234567890123456", /* 24 */"12345678904567890123456", /* 1 */"1",
            /* 1 special character */"€", /* 25 with special characters */"dsdsadadwd€€14@|#|4€#€¬~@" })
    void testNameValid(final String name) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, name, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(0), violations.size(), "No constraint violations.");
    }

    /**
     * Verifies that DNI with more or less 8 numbers and Letter in other order
     * generates validation error.
     *
     * @param dniValue the DNI values for validate.
     */
    @DisplayName("Validate not valid format values for DNI.")
    @ParameterizedTest
    @ValueSource(strings = { "3272189N", "332721860J", "T00000000", "99999R9999", "12A34567A8", "12345678", "123456789",
            "AAAAAAAAA" })
    void testNotValidDni(final String dniValue) {
        var signUpRequest = new SignUpRequestForm(dniValue, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One dni violation.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.DNI_BAD_FORMAT, constraintViolation.getMessage(),
                "The message is about dni format.");
        Assertions.assertEquals(signUpRequest.dni(), constraintViolation.getInvalidValue(),
                "The message is about dni format.");
    }

    /**
     * Verifies validation null dni is not valid.
     */
    @DisplayName("Validate not valid null value for DNI.")
    @Test
    void testNullDniNotValid() {
        final String dniValue = null;
        var signUpRequest = new SignUpRequestForm(dniValue, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One error.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.DNI_BAD_FORMAT, constraintViolation.getMessage(),
                "The message is about dni format.");
        Assertions.assertEquals(signUpRequest.dni(), constraintViolation.getInvalidValue(),
                "The message is about dni format.");
    }

    /**
     * Verifies validation null first surname is not valid.
     */
    @DisplayName("Validate not valid null value for first surname.")
    @Test
    void testNullFirstSurnameNotValid() {
        final String firstSurname = null;
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurname, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One error.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.FIRST_SURNAME_NOT_BLANK, constraintViolation.getMessage(),
                "The message is about first surname.");
        Assertions.assertEquals(signUpRequest.firstSurname(), constraintViolation.getInvalidValue(),
                "The message is about first surname format.");
    }

    /**
     * Verifies validation null name is not valid.
     */
    @DisplayName("Validate not valid null value for DNI.")
    @Test
    void testNullNameNotValid() {
        final String nameValue = null;
        var signUpRequest = new SignUpRequestForm(VALID_DNI, nameValue, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One error.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.NAME_NOT_BLANK, constraintViolation.getMessage(),
                "The message is about dni format.");
        Assertions.assertEquals(signUpRequest.name(), constraintViolation.getInvalidValue(),
                "The message is about dni format.");
    }

    /**
     * Verifies validation birth must be past.
     *
     * @param birthDate The sign up request date.
     */
    @DisplayName("Validate birthday date must be past.")
    @ParameterizedTest
    @MethodSource("datePastProvider")
    void testOnlyPastBirthDateAreValid(final LocalDate birthDate) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                birthDate, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(0), violations.size(), "No constraint violations.");
    }

    /**
     * Verifies validation password must be between min(6) and max(20) length.
     *
     * @param password
     */
    @DisplayName("Validate password no valid length.")
    @ParameterizedTest
    @ValueSource(strings = { /* 4 */"1234", /* 22 */ "12345", /* 21 */"123456789012345678901",
            /* 22 */ "1234567890123456789012" })
    void testPasswordLengthOutBoundsNotValid(final String password) {
        var signUpRequest = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, password, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(1), violations.size(), "One error.");
        var constraintViolation = violations.iterator().next();
        Assertions.assertEquals(Constants.PASSWORD_SIZE_MESSAGE, constraintViolation.getMessage(),
                "The constraint message is about password.");
        Assertions.assertEquals(signUpRequest.password(), constraintViolation.getInvalidValue(),
                "The constraint message is about password.");

    }

    /**
     * Verifies that DNI with 8 numbers and Letter (TRWAGMYFPDXBNJZSQVHLCKE) are
     * valid.
     *
     * @param dniValue the DNI values for validate.
     */
    @DisplayName("Validate valid values for DNI.")
    @ParameterizedTest
    @ValueSource(strings = { "32721859N", "32721860J", "00000000T", "99999999R", "12345678A" })
    void testValidDni(final String dniValue) {
        var signUpRequest = new SignUpRequestForm(dniValue, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequest);
        Assertions.assertEquals(Integer.valueOf(0), violations.size(), "No dni violations.");
    }

}
