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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.form.responses.FederateStateExtendedResponseList;
import es.org.cxn.backapp.model.form.responses.FederateStateResponse;
import es.org.cxn.backapp.service.FederateStateService;

/**
 * The FederateController class handles HTTP requests related to federate state
 * management. It provides endpoints for confirming a federate state, federating
 * a member, retrieving federate state data for the authenticated user, and
 * fetching federate state data for all users.
 *
 * <p>
 * This controller supports methods for confirming a federate status, federating
 * members with DNI document uploads, and retrieving federate state information.
 * It uses the {@link FederateStateService} to interact with the federate state
 * business logic.
 *
 * <p>
 * Access control is enforced via {@code @PreAuthorize} annotations to allow
 * only specific roles to confirm federate status.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/user/federate")
public class FederateController {

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
     * Confirms the federate status of a user.
     *
     * <p>
     * This endpoint is restricted to users with roles 'ADMIN', 'PRESIDENTE', or
     * 'SECRETARIO'.
     *
     * @param userEmail the email of the user to confirm federate status for
     * @return a ResponseEntity containing the federate state response
     * @throws ResponseStatusException if there is an error confirming the federate
     *                                 state
     */
    @PatchMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    public ResponseEntity<FederateStateResponse> confirmFederate(@RequestParam final String userEmail) {

        try {
            final var result = federateStateService.confirmFederate(userEmail);
            return new ResponseEntity<>(new FederateStateResponse(result), HttpStatus.OK);
        } catch (FederateStateServiceException | UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Federates a member by uploading their DNI documents.
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
     * Retrieves the federate state for the currently authenticated user.
     *
     * @return a ResponseEntity containing the federate state response for the user
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
     * @return a ResponseEntity containing the federate state data for all users
     */
    @GetMapping("/getAll")
    public ResponseEntity<FederateStateExtendedResponseList> getFederateStateMembers() {
        final var entitiesList = federateStateService.getAll();

        final var result = FederateStateExtendedResponseList.fromEntities(entitiesList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
