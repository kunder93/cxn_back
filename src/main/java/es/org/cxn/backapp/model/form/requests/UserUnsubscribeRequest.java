
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request for unsubscribe an user.
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
public final class UserUnsubscribeRequest implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3112841234615975705L;

  /**
   * The user email.
   */
  @NotNull
  private String email;

  /**
   * The user password.
   */
  @NotNull
  private String password;

}
