
package es.org.cxn.backapp.model.form.responses.user.address;

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

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * Represents the response DTO used by the controller for requesting one
 * country data.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller when requesting
 * country data.
 * </p>
 *
 * @param shortName   The country short name.
 * @param fullName    The country full name.
 * @param numericCode The country numeric code.
 * @param alpha2Code  The country alpha 2 code.
 * @param alpha3Code  The country alpha 3 code.
 * @author Santiago Paz
 */
public record CountryResponse(
      String shortName, String fullName, Integer numericCode, String alpha2Code,
      String alpha3Code
) {

  /**
   * Constructs a {@link CountryResponse} from a
   * {@link PersistentCountryEntity}.
   *
   * @param value The country entity to extract values from.
   */
  public CountryResponse(final PersistentCountryEntity value) {
    this(
          value.getShortName(), value.getFullName(), value.getNumericCode(),
          value.getAlpha2Code(), value.getAlpha3Code()
    );
  }
}
