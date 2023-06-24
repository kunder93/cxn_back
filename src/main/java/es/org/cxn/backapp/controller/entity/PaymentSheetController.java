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

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.form.requests.AddFoodHousingToPaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequestForm;
import es.org.cxn.backapp.model.form.requests.AddSelfVehicleRequestForm;
import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.responses.PaymentSheetListResponse;
import es.org.cxn.backapp.model.form.responses.PaymentSheetResponse;
import es.org.cxn.backapp.service.PaymentSheetService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for payment sheets.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/paymentSheet")
public class PaymentSheetController {

  /**
   * COMMON MESSAGE PART IN EXCEPTION MESSAGES.
   */
  private static final String EXCEPTION_MESSAGE_WRAPPER =
        "An PaymentSheetServiceException occurred: {}";

  /**
   * The logger.
   */
  private static final Logger LOGGER =
        LoggerFactory.getLogger(PaymentSheetController.class);
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

    paymentSheetService =
          checkNotNull(service, "Received a null pointer as service");
  }

  /**
   * Get the Payment Sheets list with all payments.
   *
   * @return List with all payment sheets.
   */
  @GetMapping
  public ResponseEntity<PaymentSheetListResponse> getPaymentSheets() {
    final var paymentSheetsEntityList = paymentSheetService.getPaymentSheets();
    final var responseList =
          new PaymentSheetListResponse(paymentSheetsEntityList);
    return new ResponseEntity<>(responseList, HttpStatus.OK);

  }

  /**
   * Get the Payment Sheet data trough his identifier.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @return form with the created company data.
   */
  @GetMapping(value = "/{paymentSheetId}")
  public ResponseEntity<PaymentSheetResponse> getPaymentSheet(@PathVariable
  final Integer paymentSheetId) {
    try {
      final var result = paymentSheetService.findById(paymentSheetId);
      final var response = new PaymentSheetResponse(result);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Create a new payment sheet.
   *
   * @param createPaymentSheetRequestForm form with data to create payment
   *                                      sheet.
   * @return form with the created payment sheet data.
   */
  @PostMapping()
  public ResponseEntity<PaymentSheetResponse>
        createPaymentSheet(@RequestBody @Valid
  final CreatePaymentSheetRequestForm createPaymentSheetRequestForm) {
    try {
      final var result = paymentSheetService.add(
            createPaymentSheetRequestForm.getReason(),
            createPaymentSheetRequestForm.getPlace(),
            createPaymentSheetRequestForm.getStartDate(),
            createPaymentSheetRequestForm.getEndDate(),
            createPaymentSheetRequestForm.getUserEmail()
      );
      final var response = new PaymentSheetResponse(result);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Create a regular transport for payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @param requestForm    the new data for regular transport.
   * @return The regular transport data.
   */
  @PostMapping(value = "/{paymentSheetId}" + "/addRegularTransport")
  public ResponseEntity<String> addRegularTransportToPaymentSheet(@PathVariable
  final Integer paymentSheetId, @RequestBody
  final AddRegularTransportRequestForm requestForm) {
    try {
      paymentSheetService.addRegularTransportToPaymentSheet(
            paymentSheetId, requestForm.getCategory(),
            requestForm.getDescription(), requestForm.getInvoiceNumber(),
            requestForm.getInvoiceSeries()
      );
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Remove regular transport from payment sheet.
   *
   * @param paymentSheetId     The payment sheet identifier.
   * @param regularTransportId The regular transport identifier.
   * @return The payment sheet without regular transport that has been deleted.
   */
  @PostMapping(value = "/{paymentSheetId}" + "/{regularTransportId}")
  public ResponseEntity<String>
        removeRegularTransportFromPaymentSheet(@PathVariable
  final Integer paymentSheetId, @PathVariable
  final Integer regularTransportId) {
    try {
      paymentSheetService.removeRegularTransportFromPaymentSheet(
            paymentSheetId, regularTransportId
      );
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
  @PostMapping(value = "/{paymentSheetId}" + "/addSelfVehicle")
  public ResponseEntity<String> addSelfVehicleToPaymentSheet(@PathVariable
  final Integer paymentSheetId, @RequestBody
  final AddSelfVehicleRequestForm requestForm) {
    try {
      paymentSheetService.addSelfVehicleToPaymentSheet(
            paymentSheetId, requestForm.getPlaces(), requestForm.getDistance(),
            requestForm.getKmPrice()
      );

      return new ResponseEntity<>("", HttpStatus.OK);

    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

  }

  /**
   * Remove self vehicle from payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @return The payment sheet data without self vehicle.
   */
  @PostMapping(value = "/{paymentSheetId}" + "/removeSelfVehicle")
  public ResponseEntity<String> removeSelfVehicleFromPaymentSheet(@PathVariable
  final Integer paymentSheetId) {
    try {
      paymentSheetService.removeSelfVehicle(paymentSheetId);
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Add food housing to payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @param requestForm    The food housing data for add to payment sheet.
   * @return The payment sheet with data added.
   */
  @PostMapping(value = "/{paymentSheetId}" + "/addFoodHousing")
  public ResponseEntity<String> addFoodHousingToPaymentSheet(@PathVariable
  final Integer paymentSheetId, @RequestBody
  final AddFoodHousingToPaymentSheetRequestForm requestForm) {
    try {
      paymentSheetService.addFoodHousingToPaymentSheet(
            paymentSheetId, requestForm.getAmountDays(),
            requestForm.getDayPrice(), requestForm.getOvernight()
      );

      return new ResponseEntity<>("", HttpStatus.OK);

    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Remmove food housing from payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @return The payment sheet data without food housing.
   */
  @PostMapping(value = "/{paymentSheetId}" + "/removeFoodHousing")
  public ResponseEntity<String> removeFoodHousingFromPaymentSheet(@PathVariable
  final Integer paymentSheetId) {
    try {
      paymentSheetService.removeFoodHousing(paymentSheetId);
      return new ResponseEntity<>("", HttpStatus.OK);

    } catch (PaymentSheetServiceException e) {
      LOGGER.error(EXCEPTION_MESSAGE_WRAPPER, e.getMessage(), e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
