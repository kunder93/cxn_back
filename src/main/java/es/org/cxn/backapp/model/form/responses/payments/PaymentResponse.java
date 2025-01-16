package es.org.cxn.backapp.model.form.responses.payments;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import es.org.cxn.backapp.model.PaymentsEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;

/**
 * Represents a response containing payment information. This record is used to
 * provide detailed information about a payment in response to API requests.
 *
 * @param id          The unique identifier of the payment.
 * @param title       The title of the payment, describing its purpose or
 *                    reason.
 * @param description A brief description of the payment, providing additional
 *                    details.
 * @param state       The current state of the payment (e.g., pending,
 *                    completed, canceled).
 * @param category    The category of the payment, representing its
 *                    classification or type.
 * @param amount      The amount of the payment. Must be a positive decimal
 *                    value.
 * @param userDni     The DNI of the user associated with the payment.
 * @param createdAt   The date and time when the payment was created.
 * @param paidAt      The date and time when the payment was marked as paid. Can
 *                    be null if not yet paid.
 */
public record PaymentResponse(UUID id, String title, String description, PaymentsState state, PaymentsCategory category,
        BigDecimal amount, String userDni, LocalDateTime createdAt, LocalDateTime paidAt) {

    /**
     * Constructs a PaymentResponse object from a {@link PaymentsEntity}.
     *
     * @param entity The PaymentsEntity object containing payment data. Must not be
     *               null.
     */
    public PaymentResponse(final PaymentsEntity entity) {
        this(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getState(), entity.getCategory(),
                entity.getAmount(), entity.getUserDni(), entity.getCreatedAt(), entity.getPaidAt());
    }
}
