package es.org.cxn.backapp.model.form.responses.payments;

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
    public PaymentResponse(PaymentsEntity entity) {
        this(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getState(), entity.getCategory(),
                entity.getAmount(), entity.getUserDni(), entity.getCreatedAt(), entity.getPaidAt());
    }
}
