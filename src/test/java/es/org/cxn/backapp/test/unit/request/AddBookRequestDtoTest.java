
package es.org.cxn.backapp.test.unit.request;

import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.form.requests.AuthorRequest;
import es.org.cxn.backapp.model.form.requests.ValidationConstants;
import es.org.cxn.backapp.model.form.requests.ValidationMessages;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for validating the AddBookRequestDto.
 * This class is marked as final to prevent extension.
 */
public final class AddBookRequestDtoTest {

  /**
   * Valid ISBN of 13 digits for testing.
   */
  private static final long VALID_ISBN_13 = 1234567890123L;

  /**
   * Valid ISBN of 12 digits for testing.
   */
  private static final long VALID_ISBN_12 = 123456789012L;

  /**
   * Valid title for testing.
   */
  private static final String VALID_TITLE = "Valid Title";

  /**
   * Valid gender for testing.
   */
  private static final String VALID_GENDER = "Fiction";

  /**
   * Valid language for testing.
   */
  private static final String VALID_LANGUAGE = "English";

  /**
   * Valid publish date for testing.
   */
  private static final LocalDate VALID_PUBLISH_DATE = LocalDate.of(2023, 1, 1);

  /**
   * Valid author for testing.
   */
  private static final AuthorRequest VALID_AUTHOR =
        new AuthorRequest("John", "Doe", "American");

  /**
   * Validator instance for validating the request DTOs.
   */
  private static Validator validator;

  /**
   * Sets up the validator before running the tests.
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests the validation of ISBN values.
   *
   * @param isbn the ISBN to validate.
   */
  @ParameterizedTest
  @ValueSource(longs = { VALID_ISBN_13, VALID_ISBN_12 })
  @DisplayName("Validate ISBN values")
  void testIsbnValidation(final Long isbn) {
    var authRequest = new AddBookRequestDto(
          isbn, VALID_TITLE, VALID_GENDER, VALID_PUBLISH_DATE, VALID_LANGUAGE,
          List.of(VALID_AUTHOR)
    );

    Set<ConstraintViolation<AddBookRequestDto>> violations =
          validator.validate(authRequest);

    Assertions.assertTrue(
          violations.isEmpty(), "No violations expected for valid ISBN."
    );
  }

