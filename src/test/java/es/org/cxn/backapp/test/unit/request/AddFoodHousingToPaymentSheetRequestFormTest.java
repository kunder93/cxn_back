
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.AddFoodHousingToPaymentSheetRequest;
import es.org.cxn.backapp.model.form.requests.ValidationConstants;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link AddFoodHousingToPaymentSheetRequest}.
 * <p>
 * This class contains tests to validate the correctness of the
 * {@link AddFoodHousingToPaymentSheetRequest} class. It ensures that
 * various constraints are correctly enforced based on the provided
 * parameters and validation annotations.
 * </p>
 * <p>
 * The tests cover valid cases, boundary values, and invalid inputs to
 * ensure that the validation logic behaves as expected.
 * </p>
 */
class AddFoodHousingToPaymentSheetRequestFormTest {

  /**
   * Validator instance for performing constraint validation.
   */
  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests valid cases for different combinations of amountDays, dayPrice, and
   * overnight values.
   *
   * @param amountDays The number of days for the event.
   * @param dayPrice   The price per day.
   * @param overnight  Indicates if sleep is included.
   */
  @ParameterizedTest(
        name = "Valid case with amountDays={0}, dayPrice={1}, overnight={2}"
  )
  @CsvSource({ "10, 50.00, true", "10, 25.00, false" })
  @DisplayName("Valid cases for different amountDays and dayPrice values")
  void testValidCases(
        final Integer amountDays, final BigDecimal dayPrice,
        final Boolean overnight
  ) {
    final var form = new AddFoodHousingToPaymentSheetRequest(
          amountDays, dayPrice, overnight
    );
    final Set<ConstraintViolation<AddFoodHousingToPaymentSheetRequest>> violations =
          validator.validate(form);
    assertTrue(violations.isEmpty(), "No violations expected for valid input.");
  }

  /**
   * Tests boundary cases for amountDays to ensure they are within the valid
   * range.
   *
   * @param amountDays The number of days for the event.
   */
  @ParameterizedTest(name = "Boundary case with amountDays={0}")
  @CsvSource(
    { "1", // Minimum amountDays
        "20" // Maximum amountDays
    }
  )
  @DisplayName("Boundary cases for amountDays")
  void testBoundaryAmountDays(final Integer amountDays) {
    final var form = new AddFoodHousingToPaymentSheetRequest(
          amountDays, new BigDecimal("10.00"), true
    );
    final Set<ConstraintViolation<AddFoodHousingToPaymentSheetRequest>> violations =
          validator.validate(form);
    assertTrue(
          violations.isEmpty(),
          "No violations expected for boundary amountDays."
    );
  }

  /**
   * Tests invalid dayPrice values based on the overnight status to ensure
   * proper validation.
   *
   * @param overnight  Indicates if sleep is included.
   * @param dayPrice   The price per day that exceeds the maximum allowed value.
   */
  @ParameterizedTest(
        name = "Invalid dayPrice with amountDays=10, overnight={1},"
              + " dayPrice={2}"
  )
  @CsvSource(
    { "true, 51.00", // Day price exceeds max with overnight true
        "false, 26.00" // Day price exceeds max with overnight false
    }
  )
  @DisplayName("Invalid cases for dayPrice based on overnight status")
  void testInvalidDayPrice(final Boolean overnight, final BigDecimal dayPrice) {
    final var form =
          new AddFoodHousingToPaymentSheetRequest(10, dayPrice, overnight);
    final Set<ConstraintViolation<AddFoodHousingToPaymentSheetRequest>> violations =
          validator.validate(form);
    assertEquals(
          1, violations.size(), "One violation expected for invalid dayPrice."
    );
    final var violation = violations.iterator().next();
    assertTrue(
          violation.getMessage().contains(
                overnight ? ValidationConstants.DAY_PRICE_MESSAGE_OVERNIGHT
                      : ValidationConstants.DAY_PRICE_MESSAGE_NO_OVERNIGHT
          ),
          "Violation message should indicate dayPrice exceeds maximum value"
                + " for the given overnight status."
    );
  }

  /**
   * Tests invalid amountDays values outside the valid range to ensure proper
   * validation.
   *
   * @param amountDays The number of days for the event that is out of valid
   * range.
   */
  @ParameterizedTest(name = "Invalid amountDays={0}")
  @CsvSource(
    { "0", // Below minimum amountDays
        "21" // Above maximum amountDays
    }
  )
  @DisplayName("Invalid cases for amountDays outside valid range")
  void testInvalidAmountDays(final Integer amountDays) {
    final var form = new AddFoodHousingToPaymentSheetRequest(
          amountDays, new BigDecimal("10.00"), true
    );
    final Set<ConstraintViolation<AddFoodHousingToPaymentSheetRequest>> violations =
          validator.validate(form);
    assertEquals(
          1, violations.size(), "One violation expected for invalid amountDays."
    );
    final var violation = violations.iterator().next();
    assertEquals(
          ValidationConstants.AMOUNT_DAYS_MESSAGE, violation.getMessage(),
          "Violation message should indicate amountDays is out of valid range."
    );
  }
}
