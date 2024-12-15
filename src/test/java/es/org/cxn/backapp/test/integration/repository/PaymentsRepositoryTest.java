package es.org.cxn.backapp.test.integration.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import es.org.cxn.backapp.model.persistence.PersistentPaymentsEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.repository.PaymentsEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import jakarta.transaction.Transactional;

/**
 * Integration tests for the {@link PaymentsEntityRepository}.
 * <p>
 * This class contains tests that verify the correct behavior of the
 * {@link PaymentsEntityRepository} in a Spring Boot application context. Tests
 * are performed within a transactional context to ensure database changes are
 * rolled back after the test execution.
 * </p>
 *
 * <p>
 * Each test verifies the functionality of a specific method in the repository,
 * such as creating, reading, updating, and deleting payment entities.
 * </p>
 *
 * <p>
 * This test class uses JUnit 5 and Spring Boot's testing framework.
 * </p>
 *
 * @author Santi
 */
@DataJpaTest
@ActiveProfiles("test")
class PaymentsRepositoryTest {

    @Autowired
    private PaymentsEntityRepository paymentsEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    private final String userDni = "32721850X";

    /**
     * Test case for creating a payment entity and verifying its saved state.
     * <p>
     * This test verifies that a payment entity can be saved successfully and its
     * attributes (e.g., amount, description, category, created date) match the
     * expected values.
     * </p>
     */
    @Transactional
    @Test
    @DisplayName("Create a Payment Entity")
    void createPaymentEntity() {
        // Arrange
        PersistentPaymentsEntity payment = new PersistentPaymentsEntity();
        final String description = "Test payment description";
        final String category = "Test payment category";
        final LocalDateTime createdDate = LocalDateTime.of(2012, 5, 14, 10, 30);
        final LocalDateTime paidAt = LocalDateTime.of(2012, 5, 19, 11, 20);
        final String paymentState = "Test payment state";
        final String paymentTitle = "Test Payment title";
        final BigDecimal paymentAmount = BigDecimal.valueOf(100.50);
        payment.setAmount(paymentAmount);
        payment.setDescription(description);
        payment.setCategory(category);
        payment.setCreatedAt(createdDate);
        payment.setPaidAt(paidAt);
        payment.setState(paymentState);
        payment.setTitle(paymentTitle);
        payment.setUserDni(userDni);

        // Act
        PersistentPaymentsEntity savedPayment = paymentsEntityRepository.save(payment);

        // Assert
        Assertions.assertNotNull(savedPayment, "The saved payment entity should not be null");
        Assertions.assertNotNull(savedPayment.getId(), "The saved payment entity ID should not be null");
        Assertions.assertEquals(paymentTitle, savedPayment.getTitle(), "The payment title should match");
        Assertions.assertEquals(description, savedPayment.getDescription(), "The payment description should match");
        Assertions.assertEquals(category, savedPayment.getCategory(), "The payment category should match");
        Assertions.assertEquals(paymentState, savedPayment.getState(), "The payment state should match");
        Assertions.assertEquals(paymentAmount, savedPayment.getAmount(), "The payment amount should match");
        Assertions.assertEquals(createdDate, savedPayment.getCreatedAt(), "The created date should match");
        Assertions.assertEquals(paidAt, savedPayment.getPaidAt(), "The paid at date should match");
        Assertions.assertEquals(userDni, savedPayment.getUserDni(), "The user DNI should match");
    }

    /**
     * Test case for deleting a payment entity and verifying that it is no longer
     * present.
     * <p>
     * This test verifies that a payment entity can be deleted successfully, and
     * that querying the entity by its ID returns an empty result.
     * </p>
     */
    @Transactional
    @Test
    @DisplayName("Delete a Payment Entity and check that no one with same id found.")
    void deletePaymentEntity() {
        // Arrange
        PersistentPaymentsEntity payment = new PersistentPaymentsEntity();
        final String description = "Test payment description";
        final String category = "Test payment category";
        final LocalDateTime createdDate = LocalDateTime.of(2012, 5, 14, 10, 30);
        final LocalDateTime paidAt = LocalDateTime.of(2012, 5, 19, 11, 20);
        final String paymentState = "Test payment state";
        final String paymentTitle = "Test Payment title";
        final BigDecimal paymentAmount = BigDecimal.valueOf(100.50);
        payment.setAmount(paymentAmount);
        payment.setDescription(description);
        payment.setCategory(category);
        payment.setCreatedAt(createdDate);
        payment.setPaidAt(paidAt);
        payment.setState(paymentState);
        payment.setTitle(paymentTitle);
        payment.setUserDni(userDni);
        final PersistentPaymentsEntity savedPayment = paymentsEntityRepository.save(payment);
        final UUID id = savedPayment.getId();

        // Act
        paymentsEntityRepository.deleteById(id);
        final var deletedPayment = paymentsEntityRepository.findById(id);

        // Assert
        Assertions.assertTrue(deletedPayment.isEmpty(), "no payment with id found.");
    }

    /**
     * Initializes the test setup by saving a test user to the database. This method
     * is executed before each test case.
     */
    @BeforeEach
    void initialize() {
        final UserProfile userProfile = new UserProfile();
        userProfile.setBirthDate(LocalDate.of(1991, 10, 21));
        userProfile.setFirstSurname("FirstSurname");
        userProfile.setSecondSurname("secondSurname");
        userProfile.setGender("Gender");
        userProfile.setName("UserName");
        final PersistentUserEntity userEntity = PersistentUserEntity.builder().dni(userDni).email("email@email.es")
                .enabled(Boolean.TRUE).kindMember(UserType.SOCIO_NUMERO).password("aaadddd").profile(userProfile)
                .build();
        userEntityRepository.save(userEntity);
    }

