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
import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.form.requests.UserUpdateRequestForm;
import es.org.cxn.backapp.model.form.responses.UserDataResponse;
import es.org.cxn.backapp.model.form.responses.UserListDataResponse;
import es.org.cxn.backapp.model.form.responses.UserUpdateResponseForm;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.UserServiceUpdateForm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
  @CrossOrigin
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
  public ResponseEntity<UserUpdateResponseForm> updateUserData(@RequestBody
  final UserUpdateRequestForm userUpdateRequestForm) {
    final var userName =
          SecurityContextHolder.getContext().getAuthentication().getName();

    final var name = userUpdateRequestForm.getName();
    final var firstSurname = userUpdateRequestForm.getFirstSurname();
    final var secondSurname = userUpdateRequestForm.getSecondSurname();
    final var birthDate = userUpdateRequestForm.getBirthDate();
    final var gender = userUpdateRequestForm.getGender();
    final var userServiceUpdateForm = new UserServiceUpdateForm(
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
  @CrossOrigin
  @GetMapping("/getAll")
  public ResponseEntity<UserListDataResponse> getAllUserData() {
    final var users = userService.getAll();
    return new ResponseEntity<>(new UserListDataResponse(users), HttpStatus.OK);
  }

  /**
   * Change user kind of member.
   *
   * @param userChangeKindMemberRequest The email as user identifier and new kind of member.
   * @return user data with new kind of member.
   */
  @CrossOrigin
  @PatchMapping("/changeKindOfMember")
  public ResponseEntity<UserDataResponse> changeUserKindOfMember(@RequestBody
  final UserChangeKindMemberRequest userChangeKindMemberRequest) {
    UserEntity result;
    try {
      result = userService.changeKindMember(
            userChangeKindMemberRequest.getEmail(),
            userChangeKindMemberRequest.getKindMember()
      );
    } catch (UserServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }

    return new ResponseEntity<>(new UserDataResponse(result), HttpStatus.OK);
  }

}
