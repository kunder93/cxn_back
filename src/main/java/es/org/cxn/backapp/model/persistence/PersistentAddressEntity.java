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

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "AddressEntity")
@Table(name = "address")
@Data
@NoArgsConstructor
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

}
