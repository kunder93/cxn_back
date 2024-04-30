
package es.org.cxn.backapp.exceptions;

import es.org.cxn.backapp.model.UserRoleName;

/**
 * Exception thrown by service when role with this name is found Cannot exist
 * more than one role with same name.
 *
 * @author Santiago Paz.
 *
 */
public final class RoleNameExistsException extends Exception {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 204929958575646602L;

  /**
   * The role name field.
   */
  private final UserRoleName roleName;

  /**
   * Main constructor.
   *
   * @param value the role name.
   */
  public RoleNameExistsException(final UserRoleName value) {
    super(value.toString());
    this.roleName = value;
  }

  /**
   * Getter for roleName field.
   *
   * @return the role name.
   */
  public UserRoleName getRoleName() {
    return roleName;
  }

  @Override
  public String getMessage() {
    return "Role name: " + roleName + " already exists";
  }

}
