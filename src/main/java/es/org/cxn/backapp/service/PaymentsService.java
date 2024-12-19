/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import es.org.cxn.backapp.model.PaymentsEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.service.dto.PaymentDetails;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;

/**
 * Interface for services that handle payment operations.
 * <p>
 * This interface defines the methods for managing payment records in the
 * system. It includes the ability to create new payment records, which are then
 * saved to the database with relevant details such as the payment amount,
 * category, description, title, and associated user.
 * </p>
 *
 * @see PaymentsEntity
 * @see PaymentsCategory
 * @see PaymentsState
 */
public interface PaymentsService {

    /**
     * Cancels a payment by updating its state to {@link PaymentsState#CANCELLED}.
     * <p>
     * This method retrieves a payment record by its unique identifier, checks if
     * the payment is already in the {@link PaymentsState#CANCELLED} state, and if
     * not, updates its state to {@link PaymentsState#CANCELLED}. The updated
     * payment entity is then saved to the database.
     * </p>
     *
     * @param paymentId the unique identifier of the payment to be cancelled.
     * @return the updated payment entity with state set to
     *         {@link PaymentsState#CANCELLED}.
     * @throws PaymentsServiceException if the payment with the given ID does not
     *                                  exist or if the payment is already in the
     *                                  {@link PaymentsState#CANCELLED} state.
     */
    PaymentsEntity cancelPayment(UUID paymentId) throws PaymentsServiceException;

    /**
     * Creates a new payment record with the given parameters and saves it to the
     * database.
     * <p>
     * This method creates a new payment entity, sets the provided values (amount,
     * category, description, title, and user DNI), sets the payment state to
     * {@link PaymentsState#UNPAID}, and assigns the current date and time to the
     * created and paid timestamps.
     * </p>
     *
     * @param amount      the monetary amount of the payment.
     * @param category    the category of the payment (e.g., MEMBER_PAYMENT).
     * @param description the description of the payment.
     * @param title       the title or name of the payment.
     * @param userDni     the DNI of the user associated with the payment.
     * @return the newly created and saved payment entity.
     * @throws PaymentsServiceException if the payment amount is {@code null} or not
     *                                  greater than zero.
     * @see PaymentsCategory
     * @see PaymentsState
     */
    PaymentsEntity createPayment(BigDecimal amount, PaymentsCategory category, String description, String title,
            String userDni) throws PaymentsServiceException;

    /**
     * Finds a payment by its ID.
     *
     * <p>
     * This method attempts to retrieve a {@link PaymentsEntity} from the repository
     * based on the provided payment ID. If no payment is found with the specified
     * ID, a {@link PaymentsServiceException} is thrown.
     * </p>
     *
     * @param paymentId The ID of the payment to be retrieved.
     * @return The {@link PaymentsEntity} object corresponding to the given payment
     *         ID.
     * @throws PaymentsServiceException if no payment with the specified ID is
     *                                  found. The exception message will indicate
     *                                  the ID that was not found.
     *
     * @see PaymentsEntity
     * @see PaymentsServiceException
     */
    PaymentsEntity findPayment(UUID paymentId) throws PaymentsServiceException;

    /**
     * Retrieves all users with their associated payments.
     *
     * @return a map where the key is the user's DNI and the value is a list of
     *         their payments.
     */
    Map<String, List<PaymentDetails>> getAllUsersWithPayments();

    /**
     * Retrieves all payments associated with a given user's DNI.
     * <p>
     * This method queries the payments repository to find all payments linked to
     * the specified user, identified by their DNI (Documento Nacional de
     * Identidad). The resulting list contains all payment records related to the
     * user. If user not exists or no have payments an empty list is returned.
     * </p>
     *
     * @param userDni the DNI (Documento Nacional de Identidad) of the user whose
     *                payments are to be retrieved. Must not be null or empty.
     * @return a list of {@link PaymentsEntity} objects representing the user's
     *         payments. If no payments are found, an empty list is returned.
     * @throws IllegalArgumentException if the provided userDni is null or empty.
     */
    List<PersistentPaymentsEntity> getUserPayments(String userDni);

    /**
     * Processes a payment by updating its state to {@link PaymentsState#PAID} and
     * setting the paid timestamp.
     * <p>
     * This method retrieves a payment record by its unique identifier, verifies
     * that the payment is currently in the {@link PaymentsState#UNPAID} state, and
     * then changes its state to {@link PaymentsState#PAID}. The timestamp of the
     * payment is also updated to the provided payment date.
     * </p>
     *
     * @param paymentId   the unique identifier of the payment to be processed.
     * @param paymentDate the date and time when the payment was made.
     * @return the updated payment entity with state set to
     *         {@link PaymentsState#PAID} and the paid timestamp set.
     * @throws PaymentsServiceException if the payment with the given ID does not
     *                                  exist or if the payment is not in the
     *                                  {@link PaymentsState#UNPAID} state.
     */
    PaymentsEntity makePayment(UUID paymentId, LocalDateTime paymentDate) throws PaymentsServiceException;

}
