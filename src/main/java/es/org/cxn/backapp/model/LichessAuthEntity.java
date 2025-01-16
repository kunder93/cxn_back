package es.org.cxn.backapp.model;

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

import java.io.Serializable;
import java.time.LocalDateTime;

import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;

/**
 * Interface for Lichess Authentication Entity.
 * <p>
 * This interface provides methods to access and modify the Lichess
 * authentication data such as access token, expiration time, scope, state, and
 * token type.
 * </p>
 */
public interface LichessAuthEntity extends Serializable {

    /**
     * Gets the Lichess access token.
     *
     * @return the access token as a {@link String}
     */
    String getAccessToken();

    /**
     * Gets date when auth was created.
     *
     * @return The time when token has been created.
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets the token expiration date time.
     *
     * @return the expiration date time {@link LocalDateTime}
     */
    LocalDateTime getExpirationDate();

    /**
     * Gets the scope of the Lichess authentication.
     *
     * @return the scope as a {@link String}
     */
    String getScope();

    /**
     * Gets the state of the Lichess authentication.
     *
     * @return the state as a {@link String}
     */
    String getState();

    /**
     * Gets the token type of the Lichess authentication.
     *
     * @return the token type as a {@link String}
     */
    String getTokenType();

    /**
     * Get the token owner.
     *
     * @return The associated user entity.
     */
    PersistentUserEntity getUser();

    /**
     * Gets the token's user dni.
     *
     * @return user dni.
     */
    String getUserDni();

    /**
     * Sets the Lichess access token.
     *
     * @param accessToken the access token to set
     */
    void setAccessToken(String accessToken);

    /**
     * Sets date when token was created.
     *
     * @param createdAt Date when token was created.
     */
    void setCreatedAt(LocalDateTime createdAt);

    /**
     * Sets the expiration date of the Lichess token.
     *
     * @param expirationDate the expiration date.
     */
    void setExpirationDate(LocalDateTime expirationDate);

    /**
     * Sets the scope of the Lichess authentication.
     *
     * @param scope the scope to set
     */
    void setScope(String scope);

    /**
     * Sets the state of the Lichess authentication.
     *
     * @param state the state to set
     */
    void setState(String state);

    /**
     * Sets the token type of the Lichess authentication.
     *
     * @param tokenType the token type to set
     */
    void setTokenType(String tokenType);

    /**
     * Set auth token owner.
     *
     * @param user The token owner.
     */
    void setUser(PersistentUserEntity user);

    /**
     * Sets the user's token dni.
     *
     * @param userDni The user dni.
     */
    void setUserDni(String userDni);
}
