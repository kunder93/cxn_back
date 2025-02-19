
package es.org.cxn.backapp.test.unit.services;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultFederateStateService;
import es.org.cxn.backapp.service.impl.storage.DefaultImageStorageService;
import es.org.cxn.backapp.service.impl.storage.FileLocation;

/**
 * Unit tests for the {@link DefaultFederateStateService} class.
 *
 * This test class verifies the behavior of the
 * {@link DefaultFederateStateService} methods
 *
 * @author Santiago Paz.
 */
@ExtendWith(MockitoExtension.class)
class FederateStateServiceTest {
    /**
     * Constant representing the email address of the user. This value is used to
     * identify the user's email in the system.
     */
    private static final String USER_EMAIL = "test@example.com";

    /**
     * Constant representing the DNI (National Identity Number) of the user. This
     * value is used to uniquely identify the user in the system.
     */
    private static final String USER_DNI = "12345678A";

    /**
     * Constant representing the base directory path for storing images. This is
     * used as the root directory where images are stored in the system.
     */
    private static final String IMAGE_BASE_DIRECTORY = "base-directory";

    /**
     * Constant representing the payment amount. This value is used for the cost of
     * a specific payment, such as a user federation fee.
     */
    private static final BigDecimal PAYMENT_AMOUNT = BigDecimal.valueOf(15.00);

    /**
     * Constant representing the payment description. This description is used to
     * explain the purpose of a payment, such as federation fees.
     */
    private static final String PAYMENT_DESCRIPTION = "Coste ficha federativa.";

    /**
     * Constant representing the payment title. This value is used as the title of a
     * payment, for example, the payment for federating a user.
     */
    private static final String PAYMENT_TITLE = "Pago federar usuario";

    /**
     * Mock repository for {@link FederateStateEntityRepository}. This mock is used
     * to simulate interactions with the federate state repository, allowing the
     * test to verify service logic without interacting with the actual database.
     */
    @Mock
    private FederateStateEntityRepository federateStateRepository;

    /**
     * Mock service for {@link UserService}. This mock is used to simulate
     * interactions with the user service, enabling the test to focus on the
     * federate state service without invoking actual user service logic.
     */
    @Mock
    private UserService userService;

    /**
     * The mocked payment service.
     */
    @Mock
    private PaymentsService paymentsService;

    /**
     * The mocked federateState entity.
     */
    @Mock
    private PersistentFederateStateEntity federateStateEntity;

    /**
     * The mocked image storage service.
     */
    @Mock
    private DefaultImageStorageService imageStorageService;

    /**
     * Mock MultipartFile representing the front side of a DNI (Documento Nacional
     * de Identidad) image. This mock is used to simulate uploading of a DNI front
     * image during testing.
     */
    @Mock
    private MultipartFile frontDniFile;

    /**
     * Mock MultipartFile representing the back side of a DNI image. This mock is
     * used to simulate uploading of a DNI back image during testing.
     */
    @Mock
    private MultipartFile backDniFile;

    /**
     * Instance of the {@link DefaultFederateStateService} class, which is the
     * service under test. The service is injected with the mocked dependencies to
     * test its behavior.
     */
    @InjectMocks
    private DefaultFederateStateService federateStateService;

