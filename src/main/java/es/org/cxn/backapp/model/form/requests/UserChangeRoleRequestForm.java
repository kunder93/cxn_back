
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.UserRoleName;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form request used for add or remove roles from user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class UserChangeRoleRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 726911880297432628L;

  /**
   * Name field.
   * <p>
   * This is a required field and can't be empty.
   */
  @NotEmpty
  private String email;

  /**
   * User role name field.
   * No empty and not null, user almost have one role.
   */
  @NotEmpty
  private List<UserRoleName> userRoles;

}
