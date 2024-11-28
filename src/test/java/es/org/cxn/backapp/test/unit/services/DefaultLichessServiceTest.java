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

/**
 * Unit test class for {@link DefaultLichessService}. This class verifies the
 * behavior of the {@link DefaultLichessService} class, including interaction
 * with its repositories and service methods. It covers different scenarios for
 * Lichess profile management and OAuth authorization requests.
 *
 * The tests are focused on ensuring proper exception handling, data retrieval
 * and transformation, and repository interactions.
 *
 * @author Santiago Paz
 */
@ExtendWith(MockitoExtension.class)
class DefaultLichessServiceTest {

    /**
     * Minimum number of Blitz games required for a certain rating level.
     */
    private static final int BLITZ_GAMES_1 = 50;

    /**
     * Minimum number of Blitz games required for a higher rating level.
     */
    private static final int BLITZ_GAMES_2 = 70;

    /**
     * Minimum number of Bullet games required for a certain rating level.
     */
    private static final int BULLET_GAMES_1 = 30;

    /**
     * Minimum number of Bullet games required for a higher rating level.
     */
    private static final int BULLET_GAMES_2 = 40;

    /**
     * Minimum number of Classical games required for a certain rating level.
     */
    private static final int CLASSICAL_GAMES_1 = 10;

    /**
     * Minimum number of Classical games required for a higher rating level.
     */
    private static final int CLASSICAL_GAMES_2 = 20;

    /**
     * Minimum number of Rapid games required for a certain rating level.
     */
    private static final int RAPID_GAMES_1 = 25;

    /**
     * Minimum number of Rapid games required for a higher rating level.
     */
    private static final int RAPID_GAMES_2 = 30;

    /**
     * Minimum number of Puzzle games required for a certain rating level.
     */
    private static final int PUZZLE_GAMES_1 = 100;

    /**
     * Minimum number of Puzzle games required for a higher rating level.
     */
    private static final int PUZZLE_GAMES_2 = 150;

    /**
     * Rating threshold for Blitz games at level 1.
     */
    private static final int BLITZ_RATING_1 = 1500;

    /**
     * Rating threshold for Blitz games at level 2.
     */
    private static final int BLITZ_RATING_2 = 1550;

    /**
     * Rating threshold for Bullet games at level 1.
     */
    private static final int BULLET_RATING_1 = 1450;

    /**
     * Rating threshold for Bullet games at level 2.
     */
    private static final int BULLET_RATING_2 = 1480;

    /**
     * Rating threshold for Classical games at level 1.
     */
    private static final int CLASSICAL_RATING_1 = 1600;

    /**
     * Rating threshold for Classical games at level 2.
     */
    private static final int CLASSICAL_RATING_2 = 1650;

    /**
     * Rating threshold for Rapid games at level 1.
     */
    private static final int RAPID_RATING_1 = 1550;

    /**
     * Rating threshold for Rapid games at level 2.
     */
    private static final int RAPID_RATING_2 = 1600;

    /**
     * Rating threshold for Puzzle games at level 1.
     */
    private static final int PUZZLE_RATING_1 = 1200;

    /**
     * Rating threshold for Puzzle games at level 2.
     */
    private static final int PUZZLE_RATING_2 = 1250;

    /**
     * Rating deviation for Blitz games at level 1.
     */
    private static final int BLITZ_RD_1 = 30;

    /**
     * Rating deviation for Blitz games at level 2.
     */
    private static final int BLITZ_RD_2 = 20;

    /**
     * Rating deviation for Bullet games at level 1.
     */
    private static final int BULLET_RD_1 = 25;

    /**
     * Rating deviation for Bullet games at level 2.
     */
    private static final int BULLET_RD_2 = 22;

    /**
     * Rating deviation for Classical games at level 1.
     */
    private static final int CLASSICAL_RD_1 = 15;

    /**
     * Rating deviation for Classical games at level 2.
     */
    private static final int CLASSICAL_RD_2 = 12;

