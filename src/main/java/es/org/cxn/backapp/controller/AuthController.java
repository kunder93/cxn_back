/**
 * The MIT License (MIT)
 *
 * <p>Copyright (c) 2020 the original author or authors.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.controller;

import com.google.common.base.Preconditions;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.MyPrincipalUser;
import es.org.cxn.backapp.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for authentication operations.
 *
 * @author Santiago Paz Perez.
 */
@Controller
@RequestMapping("/api/auth")
public class AuthController {

  /**
   * The user Service.
   */
  private final UserService userService;

  /**
   * The authentication manager.
   */
  private final AuthenticationManager authManager;

  /**
   * The user details service.
   */
  private final UserDetailsService userDetailsService;

  /**
   * The jwt utilities.
   */
  private final DefaultJwtUtils jwtUtils;

  /**
   * Constructs a controller with the specified dependencies.
   *
   * @param serviceUser     the user service.
   * @param authManag       the authenticationManager.
   * @param userDetailsServ the userDetailsService.
   * @param jwtUtil         the jwtUtils.
   */
  public AuthController(
        final UserService serviceUser, final AuthenticationManager authManag,
        final UserDetailsService userDetailsServ, final DefaultJwtUtils jwtUtil
  ) {
    super();
    this.userService = Preconditions
          .checkNotNull(serviceUser, "Received a null pointer as userService");
    this.authManager = Preconditions.checkNotNull(
          authManag, "Received a null pointer as authenticationManager"
    );
    this.userDetailsService = Preconditions.checkNotNull(
          userDetailsServ, "Received a null pointer as authenticationManager"
    );
    this.jwtUtils = Preconditions
          .checkNotNull(jwtUtil, "Received a null pointer as jwtUtils");

  }

  /**
   * Creates an user with default user Role.
   *
   * @param signUpRequestForm user data to create user profile.
   * @return the created user data @link{SignUpResponseForm}.
   */
  @CrossOrigin
  @PostMapping("/signup")
  public ResponseEntity<SignUpResponseForm>
        registerUser(final @Valid @RequestBody
  SignUpRequestForm signUpRequestForm) {
    var defaultUserRole = "USER";
    try {
      userService.add(
            signUpRequestForm.getDni(), signUpRequestForm.getName(),
            signUpRequestForm.getFirstSurname(),
            signUpRequestForm.getSecondSurname(),
            signUpRequestForm.getBirthDate(), signUpRequestForm.getGender(),
            signUpRequestForm.getPassword(), signUpRequestForm.getEmail(),
            signUpRequestForm.getApartmentNumber(),
            signUpRequestForm.getBuilding(), signUpRequestForm.getCity(),
            signUpRequestForm.getPostalCode(), signUpRequestForm.getStreet(),
            signUpRequestForm.getCountryNumericCode(),
            signUpRequestForm.getCountrySubdivisionName()
      );
      final var createdUser =
            userService.addRole(signUpRequestForm.getEmail(), defaultUserRole);
      final var signUpResponseForm = new SignUpResponseForm(
            createdUser.getDni(), createdUser.getName(),
            createdUser.getFirstSurname(), createdUser.getSecondSurname(),
            createdUser.getBirthDate(), createdUser.getGender(),
            createdUser.getEmail(), createdUser.getRoles()
      );

      return new ResponseEntity<>(signUpResponseForm, HttpStatus.CREATED);

    } catch (UserServiceException e) {

      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

  }

  /**
   * Authenticate user with loginRequest and give a jwt Token.
   *
   * @param loginRequest request form with user data email and password.
   * @return ResponseEntity with result of authenticate, ACCEPTED or failed
   *         with error description.
   */
  @CrossOrigin
  @PostMapping("/signinn")
  public ResponseEntity<AuthenticationResponse>
        authenticateUser(final @Valid @RequestBody
  AuthenticationRequest loginRequest) {
    try {
      authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                  loginRequest.getEmail(), loginRequest.getPassword()
            )
      );
    } catch (BadCredentialsException | DisabledException e) {
      throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED, e.getMessage()
      );
    } catch (LockedException e) {
      throw new ResponseStatusException(HttpStatus.LOCKED, e.getMessage());
    }
    final MyPrincipalUser userDetails;
    try {
      userDetails = (MyPrincipalUser) userDetailsService
            .loadUserByUsername(loginRequest.getEmail());
    } catch (UsernameNotFoundException e) {
      throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED, e.getMessage()
      );
    }
    var jwt = jwtUtils.generateToken(userDetails);
    return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
  }

}
