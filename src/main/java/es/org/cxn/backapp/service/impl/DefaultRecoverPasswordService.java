
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

import java.io.IOException;
import java.util.Objects;

import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;

import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.RecoverPasswordService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.RecoverPasswordServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import jakarta.mail.MessagingException;

/**
 * Default implementation of the {@link RecoverPasswordService} interface.
 *
 * <p>
 * This service handles the password recovery process by generating and
 * consuming one-time tokens (OTT) for user authentication, and by sending
 * recovery emails containing these tokens.
 * </p>
 *
 * <p>
 * It relies on {@link OneTimeTokenService} for token management,
 * {@link EmailService} for sending emails, and {@link UserService} for user
 * data retrieval and password updates.
 * </p>
 */
@Service
public final class DefaultRecoverPasswordService implements RecoverPasswordService {

    /**
     * Service for generating and consuming one-time tokens used in authentication.
     */
    private final OneTimeTokenService oneTimeTokenService;

    /**
     * Service for sending emails, specifically recovery emails with tokens.
     *
     */
    private final EmailService emailService;

    /**
     * Service for user management, including retrieval by email or DNI and password
     * recovery.
     */
    private final UserService userService;

    /**
     * Constructs a new {@code DefaultRecoverPasswordService} with the specified
     * dependencies.
     *
     * @param oneTimeTokenServ the one-time token service; must not be {@code null}
     * @param emailServ        the email service; must not be {@code null}
     * @param userServ         the user service; must not be {@code null}
     * @throws NullPointerException if any argument is {@code null}
     */
    public DefaultRecoverPasswordService(final OneTimeTokenService oneTimeTokenServ, final EmailService emailServ,
            final UserService userServ) {
        oneTimeTokenService = Objects.requireNonNull(oneTimeTokenServ,
                "Received a null pointer as one time token service.");
        emailService = Objects.requireNonNull(emailServ, "Received a null pointer as email service.");
        userService = Objects.requireNonNull(userServ, "Received a null pointer as user service.");
    }

    /**
     * Normalizes an email string by trimming whitespace and converting to
     * lowercase.
     *
     * @param email the email to normalize; may be {@code null}
     * @return the normalized email, or {@code null} if the input was {@code null}
     */
    private String normalizeEmail(final String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }

    /**
     * Resets the password for the user identified by the given token and DNI.
     *
     * <p>
     * This method consumes the provided one-time token to authenticate the user,
     * verifies that the token corresponds to the user identified by the DNI, and
     * updates the user's password.
     * </p>
     *
     * @param token       the one-time token string to consume
     * @param newPassword the new password to set
     * @param dni         the user's DNI to verify identity
     * @throws RecoverPasswordServiceException if the token is invalid or expired,
     *                                         if the token and DNI do not match, or
     *                                         if the user cannot be found or
     *                                         updated
     */
    @Override
    public void resetPassword(final String token, final String newPassword, final String dni)
            throws RecoverPasswordServiceException {
        OneTimeToken verifiedToken = oneTimeTokenService.consume(new OneTimeTokenAuthenticationToken(token));
        // Verify token not null.
        if (verifiedToken == null) {
            throw new RecoverPasswordServiceException("Token inválido o expirado");
        }
        try {
            final var userByDni = userService.findByDni(dni);
            if (!userByDni.getEmail().equals(verifiedToken.getUsername())) {
                throw new RecoverPasswordServiceException("Diferent token and/or user dni.");
            }
            userService.recoverPassword(verifiedToken.getUsername(), newPassword);
        } catch (UserServiceException e) {
            throw new RecoverPasswordServiceException("User not found.");
        }

    }

    /**
     * Sends a recovery token via email to the user identified by the provided email
     * and DNI.
     *
     * <p>
     * This method verifies that the email and DNI correspond to the same user,
     * generates a one-time token for that user, and sends a recovery email
     * containing the token.
     * </p>
     *
     * @param email   the email address of the user to send the token to
     * @param userDni the DNI of the user to verify identity
     * @throws RecoverPasswordServiceException if the email and DNI do not match a
     *                                         user, or if sending the email fails
     */
    @Override
    public void sendToken(final String email, final String userDni) throws RecoverPasswordServiceException {
        try {
            final var normEmail = normalizeEmail(email);
            final var userByEmail = userService.findByEmail(normEmail);
            final var userByDni = userService.findByDni(userDni);
            if (!userByEmail.equals(userByDni)) {
                throw new RecoverPasswordServiceException("User dni and user email not equals.");
            }
            OneTimeToken token = oneTimeTokenService.generate(new GenerateOneTimeTokenRequest(normEmail));
            emailService.sendRecoverPasswordEmail(normEmail, userDni, token.getTokenValue());
        } catch (MessagingException | IOException | UserServiceException e) {
            throw new RecoverPasswordServiceException("Cannot send token.");
        }
    }

}
