
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;
import es.org.cxn.backapp.service.UserService;
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

    @BeforeEach
    public void setup() {
        federateStateService = new DefaultFederateStateService(federateStateRepository, userService);
    }

    @Test
    void testChangeAutoRenewNoFederateStateFound() throws UserServiceException {
        // Arrange
        final String userEmail = "test@example.com";
        final String userDni = "12345678X";
        UserEntity mockUser = new PersistentUserEntity(); // Create a mock UserEntity
        mockUser.setDni(userDni); // Set the DNI of the mock user to match your test scenario

        when(userService.findByEmail(userEmail)).thenReturn(mockUser);
        when(federateStateRepository.findById(userDni)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FederateStateServiceException.class, () -> {
            federateStateService.changeAutoRenew(userEmail);
        });
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
    void testConfirmCancelFederateNoFederateStateFound() {
        // Arrange
        final String userDni = "12345678X";
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

}
