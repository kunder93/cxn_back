
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.PaymentsEntity;
import es.org.cxn.backapp.model.form.requests.payments.CreatePaymentRequest;
import es.org.cxn.backapp.model.form.responses.payments.PaymentResponse;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import jakarta.validation.Valid;

/**
 * Controller for handling payment-related operations. Provides endpoints for
 * creating, retrieving, updating, and managing payments.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    /**
     * Service for manage payments.
     */
    private final PaymentsService paymentsService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service The payments service to be used by this controller. Must not
     *                be null.
     * @throws NullPointerException if the provided service is null.
     */
    public PaymentsController(final PaymentsService service) {
        super();
        paymentsService = checkNotNull(service, "Received a null pointer as service");
    }

    /**
     * Cancels a payment by its ID.
     *
     * @param paymentId The unique identifier of the payment to be canceled.
     * @return A ResponseEntity containing the updated payment information.
     * @throws ResponseStatusException if the payment cannot be canceled.
     */
    @PatchMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(@Valid
    @PathVariable final UUID paymentId) {
        try {
            PaymentsEntity updatedPayment;
            updatedPayment = paymentsService.cancelPayment(paymentId);
            final var response = new PaymentResponse(updatedPayment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PaymentsServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Creates a new payment.
     *
     * @param request The request containing payment details.
     * @return A ResponseEntity containing the created payment information.
     * @throws ResponseStatusException if the payment cannot be created.
     */
    @PostMapping()
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody
    @Valid final CreatePaymentRequest request) {
        try {
            final var createdPayment = paymentsService.createPayment(request.amount(), request.category(),
                    request.description(), request.title(), request.userDni());
            final var response = new PaymentResponse(createdPayment);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (PaymentsServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Retrieves payment information by ID.
     *
     * @param paymentId The unique identifier of the payment.
     * @return A ResponseEntity containing the payment information.
     * @throws ResponseStatusException if the payment cannot be found.
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentInfo(@PathVariable final UUID paymentId) {
        try {
            final var paymentFound = paymentsService.findPayment(paymentId);
            final var response = new PaymentResponse(paymentFound);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PaymentsServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    /**
     * Retrieves all payments associated with a user.
     *
     * @param userDni The DNI of the user.
     * @return A ResponseEntity containing a list of payments for the user.
     */
    @GetMapping("/user/{userDni}")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(@PathVariable final String userDni) {
        final List<PersistentPaymentsEntity> results = paymentsService.getUserPayments(userDni);
        List<PaymentResponse> responsesList = new ArrayList<>();
        results.forEach((PersistentPaymentsEntity paymentEntity) -> {
            responsesList.add(new PaymentResponse(paymentEntity));
        });
        return new ResponseEntity<>(responsesList, HttpStatus.OK);
    }

    /**
     * Marks a payment as paid.
     *
     * @param paymentId The unique identifier of the payment to be marked as paid.
     * @return A ResponseEntity containing the updated payment information.
     * @throws ResponseStatusException if the payment cannot be updated.
     */
    @PatchMapping("/{paymentId}/pay")
    public ResponseEntity<PaymentResponse> makePayment(@PathVariable final UUID paymentId) {
        try {
            PaymentsEntity updatedPayment;
            updatedPayment = paymentsService.makePayment(paymentId, LocalDateTime.now());
            final var response = new PaymentResponse(updatedPayment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PaymentsServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
