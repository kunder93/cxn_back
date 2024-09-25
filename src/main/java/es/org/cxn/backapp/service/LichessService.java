package es.org.cxn.backapp.service;

import java.time.LocalDateTime;

import es.org.cxn.backapp.exceptions.LichessServiceException;
import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.PersistentLichessProfileEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.service.dto.CreateLichessProfileDto;

/**
 * Interface for Lichess user authentication processes.
 */
public interface LichessService {

    /**
     * Get user code verifier.
     *
     * @param userEmail The user email, user identifier.
     * @return The code verifier.
     * @throws LichessServiceException When user with provided email not found.
     */
    String getCodeVerifier(String userEmail) throws LichessServiceException;

    /**
     * @param userEmail
     * @return
     * @throws Exception
     */
    PersistentLichessProfileEntity getLichessProfile(String userEmail) throws Exception;

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
    void saveAuthToken(String tokenType, String accessToken, LocalDateTime expirationDate, String userEmail)
            throws LichessServiceException;

    /**
     * Save lichess authorization.
     *
     * @param lichessAuth The lichess authorization entity.
     * @return Stored lichess authorization entity.
     */
    PersistentLichessAuthEntity saveLichessAuth(PersistentLichessAuthEntity lichessAuth);

    /**
     * @param lichessProfileDto The dto with Lichess profile data.
     * @return The Lichess profile entity stored.
     * @throws LichessServiceException When user not found.
     */
    PersistentLichessProfileEntity saveLichessProfile(CreateLichessProfileDto lichessProfileDto)
            throws LichessServiceException;

    /**
     * Save authorization code request from user.
     *
     * @param userEmail    The user email, user identifier.
     * @param codeVerifier The authorization code used to request token.
     * @return The Authorization entity stored.
     * @throws LichessServiceException When user with provided email not found.
     */
    PersistentOAuthAuthorizationRequestEntity saveOAuthRequest(String userEmail, String codeVerifier)
            throws LichessServiceException;

}