    /**
     * Rating deviation for Rapid games at level 1.
     */
    private static final int RAPID_RD_1 = 20;

    /**
     * Rating deviation for Rapid games at level 2.
     */
    private static final int RAPID_RD_2 = 18;

    /**
     * Rating deviation for Puzzle games at level 1.
     */
    private static final int PUZZLE_RD_1 = 10;

    /**
     * Rating deviation for Puzzle games at level 2.
     */
    private static final int PUZZLE_RD_2 = 15;

    /**
     * Progress percentage for Blitz games at level 1.
     */
    private static final int BLITZ_PROG_1 = 20;

    /**
     * Progress percentage for Blitz games at level 2.
     */
    private static final int BLITZ_PROG_2 = 30;

    /**
     * Progress percentage for Bullet games at level 1.
     */
    private static final int BULLET_PROG_1 = 10;

    /**
     * Progress percentage for Bullet games at level 2.
     */
    private static final int BULLET_PROG_2 = 15;

    /**
     * Progress percentage for Classical games at level 1.
     */
    private static final int CLASSICAL_PROG_1 = 5;

    /**
     * Progress percentage for Classical games at level 2.
     */
    private static final int CLASSICAL_PROG_2 = 10;

    /**
     * Progress percentage for Rapid games at level 1.
     */
    private static final int RAPID_PROG_1 = 15;

    /**
     * Progress percentage for Rapid games at level 2.
     */
    private static final int RAPID_PROG_2 = 20;

    /**
     * Progress percentage for Puzzle games at level 1.
     */
    private static final int PUZZLE_PROG_1 = 50;

    /**
     * Progress percentage for Puzzle games at level 2.
     */
    private static final int PUZZLE_PROG_2 = 70;

    /**
     * Blitz games at level 1 are not provisional.
     */
    private static final boolean BLITZ_PROV_1 = false;

    /**
     * Blitz games at level 2 are not provisional.
     */
    private static final boolean BLITZ_PROV_2 = false;

    /**
     * Bullet games at level 1 are provisional.
     */
    private static final boolean BULLET_PROV_1 = true;

    /**
     * Bullet games at level 2 are provisional.
     */
    private static final boolean BULLET_PROV_2 = true;

    /**
     * Classical games at level 1 are not provisional.
     */
    private static final boolean CLASSICAL_PROV_1 = false;

    /**
     * Classical games at level 2 are not provisional.
     */
    private static final boolean CLASSICAL_PROV_2 = false;

    /**
     * Rapid games at level 1 are provisional.
     */
    private static final boolean RAPID_PROV_1 = true;

    /**
     * Rapid games at level 2 are provisional.
     */
    private static final boolean RAPID_PROV_2 = true;

    /**
     * Puzzle games at level 1 are not provisional.
     */
    private static final boolean PUZZLE_PROV_1 = false;

    /**
     * Puzzle games at level 2 are not provisional.
     */
    private static final boolean PUZZLE_PROV_2 = false;
    /**
     * The {@link DefaultLichessService} instance to be tested. This service
     * provides methods to interact with Lichess data and handle authorization.
     */
    @InjectMocks
    private DefaultLichessService lichessService;

    /**
     * Mocked repository for handling Lichess authentication data.
     */
    @Mock
    private LichessAuthRepository lichessAuthRepository;

    /**
     * Mocked repository for handling OAuth authorization request data.
     */
    @Mock
    private OAuthAuthorizationRequestRepository oAuthAuthorizationRequestRepository;

    /**
     * Mocked repository for handling user entity data.
     */
    @Mock
    private UserEntityRepository userEntityRepository;

    /**
     * Mocked repository for handling Lichess entity data.
     */
    @Mock
    private LichessEntityRepository lichessEntityRepository;

    /**
     * Test instance of {@link PersistentUserEntity} to simulate a user entity.
     */
    private PersistentUserEntity userEntity;

    /**
     * Test instance of {@link PersistentLichessAuthEntity} to simulate Lichess
     * authorization data.
     */
    private PersistentLichessAuthEntity authEntity;

