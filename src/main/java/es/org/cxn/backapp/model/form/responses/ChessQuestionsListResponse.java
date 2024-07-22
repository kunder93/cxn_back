
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.ChessQuestionEntity;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Represents the form used by controller as response for requesting all
 * chess questions.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
@Data
public final class ChessQuestionsListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -657599150638127210L;
  /**
   * List with all stored chess questions.
   */
  private List<ChessQuestionResponse> chessQuestionList = new ArrayList<>();

  /**
   * Constructor with provided parameters values.
   *
   * @param value The chess questions list.
   */
  public ChessQuestionsListResponse(
        final List<PersistentChessQuestionEntity> value
  ) {
    super();
    value.forEach(
          (ChessQuestionEntity e) -> this.chessQuestionList.add(
                ChessQuestionResponse.builder().id(e.getIdentifier())
                      .email(e.getEmail()).topic(e.getTopic())
                      .category(e.getCategory()).message(e.getMessage())
                      .date(e.getDate()).seen(e.isSeen()).build()
          )
    );
  }
}
