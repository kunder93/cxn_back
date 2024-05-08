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

package es.org.cxn.backapp.model;

/**
 * An Address entity interface.
 *
 * @author Santiago Paz Perez
 */
public interface AddressEntity extends java.io.Serializable {
  /**
   * @return The Address user's dni.
   */
  String getUserDni();

  /**
   * @return The address postal code.
   */
  String getPostalCode();

  /**
   * @return The address apartment number.
   */
  String getApartmentNumber();

  /**
   * @return The address building.
   */
  String getBuilding();

  /**
   * @return The address street.
   */
  String getStreet();

  /**
   * @return The address city.
   */
  String getCity();

  /**
   * @return The address user entity.
   */
  UserEntity getUser();

  /**
   * @return The address country.
   */
  CountryEntity getCountry();

  /**
   * @return The address country subdivision.
   */
  CountrySubdivisionEntity getCountrySubdivision();

}
