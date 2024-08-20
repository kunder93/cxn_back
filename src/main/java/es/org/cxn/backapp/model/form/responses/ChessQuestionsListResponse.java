
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;

import java.util.Collection;

/**
 * Represents the form used by the controller as a response for requesting all
 * chess questions.
 * <p>
 * This is a Data Transfer Object (DTO), meant to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param chessQuestionList Collection with all stored chess questions.
 *
 * @author Santiago Paz.
 */
public record ChessQuestionsListResponse(
      Collection<ChessQuestionResponse> chessQuestionList
) {

  /**
   * Static factory method to create an instance of ChessQuestionsListResponse.
   *
   * @param value The collection of persistent chess questions.
   * @return A new ChessQuestionsListResponse instance.
   */
  public static ChessQuestionsListResponse
        from(final Collection<PersistentChessQuestionEntity> value) {
    Collection<ChessQuestionResponse> responses = value.stream()
          .map(
                e -> new ChessQuestionResponse(
                      e.getIdentifier(), e.getEmail(), e.getCategory(),
                      e.getTopic(), e.getMessage(), e.getDate(), e.isSeen()
                )
          ).toList();
    return new ChessQuestionsListResponse(responses);
  }
}
