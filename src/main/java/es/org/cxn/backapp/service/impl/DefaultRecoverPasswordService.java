
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
 * Implementation of {@link RecoverPasswordService} that provides password
 * recovery functionality using a one-time token system.
 *
 * <p>
 * This service allows users to request a password reset token via email and
 * subsequently reset their password by providing the valid token.
 * </p>
 *
 */
@Service
public final class DefaultRecoverPasswordService implements RecoverPasswordService {

    /**
     * Service for generating and validating one-time tokens.
     * <p>
     * This service is responsible for creating unique tokens that allow users to
     * reset their passwords securely.
     * </p>
     */
    private final OneTimeTokenService oneTimeTokenService;

    /**
     * Service for sending emails.
     * <p>
     * This service is used to send password recovery emails containing the one-time
     * token or recovery link.
     * </p>
     */
    private final EmailService emailService;

    /**
     * Service for managing user accounts.
     * <p>
     * This service provides operations for retrieving and updating user details,
     * ensuring that password resets are properly applied to the correct user.
     * </p>
     */
    private final UserService userService;

    /**
     * Constructs a new {@code DefaultRecoverPasswordService} with the required
     * dependencies.
     *
     * @param oneTimeTokenServ The service responsible for generating and consuming
     *                         one-time tokens.
     * @param emailServ        The service used to send password recovery emails.
     * @param userServ         The service for managing user accounts.
     * @throws NullPointerException If any of the provided services are
     *                              {@code null}.
     */
    public DefaultRecoverPasswordService(final OneTimeTokenService oneTimeTokenServ, final EmailService emailServ,
            final UserService userServ) {
        oneTimeTokenService = Objects.requireNonNull(oneTimeTokenServ,
                "Received a null pointer as one time token service.");
        emailService = Objects.requireNonNull(emailServ, "Received a null pointer as email service.");
        userService = Objects.requireNonNull(userServ, "Received a null pointer as user service.");
    }

    /**
     * Normalizes the provided email by trimming whitespace and converting it to
     * lowercase.
     *
     * @param email The email to normalize.
     * @return The normalized email, or {@code null} if the input email is
     *         {@code null}.
     */
    private String normalizeEmail(final String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }

    /**
     * Resets the user's password using a valid one-time token.
     *
     * @param token       The one-time token used to authenticate the password reset
     *                    request.
     * @param newPassword The new password to be set for the user.
     * @param dni         The DNI (identification number) of the user.
     * @throws RecoverPasswordServiceException If the token is invalid or expired,
     *                                         the user is not found, or if there is
     *                                         a mismatch between the token and user
     *                                         information.
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
     * Sends a password recovery token to the specified email if the email and DNI
     * match.
     *
     * @param email   The email address of the user requesting the password reset.
     * @param userDni The DNI (identification number) of the user.
     * @throws RecoverPasswordServiceException If the email and DNI do not match, or
     *                                         if the email cannot be sent.
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
