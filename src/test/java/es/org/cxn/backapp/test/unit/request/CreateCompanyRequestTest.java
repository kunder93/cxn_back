
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.CreateCompanyRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link CreateCompanyRequest} class.
 * <p>
 * These tests verify that the validation constraints defined in the
 * {@link CreateCompanyRequest} class are properly enforced for
 * both valid and invalid inputs. The tests cover a range of cases including
 * frontier values and invalid inputs to ensure that the validation
 * annotations work as expected.
 * </p>
 */
public class CreateCompanyRequestTest {

  /**
   * Validator instance for performing constraint validation.
   */
  private static Validator validator;

  /**
   * Sets up the validator instance before any of the tests are run.
   * This method initializes the validation factory and creates a
   * {@link Validator} instance used for validating the form objects.
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests the {@link CreateCompanyRequest} class with valid inputs,
   * including frontier values that are at the edge of allowed lengths.
   * <p>
   * The test cases include:
   * <ul>
   *   <li>A NIF with the maximum length of 10 characters.</li>
   *   <li>A name with the maximum length of 40 characters.</li>
   *   <li>An address with the maximum length of 60 characters.</li>
   * </ul>
   * </p>
   *
   * @param nif The Tax Identification Number (NIF) of the company.
   * @param name The name of the company.
   * @param address The address of the company.
   */
  @ParameterizedTest(name = "Valid case: nif={0}, name={1}, address={2}")
  @CsvSource(
    {
        // Valid cases with frontier values
        "1234567890, ValidName, ValidAddress",
        // Valid input with max length for NIF
        "1234567890, MaxLengthCompanyNameIs40Charactersssssss, ValidAddress",
        // Valid input with max length for Name
        "1234567890, ShortName, MaxLengthCompanyAddressIs60Characterssssssssss"
              + "ssssssssssssss", // Valid input with max length for Address
    }
  )
  @DisplayName("Valid cases with frontier values for CreateCompanyRequest")
  void testValidCases(
        final String nif, final String name, final String address
  ) {
    // Given: A valid CreateCompanyRequest with the provided parameters
    final var form = new CreateCompanyRequest(nif, name, address);

    // When: The form is validated
    final Set<ConstraintViolation<CreateCompanyRequest>> violations =
          validator.validate(form);

    // Then: No violations should be found
    assertTrue(violations.isEmpty(), "No violations expected for valid input.");
  }

  /**
   * Tests the {@link CreateCompanyRequest} class with invalid inputs,
   * including cases with values that are too short, too long, or contain
   * only whitespace.
   * <p>
   * The test cases include:
   * <ul>
   *   <li>A blank NIF.</li>
   *   <li>A NIF that exceeds the maximum length of 10 characters.</li>
   *   <li>A blank name.</li>
   *   <li>A name that exceeds the maximum length of 40 characters.</li>
   *   <li>A blank address.</li>
   *   <li>An address that exceeds the maximum length of 60 characters.</li>
   *   <li>A NIF with only whitespace.</li>
   *   <li>A name with only whitespace.</li>
   *   <li>An address with only whitespace.</li>
   * </ul>
   * </p>
   *
   * @param nif The Tax Identification Number (NIF) of the company.
   * @param name The name of the company.
   * @param address The address of the company.
   */
  @ParameterizedTest(name = "Invalid case: nif={0}, name={1}, address={2}")
  @CsvSource(
    {
        // Invalid cases with frontier values
        ", ValidName, ValidAddress", // Blank NIF
        "12345678901, ValidName, ValidAddress", // NIF exceeding max length
        "1234567890, , ValidAddress", // Blank Name
        "1234567890, NameExceedingMaxLength_41_Characters56579, ValidAddress",
        // Name exceeding max length
        "1234567890, ValidName, ", // Blank Address
        "1234567890, ValidName, AddressExceedingMaxLength_61_Characters12312312"
              + "31231231231231", // Address exceeding max length
        "      , ValidName, ValidAddress", // NIF with white spaces
        "1234567890,        , ValidAddress", // Name with white spaces
        "1234567890, ValidName,           " // Address white spaces
    }
  )
  @DisplayName("Invalid cases for CreateCompanyRequest")
  void testInvalidCases(
        final String nif, final String name, final String address
  ) {
    // Given: An invalid CreateCompanyRequest with the provided parameters
    final var form = new CreateCompanyRequest(nif, name, address);

    // When: The form is validated
    final Set<ConstraintViolation<CreateCompanyRequest>> violations =
          validator.validate(form);

    // Then: Violations should be found
    assertFalse(violations.isEmpty(), "Violations expected for invalid input.");
  }
}
