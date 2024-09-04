
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the form used by the controller as a request to create a chess
 * question.
 * <p>
 * This is a Data Transfer Object (DTO) meant to facilitate communication
 * between the view layer and the controller. It includes Java validation
 * annotations to ensure that all required data is provided and adheres to the
 * necessary constraints.
 * </p>
 *
 * <p>
 * This DTO is immutable and includes the following fields:
 * </p>
 *
 * <ul>
 *   <li>{@code email}: The email of the user submitting the question.
 *   This field must not be blank and must adhere to email format
 *   validation.</li>
 *   <li>{@code category}: The category of the chess question.
 *   This field must not be blank and must not exceed 30 characters.</li>
 *   <li>{@code topic}: The topic related to the chess question.
 *   This field must not be blank and must not exceed 50 characters.</li>
 *   <li>{@code message}: The content of the chess question.
 *   This field must not be blank.</li>
 * </ul>
 *
 * @param email   The email address of the user submitting the question.
 * Must not be blank and must be a valid email format.
 * @param category The category to which the chess question belongs.
 * Must not be blank and must not exceed 30 characters.
 * @param topic    The topic of the chess question. Must not be blank and
 * must not exceed 50 characters.
 * @param message  The content of the chess question. Must not be blank.
 *
 * @author Santiago Paz
 */
public record CreateChessQuestionRequest(

      @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
      @Email(message = ValidationConstants.EMAIL_INVALID_MESSAGE)
      @Size(
            max = ValidationConstants.EMAIL_MAX_SIZE,
            message = ValidationConstants.EMAIL_SIZE_MESSAGE
      )
      String email,

      @NotBlank(message = ValidationConstants.CATEGORY_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.CATEGORY_MAX_SIZE,
            message = ValidationConstants.CATEGORY_SIZE_MESSAGE
      )
      String category,

      @NotBlank(message = ValidationConstants.TOPIC_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.TOPIC_MAX_SIZE,
            message = ValidationConstants.TOPIC_SIZE_MESSAGE
      )
      String topic,

      @NotBlank(message = ValidationConstants.MESSAGE_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.MESSAGE_MAX_LENGTH,
            message = ValidationConstants.MESSAGE_MAX_LENGTH_MESSAGE
      )
      String message
) {

}
