package es.org.cxn.backapp.model.persistence;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.io.Serial;
import java.time.LocalDateTime;

import es.org.cxn.backapp.model.OAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents a persistent OAuth Authorization Request entity.
 */
@Entity
@Table(name = "oauth_authorization_requests")
public class PersistentOAuthAuthorizationRequestEntity implements OAuthAuthorizationRequestEntity {

    /**
     * The serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 3828841937570827646L;

    /**
     * The user's auth owner dni.
     */
    @Id
    @Column(name = "user_dni", nullable = false)
    private String userDni;

    /**
     * User entity owner of this authorization.
     */
    @OneToOne
    @JoinColumn(name = "user_dni", referencedColumnName = "dni", insertable = false, updatable = false)
    private PersistentUserEntity user;

    /**
     * The code verifier used for the OAuth flow.
     */
    @Column(name = "code_verifier")
    private String codeVerifier;

    /**
     * The state of the OAuth authorization request.
     */
    @Column(name = "state")
    private String state;

    /**
     * The timestamp of when the authorization request was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Default constructor for the PersistentOAuthAuthorizationRequestEntity class.
     * <p>
     * This constructor initializes a new instance of the
     * PersistentOAuthAuthorizationRequestEntity class. It is required by JPA and
     * other frameworks that use reflection for object creation.
     * </p>
     */
    public PersistentOAuthAuthorizationRequestEntity() {
        // Default constructor
    }

    /**
     * Gets the code verifier used for the OAuth flow.
     *
     * @return the code verifier.
     */
    @Override
    public String getCodeVerifier() {
        return codeVerifier;
    }

    /**
     * Gets the timestamp of when the authorization request was created.
     *
     * @return the creation timestamp.
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the state of the OAuth authorization request.
     *
     * @return the state.
     */
    @Override
    public String getState() {
        return state;
    }

    /**
     * Gets the DNI of the user associated with this authorization request.
     *
     * @return the user's DNI, or null if no user is associated.
     */
    @Override
    public String getUserDni() {
        return userDni;
    }

    /**
     * Sets the code verifier used for the OAuth flow.
     *
     * @param value the code verifier to set.
     */
    @Override
    public void setCodeVerifier(final String value) {
        codeVerifier = value;
    }

    /**
     * Sets the timestamp of when the authorization request was created.
     *
     * @param value the creation timestamp to set.
     */
    @Override
    public void setCreatedAt(final LocalDateTime value) {
        createdAt = value;
    }

    /**
     * Sets the state of the OAuth authorization request.
     *
     * @param value the state to set.
     */
    @Override
    public void setState(final String value) {
        state = value;
    }

    /**
     * Sets the DNI of the user associated with this authorization request.
     *
     * @param value the user's DNI to set.
     */
    @Override
    public void setUserDni(final String value) {
        userDni = value;
    }
}
