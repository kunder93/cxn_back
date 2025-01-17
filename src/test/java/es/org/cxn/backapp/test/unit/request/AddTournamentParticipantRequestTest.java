
package es.org.cxn.backapp.test.unit.request;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.assertj.core.api.Assertions.assertThat;

import es.org.cxn.backapp.model.form.requests.AddTournamentParticipantRequest;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link AddTournamentParticipantRequest}.
 * <p>
 * This test class verifies the validation constraints of the
 * {@link AddTournamentParticipantRequest} DTO.
 * </p>
 */
class AddTournamentParticipantRequestTest {

  /**
   * The validator.
   */
  private final Validator validator;

  AddTournamentParticipantRequestTest() {
    var factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  /**
   * Tests {@link AddTournamentParticipantRequest} with valid input values.
   * <p>
   * This parameterized test runs with multiple sets of valid input values to
   * check if the DTO validates correctly according to the constraints defined
   * in the {@link AddTournamentParticipantRequest} class.
   * </p>
   *
   * @param fideId       The FIDE ID to be validated.
   * @param name         The name of the participant to be validated.
   * @param club         The club of the participant to be validated.
   * @param birthDate    The birth date of the participant to be validated.
   * @param categoryStr  The category of the participant to be validated.
   */
  @ParameterizedTest
  @CsvSource(
    {
        // fideId, name, club, birthDate, category
        "1234567, John Doe, Chess Club, 2010-05-20, SUB12",
        // Valid FIDE ID, name, club, birth date, and category.
        "9999999, Jane Doe, Elite Club, 2008-03-15, SUB14",
        // FIDE ID at upper valid limit.
        "4567890, Aliceeeeeeee Smithhhhhhh JonsonnnnJonsonnnnnnnnnnn,"
              + " National Club, 2005-07-25, SUB10",
        //Max name length of 50 characters.
        "7894561, Bob Brown, Local Club Name Very Very Long, 2015-02-15, SUB8"
    //Max club length valid of 30 characters.

    }
  )
  void testAddTournamentParticipantRequestValid(
        final BigInteger fideId, final String name, final String club,
        final String birthDate, final String categoryStr
  ) {
    // Parse the date string to LocalDate
    var parsedBirthDate = LocalDate.parse(birthDate);

    // Convert category string to enum
    var category = TournamentCategory.valueOf(categoryStr);

    // Create the request object
    var request = new AddTournamentParticipantRequest(
          fideId, name, club, parsedBirthDate, category, "1"
    );

    // Validate the request
    Set<ConstraintViolation<AddTournamentParticipantRequest>> violations =
          validator.validate(request);

    // Assert violations are present
    assertThat(violations).withFailMessage(
          "Expected  NO violations but found. Violations: %s", violations
    ).isEmpty();
  }

  /**
   * Tests {@link AddTournamentParticipantRequest} with invalid input values.
   * <p>
   * This parameterized test runs with multiple sets of invalid input values to
   * check if the DTO correctly identifies invalid requests.
   * </p>
   *
   * @param fideIdStr     The FIDE ID to be validated as a string.
   * @param name          The name of the participant to be validated.
   * @param club          The club of the participant to be validated.
   * @param birthDateStr  The birth date of the participant to be validated as
   * a string.
   * @param categoryStr   The category of the participant to be validated as a
   * string.
   */
  @ParameterizedTest
  @CsvSource(
    {
        // fideId, name, club, birthDate, category
        ", John Doe, Chess Club, 2010-05-20, SUB12",
        // Invalid: FIDE ID is missing (required field).
        "-1, John Doe, Chess Club, 2010-05-20, SUB12",
        // Invalid: FIDE ID is negative (must be positive).
        "10000000, John Doe, Chess Club, 2010-05-20, SUB12",
        // Invalid: FIDE ID exceeds the upper limit (max 9999999).
        "1234567,   , Chess Club, 2010-05-20, SUB12",
        // Invalid: Name is missing (required field).
        "1234567, A very long name that exceeds the maximum length al,"
              + " Chess Club, 2010-05-20, SUB12",
        // Invalid: Name exceeds 50 (51) characters.
        "1234567, John Doe, , 2010-05-20, SUB12",
        // Invalid: Club is missing (required field).
        "1234567, John Doe, A very long club name that exce, 2010-05-20, SUB12",
        // Invalid: Club name exceeds 30 (31) characters.
        "1234567, John Doe, Chess Club, , SUB12",
        // Invalid: Birth date is missing (required field).
        "1234567, John Doe, Chess Club, 2030-01-01, SUB12",
        // Invalid: Birth date is in the future.
        "1234567, John Doe, Chess Club, 2010-05-20, "
    // Invalid: Category is missing (required field).
    }
  )
  void testAddTournamentParticipantRequestInvalid(
        final String fideIdStr, final String name, final String club,
        final String birthDateStr, final String categoryStr
  ) {
    // Parse the fideId and birthDate
    var fideId = (fideIdStr == null || fideIdStr.isBlank()) ? null
          : new BigInteger(fideIdStr);
    var parsedBirthDate =
          (birthDateStr == null || birthDateStr.isBlank()) ? null
                : LocalDate.parse(birthDateStr);

    // Convert category string to enum
    TournamentCategory category = null;
    if (categoryStr != null && !categoryStr.isBlank()) {
      try {
        category = TournamentCategory.valueOf(categoryStr);
      } catch (IllegalArgumentException e) {
        // Invalid category, will be validated as invalid
      }
    }

    // Create the request object
    var request = new AddTournamentParticipantRequest(
          fideId, name, club, parsedBirthDate, category, "1"
    );

    // Validate the request
    Set<ConstraintViolation<AddTournamentParticipantRequest>> violations =
          validator.validate(request);

    // Assert violations are present
    assertThat(violations).withFailMessage(
          "Expected violations but found none. Violations: %s", violations
    ).isNotEmpty();
  }

}
