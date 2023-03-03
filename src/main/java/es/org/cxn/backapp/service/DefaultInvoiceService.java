/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.org.cxn.backapp.exceptions.InvoiceServiceException;
import es.org.cxn.backapp.model.InvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.repository.InvoiceEntityRepository;

/**
 * Default implementation of the {@link UserService}.
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

        invoiceRepository = checkNotNull(
                repo, "Received a null pointer as repository"
        );
    }

    @Transactional
    @Override
    public PersistentInvoiceEntity add(
            int numberValue, String seriesValue, LocalDate expeditionDateValue,
            LocalDate advancePaymentDateValue, Boolean taxExemptValue,
            PersistentCompanyEntity seller, PersistentCompanyEntity buyer

    ) throws InvoiceServiceException {
        var invoices = invoiceRepository
                .findByNumberAndSeries(numberValue, seriesValue);
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
    public void remove(String series, int number)
            throws InvoiceServiceException {
        var invoicesList = invoiceRepository
                .findByNumberAndSeries(number, series);
        if (invoicesList.size() == 1) {
            var invoice = invoicesList.get(0);
            invoiceRepository.delete(invoice);
        } else {
            throw new InvoiceServiceException(INVOICE_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public List<PersistentInvoiceEntity> getInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public InvoiceEntity findById(Integer identifier)
            throws InvoiceServiceException {
        final Optional<PersistentInvoiceEntity> entity;
        checkNotNull(identifier, "Received a null pointer as identifier");
        entity = invoiceRepository.findById(identifier);
        if (entity.isEmpty()) {
            throw new InvoiceServiceException(INVOICE_NOT_FOUND_MESSAGE);
        }
        return entity.get();
    }

}
