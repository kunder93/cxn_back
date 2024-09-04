
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Represents the form used by the controller as a request to create a payment
 * sheet.
 * <p>
 * This is a Data Transfer Object (DTO) that facilitates communication between
 * the view and the controller.
 * It encapsulates the necessary information to create a payment sheet,
 * ensuring that all required data
 * is available for processing.
 * <p>
 * This record is immutable and thread-safe, providing automatic generation of
 * standard methods like
 * {@code equals()}, {@code hashCode()}, and {@code toString()}.
 *
 * @param userEmail The email of the user who owns the payment sheet.
 * @param reason The reason for the payment sheet event.
 * @param place The place where the payment sheet event is held.
 * @param startDate The start date of the payment sheet event.
 * @param endDate The end date of the payment sheet event.
 *
 * @serial The serial version UID is used for ensuring compatibility during the
 * deserialization process.
 *
 * @see LocalDate
 * @author Santiago Paz
 */
public record CreatePaymentSheetRequest(
      @NotBlank(
            message = ValidationConstants.PAYMENT_SHEET_USER_EMAIL_REQUIRED_MESSAGE
      )
      @Email(
            message = ValidationConstants.PAYMENT_SHEET_USER_EMAIL_INVALID_MESSAGE
      )
      @Size(
            max = ValidationConstants.PAYMENT_SHEET_USER_EMAIL_MAX_LENGTH,
            message = ValidationConstants.PAYMENT_SHEET_USER_EMAIL_MAX_LENGTH_MESSAGE
      )
      String userEmail,

      @NotBlank(
            message = ValidationConstants.PAYMENT_SHEET_REASON_REQUIRED_MESSAGE
      )
      @Size(
            max = ValidationConstants.PAYMENT_SHEET_REASON_MAX_LENGTH,
            message = ValidationConstants.PAYMENT_SHEET_REASON_MAX_LENGTH_MESSAGE
      )
      String reason,

      @NotBlank(
            message = ValidationConstants.PAYMENT_SHEET_PLACE_REQUIRED_MESSAGE
      )
      @Size(
            max = ValidationConstants.PAYMENT_SHEET_PLACE_MAX_LENGTH,
            message = ValidationConstants.PAYMENT_SHEET_PLACE_MAX_LENGTH_MESSAGE
      )
      String place,

      @NotNull(
            message = ValidationConstants.PAYMENT_SHEET_START_DATE_REQUIRED_MESSAGE
      )
      LocalDate startDate,

      @NotNull(
            message = ValidationConstants.PAYMENT_SHEET_END_DATE_REQUIRED_MESSAGE
      )
      LocalDate endDate
) {
  /**
   * Custom validation to ensure the end date is not before the start date.
   *
   * @param userEmail The user email.
   * @param reason The payment sheet reason.
   * @param place The payment sheet place.
   * @param startDate The payment sheet start date event.
   * @param endDate The payment sheet end date event.
   */
  public CreatePaymentSheetRequest {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException(
            "End date must be on or after start date"
      );
    }
  }
}
