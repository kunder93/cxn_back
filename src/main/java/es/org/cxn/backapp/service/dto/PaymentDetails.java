package es.org.cxn.backapp.service.dto;

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

import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;

/**
 * Represents the details of a payment, including its title, description,
 * amount, category, state, and associated timestamps.
 *
 * @param id          the payment identifier as a {link UUID}.
 * @param title       the title of the payment.
 * @param description the description of the payment.
 * @param amount      the payment amount as a {@link BigDecimal}.
 * @param category    the {@link PaymentsCategory} of the payment.
 * @param state       the {@link PaymentsState} of the payment.
 * @param createdAt   the {@link LocalDateTime} when the payment was created.
 * @param paidAt      the {@link LocalDateTime} when the payment was completed,
 *                    or {@code null} if not yet paid.
 */
public record PaymentDetails(UUID id, String title, String description, BigDecimal amount, PaymentsCategory category,
        PaymentsState state, LocalDateTime createdAt, LocalDateTime paidAt) {
}
