
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

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.UserChangeRoleRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Unit test for {@link UserChangeRoleRequest}.
 * <p>
 * This test class verifies the validation of the {@link UserChangeRoleRequest}
 * record using parameterized tests with various valid and invalid values for
 * email and userRoles.
 * </p>
 */
public class UserChangeRoleRequestTest {

    /**
     * The validator.
     */
    private static Validator validator;

    /**
     * Sets up the validator factory and initializes the {@link Validator} instance
     * to be used for validating {@link UserChangeRoleRequest} objects in the tests.
     * <p>
     * This method is annotated with {@link BeforeAll}, which means it will be
     * executed once before any of the test methods in this class are run. It
     * initializes the {@code validator} field using the default validator factory
     * provided by Jakarta Bean Validation (formerly known as JSR 380).
     * </p>
     * <p>
     * The validator is used to ensure that the objects being tested adhere to the
     * validation constraints defined in the {@link UserChangeRoleRequest} class.
     * </p>
     */
    @BeforeAll
    public static void setup() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests the validation of {@link UserChangeRoleRequest} with various email
     * addresses and userRoles lists.
     *
     * @param email              The email address to be tested.
     * @param userRoles          The user roles to be tested, provided as a
     *                           comma-separated list of roles.
     * @param expectedViolations The expected number of validation violations.
     */
    @ParameterizedTest(name = "email={0}, userRoles={1}, expectedViolations={2}")
    @CsvSource({
            // Valid case
            "'valid.email@example.com', 'ROLE_ADMIN,ROLE_SOCIO', 0",

            // Invalid cases
            "'invalid-email', 'ROLE_ADMIN,ROLE_SOCIO', 1", // Invalid email format
            "'valid.email@example.com','' , 1", // Empty userRoles list
            "'valid.email@example.com','' , 1", // Empty userRoles list
            " , 'ROLE_ADMIN,ROLE_SOCIO', 1" // Empty email
    })
    void testUserChangeRoleRequestFormValidation(final String email, final String userRoles,
            final int expectedViolations) {
        // Convert comma-separated userRoles to List<UserRoleName>
        List<UserRoleName> rolesList = userRoles.isEmpty() || userRoles.trim().isEmpty() ? List.of()
                : List.of(userRoles.split(",")).stream().map(UserRoleName::valueOf).toList();

        var request = new UserChangeRoleRequest(email, rolesList);

        Set<ConstraintViolation<UserChangeRoleRequest>> violations = validator.validate(request);

        assertEquals(expectedViolations, violations.size(),
                "The number of violations should match the expected number.");
    }
}
