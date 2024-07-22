
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of update company.
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
public final class ChangeChessQuestionHasSeenRequestForm
      implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -5644765320193491570L;

  /**
   * The chess question identifier.
   */
  private int id;

}
