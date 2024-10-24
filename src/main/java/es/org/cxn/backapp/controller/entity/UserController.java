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

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.form.requests.UserChangeEmailRequest;
import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;
import es.org.cxn.backapp.model.form.requests.UserUnsubscribeRequest;
import es.org.cxn.backapp.model.form.requests.UserUpdateRequestForm;
import es.org.cxn.backapp.model.form.responses.ProfileImageResponse;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.UserListDataResponse;
import es.org.cxn.backapp.model.form.responses.UserUpdateResponseForm;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;

/**
 * Rest controller for managing user-related operations.
 *
 * <p>
 * This controller provides endpoints for authenticated users to:
 * <ul>
 * <li>Retrieve their user data</li>
 * <li>Update their personal information</li>
 * <li>Change their email, password, and membership status</li>
 * <li>Unsubscribe from the system</li>
 * </ul>
 *
 * <p>
 * Authorization is enforced for all endpoints. The user must be authenticated
 * to access most of the methods, and specific roles (ADMIN, PRESIDENTE,
 * TESORERO, SECRETARIO) are required to access user lists.
 *
 * @see UserService
 * @see UserUpdateRequestForm
 * @see UserChangeEmailRequest
 * @see UserChangeKindMemberRequest
 * @see UserChangePasswordRequest
 * @see UserUnsubscribeRequest
 * @see UserUpdateResponseForm
 * @see UserListDataResponse
 * @see UserDataResponse
 * @see UserServiceUpdateDto
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * Represents a request to update the profile image URL for a user.
     *
     * <p>
     * This class contains the profile image URL that will be used to update the
     * user's profile image in the system.
     * </p>
     */
    public class ProfileImageUpdateRequest {

        /** The URL of the new profile image. */
        private String profileImageUrl;

        /**
         * Default constructor.
         */
        ProfileImageUpdateRequest() {
            // Default public constructor.
        }

        /**
         * Gets the profile image URL.
         *
         * @return the URL of the profile image.
         */
        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        /**
         * Sets the profile image URL.
         *
         * @param profileImageUrl the new URL of the profile image.
         */
        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    /**
     * The user service to handle business logic related to user operations.
     */
    private final UserService userService;

    /**
     * Constructs the controller with a given user service.
     *
     * @param service the user service instance, must not be null.
     */
    public UserController(final UserService service) {
        super();
        userService = checkNotNull(service, "Received a null pointer as user service");
    }

    /**
     * Changes the user's email.
     *
     * <p>
     * The user must be authenticated to access this endpoint.
     *
     * @param userChangeEmailRequest the request containing the current and new
     *                               email.
     * @return a {@link UserDataResponse} with the updated email.
     * @throws ResponseStatusException if the update fails.
     */
    @PatchMapping("/changeEmail")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDataResponse> changeUserEmail(
            @RequestBody final UserChangeEmailRequest userChangeEmailRequest) {
        final UserEntity result;
        try {
            result = userService.changeUserEmail(userChangeEmailRequest.email(), userChangeEmailRequest.newEmail());
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(new UserDataResponse(result), HttpStatus.OK);
    }

    /**
     * Changes the kind of membership of a user.
     *
     * <p>
     * The user must be authenticated to access this endpoint.
     *
     * @param userChangeKindMemberReq the request containing the user's email and
     *                                new kind of member.
     * @return a {@link UserDataResponse} with the updated membership information.
     * @throws ResponseStatusException if the update fails.
     */
    @PatchMapping("/changeKindOfMember")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDataResponse> changeUserKindOfMember(
            @RequestBody final UserChangeKindMemberRequest userChangeKindMemberReq) {
        final UserEntity result;
        try {
            result = userService.changeKindMember(userChangeKindMemberReq.email(),
                    userChangeKindMemberReq.kindMember());
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(new UserDataResponse(result), HttpStatus.OK);
    }

    /**
     * Changes the user's password.
     *
     * <p>
     * The user must be authenticated to access this endpoint.
     *
     * @param userChangePasswordRequest the request containing the current and new
     *                                  password.
     * @return a {@link UserDataResponse} with the updated password information.
     * @throws ResponseStatusException if the update fails.
     */
    @PatchMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDataResponse> changeUserPassword(
            @RequestBody final UserChangePasswordRequest userChangePasswordRequest) {
        final UserEntity result;
        try {
            result = userService.changeUserPassword(userChangePasswordRequest.email(),
                    userChangePasswordRequest.currentPassword(), userChangePasswordRequest.newPassword());
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(new UserDataResponse(result), HttpStatus.OK);
    }

    /**
     * Retrieves the list of all users' data.
     *
     * <p>
     * Requires the user to have one of the following roles: ADMIN, PRESIDENTE,
     * TESORERO, SECRETARIO.
     *
     * @return a {@link UserListDataResponse} containing a list of all users.
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or " + "hasRole('SECRETARIO')")
    public ResponseEntity<UserListDataResponse> getAllUserData() {
        final var users = userService.getAll();
        final var response = UserListDataResponse.fromUserEntities(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves the authenticated user's data.
     *
     * <p>
     * The user must be authenticated to access this endpoint.
     *
     * @return a {@link UserDataResponse} containing the authenticated user's
     *         information.
     * @throws ResponseStatusException if the user is not authenticated.
     */
    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDataResponse> getUserData() {
        final var authName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var user = userService.findByEmail(authName);
            return new ResponseEntity<>(new UserDataResponse(user), HttpStatus.OK);
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    /**
     * Retrieves the profile image of the authenticated user.
     *
     * <p>
     * This endpoint allows authenticated users to obtain their profile image. It
     * retrieves the user's information based on the authenticated email and returns
     * the profile image as a response.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a {@link ProfileImageResponse}
     *         with the user's profile image data, along with an HTTP status of 200
     *         OK if successful.
     * @throws ResponseStatusException if the user is not authenticated or if there
     *                                 is an error retrieving the profile image,
     *                                 resulting in a 400 Bad Request response.
     */
    @GetMapping("/obtainProfileImage")
    public ResponseEntity<ProfileImageResponse> obtainProfileImage() {
        final var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var user = userService.findByEmail(userName);
            final var imageProfile = userService.getProfileImage(user.getDni());

            return new ResponseEntity<>(imageProfile, HttpStatus.OK);
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Unsubscribes the user from the system.
     *
     * <p>
     * The user must be authenticated to access this endpoint.
     *
     * @param userUnsubscribeRequest the request containing the user's email and
     *                               password.
     * @return a {@link ResponseEntity} indicating the result of the operation.
     * @throws ResponseStatusException if the unsubscription fails.
     */
    @DeleteMapping("/unsubscribe")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HttpStatus> unsubscribeUser(
            @RequestBody final UserUnsubscribeRequest userUnsubscribeRequest) {
        try {
            userService.unsubscribe(userUnsubscribeRequest.email(), userUnsubscribeRequest.password());
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates the authenticated user's data.
     *
     * <p>
     * The user must be authenticated to access this endpoint.
     *
     * @param userUpdateRequestForm the form containing the updated user data.
     * @return a {@link UserUpdateResponseForm} with the updated user information.
     * @throws ResponseStatusException if the update fails.
     */
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserUpdateResponseForm> updateUserData(
            @RequestBody final UserUpdateRequestForm userUpdateRequestForm) {
        final var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        final var userServiceUpdateForm = new UserServiceUpdateDto(userUpdateRequestForm.name(),
                userUpdateRequestForm.firstSurname(), userUpdateRequestForm.secondSurname(),
                userUpdateRequestForm.birthDate(), userUpdateRequestForm.gender());
        try {
            final var userUpdated = userService.update(userServiceUpdateForm, userName);

            return new ResponseEntity<>(new UserUpdateResponseForm(userUpdated), HttpStatus.OK);
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Endpoint para subir y actualizar la imagen de perfil.
     *
     * @param profileImage el archivo de imagen a subir.
     * @return una respuesta con los datos del usuario actualizado.
     */
    @PatchMapping("/uploadProfileImageFile")
    public ResponseEntity<ProfileImageResponse> uploadProfileImage(@RequestParam MultipartFile profileImage) {
        final var userName = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            final var userEntity = userService.findByEmail(userName);

            // Validar el archivo si es necesario (tipo, tamaño, etc.)
            if (profileImage.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo no debe estar vacío");
            }

            // Puedes agregar más validaciones aquí (por ejemplo, tipos MIME permitidos)

            // Llama al servicio para guardar la URL o archivo en tu sistema
            var updatedUser = userService.saveProfileImageFile(userEntity.getDni(), profileImage);

            return new ResponseEntity<>(new ProfileImageResponse(updatedUser.getProfileImage()), HttpStatus.OK);

        } catch (IOException | UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates the profile image URL for the authenticated user.
     *
     * <p>
     * This method handles PATCH requests to update the profile image URL. It
     * retrieves the authenticated user's name from the security context and invokes
     * the user service to update the profile image URL. If successful, it returns a
     * response containing the updated profile image information.
     * </p>
     *
     * @param requestBody a map containing the new profile image URL to be set under
     *                    the key "profileImageUrl".
     * @return a {@link ResponseEntity} containing a {@link ProfileImageResponse}
     *         with the updated profile image data, along with an HTTP status of 200
     *         OK if successful.
     * @throws ResponseStatusException if the update fails due to a
     *                                 {@link UserServiceException}, resulting in a
     *                                 400 Bad Request response.
     */
    @PatchMapping("/uploadProfileImage")
    public ResponseEntity<ProfileImageResponse> uploadProfileImageUrl(
            @RequestBody final Map<String, String> requestBody) {
        final var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            String profileImageUrl = requestBody.get("profileImageUrl"); // Extract the value from the map
            final var userUpdated = userService.saveProfileImage(userName, profileImageUrl);
            return new ResponseEntity<>(new ProfileImageResponse(userUpdated.getProfileImage()), HttpStatus.OK);
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
