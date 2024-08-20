
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotNull;

/**
 * Represents the form used by the controller as a request to unsubscribe a
 * user.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, ensuring that the necessary information is provided for
 * a user to be unsubscribed from the system.
 * </p>
 *
 * <p>
 * The {@code UserUnsubscribeRequest} includes fields for the user's email
 * and password, which are required to authenticate the user before processing
 * the unsubscribe request.
 * </p>
 *
 * @param email    The email address of the user requesting to unsubscribe.
 * This field is mandatory and cannot be null.
 * @param password The password associated with the user's account.
 * This field is mandatory and cannot be null.
 *
 * <p>
 * The fields are validated using Java validation annotations to ensure that
 * non-null values are provided.
 * </p>
 *
 * @author Santiago Paz
 */
public record UserUnsubscribeRequest(@NotNull
String email, @NotNull
String password) {

}
