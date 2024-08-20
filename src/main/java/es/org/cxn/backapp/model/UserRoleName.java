
package es.org.cxn.backapp.model;

/**
 * Enumeration representing the names of the user roles within the system.
 * Each role corresponds to a specific level of access or set of privileges.
 *
 * <p>
 * Roles include:
 * <ul>
 *   <li>ROLE_ADMIN - Grants all privileges within the system.</li>
 *   <li>ROLE_PRESIDENTE - Role of the president.</li>
 *   <li>ROLE_SECRETARIO - Role of the secretary.</li>
 *   <li>ROLE_TESORERO - Role of the treasurer.</li>
 *   <li>ROLE_SOCIO - Role of a regular member.</li>
 *   <li>ROLE_CANDIDATO_SOCIO - Role of a candidate for membership.</li>
 * </ul>
 *
 * <p>
 * These roles are used to define the permissions and access levels of different
 * users within the application.
 *
 * @author Santi
 */
public enum UserRoleName {
  /**
   * Admin role grants all privileges.
   */
  ROLE_ADMIN,

  /**
   * Role of the president.
   */
  ROLE_PRESIDENTE,

  /**
   * Role of the secretary.
   */
  ROLE_SECRETARIO,

  /**
   * Role of the treasurer.
   */
  ROLE_TESORERO,

  /**
   * Role of a regular member.
   */
  ROLE_SOCIO,

  /**
   * Role of a candidate for membership.
   */
  ROLE_CANDIDATO_SOCIO
}
