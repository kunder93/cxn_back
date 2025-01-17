
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

/**
 * Represents an Address entity with various details about a user's address.
 * This includes the user's DNI, postal code, apartment number, building,
 * street, city, user entity, country, and country subdivision.
 * <p>
 * It provides methods to access these fields.
 * </p>
 *
 * @author Santiago Paz Perez
 */
public interface AddressEntity extends java.io.Serializable {

  /**
   * Gets the user's DNI associated with the address.
   *
   * @return The Address user's DNI.
   */
  String getUserDni();

  /**
   * Gets the postal code of the address.
   *
   * @return The address postal code.
   */
  String getPostalCode();

  /**
   * Gets the apartment number of the address.
   *
   * @return The address apartment number.
   */
  String getApartmentNumber();

  /**
   * Gets the building information of the address.
   *
   * @return The address building.
   */
  String getBuilding();

  /**
   * Gets the street name of the address.
   *
   * @return The address street.
   */
  String getStreet();

  /**
   * Gets the city where the address is located.
   *
   * @return The address city.
   */
  String getCity();

  /**
   * Gets the user entity associated with the address.
   *
   * @return The address user entity.
   */
  UserEntity getUser();

  /**
   * Gets the country associated with the address.
   *
   * @return The address country.
   */
  CountryEntity getCountry();

  /**
   * Gets the country subdivision (state, province, etc.) associated with the
   * address.
   *
   * @return The address country subdivision.
   */
  CountrySubdivisionEntity getCountrySubdivision();

}
