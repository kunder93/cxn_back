
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.org.cxn.backapp.model.form.requests.DeleteInvoiceRequest;
import es.org.cxn.backapp.model.form.requests.ValidationConstants;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit test for {@link DeleteInvoiceRequest}.
 * <p>
 * This test class verifies the validation of the {@link DeleteInvoiceRequest}
 * record using parameterized tests with different edge cases and invalid
 * scenarios.
 * </p>
 */
public class DeleteInvoiceRequestTest {

  /**
   * The validator.
   */
  private static Validator validator;

  /**
   * Sets up the validator factory and initializes the {@link Validator}
   * instance to be used for validating {@link DeleteInvoiceRequest} objects
   *  in the tests.
   * <p>
   * This method is annotated with {@link BeforeAll}, which means it will be
   * executed once before any of the test methods in this class are run.
   * It initializes the {@code validator} field using the default validator
   * factory provided by Jakarta Bean Validation (formerly known as JSR 380).
   * </p>
   * <p>
   * The validator is used to ensure that the objects being tested adhere to the
   * validation constraints defined in the {@link DeleteInvoiceRequest} class.
   * </p>
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests the validation of {@link DeleteInvoiceRequest} with various
   * invoice IDs.
   *
   * @param invoiceId The invoice ID to be tested.
   * @param expectedViolations The expected number of validation violations.
   */
  @ParameterizedTest(name = "invoiceId={0}, expectedViolations={1}")
  @CsvSource(
    { "0, 1", // Invalid: Not positive
        "1, 0", // Valid: Minimum positive value
        "1000, 0", // Valid: Maximum allowed value
        "1001, 1" // Invalid: Exceeds maximum allowed value
    }
  )
  void testInvoiceIdValidation(
        final Integer invoiceId, final int expectedViolations
  ) {
    var request = new DeleteInvoiceRequest(invoiceId);

    Set<ConstraintViolation<DeleteInvoiceRequest>> violations =
          validator.validate(request);

    assertEquals(
          expectedViolations, violations.size(),
          "The number of violations should match the expected number."
    );

    if (expectedViolations > 0) {
      var violation = violations.iterator().next();
      if (invoiceId == null) {
        assertEquals(
              ValidationConstants.ID_NOT_NULL_MESSAGE, violation.getMessage(),
              "The constraint message should indicate that the invoice ID"
                    + " cannot be null."
        );
      } else if (invoiceId <= 0) {
        assertEquals(
              ValidationConstants.ID_POSITIVE_MESSAGE, violation.getMessage(),
              "The constraint message should indicate that the invoice ID must"
                    + " be positive."
        );
      } else if (invoiceId > ValidationConstants.MAX_ID) {
        assertEquals(
              ValidationConstants.ID_MAX_MESSAGE, violation.getMessage(),
              "The constraint message should indicate that the invoice ID must"
                    + " not exceed 1000."
        );
      }
    }
  }
}