  /**
   * Tests the validation of title length.
   *
   * @param title the title to validate.
   */
  @ParameterizedTest
  @ValueSource(
        strings = { "", "a",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                  + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" }
  )
  @DisplayName("Validate title length")
  void testTitleLengthValidation(final String title) {
    var authRequest = new AddBookRequestDto(
          VALID_ISBN_13, title, VALID_GENDER, VALID_PUBLISH_DATE,
          VALID_LANGUAGE, List.of(VALID_AUTHOR)
    );

    Set<ConstraintViolation<AddBookRequestDto>> violations =
          validator.validate(authRequest);

    if (title.isEmpty() || title.length() < ValidationConstants.MIN_TITLE_LENGTH
          || title.length() > ValidationConstants.MAX_TITLE_LENGTH) {
      Assertions.assertEquals(
            1, violations.size(), "One error expected for invalid title length."
      );
      var violation = violations.iterator().next();
      Assertions.assertEquals(
            ValidationMessages.TITLE_SIZE, violation.getMessage(),
            "The constraint message should indicate title length issue."
      );
    } else {
      Assertions.assertTrue(
            violations.isEmpty(),
            "No violations expected for valid title length."
      );
    }
  }

  /**
   * Tests the validation of gender length.
   *
   * @param gender the gender to validate.
   */
  @ParameterizedTest
  @MethodSource("provideGenderValues")
  @DisplayName("Validate gender length")
  void testGenderLengthValidation(final String gender) {
    var authRequest = new AddBookRequestDto(
          VALID_ISBN_13, VALID_TITLE, gender, VALID_PUBLISH_DATE,
          VALID_LANGUAGE, List.of(VALID_AUTHOR)
    );

    Set<ConstraintViolation<AddBookRequestDto>> violations =
          validator.validate(authRequest);

    if (gender != null
          && (gender.length() < ValidationConstants.MIN_GENDER_LENGTH
                || gender.length() > ValidationConstants.MAX_GENDER_LENGTH)) {
      Assertions.assertEquals(
            1, violations.size(),
            "One error expected for invalid gender length."
      );
      var violation = violations.iterator().next();
      Assertions.assertEquals(
            ValidationMessages.GENDER_SIZE, violation.getMessage(),
            "The constraint message should indicate gender length issue."
      );
    } else {
      Assertions.assertTrue(
            violations.isEmpty(),
            "No violations expected for valid gender length."
      );
    }
  }

  /**
   * Tests the validation of language length.
   *
   * @param language the language to validate.
   */
  @ParameterizedTest
  @MethodSource("provideLanguageValues")
  @DisplayName("Validate language length")
  void testLanguageLengthValidation(final String language) {
    var authRequest = new AddBookRequestDto(
          VALID_ISBN_13, VALID_TITLE, VALID_GENDER, VALID_PUBLISH_DATE,
          language, List.of(VALID_AUTHOR)
    );

    Set<ConstraintViolation<AddBookRequestDto>> violations =
          validator.validate(authRequest);

    if (language != null && (language
          .length() < ValidationConstants.MIN_LANGUAGE_LENGTH
          || language.length() > ValidationConstants.MAX_LANGUAGE_LENGTH)) {
      Assertions.assertEquals(
            1, violations.size(),
            "One error expected for invalid language length."
      );
      var violation = violations.iterator().next();
      Assertions.assertEquals(
            ValidationMessages.LANGUAGE_SIZE, violation.getMessage(),
            "The constraint message should indicate language length issue."
      );
    } else {
      Assertions.assertTrue(
            violations.isEmpty(),
            "No violations expected for valid language length."
      );
    }
  }

  /**
   * Tests the validation of the authors list size.
   *
   * @param authorsListSize the size of the authors list.
   */
  @ParameterizedTest
  @ValueSource(ints = { 0, 1, 2 }) // 0 is invalid, 1 is valid, 2 is valid
  @DisplayName("Validate authors list size")
  void testAuthorsListSizeValidation(final int authorsListSize) {
    var authorsList =
          authorsListSize > 0 ? List.of(VALID_AUTHOR) : Collections.emptyList();

    var authRequest = new AddBookRequestDto(
          VALID_ISBN_13, VALID_TITLE, VALID_GENDER, VALID_PUBLISH_DATE,
          VALID_LANGUAGE, (List<AuthorRequest>) authorsList
    );

    Set<ConstraintViolation<AddBookRequestDto>> violations =
          validator.validate(authRequest);

    if (authorsListSize < ValidationConstants.MIN_AUTHORS_LIST_SIZE) {
      Assertions.assertEquals(
            1, violations.size(),
            "One error expected for invalid authors list size."
      );
      var violation = violations.iterator().next();
      Assertions.assertEquals(
            ValidationMessages.AUTHORS_LIST_SIZE, violation.getMessage(),
            "The constraint message should indicate authors list size issue."
      );
    } else {
      Assertions.assertTrue(
            violations.isEmpty(),
            "No violations expected for valid authors list size."
      );
    }
  }

  /**
   * Provides a stream of gender values for testing.
   *
   * @return a stream of gender values.
   */
  private static Stream<String> provideGenderValues() {
    return Stream.of(
          "", // Invalid: too short
          "a", // Valid: minimum length
          "a".repeat(ValidationConstants.MAX_GENDER_LENGTH),
          // Valid: maximum length
          "a".repeat(ValidationConstants.MAX_GENDER_LENGTH + 1)
          // Invalid: too long
    );
  }

  /**
   * Provides a stream of language values for testing.
   *
   * @return a stream of language values.
   */
  private static Stream<String> provideLanguageValues() {
    return Stream.of(
          "", // Invalid: too short
          "a", // Valid: minimum length
          "a".repeat(ValidationConstants.MAX_LANGUAGE_LENGTH),
          // Valid: maximum length
          "a".repeat(ValidationConstants.MAX_LANGUAGE_LENGTH + 1)
          // Invalid: too long
    );
  }
}
