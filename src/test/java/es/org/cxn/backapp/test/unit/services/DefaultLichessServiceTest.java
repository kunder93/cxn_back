package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.org.cxn.backapp.exceptions.LichessServiceException;
import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.PersistentLichessProfileEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.LichessAuthRepository;
import es.org.cxn.backapp.repository.LichessEntityRepository;
import es.org.cxn.backapp.repository.OAuthAuthorizationRequestRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.DefaultLichessService;
import es.org.cxn.backapp.service.dto.LichessProfileDto;
import es.org.cxn.backapp.service.dto.LichessSaveProfileDto;
import es.org.cxn.backapp.service.dto.LichessSaveProfileDto.SaveGameStatistics;

@ExtendWith(MockitoExtension.class)
class DefaultLichessServiceTest {

    @InjectMocks
    private DefaultLichessService lichessService;

    @Mock
    private LichessAuthRepository lichessAuthRepository;

    @Mock
    private OAuthAuthorizationRequestRepository oAuthAuthorizationRequestRepository;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private LichessEntityRepository lichessEntityRepository;

    private PersistentUserEntity userEntity;
    private PersistentLichessAuthEntity authEntity;
    private PersistentOAuthAuthorizationRequestEntity authRequestEntity;

    @BeforeEach
    void setUp() {
        userEntity = new PersistentUserEntity();
        userEntity.setDni("12345678");
        userEntity.setEmail("test@example.com");
        userEntity.setName("Test");
        userEntity.setFirstSurname("User");
        userEntity.setSecondSurname("Example");

        authEntity = new PersistentLichessAuthEntity();
        authEntity.setAccessToken("token123");
        authEntity.setExpirationDate(LocalDateTime.now().plusHours(1));
        authEntity.setUserDni(userEntity.getDni());

        authRequestEntity = new PersistentOAuthAuthorizationRequestEntity();
        authRequestEntity.setUserDni(userEntity.getDni());
        authRequestEntity.setCodeVerifier("verifier123");
    }

    @Test
    void testGetCodeVerifier_Success() throws LichessServiceException {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        when(oAuthAuthorizationRequestRepository.findById(userEntity.getDni()))
                .thenReturn(Optional.of(authRequestEntity));

        String codeVerifier = lichessService.getCodeVerifier(userEntity.getEmail());

        assertEquals("verifier123", codeVerifier);
        verify(userEntityRepository).findByEmail(userEntity.getEmail());
        verify(oAuthAuthorizationRequestRepository).findById(userEntity.getDni());
    }

