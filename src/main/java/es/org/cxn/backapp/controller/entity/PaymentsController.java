
package es.org.cxn.backapp.controller.entity;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.dto.PaymentDetails;
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
        paymentsService = Objects.requireNonNull(service, "Received a null pointer as service");
    }

    /**
     * Cancels a payment by its ID.
     *
     * @param paymentId The unique identifier of the payment to be canceled.
     * @return A ResponseEntity containing the updated payment information.
     * @throws ResponseStatusException if the payment cannot be canceled.
     */
    @PatchMapping("/{paymentId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO')")
    public ResponseEntity<PaymentResponse> cancelPayment(@Valid
    @PathVariable final UUID paymentId) {
        try {
            final PaymentsEntity updatedPayment;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO')")
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
     * Retrieves from all users all payment information.
     *
     * @return A List with all users basic info with their payments info list.
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO')")
    public ResponseEntity<Map<String, List<PaymentDetails>>> getAllUsersPayments() {

        final var usersPaymentsList = paymentsService.getAllUsersWithPayments();

        return new ResponseEntity<>(usersPaymentsList, HttpStatus.OK);

    }

    /**
     * Get payments for authenticated user.
     *
     * @return The payments info list for an user.
     */
    @GetMapping()
    public ResponseEntity<Set<PaymentDetails>> getOwnPayments() {
        final var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PaymentDetails> userPayments;
        try {
            userPayments = paymentsService.getUserPaymentsByEmail(userEmail);

            Set<PaymentDetails> response = new HashSet<>();

            userPayments.forEach((PaymentDetails payment) -> response
                    .add(new PaymentDetails(payment.id(), payment.title(), payment.description(), payment.amount(),
                            payment.category(), payment.state(), payment.createdAt(), payment.paidAt())));
            return new ResponseEntity<>(response, HttpStatus.OK);
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO')")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(@PathVariable final String userDni) {
        final List<PaymentDetails> results = paymentsService.getUserPayments(userDni);
        final List<PaymentResponse> responsesList = new ArrayList<>();
        results.forEach(
                (PaymentDetails paymentDetails) -> responsesList.add(new PaymentResponse(paymentDetails, userDni)));
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO')")
    public ResponseEntity<PaymentResponse> makePayment(@PathVariable final UUID paymentId) {
        try {
            final PaymentsEntity updatedPayment;
            updatedPayment = paymentsService.makePayment(paymentId, LocalDateTime.now());
            final var response = new PaymentResponse(updatedPayment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PaymentsServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
