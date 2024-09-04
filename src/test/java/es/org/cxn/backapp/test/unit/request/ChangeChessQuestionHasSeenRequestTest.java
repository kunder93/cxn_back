
package es.org.cxn.backapp.test.unit.request;

import es.org.cxn.backapp.model.form.requests.ChangeChessQuestionHasSeenRequest;
import es.org.cxn.backapp.model.form.requests.ValidationConstants;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit test class for validating {@link ChangeChessQuestionHasSeenRequest}
 * using parameterized tests.
 * <p>
 * This class contains tests to ensure that the
 * {@link ChangeChessQuestionHasSeenRequest} DTO correctly enforces
 * validation constraints for the ID field. It uses Jakarta Bean Validation
 * to check the constraints and verify
 * that the validation logic is functioning as expected.
 * </p>
 *
 * <p>
 * The tests are parameterized to cover various cases including valid and
 * invalid inputs, and edge cases for the ID field.
 * </p>
 */
public class ChangeChessQuestionHasSeenRequestTest {

  /**
   * The validator instance used for validating
   * {@link ChangeChessQuestionHasSeenRequest} objects.
   */
  private static Validator validator;

  /**
   * Sets up the validator instance before any tests are run.
   * <p>
   * This method is called once before all the tests in this class.
   * It initializes the Jakarta Bean Validation {@link Validator} to be used
   * for validating the {@link ChangeChessQuestionHasSeenRequest} instances.
   * </p>
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests the validation of the ID field in
   * {@link ChangeChessQuestionHasSeenRequest}.
   * <p>
   * This method is parameterized to test various cases for the ID field,
   * including:
   * <ul>
   *   <li>Null values</li>
   *   <li>Non-positive values</li>
   *   <li>Values at the edge of the allowed range</li>
   *   <li>Values exceeding the maximum allowed value</li>
   * </ul>
   * The method checks the number of validation violations and ensures that
   *  the constraint messages match the expected values based on the input data.
   * </p>
   *
   * @param id                The ID value to test. Can be null or a number.
   * @param expectedViolations The expected number of validation violations
   * for the given ID.
   */
  @ParameterizedTest(name = "id={0}, expectedViolations={1}")
  @CsvSource(
    { "0, 1", // Invalid: Not positive
        "999, 0", // Valid: Edge case, just below the max allowed value
        "1000, 0", // Valid: Edge case, exactly at the max allowed value
        "1001, 1" // Invalid: Exceeds the max allowed value
    }
  )
  void testIdValidation(final Integer id, final int expectedViolations) {
    var request = new ChangeChessQuestionHasSeenRequest(id);

    Set<ConstraintViolation<ChangeChessQuestionHasSeenRequest>> violations =
          validator.validate(request);

    Assertions.assertEquals(
          expectedViolations, violations.size(),
          "The number of violations should match the expected number."
    );

    if (expectedViolations > 0) {
      var violation = violations.iterator().next();
      if (id == null) {
        Assertions.assertEquals(
              ValidationConstants.ID_NOT_NULL_MESSAGE, violation.getMessage(),
              "The constraint message should indicate that the ID cannot be"
                    + " null."
        );
      } else if (id <= 0) {
        Assertions.assertEquals(
              ValidationConstants.ID_POSITIVE_MESSAGE, violation.getMessage(),
              "The constraint message should indicate that the ID must be"
                    + " positive."
        );
      } else if (id > ValidationConstants.MAX_ID) {
        Assertions.assertEquals(
              ValidationConstants.ID_MAX_MESSAGE, violation.getMessage(),
              "The constraint message should indicate that the ID must not"
                    + " exceed 1000."
        );
      }
    }
  }
}
