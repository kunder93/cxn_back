package es.org.cxn.backapp.test.unit.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.form.requests.AddActivityRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TestAddActivityRequest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCategoryNotNull() {
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImageFile.isEmpty()).thenReturn(true);
        AddActivityRequest request = new AddActivityRequest("Valid Title", "This is a valid description.",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), null, mockImageFile);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Category must not be null.");
        assertEquals("category", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testDescriptionNotNull() {
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImageFile.isEmpty()).thenReturn(true);
        AddActivityRequest request = new AddActivityRequest("Valid Title", null, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "Category", mockImageFile);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Description must not be null.");
        assertEquals("description", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testEndDateNotNull() {
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImageFile.isEmpty()).thenReturn(true);
        AddActivityRequest request = new AddActivityRequest("Valid Title", "This is a valid description.",
                LocalDateTime.now().plusDays(1), null, "Category", mockImageFile);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "End date must not be null.");
        assertEquals("endDate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testImageFileValid() {
        // Mocking a MultipartFile instance
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);

        // Simulating a non-empty image file
        Mockito.when(mockImageFile.isEmpty()).thenReturn(false);

        // Simulating a valid content type for the image file
        Mockito.when(mockImageFile.getContentType()).thenReturn("image/jpeg");

        // Simulating a valid file size
        Mockito.when(mockImageFile.getSize()).thenReturn(1024L); // Example size in bytes

        // Creating a valid AddActivityRequest with the mocked image file
        AddActivityRequest request = new AddActivityRequest("Valid Title", "This is a valid description.",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Category", mockImageFile);

        // Validating the request
        var violations = validator.validate(request);

        // Assert that there are no violations since the image file should be valid
        assertTrue(violations.isEmpty(), "Image file should be valid. No violations expected.");
    }

    @Test
    void testStartDateNotNull() {
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImageFile.isEmpty()).thenReturn(true);
        AddActivityRequest request = new AddActivityRequest("Valid Title", "This is a valid description.", null,
                LocalDateTime.now().plusDays(2), "Category", mockImageFile);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Start date must not be null.");
        assertEquals("startDate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testTitleNotNull() {
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImageFile.isEmpty()).thenReturn(true);
        AddActivityRequest request = new AddActivityRequest(null, "This is a valid description.",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Category", mockImageFile);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Title must not be null.");
        assertEquals("title", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testValidAddActivityRequest() {
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImageFile.isEmpty()).thenReturn(true);

        AddActivityRequest request = new AddActivityRequest("Valid Title",
                "This is a valid description of the activity.", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "Category", mockImageFile);

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Request should be valid.");
    }
}
