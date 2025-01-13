
package es.org.cxn.backapp.test.integration.services;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.service.impl.DefaultPaymentsService;
import jakarta.transaction.Transactional;

/**
 * Unit tests for {@link DefaultPaymentsService}.
 * <p>
 * This class contains tests to verify the functionality of the
 * {@link DefaultPaymentsService} service. It ensures that various methods of
 * the service behave as expected and handle different scenarios correctly. The
 * </p>
 *
 * @author Santiago Paz
 */
@SpringBootTest()
@ActiveProfiles("test")
final class TestPaymentsService {

    /**
     * The service for managing payments. Used to test the functionality of
     * {@link PaymentsService}.
     */
    @Autowired
    private PaymentsService paymentsService;

    /**
     * The service for managing users. Used to perform operations related to user
     * management and registration.
     */
    @Autowired
    private UserService userService;

    /**
     * Mocked mail sender to avoid sending actual emails during tests. Simulates
     * email sending functionality.
     */
    @MockBean
    private JavaMailSender mailSender;

    /**
     * Mocked email service to test email-related functionality without triggering
     * real email-sending operations.
     */
    @MockBean
    private DefaultEmailService emailService;

    /**
     * Test user dni.
     */
    private final String userDni = "32721880X";

    @BeforeEach
    void setUp() throws UserServiceException {
        // Define variables for address details
        final String apartmentNumber = "12B";
        final String building = "Building A";
        final String city = "CityName";
        final String postalCode = "12345";
        final String street = "Main Street 1";
        final int countryNumericCode = 724;
        final String countrySubdivisionName = "Burgos";

        // Create an AddressRegistrationDetailsDto using the variables
        AddressRegistrationDetailsDto addressDetails = new AddressRegistrationDetailsDto(apartmentNumber, building,
                city, postalCode, street, countryNumericCode, countrySubdivisionName);

        // Define variables for user details
        final String name = "John";
        final String firstSurname = "Doe";
        final String secondSurname = "Smith";
        final LocalDate birthDate = LocalDate.of(1990, 1, 1);
        final String gender = "Male";
        final String password = "password123";
        final String email = "johndoe@example.com";
        final UserType kindMember = UserType.SOCIO_NUMERO;

        // Create a UserRegistrationDetailsDto using the variables
        UserRegistrationDetailsDto createUserDto = new UserRegistrationDetailsDto(userDni, name, firstSurname,
                secondSurname, birthDate, gender, password, email, addressDetails, kindMember);

        // Call the userService to add the user
        userService.add(createUserDto);
    }

    /**
     * Tests the behavior of the cancelPayment method when attempting to cancel a
     * payment that has already been cancelled. Verifies that a
     * PaymentsServiceException is thrown with the expected error message.
     *
     * @throws PaymentsServiceException if there is an error during the initial
     *                                  cancellation of the payment.
     */
    @Test
    @Transactional
    void testCancelPaymentWhenAlreadyCancelledThrowsException() throws PaymentsServiceException {
        // Arrange: Create and cancel a payment
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";
        final var createdPayment = paymentsService.createPayment(paymentAmount, paymentCategory, paymentDescription,
                paymentTitle, userDni);
        paymentsService.cancelPayment(createdPayment.getId()); // Initial cancellation

        // Act and Assert: Attempt to cancel the already cancelled payment and verify
        // the exception is thrown
        final PaymentsServiceException exception = Assertions.assertThrows(PaymentsServiceException.class,
                () -> paymentsService.cancelPayment(createdPayment.getId()),
                "Cancelling a payment that is already cancelled should throw a PaymentsServiceException.");

        // Assert: Verify the exception message
        final String expectedMessage = "Payment with id: " + createdPayment.getId() + " is already "
                + PaymentsState.CANCELLED + ".";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment is already cancelled.");
    }

