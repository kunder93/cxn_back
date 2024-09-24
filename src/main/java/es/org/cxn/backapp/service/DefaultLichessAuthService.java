package es.org.cxn.backapp.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.exceptions.LichessAuthServiceException;
import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.LichessAuthRepository;
import es.org.cxn.backapp.repository.OAuthAuthorizationRequestRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import jakarta.transaction.Transactional;

/**
 * Service for manage Authorization and Authentication of lichess profiles.
 */
@Service
public class DefaultLichessAuthService implements LichessAuthService {

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
     * Builds this service. Main constructor.
     *
     * @param lichessAuthRepo               The Lichess authentication repository.
     * @param oAuthAuthorizationRequestRepo The Lichess authorization repository.
     * @param userEntityRepo                The user entity repository.
     */
    public DefaultLichessAuthService(final LichessAuthRepository lichessAuthRepo,
            final OAuthAuthorizationRequestRepository oAuthAuthorizationRequestRepo,
            final UserEntityRepository userEntityRepo) {
        lichessAuthRepository = lichessAuthRepo;
        oAuthAuthorizationRequestRepository = oAuthAuthorizationRequestRepo;
        userEntityRepository = userEntityRepo;
    }

    /**
     * Get user code verifier.
     *
     * @param userEmail The user email, user identifier.
     * @return The code verifier.
     * @throws LichessAuthServiceException When user with provided email not found.
     */
    @Override
    public String getCodeVerifier(final String userEmail) throws LichessAuthServiceException {
        final var userEntity = getUserByEmail(userEmail);
        final var oAuthAuthorizationRequest = oAuthAuthorizationRequestRepository.findById(userEntity.getDni());
        if (oAuthAuthorizationRequest.isEmpty()) {
            throw new LichessAuthServiceException("User with dni: " + userEntity.getDni() + "no have OAuthRequest.");
        }
        return oAuthAuthorizationRequest.get().getCodeVerifier();
    }

    /**
     * Find user entity with provided email.
     *
     * @param userEmail The user email.
     * @return The user entity with provided email.
     * @throws LichessAuthServiceException If no user found.
     */
    private PersistentUserEntity getUserByEmail(final String userEmail) throws LichessAuthServiceException {
        final var userOptional = userEntityRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new LichessAuthServiceException("User with email: " + userEmail + " not found.");
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
     * @throws LichessAuthServiceException When cannot be added cause user with
     *                                     provided email not found.
     */
    @Transactional
    @Override
    public void saveAuthToken(final String tokenType, final String accessToken, final LocalDateTime expirationDate,
            final String userEmail) throws LichessAuthServiceException {
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
     * Save authorization code request from user.
     *
     * @param userEmail    The user email, user identifier.
     * @param codeVerifier The authorization code used to request token.
     * @return The Authorization entity stored.
     * @throws LichessAuthServiceException When user with provided email not found.
     */
    @Override
    public PersistentOAuthAuthorizationRequestEntity saveOAuthRequest(final String userEmail, final String codeVerifier)
            throws LichessAuthServiceException {
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
