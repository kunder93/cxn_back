package es.org.cxn.backapp.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

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

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.ott.DefaultOneTimeToken;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;

import es.org.cxn.backapp.model.persistence.user.PersistentOneTimeTokenEntity;
import es.org.cxn.backapp.repository.OneTimeTokenEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import jakarta.transaction.Transactional;

/**
 * Custom service for managing One-Time Tokens (OTT) used in authentication.
 *
 * <p>
 * This implementation handles the generation and consumption of one-time
 * tokens, storing them in the database and ensuring their temporal validity.
 * </p>
 *
 * <p>
 * Generated tokens are valid for 15 minutes (900 seconds). Once consumed or
 * expired, they are removed from the repository.
 * </p>
 */
@Service
public class CustomOneTimeTokenService implements OneTimeTokenService {

    /**
     * Duration in seconds that a generated one-time token remains valid. Tokens
     * expire 900 seconds (15 minutes) after creation.
     */
    private static final long TOKEN_VALIDITY_SECONDS = 900;

    /**
     * Repository interface for managing persistent one-time token entities. Used to
     * store, retrieve, and delete one-time tokens in the database.
     */
    private final OneTimeTokenEntityRepository tokenRepository;

    /**
     * Repository interface for managing user entities. Used to look up users when
     * generating one-time tokens.
     */
    private final UserEntityRepository userRepository;

    /**
     * Constructs a {@code CustomOneTimeTokenService} with the specified
     * repositories.
     *
     * @param tokenRepo the repository for managing persistent one-time token
     *                  entities; must not be {@code null}
     * @param userRepo  the repository for managing user entities; must not be
     *                  {@code null}
     * @throws NullPointerException if {@code tokenRepo} or {@code userRepo} is
     *                              {@code null}
     */
    public CustomOneTimeTokenService(final OneTimeTokenEntityRepository tokenRepo,
            final UserEntityRepository userRepo) {
        this.tokenRepository = checkNotNull(tokenRepo, "Received a null pointer as token repository");
        this.userRepository = checkNotNull(userRepo, "Received a null pointer as user repository");
    }

    /**
     * Consumes a one-time token (OTT) to authenticate the user.
     *
     * <p>
     * Validates that the token exists and has not expired. If valid, the token is
     * deleted from the repository to prevent reuse.
     * </p>
     *
     * @param authenticationToken the authentication token containing the one-time
     *                            token
     * @return the consumed token including token value, user identifier, and
     *         expiration time
     * @throws IllegalArgumentException if the token does not exist or has expired
     */
    @Override
    @Transactional
    public OneTimeToken consume(final OneTimeTokenAuthenticationToken authenticationToken) {
        String tokenValue = (String) authenticationToken.getCredentials();

        PersistentOneTimeTokenEntity entity = tokenRepository.findById(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        if (entity.getExpiredAt().isBefore(Instant.now())) {
            tokenRepository.delete(entity); // Eliminar el token expirado
            throw new IllegalArgumentException("Token expired");
        }

        tokenRepository.delete(entity); // Eliminar el token tras su uso
        return new DefaultOneTimeToken(entity.getTokenValue(), entity.getUser().getEmail(), entity.getExpiredAt());
    }

    /**
     * Generates a new one-time token (OTT) for the specified user.
     *
     * <p>
     * Creates a unique token with a time-limited validity (15 minutes) and saves it
     * in the repository.
     * </p>
     *
     * @param request the request containing the username for whom the token is
     *                generated
     * @return the generated token including its value, associated user, and
     *         expiration time
     * @throws IllegalArgumentException if the user is not found
     */
    @Override
    @Transactional
    public OneTimeToken generate(final GenerateOneTimeTokenRequest request) {
        var user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String tokenValue = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(TOKEN_VALIDITY_SECONDS);

        PersistentOneTimeTokenEntity entity = PersistentOneTimeTokenEntity.builder().tokenValue(tokenValue).user(user)
                .expiredAt(expiresAt).build();

        tokenRepository.save(entity);

        return new DefaultOneTimeToken(tokenValue, user.getEmail(), expiresAt);
    }

}