    /**
     * Test instance of {@link PersistentOAuthAuthorizationRequestEntity} to
     * simulate OAuth authorization request.
     */
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
    void testGetCodeVerifierSuccess() throws LichessServiceException {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        when(oAuthAuthorizationRequestRepository.findById(userEntity.getDni()))
                .thenReturn(Optional.of(authRequestEntity));

        String codeVerifier = lichessService.getCodeVerifier(userEntity.getEmail());

        assertEquals("verifier123", codeVerifier);
        verify(userEntityRepository).findByEmail(userEntity.getEmail());
        verify(oAuthAuthorizationRequestRepository).findById(userEntity.getDni());
    }

    @Test
    void testGetCodeVerifierThrowsExceptionWhenOAuthRequestNotFound() {
        String userEmail = "email1@email.es";

        // Mocking a user entity
        PersistentUserEntity usrEntity = new PersistentUserEntity();
        usrEntity.setDni("123456");
        usrEntity.setEmail(userEmail);

        // Mocking the repository to return a valid user
        when(userEntityRepository.findByEmail(userEmail)).thenReturn(Optional.of(usrEntity));

        // Mocking the oAuthAuthorizationRequestRepository to return an empty Optional
        when(oAuthAuthorizationRequestRepository.findById(usrEntity.getDni())).thenReturn(Optional.empty());

        // Act & Assert
        LichessServiceException exception = assertThrows(LichessServiceException.class, () -> {
            lichessService.getCodeVerifier(userEmail);
        });

        // Assert that the exception contains the correct message
        assertEquals("User with dni: 123456 no have OAuthRequest.", exception.getMessage());
    }

