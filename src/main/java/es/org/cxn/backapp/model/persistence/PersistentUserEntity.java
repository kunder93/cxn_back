/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.model.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * User Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "UserEntity")
@Table(name = "users")
@AllArgsConstructor
@Data
@Builder
public class PersistentUserEntity implements UserEntity {

    /**
     * kind of member that users can be.
     *
     * @author Santi
     *
     */
    public enum UserType {
        /**
         * Socio numerario, cuota de 30, mayor de 18 independiente economicamente.
         */
        SOCIO_NUMERO,
        /**
         * Socio aspirante, menor de 18, sin voto en junta.
         */
        SOCIO_ASPIRANTE,
        /**
         * Socio honorario, cuota de 0, sin voto en junta.
         */
        SOCIO_HONORARIO,
        /**
         * Depende economicamente de socio de numero, cuota 0, sin voto en junta.
         */
        SOCIO_FAMILIAR
    }

    /**
     * Serialization ID.
     */
    @Transient
    private static final long serialVersionUID = 1328773339450853291L;

    /**
     * Entity's dni aka Identifier.
     */
    @Id
    @Column(name = "dni", nullable = false, unique = true)
    @NonNull
    private String dni;

    /**
     * Name of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "name", nullable = false, unique = false)
    @NonNull
    private String name;

    /**
     * First surname of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "first_surname", nullable = false, unique = false)
    @NonNull
    private String firstSurname;

    /**
     * Second surname of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "second_surname", nullable = false, unique = false)
    @NonNull
    private String secondSurname;

    /**
     * Birth date of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "birth_date", nullable = false, unique = false)
    @NonNull
    private LocalDate birthDate;

    /**
     * Gender of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "gender", nullable = false, unique = false)
    @NonNull
    private String gender;

    /**
     * Password of the user.
     *
     */
    @Column(name = "password", nullable = false, unique = false)
    @NonNull
    private String password;

    /**
     * Email of the user.
     *
     */
    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    private String email;

    /**
     * Kind of user member.
     *
     */
    @Column(name = "kind_member", nullable = false, unique = false)
    @Builder.Default
    @NonNull
    @Enumerated(EnumType.STRING)
    private UserType kindMember = UserType.SOCIO_NUMERO;

    /**
     * User status boolean, enabled or disiabled, true or false.
     */
    @Column(name = "enabled", nullable = false, unique = false)
    @Builder.Default
    private boolean enabled = Boolean.TRUE;

    /**
     * Roles associated with this user.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    // @formatter:off
    @JoinTable(name = "role_users", joinColumns = @JoinColumn(name = "user_dni"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<PersistentRoleEntity> roles = new HashSet<>();

    /**
     * The payment sheet user owner.
     */
    @OneToMany(mappedBy = "userOwner")
    @Builder.Default
    private List<PersistentPaymentSheetEntity> paymentSheets = new ArrayList<>();

    /**
     * The user address.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private PersistentAddressEntity address;

    /**
     * The user lichess auth for lichess API usage.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PersistentLichessAuthEntity lichessAuth;

    /**
     * The user OAuth authorization request for OAuth API usage.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PersistentOAuthAuthorizationRequestEntity oauthAuthorizationRequest;


    /**
     * The associated profile image entity. This establishes a one-to-one
     * relationship between the user and their profile image.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PersistentProfileImageEntity profileImage;

    /**
     * The associated profile image entity. This establishes a one-to-one
     * relationship between the user and their profile image.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private PersistentFederateStateEntity federateState;

    /**
     * No-args constructor. If use remember change values of federateState cause it needs valid user dni.
     */
    public PersistentUserEntity() {
        this.federateState = new PersistentFederateStateEntity(
                "", // Initial value for dni (can be set later)
                "", // Initial value for dniFrontImageUrl
                "", // Initial value for dniBackImageUrl
                false, // Initial value for automaticRenewal
                LocalDate.now(), // Set dniLastUpdate to current date
                FederateState.NO_FEDERATE // Default state
            );
    }