    @Test
    void testGetCodeVerifier_UserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.getCodeVerifier(userEntity.getEmail()));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testGetCodeVerifierThrowsExceptionWhenOAuthRequestNotFound() {
        String userEmail = "email1@email.es";

        // Mocking a user entity
        PersistentUserEntity userEntity = new PersistentUserEntity();
        userEntity.setDni("123456");
        userEntity.setEmail(userEmail);

        // Mocking the repository to return a valid user
        when(userEntityRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));

        // Mocking the oAuthAuthorizationRequestRepository to return an empty Optional
        when(oAuthAuthorizationRequestRepository.findById(userEntity.getDni())).thenReturn(Optional.empty());

        // Act & Assert
        LichessServiceException exception = assertThrows(LichessServiceException.class, () -> {
            lichessService.getCodeVerifier(userEmail);
        });

        // Assert that the exception contains the correct message
        assertEquals("User with dni: 123456 no have OAuthRequest.", exception.getMessage());
    }

    @Test
    void testGetLichessProfile_ProfileDoesNotExist() throws LichessServiceException {
        // Arrange
        String userEmail = "test@example.com";
        String userDni = userEntity.getDni();

        // Mocking
        when(userEntityRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(lichessEntityRepository.findById(userDni)).thenReturn(Optional.empty());

        // Act
        LichessProfileDto result = lichessService.getLichessProfile(userEmail);

        // Assert
        assertNotNull(result);
        // Only assert games
        assertEquals(0, result.blitz().games());
        assertEquals(0, result.blitz().rating());
        assertEquals(0, result.bullet().games());
        assertEquals(0, result.bullet().rating());
        assertEquals(0, result.classical().games());
        assertEquals(0, result.classical().rating());
        assertEquals(0, result.rapid().games());
        assertEquals(0, result.rapid().rating());
        assertEquals(0, result.puzzle().games());
        assertEquals(0, result.puzzle().rating());
    }

    @Test
    void testGetLichessProfile_ProfileExists() throws LichessServiceException {
        // Arrange
        String userEmail = "test@example.com";
        String userDni = userEntity.getDni();

        // Create a mock Lichess profile entity
        PersistentLichessProfileEntity lichessProfileEntity = new PersistentLichessProfileEntity();
        lichessProfileEntity.setId("profileId");
        lichessProfileEntity.setUsername("testUser");
        lichessProfileEntity.setUpdatedAt(LocalDateTime.now());
        lichessProfileEntity.setBlitzGames(10);
        lichessProfileEntity.setBlitzRating(1500);
        lichessProfileEntity.setBlitzRd(30);
        lichessProfileEntity.setBlitzProg(0);
        lichessProfileEntity.setBlitzProv(false);

        // Mocking
        when(userEntityRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(lichessEntityRepository.findById(userDni)).thenReturn(Optional.of(lichessProfileEntity));

        // Act
        LichessProfileDto result = lichessService.getLichessProfile(userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(lichessProfileEntity.getUsername(), result.username());
        assertEquals(lichessProfileEntity.getUpdatedAt(), result.updatedAt());
        assertEquals(10, result.blitz().games());
        assertEquals(1500, result.blitz().rating());
    }

    @Test
    void testGetLichessProfile_UserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.getLichessProfile(userEntity.getEmail()));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testGetLichessProfiles() {

        // Sample Lichess profile entities
        List<PersistentLichessProfileEntity> lichessProfilesEntitiesList = new ArrayList<>();

        // Creating the first Lichess profile entity
        PersistentLichessProfileEntity profile1 = new PersistentLichessProfileEntity();
        profile1.setUserDni("123456"); // User's unique identifier
        profile1.setId("lichessId1"); // Lichess profile ID
        profile1.setUsername("user1"); // Username
        profile1.setBlitzGames(50); // Number of blitz games
        profile1.setBlitzRating(1500); // Blitz rating
        profile1.setBlitzRd(30); // Blitz rating deviation
        profile1.setBlitzProg(20); // Blitz progress
        profile1.setBlitzProv(false); // Is blitz rating provisional?
        profile1.setBulletGames(30); // Number of bullet games
        profile1.setBulletRating(1450); // Bullet rating
        profile1.setBulletRd(25); // Bullet rating deviation
        profile1.setBulletProg(10); // Bullet progress
        profile1.setBulletProv(true); // Is bullet rating provisional?
        profile1.setClassicalGames(10); // Number of classical games
        profile1.setClassicalRating(1600); // Classical rating
        profile1.setClassicalRd(15); // Classical rating deviation
        profile1.setClassicalProg(5); // Classical progress
        profile1.setClassicalProv(false); // Is classical rating provisional?
        profile1.setRapidGames(25); // Number of rapid games
        profile1.setRapidRating(1550); // Rapid rating
        profile1.setRapidRd(20); // Rapid rating deviation
        profile1.setRapidProg(15); // Rapid progress
        profile1.setRapidProv(true); // Is rapid rating provisional?
        profile1.setPuzzleGames(100); // Number of puzzle games
        profile1.setPuzzleRating(1200); // Puzzle rating
        profile1.setPuzzleRd(10); // Puzzle rating deviation
        profile1.setPuzzleProg(50); // Puzzle progress
        profile1.setPuzzleProv(false); // Is puzzle rating provisional?
        profile1.setUpdatedAt(LocalDateTime.now()); // Last updated date

        // Creating the second Lichess profile entity
        PersistentLichessProfileEntity profile2 = new PersistentLichessProfileEntity();
        profile2.setUserDni("654321"); // User's unique identifier
        profile2.setId("lichessId2"); // Lichess profile ID
        profile2.setUsername("user2"); // Username
        profile2.setBlitzGames(70); // Number of blitz games
        profile2.setBlitzRating(1550); // Blitz rating
        profile2.setBlitzRd(20); // Blitz rating deviation
        profile2.setBlitzProg(30); // Blitz progress
        profile2.setBlitzProv(false); // Is blitz rating provisional?
        profile2.setBulletGames(40); // Number of bullet games
        profile2.setBulletRating(1480); // Bullet rating
        profile2.setBulletRd(22); // Bullet rating deviation
        profile2.setBulletProg(15); // Bullet progress
        profile2.setBulletProv(true); // Is bullet rating provisional?
        profile2.setClassicalGames(20); // Number of classical games
        profile2.setClassicalRating(1650); // Classical rating
        profile2.setClassicalRd(12); // Classical rating deviation
        profile2.setClassicalProg(10); // Classical progress
        profile2.setClassicalProv(false); // Is classical rating provisional?
        profile2.setRapidGames(30); // Number of rapid games
        profile2.setRapidRating(1600); // Rapid rating
        profile2.setRapidRd(18); // Rapid rating deviation
        profile2.setRapidProg(20); // Rapid progress
        profile2.setRapidProv(true); // Is rapid rating provisional?
        profile2.setPuzzleGames(150); // Number of puzzle games
        profile2.setPuzzleRating(1250); // Puzzle rating
        profile2.setPuzzleRd(15); // Puzzle rating deviation
        profile2.setPuzzleProg(70); // Puzzle progress
        profile2.setPuzzleProv(false); // Is puzzle rating provisional?
        profile2.setUpdatedAt(LocalDateTime.now()); // Last updated date
        // Mocking the repository methods

        lichessProfilesEntitiesList.add(profile1);
        lichessProfilesEntitiesList.add(profile2);

        when(lichessEntityRepository.findAll()).thenReturn(lichessProfilesEntitiesList);

        // Creating user entities to mock the userEntityRepository
        PersistentUserEntity userEntity1 = new PersistentUserEntity();
        userEntity1.setDni("123456");
        userEntity1.setEmail("email1@email.es");
        userEntity1.setName("user1");
        userEntity1.setFirstSurname("Doe");
        userEntity1.setSecondSurname("Doa");

        PersistentUserEntity userEntity2 = new PersistentUserEntity();
        userEntity2.setDni("654321");
        userEntity2.setEmail("email2@email.es");
        userEntity2.setName("user2");
        userEntity2.setFirstSurname("Doe2");
        userEntity2.setSecondSurname("Doa2");

        // Mock the userEntityRepository responses
        when(userEntityRepository.findByDni("123456")).thenReturn(Optional.of(userEntity1));
        when(userEntityRepository.findByDni("654321")).thenReturn(Optional.of(userEntity2));

        // Act
        List<LichessProfileDto> result = lichessService.getLichessProfiles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Check that we got 2 profiles in the result

    }

    @Test
    void testSaveAuthToken_Success() throws LichessServiceException {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        lichessService.saveAuthToken("Bearer", "token123", LocalDateTime.now().plusHours(1), userEntity.getEmail());

        verify(lichessAuthRepository).save(any(PersistentLichessAuthEntity.class));
        verify(userEntityRepository).save(userEntity);
    }

    @Test
    void testSaveAuthToken_UserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class, () -> lichessService
                .saveAuthToken("Bearer", "token123", LocalDateTime.now().plusHours(1), userEntity.getEmail()));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testSaveLichessProfile_Success() throws LichessServiceException {
        // Create an instance of SaveGameStatistics for each game type
        SaveGameStatistics blitzStats = new SaveGameStatistics(10, 1500, 30, 0, false);
        SaveGameStatistics bulletStats = new SaveGameStatistics(20, 1400, 40, 0, false);
        SaveGameStatistics classicalStats = new SaveGameStatistics(30, 1600, 50, 0, false);
        SaveGameStatistics rapidStats = new SaveGameStatistics(40, 1300, 60, 0, false);
        SaveGameStatistics puzzleStats = new SaveGameStatistics(50, 1200, 70, 0, false);

        // Create the DTO with proper statistics
        LichessSaveProfileDto dto = new LichessSaveProfileDto("test@example.com", "profileId", "testUser",
                LocalDateTime.now(), blitzStats, bulletStats, classicalStats, rapidStats, puzzleStats);

        // Mock the user repository to return the user entity
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        // Mock the lichess entity repository to return the saved entity
        PersistentLichessProfileEntity savedEntity = new PersistentLichessProfileEntity();
        savedEntity.setId("profileId");
        savedEntity.setUsername("testUser");
        savedEntity.setUserDni(userEntity.getDni());
        savedEntity.setBlitzGames(blitzStats.games());
        savedEntity.setBlitzRating(blitzStats.rating());
        savedEntity.setBulletGames(bulletStats.games());
        savedEntity.setBulletRating(bulletStats.rating());
        savedEntity.setClassicalGames(classicalStats.games());
        savedEntity.setClassicalRating(classicalStats.rating());
        savedEntity.setRapidGames(rapidStats.games());
        savedEntity.setRapidRating(rapidStats.rating());
        savedEntity.setPuzzleGames(puzzleStats.games());
        savedEntity.setPuzzleRating(puzzleStats.rating());

        // Mock saving behavior
        when(lichessEntityRepository.save(any(PersistentLichessProfileEntity.class))).thenReturn(savedEntity);

        // Call the service method
        PersistentLichessProfileEntity result = lichessService.saveLichessProfile(dto);

        // Assertions to verify the profile was saved correctly
        assertNotNull(result);
        assertEquals("profileId", result.getId());
        assertEquals("testUser", result.getUsername());
        assertEquals(userEntity.getDni(), result.getUserDni());
        assertEquals(blitzStats.games(), result.getBlitzGames());
        assertEquals(blitzStats.rating(), result.getBlitzRating());
        assertEquals(bulletStats.games(), result.getBulletGames());
        assertEquals(bulletStats.rating(), result.getBulletRating());
        assertEquals(classicalStats.games(), result.getClassicalGames());
        assertEquals(classicalStats.rating(), result.getClassicalRating());
        assertEquals(rapidStats.games(), result.getRapidGames());
        assertEquals(rapidStats.rating(), result.getRapidRating());
        assertEquals(puzzleStats.games(), result.getPuzzleGames());
        assertEquals(puzzleStats.rating(), result.getPuzzleRating());

        // Verify interactions with the repositories
        verify(lichessEntityRepository).save(any(PersistentLichessProfileEntity.class));
    }

    @Test
    void testSaveLichessProfile_UserNotFound() {
        LichessSaveProfileDto dto = new LichessSaveProfileDto("test@example.com", "profileId", "testUser",
                LocalDateTime.now(), null, null, null, null, null);

        when(userEntityRepository.findByEmail(dto.userEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.saveLichessProfile(dto));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testSaveOAuthRequest_Success() throws LichessServiceException {
        // Create a sample PersistentOAuthAuthorizationRequestEntity
        final var oAuthAuthorizationRequestEntity = new PersistentOAuthAuthorizationRequestEntity();
        oAuthAuthorizationRequestEntity.setUserDni(userEntity.getDni());
        oAuthAuthorizationRequestEntity.setCodeVerifier("verifier123");
        oAuthAuthorizationRequestEntity.setCreatedAt(LocalDateTime.now());
        oAuthAuthorizationRequestEntity.setState("tmpState");

        // Mock the user repository to return the user entity
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        // Mock the OAuth request repository to return the request entity when saved
        when(oAuthAuthorizationRequestRepository.save(any(PersistentOAuthAuthorizationRequestEntity.class)))
                .thenReturn(oAuthAuthorizationRequestEntity);
        // Mock the OAuth request repository to return the request entity when saved
        when(userEntityRepository.save(any(PersistentUserEntity.class))).thenReturn(userEntity);

        // Call the service method
        PersistentOAuthAuthorizationRequestEntity savedRequest = lichessService.saveOAuthRequest(userEntity.getEmail(),
                "verifier123");

        // Assertions to verify the request was saved correctly
        assertNotNull(savedRequest);
        assertEquals(oAuthAuthorizationRequestEntity.getCodeVerifier(), savedRequest.getCodeVerifier());
        assertEquals(oAuthAuthorizationRequestEntity.getUserDni(), savedRequest.getUserDni());

        // Verify interactions with the repositories
        verify(oAuthAuthorizationRequestRepository).save(any(PersistentOAuthAuthorizationRequestEntity.class));
        verify(userEntityRepository).save(userEntity);
    }

    @Test
    void testSaveOAuthRequest_UserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.saveOAuthRequest(userEntity.getEmail(), "verifier123"));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

}