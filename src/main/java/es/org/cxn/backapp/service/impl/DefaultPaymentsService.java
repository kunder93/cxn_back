package es.org.cxn.backapp.service.impl;

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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.model.PaymentsEntity;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.repository.PaymentsEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.dto.PaymentDetails;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

/**
 * DefaultPaymentsService is an implementation of the PaymentsService interface
 * that provides functionality for managing payment records.
 * <p>
 * This service includes methods for creating and saving payment records to the
 * database, including details such as amount, category, description, state, and
 * user information.
 * </p>
 *
 * @see PaymentsService
 * @see PaymentsEntityRepository
 * @see PersistentPaymentsEntity
 */
@Service
public final class DefaultPaymentsService implements PaymentsService {

    /**
     * The repository used for saving and retrieving payment entities.
     */
    private final PaymentsEntityRepository paymentsRepository;

    /**
     * The user repository used by this service.
     */
    private final UserEntityRepository userRepository;

    /**
     * The email service used by this service.
     */
    private final EmailService emailService;

    /**
     * Constructs a DefaultPaymentsService with the provided payments repository.
     *
     * @param repository the repository used to persist payment data. Must not be
     *                   null.
     * @param emailServ  The email service instance used by this service.
     * @param userRepo   the user repository.
     * @throws IllegalArgumentException if the repository is null.
     */
    public DefaultPaymentsService(final PaymentsEntityRepository repository, final UserEntityRepository userRepo,
            final EmailService emailServ) {
        paymentsRepository = checkNotNull(repository, "Payments entity repository cannot be null.");
        userRepository = checkNotNull(userRepo, "User repository cannot be null.");
        emailService = checkNotNull(emailServ, "Email service cannot be null.");
    }

    /**
     * Transforms a list of PersistentPaymentsEntity objects to a list of
     * PaymentDetails.
     *
     * @param payments the list of PersistentPaymentsEntity objects to transform
     * @return an unmodifiable list of PaymentDetails, each representing a payment's
     *         details including title, description, amount, category, state, and
     *         timestamps
     */
    public static List<PaymentDetails> transformToPaymentDetails(final List<PersistentPaymentsEntity> payments) {
        return payments.stream()
                .map(payment -> new PaymentDetails(payment.getId(), payment.getTitle(), payment.getDescription(),
                        payment.getAmount(), payment.getCategory(), payment.getState(), payment.getCreatedAt(),
                        payment.getPaidAt()))
                .toList(); // Stream.toList() for an unmodifiable list
    }

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
    @Override
    public PaymentsEntity cancelPayment(final UUID paymentId) throws PaymentsServiceException {
        final Optional<PersistentPaymentsEntity> paymentOptional = paymentsRepository.findById(paymentId);

        if (paymentOptional.isEmpty()) {
            throw new PaymentsServiceException(getPaymentNotFoundMessage(paymentId));
        }
        final var paymentEntity = paymentOptional.get();
        if (paymentEntity.getState().equals(PaymentsState.CANCELLED)) {
            throw new PaymentsServiceException(
                    "Payment with id: " + paymentId + " is already " + PaymentsState.CANCELLED + ".");
        } else {
            paymentEntity.setState(PaymentsState.CANCELLED);
            return paymentsRepository.save(paymentEntity);
        }
    }

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
    @Override
    public PaymentsEntity createPayment(final BigDecimal amount, final PaymentsCategory category,
            final String description, final String title, final String userDni) throws PaymentsServiceException {

        // Validate mandatory fields
        Objects.requireNonNull(amount, "Payment amount must not be null.");
        Objects.requireNonNull(category, "Payment category must not be null.");
        Objects.requireNonNull(description, "Payment description must not be null.");
        Objects.requireNonNull(title, "Payment title must not be null.");
        Objects.requireNonNull(userDni, "User DNI must not be null.");

        final PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();

        // Validate payment amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentsServiceException("Payment amount must be greater than zero.");
        }

        // Additional validation for amount greater than 100
        if (amount.compareTo(new BigDecimal("100")) > 0) {
            throw new PaymentsServiceException("Amount greater than 100 is not valid.");
        }

