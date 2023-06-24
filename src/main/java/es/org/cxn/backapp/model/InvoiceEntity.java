/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentOperationEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * A Invoice entity interface.
 *
 * @author Santiago Paz Perez.
 */
public interface InvoiceEntity extends Serializable {

  /**
   * Returns the number of the invoice.
   *
   * @return the invoice entity's number.
   */
  int getNumber();

  /**
   * Get the invoice series.
   *
   * @return series.
   */
  String getSeries();

  /**
   * Get the invoice expedition date.
   *
   * @return the invoice expedition date.
   */
  LocalDate getExpeditionDate();

  /**
   * Get the invoice advance payment date ' fecha pago anticipado'.
   *
   * @return the invoice advance payment date
   */
  LocalDate getAdvancePaymentDate();

  /**
   * Get the invoice tax exempt 'operacion exenta'.
   *
   * @return the invoice tax exempt.
   */
  Boolean getTaxExempt();

  /**
   * Return invoice operations.
   *
   * @return the invoice operations.
   */
  Set<PersistentOperationEntity> getOperations();

  /**
   * Changes the number of the invoice entity.
   *
   * @param value the number to set on the invoice entity.
   */
  void setNumber(int value);

  /**
   * Changes the invoice series.
   *
   * @param value the series to set on the invoice.
   */
  void setSeries(String value);

  /**
   * Changes the invoice expedition date.
   *
   * @param value the expedition date to set on the invoice.
   */
  void setExpeditionDate(LocalDate value);

  /**
   * Changes the invoice advance payment date.
   *
   * @param value the advance payment date to set on the invoice.
   */
  void setAdvancePaymentDate(LocalDate value);

  /**
   * Changes the invoice tax exempt.
   *
   * @param value the tax exempt to set on the invoice.
   */
  void setTaxExempt(Boolean value);

  /**
   * Changes the operations of the invoice.
   *
   * @param value the operations to set on user.
   */
  void setOperations(Set<PersistentOperationEntity> value);

}
