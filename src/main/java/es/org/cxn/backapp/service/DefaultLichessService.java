package es.org.cxn.backapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.exceptions.LichessServiceException;
import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.PersistentLichessProfileEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.LichessAuthRepository;
import es.org.cxn.backapp.repository.LichessEntityRepository;
import es.org.cxn.backapp.repository.OAuthAuthorizationRequestRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.dto.LichessProfileDto;
import es.org.cxn.backapp.service.dto.LichessSaveProfileDto;
import jakarta.transaction.Transactional;

/**
 * Service for manage Authorization and Authentication of lichess profiles.
 */
@Service
public class DefaultLichessService implements LichessService {

    /**
     * The lichess authentication repository.
     */
    private final LichessAuthRepository lichessAuthRepository;

    /**
     * The lichess authorization request repository.
     */
    private final OAuthAuthorizationRequestRepository oAuthAuthorizationRequestRepository;

    /**
     * The user entity repository.
     */
    private final UserEntityRepository userEntityRepository;

    /**
     * The lichess user profile repository.
     */
    private final LichessEntityRepository lichessEntityRepository;

    /**
     * Builds this service. Main constructor.
     *
     * @param lichessAuthRepo               The Lichess authentication repository.
     * @param oAuthAuthorizationRequestRepo The Lichess authorization repository.
     * @param userEntityRepo                The user entity repository.
     * @param lichessEntityRepo             The lichess profile entity repository.
     */
    public DefaultLichessService(final LichessAuthRepository lichessAuthRepo,
            final OAuthAuthorizationRequestRepository oAuthAuthorizationRequestRepo,
            final UserEntityRepository userEntityRepo, final LichessEntityRepository lichessEntityRepo) {
        lichessAuthRepository = lichessAuthRepo;
        oAuthAuthorizationRequestRepository = oAuthAuthorizationRequestRepo;
        userEntityRepository = userEntityRepo;
        lichessEntityRepository = lichessEntityRepo;
    }

    /**
     * Retrieves the authentication token for a user based on their email address.
     *
     * This method first checks if the user's authentication token has expired. If
     * the token is expired, it throws a {@link LichessServiceException} with the
     * message "Token is expired." If the token is valid, it returns the access
     * token associated with the user.
     *
     * @param userEmail the email address of the user whose token is to be retrieved
     * @return the authentication token of the user
     * @throws LichessServiceException if the token is expired or if there is an
     *                                 error retrieving the token
     */
    @Override
    public String getAuthToken(final String userEmail) throws LichessServiceException {
        final var userEntity = getUserByEmail(userEmail);
        final var userAuth = userEntity.getLichessAuth();
        if (userAuth == null) {
            throw new LichessServiceException("User with email: " + userEmail + " no have lichess auth.");
        }
        if (userAuth.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new LichessServiceException(
                    "Token is expired. Tooken date is: " + userAuth.getExpirationDate().toString());
        } else {
            return userAuth.getAccessToken();
        }
    }

    /**
     * Get user code verifier.
     *
     * @param userEmail The user email, user identifier.
     * @return The code verifier.
     * @throws LichessServiceException When user with provided email not found.
     */
    @Override
    public String getCodeVerifier(final String userEmail) throws LichessServiceException {
        final var userEntity = getUserByEmail(userEmail);
        final var oAuthAuthorizationRequest = oAuthAuthorizationRequestRepository.findById(userEntity.getDni());
        if (oAuthAuthorizationRequest.isEmpty()) {
            throw new LichessServiceException("User with dni: " + userEntity.getDni() + " no have OAuthRequest.");
        }
        return oAuthAuthorizationRequest.get().getCodeVerifier();
    }

