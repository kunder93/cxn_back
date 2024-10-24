
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the user form used by the controller as a request to change the
 * user's password.
 * <p>
 * This is a DTO (Data Transfer Object) used for communication between the view
 * and the controller. The record is immutable and provides built-in
 * implementations for methods such as {@code equals()}, {@code hashCode()},
 * and {@code toString()}.
 * <p>
 * The record includes fields for the user email, current password, and new
 * password.
 *
 * @param email          The user's email address.
 * @param currentPassword The user's current password.
 * @param newPassword     The user's new password.
 *
 * @author Santiago Paz.
 */
public record UserChangePasswordRequest(
      @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
      @Email(message = ValidationConstants.EMAIL_INVALID_MESSAGE)
      String email,

      @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE) @Size(
            min = ValidationConstants.PASSWORD_MIN_LENGTH,
            max = ValidationConstants.PASSWORD_MAX_LENGTH,
            message = ValidationConstants.PASSWORD_SIZE_MESSAGE
      )
      String currentPassword,

      @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE) @Size(
            min = ValidationConstants.PASSWORD_MIN_LENGTH,
            max = ValidationConstants.PASSWORD_MAX_LENGTH,
            message = ValidationConstants.PASSWORD_SIZE_MESSAGE
      )
      String newPassword
) {
}
