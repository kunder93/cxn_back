
package es.org.cxn.backapp.model;

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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.team.PersistentTeamEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;
import lombok.NonNull;

/**
 * Represents a user entity with various personal and account details. This
 * interface defines the methods to access and modify user information such as
 * identifiers, names, roles, and associated address.
 *
 * @author Santiago Paz Perez
 */
public interface UserEntity extends Serializable {

    /**
     * Adds a role to the user.
     *
     * @param role The role to add.
     * @return {@code true} if the role was added successfully, {@code false}
     *         otherwise.
     */
    boolean addRole(@NonNull PersistentRoleEntity role);

    /**
     * Retrieves the address entity associated with the user.
     *
     * @return The user's address entity, or {@code null} if not set.
     */
    PersistentAddressEntity getAddress();

    /**
     * Gets the complete name.
     *
     * @return The complete name, that is name, first surname and second surname.
     */
    String getCompleteName();

    /**
     * Retrieves the identifier assigned to this user entity (DNI).
     *
     * @return The user's identifier (DNI).
     */
    String getDni();

    /**
     * Retrieves the user's email address.
     *
     * @return The user's email address.
     */
    String getEmail();

    /**
     * Get associated federate state.
     *
     * @return user federate state entity.
     */
    PersistentFederateStateEntity getFederateState();

    /**
     * Retrieves the type of user (e.g., admin, regular).
     *
     * @return The user's type.
     */
    UserType getKindMember();

    /**
     * Retrieves the user's password.
     *
     * @return The user's password.
     */
    String getPassword();

    /**
     * Retrieves the user's profile information.
     *
     * <p>
     * The profile contains personal details such as the user's name, surname,
     * birthdate, and gender.
     * </p>
     *
     * @return The {@link UserProfile} associated with the user.
     */
    UserProfile getProfile();

    /**
     * User profile image.
     *
     * @return The user profile image entity.
     */
    PersistentProfileImageEntity getProfileImage();

    /**
     * Retrieves the roles assigned to the user.
     *
     * @return A set of roles assigned to the user.
     */
    Set<PersistentRoleEntity> getRoles();

    /**
     * Get the team assigned to this user.
     *
     * @return The team assigned.
     */
    PersistentTeamEntity getTeamAssigned();

    /**
     * Get the team preferred by this user.
     *
     * @return The team preferred.
     */
    PersistentTeamEntity getTeamPreferred();

    /**
     * Get the unsubscribe initial action date time.
     *
     * @return The initial unsubscribe action start date time.
     */
    LocalDateTime getUnsubscribeDate();

    /**
     * Checks if the user's account is enabled.
     *
     * @return {@code true} if the account is enabled, {@code false} otherwise.
     */
    boolean isEnabled();

    /**
     * Removes a role from the user.
     *
     * @param role The role to remove.
     * @return {@code true} if the role was removed successfully, {@code false}
     *         otherwise.
     */
    boolean removeRole(@NonNull PersistentRoleEntity role);

    /**
     * Sets the address entity associated with the user.
     *
     * @param address The new address entity.
     */
    void setAddress(PersistentAddressEntity address);

    /**
     * Sets the identifier (DNI) for this user entity.
     *
     * @param value The new identifier for the user.
     */
    void setDni(String value);

    /**
     * Sets the user's email address.
     *
     * @param email The new email address.
     */
    void setEmail(String email);

    /**
     * Enables or disables the user's account.
     *
     * @param value {@code true} to enable the account, {@code false} to disable.
     */
    void setEnabled(boolean value);

    /**
     * Set user federate state.
     *
     * @param federateState the federate state entity for associate with this user.
     */
    void setFederateState(PersistentFederateStateEntity federateState);

    /**
     * Sets the type of user (e.g., admin, regular).
     *
     * @param kindMember The new user type.
     */
    void setKindMember(UserType kindMember);

    /**
     * Sets oAuthAuthorizationRequest.
     *
     * @param savedOAuth the OAuthAuthorizationRequest entity.
     */
    void setOauthAuthorizationRequest(PersistentOAuthAuthorizationRequestEntity savedOAuth);

    /**
     * Sets the user's password.
     *
     * @param password The new password.
     */
    void setPassword(String password);

    /**
     * Sets the user's profile information.
     *
     * <p>
     * The profile should include personal details such as the user's name, surname,
     * birthdate, and gender.
     * </p>
     *
     * @param value The new {@link UserProfile} to associate with the user.
     */
    void setProfile(UserProfile value);

    /**
     * Sets the user profile image entity.
     *
     * @param profileImage The new profile image entity associated to user.
     */
    void setProfileImage(PersistentProfileImageEntity profileImage);

    /**
     * Sets the roles assigned to the user.
     *
     * @param roles The new set of roles.
     */
    void setRoles(Set<PersistentRoleEntity> roles);

    /**
     * Sets the team assigned to this user.
     *
     * @param team The team assigned.
     */
    void setTeamAssigned(PersistentTeamEntity team);

    /**
     * Sets the team preferred by this user.
     *
     * @param teamPreferred The team preferred.
     */
    void setTeamPreferred(PersistentTeamEntity teamPreferred);

    /**
     * Sets unsubscribe date with time.
     *
     * @param date The unsubscribe start action initial moment.
     */
    void setUnsubscribeDate(LocalDateTime date);

}
