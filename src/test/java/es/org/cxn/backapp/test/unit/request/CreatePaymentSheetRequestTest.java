
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the CreatePaymentSheetRequest class.
 *
 * This test class covers validation scenarios for date ranges,
 * user email, reason, and place fields in the CreatePaymentSheetRequest.
 */
class CreatePaymentSheetRequestTest {

  /**
   * ValidatorFactory instance to create Validator instances.
   * This factory provides a way to access the default validation framework.
   */
  private final ValidatorFactory factory =
        Validation.buildDefaultValidatorFactory();

  /**
   * Validator instance used for validating the CreatePaymentSheetRequest
   * objects.
   * This validator is initialized using the ValidatorFactory.
   */
  private final Validator validator = factory.getValidator();

  /**
   * DateTimeFormatter used for parsing date strings in the format "yyyy-MM-dd".
   * This formatter ensures that the dates provided in the test cases are
   * correctly parsed into LocalDate objects.
   */
  private final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Test cases for valid date ranges.
   *
   * @param startDateStr the start date as a string
   * @param endDateStr   the end date as a string
   */
  @ParameterizedTest(name = "{index} => startDate={0}, endDate={1}")
  @CsvSource(
    { "2024-09-01,2024-09-01", // Start and End date are the same
        "2024-09-01,2024-09-02", // End date is after Start date
        "2024-01-01,2024-12-31" // Large date range
    }
  )
  @DisplayName("Test valid date ranges")
  void testValidDates(final String startDateStr, final String endDateStr) {
    final var startDate = LocalDate.parse(startDateStr, formatter);
    final var endDate = LocalDate.parse(endDateStr, formatter);

    assertDoesNotThrow(
          () -> new CreatePaymentSheetRequest(
                "user@example.com", "Conference", "New York", startDate, endDate
          ), "Expected valid date range to not throw an exception."
    );
  }

  /**
   * Test cases for invalid date ranges.
   *
   * @param startDateStr the start date as a string
   * @param endDateStr   the end date as a string
   */
  @ParameterizedTest(name = "{index} => startDate={0}, endDate={1}")
  @CsvSource(
    { "2024-09-02,2024-09-01", // End date before Start date
        "2024-09-01,2023-09-01", // End date in a previous year
        "2024-12-31,2024-01-01" // End date in an earlier month
    }
  )
  @DisplayName("Test invalid date ranges")
  void testInvalidDates(final String startDateStr, final String endDateStr) {
    final var startDate = LocalDate.parse(startDateStr, formatter);
    final var endDate = LocalDate.parse(endDateStr, formatter);

    Exception exception = assertThrows(
          IllegalArgumentException.class,
          () -> new CreatePaymentSheetRequest(
                "user@example.com", "Conference", "New York", startDate, endDate
          ), "Expected invalid date range to throw an IllegalArgumentException."
    );

    final var expectedMessage = "End date must be on or after start date";
    final var actualMessage = exception.getMessage();

    assertTrue(
          actualMessage.contains(expectedMessage),
          "Expected exception message to contain: " + expectedMessage
    );
  }

  @ParameterizedTest(name = "{index} => userEmail={0}, reason={1}, place={2}")
  @CsvSource(
    {
        // Failing examples
        "'not-an-email', 'Valid Reason', 'Valid Place'", // Invalid email format
        "    , 'Valid Reason', 'Valid Place'", // Blank email
        "fiftyone.chars.email.chars.email.fiftyone@chars.com, 'Valid Reason',"
              + " 'Valid Place'", // 51 chars email
        "'a@b.com',    , 'Valid Place'", // Blank reason
        "'a@b.com', ValidReasonSixtyOneCharsxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
              + "xxxxx, 'Valid Place'", // 61 chars reason
        "'a@b.com', 'Valid Reason',    ", // Blank place
        "'a@b.com', 'Valid Reason', ValidPlaceFiftyOneCharsxxxxxxxxxxxxxxxxxxx"
              + "xxxxxxxxx" // 51 chars place
    }
  )
  @DisplayName("Test cases where validation should fail")
  void testFailingValidationExamples(
        final String userEmail, final String reason, final String place
  ) {
    final var form = new CreatePaymentSheetRequest(
          userEmail, reason, place, LocalDate.now(), LocalDate.now().plusDays(1)
    );

    final Set<ConstraintViolation<CreatePaymentSheetRequest>> violations =
          validator.validate(form);

    assertTrue(
          !violations.isEmpty(), "Expected validation to fail, but it passed."
    );
  }

  @ParameterizedTest(name = "{index} => userEmail={0}, reason={1}, place={2}")
  @CsvSource(
    {
        // Non-failing examples
        "'valid.email@example.com', 'Valid Reason', 'Valid Place'",
        // Valid example
        "'a@b.com', 'Valid Reason', 'Valid Place'", // Minimal valid input
        "'fifty.chars.email.fifty.chars.email.fift@chars.com', 'Valid Reason',"
              + " 'Valid Place'", // 50 chars email
        "'a@b.com', 'ValidReasonSixtyCharsxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
              + "xxxxx', 'Valid Place'", // 60 chars reason
        "'a@b.com', 'Valid Reason', 'ValidPlaceFiftyCharsxxxxxxxxxxxxxxxxxxxx"
              + "xxxxxxxxxx'" // 50 chars place
    }
  )
  @DisplayName("Test cases where validation should not fail")
  void testNonFailingValidationExamples(
        final String userEmail, final String reason, final String place
  ) {
    final var form = new CreatePaymentSheetRequest(
          userEmail, reason, place, LocalDate.now(), LocalDate.now().plusDays(1)
    );

    final Set<ConstraintViolation<CreatePaymentSheetRequest>> violations =
          validator.validate(form);

    assertTrue(
          violations.isEmpty(), "Expected validation to pass, but it failed."
    );
    assertDoesNotThrow(
          () -> new CreatePaymentSheetRequest(
                userEmail, reason, place, LocalDate.now(),
                LocalDate.now().plusDays(1)
          ), "Expected valid input to not throw any exception."
    );
  }
}
