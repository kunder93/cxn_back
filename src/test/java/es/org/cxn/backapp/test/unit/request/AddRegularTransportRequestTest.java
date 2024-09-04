
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.math.BigInteger;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AddRegularTransportRequestTest {

  /**
   * Validator instance for performing constraint validation.
   */
  private static Validator validator;

  /**
   * Sets up the validator instance before all tests are run.
   */
  @BeforeAll
  static void setUpValidator() {

    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest(
        name = "Valid case with category='{0}', description='{1}',"
              + " invoiceNumber={2}, invoiceSeries='{3}'"
  )
  @CsvSource(
    { "ValidCategory, ValidDescription, 12345, Series01", // All valid inputs
        "Cat, Desc, 1, S", // Minimum valid length for each field
        "CategoryWithExactlyThirtyChars, DescriptionWithExactlyOneHundredCha"
              + "rssssssssssssssssssssssssssssssssssssssssssssssssssssssss"
              + "ssssssss, 12345, Series1234",
        // Max length for category and description, valid invoice series
        "Cat, Desc, 1, ValidSeris",
        // Minimal case for invoiceNumber, valid length for invoiceSeries
        "ValidCategory, ValidDescription, 99999, Series1234"
    // Max length for invoiceSeries, valid length for other fields
    }
  )
  @DisplayName("Valid cases for different field values")
  void testValidCases(
        final String category, final String description,
        final BigInteger invoiceNumber, final String invoiceSeries
  ) {
    final var form = new AddRegularTransportRequest(
          category, description, invoiceNumber, invoiceSeries
    );
    final Set<ConstraintViolation<AddRegularTransportRequest>> violations =
          validator.validate(form);

    if (!violations.isEmpty()) {
      for (ConstraintViolation<AddRegularTransportRequest> violation : violations) {
        System.out.println("Property Path: " + violation.getPropertyPath());
        System.out.println("Invalid Value: " + violation.getInvalidValue());
        System.out.println("Message: " + violation.getMessage());
      }
    }

    assertTrue(violations.isEmpty(), "No violations expected for valid input.");
  }

  @ParameterizedTest(
        name = "Invalid case with category='{0}', description='{1}',"
              + " invoiceNumber={2}, invoiceSeries='{3}'"
  )
  @CsvSource(
    { "CategoryWithThirtyOneCharssssss, ValidDescription, 12345, Series01",
        // Exceeds max length for category
        "ValidCategory, DescriptionWithMoreThanOneHundredCharsForTesting"
              + "PurposeToCheckValidationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
              + "nnnnnnnnnn, 12345, Series01",
        // Exceeds max length for description
        "ValidCategory, ValidDescription, -1, Series01",
        // Invalid invoiceNumber (assuming negative is invalid)
        "ValidCategory, ValidDescription, 12345, SriesMreTen"
    // Exceeds max length for invoiceSeries
    }
  )
  @DisplayName("Invalid cases for different field values")
  void testInvalidCases(
        final String category, final String description,
        final BigInteger invoiceNumber, final String invoiceSeries
  ) {
    final var form = new AddRegularTransportRequest(
          category, description, invoiceNumber, invoiceSeries
    );
    final Set<ConstraintViolation<AddRegularTransportRequest>> violations =
          validator.validate(form);

    assertFalse(
          violations.isEmpty(), "Violations are expected for invalid input."
    );
    for (ConstraintViolation<AddRegularTransportRequest> violation : violations) {
      System.out.println("Property Path: " + violation.getPropertyPath());
      System.out.println("Invalid Value: " + violation.getInvalidValue());
      System.out.println("Message: " + violation.getMessage());
    }
  }
}
