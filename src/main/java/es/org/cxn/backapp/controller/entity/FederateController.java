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

package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.form.responses.FederateStateExtendedResponseList;
import es.org.cxn.backapp.model.form.responses.FederateStateExtendedResponseList.FederateStateExtendedResponse;
import es.org.cxn.backapp.model.form.responses.FederateStateResponse;
import es.org.cxn.backapp.service.FederateStateService;

/**
 * The FederateController class handles HTTP requests related to federate state
 * management. It provides endpoints for confirming a federate state, federating
 * a member, retrieving federate state data for the authenticated user, updating
 * DNI information, and fetching federate state data for all users.
 *
 * <p>
 * This controller supports methods for confirming federate status, federating
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
 * </p>
 *
 * @see FederateStateService
 * @see ResponseEntity
 * @see ResponseStatusException
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/user/federate")
public class FederateController {

    private record ConfirmCancelFederateRequest(String userDni) {
    }

    private final FederateStateService federateStateService;

    /**
     * Constructs a new FederateController with the specified FederateStateService.
     *
     * @param federateStateServ the federate state service to be used
     * @throws NullPointerException if the federate state service is null
     */
    public FederateController(final FederateStateService federateStateServ) {
        super();
        federateStateService = checkNotNull(federateStateServ, "Received a null pointer as federate state service");
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
     * @throws ResponseStatusException if there is an error during the confirmation
     *                                 process
     */
    @PatchMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    public ResponseEntity<FederateStateExtendedResponse> confirmCancelFederate(
            @RequestBody final ConfirmCancelFederateRequest request) {
        try {
            final var result = federateStateService.confirmCancelFederate(request.userDni);
            return new ResponseEntity<>(new FederateStateExtendedResponse(result), HttpStatus.OK);
        } catch (FederateStateServiceException | UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Federates a member by uploading their DNI documents and enabling or disabling
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

        } catch (Exception e) {
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
            final var result = federateStateService.getFederateData(userEmail);
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
    public ResponseEntity<FederateStateExtendedResponseList> getFederateStateMembers() {
        final var entitiesList = federateStateService.getAll();

        final var result = FederateStateExtendedResponseList.fromEntities(entitiesList);

        return new ResponseEntity<>(result, HttpStatus.OK);
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
