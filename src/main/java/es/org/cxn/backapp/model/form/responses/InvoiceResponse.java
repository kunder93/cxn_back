
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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
   * Provided parameters constructor.
   *
   * @param iNumber             The invoic number.
   * @param iSeries             The invoice series.
   * @param iAdvancePaymentDate The invoice advance payment date.
   * @param iExpeditionDate     The invoice expedition date.
   * @param iTaxExempt          The invoice tax exempt value.
   * @param iSellerNif          The invoice seller nif.
   * @param iBuyerNif           The invoice buyer nif.
   */
  public InvoiceResponse(
        final int iNumber, final String iSeries,
        final LocalDate iAdvancePaymentDate, final LocalDate iExpeditionDate,
        final Boolean iTaxExempt, final String iSellerNif,
        final String iBuyerNif
  ) {
    super();
    this.number = iNumber;
    this.series = iSeries;
    this.advancePaymentDate = iAdvancePaymentDate;
    this.expeditionDate = iExpeditionDate;
    this.taxExempt = iTaxExempt;
    this.sellerNif = iSellerNif;
    this.buyerNif = iBuyerNif;
  }

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

  /**
   * Main empty constructor.
   */
  public InvoiceResponse() {
    super();
  }

  /**
   * @return The invoice response number.
   */
  public int getNumber() {
    return number;
  }

  /**
   * @param value The invoice response number
   */
  public void setNumber(final int value) {
    this.number = value;
  }

  /**
   * @return The invoice response series.
   */
  public String getSeries() {
    return series;
  }

  /**
   * @param value The invoice response series.
   */
  public void setSeries(final String value) {
    this.series = value;
  }

  /**
   * @return The invoice response advance payment date.
   */
  public LocalDate getAdvancePaymentDate() {
    return advancePaymentDate;
  }

  /**
   * @param value The invoice response advance payment date.
   */
  public void setAdvancePaymentDate(final LocalDate value) {
    this.advancePaymentDate = value;
  }

  /**
   * @return The invoice expedition date.
   */
  public LocalDate getExpeditionDate() {
    return expeditionDate;
  }

  /**
   * @param value The invoice response expedition date.
   */
  public void setExpeditionDate(final LocalDate value) {
    this.expeditionDate = value;
  }

  /**
   * @return The invoice response tax exempt value.
   */
  public Boolean getTaxExempt() {
    return taxExempt;
  }

  /**
   * @param value The invoice response tax exempt value.
   */
  public void setTaxExempt(final Boolean value) {
    this.taxExempt = value;
  }

  /**
   * @return the invoice response seller nif.
   */
  public String getSellerNif() {
    return sellerNif;
  }

  /**
   * @param value The invoice response seller nif.
   */
  public void setSellerNif(final String value) {
    this.sellerNif = value;
  }

  /**
   * @return The invoice response buyer nif.
   */
  public String getBuyerNif() {
    return buyerNif;
  }

  /**
   * @param value The invoice response buyer nif.
   */
  public void setBuyerNif(final String value) {
    this.buyerNif = value;
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
    var other = (InvoiceResponse) obj;
    return Objects.equals(advancePaymentDate, other.advancePaymentDate)
          && Objects.equals(buyerNif, other.buyerNif)
          && Objects.equals(expeditionDate, other.expeditionDate)
          && number == other.number
          && Objects.equals(sellerNif, other.sellerNif)
          && Objects.equals(series, other.series)
          && Objects.equals(taxExempt, other.taxExempt);
  }

  @Override
  public String toString() {
    return "InvoiceResponse [number=" + number + ", series=" + series
          + ", advancePaymentDate=" + advancePaymentDate + ", expeditionDate="
          + expeditionDate + ", taxExempt=" + taxExempt + ", sellerNif="
          + sellerNif + ", buyerNif=" + buyerNif + "]";
  }

}
