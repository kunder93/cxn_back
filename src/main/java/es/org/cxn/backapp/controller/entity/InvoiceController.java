/**
 * The MIT License (MIT)
 *
 * <p>Copyright (c) 2020 the original author or authors.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.CompanyServiceException;
import es.org.cxn.backapp.exceptions.InvoiceServiceException;
import es.org.cxn.backapp.model.InvoiceEntity;
import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.service.CompanyService;
import es.org.cxn.backapp.service.DefaultCompanyService;
import es.org.cxn.backapp.service.InvoiceService;

import jakarta.validation.Valid;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rest controller for invoices.
 *
 * @author Santiago Paz
 *
 */
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

  /**
   * The invoice service.
   */
  private final InvoiceService invoiceService;
  /**
   * The company service.
   */
  private final CompanyService companyService;

  /**
   * Constructs a controller with the specified dependencies.
   *
   * @param invoiceServ The invoice service.
   * @param companyServ The company service.
   */
  public InvoiceController(
        final InvoiceService invoiceServ,
        final DefaultCompanyService companyServ
  ) {
    super();
    invoiceService =
          checkNotNull(invoiceServ, "Received a null pointer as service");
    companyService =
          checkNotNull(companyServ, "Received a null pointer as service");
  }

  /**
   * Create a new invoice.
   *
   * @param invoiceCreateRequestForm form with data to create invoice.
   *                                 {@link CreateInvoiceRequestForm}.
   * @return response with the created invoice data.
   */
  @CrossOrigin
  @PostMapping()
  public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody @Valid
  final CreateInvoiceRequestForm invoiceCreateRequestForm) {
    final var sellerNif = invoiceCreateRequestForm.sellerNif();
    final var buyerNif = invoiceCreateRequestForm.buyerNif();
    try {
      final var sellerCompany =
            (PersistentCompanyEntity) companyService.findById(sellerNif);
      final var buyerCompany =
            (PersistentCompanyEntity) companyService.findById(buyerNif);

      if (sellerCompany.getNif().equals(buyerCompany.getNif())) {
        throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Seller and buyer cannot be the same"
        );
      }
      final var result = invoiceService.add(
            invoiceCreateRequestForm.number(),
            invoiceCreateRequestForm.series(),
            invoiceCreateRequestForm.expeditionDate(),
            invoiceCreateRequestForm.advancePaymentDate(),
            invoiceCreateRequestForm.taxExempt(), sellerCompany, buyerCompany
      );
      final var response = new InvoiceResponse(
            result.getNumber(), result.getSeries(),
            result.getAdvancePaymentDate(), result.getExpeditionDate(),
            result.getTaxExempt(), result.getSeller().getNif(),
            result.getBuyer().getNif()
      );
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (CompanyServiceException | InvoiceServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
  }

  /**
   * Get an invoice by its series and number.
   *
   * @param series The invoice series.
   * @param number The invoice number.
   * @return Response status 200 OK with invoice details or error status.
   */
  @CrossOrigin
  @GetMapping("/{series}/{number}")
  public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable
  final String series, @PathVariable
  final int number) {
    try {
      var invoiceEntity = invoiceService.findBySeriesAndNumber(series, number);
      var response = InvoiceResponse.fromEntity(invoiceEntity);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvoiceServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
  }

  /**
   * Returns all stored invoices.
   *
   * @return all stored invoices.
   */
  @CrossOrigin
  @GetMapping()
  public ResponseEntity<InvoiceListResponse> getAllInvoices() {

    final var invoices = invoiceService.getInvoices();
    final var invoiceResponseList = new ArrayList<InvoiceResponse>();
    invoices.forEach(
          (InvoiceEntity invoice) -> invoiceResponseList.add(
                new InvoiceResponse(
                      invoice.getNumber(), invoice.getSeries(),
                      invoice.getAdvancePaymentDate(),
                      invoice.getExpeditionDate(), invoice.getTaxExempt(),
                      ((PersistentInvoiceEntity) invoice).getSeller().getNif(),
                      ((PersistentInvoiceEntity) invoice).getBuyer().getNif()
                )
          )
    );
    return new ResponseEntity<>(
          new InvoiceListResponse(invoiceResponseList), HttpStatus.OK
    );
  }

  /**
   * Delete invoice providing a invoice series and number.
   *
   * @param series The invoice series.
   * @param number The invoice number.
   * @return Response status 200 OK or some error.
   */
  @CrossOrigin
  @DeleteMapping("/{series}/{number}")
  public ResponseEntity<Boolean> deleteInvoice(@PathVariable
  final String series, @PathVariable
  final int number) {
    try {
      invoiceService.remove(series, number);
    } catch (InvoiceServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

}
