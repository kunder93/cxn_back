
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class DeleteInvoiceRequest implements Serializable {

  /**
   * The UID.
   */
  private static final long serialVersionUID = 7125614240029897056L;

  /**
   * The invoice identifier.
   */
  private int invoiceId;

}
