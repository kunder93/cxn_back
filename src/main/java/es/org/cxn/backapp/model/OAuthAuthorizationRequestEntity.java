package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Interface representing an OAuth Authorization Request.
 */
public interface OAuthAuthorizationRequestEntity extends Serializable {
    /**
     * Gets the code verifier for the authorization request.
     *
     * @return the code verifier
     */
    String getCodeVerifier();

    /**
     * Gets the creation timestamp of the authorization request.
     *
     * @return the creation timestamp
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets the state of the authorization request.
     *
     * @return the state
     */
    String getState();

    /**
     * Gets the user's DNI associated with the authorization request.
     *
     * @return the user's DNI
     */
    String getUserDni();

    /**
     * Sets the code verifier for the authorization request.
     *
     * @param codeVerifier the code verifier
     */
    void setCodeVerifier(String codeVerifier);

    /**
     * Sets the creation timestamp of the authorization request.
     *
     * @param createdAt the creation timestamp
     */
    void setCreatedAt(LocalDateTime createdAt);

    /**
     * Sets the state of the authorization request.
     *
     * @param state the state
     */
    void setState(String state);

    /**
     * Sets the user's DNI associated with the authorization request.
     *
     * @param userDni the user's DNI
     */
    void setUserDni(String userDni);
}
