
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.UserRoleName;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Represents the form request used for adding or removing roles from a user.
 * <p>
 * This is a DTO (Data Transfer Object), meant to facilitate communication
 * between
 * the view and the controller. It includes validation annotations to ensure
 * that the controller receives all the required data.
 * </p>
 * <p>
 * Note: In a record, validation annotations cannot be directly applied to the
 * components. Ensure that validation is handled appropriately in the
 * application logic or by using a custom validation mechanism.
 * </p>
 *
 * @param email The user's email address. This field is required and cannot be
 * empty.
 * @param userRoles A list of user roles. This field cannot be empty and should
 * contain at least one role.
 *
 * @author Santiago Paz Perez
 */
public record UserChangeRoleRequestForm(@NotEmpty
String email, @NotNull @NotEmpty
List<UserRoleName> userRoles) {

}
