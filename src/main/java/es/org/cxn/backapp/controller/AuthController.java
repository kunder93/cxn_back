
package es.org.cxn.backapp.controller;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
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
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.base.Preconditions;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.requests.UnsubscribeRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import es.org.cxn.backapp.security.MyPrincipalUser;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

/**
 * Controller for authentication operations.
 *
 * @author Santiago Paz Perez.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * The user service for handling user-related operations.
     */
    private final UserService userService;

    /**
     * The authentication manager for handling authentication requests.
     */
    private final AuthenticationManager authManager;

    /**
     * The user details service for loading user-specific details.
     */
    private final UserDetailsService usrDtlsSrv;

    /**
     * The email service for sending emails.
     */
    private final DefaultEmailService emailService;

    /**
     * The jwtUtils service for validate auth tokens.
     */
    private final DefaultJwtUtils jwtUtils;

    /**
     * The role service.
     */
    private final RoleService roleService;

    /**
     * Constructs an {@code AuthController} with essential security and user
     * management dependencies.
     *
     * <p>
     * <strong>Dependency Requirements:</strong>
     * </p>
     * <ul>
     * <li>All parameters must be non-null (validated via
     * {@link Preconditions#checkNotNull})</li>
     * <li>Dependencies are typically injected by the Spring framework</li>
     * </ul>
     *
     * @param serviceUser     User service for user management operations
     * @param authManag       Authentication manager for credential validation
     * @param userDetailsServ User details service for security context loading
     * @param jwtUtils        JWT utilities for token generation/validation
     * @param emailServ       Email service for notifications and verification
     * @param roleServ        The role service.
     * @throws NullPointerException if any parameter is null
     *
     * @see UserService Core user management operations
     * @see AuthenticationManager Spring Security authentication entry point
     * @see UserDetailsService Spring Security user loading mechanism
     * @see DefaultJwtUtils JWT token handling utilities
     * @see DefaultEmailService Email delivery service
     */
    public AuthController(final UserService serviceUser, final AuthenticationManager authManag,
            final UserDetailsService userDetailsServ, final DefaultJwtUtils jwtUtils,
            final DefaultEmailService emailServ, final RoleService roleServ) {
        super();
        this.jwtUtils = Preconditions.checkNotNull(jwtUtils, "Received a null pointer as jwtUtils");
        this.userService = Preconditions.checkNotNull(serviceUser, "Received a null pointer as userService");
        this.authManager = Preconditions.checkNotNull(authManag, "Received a null pointer as authenticationManager");
        this.usrDtlsSrv = Preconditions.checkNotNull(userDetailsServ, "Received a null pointer as userDetailsService");
        this.emailService = Preconditions.checkNotNull(emailServ, "Received a null pointer as email service.");
        this.roleService = Preconditions.checkNotNull(roleServ, "Received a null pointer as email service.");
    }

    /**
     * Creates an address details object from the provided sign-up request form.
     *
     * @param signUpRequestForm the sign-up request form containing address
     *                          information.
     * @return an {@link AddressRegistrationDetailsDto} containing address details.
     */
    private static AddressRegistrationDetailsDto createAddressDetails(final SignUpRequestForm signUpRequestForm) {
        return new AddressRegistrationDetailsDto(signUpRequestForm.apartmentNumber(), signUpRequestForm.building(),
                signUpRequestForm.city(), signUpRequestForm.postalCode(), signUpRequestForm.street(),
                signUpRequestForm.countryNumericCode(), signUpRequestForm.countrySubdivisionName());
    }

    /**
     * Creates a user details object from the provided sign-up request form and
     * address details.
     *
     * @param signUpRequestForm the sign-up request form containing user
     *                          information.
     * @param addressDetails    the address details for the user.
     * @return a {@link UserRegistrationDetailsDto} containing user and address
     *         details.
     */
    private static UserRegistrationDetailsDto createUserDetails(final SignUpRequestForm signUpRequestForm,
            final AddressRegistrationDetailsDto addressDetails) {
        return new UserRegistrationDetailsDto(signUpRequestForm.dni(), signUpRequestForm.name(),
                signUpRequestForm.firstSurname(), signUpRequestForm.secondSurname(), signUpRequestForm.birthDate(),
                signUpRequestForm.gender(), signUpRequestForm.password(), signUpRequestForm.email(), addressDetails,
                signUpRequestForm.kindMember());
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginRequest the form containing user email and password for
     *                     authentication.
     * @return a {@link ResponseEntity} containing the authentication response with
     *         JWT token and HTTP status code {@code 200 OK}.
     * @throws ResponseStatusException if authentication fails due to incorrect
     *                                 credentials, disabled account, or account
     *                                 locked.
     */
    @PostMapping("/signinn")
    public ResponseEntity<AuthenticationResponse> authenticateUser(final @Valid
    @RequestBody AuthenticationRequest loginRequest) {
        final var email = loginRequest.email();
        final var password = loginRequest.password();
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException | DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (LockedException e) {
            throw new ResponseStatusException(HttpStatus.LOCKED, e.getMessage(), e);
        }
        final MyPrincipalUser userDetails;
        try {
            userDetails = (MyPrincipalUser) usrDtlsSrv.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
        final var jwt = jwtUtils.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
    }

    /**
     * Registers a new user and assigns a default role.
     *
     * @param signUpRequestForm the form containing user data for registration.
     * @return a {@link ResponseEntity} containing the response form with the
     *         created user data and HTTP status code {@code 201 Created}.
     * @throws IOException             When cannot load message template.
     * @throws ResponseStatusException if there is a problem with user registration.
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseForm> registerUser(@Valid
    @RequestBody final SignUpRequestForm signUpRequestForm) throws IOException {

        final var defaultUserRole = UserRoleName.ROLE_CANDIDATO_SOCIO;
        final var initialUserRolesSet = new ArrayList<UserRoleName>();
        initialUserRolesSet.add(defaultUserRole);

        final var addressDetails = createAddressDetails(signUpRequestForm);
        final var userDetails = createUserDetails(signUpRequestForm, addressDetails);

        try {
            userService.add(userDetails);
            final var createdUser = roleService.changeUserRoles(signUpRequestForm.email(), initialUserRolesSet);
            final var signUpRspnsFrm = SignUpResponseForm.fromEntity(createdUser);

            emailService.sendSignUp(signUpRequestForm.email(), signUpRequestForm.name(),
                    "Te damos la bienvenida a Círculo Xadrez Narón.");

            return new ResponseEntity<>(signUpRspnsFrm, HttpStatus.CREATED);
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * Unsubscribe existent user.
     * <p>
     * This endpoint allows an authenticated user to unsubscribe. It retrieves the
     * user's email or username from the authentication context and invokes the user
     * service to handle the unsubscription logic.
     * </p>
     *
     * @param unsubscribeRequest The dto for receive controller data validation
     *                           password.
     *
     * @return ResponseEntity indicating the result of the operation.
     */
    @PatchMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody final UnsubscribeRequest unsubscribeRequest) {
        final var authName = SecurityContextHolder.getContext().getAuthentication().getName();
        final String validationPassword = unsubscribeRequest.password();

        try {
            userService.unsubscribe(authName, validationPassword);
            return ResponseEntity.ok("Successfully unsubscribed");
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
