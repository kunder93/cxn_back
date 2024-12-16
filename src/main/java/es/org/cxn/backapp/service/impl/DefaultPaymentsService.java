package es.org.cxn.backapp.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.model.PaymentsEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.repository.PaymentsEntityRepository;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;

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
     * Constructs a DefaultPaymentsService with the provided payments repository.
     *
     * @param repository the repository used to persist payment data. Must not be
     *                   null.
     * @throws IllegalArgumentException if the repository is null.
     */
    public DefaultPaymentsService(final PaymentsEntityRepository repository) {
        paymentsRepository = checkNotNull(repository, "payments entity repository cannot be null");
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
            throw new PaymentsServiceException("No payment with id: " + paymentId + " found.");
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
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
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
    public PaymentsEntity makePayment(final UUID paymentId, final LocalDateTime paymentDate)
            throws PaymentsServiceException {
        Objects.requireNonNull(paymentDate, "Payment date must not be null.");

        final Optional<PersistentPaymentsEntity> paymentOptional = paymentsRepository.findById(paymentId);

        if (paymentOptional.isEmpty()) {
            throw new PaymentsServiceException("No payment with id: " + paymentId + " found.");
        }
        final var paymentEntity = paymentOptional.get();

        if (paymentEntity.getState().equals(PaymentsState.UNPAID)) {
            paymentEntity.setState(PaymentsState.PAID);
            paymentEntity.setPaidAt(paymentDate);
            return paymentsRepository.save(paymentEntity);
        } else {
            throw new PaymentsServiceException(
                    "Payment with id: " + paymentId + " have not " + PaymentsState.UNPAID + " state.");
        }

    }

}
