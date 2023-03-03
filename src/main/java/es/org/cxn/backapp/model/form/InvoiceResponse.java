package es.org.cxn.backapp.model.form;

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
public class InvoiceResponse implements Serializable {

    /**
     * Serial UID
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
     * The invoice seller nif or cif.
     */
    private String sellerNifCif;

    /**
     * The invoice buyer nif or cif.
     */
    private String buyerNifCif;

    /**
     * Provided params constructor.
     *
     * @param iNumber             The invoic number.
     * @param iSeries             The invoice series.
     * @param iAdvancePaymentDate The invoice advance payment date.
     * @param iExpeditionDate     The invoice expedition date.
     * @param iTaxExempt          The invoice tax exempt value.
     * @param iSellerNifCif       The invoice seller nif or cif.
     * @param iBuyerNifCif        The invoice buyer nif or cif.
     */
    public InvoiceResponse(
            int iNumber, String iSeries, LocalDate iAdvancePaymentDate,
            LocalDate iExpeditionDate, Boolean iTaxExempt, String iSellerNifCif,
            String iBuyerNifCif
    ) {
        super();
        this.number = iNumber;
        this.series = iSeries;
        this.advancePaymentDate = iAdvancePaymentDate;
        this.expeditionDate = iExpeditionDate;
        this.taxExempt = iTaxExempt;
        this.sellerNifCif = iSellerNifCif;
        this.buyerNifCif = iBuyerNifCif;
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
    public void setNumber(int value) {
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
    public void setSeries(String value) {
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
    public void setAdvancePaymentDate(LocalDate value) {
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
    public void setExpeditionDate(LocalDate value) {
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
    public void setTaxExempt(Boolean value) {
        this.taxExempt = value;
    }

    /**
     * @return the invoice response seller nif or cif.
     */
    public String getSellerNifCif() {
        return sellerNifCif;
    }

    /**
     * @param value The invoice response seller nif or cif.
     */
    public void setSellerNifCif(String value) {
        this.sellerNifCif = value;
    }

    /**
     * @return The invoice response buyer nif or cif.
     */
    public String getBuyerNifCif() {
        return buyerNifCif;
    }

    /**
     * @param value The invoice response buyer nif or cif.
     */
    public void setBuyerNifCif(String value) {
        this.buyerNifCif = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                advancePaymentDate, buyerNifCif, expeditionDate, number,
                sellerNifCif, series, taxExempt
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
        var other = (InvoiceResponse) obj;
        return Objects.equals(advancePaymentDate, other.advancePaymentDate)
                && Objects.equals(buyerNifCif, other.buyerNifCif)
                && Objects.equals(expeditionDate, other.expeditionDate)
                && number == other.number
                && Objects.equals(sellerNifCif, other.sellerNifCif)
                && Objects.equals(series, other.series)
                && Objects.equals(taxExempt, other.taxExempt);
    }

    @Override
    public String toString() {
        return "InvoiceResponse [number=" + number + ", series=" + series
                + ", advancePaymentDate=" + advancePaymentDate
                + ", expeditionDate=" + expeditionDate + ", taxExempt="
                + taxExempt + ", sellerNifCif=" + sellerNifCif
                + ", buyerNifCif=" + buyerNifCif + "]";
    }

}
