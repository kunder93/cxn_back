
package es.org.cxn.backapp.test.unit.request;

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

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.persistence.user.UserType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Unit test for {@link UserChangeKindMemberRequest}.
 * <p>
 * This test class verifies the validation of the
 * {@link UserChangeKindMemberRequest} record using parameterized tests with
 * various valid and invalid values for email and kindMember.
 * </p>
 */
public class UserChangeKindMemberRequestTest {

    /**
     * The validator.
     */
    private static Validator validator;

    /**
     * Sets up the validator factory and initializes the {@link Validator} instance
     * to be used for validating {@link UserChangeKindMemberRequest} objects in the
     * tests.
     * <p>
     * This method is annotated with {@link BeforeAll}, which means it will be
     * executed once before any of the test methods in this class are run. It
     * initializes the {@code validator} field using the default validator factory
     * provided by Jakarta Bean Validation (formerly known as JSR 380).
     * </p>
     * <p>
     * The validator is used to ensure that the objects being tested adhere to the
     * validation constraints defined in the {@link UserChangeKindMemberRequest}
     * class.
     * </p>
     */
    @BeforeAll
    public static void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests the validation of {@link UserChangeKindMemberRequest} with various
     * email addresses and kindMember values.
     *
     * @param email              The email address to be tested.
     * @param kindMember         The type of member to be tested.
     * @param expectedViolations The expected number of validation violations.
     */
    @ParameterizedTest(name = "email={0}, kindMember={1}, expectedViolations={2}")
    @CsvSource({ "'valid.email@example.com', 'SOCIO_NUMERO', 0", // Valid case
            "'invalid-email', 'SOCIO_NUMERO', 1", // Invalid: Email is invalid
            "'valid.email@example.com', '', 1", // Invalid: KindMember is blank
            "'', 'SOCIO_NUMERO', 1", // Invalid: Email is blank
            "'valid.email@example.com', 'INVALID_TYPE', 1"
    // Invalid: Invalid kindMember value
    })
    void testUserChangeKindMemberRequestValidation(final String email, final String kindMember,
            final int expectedViolations) {
        UserType kindMemberEnum = null;
        if (!kindMember.isEmpty()) {
            try {
                kindMemberEnum = UserType.valueOf(kindMember);
            } catch (IllegalArgumentException e) {
                // Enum value is invalid; it will be null
            }
        }
        var request = new UserChangeKindMemberRequest(email, kindMemberEnum);

        Set<ConstraintViolation<UserChangeKindMemberRequest>> violations = validator.validate(request);

        assertEquals(expectedViolations, violations.size(),
                "The number of violations should match the expected number.");
    }
}
