
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Represents the form used by the controller as a request to add a self-owned
 * vehicle to a payment sheet.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, ensuring that the necessary information is provided for
 * adding a self-owned vehicle to the payment sheet.
 * </p>
 *
 * <p>
 * The {@code AddSelfVehicleRequest} includes fields for the number of
 * places in the vehicle, the distance traveled, and the price per kilometer.
 * These fields help calculate the total cost associated with using the self
 * owned vehicle.
 * </p>
 *
 * @param places   The number of available places in the vehicle. This is
 * expected to be a string representation of the number of seats or capacity,
 * up to 100 characters.
 * @param distance The distance traveled by the vehicle in kilometers.
 * This is a decimal number that should not exceed 50,000.
 * @param kmPrice  The cost per kilometer of using the vehicle. This is a
 * decimal value between 0.01 and 0.19 inclusive.
 *
 * <p>
 * These fields are expected to be validated by the controller to ensure
 * accurate data is received for processing the payment sheet.
 * </p>
 *
 * @author Santiago Paz
 */
public record AddSelfVehicleRequest(
      @NotNull(message = ValidationConstants.NOT_NULL_MESSAGE) @Size(
            max = ValidationConstants.MAX_PLACES_LENGTH,
            message = ValidationConstants.PLACES_SIZE_MESSAGE
      )
      String places,

      @Positive(message = ValidationConstants.DISTANCE_MIN_MESSAGE)
      @NotNull(message = ValidationConstants.NOT_NULL_MESSAGE)
      @DecimalMax(
            value = ValidationConstants.MAX_DISTANCE, inclusive = true,
            message = ValidationConstants.DISTANCE_MAX_MESSAGE
      )
      BigDecimal distance,

      @Positive(message = ValidationConstants.KM_PRICE_MIN_MESSAGE)
      @NotNull(message = ValidationConstants.NOT_NULL_MESSAGE)
      @DecimalMax(
            value = ValidationConstants.MAX_KM_PRICE, inclusive = true,
            message = ValidationConstants.KM_PRICE_MAX_MESSAGE
      )
      BigDecimal kmPrice
) {
}
