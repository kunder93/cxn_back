
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as request of add regular transport to
 * paymentSheet.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class AddRegularTransportRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3112385622215947705L;

  /**
   * The regular transport Category.
   */
  private String category;
  /**
   * The regular transport description.
   */
  private String description;

  /**
   * The regular transport invoice if it have.
   */
  private Integer invoiceNumber;

  /**
   * The regular transport invoice series.
   */
  private String invoiceSeries;

  /**
   * @param category      the category.
   * @param description   the description.
   * @param invoiceNumber the invoice number.
   * @param invoiceSeries the invoice series.
   */
  public AddRegularTransportRequestForm(
        final String category, final String description,
        final Integer invoiceNumber, final String invoiceSeries
  ) {
    super();
    this.category = category;
    this.description = description;
    this.invoiceNumber = invoiceNumber;
    this.invoiceSeries = invoiceSeries;
  }

  /**
   * Main empty constructor.
   */
  public AddRegularTransportRequestForm() {
    super();
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
   * @return The invoice number.
   */
  public Integer getInvoiceNumber() {
    return invoiceNumber;
  }

  /**
   * @return the invoice series.
   */
  public String getInvoiceSeries() {
    return invoiceSeries;
  }

  /**
   * @param value The category.
   */
  public void setCategory(final String value) {
    this.category = value;
  }

  /**
   * @param value The description,
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  /**
   * @param value The invoice number.
   */
  public void setInvoiceNumber(final Integer value) {
    this.invoiceNumber = value;
  }

  /**
   * @param value The invoice series.
   */
  public void setInvoiceSeries(final String value) {
    this.invoiceSeries = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, description, invoiceNumber, invoiceSeries);
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
    var other = (AddRegularTransportRequestForm) obj;
    return Objects.equals(category, other.category)
          && Objects.equals(description, other.description)
          && Objects.equals(invoiceNumber, other.invoiceNumber)
          && Objects.equals(invoiceSeries, other.invoiceSeries);
  }

  @Override
  public String toString() {
    return "AddRegularTransportRequestForm [category=" + category
          + ", description=" + description + ", invoiceNumber=" + invoiceNumber
          + ", invoiceSeries=" + invoiceSeries + "]";
  }

}
