
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

import java.io.Serializable;

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
public final class RegularTransportResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3180089459011962505L;

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
   * Main empty constructor.
   */
  public RegularTransportResponse() {
    super();
  }

  /**
   * Constructor with regular transport entity.
   *
   * @param entity The Regular Transport entity.
   */
  public RegularTransportResponse(
        final PersistentRegularTransportEntity entity
  ) {
    super();
    this.category = entity.getCategory();
    this.description = entity.getDescription();
    this.invoiceResponse = new InvoiceResponse(entity.getTransportInvoice());
  }

  /**
   * @return The category.
   */
  public String getCategory() {
    return category;
  }

  /**
   * @return The description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return The invoice response.
   */
  public InvoiceResponse getInvoiceResponse() {
    return invoiceResponse;
  }

  /**
   * @param category The category.
   */
  public void setCategory(final String category) {
    this.category = category;
  }

  /**
   * @param description The description.
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @param invoiceResponse The invoice response.
   */
  public void setInvoiceResponse(final InvoiceResponse invoiceResponse) {
    this.invoiceResponse = invoiceResponse;
  }

}
