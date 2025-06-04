
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

import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.RecoverPasswordService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.RecoverPasswordServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import jakarta.mail.MessagingException;

/**
 * Default implementation of the {@link RecoverPasswordService} interface.
 * <p>
 * Provides functionality to send one-time tokens via email for password
 * recovery and to reset the password using a valid token.
 * </p>
 */
@Service
public final class DefaultRecoverPasswordService implements RecoverPasswordService {

    private final OneTimeTokenService oneTimeTokenService;
    private final EmailService emailService;
    private final UserService userService;

    /**
     * Constructs a new {@code DefaultRecoverPasswordService}.
     *
     * @param oneTimeTokenServ the service used to generate and consume one-time
     *                         tokens
     * @param emailServ        the email service used to send recovery emails
     * @param userServ         the user service used to retrieve and update user
     *                         data
     * @throws NullPointerException if any parameter is {@code null}
     */
    public DefaultRecoverPasswordService(final OneTimeTokenService oneTimeTokenServ, final EmailService emailServ,
            final UserService userServ) {
        oneTimeTokenService = Preconditions.checkNotNull(oneTimeTokenServ,
                "Received a null pointer as one time token service.");
        emailService = Preconditions.checkNotNull(emailServ, "Received a null pointer as email service.");
        userService = Preconditions.checkNotNull(userServ, "Received a null pointer as user service.");
    }

    /**
     * Normalizes an email by trimming it and converting it to lowercase.
     *
     * @param email the email to normalize
     * @return the normalized email, or {@code null} if the input was {@code null}
     */
    private String normalizeEmail(String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }

    /**
     * Resets the user's password after verifying the one-time token.
     *
     * @param token       the one-time token value
     * @param newPassword the new password to set
     * @param dni         the user's DNI
     * @throws RecoverPasswordServiceException if the token is invalid, expired, or
     *                                         the user does not match the token
     */
    @Override
    public void resetPassword(String token, String newPassword, String dni) throws RecoverPasswordServiceException {
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
     * Sends a one-time token to the user's email if the provided email and DNI
     * match.
     *
     * @param email   the user's email
     * @param userDni the user's DNI
     * @throws RecoverPasswordServiceException if the user data is invalid or the
     *                                         token could not be sent
     */
    @Override
    public void sendToken(String email, String userDni) throws RecoverPasswordServiceException {
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
