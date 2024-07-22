
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of create invoice.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public final class CreateInvoiceRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133089826012147705L;

  /**
   * Invoice number.
   */
  private int number;
  /**
   * Invoice series.
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
   * Invoice tax exempt for calculating taxes.
   */
  private Boolean taxExempt;
  /**
   * Seller nif.
   */
  private String sellerNif;
  /**
   * Buyer nif.
   */
  private String buyerNif;

}
