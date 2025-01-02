package es.org.cxn.backapp.service.dto;

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
