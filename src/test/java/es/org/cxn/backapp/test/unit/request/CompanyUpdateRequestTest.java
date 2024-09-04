
package es.org.cxn.backapp.test.unit.request;

import es.org.cxn.backapp.model.form.requests.CompanyUpdateRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link CompanyUpdateRequestForm} class.
 * These tests validate boundary values for the name and address fields using
 * CSV-based parameterization.
 */
public class CompanyUpdateRequestTest {

  /**
   * The validator.
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
   * Tests the validation of the name field with boundary values using
   * CSV parameters.
   *
   * @param name the name to validate.
   * @param expectedViolations the expected number of violations.
   */
  @ParameterizedTest
  @CsvSource(
    { "'', 1", // Invalid: blank
        "'a', 0", // Valid: 1 character
        "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 0",
        // Valid: 40 characters
        "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 1"
    // Invalid: 41 characters
    }
  )
  @DisplayName("Validate company name length")
  void testNameValidation(final String name, final int expectedViolations) {
    var form = new CompanyUpdateRequest(name, "Valid Address");

    Set<ConstraintViolation<CompanyUpdateRequest>> violations =
          validator.validate(form);

    Assertions.assertEquals(
          expectedViolations, violations.size(),
          "Unexpected number of violations for name."
    );
  }

  /**
   * Tests the validation of the address field with boundary values using
   * CSV parameters.
   *
   * @param address the address to validate.
   * @param expectedViolations the expected number of violations.
   */
  @ParameterizedTest
  @CsvSource(
    { "'', 1", // Invalid: blank
        "'a', 0", // Valid: 1 character
        "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 0",
        // Valid: 60 characters
        "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 1"
    // Invalid: 61 characters
    }
  )
  @DisplayName("Validate company address length")
  void testAddressValidation(
        final String address, final int expectedViolations
  ) {
    var form = new CompanyUpdateRequest("Valid Name", address);

    Set<ConstraintViolation<CompanyUpdateRequest>> violations =
          validator.validate(form);

    Assertions.assertEquals(
          expectedViolations, violations.size(),
          "Unexpected number of violations for address."
    );
  }
}
