
package es.org.cxn.backapp.model.form.responses;

/**
 * Represents the form used for responding to a user authentication request.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, encapsulating the authentication token (JWT) issued upon
 * successful user authentication.
 * </p>
 *
 * <p>
 * The {@code AuthenticationResponse} record is immutable, ensuring that the
 * JSON Web Token (JWT) remains consistent and unaltered throughout its
 * lifecycle once it is created.
 * </p>
 *
 * @param jwt The JSON Web Token (JWT) issued upon successful authentication.
 * This token is used to authorize subsequent requests made by the
 * authenticated user.
 *
 * @author Santiago Paz Perez
 */
public record AuthenticationResponse(String jwt) {
}
