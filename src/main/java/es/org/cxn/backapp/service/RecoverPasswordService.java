
package es.org.cxn.backapp.service;

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

import es.org.cxn.backapp.service.exceptions.RecoverPasswordServiceException;

/**
 * Service interface for handling password recovery operations.
 *
 * <p>
 * This service provides methods to send one-time tokens to users for password
 * reset purposes and to reset the user's password by consuming a valid token.
 * </p>
 */
public interface RecoverPasswordService {

    /**
     * Resets the password for a user by consuming a one-time token.
     *
     * <p>
     * Verifies the provided token and user identity (via DNI), then updates the
     * password to the new value.
     * </p>
     *
     * @param token       the one-time token used to authorize the password reset
     * @param newPassword the new password to be set
     * @param dni         the user's DNI to verify identity
     * @throws RecoverPasswordServiceException if the token is invalid or expired,
     *                                         if the user cannot be found, or if
     *                                         the token and DNI do not match
     */
    void resetPassword(String token, String newPassword, String dni) throws RecoverPasswordServiceException;

    /**
     * Sends a one-time token to the specified email for password recovery.
     *
     * <p>
     * Verifies that the provided email and DNI correspond to the same user,
     * generates a recovery token, and sends it via email.
     * </p>
     *
     * @param email   the email address of the user to send the recovery token to
     * @param userDni the user's DNI to verify identity
     * @throws RecoverPasswordServiceException if the user is not found, if email
     *                                         sending fails, or if the email and
     *                                         DNI do not match
     */
    void sendToken(String email, String userDni) throws RecoverPasswordServiceException;

}
