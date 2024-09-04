
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link CreateChessQuestionRequest} class.
 * <p>
 * This test class verifies that the validation logic for the
 * {@link CreateChessQuestionRequest} works correctly, ensuring
 * both valid and invalid cases are handled as expected.
 * </p>
 * <p>
 * Tests are categorized into valid and invalid cases, using various
 * parameterized inputs to cover different scenarios and boundary conditions.
 * </p>
 * <p>
 * The tests use the JUnit 5 framework with parameterized tests to
 * provide a range of inputs and validate the behavior of the form.
 * </p>
 */
public class CreateChessQuestionRequestTest {

  /**
   * Validator instance for performing constraint validation.
   */
  private static Validator validator;

  /**
   * Sets up the test environment by initializing the validator.
   * This method is executed once before all test methods are run.
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests valid cases for the {@link CreateChessQuestionRequest} class.
   * <p>
   * This parameterized test checks various valid scenarios, including:
   * <ul>
   *   <li>Email with exactly 50 characters</li>
   *   <li>Category with exactly 30 characters</li>
   *   <li>Topic with exactly 50 characters</li>
   *   <li>Message with maximum allowed length (400 characters)</li>
   * </ul>
   * </p>
   *
   * @param email   The email address of the user submitting the question.
   * @param category The category to which the chess question belongs.
   * @param topic    The topic of the chess question.
   * @param message  The content of the chess question.
   */
  @ParameterizedTest(
        name = "Valid case: email={0}, category={1}, topic={2}, message={3}"
  )
  @CsvSource(
    { "50charactersEmailaaaadsdddd@exampleWith50chars.com, CategoryA, TopicA,"
          + " This is a valid message", // 50 characters email is valid.
        "user@mail.com, 30CharValidCategoryExamplepppp, ValidTopic, "
              + "Short message", // Category with 30 char is valid.
        "a@b.c, A, ExampleTopicWith50Characters()&%$*^¨Ç.,_-asdwrsdas, M",
        // Topic with 50 characters is valid.
        "long.email.address@domain.com, validCategoryExample, "
              + "ValidTopicExample, "
              + "This is a valid message of maximum allowed length for the"
              + " message at does not exceed 400 characters."
              + "This is a valid message of maximum allowed length for the"
              + " message at does not exceed 400 characters."
              + "This is a valid message of maximum allowed length for the"
              + " message at does not exceed 400 characters."
              + "This is a valid message of maximum allowed length for the"
              + " message at does not exceed 400 characters." }
  )
  @DisplayName(
    "Valid cases with frontier values for CreateChessQuestionRequest"
  )
  void testValidCases(
        final String email, final String category, final String topic,
        final String message
  ) {
    //Given:A valid CreateChessQuestionRequest with the provided parameters
    final var form =
          new CreateChessQuestionRequest(email, category, topic, message);

    // When: The form is validated
    final Set<ConstraintViolation<CreateChessQuestionRequest>> violations =
          validator.validate(form);

    // Then: No violations should be found
    assertTrue(violations.isEmpty(), "No violations expected for valid input.");
  }

  /**
   * Tests invalid cases for the {@link CreateChessQuestionRequest} class.
   * <p>
   * This parameterized test checks various invalid scenarios, including:
   * <ul>
   *   <li>Blank email</li>
   *   <li>Invalid email format</li>
   *   <li>Blank category</li>
   *   <li>Category exceeding max size</li>
   *   <li>Blank topic</li>
   *   <li>Topic exceeding max size</li>
   *   <li>Blank message</li>
   *   <li>Message exceeding max length</li>
   * </ul>
   * </p>
   *
   * @param email   The email address of the user submitting the question.
   * @param category The category to which the chess question belongs.
   * @param topic    The topic of the chess question.
   * @param message  The content of the chess question.
   */
  @ParameterizedTest(
        name = "Invalid case: email={0}, category={1}, topic={2}, message={3}"
  )
  @CsvSource(
    { ", ValidCategory, ValidTopic, Valid message", // Null email
        "invalid-email, ValidCategory, ValidTopic, Valid message",
        // Invalid email format
        "valid.email@example.com, , ValidTopic, Valid message",
        // Null category
        "valid.email@example.com, CategoryWith31CharNotValidddddd,"
              + " ValidTopic, Valid message", // Category exceeding max size
        "valid.email@example.com, ValidCategory, , Valid message",
        // Null topic
        "valid.email@example.com, ValidCategory, TopicExceedingMaxSize_ABCDE"
              + "FGHIDASDASDD51Characters, Valid message",
        // Topic exceeding max size
        "valid.email@example.com, ValidCategory, ValidTopic, ", // Null message
        "valid.email@example.com, ValidCategory, ValidTopic, "
              + "This message exceeds the maximum allowed length for the"
              + " message field and should trigger a validation error."
              + " This message exceeds the maximum allowed length for the"
              + " message field and should trigger a validation error."
              + " This message exceeds the maximum allowed length for the"
              + " message field and should trigger a validation error."
              + " This message exceeds the maximum allowed length for the"
              + " message field and should trigger a validation error."
              + " This message exceeds the maximum allowed length for the"
              + " message field and should trigger a validation error."
              + " End of message.",
        ", ValidCategory, ValidTopic, Valid message", // Blank email
        "valid.email@example.com, , ValidTopic, Valid message",
        // Blank category
        "valid.email@example.com, ValidCategory, , Valid message",
        // Blank topic
        "valid.email@example.com, ValidCategory, ValidTopic, "
    // Blank message
    }
  )
  @DisplayName("Invalid cases for CreateChessQuestionRequest")
  void testInvalidCases(
        final String email, final String category, final String topic,
        final String message
  ) {
    // Given: An invalid CreateChessQuestionRequest with the provided
    // parameters.
    final var form =
          new CreateChessQuestionRequest(email, category, topic, message);

    // When: The form is validated
    final Set<ConstraintViolation<CreateChessQuestionRequest>> violations =
          validator.validate(form);

    // Then: Violations should be found
    assertFalse(violations.isEmpty(), "Violations expected for invalid input.");
  }
}
