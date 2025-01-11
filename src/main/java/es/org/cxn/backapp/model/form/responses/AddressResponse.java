
package es.org.cxn.backapp.model.form.responses;

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

import es.org.cxn.backapp.model.AddressEntity;

/**
 * A Data Transfer Object (DTO) used by controllers to provide a response
 * containing address details. This record is intended for communication
 * between the view layer and the controller, encapsulating all the necessary
 * address-related data.
 *
 * <p>
 * The {@code AddressResponse} is designed to be immutable, ensuring that the
 * data it carries remains consistent throughout its lifecycle.
 * It includes fields such as postal code, apartment number, building,
 * street, city, country name, and sub-country name.
 * </p>
 *
 * <p>
 * This record can be constructed directly with the necessary data or by passing
 * an {@link AddressEntity} object, from which it extracts and maps the required
 * data.
 * </p>
 *
 * @param postalCode      The postal code of the address.
 * @param apartmentNumber The apartment number within the building.
 * @param building        The name or number of the building.
 * @param street          The street name where the address is located.
 * @param city            The city where the address is located.
 * @param countryName     The full name of the country.
 * @param subCountryName  The name of the subdivision within the country
 * (e.g., state or province).
 *
 * @author Santiago Paz
 */
public record AddressResponse(
      String postalCode, String apartmentNumber, String building, String street,
      String city, String countryName, String subCountryName
) {

  /**
   * Constructs an {@code AddressResponse} by extracting and mapping data from
   * the provided {@link AddressEntity}.
   *
   * @param address The address entity containing the data to be represented
   *                in the response.
   *
   * @see AddressEntity
   */
  public AddressResponse(final AddressEntity address) {
    this(
          address.getPostalCode(), address.getApartmentNumber(),
          address.getBuilding(), address.getStreet(), address.getCity(),
          address.getCountry().getFullName(),
          address.getCountrySubdivision().getName()
    );
  }
}
