
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Represents the form used by the controller as a request to create
 * an invoice.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between
 * the view and the controller. It encapsulates the necessary information
 * required to generate an invoice and ensures that the data provided is
 * sufficient for the invoice creation process.
 * </p>
 *
 * <p>
 * This DTO includes validation annotations to ensure that the required data
 * is provided and conforms to the expected format. The data must be validated
 * at the controller level or service layer as well.
 * </p>
 *
 * @param number               The invoice number. This field represents a
 *  unique identifier for the invoice.
 * @param series               The invoice series. This is used to group
 *  invoices under a specific series, which can be helpful for
 *  organizational purposes.
 * @param advancePaymentDate   The date when an advance payment was made. This
 *  date can be null if no advance payment was involved.
 * @param taxExempt            A boolean indicating whether the invoice is
 *  tax-exempt. This field should not be null.
 * @param sellerNif            The NIF (Tax Identification Number) of the
 *  seller. This field should contain the seller's identification number.
 * @param buyerNif             The NIF (Tax Identification Number) of the buyer.
 *  This field should contain the buyer's identification number.
 * @param expeditionDate       The date when the invoice is issued or the goods
 *  are dispatched. This field can be null if not applicable.
 *
 * @author Santiago Paz
 */
public record CreateInvoiceRequest(
      @NotNull(message = ValidationConstants.INVOICE_NUMBER_NOT_NULL_MESSAGE)
      @Positive(message = ValidationConstants.INVOICE_NUMBER_MIN_MESSAGE)
      @Max(
            value = ValidationConstants.INVOICE_NUMBER_MAX,
            message = ValidationConstants.INVOICE_NUMBER_MESSAGE
      )
      BigInteger number,

      @NotBlank(message = ValidationConstants.INVOICE_SERIES_SIZE_MESSAGE)
      @Size(
            max = ValidationConstants.INVOICE_SERIES_MAX_LENGTH,
            message = ValidationConstants.INVOICE_SERIES_SIZE_MESSAGE
      )
      String series,

      @NotNull(message = ValidationConstants.INVOICE_DATE_NOT_NULL_MESSAGE)
      LocalDate advancePaymentDate,

      @NotNull(
            message = ValidationConstants.INVOICE_TAX_EXEMPT_NOT_NULL_MESSAGE
      )
      Boolean taxExempt,

      @NotBlank(message = ValidationConstants.INVOICE_NIF_NOT_BLANK_MESSAGE)
      @Size(
            max = ValidationConstants.INVOICE_NIF_MAX_LENGTH,
            message = ValidationConstants.INVOICE_NIF_SIZE_MESSAGE
      )
      String sellerNif,

      @NotBlank(message = ValidationConstants.INVOICE_NIF_NOT_BLANK_MESSAGE)
      @Size(
            max = ValidationConstants.INVOICE_NIF_MAX_LENGTH,
            message = ValidationConstants.INVOICE_NIF_SIZE_MESSAGE
      )
      String buyerNif,

      @NotNull(message = ValidationConstants.INVOICE_DATE_NOT_NULL_MESSAGE)
      LocalDate expeditionDate
) {

}
