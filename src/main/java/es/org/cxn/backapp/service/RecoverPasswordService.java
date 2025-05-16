
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
 * <p>
 * This service provides methods to send a one-time token to users via email and
 * reset their passwords using a valid token.
 * </p>
 */
public interface RecoverPasswordService {

    /**
     * Resets the user's password using a one-time token.
     *
     * @param token       The one-time token provided for password reset.
     * @param newPassword The new password to be set for the user.
     * @param dni         The user's DNI (Documento Nacional de Identidad).
     * @throws RecoverPasswordServiceException If the token is invalid or expired,
     *                                         or if the user is not found.
     */
    void resetPassword(String token, String newPassword, String dni) throws RecoverPasswordServiceException;

    /**
     * Sends a password recovery token to the user's email.
     *
     * @param email   The email address associated with the user account.
     * @param userDni The DNI (Documento Nacional de Identidad) of the user.
     * @throws RecoverPasswordServiceException If the user is not found or if there
     *                                         is an issue sending the token.
     */
    void sendToken(String email, String userDni) throws RecoverPasswordServiceException;

}
