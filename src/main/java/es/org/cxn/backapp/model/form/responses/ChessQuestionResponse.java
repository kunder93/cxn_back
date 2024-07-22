
package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response for requesting
 * chess question.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ChessQuestionResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3199989826999937705L;

  /**
   * The chess question identifier.
   */
  private int id;

  /**
   * The email of who send the question.
   */
  private String email;

  /**
   * The chess question category.
   */
  private String category;

  /**
   * The chess question topic.
   */
  private String topic;

  /**
   * The chess question message.
   */
  private String message;

  /**
   * The chess question date.
   */
  private LocalDateTime date;

  /**
   * The chess question seen state.
   */
  private boolean seen;

}
