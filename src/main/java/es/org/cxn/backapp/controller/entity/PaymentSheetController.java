/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
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

package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.form.requests.AddFoodHousingToPaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequestForm;
import es.org.cxn.backapp.model.form.requests.AddSelfVehicleRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;
import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.responses.PaymentSheetListResponse;
import es.org.cxn.backapp.model.form.responses.PaymentSheetResponse;
import es.org.cxn.backapp.service.PaymentSheetService;
import jakarta.validation.Valid;

/**
 * Rest controller for companies.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/paymentSheet")
public class PaymentSheetController {

    /**
     * The company service.
     */
    private final PaymentSheetService paymentSheetService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service company service.
     */
    public PaymentSheetController(final PaymentSheetService service) {
        super();

        paymentSheetService = checkNotNull(
                service, "Received a null pointer as service"
        );
    }

    /**
     * Get the Payment Sheets list with all payments.
     *
     * @return List with all payment sheets.
     */
    @GetMapping
    public ResponseEntity<PaymentSheetListResponse> getPaymentSheets() {
        var paymentSheetsEntityList = paymentSheetService.getPaymentSheets();
        var responseList = new PaymentSheetListResponse(
                paymentSheetsEntityList
        );
        return new ResponseEntity<>(responseList, HttpStatus.OK);

    }

    /**
     * Get the Payment Sheet response data though his identifier.
     *
     * @param createCompanyRequestForm form with data to create company.
     *                                 {@link CreateCompanyRequestForm}.
     * @return form with the created company data.
     */
    @GetMapping(value = "/{paymentSheetId}")
    public ResponseEntity<PaymentSheetResponse> getPaymentSheet(
            @PathVariable Integer paymentSheetId
    ) {
        try {
            var result = paymentSheetService.findById(paymentSheetId);
            var response = new PaymentSheetResponse(result);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    /**
     * Create a new payment sheet.
     *
     * @param createCompanyRequestForm form with data to create company.
     *                                 {@link CreateCompanyRequestForm}.
     * @return form with the created company data.
     */
    @PostMapping()
    public ResponseEntity<PaymentSheetResponse> createPaymentSheet(
            @RequestBody @Valid final CreatePaymentSheetRequestForm createPaymentSheetRequestForm
    ) {
        try {
            var result = paymentSheetService.add(
                    createPaymentSheetRequestForm.getReason(),
                    createPaymentSheetRequestForm.getPlace(),
                    createPaymentSheetRequestForm.getStartDate(),
                    createPaymentSheetRequestForm.getEndDate(),
                    createPaymentSheetRequestForm.getUserEmail()
            );
            var response = new PaymentSheetResponse(result);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    /**
     * Create a regular transport for payment sheet.
     *
     * @param nifCif      the company nif or cif.
     * @param requestForm the new data for update company.
     * @return The company with data updated.
     */
    @PostMapping(value = "/{paymentSheetId}" + "/addRegularTransport")
    public ResponseEntity<String> addRegularTransportToPaymentSheet(
            @PathVariable Integer paymentSheetId,
            @RequestBody AddRegularTransportRequestForm requestForm
    ) {
        try {
            var result = paymentSheetService.addRegularTransportToPaymentSheet(
                    paymentSheetId, requestForm.getCategory(),
                    requestForm.getDescription(),
                    requestForm.getInvoiceNumber(),
                    requestForm.getInvoiceSeries()
            );
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    /**
     * Create a regular transport for payment sheet.
     *
     * @param nifCif      the company nif or cif.
     * @param requestForm the new data for update company.
     * @return The company with data updated.
     */
    @PostMapping(value = "/{paymentSheetId}" + "/{regularTransportId}")
    public ResponseEntity<String> removeRegularTransportFromPaymentSheet(
            @PathVariable Integer paymentSheetId,
            @PathVariable Integer regularTransportId
    ) {
        try {
            paymentSheetService.removeRegularTransportFromPaymentSheet(
                    paymentSheetId, regularTransportId
            );
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    /**
     *
     *
     * @param nifCif      the company nif or cif.
     * @param requestForm the new data for update company.
     * @return The company with data updated.
     */
    @PostMapping(value = "/{paymentSheetId}" + "/addSelfVehicle")
    public ResponseEntity<String> addSelfVehicleToPaymentSheet(
            @PathVariable Integer paymentSheetId,
            @RequestBody AddSelfVehicleRequestForm requestForm
    ) {
        try {
            var result = paymentSheetService.addSelfVehicleToPaymentSheet(
                    paymentSheetId, requestForm.getPlaces(),
                    requestForm.getDistance(), requestForm.getKmPrice()
            );

            return new ResponseEntity<>("", HttpStatus.OK);

        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }

    }

    /**
     *
     *
     * @param nifCif      the company nif or cif.
     * @param requestForm the new data for update company.
     * @return The company with data updated.
     */
    @PostMapping(value = "/{paymentSheetId}" + "/removeSelfVehicle")
    public ResponseEntity<String> removeSelfVehicleFromPaymentSheet(
            @PathVariable Integer paymentSheetId
    ) {
        try {
            paymentSheetService.removeSelfVehicle(paymentSheetId);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    /**
     *
     *
     * @param nifCif      the company nif or cif.
     * @param requestForm the new data for update company.
     * @return The company with data updated.
     */
    @PostMapping(value = "/{paymentSheetId}" + "/addFoodHousing")
    public ResponseEntity<String> addFoodHousingToPaymentSheet(
            @PathVariable Integer paymentSheetId,
            @RequestBody AddFoodHousingToPaymentSheetRequestForm requestForm
    ) {
        try {
            var result = paymentSheetService.addFoodHousingToPaymentSheet(
                    paymentSheetId, requestForm.getAmountDays(),
                    requestForm.getDayPrice(), requestForm.getOvernight()
            );

            return new ResponseEntity<>("", HttpStatus.OK);

        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    /**
     *
     *
     * @param nifCif      the company nif or cif.
     * @param requestForm the new data for update company.
     * @return The company with data updated.
     */
    @PostMapping(value = "/{paymentSheetId}" + "/removeFoodHousing")
    public ResponseEntity<String> removeFoodHousingFromPaymentSheet(
            @PathVariable Integer paymentSheetId
    ) {
        try {
            paymentSheetService.removeFoodHousing(paymentSheetId);
            return new ResponseEntity<>("", HttpStatus.OK);

        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }
}
