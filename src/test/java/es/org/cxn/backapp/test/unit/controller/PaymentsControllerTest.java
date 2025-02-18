
package es.org.cxn.backapp.test.unit.controller;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.controller.entity.PaymentsController;
import es.org.cxn.backapp.model.PaymentsEntity;
import es.org.cxn.backapp.model.form.requests.payments.CreatePaymentRequest;
import es.org.cxn.backapp.model.form.responses.payments.PaymentResponse;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.dto.PaymentDetails;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;

class PaymentsControllerTest {

    @Mock
    private PaymentsService paymentsService;

    @InjectMocks
    private PaymentsController paymentsController;

    @Test
    void cancelPayment_ShouldReturnUpdatedPayment() throws PaymentsServiceException {
        UUID paymentId = UUID.randomUUID();
        PaymentsEntity mockPayment = new PersistentPaymentsEntity();
        when(paymentsService.cancelPayment(paymentId)).thenReturn(mockPayment);

        ResponseEntity<?> response = paymentsController.cancelPayment(paymentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cancelPayment_ShouldThrowException_WhenServiceFails() throws PaymentsServiceException {
        UUID paymentId = UUID.randomUUID();
        when(paymentsService.cancelPayment(paymentId)).thenThrow(new PaymentsServiceException("Error"));

        assertThrows(ResponseStatusException.class, () -> paymentsController.cancelPayment(paymentId));
    }

    @Test
    void createPayment_ShouldReturnCreatedPayment() throws PaymentsServiceException {
        String dni = "12345678A";
        PaymentsEntity mockPayment = new PersistentPaymentsEntity();
        when(paymentsService.createPayment(any(), any(), any(), any(), eq(dni))).thenReturn(mockPayment);

        ResponseEntity<?> response = paymentsController.createPayment(new CreatePaymentRequest("12345678A", "Title",
                "Description", PaymentsCategory.FEDERATE_PAYMENT, new BigDecimal("100.0")));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createPayment_ShouldThrowException_WhenServiceFails() throws PaymentsServiceException {
        when(paymentsService.createPayment(any(), any(), any(), any(), any()))
                .thenThrow(new PaymentsServiceException("Error"));

        assertThrows(ResponseStatusException.class,
                () -> paymentsController.createPayment(new CreatePaymentRequest("12345678A", "Title", "Description",
                        PaymentsCategory.FEDERATE_PAYMENT, new BigDecimal("100.0"))));
    }

    @Test
    void getAllUsersPayments_ShouldReturnUsersPayments() {
        Map<String, List<PaymentDetails>> payments = new HashMap<>();
        when(paymentsService.getAllUsersWithPayments()).thenReturn(payments);

        ResponseEntity<Map<String, List<PaymentDetails>>> response = paymentsController.getAllUsersPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getOwnPayments_ShouldReturnUserPayments() throws PaymentsServiceException {
        String email = "test@example.com";
        PersistentPaymentsEntity mockPayment = new PersistentPaymentsEntity();
        mockPayment.setId(UUID.randomUUID());

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(paymentsService.getUserPaymentsByEmail(email)).thenReturn(List.of(mockPayment));

        ResponseEntity<Set<PaymentDetails>> response = paymentsController.getOwnPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getOwnPayments_ShouldThrowException_WhenServiceFails() throws PaymentsServiceException {
        String email = "test@example.com";

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(paymentsService.getUserPaymentsByEmail(email)).thenThrow(new PaymentsServiceException("Error"));

        assertThrows(ResponseStatusException.class, () -> paymentsController.getOwnPayments());
    }

    @Test
    void getPaymentInfo_ShouldReturnPaymentDetails() throws PaymentsServiceException {
        UUID paymentId = UUID.randomUUID();
        PaymentsEntity mockPayment = new PersistentPaymentsEntity();
        when(paymentsService.findPayment(paymentId)).thenReturn(mockPayment);

        ResponseEntity<?> response = paymentsController.getPaymentInfo(paymentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getPaymentInfo_ShouldThrowException_WhenServiceFails() throws PaymentsServiceException {
        UUID paymentId = UUID.randomUUID();
        when(paymentsService.findPayment(paymentId)).thenThrow(new PaymentsServiceException("Error"));

        assertThrows(ResponseStatusException.class, () -> paymentsController.getPaymentInfo(paymentId));
    }

    @Test
    void getUserPayments_ShouldReturnUserPayments() {
        // Arrange
        String dni = "12345678A";
        PersistentPaymentsEntity mockPayment = new PersistentPaymentsEntity();
        when(paymentsService.getUserPayments(dni)).thenReturn(List.of(mockPayment));

        // Act
        ResponseEntity<List<PaymentResponse>> response = paymentsController.getUserPayments(dni);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void makePayment_ShouldReturnUpdatedPayment() throws PaymentsServiceException {
        UUID paymentId = UUID.randomUUID();
        PaymentsEntity mockPayment = new PersistentPaymentsEntity();
        when(paymentsService.makePayment(eq(paymentId), any())).thenReturn(mockPayment);

        ResponseEntity<?> response = paymentsController.makePayment(paymentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void makePayment_ShouldThrowException_WhenServiceFails() throws PaymentsServiceException {
        UUID paymentId = UUID.randomUUID();
        when(paymentsService.makePayment(eq(paymentId), any())).thenThrow(new PaymentsServiceException("Error"));

        assertThrows(ResponseStatusException.class, () -> paymentsController.makePayment(paymentId));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
