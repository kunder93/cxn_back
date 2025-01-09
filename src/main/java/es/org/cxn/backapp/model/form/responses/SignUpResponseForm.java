package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDate;
import java.util.Collections;
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
     * Canonical constructor for SignUpResponseForm.
     * <p>
     * This constructor ensures that the userRoles set is defensively copied and
     * wrapped in an unmodifiable set to guarantee immutability.
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
     */
    public SignUpResponseForm(final String dni, final String name, final String firstSurname,
            final String secondSurname, final LocalDate birthDate, final String gender, final String email,
            final UserType kindMember, final Set<UserRoleName> userRoles) {
        // Create a defensive copy of the userRoles set to avoid external modification
        this.dni = dni;
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.kindMember = kindMember;
        this.userRoles = Collections.unmodifiableSet(userRoles);
    }

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
        // Create a defensive copy of the roles to ensure immutability
        final Set<UserRoleName> userRoles = EnumSet.noneOf(UserRoleName.class);
        user.getRoles().forEach(role -> userRoles.add(role.getName()));

        // Return the new SignUpResponseForm with an unmodifiable set of user roles
        return new SignUpResponseForm(user.getDni(), user.getProfile().getName(), user.getProfile().getFirstSurname(),
                user.getProfile().getSecondSurname(), user.getProfile().getBirthDate(), user.getProfile().getGender(),
                user.getEmail(), user.getKindMember(), Collections.unmodifiableSet(userRoles));
    }

    /**
     * Returns an immutable copy of the user roles set to avoid modification of
     * internal data.
     *
     * @return An immutable copy of the user roles set.
     */
    @Override
    public Set<UserRoleName> userRoles() {
        // Return an immutable copy of the userRoles set
        return Collections.unmodifiableSet(userRoles);
    }
}
