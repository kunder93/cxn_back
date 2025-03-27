package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for requesting a one-time token for password recovery.
 *
 * <p>
 * Both email and dni are required to ensure that the request is secure.
 * </p>
 *
 * @param email the user's email
 * @param dni   the user's DNI
 */
public record OTTRequest(@NotNull(message = "Email must not be null")
@NotBlank(message = "Email must not be blank")
@Size(max = 255, message = "Email must not exceed 255 characters") String email,

        @NotNull(message = "DNI must not be null")
        @NotBlank(message = "DNI must not be blank")
        @Size(max = 9, message = "DNI must not exceed 9 characters") String dni) {
}