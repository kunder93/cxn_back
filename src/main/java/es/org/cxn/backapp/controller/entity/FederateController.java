
package es.org.cxn.backapp.controller.entity;

import java.util.Objects;

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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.form.requests.ConfirmCancelFederateRequest;
import es.org.cxn.backapp.model.form.responses.FederateStateExtendedResponseList;
import es.org.cxn.backapp.model.form.responses.FederateStateExtendedResponseList.FederateStateExtendedResponse;
import es.org.cxn.backapp.model.form.responses.FederateStateResponse;
import es.org.cxn.backapp.model.form.responses.user.DniImagesResponse;
import es.org.cxn.backapp.service.FederateStateService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.UserDniImagesDto;
import es.org.cxn.backapp.service.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;

/**
 * The FederateController class handles HTTP requests related to federate state
 * management. It provides endpoints for confirming a federate state, federate a
 * member, retrieving federate state data for the authenticated user, updating
 * DNI information, and fetching federate state data for all users.
 *
 * <p>
 * This controller supports methods for confirming federate status, federate
 * members with DNI document uploads, and retrieving federate state information.
 * It uses the {@link FederateStateService} to interact with the federate state
 * business logic.
 * </p>
 *
 * <p>
 * Access control is enforced via {@code @PreAuthorize} annotations to allow
 * only specific roles to confirm federate status or manage auto-renewal
 * settings.
 * </p>
 *
 * <p>
 * Example endpoints:
 * </p>
 * <ul>
 * <li>Change auto-renewal settings for a federate user</li>
 * <li>Federate a member by uploading DNI files</li>
 * <li>Retrieve federate state information for the current user</li>
 * <li>Confirm or cancel a user's federate status</li>
 * </ul>
 *
 * <p>
 * All methods handle exceptions and return appropriate HTTP status codes in
 * case of errors, such as bad requests or access violations.
 *
 * @see FederateStateService
 * @see ResponseEntity
 * @see ResponseStatusException
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/user/federate")
public class FederateController {

    /**
     * A service that handles federate state-related operations for users, including
     * actions such as toggling auto-renewal, confirming or canceling federate
     * status, federating members with document uploads, and retrieving federate
     * state information.
     *
     * <p>
     * FederateStateService provides the main business logic for managing federate
     * state data in conjunction with this controller.
     * </p>
     *
     * @see FederateStateService
     */
    private final FederateStateService federateStateService;

    /**
     * The user service that provides user-related operations such as loading user
     * data, updating profiles, and managing user roles.
     *
     * <p>
     * UserService is used to interact with user-related data, including retrieving
     * user details and performing actions like updating or deleting user profiles.
     * </p>
     *
     * @see UserService Service responsible for handling user data and operations
     */
    private final UserService userService;

    /**
     * Constructs a new FederateController with the specified FederateStateService.
     *
     * @param federateStateServ The federate state service.
     * @param userServ          The user service.
     * @throws NullPointerException if the federate state service is null
     */
    public FederateController(final FederateStateService federateStateServ, final UserService userServ) {
        federateStateService = Objects.requireNonNull(federateStateServ,
                "Received a null pointer as federate state service");
        userService = Objects.requireNonNull(userServ, "Received a null pointer as user service.");
    }

    /**
     * Changes the auto-renewal property of the current user's federate state.
     *
     * <p>
     * This endpoint allows users to toggle the auto-renewal setting of their
     * federate state. It retrieves the user's email from the security context,
     * fetches the federate state, and changes the auto-renew property.
     * </p>
     *
     * @return a ResponseEntity containing the updated federate state response
     * @throws ResponseStatusException if there is an error during the update
     *                                 process
     */
    @PatchMapping("/changeAutoRenew")
    public ResponseEntity<FederateStateResponse> changeAutoRenew() {
        final var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var entity = federateStateService.changeAutoRenew(userEmail);
            return new ResponseEntity<>(new FederateStateResponse(entity), HttpStatus.OK);
        } catch (UserServiceException | FederateStateServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Confirms or cancels the federate status of a user identified by the given
     * DNI.
     *
     * <p>
     * This endpoint is restricted to users with roles 'ADMIN', 'PRESIDENTE', or
     * 'SECRETARIO'. It accepts the user's DNI as input and uses the service to
     * update their federate status.
     * </p>
     *
     * @param request the request containing the user's DNI
     * @return a ResponseEntity containing the updated federate state response
     * @throws PaymentsServiceException if associated payment cannot be deleted.
     * @throws ResponseStatusException  if there is an error during the confirmation
     *                                  process
     */
    @PatchMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    public ResponseEntity<FederateStateExtendedResponse> confirmCancelFederate(
            @RequestBody final ConfirmCancelFederateRequest request) throws PaymentsServiceException {
        try {
            final var result = federateStateService.confirmCancelFederate(request.userDni());
            return new ResponseEntity<>(new FederateStateExtendedResponse(result), HttpStatus.OK);
        } catch (FederateStateServiceException | UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Federate a member by uploading their DNI documents and enabling or disabling
     * auto-renewal.
     *
     * <p>
     * This endpoint accepts the front and back images of the user's DNI documents
     * along with the auto-renewal flag. It uses the current user's email from the
     * security context to federate the member and store their federate state.
     * </p>
     *
     * @param frontDni    the front image of the DNI document
     * @param backDni     the back image of the DNI document
     * @param autoRenewal flag indicating whether automatic renewal is enabled
     * @return a ResponseEntity containing the federate state response
     * @throws ResponseStatusException if there is an error during the federate
     *                                 operation
     */
    @PostMapping()
    public ResponseEntity<FederateStateResponse> federateMember(@RequestParam final MultipartFile frontDni,
            @RequestParam final MultipartFile backDni, final boolean autoRenewal) {
        final var userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // UserName = UserEmail

        try {
            final var federateStateEntity = federateStateService.federateMember(userEmail, frontDni, backDni,
                    autoRenewal);
            return new ResponseEntity<>(new FederateStateResponse(federateStateEntity), HttpStatus.OK);

        } catch (UserServiceException | FederateStateServiceException | PaymentsServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Retrieves the federate state data for the currently authenticated user.
     *
     * <p>
     * This endpoint fetches the federate state for the user based on their email
     * obtained from the security context.
     * </p>
     *
     * @return a ResponseEntity containing the federate state response
     * @throws ResponseStatusException if there is an error retrieving the federate
     *                                 state
     */
    @GetMapping()
    public ResponseEntity<FederateStateResponse> getFederateState() {
        final var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var result = federateStateService.getFederateDataByEmail(userEmail);
            return new ResponseEntity<>(new FederateStateResponse(result), HttpStatus.OK);
        } catch (FederateStateServiceException | UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Retrieves federate state data for all users.
     *
     * <p>
     * This endpoint fetches the federate state for all members and returns the data
     * in a list.
     * </p>
     *
     * @return a ResponseEntity containing the federate state data for all users
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    public ResponseEntity<FederateStateExtendedResponseList> getFederateStateMembers() {
        final var entitiesList = federateStateService.getAll();

        final var result = FederateStateExtendedResponseList.fromEntities(entitiesList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves the federate state data from user using dni.
     *
     * <p>
     * This endpoint fetches the federate state for the user based on their dni.
     * </p>
     *
     * @param userDni The user identifier for locating their info.
     * @return a ResponseEntity containing the federate state response
     * @throws ResponseStatusException if there is an error retrieving the federate
     *                                 state
     */
    @GetMapping("/{userDni}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    public ResponseEntity<FederateStateResponse> getFederateStateUsingDni(final @PathVariable String userDni) {
        try {
            final var result = federateStateService.getFederateDataByDni(userDni);
            return new ResponseEntity<>(new FederateStateResponse(result), HttpStatus.OK);
        } catch (FederateStateServiceException | UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Retrieves the authenticated user's DNI images.
     *
     * @return ResponseEntity containing the front and back images of the user's
     *         DNI.
     * @throws ResponseStatusException if the user is not found or an error occurs
     *                                 while retrieving the images.
     */
    @GetMapping("/dni")
    public ResponseEntity<DniImagesResponse> getOwnDniImage() {
        final var authName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var userEntity = userService.findByEmail(authName);
            final String userDni = userEntity.getDni();
            final UserDniImagesDto dniImagesServiceDto = federateStateService.getDniImages(userDni);
            return new ResponseEntity<>(
                    new DniImagesResponse(dniImagesServiceDto.frontImage(), dniImagesServiceDto.backImage()),
                    HttpStatus.OK);
        } catch (FederateStateServiceException | UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Retrieves the DNI images of a user based on the provided DNI.
     *
     * @param userDni The DNI of the user whose images are to be retrieved.
     * @return ResponseEntity containing the front and back images of the specified
     *         user's DNI.
     * @throws ResponseStatusException if an error occurs while retrieving the
     *                                 images.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or " + "hasRole('SECRETARIO')")
    @GetMapping("/dni/{userDni}")
    public ResponseEntity<DniImagesResponse> getUserDniImage(final @PathVariable String userDni) {
        try {
            final UserDniImagesDto dniImagesServiceDto = federateStateService.getDniImages(userDni);
            return new ResponseEntity<>(
                    new DniImagesResponse(dniImagesServiceDto.frontImage(), dniImagesServiceDto.backImage()),
                    HttpStatus.OK);
        } catch (FederateStateServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates the DNI documents (front and back) for the currently authenticated
     * user.
     *
     * <p>
     * This endpoint accepts new front and back images for the user's DNI, validates
     * them, and updates the user's federate state accordingly.
     * </p>
     *
     * @param frontDni the front image of the DNI document
     * @param backDni  the back image of the DNI document
     * @return a ResponseEntity containing the updated federate state response
     * @throws ResponseStatusException if there is an error updating the DNI
     *                                 documents
     */
    @PatchMapping("/updateDni")
    public ResponseEntity<FederateStateResponse> updateDni(@RequestParam final MultipartFile frontDni,
            @RequestParam final MultipartFile backDni) {
        final var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var entity = federateStateService.updateDni(userEmail, frontDni, backDni);
            return new ResponseEntity<>(new FederateStateResponse(entity), HttpStatus.OK);
        } catch (UserServiceException | FederateStateServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
