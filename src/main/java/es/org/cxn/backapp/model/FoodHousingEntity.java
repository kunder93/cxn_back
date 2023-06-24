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
import java.util.Set;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

/**
 * A food housing entity interface.
 *
 * @author Santiago Paz Perez.
 */
public interface FoodHousingEntity extends Serializable {

  /**
   * Getter for food housing entity identifier.
   *
   * @return the food housing identifier.
   */
  Integer getId();

  /**
   * @return The amount of days.
   */
  Integer getAmountDays();

  /**
   * @return The price per day.
   */
  double getDayPrice();

  /**
   * @return The overnight value.
   */
  Boolean getOvernight();

  /**
   * @param id The food housing identifier.
   */
  void setId(Integer id);

  /**
   * @param amountDays The amount of days.
   */
  void setAmountDays(Integer amountDays);

  /**
   * @param dayPrice The price per day.
   */
  void setDayPrice(double dayPrice);

  /**
   * @param overnight The overnight value.
   */
  void setOvernight(Boolean overnight);

  /**
   * @return Get payment sheet entity associated.
   */
  PersistentPaymentSheetEntity getPaymentSheet();

  /**
   * @return Set with invoices that food housing owns.
   */
  Set<PersistentInvoiceEntity> getInvoices();

  /**
   * @param paymentSheet The payment sheet associated with this food housing
   *                     entity.
   */
  void setPaymentSheet(PersistentPaymentSheetEntity paymentSheet);

  /**
   * @param invoices The invoices assocated with this food housing entity.
   */
  void setInvoices(Set<PersistentInvoiceEntity> invoices);

}
