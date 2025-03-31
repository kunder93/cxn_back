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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOneTimeTokenService implements OneTimeTokenService {

    private final OneTimeTokenEntityRepository tokenRepository;
    private final UserEntityRepository userRepository;
    private final long tokenValiditySeconds = 900; // 15 min

    @Override
    @Transactional
    public OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
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

    @Override
    @Transactional
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        var user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String tokenValue = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(tokenValiditySeconds);

        PersistentOneTimeTokenEntity entity = PersistentOneTimeTokenEntity.builder().tokenValue(tokenValue).user(user)
                .expiredAt(expiresAt).build();

        tokenRepository.save(entity);

        return new DefaultOneTimeToken(tokenValue, user.getEmail(), expiresAt);
    }

}
