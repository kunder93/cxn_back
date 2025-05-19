
package es.org.cxn.backapp.test.unit.request;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.requests.team.CreateTeamRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Unit test class for {@link CreateTeamRequest}.
 *
 * This class verifies the validation constraints on the
 * {@code CreateTeamRequest} object, including non-null, non-blank, and maximum
 * length restrictions.
 */
final class CreateTeamRequestTest {

    /**
     * Validator instance.
     */
    private static Validator validator;

    /**
     * Sets up the validator before running the tests.
     */
    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests that a blank category does not pass validation.
     */
    @Test
    void testCategoryBlank() {
        CreateTeamRequest request = new CreateTeamRequest("Valid Name", "Valid Description", "");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for blank category");
    }

    /**
     * Tests that a {@code null} category does not pass validation.
     */
    @Test
    void testCategoryNull() {
        CreateTeamRequest request = new CreateTeamRequest("Valid Name", "Valid Description", null);
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for null category");
    }

    /**
     * Tests that a category exceeding the maximum length of 255 characters does not
     * pass validation.
     */
    @Test
    void testCategoryTooLong() {
        String longCategory = "a".repeat(256);
        CreateTeamRequest request = new CreateTeamRequest("Valid Name", "Valid Description", longCategory);
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for category length");
    }

    /**
     * Tests that a blank description does not pass validation.
     */
    @Test
    void testDescriptionBlank() {
        CreateTeamRequest request = new CreateTeamRequest("Valid Name", "", "Valid Category");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for blank description");
    }

    /**
     * Tests that a {@code null} description does not pass validation.
     */
    @Test
    void testDescriptionNull() {
        CreateTeamRequest request = new CreateTeamRequest("Valid Name", null, "Valid Category");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for null description");
    }

    /**
     * Tests that a description exceeding the maximum length of 255 characters does
     * not pass validation.
     */
    @Test
    void testDescriptionTooLong() {
        String longDescription = "a".repeat(256);
        CreateTeamRequest request = new CreateTeamRequest("Valid Name", longDescription, "Valid Category");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for description length");
    }

    /**
     * Tests that a blank name does not pass validation.
     */
    @Test
    void testNameBlank() {
        CreateTeamRequest request = new CreateTeamRequest("", "Valid Description", "Valid Category");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for blank name");
    }

    /**
     * Tests that a {@code null} name does not pass validation.
     */
    @Test
    void testNameNull() {
        CreateTeamRequest request = new CreateTeamRequest(null, "Valid Description", "Valid Category");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for null name");
    }

    /**
     * Tests that a name exceeding the maximum length of 100 characters does not
     * pass validation.
     */
    @Test
    void testNameTooLong() {
        String longName = "a".repeat(101);
        CreateTeamRequest request = new CreateTeamRequest(longName, "Valid Description", "Valid Category");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected validation error for name length");
    }

    /**
     * Tests that a valid {@link CreateTeamRequest} object passes validation without
     * any errors.
     */
    @Test
    void testValidRequest() {
        CreateTeamRequest request = new CreateTeamRequest("Valid Name", "Valid Description", "Valid Category");
        Set<ConstraintViolation<CreateTeamRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }
}