    @Test
    void testGetCodeVerifierUserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.getCodeVerifier(userEntity.getEmail()));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testGetLichessProfileProfileDoesNotExist() throws LichessServiceException {
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
    void testGetLichessProfileProfileExists() throws LichessServiceException {
        // Arrange
        String userEmail = "test@example.com";
        String userDni = userEntity.getDni();

        // Constants for test values
        final String profileId = "profileId";
        final String username = "testUser";
        final int blitzGames = 10;
        final int blitzRating = 1500;
        final int blitzRd = 30;
        final int blitzProg = 0;
        final boolean blitzProv = false;

        // Create a mock Lichess profile entity
        PersistentLichessProfileEntity lichessProfileEntity = new PersistentLichessProfileEntity();
        lichessProfileEntity.setIdentifier(profileId);
        lichessProfileEntity.setUsername(username);
        lichessProfileEntity.setUpdatedAt(LocalDateTime.now());
        lichessProfileEntity.setBlitzGames(blitzGames);
        lichessProfileEntity.setBlitzRating(blitzRating);
        lichessProfileEntity.setBlitzRd(blitzRd);
        lichessProfileEntity.setBlitzProg(blitzProg);
        lichessProfileEntity.setBlitzProv(blitzProv);

        // Mocking
        when(userEntityRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(lichessEntityRepository.findById(userDni)).thenReturn(Optional.of(lichessProfileEntity));

        // Act
        LichessProfileDto result = lichessService.getLichessProfile(userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(lichessProfileEntity.getUsername(), result.username());
        assertEquals(lichessProfileEntity.getUpdatedAt(), result.updatedAt());
        assertEquals(blitzGames, result.blitz().games());
        assertEquals(blitzRating, result.blitz().rating());
    }

    @Test
    void testGetLichessProfiles() {
        // Sample Lichess profile entities
        List<PersistentLichessProfileEntity> lichessProfilesEntitiesList = new ArrayList<>();

        // Creating the first Lichess profile entity
        PersistentLichessProfileEntity profile1 = new PersistentLichessProfileEntity();
        profile1.setUserDni("123456");
        profile1.setIdentifier("lichessId1");
        profile1.setUsername("user1");
        profile1.setBlitzGames(BLITZ_GAMES_1);
        profile1.setBlitzRating(BLITZ_RATING_1);
        profile1.setBlitzRd(BLITZ_RD_1);
        profile1.setBlitzProg(BLITZ_PROG_1);
        profile1.setBlitzProv(BLITZ_PROV_1);
        profile1.setBulletGames(BULLET_GAMES_1);
        profile1.setBulletRating(BULLET_RATING_1);
        profile1.setBulletRd(BULLET_RD_1);
        profile1.setBulletProg(BULLET_PROG_1);
        profile1.setBulletProv(BULLET_PROV_1);
        profile1.setClassicalGames(CLASSICAL_GAMES_1);
        profile1.setClassicalRating(CLASSICAL_RATING_1);
        profile1.setClassicalRd(CLASSICAL_RD_1);
        profile1.setClassicalProg(CLASSICAL_PROG_1);
        profile1.setClassicalProv(CLASSICAL_PROV_1);
        profile1.setRapidGames(RAPID_GAMES_1);
        profile1.setRapidRating(RAPID_RATING_1);
        profile1.setRapidRd(RAPID_RD_1);
        profile1.setRapidProg(RAPID_PROG_1);
        profile1.setRapidProv(RAPID_PROV_1);
        profile1.setPuzzleGames(PUZZLE_GAMES_1);
        profile1.setPuzzleRating(PUZZLE_RATING_1);
        profile1.setPuzzleRd(PUZZLE_RD_1);
        profile1.setPuzzleProg(PUZZLE_PROG_1);
        profile1.setPuzzleProv(PUZZLE_PROV_1);
        profile1.setUpdatedAt(LocalDateTime.now());

        // Creating the second Lichess profile entity
        PersistentLichessProfileEntity profile2 = new PersistentLichessProfileEntity();
        profile2.setUserDni("654321");
        profile2.setIdentifier("lichessId2");
        profile2.setUsername("user2");
        profile2.setBlitzGames(BLITZ_GAMES_2);
        profile2.setBlitzRating(BLITZ_RATING_2);
        profile2.setBlitzRd(BLITZ_RD_2);
        profile2.setBlitzProg(BLITZ_PROG_2);
        profile2.setBlitzProv(BLITZ_PROV_2);
        profile2.setBulletGames(BULLET_GAMES_2);
        profile2.setBulletRating(BULLET_RATING_2);
        profile2.setBulletRd(BULLET_RD_2);
        profile2.setBulletProg(BULLET_PROG_2);
        profile2.setBulletProv(BULLET_PROV_2);
        profile2.setClassicalGames(CLASSICAL_GAMES_2);
        profile2.setClassicalRating(CLASSICAL_RATING_2);
        profile2.setClassicalRd(CLASSICAL_RD_2);
        profile2.setClassicalProg(CLASSICAL_PROG_2);
        profile2.setClassicalProv(CLASSICAL_PROV_2);
        profile2.setRapidGames(RAPID_GAMES_2);
        profile2.setRapidRating(RAPID_RATING_2);
        profile2.setRapidRd(RAPID_RD_2);
        profile2.setRapidProg(RAPID_PROG_2);
        profile2.setRapidProv(RAPID_PROV_2);
        profile2.setPuzzleGames(PUZZLE_GAMES_2);
        profile2.setPuzzleRating(PUZZLE_RATING_2);
        profile2.setPuzzleRd(PUZZLE_RD_2);
        profile2.setPuzzleProg(PUZZLE_PROG_2);
        profile2.setPuzzleProv(PUZZLE_PROV_2);
        profile2.setUpdatedAt(LocalDateTime.now());

        lichessProfilesEntitiesList.add(profile1);
        lichessProfilesEntitiesList.add(profile2);

        // Mocking the repository methods
        when(lichessEntityRepository.findAll()).thenReturn(lichessProfilesEntitiesList);

        // Mock the userEntityRepository responses
        when(userEntityRepository.findByDni("123456")).thenReturn(Optional.of(new PersistentUserEntity()));
        when(userEntityRepository.findByDni("654321")).thenReturn(Optional.of(new PersistentUserEntity()));

        // Act
        List<LichessProfileDto> result = lichessService.getLichessProfiles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Check that we got 2 profiles in the result
    }

    @Test
    void testGetLichessProfileUserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.getLichessProfile(userEntity.getEmail()));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testSaveAuthTokenSuccess() throws LichessServiceException {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        lichessService.saveAuthToken("Bearer", "token123", LocalDateTime.now().plusHours(1), userEntity.getEmail());

        verify(lichessAuthRepository).save(any(PersistentLichessAuthEntity.class));
        verify(userEntityRepository).save(userEntity);
    }

    @Test
    void testSaveAuthTokenUserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class, () -> lichessService
                .saveAuthToken("Bearer", "token123", LocalDateTime.now().plusHours(1), userEntity.getEmail()));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testSaveLichessProfileSuccess() throws LichessServiceException {
        // Declare variables to avoid magic numbers
        final int blitzGames = 10;
        final int blitzRating = 1500;
        final int blitzRd = 30;
        final int blitzProg = 0;
        final boolean blitzProv = false;

        final int bulletGames = 20;
        final int bulletRating = 1400;
        final int bulletRd = 40;
        final int bulletProg = 0;
        final boolean bulletProv = false;

        final int classicalGames = 30;
        final int classicalRating = 1600;
        final int classicalRd = 50;
        final int classicalProg = 0;
        final boolean classicalProv = false;

        final int rapidGames = 40;
        final int rapidRating = 1300;
        final int rapidRd = 60;
        final int rapidProg = 0;
        final boolean rapidProv = false;

        final int puzzleGames = 50;
        final int puzzleRating = 1200;
        final int puzzleRd = 70;
        final int puzzleProg = 0;
        final boolean puzzleProv = false;

        // Create an instance of SaveGameStatistics for each game type using the
        // declared variables
        SaveGameStatistics blitzStats = new SaveGameStatistics(blitzGames, blitzRating, blitzRd, blitzProg, blitzProv);
        SaveGameStatistics bulletStats = new SaveGameStatistics(bulletGames, bulletRating, bulletRd, bulletProg,
                bulletProv);
        SaveGameStatistics classicalStats = new SaveGameStatistics(classicalGames, classicalRating, classicalRd,
                classicalProg, classicalProv);
        SaveGameStatistics rapidStats = new SaveGameStatistics(rapidGames, rapidRating, rapidRd, rapidProg, rapidProv);
        SaveGameStatistics puzzleStats = new SaveGameStatistics(puzzleGames, puzzleRating, puzzleRd, puzzleProg,
                puzzleProv);

        // Create the DTO with proper statistics
        LichessSaveProfileDto dto = new LichessSaveProfileDto("test@example.com", "profileId", "testUser",
                LocalDateTime.now(), blitzStats, bulletStats, classicalStats, rapidStats, puzzleStats);

        // Mock the user repository to return the user entity
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        // Mock the lichess entity repository to return the saved entity
        PersistentLichessProfileEntity savedEntity = new PersistentLichessProfileEntity();
        savedEntity.setIdentifier("profileId");
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
        assertEquals("profileId", result.getIdentifier());
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
    void testSaveLichessProfileUserNotFound() {
        LichessSaveProfileDto dto = new LichessSaveProfileDto("test@example.com", "profileId", "testUser",
                LocalDateTime.now(), null, null, null, null, null);

        when(userEntityRepository.findByEmail(dto.userEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.saveLichessProfile(dto));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testSaveOAuthRequestSuccess() throws LichessServiceException {
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
    void testSaveOAuthRequestUserNotFound() {
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        LichessServiceException thrown = assertThrows(LichessServiceException.class,
                () -> lichessService.saveOAuthRequest(userEntity.getEmail(), "verifier123"));

        assertEquals("User with email: test@example.com not found.", thrown.getMessage());
    }

}
