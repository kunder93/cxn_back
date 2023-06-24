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
   * Serialization ID.
   */
  @Transient
  private static final long serialVersionUID = 1328773339450853291L;

  /**
   * Entity's dni aka Identifier.
   */
  @Id
  @Column(name = "dni", nullable = false, unique = true)
  private String dni = "";

  /**
   * Name of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "name", nullable = false, unique = false)
  private String name = "";

  /**
   * First surname of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "first_surname", nullable = false, unique = false)
  private String firstSurname = "";

  /**
   * Second surname of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "second_surname", nullable = false, unique = false)
  private String secondSurname = "";

  /**
   * Birth date of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Temporal(TemporalType.DATE)
  @Column(name = "birth_date", nullable = false, unique = false)
  private LocalDate birthDate;

  /**
   * Gender of the user.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "gender", nullable = false, unique = false)
  private String gender = "";

  /**
   * Password of the user.
   *
   */
  @Column(name = "password", nullable = false, unique = false)
  private String password = "";

  /**
   * Email of the user.
   *
   */
  @Column(name = "email", nullable = false, unique = true)
  private String email = "";

  /**
   * Roles associated with this user.
   */
  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
  @JoinTable(
        name = "role_users", joinColumns = @JoinColumn(name = "user_dni"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<PersistentRoleEntity> roles = new HashSet<>();

  /**
   * The payment sheet user owner.
   */
  @OneToMany(mappedBy = "userOwner")
  private List<PersistentPaymentSheetEntity> paymentSheets = new ArrayList<>();

  /**
   * The user address.
   */
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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
   */
  public PersistentUserEntity(
        final String dniValue, final String nameValue,
        final String firstSurnameValue, final String secondSurnameValue,
        final LocalDate birthDateValue, final String genderValue,
        final String passwordValue, final String emailValue,
        final PersistentAddressEntity address
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
  }

  /**
   * Returns the dni aka identifier assigned to this user entity.
   *
   * @return the user's identifier
   */
  @Override
  public String getDni() {
    return dni;
  }

  /**
   * Get address entity.
   */
  @Override
  public PersistentAddressEntity getAddress() {
    return address;
  }

  /**
   * Set address entity.
   */
  @Override
  public void setAddress(final PersistentAddressEntity address) {
    this.address = address;
  }

  /**
   * Get user name.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Get user first surname.
   */
  @Override
  public String getFirstSurname() {
    return firstSurname;
  }

  /**
   * Get user second surname.
   */
  @Override
  public String getSecondSurname() {
    return secondSurname;
  }

  /**
   * Get user birth date.
   */
  @Override
  public LocalDate getBirthDate() {
    return birthDate;
  }

  /**
   * Get user gender.
   */
  @Override
  public String getGender() {
    return gender;
  }

  /**
   * Get user password.
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Get user email.
   */
  @Override
  public String getEmail() {
    return email;
  }

  /**
   * Get user set of roles.
   */
  @Override
  public Set<PersistentRoleEntity> getRoles() {
    return new HashSet<>(roles);
  }

  /**
   * @return The payment sheet list.
   */
  public List<PersistentPaymentSheetEntity> getPaymentSheets() {
    return new ArrayList<>(paymentSheets);
  }

  /**
   * @param paymentSheets The payment sheet list.
   */
  public void setPaymentSheets(
        final List<PersistentPaymentSheetEntity> paymentSheets
  ) {
    this.paymentSheets = new ArrayList<>(paymentSheets);
  }

  /**
   * Set user id.
   */
  @Override
  public void setDni(final String value) {
    dni = checkNotNull(value, "Received a null pointer as id");
  }

  /**
   * Set user name.
   */
  @Override
  public void setName(final String value) {
    this.name = checkNotNull(value, "Received a null pointer as name");
  }

  /**
   * Set first surname.
   */
  @Override
  public void setFirstSurname(final String value) {
    this.firstSurname =
          checkNotNull(value, "Received a null pointer as first surname");
  }

  /**
   * Set second surname.
   */
  @Override
  public void setSecondSurname(final String value) {
    this.secondSurname =
          checkNotNull(value, "Received a null pointer as second surname");
  }

  /**
   * Set birth date.
   */
  @Override
  public void setBirthDate(final LocalDate value) {
    this.birthDate =
          checkNotNull(value, "Received a null pointer as birth date");
  }

  /**
   * Set gender.
   */
  @Override
  public void setGender(final String value) {
    this.gender = checkNotNull(value, "Received a null pointer as gender");
  }

  /**
   * Set password.
   */
  @Override
  public void setPassword(final String value) {
    this.password = checkNotNull(value, "Received a null pointer as password");

  }

  /**
   * Set email.
   */
  @Override
  public void setEmail(final String value) {
    this.email = checkNotNull(value, "Received a null pointer as email");

  }

  /**
   * Put a Set of Roles.
   */
  @Override
  public void setRoles(final Set<PersistentRoleEntity> roles) {
    this.roles = new HashSet<>(roles);
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
          secondSurname
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
          && Objects.equals(secondSurname, other.secondSurname);
  }

  /**
   * To string method.
   */
  @Override
  public String toString() {
    return "PersistentUserEntity [dni=" + dni + ", name=" + name
          + ", first_surname=" + firstSurname + ", second_surname="
          + secondSurname + ", birth_date=" + birthDate + ", gender=" + gender
          + ", password=" + password + ", email=" + email + "]";
  }

}
