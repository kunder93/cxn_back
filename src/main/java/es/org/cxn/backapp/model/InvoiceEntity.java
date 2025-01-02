
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an invoice entity with details such as number, series, dates, and
 * tax exemption. Provides methods to access and modify invoice details.
 *
 * @author Santiago Paz Perez
 */
public interface InvoiceEntity extends Serializable {

    /**
     * Retrieves the date of advance payment, if applicable.
     *
     * @return The advance payment date of the invoice, or {@code null} if not
     *         applicable.
     */
    LocalDate getAdvancePaymentDate();

    /**
     * Retrieves the company that is the buyer of the invoice.
     *
     * @return The buyer company.
     */
    CompanyEntity getBuyer();

    /**
     * Retrieves the date when the invoice was issued.
     *
     * @return The invoice expedition date.
     */
    LocalDate getExpeditionDate();

    /**
     * Get the invoice entity identifier.
     *
     * @return the invoice identifier.
     */
    Integer getIdentifier();

    /**
     * Retrieves the number assigned to the invoice.
     *
     * @return The invoice number.
     */
    int getNumber();

    /**
     * Retrieves the company that is the seller of the invoice.
     *
     * @return The seller company.
     */
    CompanyEntity getSeller();

    /**
     * Retrieves the series of the invoice.
     *
     * @return The invoice series.
     */
    String getSeries();

    /**
     * Checks if the invoice is tax-exempt.
     *
     * @return {@code true} if the invoice is tax-exempt, {@code false} otherwise,
     *         or {@code null} if not specified.
     */
    Boolean isTaxExempt();

    /**
     * Sets the advance payment date for the invoice.
     *
     * @param value The new advance payment date.
     */
    void setAdvancePaymentDate(LocalDate value);

    /**
     * Sets the expedition date for the invoice.
     *
     * @param value The new expedition date.
     */
    void setExpeditionDate(LocalDate value);

    /**
     * Set the invoice entity identifier.
     *
     * @param value the invoice identifier.
     */
    void setIdentifier(Integer value);

    /**
     * Sets the number for the invoice.
     *
     * @param value The new invoice number.
     */
    void setNumber(int value);

    /**
     * Sets the series for the invoice.
     *
     * @param value The new invoice series.
     */
    void setSeries(String value);

    /**
     * Sets whether the invoice is tax-exempt.
     *
     * @param value {@code true} if tax-exempt, {@code false} otherwise, or
     *              {@code null} if not specified.
     */
    void setTaxExempt(Boolean value);

}
