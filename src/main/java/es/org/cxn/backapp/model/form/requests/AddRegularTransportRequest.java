
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;

/**
 * Represents the form used by the controller as a request to add regular
 * transport to a payment sheet.
 * <p>
 * This is a Data Transfer Object (DTO) meant to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * <p>
 * The form includes Java validation annotations to ensure the data adheres
 * to specific business rules:
 * <ul>
 *   <li><strong>category:</strong> Must be non-null and at most 30
 *   characters.</li>
 *   <li><strong>description:</strong> Must be non-null and at most 100
 *   characters.</li>
 *   <li><strong>invoiceNumber:</strong> Must be non-null.</li>
 *   <li><strong>invoiceSeries:</strong> Must be non-null and at most 10
 *   characters.</li>
 * </ul>
 *
 * @param category      The regular transport category. Must be non-null and up
 *                      to 30 characters.
 * @param description   The regular transport description. Must be non-null and
 *                      up to 100 characters.
 * @param invoiceNumber The regular transport invoice number. Must be non-null.
 * @param invoiceSeries The regular transport invoice series. Must be non-null
 *                      and up to 10 characters.
 *
 * @author Santiago Paz.
 */
public record AddRegularTransportRequest(

      @NotNull(message = ValidationConstants.CATEGORY_NOT_NULL_MESSAGE) @Size(
            max = ValidationConstants.MAX_CATEGORY_LENGTH,
            message = ValidationConstants.CATEGORY_SIZE_MESSAGE
      )
      String category,

      @NotNull(message = ValidationConstants.DESCRIPTION_NOT_NULL_MESSAGE)
      @Size(
            max = ValidationConstants.MAX_DESCRIPTION_LENGTH,
            message = ValidationConstants.DESCRIPTION_SIZE_MESSAGE
      )
      String description,

      @Positive
      @NotNull(message = ValidationConstants.INVOICE_NUMBER_NOT_NULL_MESSAGE)
      BigInteger invoiceNumber,

      @NotNull(message = ValidationConstants.INVOICE_SERIES_NOT_NULL_MESSAGE)
      @Size(
            max = ValidationConstants.MAX_INVOICE_SERIES_LENGTH,
            message = ValidationConstants.INVOICE_SERIES_SIZE_MESSAGE
      )
      String invoiceSeries
) {

}
