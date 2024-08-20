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

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.form.requests.UserChangeEmailRequest;
import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;
import es.org.cxn.backapp.model.form.requests.UserUnsubscribeRequest;
import es.org.cxn.backapp.model.form.requests.UserUpdateRequestForm;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.UserListDataResponse;
import es.org.cxn.backapp.model.form.responses.UserUpdateResponseForm;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rest controller for the example entities.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

  /**s
   * The user service.
   */
  private final UserService userService;

  /**
   * Constructs a controller with the specified dependencies.
   *
   * @param service example entity service.
   */
  public UserController(final UserService service) {
    super();

    userService = checkNotNull(service, "Received a null pointer as service");
  }

  /**
   * Returns a data from user.
   *
   * @return info for this user.
   */
  @CrossOrigin(origins = "*")
  @GetMapping()
  public ResponseEntity<UserDataResponse> getUserData() {
    final var authName =
          SecurityContextHolder.getContext().getAuthentication().getName();
    try {
      final var user = userService.findByEmail(authName);
      return new ResponseEntity<>(new UserDataResponse(user), HttpStatus.OK);
    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED, e.getMessage(), e
      );
    }
  }

  /**
   * Update user data.
   *
   * @param userUpdateRequestForm form with data to update user
   *                              {@link UserUpdateRequestForm}.
   * @return form with the updated user data.
   */
  @PostMapping()
  @CrossOrigin(origins = "*")
  public ResponseEntity<UserUpdateResponseForm> updateUserData(@RequestBody
  final UserUpdateRequestForm userUpdateRequestForm) {
    final var userName =
          SecurityContextHolder.getContext().getAuthentication().getName();

    final var name = userUpdateRequestForm.name();
    final var firstSurname = userUpdateRequestForm.firstSurname();
    final var secondSurname = userUpdateRequestForm.secondSurname();
    final var birthDate = userUpdateRequestForm.birthDate();
    final var gender = userUpdateRequestForm.gender();
    final var userServiceUpdateForm = new UserServiceUpdateDto(
          name, firstSurname, secondSurname, birthDate, gender
    );
    try {
      final var userUpdated =
            userService.update(userServiceUpdateForm, userName);
      return new ResponseEntity<>(
            new UserUpdateResponseForm(userUpdated), HttpStatus.OK
      );

    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
  }

  /**
   * Returns all users data.
   *
   * @return list with all users info.
   */
  @CrossOrigin(origins = "*")
  @GetMapping("/getAll")
  public ResponseEntity<UserListDataResponse> getAllUserData() {
    final var users = userService.getAll();
    // Use the fromUserEntities method to create the UserListDataResponse
    var response = UserListDataResponse.fromUserEntities(users);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Change user kind of member.
   *
   * @param userChangeKindMemberReq The email as user identifier and
   * new kind of member.
   * @return user data with new kind of member.
   */
  @CrossOrigin(origins = "*")
  @PatchMapping("/changeKindOfMember")
  public ResponseEntity<UserDataResponse> changeUserKindOfMember(@RequestBody
  final UserChangeKindMemberRequest userChangeKindMemberReq) {
    final UserEntity result;
    try {
      result = userService.changeKindMember(
            userChangeKindMemberReq.email(),
            userChangeKindMemberReq.kindMember()
      );
    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
    return new ResponseEntity<>(new UserDataResponse(result), HttpStatus.OK);
  }

  /**
   * Change user email.
   * @param userChangeEmailRequest The current and new emails.
   * @return user data with new email.
   */
  @CrossOrigin(origins = "*")
  @PatchMapping("/changeEmail")
  public ResponseEntity<UserDataResponse> changeUserEmail(@RequestBody
  final UserChangeEmailRequest userChangeEmailRequest) {
    final UserEntity result;
    try {
      result = userService.changeUserEmail(
            userChangeEmailRequest.email(), userChangeEmailRequest.newEmail()
      );
    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
    return new ResponseEntity<>(new UserDataResponse(result), HttpStatus.OK);
  }

  /**
   * Change user password.
   * @param userChangePasswordRequest The current and
   * new passwords and user email.
   * @return user data.
   */
  @CrossOrigin(origins = "*")
  @PatchMapping("/changePassword")
  public ResponseEntity<UserDataResponse> changeUserPassword(@RequestBody
  final UserChangePasswordRequest userChangePasswordRequest) {
    final UserEntity result;
    try {
      result = userService.changeUserPassword(
            userChangePasswordRequest.email(),
            userChangePasswordRequest.currentPassword(),
            userChangePasswordRequest.newPassword()
      );
    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
    return new ResponseEntity<>(new UserDataResponse(result), HttpStatus.OK);
  }

  /**
   * Unsubscribe an user.
   * @param userUnsubscribeRequest The current user unsubscribe request
   * with email and password.
   * @return  Ok or error.
   */
  @CrossOrigin(origins = "*")
  @DeleteMapping("/unsubscribe")
  public ResponseEntity<?> unsubscribeUser(@RequestBody
  final UserUnsubscribeRequest userUnsubscribeRequest) {
    try {
      userService.unsubscribe(
            userUnsubscribeRequest.email(), userUnsubscribeRequest.password()
      );
    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
