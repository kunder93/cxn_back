
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.time.LocalDate;
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

  /**
   * Main empty constructor.
   */
  public CreateInvoiceRequestForm() {
    super();
  }

  /**
   * Main arguments constructor.
   *
   * @param number             the invoice number
   * @param series             the invoice series
   * @param advancePaymentDate the invoice payment date
   * @param expeditionDate     the invoice expedition date
   * @param taxExempt          the invoice tax exempt
   * @param sellerNif          the invoice seller company nif.
   * @param buyerNif           the invoice buyer company nif.
   */
  public CreateInvoiceRequestForm(
        final int number, final String series,
        final LocalDate advancePaymentDate, final LocalDate expeditionDate,
        final Boolean taxExempt, final String sellerNif, final String buyerNif
  ) {
    super();
    this.number = number;
    this.series = series;
    this.advancePaymentDate = advancePaymentDate;
    this.expeditionDate = expeditionDate;
    this.taxExempt = taxExempt;
    this.sellerNif = sellerNif;
    this.buyerNif = buyerNif;
  }

  @Override
  public String toString() {
    return "CreateInvoiceRequestForm [number=" + number + ", series=" + series
          + ", advancePaymentDate=" + advancePaymentDate + ", expeditionDate="
          + expeditionDate + ", taxExempt=" + taxExempt + ", sellerNif="
          + sellerNif + ", buyerNif=" + buyerNif + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(
          advancePaymentDate, buyerNif, expeditionDate, number, sellerNif,
          series, taxExempt
    );
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
    var other = (CreateInvoiceRequestForm) obj;
    return Objects.equals(advancePaymentDate, other.advancePaymentDate)
          && Objects.equals(buyerNif, other.buyerNif)
          && Objects.equals(expeditionDate, other.expeditionDate)
          && number == other.number
          && Objects.equals(sellerNif, other.sellerNif)
          && Objects.equals(series, other.series)
          && Objects.equals(taxExempt, other.taxExempt);
  }

  /**
   * Get invoice seller nif.
   *
   * @return The invoice seller nif.
   */
  public String getSellerNif() {
    return sellerNif;
  }

  /**
   * Set invoice seller company nif.
   *
   * @param value The invoice seller company nif.
   */
  public void setSellerNif(final String value) {
    this.sellerNif = value;
  }

  /**
   * Get invoice buyer nif.
   *
   * @return The invoice buyer company nif.
   */
  public String getBuyerNif() {
    return buyerNif;
  }

  /**
   * Set invoice buyer company nif.
   *
   * @param value The new buyer company nif.
   */
  public void setBuyerNif(final String value) {
    this.buyerNif = value;
  }

  /**
   * Get invoice number.
   *
   * @return The invoice number.
   */
  public int getNumber() {
    return number;
  }

  /**
   * Set invoice number.
   *
   * @param value The invoice number.
   */
  public void setNumber(final int value) {
    this.number = value;
  }

  /**
   * Get invoice series.
   *
   * @return The invoice series.
   */
  public String getSeries() {
    return series;
  }

  /**
   * Set invoice series.
   *
   * @param value the new invoice series.
   */
  public void setSeries(final String value) {
    this.series = value;
  }

  /**
   * Get invoice advance payment date.
   *
   * @return the invoice advance payment date.
   */
  public LocalDate getAdvancePaymentDate() {
    return advancePaymentDate;
  }

  /**
   * Set invoice advance payment date.
   *
   * @param value the invoice advance payment date.
   */
  public void setAdvancePaymentDate(final LocalDate value) {
    this.advancePaymentDate = value;
  }

  /**
   * Get the invoice expedition date.
   *
   * @return the invoice expedition date.
   */
  public LocalDate getExpeditionDate() {
    return expeditionDate;
  }

  /**
   * Set invoice expedition date.
   *
   * @param value the invoice expedition date.
   */
  public void setExpeditionDate(final LocalDate value) {
    this.expeditionDate = value;
  }

  /**
   * Get invoice tax exempt value.
   *
   * @return The tax exempt value.
   */
  public Boolean getTaxExempt() {
    return taxExempt;
  }

  /**
   * Setter for Invoice request tax exempt value.
   *
   * @param value The tax exempt boolean value.
   */
  public void setTaxExempt(final Boolean value) {
    this.taxExempt = value;
  }

}
