
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents the form used by the controller as a request to create a
 * chess question.
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
 *   This field must not be blank.</li>
 *   <li>{@code category}: The category of the chess question.
 *   This field must not be blank.</li>
 *   <li>{@code topic}: The topic related to the chess question.
 *   This field must not be blank.</li>
 *   <li>{@code message}: The content of the chess question.
 *   This field must not be blank.</li>
 * </ul>
 *
 * @param email   The email address of the user submitting the question.
 * Must not be blank.
 * @param category The category to which the chess question belongs.
 * Must not be blank.
 * @param topic    The topic of the chess question. Must not be blank.
 * @param message  The content of the chess question. Must not be blank.
 *
 * @author Santiago Paz
 */
public record CreateChessQuestionRequestForm(
      @NotBlank(message = "Email must not be blank")
      String email, @NotBlank(message = "Category must not be blank")
      String category, @NotBlank(message = "Topic must not be blank")
      String topic, @NotBlank(message = "Message must not be blank")
      String message
) {

}
