
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

import java.io.Serializable;

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
public final class RegularTransportResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3180089459011962505L;

  private int identifier;
  /**
   * The response regular transport category.
   */
  private String category;
  /**
   * The response regular transport description.
   */
  private String description;
  /**
   * The response regular transport invoice response.
   */
  private InvoiceResponse invoiceResponse;

  /**
   * Constructor with regular transport entity.
   *
   * @param entity The Regular Transport entity.
   */
  public RegularTransportResponse(
        final PersistentRegularTransportEntity entity
  ) {
    super();
    this.identifier = entity.getId();
    this.category = entity.getCategory();
    this.description = entity.getDescription();
    this.invoiceResponse = new InvoiceResponse(entity.getTransportInvoice());
  }
}
