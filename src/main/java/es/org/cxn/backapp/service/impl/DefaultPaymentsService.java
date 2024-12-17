package es.org.cxn.backapp.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

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
import es.org.cxn.backapp.repository.PaymentsEntityRepository;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.UserService;
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
     * Default implementation of PaymentDetails.
     */
    private static final class DefaultPaymentDetails implements PaymentDetails {
        private final BigDecimal amount;
        private final PaymentsCategory category;
        private final PaymentsState state;

        public DefaultPaymentDetails(BigDecimal amount, PaymentsCategory category, PaymentsState state) {
            this.amount = amount;
            this.category = category;
            this.state = state;
        }

        @Override
        public BigDecimal getAmount() {
            return amount;
        }

        @Override
        public PaymentsCategory getCategory() {
            return category;
        }

        @Override
        public PaymentsState getState() {
            return state;
        }

    }

    public interface PaymentDetails {
        BigDecimal getAmount();

        PaymentsCategory getCategory();

        PaymentsState getState();

    }

    /**
     * The repository used for saving and retrieving payment entities.
     */
    private final PaymentsEntityRepository paymentsRepository;

    /**
     * The service used to interact with user-related functionality.
     * <p>
     * This service provides access to user information and business logic, such as
     * retrieving user details, validating user data, and other user management
     * operations. It is used within this service to associate payments with users
     * or to retrieve user-related data needed for payment processing.
     * </p>
     *
     * @see UserService
     */
    private final UserService userService;

    /**
     * Constructs a DefaultPaymentsService with the provided payments repository.
     *
     * @param repository the repository used to persist payment data. Must not be
     *                   null.
     * @param usrServ    the service used to interact with users data. Must not be
     *                   null.
     * @throws IllegalArgumentException if the repository is null.
     */
    public DefaultPaymentsService(final PaymentsEntityRepository repository, final UserService usrServ) {
        paymentsRepository = checkNotNull(repository, "Payments entity repository cannot be null.");
        userService = checkNotNull(usrServ, "User service cannot be null.");
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
            throw new PaymentsServiceException("No payment with id: " + paymentId + " found.");
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
        final var users = userService.getAll();

        // Create a map of user DNI to their list of payments
        return users.stream().collect(
                Collectors.toMap(UserEntity::getDni, user -> transformToPaymentDetails(getUserPayments(user.getDni())) // Adapt
                                                                                                                       // the
                                                                                                                       // payments
                ));
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

    // Helper method to transform PersistentPaymentsEntity to PaymentDetails
    private List<PaymentDetails> transformToPaymentDetails(List<PersistentPaymentsEntity> payments) {
        return payments.stream().map(
                payment -> new DefaultPaymentDetails(payment.getAmount(), payment.getCategory(), payment.getState()))
                .collect(Collectors.toList());
    }

}
