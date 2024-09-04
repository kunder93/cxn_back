
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.form.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents the form used to authenticate a user.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller by mapping all the necessary form fields related to user
 * authentication. Each field in the DTO corresponds directly to a field in the
 * authentication form.
 * </p>
 *
 * <p>
 * The {@code AuthenticationRequest} includes Java validation annotations to
 * ensure data integrity and consistency. These validations guarantee that the
 * controller only receives valid and properly formatted data, such as a valid
 * email address and a password that meets length requirements.
 * </p>
 *
 * Validation rules:
 *  <ul>
 *   <li><strong>Email:</strong> Must be a well-formed email address, cannot be
 *   blank, and must not exceed a certain length.</li>
 *   <li><strong>Password:</strong> Must not be null, and must be within a
 *   specific length range.</li>
 *  </ul>
 *
 * @param email The user's email address, which serves as the login identifier.
 *              It must be a valid, non-blank email and adhere to specified
 *              size constraints.
 * @param password The user's password, which is required for authentication.
 *                 It must be non-null and its length must fall within the
 *                 specified limits.
 *
 * @author Santiago Paz Perez
 */
public record AuthenticationRequest(
      @NotBlank(message = Constants.EMAIL_NOT_VALID) @Size(
            max = Constants.EMAIL_MAX_SIZE,
            message = Constants.MAX_SIZE_EMAIL_MESSAGE
      ) @Email(message = Constants.EMAIL_NOT_VALID)
      String email,

      @NotNull(message = Constants.NOT_NULL_PASSWORD) @Size(
            min = Constants.MIN_PASSWORD_LENGTH,
            max = Constants.MAX_PASSWORD_LENGTH,
            message = Constants.LENGTH_PASSWORD_MESSAGE
      )
      String password
) {
}
