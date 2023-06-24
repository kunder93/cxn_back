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

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;

/**
 * A Operation entity interface.
 *
 * @author Santiago Paz Perez.
 */
public interface OperationEntity extends Serializable {

  /**
   * Returns the identifier assigned to this operation entity.
   * <p>
   * If no identifier has been assigned yet, then the value is expected to be
   * {@code null} or lower than zero.
   *
   * @return the operation entity's identifier.
   */
  Integer getId();

  /**
   * Returns the price of the operation.
   *
   * @return the operation entity's price.
   */
  BigDecimal getPrice();

  /**
   * Get the operation description.
   *
   * @return operation description.
   */
  String getDescription();

  /**
   * Get the operation discount that is a percent.
   *
   * @return the operation price discount.
   */
  int getDiscount();

  /**
   * Get the operation tax rate 'tipo impositivo'.
   *
   * @return the operation tax rate.
   */
  int getTaxRate();

  /**
   * Get the operation date.
   *
   * @return the operation date.
   */
  LocalDate getOperationDate();

  /**
   * Get the operation quantity.
   *
   * @return the operation quantity.
   */
  int getQuantity();

  /**
   * Sets the identifier assigned to this invoice entity.
   *
   * @param identifier the identifier for the invoice entity.
   */
  void setId(Integer identifier);

  /**
   * Changes the operation price.
   *
   * @param value the operation price.
   */
  void setPrice(BigDecimal value);

  /**
   * Changes the operation description.
   *
   * @param value the description.
   */
  void setDescription(String value);

  /**
   * Changes the operation price discount.
   *
   * @param value the operation discount.
   */
  void setDiscount(int value);

  /**
   * Changes the operation tax rate.
   *
   * @param value the tax rate.
   */
  void setTaxRate(int value);

  /**
   * Changes the operation date.
   *
   * @param value the operation date.
   */
  void setOperationDate(LocalDate value);

  /**
   * Changes the operation quantity.
   *
   * @param value the new operation quantity.
   */
  void setQuantity(int value);

  /**
   * Changes the associated invoice.
   *
   * @param value the new operation quantity.
   */
  void setInvoice(PersistentInvoiceEntity value);

  /**
   * Get the operation associated invoice.
   *
   * @return the operation associated invoice.
   */
  PersistentInvoiceEntity getInvoice();

}