        paymentEntity.setAmount(amount);
        paymentEntity.setCategory(category);
        paymentEntity.setCreatedAt(LocalDateTime.now());
        paymentEntity.setPaidAt(null);
        paymentEntity.setDescription(description);
        paymentEntity.setState(PaymentsState.UNPAID);
        paymentEntity.setTitle(title);
        paymentEntity.setUserDni(userDni);

        return paymentsRepository.save(paymentEntity);
    }

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
    @Override
    public PaymentsEntity findPayment(final UUID paymentId) throws PaymentsServiceException {
        final Optional<PersistentPaymentsEntity> paymentOptional = paymentsRepository.findById(paymentId);

        if (paymentOptional.isEmpty()) {
            throw new PaymentsServiceException(getPaymentNotFoundMessage(paymentId));
        }
        return paymentOptional.get();
    }

    /**
     * Retrieves all users with their associated payments.
     *
     * @return a map where the key is the user's DNI and the value is a list of
     *         their payments.
     */
    @Override
    public Map<String, List<PaymentDetails>> getAllUsersWithPayments() {
        // Get all users from the user service
        final var users = userRepository.findAll();
        // Create a map of user DNI to their list of payments
        return users.stream().collect(Collectors.toMap(UserEntity::getDni,
                user -> transformToPaymentDetails(getUserPayments(user.getDni()))));
    }

    /**
     * Return payment not found message.
     *
     * @param paymentId The payment identifier.
     * @return The string with message.
     */
    private String getPaymentNotFoundMessage(final UUID paymentId) {
        return ("Payment with id: " + paymentId + " not found.");
    }

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
    @Override
    public List<PersistentPaymentsEntity> getUserPayments(final String userDni) {
        Objects.requireNonNull(userDni, "User DNI must not be null.");
        return paymentsRepository.findByUserDni(userDni);
    }

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
    @Override
    @Transactional
    public PaymentsEntity makePayment(final UUID paymentId, final LocalDateTime paymentDate)
            throws PaymentsServiceException {
        Objects.requireNonNull(paymentDate, "Payment date must not be null.");
        final Optional<PersistentPaymentsEntity> paymentOptional = paymentsRepository.findById(paymentId);
        if (paymentOptional.isEmpty()) {
            throw new PaymentsServiceException(getPaymentNotFoundMessage(paymentId));
        }
        final var paymentEntity = paymentOptional.get();
        if (paymentEntity.getState().equals(PaymentsState.UNPAID)) {
            paymentEntity.setState(PaymentsState.PAID);
            paymentEntity.setPaidAt(paymentDate);
            final var result = paymentsRepository.save(paymentEntity);
            final var userDni = result.getUserDni();
            final Optional<PersistentUserEntity> userOptional = userRepository.findByDni(userDni);
            if (userOptional.isEmpty()) {
                throw new PaymentsServiceException("No user with dni: " + userDni + " found.");
            }
            final var userEntity = userOptional.get();
            try {
                emailService.sendPaymentConfirmation(userEntity.getEmail(), userEntity.getCompleteName(),
                        paymentEntity.getAmount().toString(), paymentEntity.getDescription());
            } catch (MessagingException e) {
                throw new PaymentsServiceException("Cannot send email.", e);
            } catch (IOException e) {
                throw new PaymentsServiceException("Cannot load email template.", e);
            }
            return result;
        } else {
            throw new PaymentsServiceException(
                    "Payment with id: " + paymentId + " have not " + PaymentsState.UNPAID + " state.");
        }
    }

    /**
     * Removes a payment record by its unique identifier.
     * <p>
     * This method deletes a payment from the system by using its unique identifier
     * (payment ID). The payment record is permanently removed from the database. If
     * no payment with the specified ID is found, no action is taken.
     * </p>
     *
     * @param paymentId the unique identifier of the payment to be removed.
     * @throws PaymentsServiceException if a payment cannot be found.
     */
    @Override
    public void remove(final UUID paymentId) throws PaymentsServiceException {
        final Optional<PersistentPaymentsEntity> paymentOptional = paymentsRepository.findById(paymentId);
        if (paymentOptional.isEmpty()) {
            throw new PaymentsServiceException(getPaymentNotFoundMessage(paymentId));
        }
        paymentsRepository.deleteById(paymentId);
    }

}
