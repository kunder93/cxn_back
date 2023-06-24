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

import es.org.cxn.backapp.model.CountryEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Country Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "CountryEntity")
@Table(name = "country")
public class PersistentCountryEntity implements CountryEntity {

  /**
   * Serialization ID.
   */
  @Transient
  private static final long serialVersionUID = 1419348811150111291L;

  /**
   * Numeric code use as Country PK.
   */
  @Id
  @Column(name = "numeric_code", nullable = false, unique = true)
  private Integer numericCode;

  /**
   * Country full name.
   */
  @Column(name = "full_name", nullable = false, unique = false)
  private String fullName = "";

  /**
   * Country short name.
   */
  @Column(name = "short_name", nullable = false, unique = false)
  private String shortName = "";

  /**
   * Country 2 characters code.
   */
  @Column(name = "alpha_2_code", nullable = false, unique = false)
  private String alpha2Code = "";

  /**
   * Country 3 characters code.
   */
  @Column(name = "alpha_3_code", nullable = false, unique = false)
  private String alpha3Code = "";

  /**
   * Country subdivisions.
   */
  @OneToMany(mappedBy = "country")
  private List<PersistentCountrySubdivisionEntity> subdivisions =
        new ArrayList<>();

  /**
   * List of addresses with this country.
   */
  @OneToMany(mappedBy = "country")
  private List<PersistentAddressEntity> addressList = new ArrayList<>();

  /**
   * Constructs a country entity.
   */
  public PersistentCountryEntity() {
    super();
  }

  /**
   * Get country numeric code.
   *
   * @return The numeric code.
   */
  public Integer getNumericCode() {
    return numericCode;
  }

  /**
   * Get country full name.
   *
   * @return The full name.
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Get country short name.
   *
   * @return The short name.
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Get country alpha-2 code.
   *
   * @return The alhpa-2 code.
   */
  public String getAlpha2Code() {
    return alpha2Code;
  }

  /**
   * Get country alpha-3 code.
   *
   * @return The alpha-3 code.
   */
  public String getAlpha3Code() {
    return alpha3Code;
  }

  /**
   * Get country subdivisions list.
   *
   * @return The country subdivisions list.
   */
  public List<PersistentCountrySubdivisionEntity> getSubdivisions() {
    return new ArrayList<>(subdivisions);
  }

  /**
   * Get country address list.
   *
   * @return The address list.
   */
  public List<PersistentAddressEntity> getAddressList() {
    return new ArrayList<>(addressList);
  }

  /**
   * Set country numeric code.
   *
   * @param numericCode The numeric code.
   */
  public void setNumericCode(final Integer numericCode) {
    this.numericCode = numericCode;
  }

  /**
   * Set country full name.
   *
   * @param fullName The full name.
   */
  public void setFullName(final String fullName) {
    this.fullName = fullName;
  }

  /**
   * Set country short name.
   *
   * @param shortName The short name.
   */
  public void setShortName(final String shortName) {
    this.shortName = shortName;
  }

  /**
   * Set country alpha-2 code.
   *
   * @param alpha2Code The alpha2 code.
   */
  public void setAlpha2Code(final String alpha2Code) {
    this.alpha2Code = alpha2Code;
  }

  /**
   * Set country alpha-3 code.
   *
   * @param alpha3Code The alpha-3 code.
   */
  public void setAlpha3Code(final String alpha3Code) {
    this.alpha3Code = alpha3Code;
  }

  /**
   * Set country subdivisions list.
   *
   * @param subdivisions The country subdivisions list.
   */
  public void setSubdivisions(
        final List<PersistentCountrySubdivisionEntity> subdivisions
  ) {
    this.subdivisions = new ArrayList<>(subdivisions);
  }

  /**
   * Set country address list.
   *
   * @param addressList The address list.
   */
  public void setAddressList(final List<PersistentAddressEntity> addressList) {
    this.addressList = new ArrayList<>(addressList);
  }

}
