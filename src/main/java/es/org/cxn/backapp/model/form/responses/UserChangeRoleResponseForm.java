
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.UserRoleName;

import java.util.List;

/**
 * Represents the form used for responding to a user's role change.
 * <p>
 * This is a Data Transfer Object (DTO) designed to facilitate communication
 * between the view and the controller. It includes the user's name and the list
 * of roles assigned to the user after the role change operation.
 * </p>
 * <p>
 * The class ensures that the required data is correctly passed from the server
 * to the client and can be validated using Java's validation annotations.
 * This helps maintain data integrity and consistency in the application.
 * </p>
 *
 * @param userName  the username of the user whose roles are being changed.
 *                  This field represents the unique identifier of the user
 *                  in the system.
 * @param userRoles the list of {@link UserRoleName} objects representing the
 *                  roles assigned to the user after the change. This list
 *                  includes all the roles that the user currently holds,
 *                  providing a clear picture of the user's permissions.
 *
 * @author Santiago Paz Perez
 */
public record UserChangeRoleResponseForm(
      String userName, List<UserRoleName> userRoles
) {

}
