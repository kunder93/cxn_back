
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request to change user email.
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
public final class UserChangeEmailRequest implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3156145688215915714L;

  /**
   * The user current email.
   */
  @NotBlank
  @Email
  private String email;

  /**
   * The user new email
   */
  @NotBlank
  @Email
  private String newEmail;

}
