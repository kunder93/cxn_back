
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of create chess question.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public final class CreateChessQuestionRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3231311822214257705L;

  /**
   * The chess question user email.
   */
  @NotBlank
  private String email;

  /**
   * The chess question category.
   */
  @NotBlank
  private String category;

  /**
   * The chess question topic.
   */
  @NotBlank
  private String topic;

  /**
   * The chess question message.
   */
  @NotBlank
  private String message;

}
