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

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.InvoiceServiceException;
import es.org.cxn.backapp.model.InvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.repository.InvoiceEntityRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link InvoiceService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultInvoiceService implements InvoiceService {

  /**
   * Invoice exist message for exception.
   */
  public static final String INVOICE_EXISTS_MESSAGE = "Invoice already exists.";
  /**
   * Invoice not found message for exception.
   */
  public static final String INVOICE_NOT_FOUND_MESSAGE = "Invoice not found.";
  /**
   * Repository for the invoice entities handled by the service.
   */
  private final InvoiceEntityRepository invoiceRepository;

  /**
   * Constructs an entities service with the specified repository.
   *
   * @param repo The invoice repository{@link InvoiceEntityRepository}
   */
  public DefaultInvoiceService(final InvoiceEntityRepository repo) {
    super();

    invoiceRepository =
          checkNotNull(repo, "Received a null pointer as repository");
  }

  @Transactional
  @Override
  public PersistentInvoiceEntity add(
        final int numberValue, final String seriesValue,
        final LocalDate expeditionDateValue,
        final LocalDate advancePaymentDateValue, final Boolean taxExemptValue,
        final PersistentCompanyEntity seller,
        final PersistentCompanyEntity buyer

  ) throws InvoiceServiceException {
    final var invoices =
          invoiceRepository.findByNumberAndSeries(numberValue, seriesValue);
    if (!invoices.isEmpty()) {
      throw new InvoiceServiceException(INVOICE_EXISTS_MESSAGE);
    }

    final PersistentInvoiceEntity save;
    save = new PersistentInvoiceEntity();
    save.setNumber(numberValue);
    save.setSeries(seriesValue);
    save.setExpeditionDate(expeditionDateValue);
    save.setAdvancePaymentDate(advancePaymentDateValue);
    save.setTaxExempt(taxExemptValue);
    seller.addInvoicesAsSeller(save);
    buyer.addInvoicesAsBuyer(save);
    save.setSeller(seller);
    save.setBuyer(buyer);
    return invoiceRepository.save(save);
  }

  @Transactional
  @Override
  public void remove(final String series, final int number)
        throws InvoiceServiceException {
    final var invoice = invoiceRepository.findByNumberAndSeries(number, series);
    if (invoice.isEmpty()) {
      throw new InvoiceServiceException(INVOICE_NOT_FOUND_MESSAGE);
    }
    invoiceRepository.delete(invoice.get());
  }

  @Override
  public List<InvoiceEntity> getInvoices() {
    final var invoicesList = invoiceRepository.findAll();
    return new ArrayList<>(invoicesList);
  }

  @Override
  public InvoiceEntity findById(final Integer identifier)
        throws InvoiceServiceException {
    final Optional<PersistentInvoiceEntity> entity;
    checkNotNull(identifier, "Received a null pointer as identifier");
    entity = invoiceRepository.findById(identifier);
    if (entity.isEmpty()) {
      throw new InvoiceServiceException(INVOICE_NOT_FOUND_MESSAGE);
    }
    return entity.get();
  }

  @Override
  public InvoiceEntity
        findBySeriesAndNumber(final String series, final int number)
              throws InvoiceServiceException {
    final Optional<PersistentInvoiceEntity> entity;
    entity = invoiceRepository.findByNumberAndSeries(number, series);
    if (entity.isEmpty()) {
      throw new InvoiceServiceException(INVOICE_NOT_FOUND_MESSAGE);
    }
    return entity.get();
  }

}
