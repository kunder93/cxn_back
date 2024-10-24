
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an invoice entity with details such as number, series, dates, and
 * tax exemption.
 * Provides methods to access and modify invoice details.
 *
 * @author Santiago Paz Perez
 */
public interface InvoiceEntity extends Serializable {

  /**
   * Retrieves the number assigned to the invoice.
   *
   * @return The invoice number.
   */
  int getNumber();

  /**
   * Retrieves the series of the invoice.
   *
   * @return The invoice series.
   */
  String getSeries();

  /**
   * Retrieves the date when the invoice was issued.
   *
   * @return The invoice expedition date.
   */
  LocalDate getExpeditionDate();

  /**
   * Retrieves the date of advance payment, if applicable.
   *
   * @return The advance payment date of the invoice, or {@code null} if not
   * applicable.
   */
  LocalDate getAdvancePaymentDate();

  /**
   * Checks if the invoice is tax-exempt.
   *
   * @return {@code true} if the invoice is tax-exempt, {@code false}
   * otherwise, or {@code null} if not specified.
   */
  Boolean getTaxExempt();

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
   * Sets the expedition date for the invoice.
   *
   * @param value The new expedition date.
   */
  void setExpeditionDate(LocalDate value);

  /**
   * Sets the advance payment date for the invoice.
   *
   * @param value The new advance payment date.
   */
  void setAdvancePaymentDate(LocalDate value);

  /**
   * Sets whether the invoice is tax-exempt.
   *
   * @param value {@code true} if tax-exempt, {@code false} otherwise, or
   * {@code null} if not specified.
   */
  void setTaxExempt(Boolean value);

  /**
   * Retrieves the company that is the seller of the invoice.
   *
   * @return The seller company.
   */
  CompanyEntity getSeller();

  /**
   * Retrieves the company that is the buyer of the invoice.
   *
   * @return The buyer company.
   */
  CompanyEntity getBuyer();

}
