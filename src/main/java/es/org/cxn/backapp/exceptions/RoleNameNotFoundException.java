
package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown by service when role with provided name not found.
 *
 * @author Santiago Paz.
 *
 */
public final class RoleNameNotFoundException extends Exception {
  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 7474112384810484613L;

  /**
   * The role name field.
   */
  private final String roleName;

  /**
   * Main constructor.
   *
   * @param value the role name that throw exception.
   */
  public RoleNameNotFoundException(final String value) {
    super(value);
    this.roleName = value;
  }

  /**
   * Getter for role name field.
   *
   * @return the role name.
   */
  public String getRoleName() {
    return roleName;
  }

  @Override
  public String getMessage() {
    return "Role with name: " + roleName + " not found";
  }

}
