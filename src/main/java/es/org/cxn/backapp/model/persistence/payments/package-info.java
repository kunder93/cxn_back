/**
 * Contains classes and enums related to the persistence of payment data.
 * <p>
 * This package provides the following components:
 * </p>
 * <ul>
 * <li>{@link es.org.cxn.backapp.model.persistence.payments.PaymentsCategory} -
 * Enum defining the various categories of payments, such as membership,
 * federation, and others.</li>
 * <li>{@link es.org.cxn.backapp.model.persistence.payments.PaymentsState} -
 * Enum representing the possible states of a payment, including unpaid, paid,
 * and cancelled.</li>
 * <li>{@link es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity}
 * - Entity class representing payment records in the database, including fields
 * for payment details and associated user information.</li>
 * </ul>
 *
 * <p>
 * The classes in this package are used to model and manage payment-related data
 * in the persistence layer of the application. They enable the application to
 * categorize, track, and process payments efficiently.
 * </p>
 *
 * @author Santi
 */
package es.org.cxn.backapp.model.persistence.payments;
