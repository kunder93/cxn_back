
package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDateTime;

/**
 * Represents the form used by the controller as a response for requesting
 * a chess question.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param id The chess question identifier.
 * @param email The email of the person who sent the question.
 * @param category The chess question category.
 * @param topic The chess question topic.
 * @param message The chess question message.
 * @param date The chess question date.
 * @param seen The chess question seen state.
 *
 * @author Santiago Paz.
 */
public record ChessQuestionResponse(
      int id, String email, String category, String topic, String message,
      LocalDateTime date, boolean seen
) {
  // No need for additional methods unless specific functionality is needed
}
