package es.org.cxn.backapp.service;

import java.time.LocalDateTime;

import es.org.cxn.backapp.exceptions.LichessAuthServiceException;
import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;

/**
 * Interface for Lichess user authentication processes.
 */
public interface LichessAuthService {

    /**
     * Get user code verifier.
     *
     * @param userEmail The user email, user identifier.
     * @return The code verifier.
     * @throws LichessAuthServiceException When user with provided email not found.
     */
    String getCodeVerifier(String userEmail) throws LichessAuthServiceException;

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
    void saveAuthToken(String tokenType, String accessToken, LocalDateTime expirationDate, String userEmail)
            throws LichessAuthServiceException;

    /**
     * Save lichess authorization.
     *
     * @param lichessAuth The lichess authorization entity.
     * @return Stored lichess authorization entity.
     */
    PersistentLichessAuthEntity saveLichessAuth(PersistentLichessAuthEntity lichessAuth);

    /**
     * Save authorization code request from user.
     *
     * @param userEmail    The user email, user identifier.
     * @param codeVerifier The authorization code used to request token.
     * @return The Authorization entity stored.
     * @throws LichessAuthServiceException When user with provided email not found.
     */
    PersistentOAuthAuthorizationRequestEntity saveOAuthRequest(String userEmail, String codeVerifier)
            throws LichessAuthServiceException;

}
