
package es.org.cxn.backapp.controller;

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

import com.google.common.base.Preconditions;

import es.org.cxn.backapp.model.form.requests.OTTRequest;
import es.org.cxn.backapp.model.form.requests.ResetPasswordRequest;
import es.org.cxn.backapp.service.RecoverPasswordService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.RecoverPasswordServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;

/**
 * Controlador para la gestión de tokens de un solo uso (One-Time Token, OTT) y
 * recuperación de contraseña.
 *
 * <p>
 * Este controlador permite generar un token de un solo uso y enviarlo por
 * correo electrónico para la recuperación de contraseña. También maneja la
 * solicitud de restablecimiento de contraseña utilizando el token recibido.
 * </p>
 *
 * @author Santiago Paz Perez
 */
@RestController
@RequestMapping("/api/ott/my-generate-url")
public class OTTController {
    private final OneTimeTokenService oneTimeTokenService;
    private final UserService userService;
    private final RecoverPasswordService recoverPasswordService;

    /**
     * Constructor de {@code OTTController} que inicializa los servicios necesarios.
     *
     * @param oneTimeTokenServ Servicio para la gestión de tokens de un solo uso.
     * @param userServ         Servicio de gestión de usuarios.
     * @throws NullPointerException si algún parámetro es {@code null}.
     */
    public OTTController(final OneTimeTokenService oneTimeTokenServ, final UserService userServ,
            final RecoverPasswordService recoverPasswordServ) {
        super();

        this.oneTimeTokenService = Preconditions.checkNotNull(oneTimeTokenServ,
                "Received a null pointer as One time token service.");
        this.userService = Preconditions.checkNotNull(userServ, "Received a null pointer as user service.");
        this.recoverPasswordService = Preconditions.checkNotNull(recoverPasswordServ,
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
    public ResponseEntity<String> requestOTT(@RequestBody OTTRequest request) {
        try {
            recoverPasswordService.sendToken(request.email(), request.dni());
        } catch (RecoverPasswordServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return ResponseEntity.ok("One-Time Token sent to " + request.email());
    }

    /**
     * Permite restablecer la contraseña del usuario utilizando un token de un solo
     * uso.
     *
     * @param request Objeto que contiene el token y la nueva contraseña.
     * @return Respuesta indicando el éxito o fallo de la operación.
     */
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
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
