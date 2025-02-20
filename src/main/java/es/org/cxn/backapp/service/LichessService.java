package es.org.cxn.backapp.service;

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

import java.time.LocalDateTime;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.PersistentLichessProfileEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.service.dto.LichessProfileDto;
import es.org.cxn.backapp.service.dto.LichessSaveProfileDto;
import es.org.cxn.backapp.service.exceptions.LichessServiceException;

/**
 * Interface for Lichess user authentication processes.
 */
public interface LichessService {

    /**
     * Retrieves the authentication token for a user based on their email address.
     *
     * @param userEmail the email address of the user whose token is to be retrieved
     * @return the authentication token of the user
     * @throws LichessServiceException if there is an error retrieving the token or
     *                                 is expired
     */
    String getAuthToken(String userEmail) throws LichessServiceException;

    /**
     * Get user code verifier.
     *
     * @param userEmail The user email, user identifier.
     * @return The code verifier.
     * @throws LichessServiceException When user with provided email not found.
     */
    String getCodeVerifier(String userEmail) throws LichessServiceException;

    /**
     * Get data from stored Lichess profile.
     *
     * @param userEmail The user email.
     * @return The lichess profile associated to userEmail.
     * @throws LichessServiceException When user with given email not found.
     */
    LichessProfileDto getLichessProfile(String userEmail) throws LichessServiceException;

    /**
     * Get user lichess profile using dni identifier.
     *
     * @param userDni The user dni identifier.
     * @return Profile dto with lichess data.
     * @throws LichessServiceException When user with given dni not found.
     */
    LichessProfileDto getLichessProfileByDni(String userDni) throws LichessServiceException;

    /**
     * Get all lichess profiles and return dto with data.
     *
     * @return Dto with all lichess profiles.
     */
    List<LichessProfileDto> getLichessProfiles();

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
     * Save Lichess linked profile.
     *
     * @param lichessProfileDto The dto with Lichess profile data.
     * @return The Lichess profile entity stored.
     * @throws LichessServiceException When user not found.
     */
    PersistentLichessProfileEntity saveLichessProfile(LichessSaveProfileDto lichessProfileDto)
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
