package es.org.cxn.backapp.model.form;

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
public class CreateInvoiceRequestForm implements Serializable {

    /**
     * Serial UID
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
     * Seller cif or nif.
     */
    private String sellerCifNif;
    /**
     * Buyer cif or nif.
     */
    private String buyerCifNif;

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
     * @param sellerCifNif       the invoice seller company cif or nif.
     * @param buyerCifNif        the invoice buyer company cif or nif.
     */
    public CreateInvoiceRequestForm(
            int number, String series, LocalDate advancePaymentDate,
            LocalDate expeditionDate, Boolean taxExempt, String sellerCifNif,
            String buyerCifNif
    ) {
        super();
        this.number = number;
        this.series = series;
        this.advancePaymentDate = advancePaymentDate;
        this.expeditionDate = expeditionDate;
        this.taxExempt = taxExempt;
        this.sellerCifNif = sellerCifNif;
        this.buyerCifNif = buyerCifNif;
    }

    @Override
    public String toString() {
        return "CreateInvoiceRequestForm [number=" + number + ", series="
                + series + ", advancePaymentDate=" + advancePaymentDate
                + ", expeditionDate=" + expeditionDate + ", taxExempt="
                + taxExempt + ", sellerCifNif=" + sellerCifNif
                + ", buyerCifNif=" + buyerCifNif + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                advancePaymentDate, buyerCifNif, expeditionDate, number,
                sellerCifNif, series, taxExempt
        );
    }

    @Override
    public boolean equals(Object obj) {
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
                && Objects.equals(buyerCifNif, other.buyerCifNif)
                && Objects.equals(expeditionDate, other.expeditionDate)
                && number == other.number
                && Objects.equals(sellerCifNif, other.sellerCifNif)
                && Objects.equals(series, other.series)
                && Objects.equals(taxExempt, other.taxExempt);
    }

    /**
     * Get invoice seller cif or nif.
     *
     * @return The invoice seller cif or nif.
     */
    public String getSellerCifNif() {
        return sellerCifNif;
    }

    /**
     * Set invoice seller company cif or nif.
     *
     * @param value The invoice seller company cif or nif.
     */
    public void setSellerCifNif(String value) {
        this.sellerCifNif = value;
    }

    /**
     * Get invoice buyer cif or nif.
     *
     * @return The invoice buyer company cif or nif.
     */
    public String getBuyerCifNif() {
        return buyerCifNif;
    }

    /**
     * Set invoice buyer company cif or nif.
     *
     * @param value The new buyer company cif or nif.
     */
    public void setBuyerCifNif(String value) {
        this.buyerCifNif = value;
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
    public void setNumber(int value) {
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
    public void setSeries(String value) {
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
    public void setAdvancePaymentDate(LocalDate value) {
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
    public void setExpeditionDate(LocalDate value) {
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
    public void setTaxExempt(Boolean value) {
        this.taxExempt = value;
    }

}
