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
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.MyPrincipalUser;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetails;
import es.org.cxn.backapp.service.dto.UserRegistrationDetails;

import jakarta.validation.Valid;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for authentication operations.
 *
 * @author Santiago Paz Perez.
 */
@RestController
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
  private final UserDetailsService usrDtlsSrv;

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
    this.usrDtlsSrv = Preconditions.checkNotNull(
          userDetailsServ, "Received a null pointer as authenticationManager"
    );
    this.jwtUtils = Preconditions
          .checkNotNull(jwtUtil, "Received a null pointer as jwtUtils");

  }

  private static AddressRegistrationDetails
        createAddressDetails(final SignUpRequestForm signUpRequestForm) {
    return AddressRegistrationDetails.builder()
          .apartmentNumber(signUpRequestForm.getApartmentNumber())
          .building(signUpRequestForm.getBuilding())
          .city(signUpRequestForm.getCity())
          .postalCode(signUpRequestForm.getPostalCode())
          .street(signUpRequestForm.getStreet())
          .countryNumericCode(signUpRequestForm.getCountryNumericCode())
          .countrySubdivisionName(signUpRequestForm.getCountrySubdivisionName())
          .build();
  }

  private static UserRegistrationDetails createUserDetails(
        final SignUpRequestForm signUpRequestForm,
        final AddressRegistrationDetails addressDetails
  ) {
    return UserRegistrationDetails.builder().dni(signUpRequestForm.getDni())
          .name(signUpRequestForm.getName())
          .firstSurname(signUpRequestForm.getFirstSurname())
          .secondSurname(signUpRequestForm.getSecondSurname())
          .birthDate(signUpRequestForm.getBirthDate())
          .gender(signUpRequestForm.getGender())
          .password(signUpRequestForm.getPassword())
          .email(signUpRequestForm.getEmail())
          .kindMember(signUpRequestForm.getKindMember())
          .addressDetails(addressDetails).build();
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
    final var defaultUserRole = UserRoleName.ROLE_CANDIDATO_SOCIO;
    final var initialUserRolesSet = new ArrayList<UserRoleName>();
    initialUserRolesSet.add(defaultUserRole);

    final var addressDetails = createAddressDetails(signUpRequestForm);
    final var userDetails =
          createUserDetails(signUpRequestForm, addressDetails);

    try {
      userService.add(userDetails);
      final var createdUser = userService
            .changeUserRoles(signUpRequestForm.getEmail(), initialUserRolesSet);
      final var signUpRspnsFrm = new SignUpResponseForm(createdUser);

      return new ResponseEntity<>(signUpRspnsFrm, HttpStatus.CREATED);
    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
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
    final var email = loginRequest.getEmail();
    final var password = loginRequest.getPassword();
    try {
      authManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
      );
    } catch (BadCredentialsException | DisabledException e) {
      throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED, e.getMessage(), e
      );
    } catch (LockedException e) {
      throw new ResponseStatusException(HttpStatus.LOCKED, e.getMessage(), e);
    }
    final MyPrincipalUser userDetails;
    try {
      userDetails = (MyPrincipalUser) usrDtlsSrv.loadUserByUsername(email);
    } catch (UsernameNotFoundException e) {
      throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED, e.getMessage(), e
      );
    }
    final var jwt = jwtUtils.generateToken(userDetails);
    return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
  }

}
