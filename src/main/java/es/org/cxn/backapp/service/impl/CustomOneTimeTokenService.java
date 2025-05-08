package es.org.cxn.backapp.service.impl;

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
import java.util.Objects;
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
 * Custom implementation of {@link OneTimeTokenService} that manages the
 * generation and consumption of one-time tokens for authentication purposes.
 *
 * <p>
 * This service is responsible for generating secure one-time tokens, validating
 * them, and ensuring they are used only once. Tokens expire after a predefined
 * period.
 * </p>
 *
 */
@Service
public class CustomOneTimeTokenService implements OneTimeTokenService {

    /**
     * The validity period of a token in seconds.
     * <p>
     * Tokens are valid for 15 minutes (900 seconds) before expiring.
     * </p>
     */
    private static final long TOKEN_VALIDITY_TIME = 900; // 15 min

    /**
     * Repository for managing one-time token entities.
     * <p>
     * This repository is used to store, retrieve, and delete one-time tokens from
     * the database.
     * </p>
     */
    private final OneTimeTokenEntityRepository tokenRepository;

    /**
     * Repository for managing user entities.
     * <p>
     * This repository is used to fetch user information associated with a given
     * one-time token.
     * </p>
     */
    private final UserEntityRepository userRepository;

    /**
     * Constructs a new {@code CustomOneTimeTokenService} with required
     * dependencies.
     *
     * @param tokenRepository The repository handling token persistence.
     * @param userRepository  The repository handling user persistence.
     */
    public CustomOneTimeTokenService(final OneTimeTokenEntityRepository tokenRepository,
            final UserEntityRepository userRepository) {
        this.tokenRepository = Objects.requireNonNull(tokenRepository, "Token repository cannot be null.");
        this.userRepository = Objects.requireNonNull(userRepository, "User repository cannot be null.");
    }

    /**
     * Consumes a one-time token by validating and removing it from storage.
     *
     * @param authenticationToken The authentication token containing the one-time
     *                            token value.
     * @return A validated {@link OneTimeToken} if the token is valid and not
     *         expired.
     * @throws IllegalArgumentException If the token is invalid or expired.
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
     * Generates a new one-time token for a given user.
     *
     * @param request The request containing the username for which the token is
     *                generated.
     * @return A newly created {@link OneTimeToken}.
     * @throws IllegalArgumentException If the user is not found.
     */
    @Override
    @Transactional
    public OneTimeToken generate(final GenerateOneTimeTokenRequest request) {
        var user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String tokenValue = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(TOKEN_VALIDITY_TIME);

        PersistentOneTimeTokenEntity entity = PersistentOneTimeTokenEntity.builder().tokenValue(tokenValue).user(user)
                .expiredAt(expiresAt).build();

        tokenRepository.save(entity);

        return new DefaultOneTimeToken(tokenValue, user.getEmail(), expiresAt);
    }

}
