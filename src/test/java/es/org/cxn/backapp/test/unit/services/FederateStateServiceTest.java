
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;
import es.org.cxn.backapp.service.DefaultFederateStateService;
import es.org.cxn.backapp.service.UserService;

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

    @Mock
    private FederateStateEntityRepository federateStateRepository;

    @Mock
    private UserService userService;

    @Mock
    private MultipartFile frontDniFile;

    @Mock
    private MultipartFile backDniFile;

    @InjectMocks
    private DefaultFederateStateService federateStateService;

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