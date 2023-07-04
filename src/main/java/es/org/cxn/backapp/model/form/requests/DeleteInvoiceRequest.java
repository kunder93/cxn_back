
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

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
public final class DeleteInvoiceRequest implements Serializable {

  /**
   * The UID.
   */
  private static final long serialVersionUID = 7125614240029897056L;

  /**
   * The invoice identifier.
   */
  private int invoiceId;

  /**
   * Main arguments constructor.
   *
   * @param value the invoice identifier.
   *
   */
  public DeleteInvoiceRequest(final int value) {
    super();
    this.invoiceId = value;
  }

  /**
   * Main empty constructor.
   */
  public DeleteInvoiceRequest() {
    super();
  }

  /**
   * Get request invoice identifier.
   *
   * @return The invoice identifier.
   */
  public int getInvoiceId() {
    return invoiceId;
  }

  /**
   * Set request invoice identifier.
   *
   * @param value the invoice identifier.
   */
  public void setInvoiceId(final int value) {
    this.invoiceId = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(invoiceId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    var other = (DeleteInvoiceRequest) obj;
    return invoiceId == other.invoiceId;
  }

  @Override
  public String toString() {
    return "DeleteInvoiceRequest [invoiceId=" + invoiceId + "]";
  }

}
