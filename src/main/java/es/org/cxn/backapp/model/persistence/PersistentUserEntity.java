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

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.model.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * User Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "UserEntity")
@Table(name = "users")
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
  @Getter
  @Setter
  private String dni = "";

  /**
   * Name of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "name", nullable = false, unique = false)
  @Getter
  @Setter
  private String name = "";

  /**
   * First surname of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "first_surname", nullable = false, unique = false)
  @Getter
  @Setter
  private String firstSurname = "";

  /**
   * Second surname of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "second_surname", nullable = false, unique = false)
  @Getter
  @Setter
  private String secondSurname = "";

  /**
   * Birth date of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Temporal(TemporalType.DATE)
  @Column(name = "birth_date", nullable = false, unique = false)
  @Getter
  @Setter
  private LocalDate birthDate;

  /**
   * Gender of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "gender", nullable = false, unique = false)
  @Getter
  @Setter
  private String gender = "";

  /**
   * Password of the user.
   *
   */
  @Column(name = "password", nullable = false, unique = false)
  @Getter
  @Setter
  private String password = "";

  /**
   * Email of the user.
   *
   */
  @Column(name = "email", nullable = false, unique = true)
  @Getter
  @Setter
  private String email = "";

  /**
   * Kind of user member.
   *
   */
  @Column(name = "kind_member", nullable = false, unique = false)
  @Getter
  @Setter
  private UserType kindMember = UserType.SOCIO_NUMERO;

  /**
   * Roles associated with this user.
   */
  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
  @JoinTable(
        name = "role_users", joinColumns = @JoinColumn(name = "user_dni"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @Getter
  @Setter
  private Set<PersistentRoleEntity> roles = new HashSet<>();

  /**
   * The payment sheet user owner.
   */
  @OneToMany(mappedBy = "userOwner")
  @Getter
  @Setter
  private List<PersistentPaymentSheetEntity> paymentSheets = new ArrayList<>();

  /**
   * The user address.
   */
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Getter
  @Setter
  private PersistentAddressEntity address;

  /**
   * Constructs an example entity.
   */
  public PersistentUserEntity() {
    super();
  }

  /**
   * Constructs an example entity with provided params.
   *
   * @param dniValue           the user dni.
   * @param nameValue          the user name.
   * @param firstSurnameValue  the user first surname.
   * @param secondSurnameValue the user second surname.
   * @param birthDateValue     the user birth date.
   * @param genderValue        the user gender.
   * @param passwordValue      the user password.
   * @param emailValue         the user email.
   * @param address            the user address.
   * @param kindMember         the user kind of member.
   */
  public PersistentUserEntity(
        final String dniValue, final String nameValue,
        final String firstSurnameValue, final String secondSurnameValue,
        final LocalDate birthDateValue, final String genderValue,
        final String passwordValue, final String emailValue,
        final PersistentAddressEntity address, final UserType kindMember
  ) {
    super();

    this.dni = checkNotNull(dniValue, "Received a null pointer as dni");
    this.name = checkNotNull(nameValue, "Received a null pointer as name");
    this.firstSurname = checkNotNull(
          firstSurnameValue, "Received a null pointer as first surname"
    );
    this.secondSurname = checkNotNull(
          secondSurnameValue, "Received a null pointer as second surname"
    );
    this.birthDate =
          checkNotNull(birthDateValue, "Received a null pointer as birth date");
    this.gender =
          checkNotNull(genderValue, "Received a null pointer as gender");
    this.password =
          checkNotNull(passwordValue, "Received a null pointer as password");
    this.email = checkNotNull(emailValue, "Received a null pointer as email");
    this.address = address;
    this.kindMember = kindMember;
  }

  /**
   * Add new role.
   */
  @Override
  public boolean addRole(final PersistentRoleEntity role) {
    final var result = this.roles.add(role);
    role.getUsers().add(this);
    return result;
  }

  /**
   * Remove role.
   */
  @Override
  public boolean removeRole(final PersistentRoleEntity role) {
    final var result = this.roles.remove(role);
    role.getUsers().remove(this);
    return result;
  }

  /**
   * Hash code method.
   */
  @Override
  public int hashCode() {
    return Objects.hash(
          birthDate, email, firstSurname, gender, dni, name, password,
          secondSurname, kindMember
    );
  }

  /**
   * Equals method.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PersistentUserEntity) obj;
    return Objects.equals(birthDate, other.birthDate)
          && Objects.equals(email, other.email)
          && Objects.equals(firstSurname, other.firstSurname)
          && Objects.equals(gender, other.gender)
          && Objects.equals(dni, other.dni) && Objects.equals(name, other.name)
          && Objects.equals(password, other.password)
          && Objects.equals(secondSurname, other.secondSurname)
          && Objects.equals(kindMember, other.kindMember);
  }

  /**
   * To string method.
   */
  @Override
  public String toString() {
    return "PersistentUserEntity [dni=" + dni + ", name=" + name
          + ", first_surname=" + firstSurname + ", second_surname="
          + secondSurname + ", birth_date=" + birthDate + ", gender=" + gender
          + ", password=" + password + ", kindMember=" + kindMember + ", email="
          + email + "]";
  }

}
