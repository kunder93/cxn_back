
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/**
 * Represents the request form used by the controller to update user data.
 * <p>
 * This is a Data Transfer Object (DTO) meant to facilitate communication
 * between the view layer and the controller. It includes Java validation
 * annotations to ensure that the controller receives all required data and
 * that the data adheres to the expected constraints.
 * </p>
 *
 * <p>
 * This DTO is immutable and contains the following fields:
 * </p>
 *
 * <ul>
 *   <li>{@code name}: The user's name. Must not be empty.</li>
 *   <li>{@code firstSurname}: The user's first surname. Must not be
 *   empty.</li>
 *   <li>{@code secondSurname}: The user's second surname. Must not be
 *   empty.</li>
 *   <li>{@code birthDate}: The user's birth date. Must be a past date and not
 *   null.</li>
 *   <li>{@code gender}: The user's gender. Must not be empty.</li>
 * </ul>
 *
 * @param name         The name of the user. This field must not be empty.
 * @param firstSurname The first surname of the user. This field must not be
 * empty.
 * @param secondSurname The second surname of the user. This field must not be
 * empty.
 * @param birthDate    The birth date of the user. This field must be a past
 * date and not null.
 * @param gender       The gender of the user. This field must not be empty.
 *
 * @author Santiago Paz Perez
 */
public record UserUpdateRequestForm(
      @NotEmpty(message = "Name must not be empty")
      String name, @NotEmpty(message = "First surname must not be empty")
      String firstSurname,
      @NotEmpty(message = "Second surname must not be empty")
      String secondSurname, @NotNull(message = "Birth date must not be null")
      @Past(message = "Birth date must be a past date")
      LocalDate birthDate, @NotEmpty(message = "Gender must not be empty")
      String gender
) {

}
