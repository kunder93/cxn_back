package es.org.cxn.backapp.service.dto;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;

/**
 * Data Transfer Object (DTO) that encapsulates information about a payment.
 *
 * <p>
 * This record is used to expose payment data from the persistence layer to the
 * service and API layers. It provides a read-only projection of the underlying
 * {@link PersistentPaymentsEntity}.
 * </p>
 *
 * @param id          The unique identifier of the payment.
 * @param title       The title or concept of the payment.
 * @param description A detailed textual description of the payment. May contain
 *                    multi-line or long-form text.
 * @param state       The current processing state of the payment (e.g.,
 *                    PENDING, COMPLETED).
 * @param category    The category under which the payment is classified (e.g.,
 *                    FEE, DONATION).
 * @param paidAt      The timestamp indicating when the payment was made. May be
 *                    {@code null} if not yet paid.
 * @param amount      The monetary amount associated with the payment.
 * @param userDni     The DNI (Documento Nacional de Identidad) of the user
 *                    associated with the payment.
 * @param createdAt   The timestamp indicating when the payment record was
 *                    created.
 *
 * @see PersistentPaymentsEntity
 * @see PaymentsCategory
 * @see PaymentsState
 */
public record PaymentDto(UUID id, String title, String description, PaymentsState state, PaymentsCategory category,
        LocalDateTime paidAt, BigDecimal amount, String userDni, LocalDateTime createdAt) {

    /**
     * Constructs a new {@link PaymentDto} instance from the specified
     * {@link PersistentPaymentsEntity}.
     *
     * <p>
     * This constructor is intended to map all relevant fields from the JPA entity
     * to a DTO suitable for external consumption. All values are directly extracted
     * from the provided entity. It is expected to be used within a transactional
     * context, especially when accessing {@code @Lob} fields like
     * {@code description}.
     * </p>
     *
     * @param entity The {@link PersistentPaymentsEntity} to map from. Must not be
     *               {@code null}.
     */
    public PaymentDto(PersistentPaymentsEntity entity) {
        this(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getState(), entity.getCategory(),
                entity.getPaidAt(), entity.getAmount(), entity.getUserDni(), entity.getCreatedAt());
    }
}
