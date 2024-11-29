
package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.user.UserType;

/**
 * Represents the response form used by the controller for user sign-up.
 * <p>
 * This record is a Data Transfer Object (DTO) that facilitates communication
 * between the view and the controller, providing an immutable representation of
 * the user data.
 * </p>
 *
 * @param dni           The user's DNI.
 * @param name          The user's name.
 * @param firstSurname  The user's first surname.
 * @param secondSurname The user's second surname.
 * @param birthDate     The user's birth date.
 * @param gender        The user's gender.
 * @param email         The user's email.
 * @param kindMember    The user's type of membership.
 * @param userRoles     The set of user roles.
 *
 * @author Santiago Paz
 */
public record SignUpResponseForm(String dni, String name, String firstSurname, String secondSurname,
        LocalDate birthDate, String gender, String email, UserType kindMember, Set<UserRoleName> userRoles) {

    /**
     * Creates a {@link SignUpResponseForm} from a {@link UserEntity}.
     * <p>
     * This static factory method initializes a new {@code SignUpResponseForm} using
     * the data from the provided {@code UserEntity}.
     * </p>
     *
     * @param user The {@code UserEntity} containing user data.
     * @return A new {@code SignUpResponseForm} with values derived from the entity.
     */
    public static SignUpResponseForm fromEntity(final UserEntity user) {
        final Set<UserRoleName> userRoles = EnumSet.noneOf(UserRoleName.class);
        user.getRoles().forEach(role -> userRoles.add(role.getName()));

        return new SignUpResponseForm(user.getDni(), user.getProfile().getName(), user.getProfile().getFirstSurname(),
                user.getProfile().getSecondSurname(), user.getProfile().getBirthDate(), user.getProfile().getGender(),
                user.getEmail(), user.getKindMember(), userRoles);
    }
}
