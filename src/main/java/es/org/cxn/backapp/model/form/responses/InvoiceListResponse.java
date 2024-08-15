
package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the form used by controller as response for requesting all
 * invoices.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class InvoiceListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -6122905275533572886L;

  /**
   * List with all individual invoices responses.
   */
  private List<InvoiceResponse> invoicesList = new ArrayList<>();

  /**
   * Response constructor with parameters provided.
   *
   * @param value the invoices entity list.
   */
  public InvoiceListResponse(final List<InvoiceResponse> value) {
    super();
    value.forEach(this.invoicesList::add);
  }

  /**
   * Get all invoices.
   *
   * @return The invoices list.
   */
  public List<InvoiceResponse> getInvoicesList() {
    return new ArrayList<>(invoicesList);
  }

  /**
   * Set invoices.
   *
   * @param value the invoices list.
   */
  public void setInvoicesList(final List<InvoiceResponse> value) {
    this.invoicesList = new ArrayList<>(value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(invoicesList);
  }

  @Override
  public boolean equals(final Object obj) {
    var isEqual = false;

    // Check if the object is compared to itself
    if (this == obj) {
      isEqual = true;
    } else {
      // Check if the object is null or not the same class
      if (obj != null && getClass() == obj.getClass()) {
        // Cast the object to the correct type
        final var other = (InvoiceListResponse) obj;

        // Compare the relevant fields
        isEqual = Objects.equals(invoicesList, other.invoicesList);
      }
    }

    return isEqual;
  }

  @Override
  public String toString() {
    return "InvoiceListResponse [invoicesList=" + invoicesList + "]";
  }

}