    /**
     * Custom constructor for Lombok's {@code @Builder} to initialize a user entity.
     *
     * <p>This constructor ensures that the {@code federateState} is initialized based on the provided
     * {@code dni}. If the {@code dni} is non-null and non-empty, a new {@link PersistentFederateStateEntity}
     * is created with initial values.</p>
     *
     * @param dni             the user's DNI (identification number). Must not be null or empty if
     *                        {@code federateState} is to be initialized.
     * @param name            the user's name. Must not be null.
     * @param firstSurname    the user's first surname. Must not be null.
     * @param secondSurname   the user's second surname. Must not be null.
     * @param birthDate       the user's birth date. Must not be null.
     * @param gender          the user's gender. Must not be null.
     * @param password        the user's password. Must not be null.
     * @param email           the user's email. Must not be null.
     * @param kindMember      the user's membership type. Can be null; defaults to {@code UserType.SOCIO_NUMERO}.
     * @param enabled         indicates whether the user account is enabled. Must not be null.
     * @param rolesEntity           a set of roles associated with the user. Can be null; defaults to an empty set.
     */
    @Builder
    public PersistentUserEntity(final String dni, final String name, final String firstSurname,
            final String secondSurname, final LocalDate birthDate, final String gender, final String password,
            final String email, final UserType kindMember, final boolean enabled,
            final Set<PersistentRoleEntity> rolesEntity) {
        this.dni = dni;
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.password = password;
        this.email = email;
        this.kindMember = kindMember != null ? kindMember : UserType.SOCIO_NUMERO;
        this.enabled = enabled;
        this.roles = rolesEntity != null ? rolesEntity : new HashSet<>();

        // Now dni is already assigned, initialize federateState
        if (dni != null && !dni.isEmpty()) {
            this.federateState = new PersistentFederateStateEntity(
                this.dni,
                "", // Initial value for dniFrontImageUrl
                "", // Initial value for dniBackImageUrl
                false, // Initial value for automaticRenewal
                LocalDate.now(), // Set dniLastUpdate to current date
                FederateState.NO_FEDERATE
            );
        }
    }


    /**
     * Add new role.
     */
    @Override
    public boolean addRole(@NonNull final PersistentRoleEntity role) {
        final var result = this.roles.add(role);
        role.getUsers().add(this);
        return result;
    }

    /**
     * Equals with dni and email field.
     */
    @Override
    public boolean equals(final Object obj) {
        final boolean isEqual;

        if (this == obj) {
            isEqual = true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            isEqual = false;
        } else {
            final var other = (PersistentUserEntity) obj;
            isEqual = Objects.equals(dni, other.dni) && Objects.equals(email, other.email);
        }

        return isEqual;
    }

    /**
     * Generate complete name with user name, first name and second surname.
     *
     * @return The complete user name, name, first surname and second surname.
     */
    public String getCompleteName() {
        return (name + " " +  firstSurname +  " " + secondSurname);
    }
    /**
     * Hash code with dni and email fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dni, email);
    }

    /**
     * Remove role.
     */
    @Override
    public boolean removeRole(@NonNull final PersistentRoleEntity role) {
        final var result = this.roles.remove(role);
        role.getUsers().remove(this);
        return result;
    }

    /**
     * Sets the initial federate state for the current entity based on the provided DNI.
     */
    public void setInitialFederateState() {
        if (this.dni != null && !this.dni.isEmpty()) {
            this.federateState = new PersistentFederateStateEntity(
                this.dni,
                "", // Initial value for dniFrontImageUrl
                "", // Initial value for dniBackImageUrl
                false, // Initial value for automaticRenewal
                LocalDate.now(), // Set dniLastUpdate to current date
                FederateState.NO_FEDERATE
            );
        }
    }
}
