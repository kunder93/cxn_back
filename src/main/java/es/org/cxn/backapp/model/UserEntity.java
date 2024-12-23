
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
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
     * Retrieves the user's birth date.
     *
     * @return The user's birth date.
     */
    LocalDate getBirthDate();

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
     * Retrieves the user's first surname.
     *
     * @return The user's first surname.
     */
    String getFirstSurname();

    /**
     * Retrieves the user's gender.
     *
     * @return The user's gender.
     */
    String getGender();

    /**
     * Retrieves the type of user (e.g., admin, regular).
     *
     * @return The user's type.
     */
    UserType getKindMember();

    /**
     * Retrieves the full name of the user.
     *
     * @return The user's full name.
     */
    String getName();

    /**
     * Retrieves the user's password.
     *
     * @return The user's password.
     */
    String getPassword();

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
     * Retrieves the user's second surname.
     *
     * @return The user's second surname.
     */
    String getSecondSurname();

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
     * Sets the user's birth date.
     *
     * @param birthDate The new birth date.
     */
    void setBirthDate(LocalDate birthDate);

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
     * Sets the user's first surname.
     *
     * @param firstSurname The new first surname.
     */
    void setFirstSurname(String firstSurname);

    /**
     * Sets the user's gender.
     *
     * @param gender The new gender.
     */
    void setGender(String gender);

    /**
     * Sets the type of user (e.g., admin, regular).
     *
     * @param kindMember The new user type.
     */
    void setKindMember(UserType kindMember);

    /**
     * Sets the full name of the user.
     *
     * @param name The new full name of the user.
     */
    void setName(String name);

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
     * Sets the user's second surname.
     *
     * @param secondSurname The new second surname.
     */
    void setSecondSurname(String secondSurname);

}
