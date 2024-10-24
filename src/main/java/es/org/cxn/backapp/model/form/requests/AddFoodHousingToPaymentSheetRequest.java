
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Represents the form used by the controller as a request to add food or
 * housing to an existing payment sheet.
 * <p>
 * This Data Transfer Object (DTO) allows communication between the view and the
 * controller, capturing the necessary information for adding food or housing.
 * </p>
 *
 * <p>
 * The form includes Java validation annotations to ensure the data adheres to
 * specific business rules:
 * <ul>
 *   <li><strong>amountDays:</strong> Must be between 1 and 20 inclusive.</li>
 *   <li><strong>dayPrice:</strong> Depends on the overnight status.
 *   If overnight is true, must be at most 50.00.
 *   If false, must be at most 25.00.</li>
 *   <li><strong>overnight:</strong> Indicates whether sleep is included.</li>
 * </ul>
 *
 * @param amountDays The number of days the event lasts.
 *                   Must be between 1 and 20.
 * @param dayPrice   The price per day. Validation depends on whether overnight
 * is included.
 * @param overnight  True if sleep is included, false otherwise.
 *
 * @author Santiago Paz.
 */
@ValidDayPrice
public record AddFoodHousingToPaymentSheetRequest(

      @Min(
            value = ValidationConstants.MIN_AMOUNT_DAYS,
            message = ValidationConstants.AMOUNT_DAYS_MESSAGE
      )
      @Max(
            value = ValidationConstants.MAX_AMOUNT_DAYS,
            message = ValidationConstants.AMOUNT_DAYS_MESSAGE
      )
      Integer amountDays,

      @NotNull(message = ValidationConstants.DAY_PRICE_NON_NEGATIVE_MESSAGE)
      @DecimalMin(
            value = "0.00",
            message = ValidationConstants.DAY_PRICE_NON_NEGATIVE_MESSAGE
      )
      BigDecimal dayPrice,

      @NotNull(message = ValidationConstants.OVERNIGHT_NOT_NULL_MESSAGE)
      Boolean overnight
) {
}