    /**
     * Get Lichess profile using user email to locate it.
     *
     * @throws LichessServiceException When user with given user email not found.
     */
    @Override
    public LichessProfileDto getLichessProfile(final String userEmail) throws LichessServiceException {
        final var userEntity = getUserByEmail(userEmail);
        final String userDni = userEntity.getDni();

        final var lichessProfileOptional = lichessEntityRepository.findById(userDni);
        if (lichessProfileOptional.isEmpty()) {
            // estadísticas vacías
            final LichessProfileDto.GameStatistics emptyStats = new LichessProfileDto.GameStatistics(0, 0, 0, 0, false);

            return new LichessProfileDto(userEntity.getCompleteName(), "", "", LocalDateTime.now(), emptyStats,
                    emptyStats, emptyStats, emptyStats, emptyStats);

        }

        final var lichessProfileEntity = lichessProfileOptional.get();

        // Map params for each kind of game.
        final LichessProfileDto.GameStatistics blitzStats = new LichessProfileDto.GameStatistics(
                lichessProfileEntity.getBlitzGames(), lichessProfileEntity.getBlitzRating(),
                lichessProfileEntity.getBlitzRd(), lichessProfileEntity.getBlitzProg(),
                lichessProfileEntity.getBlitzProv());

        final LichessProfileDto.GameStatistics bulletStats = new LichessProfileDto.GameStatistics(
                lichessProfileEntity.getBulletGames(), lichessProfileEntity.getBulletRating(),
                lichessProfileEntity.getBulletRd(), lichessProfileEntity.getBulletProg(),
                lichessProfileEntity.getBulletProv());

        final LichessProfileDto.GameStatistics classicalStats = new LichessProfileDto.GameStatistics(
                lichessProfileEntity.getClassicalGames(), lichessProfileEntity.getClassicalRating(),
                lichessProfileEntity.getClassicalRd(), lichessProfileEntity.getClassicalProg(),
                lichessProfileEntity.getClassicalProv());

        final LichessProfileDto.GameStatistics rapidStats = new LichessProfileDto.GameStatistics(
                lichessProfileEntity.getRapidGames(), lichessProfileEntity.getRapidRating(),
                lichessProfileEntity.getRapidRd(), lichessProfileEntity.getRapidProg(),
                lichessProfileEntity.getRapidProv());

        final LichessProfileDto.GameStatistics puzzleStats = new LichessProfileDto.GameStatistics(
                lichessProfileEntity.getPuzzleGames(), lichessProfileEntity.getPuzzleRating(),
                lichessProfileEntity.getPuzzleRd(), lichessProfileEntity.getPuzzleProg(),
                lichessProfileEntity.getPuzzleProv());

        return new LichessProfileDto(userEntity.getCompleteName(), lichessProfileEntity.getId(),
                lichessProfileEntity.getUsername(), lichessProfileEntity.getUpdatedAt(), blitzStats, bulletStats,
                classicalStats, rapidStats, puzzleStats);
    }

    /**
     * Get all lichess profiles and return data as List of dto.
     */
    @Override
    public List<LichessProfileDto> getLichessProfiles() {
        final List<PersistentLichessProfileEntity> entitiesList = lichessEntityRepository.findAll();
        final List<LichessProfileDto> dtoList = new ArrayList<>();

        entitiesList.forEach((PersistentLichessProfileEntity entity) -> {
            final var user = userEntityRepository.findByDni(entity.getUserDni());
            final var userEntity = user.get();

            // Map statistics from entity to GameStatistics DTO
            final LichessProfileDto.GameStatistics blitzStats = new LichessProfileDto.GameStatistics(
                    entity.getBlitzGames(), entity.getBlitzRating(), entity.getBlitzRd(), entity.getBlitzProg(),
                    entity.getBlitzProv());

            final LichessProfileDto.GameStatistics bulletStats = new LichessProfileDto.GameStatistics(
                    entity.getBulletGames(), entity.getBulletRating(), entity.getBulletRd(), entity.getBulletProg(),
                    entity.getBulletProv());

            final LichessProfileDto.GameStatistics classicalStats = new LichessProfileDto.GameStatistics(
                    entity.getClassicalGames(), entity.getClassicalRating(), entity.getClassicalRd(),
                    entity.getClassicalProg(), entity.getClassicalProv());

            final LichessProfileDto.GameStatistics rapidStats = new LichessProfileDto.GameStatistics(
                    entity.getRapidGames(), entity.getRapidRating(), entity.getRapidRd(), entity.getRapidProg(),
                    entity.getRapidProv());

            final LichessProfileDto.GameStatistics puzzleStats = new LichessProfileDto.GameStatistics(
                    entity.getPuzzleGames(), entity.getPuzzleRating(), entity.getPuzzleRd(), entity.getPuzzleProg(),
                    entity.getPuzzleProv());

            // Create LichessProfileDto
            dtoList.add(new LichessProfileDto(userEntity.getCompleteName(), entity.getId(), entity.getUsername(),
                    entity.getUpdatedAt(), blitzStats, bulletStats, classicalStats, rapidStats, puzzleStats));
        });

        return dtoList;
    }

