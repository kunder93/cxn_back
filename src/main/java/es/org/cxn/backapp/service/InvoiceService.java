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

package es.org.cxn.backapp.service;

import es.org.cxn.backapp.exceptions.InvoiceServiceException;
import es.org.cxn.backapp.model.InvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for the Invoice entity domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago Paz Perez.
 */
public interface InvoiceService {

  /**
   * Creates a new invoice entity.
   *
   * @param numberValue             The invoice number.
   * @param seriesValue             The invoice series.
   * @param expeditionDateValue     The invoice expedition date.
   * @param advancePaymentDateValue The invoice advance payment date value.
   * @param taxExemptValue          The invoice tax exempt.
   * @param seller                  The seller company.
   * @param buyer                   The buyer company.
   * @return The Invoice entity persisted.
   * @throws InvoiceServiceException When invoice with number and series already
   *                                 exists.
   */
  PersistentInvoiceEntity add(
        int numberValue, String seriesValue, LocalDate expeditionDateValue,
        LocalDate advancePaymentDateValue, Boolean taxExemptValue,
        PersistentCompanyEntity seller, PersistentCompanyEntity buyer
  ) throws InvoiceServiceException;

  /**
   * Returns an invoice with the given identifier (id).
   * <p>
   * If no instance exists with that id then an exception is thrown.
   *
   * @param identifier The invoice identifier aka id.
   * @return the invoice entity for the given id.
   * @throws InvoiceServiceException When invoice with provided identifier not
   *                                 found {@link InvoiceServiceException}.
   */
  InvoiceEntity findById(Integer identifier) throws InvoiceServiceException;

  /**
   * Get all invoices.
   *
   * @return all invoices.
   */
  List<InvoiceEntity> getInvoices();

  /**
   * Remove invoice locating it with number and series.
   *
   * @param series The invoice series.
   * @param number The invoice number.
   * @throws InvoiceServiceException When invoices with series and number not
   *                                 found.
   */
  void remove(String series, int number) throws InvoiceServiceException;

}
