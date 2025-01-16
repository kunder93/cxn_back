
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
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.FederateStateEntity;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;
import es.org.cxn.backapp.service.ImageStorageService;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultFederateStateService;

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
    private FederateStateEntity federateStateEntity;

    @Mock
    private ImageStorageService imageStorageService;

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

    /**
     * Path for storing DNI images, injected from the application properties. This
     * is used by the service to determine the location for saving the uploaded DNI
     * images.
     */
    @Value("${image.location.dnis}")
    private String imageLocationDnis;

    @Test
    void federateMemberShouldSaveImagesAndUpdateStateWhenStateIsFederate() throws Exception {
        // Arrange
        PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail("test@example.com");
        user.setDni("12345678A");

        // Mock federate state as FEDERATE
        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setState(FederateState.FEDERATE);

        // Mock return values for dependencies
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(federateStateRepository.findById("12345678A")).thenReturn(Optional.of(federateState));
        when(imageStorageService.saveImage(frontDniFile, "base-directory", "user-dni", "12345678A"))
                .thenReturn("front-image-url");
        when(imageStorageService.saveImage(backDniFile, "base-directory", "user-dni", "12345678A"))
                .thenReturn("back-image-url");

        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        when(paymentsService.createPayment(BigDecimal.valueOf(15.00), PaymentsCategory.FEDERATE_PAYMENT,
                "Coste ficha federativa.", "Pago federar usuario", "12345678A")).thenReturn(paymentEntity);

        PersistentFederateStateEntity updatedEntity = new PersistentFederateStateEntity();
        updatedEntity.setUserDni("12345678A");
        updatedEntity.setState(FederateState.IN_PROGRESS);
        when(federateStateRepository.save(any(PersistentFederateStateEntity.class))).thenReturn(updatedEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.federateMember("test@example.com", frontDniFile,
                backDniFile, true);

        // Assert
        assertNotNull(result);

        verify(imageStorageService, times(1)).saveImage(frontDniFile, "base-directory", "user-dni", "12345678A");
        verify(imageStorageService, times(1)).saveImage(backDniFile, "base-directory", "user-dni", "12345678A");
        verify(paymentsService, times(1)).createPayment(BigDecimal.valueOf(15.00), PaymentsCategory.FEDERATE_PAYMENT,
                "Coste ficha federativa.", "Pago federar usuario", "12345678A");
        verify(federateStateRepository, times(1)).save(any(PersistentFederateStateEntity.class));
    }

    @Test
    void federateMemberShouldSaveImagesAndUpdateStateWhenStateIsNoFederate() throws Exception {
        // Arrange
        // Mock user and its details
        PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail("test@example.com");
        user.setDni("12345678A");

        // Mock existing federate state
        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setState(FederateState.NO_FEDERATE);

        // Mock return values for dependencies
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(federateStateRepository.findById("12345678A")).thenReturn(Optional.of(federateState));
        when(imageStorageService.saveImage(frontDniFile, "base-directory", "user-dni", "12345678A"))
                .thenReturn("front-image-url");
        when(imageStorageService.saveImage(backDniFile, "base-directory", "user-dni", "12345678A"))
                .thenReturn("back-image-url");

        PersistentPaymentsEntity paymentEntity = new PersistentPaymentsEntity();
        when(paymentsService.createPayment(BigDecimal.valueOf(15.00), PaymentsCategory.FEDERATE_PAYMENT,
                "Coste ficha federativa.", "Pago federar usuario", "12345678A")).thenReturn(paymentEntity);

        PersistentFederateStateEntity updatedEntity = new PersistentFederateStateEntity();
        updatedEntity.setUserDni("12345678A");
        updatedEntity.setState(FederateState.IN_PROGRESS);
        when(federateStateRepository.save(any(PersistentFederateStateEntity.class))).thenReturn(updatedEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.federateMember("test@example.com", frontDniFile,
                backDniFile, true);

        // Assert
        assertNotNull(result);
        assertEquals(FederateState.IN_PROGRESS, result.getState());

        verify(imageStorageService, times(1)).saveImage(frontDniFile, "base-directory", "user-dni", "12345678A");
        verify(imageStorageService, times(1)).saveImage(backDniFile, "base-directory", "user-dni", "12345678A");
        verify(paymentsService, times(1)).createPayment(BigDecimal.valueOf(15.00), PaymentsCategory.FEDERATE_PAYMENT,
                "Coste ficha federativa.", "Pago federar usuario", "12345678A");
        verify(federateStateRepository, times(1)).save(any(PersistentFederateStateEntity.class));
    }

    @Test
    void federateMemberShouldThrowExceptionWhenImageSavingFails() throws Exception {
        // Arrange
        final String userEmail = "test@example.com";
        final String userDni = "12345678A";

        final PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail(userEmail);
        user.setDni(userDni);

        final PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.NO_FEDERATE);

        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateStateEntity));
        when(imageStorageService.saveImage(frontDniFile, "base-directory", "user-dni", userDni))
                .thenThrow(new IOException("Disk full"));

        // Act & Assert
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class,
                () -> federateStateService.federateMember(userEmail, frontDniFile, backDniFile, true));
        assertEquals("Error saving DNI images: Disk full", exception.getMessage());
        verify(imageStorageService, times(1)).saveImage(frontDniFile, "base-directory", "user-dni", userDni);
        verifyNoMoreInteractions(imageStorageService);
    }

    @Test
    void federateMemberShouldThrowExceptionWhenStateIsInvalid() throws UserServiceException {
        // Arrange
        final String userEmail = "test@example.com";
        final String userDni = "12345678A";

        final PersistentUserEntity user = new PersistentUserEntity();
        user.setEmail(userEmail);
        user.setDni(userDni);

        final PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.IN_PROGRESS);

        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateStateEntity));

        // Act & Assert
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class,
                () -> federateStateService.federateMember(userEmail, frontDniFile, backDniFile, true));
        assertEquals("Bad state. User dni: " + userDni, exception.getMessage());
        verifyNoInteractions(imageStorageService);
    }

    @Test
    void federateMemberShouldThrowExceptionWhenUserNotFound() throws UserServiceException {
        // Arrange
        final String userEmail = "nonexistent@example.com";

        when(userService.findByEmail(userEmail)).thenThrow(new UserServiceException("User not found"));

        // Act & Assert
        assertThrows(UserServiceException.class,
                () -> federateStateService.federateMember(userEmail, frontDniFile, backDniFile, true));
        verifyNoInteractions(federateStateRepository);
    }

    @BeforeEach
    public void setup() {
        // Empty constructor
    }

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(federateStateService, "imageLocationDnis", "base-directory");
    }

    @Test
    void testChangeAutoRenewNoFederateStateFound() throws UserServiceException {
        // Arrange
        final String userEmail = "test@example.com";
        final String userDni = "12345678X";
        // Create a mock UserEntity
        UserEntity mockUser = new PersistentUserEntity();
        mockUser.setDni(userDni); // Set the DNI of the mock user to match your test scenario
        // Mock the behavior of the userService and federateStateRepository
        when(userService.findByEmail(userEmail)).thenReturn(mockUser);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.changeAutoRenew(userEmail);
        });
    }

    @Test
    void testChangeAutoRenewSuccess() throws Exception {
        // Arrange
        String userEmail = "user@example.com";
        String userDni = "12345";

        // Mocking the UserEntity and federateStateEntity
        UserEntity userEntity = mock(UserEntity.class);
        when(userService.findByEmail(userEmail)).thenReturn(userEntity);
        when(userEntity.getDni()).thenReturn(userDni);

        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.FEDERATE);
        federateStateEntity.setAutomaticRenewal(false); // Assume initial state is false

        // Mocking the federateStateRepository
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateStateEntity));

        // Mock the save method to return the updated federate state
        when(federateStateRepository.save(federateStateEntity)).thenReturn(federateStateEntity);

        // Act
        PersistentFederateStateEntity updatedFederateState = federateStateService.changeAutoRenew(userEmail);

        // Assert
        assertTrue(updatedFederateState.isAutomaticRenewal()); // Assert that the automatic renewal was toggled to true
        verify(federateStateRepository).save(federateStateEntity); // Ensure that save was called
    }

    @Test
    void testChangeAutoRenewThrowsExceptionWhenNotFederated() throws UserServiceException {
        // Arrange
        final String userEmail = "test@example.com";
        final String userDni = "12345678X";
        PersistentUserEntity mockUser = new PersistentUserEntity();
        mockUser.setDni(userDni);

        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setAutomaticRenewal(false);
        federateState.setState(FederateState.NO_FEDERATE); // Non-FEDERATE state

        when(userService.findByEmail(userEmail)).thenReturn(mockUser);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateState));

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.changeAutoRenew(userEmail);
        });
    }

    @Test
    void testChangeAutoRenewTogglesAutoRenewWhenFederated() throws UserServiceException, FederateStateServiceException {
        // Arrange
        final String userEmail = "test@example.com";
        final String userDni = "12345678X";
        PersistentUserEntity mockUser = new PersistentUserEntity();
        mockUser.setDni(userDni);

        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setAutomaticRenewal(false); // Initial value for testing
        federateState.setState(FederateState.FEDERATE);

        when(userService.findByEmail(userEmail)).thenReturn(mockUser);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateState));

        // Mock save operation to return the updated entity
        when(federateStateRepository.save(federateState)).thenReturn(federateState);

        // Act
        PersistentFederateStateEntity updatedFederateState = federateStateService.changeAutoRenew(userEmail);

        // Assert
        assertNotNull(updatedFederateState, "The updated federate state should not be null");
        assertTrue(updatedFederateState.isAutomaticRenewal(), "Expect autoRenew to be toggled to true");
        verify(federateStateRepository).save(federateState);
    }

    @Test
    void testConfirmCancelFederateFederate()
            throws FederateStateServiceException, UserServiceException, PaymentsServiceException {
        // Arrange
        String userDni = "12345";
        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity(); // Ensure it's a real
                                                                                                 // instance
        federateStateEntity.setState(FederateState.FEDERATE);
        federateStateEntity.setPayment(new PersistentPaymentsEntity()); // Assuming Payment class exists

        // Mock the repository to return the federateStateEntity when finding by ID
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateStateEntity));

        // Mock the save method to return the federateStateEntity after it's saved
        when(federateStateRepository.save(federateStateEntity)).thenReturn(federateStateEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.confirmCancelFederate(userDni);

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
        String userDni = "12345";

        // Create an instance of PersistentFederateStateEntity (the correct type)
        PersistentFederateStateEntity persistentFederateStateEntity = new PersistentFederateStateEntity();
        persistentFederateStateEntity.setState(FederateState.IN_PROGRESS);

        // Mock repository to return the correct entity type when findById is called
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(persistentFederateStateEntity));

        // Mock the save method to return the entity being saved
        when(federateStateRepository.save(any(PersistentFederateStateEntity.class)))
                .thenReturn(persistentFederateStateEntity);

        // Act
        PersistentFederateStateEntity result = federateStateService.confirmCancelFederate(userDni);

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
        String userDni = "12345";

        // Ensure that federateStateEntity is of type PersistentFederateStateEntity
        PersistentFederateStateEntity persistentFederateStateEntity = new PersistentFederateStateEntity();
        persistentFederateStateEntity.setState(FederateState.NO_FEDERATE);

        // Stub the repository call to return the persistentFederateStateEntity
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(persistentFederateStateEntity));

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.confirmCancelFederate(userDni);
        });
    }

    @Test
    void testConfirmCancelFederatePaymentRemovalError() throws PaymentsServiceException {
        // Arrange
        String userDni = "12345";
        // Create an instance of PersistentFederateStateEntity instead of
        // FederateStateEntity
        PersistentFederateStateEntity persistentFederateStateEntity = new PersistentFederateStateEntity();
        persistentFederateStateEntity.setState(FederateState.FEDERATE);
        persistentFederateStateEntity.setPayment(new PersistentPaymentsEntity()); // Make sure it's the correct type

        // Mocking the repository to return the correct entity type
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(persistentFederateStateEntity));

        // Throw a PaymentsServiceException when remove is called
        doThrow(new PaymentsServiceException("Error removing payment")).when(paymentsService).remove(any());

        // Act & Assert
        assertThrows(PaymentsServiceException.class, () -> {
            federateStateService.confirmCancelFederate(userDni);
        });
    }

    @Test
    void testConfirmCancelFederateUserNotFound() {
        // Arrange
        String userDni = "12345";
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.confirmCancelFederate(userDni);
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
        final String userEmail = "test@example.com";
        final String userDni = "12345678X";
        UserEntity mockUser = new PersistentUserEntity(); // Create a mock UserEntity
        mockUser.setDni(userDni); // Set the DNI of the mock user to match your test scenario

        when(userService.findByEmail(userEmail)).thenReturn(mockUser);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.getFederateData(userEmail);
        });
    }

    @Test
    void testGetFederateDataSuccess() throws UserServiceException, FederateStateServiceException {
        // Arrange
        final String userEmail = "test@example.com";
        final String userDni = "12345678X";
        UserEntity mockUser = new PersistentUserEntity(); // Create a mock UserEntity
        mockUser.setDni(userDni); // Set the DNI of the mock user to match your test scenario
        PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
        federateState.setUserDni(userDni);

        when(userService.findByEmail(userEmail)).thenReturn(mockUser);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateState));

        // Act
        PersistentFederateStateEntity result = federateStateService.getFederateData(userEmail);

        // Assert
        assertEquals(federateState, result);
    }

    @Test
    void testUpdateDniIOException() throws Exception {
        // Arrange
        String userEmail = "user@example.com";
        String userDni = "12345";

        UserEntity userEntity = mock(UserEntity.class);
        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.FEDERATE);

        // Mocking the dependencies
        when(userService.findByEmail(userEmail)).thenReturn(userEntity);
        when(userEntity.getDni()).thenReturn(userDni);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateStateEntity));

        // Match arguments exactly using only raw values
        doThrow(new IOException("Failed to save image")).when(imageStorageService).saveImage(any(MultipartFile.class),
                any(), any(), any());

        // Act & Assert
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.updateDni(userEmail, frontDniFile, backDniFile);
        });

        assertEquals("Error updating DNI images: Failed to save image", exception.getMessage());

        // Verify behavior
        verify(imageStorageService).saveImage(any(MultipartFile.class), anyString(), anyString(), anyString());
        verify(imageStorageService, never()).saveImage(backDniFile, imageLocationDnis, "user-dni", userDni);
        verify(federateStateRepository, never()).save(any());
    }

    @Test
    void testUpdateDniSuccess() throws Exception {
        // Arrange
        String userEmail = "user@example.com";
        String userDni = "12345";
        UserEntity userEntity = mock(UserEntity.class);
        PersistentFederateStateEntity federateStateEntity = new PersistentFederateStateEntity();
        federateStateEntity.setState(FederateState.FEDERATE);
        // Mocking the dependencies
        when(userService.findByEmail(userEmail)).thenReturn(userEntity);
        when(userEntity.getDni()).thenReturn(userDni);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(federateStateEntity));
        String frontImageUrl = "frontImageUrl";
        String backImageUrl = "backImageUrl";
        // Update stubbing to match base-directory
        when(imageStorageService.saveImage(frontDniFile, "base-directory", "user-dni", userDni))
                .thenReturn(frontImageUrl);
        when(imageStorageService.saveImage(backDniFile, "base-directory", "user-dni", userDni))
                .thenReturn(backImageUrl);
        when(federateStateRepository.save(federateStateEntity)).thenReturn(federateStateEntity);
        // Act
        PersistentFederateStateEntity result = federateStateService.updateDni(userEmail, frontDniFile, backDniFile);
        // Assert
        assertNotNull(result);
        assertEquals(frontImageUrl, result.getDniFrontImageUrl());
        assertEquals(backImageUrl, result.getDniBackImageUrl());
        verify(federateStateRepository).save(federateStateEntity);
        verify(imageStorageService).saveImage(frontDniFile, "base-directory", "user-dni", userDni);
        verify(imageStorageService).saveImage(backDniFile, "base-directory", "user-dni", userDni);
    }

    @Test
    void testUpdateDniUserNotFederate() throws Exception {
        final String userEmail = "user@example.com";
        final String userDni = "12345678A";
        // Given
        UserEntity mockUserEntity = mock(UserEntity.class);
        when(mockUserEntity.getDni()).thenReturn(userDni);
        when(userService.findByEmail(userEmail)).thenReturn(mockUserEntity);
        // Mock federate state entity with a non-federate state
        PersistentFederateStateEntity mockFederateStateEntity = mock(PersistentFederateStateEntity.class);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.of(mockFederateStateEntity));
        when(mockFederateStateEntity.getState()).thenReturn(FederateState.NO_FEDERATE);
        // When & Then
        FederateStateServiceException exception = assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.updateDni(userEmail, frontDniFile, backDniFile);
        });
        assertEquals("User is not in a federate state.", exception.getMessage());
    }

}
