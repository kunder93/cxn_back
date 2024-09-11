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

/**
 * Entity representing an address in the system.
 * <p>
 * This class uses JPA annotations for mapping the entity to the database.
 * It is equipped with Lombok annotations to generate getters, setters,
 * and other boilerplate code.
 * </p>
 *
 * @author Santiago Paz Perez
 */
@Entity(name = "AddressEntity")
@Table(name = "address")
@Data
public class PersistentAddressEntity implements AddressEntity {

  /**
   * Serialization ID.
   */
  @Transient
  private static final long serialVersionUID = 1396772919450111291L;

  /**
   * The unique identifier for this address, corresponding to the user's DNI.
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
   * Address street.
   */
  @Column(name = "street", nullable = false, unique = false)
  private String street = "";

  /**
   * Address city.
   */
  @Column(name = "city", nullable = false, unique = false)
  private String city = "";

  // RELATIONS

  /**
   * The user associated with this address.
   */
  @OneToOne
  @MapsId
  @JoinColumn(name = "user_dni")
  private PersistentUserEntity user;

  /**
   * The country associated with this address.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_numeric_code")
  private PersistentCountryEntity country;

  /**
   * The country subdivision associated with this address.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_subdivision_id")
  private PersistentCountrySubdivisionEntity countrySubdivision;

  /**
   * Default constructor. This is used by JPA for entity instantiation.
   */
  public PersistentAddressEntity() {
    // Default constructor
  }

}
