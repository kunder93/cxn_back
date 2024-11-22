package es.org.cxn.backapp.test.unit.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.requests.AddActivityRequestData;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TestAddActivityRequest {

    /**
     * Validator instance used to perform validation on the
     * {@link AddActivityRequestData} objects. This validator is configured using
     * Jakarta Bean Validation (JSR 303/380) and is used to ensure that the fields
     * of the {@link AddActivityRequestData} class meet the validation constraints
     * defined on them.
     */
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCategoryNotNull() {
        AddActivityRequestData request = new AddActivityRequestData("Valid Title", "This is a valid description.",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), null);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Category must not be null.");
        assertEquals("category", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testDescriptionNotNull() {

        AddActivityRequestData request = new AddActivityRequestData("Valid Title", null,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Category");

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Description must not be null.");
        assertEquals("description", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testEndDateNotNull() {
        AddActivityRequestData request = new AddActivityRequestData("Valid Title", "This is a valid description.",
                LocalDateTime.now().plusDays(1), null, "Category");

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "End date must not be null.");
        assertEquals("endDate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testStartDateNotNull() {

        AddActivityRequestData request = new AddActivityRequestData("Valid Title", "This is a valid description.", null,
                LocalDateTime.now().plusDays(2), "Category");

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Start date must not be null.");
        assertEquals("startDate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testTitleNotNull() {

        AddActivityRequestData request = new AddActivityRequestData(null, "This is a valid description.",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Category");

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Title must not be null.");
        assertEquals("title", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testValidAddActivityRequest() {
        AddActivityRequestData request = new AddActivityRequestData("Valid Title",
                "This is a valid description of the activity.", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "Category");

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Request should be valid.");
    }
}
