
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Represents the form used by the controller as a request to update the "seen"
 * status of a chess question.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, capturing the necessary information required to change
 * the "seen" status of a specific chess question.
 * </p>
 *
 * <p>
 * The {@code ChangeChessQuestionHasSeenRequest} includes an identifier
 * field for the chess question, which is used to update its "seen" status in
 * the system.
 * </p>
 *
 * @param id The unique identifier of the chess question whose "seen" status
 * is to be updated. This field is mandatory, must be a positive integer, and
 * should not exceed 1000.
 *
 * <p>
 * Includes Java validation annotations to ensure that the necessary data
 * is provided and adheres to the specified constraints.
 * </p>
 *
 * @author Santiago Paz
 */
public record ChangeChessQuestionHasSeenRequest(
      @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
      @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE)
      @Max(
            value = ValidationConstants.MAX_ID,
            message = ValidationConstants.ID_MAX_MESSAGE
      )
      int id
) {
}
