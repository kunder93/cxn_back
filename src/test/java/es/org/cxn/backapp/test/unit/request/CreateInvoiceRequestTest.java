
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link CreateInvoiceRequest} class.
 * <p>
 * These tests validate the constraints and logic applied to the fields in
 * {@link CreateInvoiceRequest}. The tests cover both valid and invalid
 * cases, ensuring that the validation framework correctly identifies
 * compliance with or violation of the constraints.
 * </p>
 */
public class CreateInvoiceRequestTest {

  /**
   * A {@link Validator} instance for validating
   * {@link CreateInvoiceRequest} objects against the defined validation
   * constraints.
   */
  private static Validator validator;

  /**
   * Sets up the test environment by initializing the {@link Validator}
   * instance.
   * This method is executed once before any test methods in the class.
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests valid cases for the {@link CreateInvoiceRequest}.
   * <p>
   * This parameterized test verifies that the form is correctly validated as
   * valid when all fields contain valid values, including frontier values
   * that test the boundaries of acceptable input.
   * </p>
   *
   * @param number              the invoice number (expected to be non-null and
   *                            within a valid range).
   * @param series              the invoice series (expected to be non-null and
   *                            within the valid length).
   * @param advancePaymentDate  the advance payment date (expected to be a valid
   *                            {@link LocalDate}).
   * @param expeditionDate      the expedition date (expected to be a valid
   *                            {@link LocalDate}).
   * @param taxExempt           whether the invoice is tax-exempt (expected to
   *                            be non-null).
   * @param sellerNif           the NIF of the seller (expected to be non-null
   *                            and of valid length).
   * @param buyerNif            the NIF of the buyer (expected to be non-null
   *                            and of valid length).
   */
  @ParameterizedTest(
        name = "Valid case: number={0}, series={1}, advancePaymentDate={2},"
              + " expeditionDate={3}, taxExempt={4}, sellerNif={5},"
              + " buyerNif={6}"
  )
  @CsvSource(
    {
        // Valid cases with frontier values
        "1, Series, 2024-08-01, 2024-08-01, false, 123456789012345678901234567"
              + "8901234567890, 1234567890123456789012345678901234567890",
        // Minimum value for invoice number.
        "1000, MaxSeries, 2024-08-01, 2024-08-01, true, 1234567890123456789012"
              + "345678901234567890, 1234567890123456789012345678901234567890",
        // Maximum value for number
        "1, 10charseri, 2024-08-01, 2024-08-01, false, 123456789012345678901"
              + "2345678901234567890, 1234567890123456789012345678901234567890",
    // Max series length.
    }
  )
  @DisplayName("Valid cases with frontier values for CreateInvoiceRequest")
  void testValidCases(
        final BigInteger number, final String series,
        final LocalDate advancePaymentDate, final LocalDate expeditionDate,
        final Boolean taxExempt, final String sellerNif, final String buyerNif
  ) {
    // Given: A valid CreateInvoiceRequest with the provided parameters
    final var form = new CreateInvoiceRequest(
          number, series, advancePaymentDate, taxExempt, sellerNif, buyerNif,
          expeditionDate
    );

    // When: The form is validated
    final Set<ConstraintViolation<CreateInvoiceRequest>> violations =
          validator.validate(form);

    // Then: No violations should be found
    assertTrue(violations.isEmpty(), "No violations expected for valid input.");
  }

  /**
   * Tests invalid cases for the {@link CreateInvoiceRequest}.
   * <p>
   * This parameterized test verifies that the form is correctly validated as
   * invalid when fields contain values that violate the validation constraints.
   * It checks various scenarios, including null values, values exceeding the
   * allowed length, and other boundary conditions.
   * </p>
   *
   * @param number              the invoice number (which may be null, negative,
   *                            or out of the valid range).
   * @param series              the invoice series (which may be null or exceed
   *                            the valid length).
   * @param advancePaymentDate  the advance payment date (expected to be a valid
   *                            {@link LocalDate}).
   * @param expeditionDate      the expedition date (expected to be a valid
   *                            {@link LocalDate}).
   * @param taxExempt           whether the invoice is tax-exemp
                                (which may be null).
   * @param sellerNif           the NIF of the seller (which may be null or
   *                            exceed the valid length).
   * @param buyerNif            the NIF of the buyer (which may be null or
   *                            exceed the valid length).
   */
  @ParameterizedTest(
        name = "Invalid case: number={0}, series={1}, advancePaymentDate={2},"
              + " expeditionDate={3}, taxExempt={4}, sellerNif={5},"
              + " buyerNif={6}"
  )
  @CsvSource(
    {
        // Invalid cases with frontier values
        ", ValidSeries, 2024-08-01, 2024-08-01, true, 12345678901234567890123"
              + "45678901234567890, 123456789012345678901234567890123456"
              + "7890", // Null number.
        "1001, ValidSeries, 2024-08-01, 2024-08-01, true, 1234567890123456789"
              + "012345678901234567890, 1234567890123456789012345"
              + "678901234567890", // Number exceeding max value.
        "0, ValidSeries, 2024-08-01, 2024-08-01, true, 1234567890123456789012"
              + "345678901234567890, 1234567890123456789012345678901"
              + "234567890", // Number cannot be 0.
        "-1, ValidSeries, 2024-08-01, 2024-08-01, true, 12345678901234567"
              + "89012345678901234567890, 12345678901234567890123456789012"
              + "34567890", // Number cannot be negative.
        "123456, , 2024-08-01, 2024-08-01, true, 123456789012345678901234567"
              + "8901234567890, 12345678901234567890123456789012345" + "67890",
        // Blank series
        "123456, 11chExceeds, 2024-08-01, 2024-08-01, true, 123456789012345678"
              + "9012345678901234567890, 1234567890123456789012345"
              + "678901234567890", // Series exceeding max length
        "123456, ValidSeries, 2024-08-01, 2024-08-01, , 12345678901234567890"
              + "12345678901234567890, 123456789012345678901234567890123"
              + "4567890", // Null taxExempt
        "123456, ValidSeries, 2024-08-01, 2024-08-01, true, , 1234567890123456"
              + "789012345678901234567890", // Blank sellerNif
        "123456, ValidSeries, 2024-08-01, 2024-08-01, true, 123456789012345678"
              + "9012345678901234567890, " // Blank buyerNif

    }
  )
  @DisplayName("Invalid cases for CreateInvoiceRequest")
  void testInvalidCases(
        final BigInteger number, final String series,
        final LocalDate advancePaymentDate, final LocalDate expeditionDate,
        final Boolean taxExempt, final String sellerNif, final String buyerNif
  ) {
    // Given: An invalid CreateInvoiceRequest with the provided parameters
    final var form = new CreateInvoiceRequest(
          number, series, advancePaymentDate, taxExempt, sellerNif, buyerNif,
          expeditionDate
    );

    // When: The form is validated
    final Set<ConstraintViolation<CreateInvoiceRequest>> violations =
          validator.validate(form);

    // Then: Violations should be found
    assertFalse(violations.isEmpty(), "Violations expected for invalid input.");
  }
}
