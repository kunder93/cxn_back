
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

import es.org.cxn.backapp.model.CountrySubdivisionEntity;

/**
 * Represents the response form used by the controller for requesting details
 * of a country subdivision.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller by mapping the necessary fields. Each field in this
 * record corresponds to a piece of information about the country subdivision.
 * <p>
 * This record provides a concise and immutable way to model data, ensuring that
 * the data remains consistent and is not modified unintentionally.
 *
 * @param name                  the name of the country subdivision.
 * @param kindSubdivisionName   the kind of subdivision.
 * @param code                  the code of the subdivision.
 *
 * @author Santiago Paz.
 */
public record SubCountryResponse(
      String name, String kindSubdivisionName, String code
) {

  /**
   * Creates a {@code SubCountryResponse} instance from a
   * {@code PersistentCountrySubdivisionEntity}.
   * <p>
   * This static factory method is used to convert a
   * {@code PersistentCountrySubdivisionEntity} into a
   * {@code SubCountryResponse}. It extracts relevant fields from the entity
   * to populate the fields of the response record.
   *
   * @param countrySubdivision the {@code PersistentCountrySubdivisionEntity}
   * from which to create the {@code SubCountryResponse}.
   * @return a new {@code SubCountryResponse} containing data from the
   * provided entity.
   */
  public static SubCountryResponse
        fromEntity(final CountrySubdivisionEntity countrySubdivision) {
    return new SubCountryResponse(
          countrySubdivision.getName(),
          countrySubdivision.getKindSubdivisionName(),
          countrySubdivision.getCode()
    );
  }
}
