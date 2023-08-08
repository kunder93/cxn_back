
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

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
public final class UserChangeKindMemberRequest implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 726912280697412328L;

  /**
   * User email name field.
   */
  @NotEmpty
  private String email;

  /**
   * kindMember field.
   * <p>
   * This is a required field and can't be empty.
   */
  @NotEmpty
  private UserType kindMember;

}