    /**
     * Find user entity with provided email.
     *
     * @param userEmail The user email.
     * @return The user entity with provided email.
     * @throws LichessServiceException If no user found.
     */
    private PersistentUserEntity getUserByEmail(final String userEmail) throws LichessServiceException {
        final var userOptional = userEntityRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new LichessServiceException("User with email: " + userEmail + " not found.");
        }
        return userOptional.get();
    }

    /**
     * Save Authorization token with other data related to user.
     *
     * @param tokenType      The kind of token.
     * @param accessToken    The token itself.
     * @param expirationDate The date when token should not be valid.
     * @param userEmail      The user email, user identifier.
     * @throws LichessServiceException When cannot be added cause user with provided
     *                                 email not found.
     */
    @Transactional
    @Override
    public void saveAuthToken(final String tokenType, final String accessToken, final LocalDateTime expirationDate,
            final String userEmail) throws LichessServiceException {
        final var userEntity = getUserByEmail(userEmail);

        final PersistentLichessAuthEntity authEntity = new PersistentLichessAuthEntity();
        authEntity.setCreatedAt(LocalDateTime.now());
        authEntity.setScope("tmpScope");
        authEntity.setState("tmpState");
        authEntity.setTokenType(tokenType);

        authEntity.setUserDni(userEntity.getDni());
        authEntity.setAccessToken(accessToken);
        authEntity.setExpirationDate(expirationDate);

        final var savedAuthEntity = lichessAuthRepository.save(authEntity);

        userEntity.setLichessAuth(savedAuthEntity);
        userEntityRepository.save(userEntity);
    }

    /**
     * Save lichess authorization.
     *
     * @param lichessAuth The lichess authorization entity.
     * @return Stored lichess authorization entity.
     */
    @Override
    public PersistentLichessAuthEntity saveLichessAuth(final PersistentLichessAuthEntity lichessAuth) {
        return lichessAuthRepository.save(lichessAuth);
    }

    /**
     *
     * @param dto The dto with profile data params.
     * @return The Lichess profile entity stored.
     * @throws LichessServiceException When user with provided email not found.
     */
    @Override
    public PersistentLichessProfileEntity saveLichessProfile(final LichessSaveProfileDto dto)
            throws LichessServiceException {
        final PersistentLichessProfileEntity entity = new PersistentLichessProfileEntity();
        final var userEntity = getUserByEmail(dto.userEmail());

        // Map main fields
        entity.setUserDni(userEntity.getDni());
        entity.setId(dto.id());
        entity.setUsername(dto.username());
        entity.setUpdatedAt(LocalDateTime.now());

        // Map Blitz statistics
        entity.setBlitzGames(dto.blitz().games());
        entity.setBlitzRating(dto.blitz().rating());
        entity.setBlitzRd(dto.blitz().rd());
        entity.setBlitzProg(dto.blitz().prog());
        entity.setBlitzProv(dto.blitz().prov());

        // Map Bullet statistics
        entity.setBulletGames(dto.bullet().games());
        entity.setBulletRating(dto.bullet().rating());
        entity.setBulletRd(dto.bullet().rd());
        entity.setBulletProg(dto.bullet().prog());
        entity.setBulletProv(dto.bullet().prov());

        // Map Classical statistics
        entity.setClassicalGames(dto.classical().games());
        entity.setClassicalRating(dto.classical().rating());
        entity.setClassicalRd(dto.classical().rd());
        entity.setClassicalProg(dto.classical().prog());
        entity.setClassicalProv(dto.classical().prov());

        // Map Rapid statistics
        entity.setRapidGames(dto.rapid().games());
        entity.setRapidRating(dto.rapid().rating());
        entity.setRapidRd(dto.rapid().rd());
        entity.setRapidProg(dto.rapid().prog());
        entity.setRapidProv(dto.rapid().prov());

        // Map Puzzle statistics
        entity.setPuzzleGames(dto.puzzle().games());
        entity.setPuzzleRating(dto.puzzle().rating());
        entity.setPuzzleRd(dto.puzzle().rd());
        entity.setPuzzleProg(dto.puzzle().prog());
        entity.setPuzzleProv(dto.puzzle().prov());

        // Save the entity to the repository
        return lichessEntityRepository.save(entity);
    }

    /**
     * Save authorization code request from user.
     *
     * @param userEmail    The user email, user identifier.
     * @param codeVerifier The authorization code used to request token.
     * @return The Authorization entity stored.
     * @throws LichessServiceException When user with provided email not found.
     */
    @Override
    public PersistentOAuthAuthorizationRequestEntity saveOAuthRequest(final String userEmail, final String codeVerifier)
            throws LichessServiceException {
        final var userEntity = getUserByEmail(userEmail);
        final var oAuthAuthorizationRequestEntity = new PersistentOAuthAuthorizationRequestEntity();
        oAuthAuthorizationRequestEntity.setUserDni(userEntity.getDni());
        oAuthAuthorizationRequestEntity.setCodeVerifier(codeVerifier);
        oAuthAuthorizationRequestEntity.setCreatedAt(LocalDateTime.now());
        oAuthAuthorizationRequestEntity.setState("tmpState");
        final var savedOAuth = oAuthAuthorizationRequestRepository.save(oAuthAuthorizationRequestEntity);
        userEntity.setOauthAuthorizationRequest(savedOAuth);

        userEntityRepository.save(userEntity);
        return savedOAuth;
    }

}