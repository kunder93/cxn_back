package es.org.cxn.backapp.test.unit.services;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.org.cxn.backapp.model.PaymentsEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.repository.PaymentsEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.dto.PaymentDetails;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import es.org.cxn.backapp.service.impl.DefaultPaymentsService;
import jakarta.mail.MessagingException;

class PaymentsServiceTest {

    @Mock
    private PaymentsEntityRepository paymentsRepository;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private DefaultPaymentsService defaultPaymentsService;

    @Test
    void cancelPayment_paymentAlreadyCancelled_throwsException() {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.CANCELLED);

        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.cancelPayment(paymentId);
        });
    }

    @Test
    void cancelPayment_paymentDoesNotExist_throwsException() {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.cancelPayment(paymentId);
        });
    }

    @Test
    void cancelPayment_paymentExists_updatesStateToCancelled() throws PaymentsServiceException {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.UNPAID);

        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));
        when(paymentsRepository.save(any(PersistentPaymentsEntity.class))).thenReturn(paymentEntity);

        // Act
        PaymentsEntity updatedPayment = defaultPaymentsService.cancelPayment(paymentId);

        // Assert
        assertEquals(PaymentsState.CANCELLED, updatedPayment.getState());
        verify(paymentsRepository, times(1)).save(paymentEntity);
    }

    @Test
    void createPayment_amountGreaterThan100_throwsException() {
        // Arrange
        BigDecimal amount = new BigDecimal("150"); // Amount greater than 100
        PaymentsCategory category = PaymentsCategory.MEMBERSHIP_PAYMENT;
        String description = "Payment for membership";
        String title = "Membership Fee";
        String userDni = "123456789";

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.createPayment(amount, category, description, title, userDni);
        });
    }

    @Test
    void createPayment_invalidAmount_throwsException() {
        // Arrange
        BigDecimal amount = new BigDecimal("-50"); // Invalid amount
        PaymentsCategory category = PaymentsCategory.MEMBERSHIP_PAYMENT;
        String description = "Payment for membership";
        String title = "Membership Fee";
        String userDni = "123456789";

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.createPayment(amount, category, description, title, userDni);
        });
    }

    @Test
    void createPayment_validInput_createsPayment() throws PaymentsServiceException {
        // Arrange
        BigDecimal amount = new BigDecimal("50");
        PaymentsCategory category = PaymentsCategory.MEMBERSHIP_PAYMENT;
        String description = "Payment for membership";
        String title = "Membership Fee";
        String userDni = "123456789";

        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(UUID.randomUUID());
        paymentEntity.setAmount(amount);
        paymentEntity.setCategory(category);
        paymentEntity.setDescription(description);
        paymentEntity.setTitle(title);
        paymentEntity.setUserDni(userDni);
        paymentEntity.setCreatedAt(LocalDateTime.now());
        paymentEntity.setState(PaymentsState.UNPAID);

        // Mock repository behavior
        when(paymentsRepository.save(any(PersistentPaymentsEntity.class))).thenReturn(paymentEntity);

        // Act
        PaymentsEntity createdPayment = defaultPaymentsService.createPayment(amount, category, description, title,
                userDni);

        // Assert
        assertNotNull(createdPayment);
        assertEquals(amount, createdPayment.getAmount());
        assertEquals(PaymentsState.UNPAID, createdPayment.getState());
        verify(paymentsRepository, times(1)).save(any(PersistentPaymentsEntity.class));
    }

    @Test
    void findPayment_paymentDoesNotExist_throwsPaymentsServiceException() {
        // Arrange
        UUID paymentId = UUID.randomUUID();

        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        PaymentsServiceException exception = assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.findPayment(paymentId);
        });

        assertEquals("Payment with id: " + paymentId + " not found.", exception.getMessage()); // Ensure the exception
                                                                                               // message is correct
        verify(paymentsRepository, times(1)).findById(paymentId);
    }

    @Test
    void findPayment_paymentExists_returnsPaymentEntity() throws PaymentsServiceException {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.PAID); // Example state
        paymentEntity.setPaidAt(LocalDateTime.now()); // Example paid date

        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

        // Act
        PaymentsEntity foundPayment = defaultPaymentsService.findPayment(paymentId);

        // Assert
        assertNotNull(foundPayment);
        assertEquals(paymentId, foundPayment.getId());
        assertEquals(PaymentsState.PAID, foundPayment.getState());
        verify(paymentsRepository, times(1)).findById(paymentId);
    }

    @Test
    void getAllUsersWithPayments_usersExist_returnsMapWithPayments() {
        // Arrange
        PersistentUserEntity user1 = new PersistentUserEntity();
        user1.setDni("12345");
        PersistentUserEntity user2 = new PersistentUserEntity();
        user2.setDni("67890");

        PersistentPaymentsEntity payment1 = new PersistentPaymentsEntity();
        payment1.setUserDni("12345");
        payment1.setAmount(BigDecimal.valueOf(100.0));

        PersistentPaymentsEntity payment2 = new PersistentPaymentsEntity();
        payment2.setUserDni("67890");
        payment2.setAmount(BigDecimal.valueOf(200.0));

        // Mock the repositories
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(paymentsRepository.findByUserDni("12345")).thenReturn(List.of(payment1));
        when(paymentsRepository.findByUserDni("67890")).thenReturn(List.of(payment2));

        // Act
        Map<String, List<PaymentDetails>> result = defaultPaymentsService.getAllUsersWithPayments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("12345"));
        assertTrue(result.containsKey("67890"));
        assertEquals(1, result.get("12345").size());
        assertEquals(1, result.get("67890").size());
        assertEquals(BigDecimal.valueOf(100.0), result.get("12345").get(0).amount());
        assertEquals(BigDecimal.valueOf(200.0), result.get("67890").get(0).amount());

        verify(userRepository, times(1)).findAll();
        verify(paymentsRepository, times(1)).findByUserDni("12345");
        verify(paymentsRepository, times(1)).findByUserDni("67890");
    }

    @Test
    void getAllUsersWithPayments_usersExistNoPayments_returnsMapWithEmptyLists() {
        // Arrange
        PersistentUserEntity user1 = new PersistentUserEntity();
        user1.setDni("12345");
        PersistentUserEntity user2 = new PersistentUserEntity();
        user2.setDni("67890");

        // Mock the repositories
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(paymentsRepository.findByUserDni("12345")).thenReturn(Collections.emptyList());
        when(paymentsRepository.findByUserDni("67890")).thenReturn(Collections.emptyList());

        // Act
        Map<String, List<PaymentDetails>> result = defaultPaymentsService.getAllUsersWithPayments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("12345"));
        assertTrue(result.containsKey("67890"));
        assertTrue(result.get("12345").isEmpty());
        assertTrue(result.get("67890").isEmpty());

        verify(userRepository, times(1)).findAll();
        verify(paymentsRepository, times(1)).findByUserDni("12345");
        verify(paymentsRepository, times(1)).findByUserDni("67890");
    }

    @Test
    void makePayment_ioException_throwsPaymentsServiceException()
            throws PaymentsServiceException, MessagingException, IOException {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        LocalDateTime paymentDate = LocalDateTime.now();

        // Create a payment entity in UNPAID state
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.UNPAID);
        paymentEntity.setPaidAt(null);
        paymentEntity.setAmount(BigDecimal.TEN);
        paymentEntity.setDescription("test payment description");
        paymentEntity.setUserDni("user-dni");

        // Mock the payments repository to return the payment entity
        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

        // Mock the user repository to return a valid user
        PersistentUserEntity userEntity = new PersistentUserEntity();
        userEntity.setEmail("user@example.com");
        UserProfile userProfile = new UserProfile();
        userProfile.setName("testName");
        userProfile.setFirstSurname("testFirstSurname");
        userProfile.setSecondSurname("testSecondSurname");
        userEntity.setProfile(userProfile);
        userEntity.setEmail("test@email.test");
        when(userRepository.findByDni("user-dni")).thenReturn(Optional.of(userEntity));
        when(paymentsRepository.save(paymentEntity)).thenReturn(paymentEntity);
        // Mock email service to throw IOException
        doThrow(IOException.class).when(emailService).sendPaymentConfirmation(anyString(), anyString(), anyString(),
                anyString());

        // Act & Assert
        PaymentsServiceException exception = assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.makePayment(paymentId, paymentDate);
        });

        // Assert that the exception message contains the expected message
        assertTrue(exception.getMessage().contains("Cannot load email template."));
        verify(paymentsRepository, times(1)).findById(paymentId);
        verify(userRepository, times(1)).findByDni("user-dni");
        verify(emailService, times(1)).sendPaymentConfirmation(anyString(), anyString(), anyString(), anyString());
        verify(paymentsRepository, times(1)).save(any(PersistentPaymentsEntity.class)); // Ensure save wasn't called
    }

    @Test
    void makePayment_messagingException_throwsPaymentsServiceException() throws MessagingException, IOException {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        LocalDateTime paymentDate = LocalDateTime.now();

        // Create a payment entity in UNPAID state
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.UNPAID);
        paymentEntity.setPaidAt(null);
        paymentEntity.setAmount(BigDecimal.TEN);
        paymentEntity.setDescription("test payment description");
        paymentEntity.setUserDni("user-dni");

        // Mock the payments repository to return the payment entity
        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

        // Mock the user repository to return a valid user
        PersistentUserEntity userEntity = new PersistentUserEntity();
        userEntity.setEmail("user@example.com");
        UserProfile userProfile = new UserProfile();
        userProfile.setName("testName");
        userProfile.setFirstSurname("testFirstSurname");
        userProfile.setSecondSurname("testSecondSurname");
        userEntity.setProfile(userProfile);
        userEntity.setEmail("test@email.test");
        when(userRepository.findByDni("user-dni")).thenReturn(Optional.of(userEntity));
        when(paymentsRepository.save(paymentEntity)).thenReturn(paymentEntity);
        // Mock email service to throw MessagingException
        doThrow(MessagingException.class).when(emailService).sendPaymentConfirmation(anyString(), anyString(),
                anyString(), anyString());

        // Act & Assert
        PaymentsServiceException exception = assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.makePayment(paymentId, paymentDate);
        });

        // Assert that the exception message contains the expected message
        assertTrue(exception.getMessage().contains("Cannot send email."));
        verify(paymentsRepository, times(1)).findById(paymentId);
        verify(userRepository, times(1)).findByDni("user-dni");
        verify(emailService, times(1)).sendPaymentConfirmation(anyString(), anyString(), anyString(), anyString());
        verify(paymentsRepository, times(1)).save(any(PersistentPaymentsEntity.class)); // Ensure save wasn't called
    }

    @Test
    void makePayment_noUserFoundForDni_throwsPaymentsServiceException() throws PaymentsServiceException {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        LocalDateTime paymentDate = LocalDateTime.now();

        // Create a payment entity that is in UNPAID state
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.UNPAID);
        paymentEntity.setPaidAt(null);
        paymentEntity.setUserDni("non-existing-dni");

        // Mock the payments repository to return the payment entity
        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));
        when(paymentsRepository.save(paymentEntity)).thenReturn(paymentEntity);
        // Mock the user repository to return an empty Optional for the DNI
        when(userRepository.findByDni("non-existing-dni")).thenReturn(Optional.empty());

        // Act & Assert
        PaymentsServiceException exception = assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.makePayment(paymentId, paymentDate);
        });

        // Assert that the exception message contains the expected message
        assertTrue(exception.getMessage().contains("No user with dni: non-existing-dni found."));
        verify(paymentsRepository, times(1)).findById(paymentId);
        verify(userRepository, times(1)).findByDni("non-existing-dni");
        verify(paymentsRepository, times(1)).save(any(PersistentPaymentsEntity.class)); // Ensure called only 1 time.
    }

    @Test
    void makePayment_paymentAlreadyPaid_throwsException() {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        LocalDateTime paymentDate = LocalDateTime.now();
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.PAID);

        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.makePayment(paymentId, paymentDate);
        });
    }

    @Test
    void makePayment_paymentExists_updatesStateToPaid()
            throws PaymentsServiceException, MessagingException, IOException {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        LocalDateTime paymentDate = LocalDateTime.now();
        String userDni = "123456789"; // Mock DNI value
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);
        paymentEntity.setState(PaymentsState.UNPAID);
        paymentEntity.setPaidAt(null);
        paymentEntity.setAmount(BigDecimal.TEN);
        paymentEntity.setUserDni(userDni); // Add DNI to payment entity
        paymentEntity.setDescription("Reason description");
        // Mock user entity that should be found using the dni
        PersistentUserEntity mockUser = new PersistentUserEntity();
        mockUser.setDni(userDni);
        UserProfile mockUsrProfile = new UserProfile();
        mockUsrProfile.setFirstSurname("tesFirstSurname");
        mockUsrProfile.setSecondSurname("testSecondSurname");
        mockUsrProfile.setName("testName");
        mockUser.setProfile(mockUsrProfile);
        mockUser.setEmail("user@example.com"); // Set other required fields

        // Mock repository behaviors
        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));
        when(paymentsRepository.save(any(PersistentPaymentsEntity.class))).thenReturn(paymentEntity);
        when(userRepository.findByDni(userDni)).thenReturn(Optional.of(mockUser)); // Mock user retrieval

        // Mock email service
        doNothing().when(emailService).sendPaymentConfirmation(anyString(), anyString(), anyString(), anyString());

        // Act
        PaymentsEntity paidPayment = defaultPaymentsService.makePayment(paymentId, paymentDate);

        // Assert
        assertEquals(PaymentsState.PAID, paidPayment.getState());
        assertNotNull(paidPayment.getPaidAt());
        verify(paymentsRepository, times(1)).save(paymentEntity);
        verify(emailService, times(1)).sendPaymentConfirmation(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void makePayment_paymentNotFound_throwsException() {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        LocalDateTime paymentDate = LocalDateTime.now();

        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            defaultPaymentsService.makePayment(paymentId, paymentDate);
        });
    }

    @Test
    void remove_paymentDoesNotExist_throwsPaymentsServiceException() {
        // Arrange
        UUID paymentId = UUID.randomUUID();

        // Mock the repository to return an empty Optional
        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> defaultPaymentsService.remove(paymentId));

        // Verify that deleteById was not called since the payment does not exist
        verify(paymentsRepository, times(0)).deleteById(paymentId);
    }

    @Test
    void remove_paymentExists_paymentIsRemoved() throws PaymentsServiceException {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        paymentEntity.setId(paymentId);

        // Mock the repository
        when(paymentsRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

        // Act
        defaultPaymentsService.remove(paymentId);

        // Assert
        verify(paymentsRepository, times(1)).deleteById(paymentId);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
