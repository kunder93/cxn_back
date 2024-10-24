package es.org.cxn.backapp.model.persistence;

import java.time.LocalDateTime;

import es.org.cxn.backapp.model.LichessAuthEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Persistent implementation of the {@link LichessAuthEntity}.
 * <p>
 * This entity represents the Lichess authentication details and is mapped to
 * the `lichess_auth` table in the database.
 * </p>
 */
@Entity
@Table(name = "lichess_auth")
public class PersistentLichessAuthEntity implements LichessAuthEntity {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -4022507450616991721L;

    /**
     * The user token owner.
     */
    @Id
    @Column(name = "user_dni", nullable = false)
    private String userDni;

    /**
     * The user associated with this Lichess authentication entity.
     * <p>
     * This is a one-to-one relationship with the {@link PersistentUserEntity}.
     * </p>
     */
    @OneToOne
    @JoinColumn(name = "user_dni", referencedColumnName = "dni", insertable = false, updatable = false)
    private PersistentUserEntity user;

    /**
     * The Lichess access token.
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * The expiration time of the access token in seconds.
     */
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    /**
     * The scope of the Lichess authentication.
     */
    @Column(name = "scope")
    private String scope;

    /**
     * The type of token received from the Lichess OAuth2 authentication process.
     */
    @Column(name = "token_type")
    private String tokenType;

    /**
     * The state parameter used during the OAuth2 authentication process.
     */
    @Column(name = "state")
    private String state;

    /**
     * Timestamp of when this entity was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getScope() {
        return scope;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTokenType() {
        return tokenType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersistentUserEntity getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserDni() {
        return userDni;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccessToken(final String value) {
        accessToken = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreatedAt(final LocalDateTime value) {
        createdAt = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExpirationDate(final LocalDateTime value) {
        expirationDate = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScope(final String value) {
        scope = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(final String value) {
        state = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTokenType(final String value) {
        tokenType = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUser(final PersistentUserEntity value) {
        user = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserDni(final String value) {
        userDni = value;
    }

}
