
package es.org.cxn.backapp.model.form.requests;

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
      String email, String currentPassword, String newPassword
) {

}
