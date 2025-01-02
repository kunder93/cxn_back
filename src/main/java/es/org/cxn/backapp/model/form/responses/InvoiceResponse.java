
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.InvoiceEntity;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Represents the response DTO used by the controller for requesting one
 * invoice.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller when requesting invoice
 * details. It includes relevant invoice information such as number, series,
 * dates, and tax information.
 * </p>
 *
 * @param number              The invoice number.
 * @param series              The invoice series.
 * @param advancePaymentDate  The invoice advance payment date.
 * @param expeditionDate      The invoice expedition date.
 * @param taxExempt           The invoice tax exempt value.
 * @param sellerNif           The invoice seller NIF.
 * @param buyerNif            The invoice buyer NIF.
 *
 * @author Santiago Paz
 */
public record InvoiceResponse(
      BigInteger number, String series, LocalDate advancePaymentDate,
      LocalDate expeditionDate, Boolean taxExempt, String sellerNif,
      String buyerNif
) {

  /**
   * Creates an {@link InvoiceResponse} from an {@link InvoiceEntity}.
   * <p>
   * This static factory method initializes a new {@code InvoiceResponse}
   * using the data from the provided {@code InvoiceEntity}.
   * </p>
   *
   * @param entity The {@code InvoiceEntity} containing the data.
   * @return A new {@code InvoiceResponse} with values derived from the entity.
   */
  public static InvoiceResponse fromEntity(final InvoiceEntity entity) {
    return new InvoiceResponse(
          BigInteger.valueOf(entity.getNumber()), entity.getSeries(),
          entity.getAdvancePaymentDate(), entity.getExpeditionDate(),
          entity.getTaxExempt(), entity.getSeller().getNif(),
          entity.getBuyer().getNif()
    );
  }
}
