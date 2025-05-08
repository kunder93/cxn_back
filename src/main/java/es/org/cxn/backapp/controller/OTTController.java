
package es.org.cxn.backapp.controller;

import java.util.Objects;

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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.form.requests.OTTRequest;
import es.org.cxn.backapp.model.form.requests.ResetPasswordRequest;
import es.org.cxn.backapp.service.RecoverPasswordService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.RecoverPasswordServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;

/**
 * Controller for handling One-Time Token (OTT) authentication and password
 * recovery.
 * <p>
 * This controller provides endpoints to generate one-time tokens for password
 * recovery and to reset a user's password using a valid OTT.
 * </p>
 *
 */
@RestController
@RequestMapping("/api/ott/my-generate-url")
public class OTTController {
    /**
     * Service for handling One-Time Token (OTT) authentication.
     * <p>
     * This service is responsible for generating, validating, and consuming
     * one-time tokens used in authentication flows.
     * </p>
     */
    private final OneTimeTokenService oneTimeTokenService;

    /**
     * Service for managing user-related operations.
     * <p>
     * This service provides methods for user authentication, registration, and
     * password recovery.
     * </p>
     */
    private final UserService userService;

    /**
     * Service for handling password recovery operations.
     * <p>
     * This service manages the process of sending one-time tokens to users and
     * validating their requests to reset passwords.
     * </p>
     */
    private final RecoverPasswordService recoverPasswordService;

    /**
     * Constructs an {@link OTTController} with the required dependencies.
     *
     * @param oneTimeTokenServ    the service for managing One-Time Tokens.
     * @param userServ            the service for managing users.
     * @param recoverPasswordServ the service for handling password recovery.
     * @throws NullPointerException if any of the services provided are null.
     */
    public OTTController(final OneTimeTokenService oneTimeTokenServ, final UserService userServ,
            final RecoverPasswordService recoverPasswordServ) {
        super();

        this.oneTimeTokenService = Objects.requireNonNull(oneTimeTokenServ,
                "Received a null pointer as One time token service.");
        this.userService = Objects.requireNonNull(userServ, "Received a null pointer as user service.");
        this.recoverPasswordService = Objects.requireNonNull(recoverPasswordServ,
                "Received a null pointer as recover password service.");
    }

    /**
     * Generates a one-time token (OTT) and sends it to the user's email for
     * password recovery. Both email and DNI are required to ensure that the request
     * is secure.
     *
     * @param request OTTRequest containing the user's email and DNI.
     * @return Response indicating the success or failure of the operation.
     * @throws ResponseStatusException if an error occurs when sending the email.
     */
    @PostMapping()
    public ResponseEntity<String> requestOTT(@RequestBody final OTTRequest request) {
        try {
            recoverPasswordService.sendToken(request.email(), request.dni());
        } catch (RecoverPasswordServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return ResponseEntity.ok("One-Time Token sent to " + request.email());
    }

    /**
     * Resets the user's password using a valid one-time token (OTT).
     *
     * @param request {@link ResetPasswordRequest} containing the token and new
     *                password.
     * @return ResponseEntity with a success message if the password is updated
     *         successfully, or an error response if the token is invalid or
     *         expired.
     */
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody final ResetPasswordRequest request) {
        try {
            OneTimeToken verifiedToken = oneTimeTokenService
                    .consume(new OneTimeTokenAuthenticationToken(request.token()));
            // Verify token not null.
            if (verifiedToken == null) {
                return ResponseEntity.badRequest().body("Token inválido o expirado.");
            }

            userService.recoverPassword(verifiedToken.getUsername(), request.newPassword());
            return ResponseEntity.ok("Tu contraseña ha sido actualizada correctamente.");
        } catch (UserServiceException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Token inválido o expirado.");
        }
    }

}
