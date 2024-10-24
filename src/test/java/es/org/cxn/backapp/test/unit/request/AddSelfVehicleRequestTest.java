
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.AddSelfVehicleRequest;

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
 * Unit tests for the {@link AddSelfVehicleRequest} class.
 *
 * <p>
 * This class contains tests that validate the constraints applied to the
 * fields of the {@code AddSelfVehicleRequest} class.
 * The tests ensure that both valid and invalid cases are handled as expected
 * according to the validation annotations defined in the DTO.
 * </p>
 *
 * <p>
 * The {@code AddSelfVehicleRequestTest} class uses JUnit 5 and Jakarta
 * Bean Validation (JSR 380) to validate the fields of the form object.
 * </p>
 *
 * <p>
 * The {@code setup} method initializes the {@link Validator} instance used to
 * validate the form object.
 * </p>
 *
 * <p>
 * The {@code testValidCases} method validates that valid inputs for the form
 * do not produce any constraint violations.
 * </p>
 *
 * <p>
 * The {@code testInvalidCases} method ensures that invalid inputs for the form
 * produce the expected constraint violations.
 * </p>
 */
public class AddSelfVehicleRequestTest {

  /**
   * Validator instance for performing constraint validation.
   */
  private static Validator validator;

  /**
   * Initializes the {@link Validator} instance before all tests are executed.
   *
   * <p>
   * This method is annotated with {@link BeforeAll}, which means it runs once
   * before any of the test methods in this class are executed. It sets up the
   * Validator instance using the default validator factory.
   * </p>
   */
  @BeforeAll
  public static void setup() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests the valid cases for {@link AddSelfVehicleRequest}.
   *
   * <p>
   * This method uses a parameterized test with CSV input values to verify that
   * valid data does not result in any constraint violations. Each set of
   * parameters represents a valid combination of {@code places},
   * {@code distance}, and {@code kmPrice}.
   * </p>
   *
   * @param places   the number of available places in the vehicle,
   * represented as a string.
   * @param distance the distance traveled by the vehicle in kilometers.
   * @param kmPrice  the cost per kilometer of using the vehicle.
   */
  @ParameterizedTest(name = "Valid case: places={0}, distance={1}, kmPrice={2}")
  @CsvSource(
    { "ValidPlaces, 10000, 0.10", // Valid inputs
        "Short, 0.0001, 0.000001",
        // Minimum values, distance and kmPrice must be positive values.
        "PlacesNoMinLength, 50000, 0.19",
        // Max length for places, max value for distance, and max price
        "100CharactersIsMaxPlacesLengthAndHereAre100CharactersOfPlacesExample"
              + "Text     WithSpaces...LLLDDD´.+ç, 25000, 0.15" }
  )
  @DisplayName("Valid cases with frontier values for AddSelfVehicleRequest")
  void testValidCases(
        final String places, final String distance, final String kmPrice
  ) {
    final var form = new AddSelfVehicleRequest(
          places, new BigDecimal(distance), new BigDecimal(kmPrice)
    );
    final Set<ConstraintViolation<AddSelfVehicleRequest>> violations =
          validator.validate(form);
    assertTrue(violations.isEmpty(), "No violations expected for valid input.");
  }

  /**
   * Tests the invalid cases for {@link AddSelfVehicleRequest}.
   *
   * <p>
   * This method uses a parameterized test with CSV input values to verify that
   * invalid data results in the expected constraint violations. Each set of
   * parameters represents an invalid combination of {@code places},
   * {@code distance}, and {@code kmPrice}.
   * </p>
   *
   * @param places   the number of available places in the vehicle, represented
   * as a string.
   * @param distance the distance traveled by the vehicle in kilometers.
   * @param kmPrice  the cost per kilometer of using the vehicle.
   */
  @ParameterizedTest(
        name = "Invalid case: places={0}, distance={1}, kmPrice={2}"
  )
  @CsvSource(
    { "ValidPlaces, 0, 0.14", // No valid distance 0 is not positive.
        "ValidPlaces, 120, 0", // No valid Km price cannot be 0.
        "ValidPlaces, 10000, 0.20", // kmPrice exceeding max value.
        "ValidPlaces, 50000.01, 0.13", // Distance exceeding max value.
        "ValidPlaces, -1, 0.10", // Negative distance
        "ValidPlaces, 258, -0.13", // Negative kmPrice
        ", 10000, 0.10", // No valid Empty places
        "101CharactersPlacesExample_ ^* ¨Ç_:Ñ¨Ç¨Ç/()==?¿·$!ª|¬8797¬€qi30q2'"
              + "tuofifsjdkgjbppeoqwtfgoirwejhkbgggg, 450, 0.13",
        // Places exceeding max value
        "ValidPlaces, 10000, -0.01" // Negative kmPrice
    }
  )
  @DisplayName(
    "Invalid cases with frontier values for AddSelfVehicleRequest"
  )
  void testInvalidCases(
        final String places, final String distance, final String kmPrice
  ) {
    final var form = new AddSelfVehicleRequest(
          places, new BigDecimal(distance), new BigDecimal(kmPrice)
    );
    final Set<ConstraintViolation<AddSelfVehicleRequest>> violations =
          validator.validate(form);
    assertFalse(violations.isEmpty(), "Violations expected for invalid input.");
  }
}