    @Test
    void federateMemberShouldSaveImagesAndUpdateStateWhenStateIsFederate() throws Exception {
        // Arrange
        PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail(USER_EMAIL);
        user.setDni(USER_DNI);

        // Mock federate state as FEDERATE
        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setState(FederateState.FEDERATE);

        // Mock return values for dependencies
        when(userService.findByEmail(USER_EMAIL)).thenReturn(user);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateState));
        when(imageStorageService.saveImage(frontDniFile, FileLocation.DNI, USER_DNI)).thenReturn("front-image-url");
        when(imageStorageService.saveImage(backDniFile, FileLocation.DNI, USER_DNI)).thenReturn("back-image-url");

        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        when(paymentsService.createPayment(PAYMENT_AMOUNT, PaymentsCategory.FEDERATE_PAYMENT, PAYMENT_DESCRIPTION,
                PAYMENT_TITLE, USER_DNI)).thenReturn(paymentEntity);

        PersistentFederateStateEntity updatedEntity = new PersistentFederateStateEntity();
        updatedEntity.setUserDni(USER_DNI);
        updatedEntity.setState(FederateState.IN_PROGRESS);
        when(federateStateRepository.save(any(PersistentFederateStateEntity.class))).thenReturn(updatedEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.federateMember("test@example.com", frontDniFile,
                backDniFile, true);

        // Assert
        assertNotNull(result);

        verify(imageStorageService, times(1)).saveImage(frontDniFile, FileLocation.DNI, USER_DNI);
        verify(imageStorageService, times(1)).saveImage(backDniFile, FileLocation.DNI, USER_DNI);
        verify(paymentsService, times(1)).createPayment(PAYMENT_AMOUNT, PaymentsCategory.FEDERATE_PAYMENT,
                PAYMENT_DESCRIPTION, PAYMENT_TITLE, USER_DNI);
        verify(federateStateRepository, times(1)).save(any(PersistentFederateStateEntity.class));
    }

    @Test
    void federateMemberShouldSaveImagesAndUpdateStateWhenStateIsNoFederate() throws Exception {
        // Arrange
        // Mock user and its details
        PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail(USER_EMAIL);
        user.setDni(USER_DNI);

        // Mock existing federate state
        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setState(FederateState.NO_FEDERATE);

        // Mock return values for dependencies
        when(userService.findByEmail(USER_EMAIL)).thenReturn(user);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateState));
        when(imageStorageService.saveImage(frontDniFile, FileLocation.DNI, USER_DNI)).thenReturn("front-image-url");
        when(imageStorageService.saveImage(backDniFile, FileLocation.DNI, USER_DNI)).thenReturn("back-image-url");

        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        when(paymentsService.createPayment(PAYMENT_AMOUNT, PaymentsCategory.FEDERATE_PAYMENT, PAYMENT_DESCRIPTION,
                PAYMENT_TITLE, USER_DNI)).thenReturn(paymentEntity);

        PersistentFederateStateEntity updatedEntity = new PersistentFederateStateEntity();
        updatedEntity.setUserDni(USER_DNI);
        updatedEntity.setState(FederateState.IN_PROGRESS);
        when(federateStateRepository.save(any(PersistentFederateStateEntity.class))).thenReturn(updatedEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.federateMember(USER_EMAIL, frontDniFile,
                backDniFile, true);

        // Assert
        assertNotNull(result);
        assertEquals(FederateState.IN_PROGRESS, result.getState());

        verify(imageStorageService, times(1)).saveImage(frontDniFile, FileLocation.DNI, USER_DNI);
        verify(imageStorageService, times(1)).saveImage(backDniFile, FileLocation.DNI, USER_DNI);
        verify(paymentsService, times(1)).createPayment(PAYMENT_AMOUNT, PaymentsCategory.FEDERATE_PAYMENT,
                PAYMENT_DESCRIPTION, PAYMENT_TITLE, USER_DNI);
        verify(federateStateRepository, times(1)).save(any(PersistentFederateStateEntity.class));
    }

    @Test
    void federateMemberShouldThrowExceptionWhenImageSavingFails() throws Exception {
        // Arrange

        final PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail(USER_EMAIL);
        user.setDni(USER_DNI);

        federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.NO_FEDERATE);

        when(userService.findByEmail(USER_EMAIL)).thenReturn(user);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateStateEntity));
        when(imageStorageService.saveImage(frontDniFile, FileLocation.DNI, USER_DNI))
                .thenThrow(new IOException("Disk full"));

        // Act & Assert
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class,
                () -> federateStateService.federateMember(USER_EMAIL, frontDniFile, backDniFile, true));
        assertEquals("Error saving DNI images: Disk full", exception.getMessage());
        verify(imageStorageService, times(1)).saveImage(frontDniFile, FileLocation.DNI, USER_DNI);
        verifyNoMoreInteractions(imageStorageService);
    }

    @Test
    void federateMemberShouldThrowExceptionWhenStateIsInvalid() throws UserServiceException {
        // Arrange

        final PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail(USER_EMAIL);
        user.setDni(USER_DNI);

        federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.IN_PROGRESS);

        when(userService.findByEmail(USER_EMAIL)).thenReturn(user);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateStateEntity));

        // Act & Assert
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class,
                () -> federateStateService.federateMember(USER_EMAIL, frontDniFile, backDniFile, true));
        assertEquals("Bad state. User dni: " + USER_DNI, exception.getMessage());
        verifyNoInteractions(imageStorageService);
    }

    @Test
    void federateMemberShouldThrowExceptionWhenUserNotFound() throws UserServiceException {
        // Arrange
        final String notExistentUserEmail = USER_EMAIL;

        when(userService.findByEmail(notExistentUserEmail)).thenThrow(new UserServiceException("User not found"));

        // Act & Assert
        assertThrows(UserServiceException.class,
                () -> federateStateService.federateMember(notExistentUserEmail, frontDniFile, backDniFile, true));
        verifyNoInteractions(federateStateRepository);
    }

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(imageStorageService, "baseDirectory", "mock-directory");
    }

    @Test
    void testChangeAutoRenewNoFederateStateFound() throws UserServiceException {
        // Arrange

        // Create a mock UserEntity
        UserEntity mockUser = new PersistentUserEntity();
        mockUser.setDni(USER_DNI); // Set the DNI of the mock user to match your test scenario
        // Mock the behavior of the userService and federateStateRepository
        when(userService.findByEmail(USER_EMAIL)).thenReturn(mockUser);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.changeAutoRenew(USER_EMAIL);
        });
    }

    @Test
    void testChangeAutoRenewSuccess() throws Exception {
        // Arrange

        // Mocking the UserEntity and federateStateEntity
        UserEntity userEntity = mock(UserEntity.class);
        when(userService.findByEmail(USER_EMAIL)).thenReturn(userEntity);
        when(userEntity.getDni()).thenReturn(USER_DNI);

        federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.FEDERATE);
        federateStateEntity.setAutomaticRenewal(false); // Assume initial state is false

        // Mocking the federateStateRepository
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateStateEntity));

        // Mock the save method to return the updated federate state
        when(federateStateRepository.save(federateStateEntity)).thenReturn(federateStateEntity);

        // Act
        PersistentFederateStateEntity updatedFederateState = federateStateService.changeAutoRenew(USER_EMAIL);

        // Assert
        assertTrue(updatedFederateState.isAutomaticRenewal()); // Assert that the automatic renewal was toggled to true
        verify(federateStateRepository).save(federateStateEntity); // Ensure that save was called
    }

    @Test
    void testChangeAutoRenewThrowsExceptionWhenNotFederated() throws UserServiceException {
        // Arrange

        PersistentUserEntity mockUser = new PersistentUserEntity();
        mockUser.setDni(USER_DNI);

        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setAutomaticRenewal(false);
        federateState.setState(FederateState.NO_FEDERATE); // Non-FEDERATE state

        when(userService.findByEmail(USER_EMAIL)).thenReturn(mockUser);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateState));

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.changeAutoRenew(USER_EMAIL);
        });
    }

    @Test
    void testChangeAutoRenewTogglesAutoRenewWhenFederated() throws UserServiceException, FederateStateServiceException {
        // Arrange

        PersistentUserEntity mockUser = new PersistentUserEntity();
        mockUser.setDni(USER_DNI);

        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setAutomaticRenewal(false); // Initial value for testing
        federateState.setState(FederateState.FEDERATE);

        when(userService.findByEmail(USER_EMAIL)).thenReturn(mockUser);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateState));

        // Mock save operation to return the updated entity
        when(federateStateRepository.save(federateState)).thenReturn(federateState);

        // Act
        PersistentFederateStateEntity updatedFederateState = federateStateService.changeAutoRenew(USER_EMAIL);

        // Assert
        assertNotNull(updatedFederateState, "The updated federate state should not be null");
        assertTrue(updatedFederateState.isAutomaticRenewal(), "Expect autoRenew to be toggled to true");
        verify(federateStateRepository).save(federateState);
    }

    @Test
    void testConfirmCancelFederateFederate()
            throws FederateStateServiceException, UserServiceException, PaymentsServiceException {
        // Arrange

        federateStateEntity = new PersistentFederateStateEntity(); // Ensure it's a real
                                                                   // instance
        federateStateEntity.setState(FederateState.FEDERATE);
        federateStateEntity.setPayment(new PersistentPaymentsEntity()); // Assuming Payment class exists

        // Mock the repository to return the federateStateEntity when finding by ID
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateStateEntity));

        // Mock the save method to return the federateStateEntity after it's saved
        when(federateStateRepository.save(federateStateEntity)).thenReturn(federateStateEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.confirmCancelFederate(USER_DNI);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(FederateState.NO_FEDERATE, result.getState());
        assertNull(result.getPayment());
        verify(paymentsService).remove(any());
        verify(federateStateRepository).save(federateStateEntity); // Verify that save was called
    }

    @Test
    void testConfirmCancelFederateInProgress()
            throws FederateStateServiceException, UserServiceException, PaymentsServiceException {
        // Arrange

        // Create an instance of PersistentFederateStateEntity (the correct type)
        PersistentFederateStateEntity persistentFederateStateEntity = new PersistentFederateStateEntity();
        persistentFederateStateEntity.setState(FederateState.IN_PROGRESS);

        // Mock repository to return the correct entity type when findById is called
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(persistentFederateStateEntity));

        // Mock the save method to return the entity being saved
        when(federateStateRepository.save(any(PersistentFederateStateEntity.class)))
                .thenReturn(persistentFederateStateEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.confirmCancelFederate(USER_DNI);

        // Check if result is null
        assertNotNull(result, "Expected non-null result, but got null");

        // Assert that the state has been changed to FEDERATE
        assertEquals(FederateState.FEDERATE, result.getState());

        // Verify that save was called on the repository with the correct entity
        verify(federateStateRepository).save(persistentFederateStateEntity);
    }

    @Test
    void testConfirmCancelFederateNoFederate() {
        // Arrange

        // Ensure that federateStateEntity is of type PersistentFederateStateEntity
        PersistentFederateStateEntity persistentFederateStateEntity = new PersistentFederateStateEntity();
        persistentFederateStateEntity.setState(FederateState.NO_FEDERATE);

        // Stub the repository call to return the persistentFederateStateEntity
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(persistentFederateStateEntity));

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.confirmCancelFederate(USER_DNI);
        });
    }

    @Test
    void testConfirmCancelFederatePaymentRemovalError() throws PaymentsServiceException {
        // Arrange

        // Create an instance of PersistentFederateStateEntity instead of
        // FederateStateEntity
        PersistentFederateStateEntity persistentFederateStateEntity = new PersistentFederateStateEntity();
        persistentFederateStateEntity.setState(FederateState.FEDERATE);
        persistentFederateStateEntity.setPayment(new PersistentPaymentsEntity()); // Make sure it's the correct type

        // Mocking the repository to return the correct entity type
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(persistentFederateStateEntity));

        // Throw a PaymentsServiceException when remove is called
        doThrow(new PaymentsServiceException("Error removing payment")).when(paymentsService).remove(any());

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            federateStateService.confirmCancelFederate(USER_DNI);
        });
    }

    @Test
    void testConfirmCancelFederateUserNotFound() {
        // Arrange

        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.confirmCancelFederate(USER_DNI);
        });
    }

    @Test
    void testGetAllFederateStateReturnEmpty() {
        List<PersistentFederateStateEntity> listOfFederateState = new ArrayList<>();
        final String firstFederateStateUserDni = "22721880E";
        final String firstFederateStateBackImageUrl = "Dni back image URL";
        final String firstFederateStateFrontImageUrl = "Front dni image URL";
        final FederateState firstFederateStateState = FederateState.IN_PROGRESS;

        PersistentFederateStateEntity firstFederateState = new PersistentFederateStateEntity();
        firstFederateState.setAutomaticRenewal(Boolean.TRUE);
        firstFederateState.setDniBackImageUrl(firstFederateStateBackImageUrl);
        firstFederateState.setDniFrontImageUrl(firstFederateStateFrontImageUrl);
        firstFederateState.setDniLastUpdate(LocalDate.now());
        firstFederateState.setState(firstFederateStateState);
        firstFederateState.setUserDni(firstFederateStateUserDni);

        listOfFederateState.add(firstFederateState);

        final String secondFederateStateUserDni = "24421880E";
        final String secondFederateStateBackImageUrl = "Dni back image URL 2";
        final String secondFederateStateFrontImageUrl = "Front dni image URL 2";
        final FederateState secondFederateStateState = FederateState.FEDERATE;

        PersistentFederateStateEntity secondFederateState = new PersistentFederateStateEntity();
        secondFederateState.setAutomaticRenewal(Boolean.FALSE);
        secondFederateState.setDniBackImageUrl(secondFederateStateBackImageUrl);
        secondFederateState.setDniFrontImageUrl(secondFederateStateFrontImageUrl);
        secondFederateState.setDniLastUpdate(LocalDate.now());
        secondFederateState.setState(secondFederateStateState);
        secondFederateState.setUserDni(secondFederateStateUserDni);

        listOfFederateState.add(secondFederateState);

        when(federateStateRepository.findAll()).thenReturn(listOfFederateState);

        // Act
        final var result = federateStateService.getAll();

        assertEquals(2, result.size(), "List only have 2 federate state added.");

        // Assert
        assertEquals(firstFederateStateUserDni, result.getFirst().getUserDni());
        assertEquals(firstFederateStateBackImageUrl, result.getFirst().getDniBackImageUrl());
        assertEquals(firstFederateStateFrontImageUrl, result.getFirst().getDniFrontImageUrl());
        assertEquals(firstFederateStateState, result.getFirst().getState());

        assertEquals(secondFederateStateUserDni, result.getLast().getUserDni());
        assertEquals(secondFederateStateBackImageUrl, result.getLast().getDniBackImageUrl());
        assertEquals(secondFederateStateFrontImageUrl, result.getLast().getDniFrontImageUrl());
        assertEquals(secondFederateStateState, result.getLast().getState());

    }

    @Test
    void testGetFederateDataNoFederateStateFound() throws UserServiceException {
        // Arrange

        UserEntity mockUser = new PersistentUserEntity(); // Create a mock UserEntity
        mockUser.setDni(USER_DNI); // Set the DNI of the mock user to match your test scenario

        when(userService.findByEmail(USER_EMAIL)).thenReturn(mockUser);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.getFederateDataByEmail(USER_EMAIL);
        });
    }

    @Test
    void testGetFederateDataSuccess() throws UserServiceException, FederateStateServiceException {
        // Arrange

        UserEntity mockUser = new PersistentUserEntity(); // Create a mock UserEntity
        mockUser.setDni(USER_DNI); // Set the DNI of the mock user to match your test scenario
        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setUserDni(USER_DNI);

        when(userService.findByEmail(USER_EMAIL)).thenReturn(mockUser);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateState));

        // Act
        PersistentFederateStateEntity result = federateStateService.getFederateDataByEmail(USER_EMAIL);

        // Assert
        assertEquals(federateState, result);
    }

    @Test
    void testUpdateDniIOException() throws Exception {
        // Arrange

        UserEntity userEntity = mock(UserEntity.class);
        federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.FEDERATE);

        // Mocking the dependencies
        when(userService.findByEmail(USER_EMAIL)).thenReturn(userEntity);
        when(userEntity.getDni()).thenReturn(USER_DNI);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateStateEntity));

        // Match arguments exactly using only raw values
        doThrow(new IOException("Failed to save image")).when(imageStorageService).saveImage(any(MultipartFile.class),
                any(), any());

        // Act & Assert
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.updateDni(USER_EMAIL, frontDniFile, backDniFile);
        });

        assertEquals("Error updating DNI images: Failed to save image", exception.getMessage());

        // Verify behavior
        verify(imageStorageService).saveImage(any(MultipartFile.class), any(), any());
        verify(imageStorageService, never()).saveImage(backDniFile, FileLocation.DNI, USER_DNI);
        verify(federateStateRepository, never()).save(any());
    }

    @Test
    void testUpdateDniSuccess() throws Exception {
        // Arrange

        UserEntity userEntity = mock(UserEntity.class);
        federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.FEDERATE);
        // Mocking the dependencies
        when(userService.findByEmail(USER_EMAIL)).thenReturn(userEntity);
        when(userEntity.getDni()).thenReturn(USER_DNI);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(federateStateEntity));
        String frontImageUrl = "frontImageUrl";
        String backImageUrl = "backImageUrl";
        // Update stubbing to match base-directory
        when(imageStorageService.saveImage(frontDniFile, FileLocation.DNI, USER_DNI)).thenReturn(frontImageUrl);
        when(imageStorageService.saveImage(backDniFile, FileLocation.DNI, USER_DNI)).thenReturn(backImageUrl);
        when(federateStateRepository.save(federateStateEntity)).thenReturn(federateStateEntity);
        // Act
        PersistentFederateStateEntity result = federateStateService.updateDni(USER_EMAIL, frontDniFile, backDniFile);
        // Assert
        assertNotNull(result);
        assertEquals(frontImageUrl, result.getDniFrontImageUrl());
        assertEquals(backImageUrl, result.getDniBackImageUrl());
        verify(federateStateRepository).save(federateStateEntity);
        verify(imageStorageService).saveImage(frontDniFile, FileLocation.DNI, USER_DNI);
        verify(imageStorageService).saveImage(backDniFile, FileLocation.DNI, USER_DNI);
    }

    @Test
    void testUpdateDniUserNotFederate() throws Exception {

        // Given
        UserEntity mockUserEntity = mock(UserEntity.class);
        when(mockUserEntity.getDni()).thenReturn(USER_DNI);
        when(userService.findByEmail(USER_EMAIL)).thenReturn(mockUserEntity);
        // Mock federate state entity with a non-federate state
        PersistentFederateStateEntity mockFederateStateEntity = mock(PersistentFederateStateEntity.class);
        when(federateStateRepository.findById(USER_DNI)).thenReturn(Optional.of(mockFederateStateEntity));
        when(mockFederateStateEntity.getState()).thenReturn(FederateState.NO_FEDERATE);
        // When & Then
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.updateDni(USER_EMAIL, frontDniFile, backDniFile);
        });
        assertEquals("User is not in a federate state.", exception.getMessage());
    }

}
