package es.org.cxn.backapp.service.dto;

import java.math.BigDecimal;

import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;

/**
 * Represents the details of a payment, including its amount, category, and
 * state.
 *
 * @param amount   the payment amount as a {@link BigDecimal}.
 * @param category the {@link PaymentsCategory} of the payment.
 * @param state    the {@link PaymentsState} of the payment.
 */
public record PaymentDetails(BigDecimal amount, PaymentsCategory category, PaymentsState state) {
}
