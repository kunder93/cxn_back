package es.org.cxn.backapp.model;

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
