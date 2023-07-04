
package es.org.cxn.backapp.model.form.requests;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Objects;

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
  private String userEmail;

  /**
   * User role name field.
   */
  @NotEmpty
  private String roleName;

  /**
   * Constructs a DTO with no data.
   */
  public UserChangeRoleRequestForm() {
    super();
  }

  /**
   * Constructs a DTO with all data.
   *
   * @param email         the userEmail field.
   * @param roleNameValue the roleName field.
   */
  public UserChangeRoleRequestForm(
        final String email, final String roleNameValue
  ) {
    super();
    this.userEmail = email;
    this.roleName = roleNameValue;

  }

  /**
   * Returns the value of the userEmail field.
   *
   * @return the value of the userEmail field.
   */
  public String getUserEmail() {
    return userEmail;
  }

  /**
   * Returns the value of the roleName field.
   *
   * @return the value of the roleName field.
   */
  public String getRoleName() {
    return roleName;
  }

  /**
   * Sets the value of the userEmail field.
   *
   * @param value the new value for the userEmail field.
   */
  public void setUserEmail(final String value) {
    this.userEmail = checkNotNull(value, "Received a null pointer as name");
  }

  /**
   * Sets the value of the roleName field.
   *
   * @param value the new value for the roleName field.
   */
  public void setRoleName(final String value) {
    this.roleName =
          checkNotNull(value, "Received a null pointer as first surname");
  }

  /**
   * The hash code method.
   */
  @Override
  public int hashCode() {
    return Objects.hash(roleName, userEmail);
  }

  /**
   * The equals method.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    var other = (UserChangeRoleRequestForm) obj;
    return Objects.equals(roleName, other.roleName)
          && Objects.equals(userEmail, other.userEmail);
  }

  /**
   * The to string method.
   */
  @Override
  public String toString() {
    return "UserChangeRoleRequestForm [userEmail=" + userEmail + ", roleName="
          + roleName + "]";
  }

}
