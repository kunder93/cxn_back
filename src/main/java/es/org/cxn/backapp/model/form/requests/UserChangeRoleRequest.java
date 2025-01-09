package es.org.cxn.backapp.model.form.requests;

import java.util.Collections;
import java.util.List;

import es.org.cxn.backapp.model.UserRoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents the form request used for adding or removing roles from a user.
 * <p>
 * This is a DTO (Data Transfer Object), meant to facilitate communication
 * between the view and the controller. It includes validation annotations to
 * ensure that the controller receives all the required data.
 * </p>
 * <p>
 * This record ensures immutability by creating a defensive copy of the
 * {@code userRoles} list in the constructor and returning an unmodifiable view
 * when accessed.
 * </p>
 *
 * @param email     The user's email address. This field is required and cannot
 *                  be empty. It must be a valid email format.
 * @param userRoles A list of user roles. This field cannot be empty and should
 *                  contain at least one role.
 *
 * @author Santiago Paz Perez
 */
public record UserChangeRoleRequest(@NotNull
@Email String email,
        @NotNull
        @NotEmpty List<UserRoleName> userRoles) {

    /**
     * Constructor for {@code UserChangeRoleRequest}.
     * <p>
     * This constructor creates a defensive copy of the {@code userRoles} list to
     * ensure immutability and prevent external modification of the internal state.
     * </p>
     *
     * @param email     The user's email address.
     * @param userRoles The list of roles to be assigned to the user.
     * @throws NullPointerException if {@code userRoles} is {@code null}.
     */
    public UserChangeRoleRequest {
        userRoles = List.copyOf(userRoles);
    }

    /**
     * Returns the list of user roles.
     * <p>
     * This method provides an unmodifiable view of the {@code userRoles} list,
     * ensuring that the internal state remains immutable.
     * </p>
     *
     * @return An unmodifiable list of {@code UserRoleName}.
     */
    @Override
    public List<UserRoleName> userRoles() {
        return Collections.unmodifiableList(userRoles);
    }
}
