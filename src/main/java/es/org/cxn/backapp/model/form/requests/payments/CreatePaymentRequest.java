package es.org.cxn.backapp.model.form.requests.payments;

import java.math.BigDecimal;

import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;

/**
 * Represents a request to create a payment. This record encapsulates all the
 * necessary information required to create a new payment.
 *
 * @param userDni     The DNI of the user making the payment. Must not be null
 *                    or empty.
 * @param title       The title of the payment. Describes the purpose or reason
 *                    for the payment.
 * @param description A brief description of the payment. Provides additional
 *                    details about the payment.
 * @param category    The category of the payment. Determines the type or
 *                    classification of the payment.
 * @param amount      The amount of the payment. Must be a positive decimal
 *                    value.
 */
public record CreatePaymentRequest(String userDni, String title, String description, PaymentsCategory category,
        BigDecimal amount) {

}
