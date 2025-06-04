
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
 * Service interface for handling password recovery using one-time tokens.
 *
 * <p>
 * This service allows sending a recovery token to the user's email and then
 * validating the token to reset the password.
 * </p>
 *
 * <p>
 * Implementations should provide the logic to generate, send, verify tokens,
 * and reset user passwords securely.
 * </p>
 *
 */
public interface RecoverPasswordService {

    /**
     * Resets the user's password using a previously generated one-time token.
     *
     * @param token       the one-time token received by email
     * @param newPassword the new password to be set
     * @param dni         the user's DNI (identifier) to validate identity
     * @throws RecoverPasswordServiceException if the token is invalid, the user is
     *                                         not found, or password reset fails
     */
    void resetPassword(String token, String newPassword, String dni) throws RecoverPasswordServiceException;

    /**
     * Sends a password recovery token to the user's registered email, verifying
     * that the email and DNI correspond to the same user.
     *
     * @param email   the user's registered email address
     * @param userDni the user's DNI (identifier)
     * @throws RecoverPasswordServiceException if the user does not exist or the
     *                                         token cannot be sent
     */
    void sendToken(String email, String userDni) throws RecoverPasswordServiceException;

}
