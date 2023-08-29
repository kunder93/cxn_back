
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response for requesting one
 * invoice.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class InvoiceResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133089826012147705L;

  /**
   * The invoice number.
   */
  private int number;
  /**
   * The invoice series.
   */
  private String series;
  /**
   * The invoice payment date.
   */
  private LocalDate advancePaymentDate;

  /**
   * The invoice expedition date.
   */
  private LocalDate expeditionDate;
  /**
   * The invoice tax exempt value.
   */
  private Boolean taxExempt;

  /**
   * The invoice seller nif.
   */
  private String sellerNif;

  /**
   * The invoice buyer nif.
   */
  private String buyerNif;

  /**
   * Constructor with invoice entity.
   *
   * @param entity The invoice entity.
   */
  public InvoiceResponse(final PersistentInvoiceEntity entity) {
    super();
    this.number = entity.getNumber();
    this.series = entity.getSeries();
    this.advancePaymentDate = entity.getAdvancePaymentDate();
    this.expeditionDate = entity.getExpeditionDate();
    this.taxExempt = entity.getTaxExempt();
    this.sellerNif = entity.getSeller().getNif();
    this.buyerNif = entity.getBuyer().getNif();
  }
}
