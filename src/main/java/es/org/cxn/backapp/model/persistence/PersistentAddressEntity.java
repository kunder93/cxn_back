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

import es.org.cxn.backapp.model.AddressEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Objects;

/**
 * User Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "AddressEntity")
@Table(name = "address")
public class PersistentAddressEntity implements AddressEntity {

  /**
   * Serialization ID.
   */
  @Transient
  private static final long serialVersionUID = 1396772919450111291L;

  /**
   * The user dni. Its unique. Works as identifier.
   */
  @Id
  private String userDni;

  /**
   * Address postal code.
   */
  @Column(name = "postal_code", nullable = false, unique = false)
  private String postalCode = "";

  /**
   * Address apartment or number.
   */
  @Column(name = "apartment_number", nullable = false, unique = false)
  private String apartmentNumber = "";

  /**
   * Address building.
   */
  @Column(name = "building", nullable = false, unique = false)
  private String building = "";

  /**
   * Address apartment or number.
   */
  @Column(name = "street", nullable = false, unique = false)
  private String street = "";

  /**
   * Address apartment or number.
   */
  @Column(name = "city", nullable = false, unique = false)
  private String city = "";

  // RELATIONS
  /**
   * The user who have this address.
   */
  @OneToOne
  @MapsId
  @JoinColumn(name = "user_dni")
  private PersistentUserEntity user;

  /**
   * The address have one country.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_numeric_code")
  private PersistentCountryEntity country;

  /**
   * The address have one country subdivision.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_subdivision_id")
  private PersistentCountrySubdivisionEntity countrySubdivision;

  /**
   * Constructs an address entity.
   */
  public PersistentAddressEntity() {
    super();
  }

  /**
   * Get the user DNI.
   *
   * @return The user DNI.
   */
  public String getUserDni() {
    return userDni;
  }

  /**
   * Set the address user DNI.
   *
   * @param userDni The user DNI.
   */
  public void setUserDni(final String userDni) {
    this.userDni = userDni;
  }

  /**
   * Get the address postal code.
   *
   * @return The postal code.
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Get the address apartment number.
   *
   * @return The apartment number.
   */
  public String getApartmentNumber() {
    return apartmentNumber;
  }

  /**
   * Get the address building.
   *
   * @return The building.
   */
  public String getBuilding() {
    return building;
  }

  /**
   * Get the address street.
   *
   * @return The street.
   */
  public String getStreet() {
    return street;
  }

  /**
   * Get the address city.
   *
   * @return The city.
   */
  public String getCity() {
    return city;
  }

  /**
   * Get the user who have this address.
   *
   * @return The user.
   */
  public PersistentUserEntity getUser() {
    return user;
  }

  /**
   * Get the address country.
   *
   * @return The country.
   */
  public PersistentCountryEntity getCountry() {
    return country;
  }

  /**
   * Get the address country subdivision.
   *
   * @return The address country subdivision.
   */
  public PersistentCountrySubdivisionEntity getCountrySubdivision() {
    return countrySubdivision;
  }

  /**
   * Get the address postal code.
   *
   * @param postalCode The postal code.
   */
  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Set the address apartment number.
   *
   * @param apartmentNumber The apartment number.
   */
  public void setApartmentNumber(final String apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }

  /**
   * Set the address building.
   *
   * @param building The building.
   */
  public void setBuilding(final String building) {
    this.building = building;
  }

  /**
   * Set address street.
   *
   * @param street The street.
   */
  public void setStreet(final String street) {
    this.street = street;
  }

  /**
   * Set the address city.
   *
   * @param city The city.
   */
  public void setCity(final String city) {
    this.city = city;
  }

  /**
   * Set the address user.
   *
   * @param user The user.
   */
  public void setUser(final PersistentUserEntity user) {
    this.user = user;
  }

  /**
   * Set the address country.
   *
   * @param country The country.
   */
  public void setCountry(final PersistentCountryEntity country) {
    this.country = country;
  }

  /**
   * Set the address country subdivision.
   *
   * @param countrySubdivision The country subdivision.
   */
  public void setCountrySubdivision(
        final PersistentCountrySubdivisionEntity countrySubdivision
  ) {
    this.countrySubdivision = countrySubdivision;
  }

  /**
   * The hash code.
   */
  @Override
  public int hashCode() {
    return Objects
          .hash(apartmentNumber, building, city, postalCode, street, userDni);
  }

  /**
   * The equals.
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
    var other = (PersistentAddressEntity) obj;
    return Objects.equals(apartmentNumber, other.apartmentNumber)
          && Objects.equals(building, other.building)
          && Objects.equals(city, other.city)
          && Objects.equals(postalCode, other.postalCode)
          && Objects.equals(street, other.street)
          && Objects.equals(userDni, other.userDni);
  }

  /**
   * The to string.
   */
  @Override
  public String toString() {
    return "PersistentAddressEntity [userDni=" + userDni + ", postalCode="
          + postalCode + ", apartmentNumber=" + apartmentNumber + ", building="
          + building + ", street=" + street + ", city=" + city + "]";
  }

}
