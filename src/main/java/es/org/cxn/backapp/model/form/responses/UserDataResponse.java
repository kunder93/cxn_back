
package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

/**
 * Represents the form used for responding to authenticated user data requests.
 * <p>
 * This record is a DTO, meant to facilitate communication between the view and
 * the controller, mapping all the values from the form. Each field in the
 * record corresponds to a field in the form.
 * <p>
 * The record is immutable and provides an automatic implementation of equals,
 * hashCode, and toString methods. The fields include user identification,
 * personal details, and role information.
 * <p>
 * The user roles are represented as a set of {@link UserRoleName}. The record
 * also includes a nested {@link AddressResponse} record for the user's address.
 * </p>
 *
 * @param dni             The user's DNI (identification number).
 * @param name            The user's first name.
 * @param firstSurname    The user's first surname.
 * @param secondSurname   The user's second surname.
 * @param gender          The user's gender.
 * @param birthDate       The user's birth date.
 * @param email           The user's email address.
 * @param kindMember      The type of membership the user holds, represented by
 *                        {@link UserType}.
 * @param userAddress     The user's address, represented by
 *                        {@link AddressResponse}.
 * @param userRoles       The set of role names associated with the user,
 *                        represented by {@link UserRoleName}.
 *
 * @param profileImageUrl THe url of profile image selected.
 * @author Santiago Paz Perez
 */
public record UserDataResponse(String dni, String name, String firstSurname, String secondSurname, String gender,
        LocalDate birthDate, String email, UserType kindMember, AddressResponse userAddress,
        Set<UserRoleName> userRoles) {

    /**
     * Constructs a {@code UserDataResponse} record from a {@link UserEntity}.
     * <p>
     * This constructor initializes all the fields of the record based on the values
     * in the provided {@code UserEntity}. The user's roles are extracted and stored
     * in a set.
     * </p>
     *
     * @param user The {@code UserEntity} from which to create the response record.
     */
    public UserDataResponse(final UserEntity user) {
        this(user.getDni(), user.getName(), user.getFirstSurname(), user.getSecondSurname(), user.getGender(),
                user.getBirthDate(), user.getEmail(), user.getKindMember(), new AddressResponse(user.getAddress()),
                extractUserRoles(user));
    }

    /**
     * Extracts the set of {@link UserRoleName} from the {@link UserEntity}.
     *
     * @param user The {@code UserEntity} whose roles are to be extracted.
     * @return A set of {@code UserRoleName} representing the user's roles.
     */
    private static Set<UserRoleName> extractUserRoles(final UserEntity user) {
        final Set<UserRoleName> roles = EnumSet.noneOf(UserRoleName.class);
        for (final PersistentRoleEntity role : user.getRoles()) {
            roles.add(role.getName());
        }
        return roles;
    }
}
