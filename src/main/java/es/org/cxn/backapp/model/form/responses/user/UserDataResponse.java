
package es.org.cxn.backapp.model.form.responses.user;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.TeamEntity;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.responses.user.address.AddressResponse;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.UserType;

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
 * @param dni               The user's DNI (identification number).
 * @param name              The user's first name.
 * @param firstSurname      The user's first surname.
 * @param secondSurname     The user's second surname.
 * @param gender            The user's gender.
 * @param birthDate         The user's birth date.
 * @param email             The user's email address.
 * @param kindMember        The type of membership the user holds, represented
 *                          by {@link UserType}.
 * @param userAddress       The user's address, represented by
 *                          {@link AddressResponse}.
 * @param userRoles         The set of role names associated with the user,
 *                          represented by {@link UserRoleName}.
 * @param assignedTeamName  The team name assigned to this user. Can be null.
 * @param preferredTeamName The team name that user preferred.
 * @param federateState     The user federate state.
 * @param unsubscribeDate   Unsubscribe date when user initiate process. Date
 *                          with no time.
 *
 * @author Santiago Paz Perez
 */
public record UserDataResponse(String dni, String name, String firstSurname, String secondSurname, String gender,
        LocalDate birthDate, String email, UserType kindMember, AddressResponse userAddress,
        Set<UserRoleName> userRoles, String assignedTeamName, String preferredTeamName, FederateState federateState,
        String unsubscribeDate) {

    /**
     * Formatter for dates to the pattern "dd/MM/yyyy".
     * <p>
     * Used to format dates consistently when converting {@link LocalDate} values to
     * strings.
     * </p>
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /**
     * Constructs a {@code UserDataResponse} record from all parameters.
     * <p>
     * This constructor initializes all the fields of the record based on the
     * provided values for each of the parameters.
     * </p>
     *
     * @param dni               The user's DNI (identification number).
     * @param name              The user's first name.
     * @param firstSurname      The user's first surname.
     * @param secondSurname     The user's second surname.
     * @param gender            The user's gender.
     * @param birthDate         The user's birth date.
     * @param email             The user's email address.
     * @param kindMember        The type of membership the user holds, represented
     *                          by {@link UserType}.
     * @param userAddress       The user's address, represented by
     *                          {@link AddressResponse}.
     * @param userRoles         The set of role names associated with the user,
     *                          represented by {@link UserRoleName}.
     * @param assignedTeamName  The team name assigned to this user. Can be null.
     * @param preferredTeamName The team name that user preferred.
     * @param federateState     The federate state.
     * @param unsubscribeDate   Unsubscribe date when user initiate process. Date
     *                          with no time.
     */
    public UserDataResponse(final String dni, final String name, final String firstSurname, final String secondSurname,
            final String gender, final LocalDate birthDate, final String email, final UserType kindMember,
            final AddressResponse userAddress, final Set<UserRoleName> userRoles, final String assignedTeamName,
            final String preferredTeamName, final FederateState federateState, String unsubscribeDate) {
        this.dni = dni;
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.kindMember = kindMember;
        this.userAddress = userAddress;
        this.userRoles = EnumSet.copyOf(userRoles);
        this.assignedTeamName = assignedTeamName;
        this.preferredTeamName = preferredTeamName;
        this.federateState = federateState;
        this.unsubscribeDate = unsubscribeDate;
    }

    /**
     * Constructs a {@code UserDataResponse} from a given {@link UserEntity}.
     * <p>
     * This constructor initializes all fields based on the values present in the
     * provided {@code UserEntity}. It extracts the user's roles, assigned team, and
     * preferred team, converting the team references to their respective names.
     * </p>
     *
     * @param user The {@code UserEntity} from which to create the response.
     */
    public UserDataResponse(final UserEntity user) {
        this(user.getDni(), user.getProfile().getName(), user.getProfile().getFirstSurname(),
                user.getProfile().getSecondSurname(), user.getProfile().getGender(), user.getProfile().getBirthDate(),
                user.getEmail(), user.getKindMember(), new AddressResponse(user.getAddress()), extractUserRoles(user),
                Optional.ofNullable(user.getTeamAssigned()).map(TeamEntity::getName).orElse(null),
                Optional.ofNullable(user.getTeamPreferred()).map(TeamEntity::getName).orElse(null),
                user.getFederateState().getState(), Optional.ofNullable(user.getUnsubscribeDate())
                        .map(dateTime -> dateTime.toLocalDate().format(DATE_FORMATTER)).orElse(null));
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

    /**
     * Returns a new {@link Set} containing the user's roles.
     * <p>
     * This method creates a new {@code HashSet} of the user's roles, ensuring that
     * the set can be modified independently from the original {@code userRoles}.
     * </p>
     *
     * @return A new {@code Set<UserRoleName>} containing the user's roles.
     */
    public Set<UserRoleName> userRoles() {
        return EnumSet.copyOf(userRoles);
    }

}
