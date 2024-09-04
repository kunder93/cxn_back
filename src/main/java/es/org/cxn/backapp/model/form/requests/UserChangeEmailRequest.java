
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents the form used by the controller to request a change of user email.
 * <p>
 * This is a DTO (Data Transfer Object), meant to facilitate communication
 * between the view and the controller. The record is immutable and provides
 * built-in implementations for methods such as {@code equals()},
 * {@code hashCode()}, and {@code toString()}.
 * <p>
 * Includes Java validation annotations to ensure that the email addresses
 * provided are not blank and are valid email addresses.
 *
 * @param email    The user's current email address.
 * @param newEmail The user's new email address.
 *
 * @author Santiago Paz.
 */
public record UserChangeEmailRequest(@NotBlank @Email
String email, @NotBlank @Email
String newEmail) {

}