    /**
     * Tests the behavior of the cancelPayment method when attempting to cancel a
     * non-existing payment. Verifies that a PaymentsServiceException is thrown with
     * the expected error message.
     */
    @Test
    @Transactional
    void testCancelPaymentWhenPaymentDoesNotExistThrowsException() {
        // Arrange: Define a random UUID that does not correspond to any existing
        // payment
        final UUID notExistingPaymentId = UUID.randomUUID();

        // Act and Assert: Attempt to cancel a non-existing payment and verify the
        // exception is thrown
        final PaymentsServiceException exception = Assertions.assertThrows(PaymentsServiceException.class,
                () -> paymentsService.cancelPayment(notExistingPaymentId),
                "Cancelling a non-existing payment should throw a PaymentsServiceException.");

        // Assert: Verify the exception message
        final String expectedMessage = "No payment with id: " + notExistingPaymentId + " found.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate the payment ID was not found.");
    }

    /**
     * Tests the cancellation of a payment that exists. Ensures the payment state
     * transitions to CANCELLED when the cancelPayment method is called.
     */
    @Test
    @Transactional
    void testCancelPaymentWhenPaymentExistsCancelled() throws PaymentsServiceException {
        // Arrange: Define payment details and create a payment
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";
        final var createdPayment = paymentsService.createPayment(paymentAmount, paymentCategory, paymentDescription,
                paymentTitle, userDni);

        // Act: Cancel the payment
        final var cancelledPayment = paymentsService.cancelPayment(createdPayment.getId());

        // Assert: Verify the payment state
        Assertions.assertEquals(PaymentsState.CANCELLED, cancelledPayment.getState(),
                "The payment state should transition to CANCELLED after cancellation.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method for creating a payment with valid details.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * successfully creates a payment with the provided details (amount, category,
     * title, description, and user DNI). It also checks that the created payment
     * has the correct attributes such as amount, category, title, description,
     * state, and associated user DNI.
     * </p>
     *
     * <p>
     * Additionally, the test confirms that the payment is initially marked as
     * {@link PaymentsState#UNPAID} and that the payment ID is generated correctly.
     * </p>
     *
     * @throws PaymentsServiceException if there is an error during payment
     *                                  creation.
     *
     * @see PaymentsService
     * @see PaymentsCategory
     * @see PaymentsState
     */
    @Test
    @Transactional
    void testCreatePayment() throws PaymentsServiceException {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";

        // Act: Create a payment
        final var createdPayment = paymentsService.createPayment(paymentAmount, paymentCategory, paymentDescription,
                paymentTitle, userDni);

        // Assert: Verify payment values
        Assertions.assertEquals(paymentAmount, createdPayment.getAmount(),
                "The payment amount should match the input value.");
        Assertions.assertEquals(paymentCategory, createdPayment.getCategory(),
                "The payment category should match the input value.");
        Assertions.assertEquals(paymentTitle, createdPayment.getTitle(),
                "The payment title should match the input value.");
        Assertions.assertEquals(paymentDescription, createdPayment.getDescription(),
                "The payment description should match the input value.");
        Assertions.assertEquals(PaymentsState.UNPAID, createdPayment.getState(),
                "The initial payment state should be UNPAID.");
        Assertions.assertEquals(userDni, createdPayment.getUserDni(),
                "The payment should be associated with the correct user DNI.");
        Assertions.assertNull(createdPayment.getPaidAt(), "The payment date should be null for an unpaid payment.");
        Assertions.assertNotNull(createdPayment.getId(), "The payment ID should be generated and not null.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the payment amount is null.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link NullPointerException} when the payment amount is null. It
     * ensures that the system enforces the rule that the payment amount cannot be
     * null.
     * </p>
     *
     * @throws NullPointerException if the payment amount is null.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testCreatePaymentWhenAmountIsNullThrowsException() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = null;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";

        // Act and Assert: Attempt to create a payment with a null amount and verify the
        // exception is thrown
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class, () -> paymentsService.createPayment(paymentAmount, paymentCategory,
                        paymentDescription, paymentTitle, userDni),
                "Creating a payment with amount of null is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "Payment amount must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment amount cannot be null.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the payment amount is zero.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link PaymentsServiceException} when the payment amount is zero. It
     * ensures that the system enforces the rule that payments cannot have an amount
     * of zero.
     * </p>
     *
     * @throws PaymentsServiceException if the payment amount is zero.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testCreatePaymentWhenAmountIsZeroThrowsException() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.ZERO;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";

        // Act and Assert: Attempt to create a payment with an amount of zero and verify
        // the exception is thrown
        final PaymentsServiceException exception = Assertions.assertThrows(
                PaymentsServiceException.class, () -> paymentsService.createPayment(paymentAmount, paymentCategory,
                        paymentDescription, paymentTitle, userDni),
                "Creating a payment with amount of zero is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "Payment amount must be greater than zero.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment amount cannot be zero.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the payment description is null.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link NullPointerException} when the payment description is null.
     * It ensures that the system enforces the requirement for a non-null
     * description when creating a payment.
     * </p>
     *
     * @throws NullPointerException if the payment description is null.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testCreatePaymentWhenDescriptionIsNull() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = null;

        // Act and Assert: Attempt to create a payment with a null description and
        // verify the exception is thrown
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class, () -> paymentsService.createPayment(paymentAmount, paymentCategory,
                        paymentDescription, paymentTitle, userDni),
                "Creating a payment with a null description is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "Payment description must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment description cannot be null.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the payment amount exceeds the maximum allowed value.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link PaymentsServiceException} when the payment amount exceeds
     * 100. It ensures that the system enforces a maximum payment amount
     * restriction.
     * </p>
     *
     * @throws PaymentsServiceException if the payment amount exceeds the maximum
     *                                  allowed value of 100.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testCreatePaymentWhenInvalidAmountGreaterThanMax() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.valueOf(101);
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";

        // Act and Assert: Attempt to create a payment with an amount greater than 100
        // and verify the exception is thrown
        final PaymentsServiceException exception = Assertions.assertThrows(
                PaymentsServiceException.class, () -> paymentsService.createPayment(paymentAmount, paymentCategory,
                        paymentDescription, paymentTitle, userDni),
                "Creating a payment amount greater than 100 is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "Amount greater than 100 is not valid.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment amount cannot be greater than 100.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the payment category is null.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link NullPointerException} when the payment category is null. It
     * ensures that the system enforces a non-null payment category for creating
     * payments.
     * </p>
     *
     * @throws NullPointerException if the payment category is null.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testCreatePaymentWhenInvalidCategoryThrowException() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = null;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";

        // Act and Assert: Attempt to create a payment with a null category and verify
        // the exception is thrown
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> paymentsService
                .createPayment(paymentAmount, paymentCategory, paymentDescription, paymentTitle, userDni),
                "Null payment category is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "Payment category must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment category cannot be null.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the payment title is null.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link NullPointerException} when the payment title is null. It
     * ensures that the system enforces a non-null payment title for creating
     * payments.
     * </p>
     *
     * @throws NullPointerException if the payment title is null.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testCreatePaymentWhenTitleIsNull() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = null;
        final String paymentDescription = "payment description";

        // Act and Assert: Attempt to create a payment with a null title and verify the
        // exception is thrown
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> paymentsService
                .createPayment(paymentAmount, paymentCategory, paymentDescription, paymentTitle, userDni),
                "Null payment title is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "Payment title must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment title cannot be null.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the user DNI is null.
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link NullPointerException} when the user DNI is null. It ensures
     * that the system enforces a non-null user DNI for creating payments.
     * </p>
     *
     * @throws NullPointerException if the user DNI is null.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testCreatePaymentWhenUserDniIsNull() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "Payment title";
        final String paymentDescription = "payment description";
        final String nullUserDni = null;

        // Act and Assert: Attempt to create a payment with a null description and
        // verify the exception is thrown
        final NullPointerException exception = Assertions
                .assertThrows(
                        NullPointerException.class, () -> paymentsService.createPayment(paymentAmount, paymentCategory,
                                paymentDescription, paymentTitle, nullUserDni),
                        "Null payment user dni is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "User DNI must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment user dni cannot be null.");
    }

    /**
     * Tests the
     * {@link PaymentsService#createPayment(BigDecimal, PaymentsCategory, String, String, String)}
     * method when the user DNI is empty (null).
     *
     * <p>
     * This test case verifies that the {@link PaymentsService#createPayment} method
     * throws a {@link NullPointerException} when the user DNI is null. It ensures
     * that the system enforces a non-null user DNI for creating payments.
     * </p>
     *
     * @throws NullPointerException if the user DNI is null or empty.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testGetUserPaymentsWhenUserDniIsEmpty() {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "Payment title";
        final String paymentDescription = "payment description";
        final String emptyUserDni = null;

        // Act and Assert: Attempt to create a payment with a null description and
        // verify the exception is thrown
        final NullPointerException exception = Assertions
                .assertThrows(
                        NullPointerException.class, () -> paymentsService.createPayment(paymentAmount, paymentCategory,
                                paymentDescription, paymentTitle, emptyUserDni),
                        "Empty payment user dni is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "User DNI must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment user dni cannot be empty.");
    }

    /**
     * Tests the {@link PaymentsService#getUserPayments(String)} method when the
     * user DNI is null.
     *
     * <p>
     * This test case verifies that the
     * {@link PaymentsService#getUserPayments(String)} method throws a
     * {@link NullPointerException} when the user DNI is null. It ensures that the
     * system enforces a non-null user DNI for retrieving user payments.
     * </p>
     *
     * @throws NullPointerException if the user DNI is null.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testGetUserPaymentsWhenUserDniIsNull() {
        final String nullDni = null;

        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> paymentsService.getUserPayments(nullDni), "Empty payment user dni is not allowed.");

        // Assert: Verify the exception message
        final String expectedMessage = "User DNI must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message should indicate that the payment user dni cannot be empty.");
    }

    /**
     * Tests the {@link PaymentsService#getUserPayments(String)} method when the
     * user does not exist.
     *
     * <p>
     * This test case verifies that the
     * {@link PaymentsService#getUserPayments(String)} method returns an empty list
     * when attempting to retrieve payments for a user who does not exist in the
     * system.
     * </p>
     *
     * @throws PaymentsServiceException if an error occurs while retrieving
     *                                  payments.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testGetUserPaymentsWhenUserDoesNotExist() {
        final String notExistingUserDni = "43343234D";
        List<PersistentPaymentsEntity> paymentsList = paymentsService.getUserPayments(notExistingUserDni);

        // Assert: Verify list of payments is empty.
        Assertions.assertEquals(0, paymentsList.size());
    }

    /**
     * Tests the {@link PaymentsService#getUserPayments(String)} method when the
     * user has multiple payments.
     *
     * <p>
     * This test case verifies that the
     * {@link PaymentsService#getUserPayments(String)} method correctly retrieves a
     * list of payments associated with the specified user. The test checks that the
     * list is not empty, the size of the list matches the number of payments
     * created, and the details of the payments in the list match the expected
     * values.
     * </p>
     *
     * @throws PaymentsServiceException if an error occurs while creating or
     *                                  retrieving payments.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testGetUserPaymentsWhenUserHasPayments() throws PaymentsServiceException {
        final int expectedPaymentsSize = 3;
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";

        final BigDecimal secondPaymentAmount = BigDecimal.TEN;
        final PaymentsCategory secondPaymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String secondPaymentTitle = "paymentTitle";
        final String secondPaymentDescription = "payment description";

        final BigDecimal thirdPaymentAmount = BigDecimal.TEN;
        final PaymentsCategory thirdPaymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String thirdPaymentTitle = "paymentTitle";
        final String thirdPaymentDescription = "payment description";

        final var firstPayment = paymentsService.createPayment(paymentAmount, paymentCategory, paymentDescription,
                paymentTitle, userDni);
        final var secondPayment = paymentsService.createPayment(secondPaymentAmount, secondPaymentCategory,
                secondPaymentDescription, secondPaymentTitle, userDni);
        final var thirdPayment = paymentsService.createPayment(thirdPaymentAmount, thirdPaymentCategory,
                thirdPaymentDescription, thirdPaymentTitle, userDni);

        // Act: Retrieve payments list from user.
        List<PersistentPaymentsEntity> paymentsList = paymentsService.getUserPayments(userDni);

        // Assert: Verify that the list is not empty
        Assertions.assertFalse(paymentsList.isEmpty(), "Payments list should not be empty for the user.");

        // Assert: Verify the list size matches the number of payments created
        Assertions.assertEquals(expectedPaymentsSize, paymentsList.size(),
                "The number of payments retrieved does not match the expected.");

        // Assert: Verify that the payments in the list match the created payments
        PersistentPaymentsEntity retrievedFirstPayment = paymentsList.getFirst();
        PersistentPaymentsEntity retrievedSecondPayment = paymentsList.get(1);
        PersistentPaymentsEntity retrievedThirdPayment = paymentsList.get(2);

        Assertions.assertEquals(firstPayment.getId(), retrievedFirstPayment.getId(),
                "The first payment in the list does not match the expected first payment.");
        Assertions.assertEquals(secondPayment.getId(), retrievedSecondPayment.getId(),
                "The second payment in the list does not match the expected second payment.");
        Assertions.assertEquals(thirdPayment.getId(), retrievedThirdPayment.getId(),
                "The third payment in the list does not match the expected third payment.");

        // Assert: Verify the payment details match the expected values (amount,
        // category, title, description)
        Assertions.assertEquals(paymentAmount, retrievedFirstPayment.getAmount(),
                "The amount for the first payment is incorrect.");
        Assertions.assertEquals(secondPaymentAmount, retrievedSecondPayment.getAmount(),
                "The amount for the second payment is incorrect.");
        Assertions.assertEquals(thirdPaymentAmount, retrievedThirdPayment.getAmount(),
                "The amount for the third payment is incorrect.");

        Assertions.assertEquals(paymentCategory, retrievedFirstPayment.getCategory(),
                "The category for the first payment is incorrect.");
        Assertions.assertEquals(secondPaymentCategory, retrievedSecondPayment.getCategory(),
                "The category for the second payment is incorrect.");
        Assertions.assertEquals(thirdPaymentCategory, retrievedThirdPayment.getCategory(),
                "The category for the third payment is incorrect.");

        Assertions.assertEquals(paymentTitle, retrievedFirstPayment.getTitle(),
                "The title for the first payment is incorrect.");
        Assertions.assertEquals(secondPaymentTitle, retrievedSecondPayment.getTitle(),
                "The title for the second payment is incorrect.");
        Assertions.assertEquals(thirdPaymentTitle, retrievedThirdPayment.getTitle(),
                "The title for the third payment is incorrect.");

        Assertions.assertEquals(paymentDescription, retrievedFirstPayment.getDescription(),
                "The description for the first payment is incorrect.");
        Assertions.assertEquals(secondPaymentDescription, retrievedSecondPayment.getDescription(),
                "The description for the second payment is incorrect.");
        Assertions.assertEquals(thirdPaymentDescription, retrievedThirdPayment.getDescription(),
                "The description for the third payment is incorrect.");
    }

    /**
     * Tests the {@link PaymentsService#makePayment(UUID, LocalDateTime)} method
     * when attempting to mark an already paid payment as paid again.
     *
     * <p>
     * This test case verifies that a {@link PaymentsServiceException} is thrown
     * when attempting to mark a payment as paid after it has already been marked as
     * paid. The exception message should indicate that the payment is not in the
     * "UNPAID" state.
     * </p>
     *
     * @throws PaymentsServiceException if an error occurs while creating or
     *                                  processing the payment.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testMakePaymentWhenAlreadyPaid() throws PaymentsServiceException {
        // Arrange: Define variables for payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";
        final LocalDateTime firstPaymentTime = LocalDateTime.of(2024, 12, 10, 10, 20);
        final LocalDateTime secondPaymentTime = LocalDateTime.of(2024, 12, 10, 12, 30);

        // Act: Create a payment
        final var createdPayment = paymentsService.createPayment(paymentAmount, paymentCategory, paymentDescription,
                paymentTitle, userDni);

        // Act: Make the first payment
        paymentsService.makePayment(createdPayment.getId(), firstPaymentTime);

        // Act & Assert: Attempt to mark the same payment as paid again and expect an
        // exception
        final PaymentsServiceException exception = Assertions.assertThrows(PaymentsServiceException.class,
                () -> paymentsService.makePayment(createdPayment.getId(), secondPaymentTime),
                "Expected exception not thrown when trying to mark an already paid payment.");

        // Assert: Verify the exception message is correct
        final String expectedMessage = "Payment with id: " + createdPayment.getId() + " have not UNPAID state.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message does not match the expected one.");
    }

    /**
     * Tests the {@link PaymentsService#makePayment(UUID, LocalDateTime)} method
     * when the payment date is null.
     *
     * <p>
     * This test case verifies that a {@link NullPointerException} is thrown when
     * attempting to make a payment with a null payment date. The exception message
     * should indicate that the payment date must not be null.
     * </p>
     *
     * @throws PaymentsServiceException if an exception occurs during the payment
     *                                  creation process (e.g., from the
     *                                  {@link PaymentsService#createPayment}
     *                                  method).
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testMakePaymentWhenPaymentDateIsNull() throws PaymentsServiceException {
        // Arrange: Define payment details
        final BigDecimal paymentAmount = BigDecimal.TEN;
        final PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        final String paymentTitle = "paymentTitle";
        final String paymentDescription = "payment description";

        // Act: Create a payment
        final var createdPayment = paymentsService.createPayment(paymentAmount, paymentCategory, paymentDescription,
                paymentTitle, userDni);
        final LocalDateTime paymentDate = null;

        // Act: Attempt to mark the same payment as paid again and expect an exception
        // The invocation that might throw an exception is now extracted into a separate
        // statement
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> paymentsService.makePayment(createdPayment.getId(), paymentDate));

        // Assert: Verify the exception message is correct
        final String expectedMessage = "Payment date must not be null.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message for null payment date.");
    }

    /**
     * Tests the {@link PaymentsService#makePayment(UUID, LocalDateTime)} method
     * when attempting to make a payment with a non-existing payment ID.
     *
     * <p>
     * This test case verifies that a {@link PaymentsServiceException} is thrown
     * when attempting to make a payment with an ID that does not exist in the
     * system. The exception message should indicate that no payment with the
     * provided ID was found.
     * </p>
     *
     * @throws PaymentsServiceException if an exception occurs during the payment
     *                                  process.
     *
     * @see PaymentsService
     */
    @Test
    @Transactional
    void testMakePaymentWhenPaymentNotFound() {
        // Arrange: Define a non-existing payment ID
        final UUID notExistingPaymentId = UUID.randomUUID();
        final var paymentDate = LocalDateTime.of(2024, 12, 10, 12, 30);

        // Act & Assert: Attempt to make a payment with a non-existing payment ID and
        // expect an exception
        final PaymentsServiceException exception = Assertions.assertThrows(PaymentsServiceException.class,
                () -> paymentsService.makePayment(notExistingPaymentId, paymentDate),
                "Expected exception thrown when trying to make payment with non-existing payment ID.");

        // Assert: Verify the exception message is correct
        final String expectedMessage = "No payment with id: " + notExistingPaymentId + " found.";
        Assertions.assertEquals(expectedMessage, exception.getMessage(),
                "The exception message for non-existing payment ID.");
    }

}
