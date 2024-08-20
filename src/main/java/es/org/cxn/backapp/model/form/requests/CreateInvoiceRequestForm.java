
package es.org.cxn.backapp.model.form.requests;

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
 * This DTO does not include validation annotations, but it is important to
 * ensure that the data provided through this form is validated at the
 * controller level or service layer.
 * </p>
 *
 * @param number               The invoice number. This field represents a
 *  unique identifier for the invoice.
 * @param series               The invoice series. This is used to group
 *  invoices under a specific series, which can be helpful for
 *                            organizational purposes.
 * @param advancePaymentDate   The date when an advance payment was made. This
 * date can be null if no advance payment was involved.
 * @param expeditionDate       The date when the invoice is issued or the goods
 *                            are dispatched. This field can be null if not
 *                            applicable.
 * @param taxExempt            A boolean indicating whether the invoice is
 * tax-exempt.
 *                            This field can be null if tax exemption status
 *                            is unknown.
 * @param sellerNif            The NIF (Tax Identification Number) of the
 * seller.
 *                            This field should contain the seller's
 *                            identification number.
 * @param buyerNif             The NIF (Tax Identification Number) of the buyer.
 *                            This field should contain the buyer's
 *                            identification number.
 *
 * @author Santiago Paz
 */
public record CreateInvoiceRequestForm(
      int number, String series, LocalDate advancePaymentDate,
      LocalDate expeditionDate, Boolean taxExempt, String sellerNif,
      String buyerNif
) {

}