    /**
     * Test case for reading a payment entity from the repository.
     * <p>
     * This test verifies that a saved payment entity can be retrieved by its ID,
     * and that the attributes of the retrieved entity match the expected values.
     * </p>
     */
    @Transactional
    @Test
    @DisplayName("Read a Payment Entity")
    void readPaymentEntity() {
        // Arrange
        PersistentPaymentsEntity payment = new PersistentPaymentsEntity();
        final String description = "Test payment description";
        final String category = "Test payment category";
        final LocalDateTime createdDate = LocalDateTime.of(2012, 5, 14, 10, 30);
        final LocalDateTime paidAt = LocalDateTime.of(2012, 5, 19, 11, 20);
        final String paymentState = "Test payment state";
        final String paymentTitle = "Test Payment title";
        final BigDecimal paymentAmount = BigDecimal.valueOf(100.50);
        payment.setAmount(paymentAmount);
        payment.setDescription(description);
        payment.setCategory(category);
        payment.setCreatedAt(createdDate);
        payment.setPaidAt(paidAt);
        payment.setState(paymentState);
        payment.setTitle(paymentTitle);
        payment.setUserDni(userDni);
        final PersistentPaymentsEntity savedPayment = paymentsEntityRepository.save(payment);
        final UUID id = savedPayment.getId();

        // Act
        final var retrievedPayment = paymentsEntityRepository.findById(id);

        // Assert
        Assertions.assertTrue(retrievedPayment.isPresent(), "Retrieved payment should be present");
        PersistentPaymentsEntity paymentEntity = retrievedPayment.get();

        Assertions.assertEquals(id, paymentEntity.getId(), "The payment ID should match");
        Assertions.assertEquals(paymentAmount, paymentEntity.getAmount(), "The payment amount should match");
        Assertions.assertEquals(description, paymentEntity.getDescription(), "The payment description should match");
        Assertions.assertEquals(category, paymentEntity.getCategory(), "The payment category should match");
        Assertions.assertEquals(createdDate, paymentEntity.getCreatedAt(), "The created date should match");
        Assertions.assertEquals(paidAt, paymentEntity.getPaidAt(), "The paid at date should match");
        Assertions.assertEquals(paymentState, paymentEntity.getState(), "The payment state should match");
        Assertions.assertEquals(paymentTitle, paymentEntity.getTitle(), "The payment title should match");
        Assertions.assertEquals(userDni, paymentEntity.getUserDni(), "The user DNI should match");
    }

    /**
     * Test case for updating a payment entity and verifying the updated values.
     * <p>
     * This test verifies that a payment entity can be updated successfully, and
     * that the updated values are correctly persisted.
     * </p>
     */
    @Transactional
    @Test
    @DisplayName("Update a Payment Entity")
    void updatePaymentEntity() {
        // Arrange
        PersistentPaymentsEntity payment = new PersistentPaymentsEntity();
        final String description = "Test payment description";
        final String category = "Test payment category";
        final LocalDateTime createdDate = LocalDateTime.of(2012, 5, 14, 10, 30);
        final LocalDateTime paidAt = LocalDateTime.of(2012, 5, 19, 11, 20);
        final String paymentState = "Test payment state";
        final String paymentTitle = "Test Payment title";
        final BigDecimal paymentAmount = BigDecimal.valueOf(100.50);
        payment.setAmount(paymentAmount);
        payment.setDescription(description);
        payment.setCategory(category);
        payment.setCreatedAt(createdDate);
        payment.setPaidAt(paidAt);
        payment.setState(paymentState);
        payment.setTitle(paymentTitle);
        payment.setUserDni(userDni);

        // Save the initial payment
        final PersistentPaymentsEntity savedPayment = paymentsEntityRepository.save(payment);

        // Updated values for assertions
        final BigDecimal updatedAmount = BigDecimal.valueOf(175.00);
        final String updatedDescription = "Updated Payment";
        final String updatedCategory = "Updated Category";
        final LocalDateTime updatedCreatedAt = LocalDateTime.of(2022, 12, 25, 9, 15);
        final LocalDateTime updatedPaidAt = LocalDateTime.of(2022, 12, 30, 10, 25);
        final String updatedState = "Updated State";
        final String updatedTitle = "Updated Payment Title";

        // Act: Update the values (excluding dni and id)
        savedPayment.setAmount(updatedAmount);
        savedPayment.setDescription(updatedDescription);
        savedPayment.setCategory(updatedCategory);
        savedPayment.setCreatedAt(updatedCreatedAt);
        savedPayment.setPaidAt(updatedPaidAt);
        savedPayment.setState(updatedState);
        savedPayment.setTitle(updatedTitle);

        PersistentPaymentsEntity updatedPayment = paymentsEntityRepository.save(savedPayment);

        // Assert using JUnit assertions
        Assertions.assertNotNull(updatedPayment, "Updated payment should not be null");
        Assertions.assertEquals(updatedAmount, updatedPayment.getAmount(), "The payment amount should be updated");
        Assertions.assertEquals(updatedDescription, updatedPayment.getDescription(),
                "The payment description should be updated");
        Assertions.assertEquals(updatedCategory, updatedPayment.getCategory(),
                "The payment category should be updated");
        Assertions.assertEquals(updatedState, updatedPayment.getState(), "The payment state should be updated");
        Assertions.assertEquals(updatedCreatedAt, updatedPayment.getCreatedAt(),
                "The created at date should be updated");
        Assertions.assertEquals(updatedPaidAt, updatedPayment.getPaidAt(), "The paid at date should be updated");
        Assertions.assertEquals(updatedTitle, updatedPayment.getTitle(), "The payment title should be updated");
    }
}
