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

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.form.requests.AddFoodHousingToPaymentSheetRequest;
import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequest;
import es.org.cxn.backapp.model.form.requests.AddSelfVehicleRequest;
import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequest;
import es.org.cxn.backapp.model.form.responses.PaymentSheetListResponse;
import es.org.cxn.backapp.model.form.responses.PaymentSheetResponse;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.service.PaymentSheetService;
import jakarta.validation.Valid;

/**
 * Controller for payment sheets.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/paymentSheet")
public class PaymentSheetController {

    /**
     * The payment sheet service.
     */
    private final PaymentSheetService paymentSheetService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service The payment sheet service.
     */
    public PaymentSheetController(final PaymentSheetService service) {
        super();

        paymentSheetService = checkNotNull(service, "Received a null pointer as service");
    }

    /**
     * Add food housing to payment sheet.
     *
     * @param paymentSheetId The payment sheet identifier.
     * @param requestForm    The food housing data for add to payment sheet.
     * @return The payment sheet with data added.
     */
    @PostMapping("/{paymentSheetId}" + "/addFoodHousing")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<String> addFoodHousingToPaymentSheet(@PathVariable final Integer paymentSheetId,
            @RequestBody final AddFoodHousingToPaymentSheetRequest requestForm) {
        try {
            paymentSheetService.addFoodHousingToPaymentSheet(paymentSheetId, requestForm.amountDays(),
                    requestForm.dayPrice(), requestForm.overnight());

            return new ResponseEntity<>("", HttpStatus.OK);

        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Create a regular transport for payment sheet.
     *
     * @param paymentSheetId The payment sheet identifier.
     * @param requestForm    the new data for regular transport.
     * @return The regular transport data.
     */
    @PostMapping("/{paymentSheetId}" + "/addRegularTransport")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<String> addRegularTransportToPaymentSheet(@PathVariable final Integer paymentSheetId,
            @RequestBody final AddRegularTransportRequest requestForm) {
        try {
            paymentSheetService.addRegularTransportToPaymentSheet(paymentSheetId, requestForm.category(),
                    requestForm.description(), requestForm.invoiceNumber().intValue(), requestForm.invoiceSeries());
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Create self vehicle for payment sheet.
     *
     * @param paymentSheetId The payment sheet identifier.
     * @param requestForm    The data for new self vehicle associated with payment
     *                       sheet.
     * @return The payment sheet data with self vehicle assocaited.
     */
    @PostMapping("/{paymentSheetId}" + "/addSelfVehicle")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<String> addSelfVehicleToPaymentSheet(@PathVariable final Integer paymentSheetId,
            @RequestBody final AddSelfVehicleRequest requestForm) {
        try {
            paymentSheetService.addSelfVehicleToPaymentSheet(paymentSheetId, requestForm.places(),
                    requestForm.distance(), requestForm.kmPrice());

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    /**
     * Creates a new payment sheet based on the provided form data.
     * <p>
     * This endpoint accepts a {@link CreatePaymentSheetRequest} and uses it to
     * create a new payment sheet. The created payment sheet is returned in a
     * {@link PaymentSheetResponse} with an HTTP status of {@code CREATED}.
     * </p>
     *
     * @param createPaymentSheetRequestForm the form with data to create the payment
     *                                      sheet.
     * @return a {@link ResponseEntity} containing the {@link PaymentSheetResponse}
     *         with HTTP status {@code CREATED} if the creation is successful, or
     *         {@code BAD_REQUEST} if there are validation issues.
     */
    @PostMapping
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<PaymentSheetResponse> createPaymentSheet(@RequestBody
    @Valid final CreatePaymentSheetRequest createPaymentSheetRequestForm) {
        try {
            // Create the payment sheet entity using the service
            final var paymentSheetEntity = paymentSheetService.add(createPaymentSheetRequestForm.reason(),
                    createPaymentSheetRequestForm.place(), createPaymentSheetRequestForm.startDate(),
                    createPaymentSheetRequestForm.endDate(), createPaymentSheetRequestForm.userEmail());

            // Convert the created entity to a PaymentSheetResponse
            final var response = PaymentSheetResponse.fromEntity((PersistentPaymentSheetEntity) paymentSheetEntity);

            // Return the response with HTTP status CREATED
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PaymentSheetServiceException e) {
            // Handle the case where creation fails
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Delete a payment sheet by its identifier.
     *
     * @param paymentSheetId The payment sheet identifier.
     * @return ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/{paymentSheetId}")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<Void> deletePaymentSheet(@PathVariable final Integer paymentSheetId) {
        try {
            paymentSheetService.deletePaymentSheet(paymentSheetId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Retrieves a payment sheet by its identifier.
     * <p>
     * This endpoint fetches a payment sheet based on its ID and returns it as a
     * {@link PaymentSheetResponse}. If the payment sheet is not found, a
     * {@link ResponseStatusException} with a {@code NOT_FOUND} status is thrown.
     * </p>
     *
     * @param paymentSheetId the identifier of the payment sheet to retrieve
     * @return a {@link ResponseEntity} containing the {@link PaymentSheetResponse}
     *         and HTTP status {@code OK} if found, or {@code NOT_FOUND} if not
     *         found.
     */
    @CrossOrigin
    @GetMapping("/{paymentSheetId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<PaymentSheetResponse> getPaymentSheet(@PathVariable final Integer paymentSheetId) {
        try {
            // Fetch the payment sheet entity from the service
            final var paymentSheetEntity = paymentSheetService.findById(paymentSheetId);

            // Convert the entity to a PaymentSheetResponse using the static method
            final var response = PaymentSheetResponse.fromEntity(paymentSheetEntity);

            // Return the response with HTTP status OK
            return ResponseEntity.ok(response);
        } catch (PaymentSheetServiceException e) {
            // Handle the case where the payment sheet is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Get the Payment Sheets list with all payments.
     *
     * @return A {@link ResponseEntity} containing a
     *         {@link PaymentSheetListResponse} with all payment sheets.
     */
    @GetMapping
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<PaymentSheetListResponse> getPaymentSheets() {

        // Fetch payment sheets entities from the service
        final Collection<PersistentPaymentSheetEntity> paymentSheetsEntityList = paymentSheetService.getPaymentSheets();

        // Convert the collection of entities to a PaymentSheetListResponse using
        // the fromEntities method
        final var responseList = PaymentSheetListResponse.fromEntities(paymentSheetsEntityList);

        return new ResponseEntity<>(responseList, HttpStatus.OK);

    }

    /**
     * Remmove food housing from payment sheet.
     *
     * @param paymentSheetId The payment sheet identifier.
     * @return The payment sheet data without food housing.
     */
    @PostMapping("/{paymentSheetId}" + "/removeFoodHousing")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<String> removeFoodHousingFromPaymentSheet(@PathVariable final Integer paymentSheetId) {
        try {
            paymentSheetService.removeFoodHousing(paymentSheetId);
            return new ResponseEntity<>("", HttpStatus.OK);

        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Remove regular transport from payment sheet.
     *
     * @param paymentSheetId     The payment sheet identifier.
     * @param regularTransportId The regular transport identifier.
     * @return The payment sheet without regular transport that has been deleted.
     */
    @PostMapping("/{paymentSheetId}" + "/{regularTransportId}")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<String> removeRegularTransportFromPaymentSheet(@PathVariable final Integer paymentSheetId,
            @PathVariable final Integer regularTransportId) {
        try {
            paymentSheetService.removeRegularTransportFromPaymentSheet(paymentSheetId, regularTransportId);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Remove self vehicle from payment sheet.
     *
     * @param paymentSheetId The payment sheet identifier.
     * @return The payment sheet data without self vehicle.
     */
    @PostMapping("/{paymentSheetId}" + "/removeSelfVehicle")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<String> removeSelfVehicleFromPaymentSheet(@PathVariable final Integer paymentSheetId) {
        try {
            paymentSheetService.removeSelfVehicle(paymentSheetId);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (PaymentSheetServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
