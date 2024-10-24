
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Represents the request to delete an invoice.
 * <p>
 * This Data Transfer Object (DTO) is used to facilitate communication between
 * the view and the controller for deleting an invoice. It includes an
 * identifier for the invoice to be deleted.
 * </p>
 *
 * <p>
 * The {@code DeleteInvoiceRequest} ensures that the invoice identifier is
 * provided and adheres to the following constraints:
 * </p>
 * <ul>
 *   <li>{@code invoiceId} cannot be {@code null}.</li>
 *   <li>{@code invoiceId} must be a positive integer.</li>
 *   <li>{@code invoiceId} must not exceed {@value ValidationConstants#MAX_ID}.</li>
 * </ul>
 *
 * @param invoiceId The unique identifier of the invoice to be deleted.
 *                  This field is required and must be a positive integer
 *                  that does not exceed {@value ValidationConstants#MAX_ID}.
 *                  <p>
 *                  If the value is {@code null}, not positive, or exceeds
 *                  {@value ValidationConstants#MAX_ID}, validation will fail.
 *                  </p>
 *
 * @author Santiago Paz
 */
public record DeleteInvoiceRequest(
      @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
      @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE)
      @Max(
            value = ValidationConstants.MAX_ID,
            message = ValidationConstants.ID_MAX_MESSAGE
      )
      int invoiceId
) {

}
