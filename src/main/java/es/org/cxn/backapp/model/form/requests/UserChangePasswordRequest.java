
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the user form used by controller as request to change user
 * password.
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
public final class UserChangePasswordRequest implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3129178499215975705L;

  /**
   * The user email.
   */
  private String email;

  /**
   * The user current password.
   */
  private String currentPassword;

  /**
   * The user new password.
   */
  private String newPassword;

}
